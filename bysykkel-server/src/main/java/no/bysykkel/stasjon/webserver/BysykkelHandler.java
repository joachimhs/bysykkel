package no.bysykkel.stasjon.webserver;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.bysykkel.stasjon.model.Station;
import no.bysykkel.stasjon.model.StationAccess;
import no.bysykkel.stasjon.util.BysykkelJsonUtil;
import no.haagensoftware.hyrrokkin.serializer.RestSerializer;
import org.apache.log4j.Logger;
import spark.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This handler will read data from urbansharing every 10 minutes (if there is a request). The data between the two
 * APIs are gathered and combined into a single API to the webapplication.
 */
public class BysykkelHandler {
  private static final Logger logger = Logger.getLogger(WebServer.class.getName());

  private static List<Station> bysykkelStations;
  private static long bysykkelStationsLastFetched = -1;

  /**
   * This method is called from the WebServer to get the data for the Bysykkel stations.
   * @param req
   * @param res
   * @return
   * @throws IOException
   */
  public static JsonElement getBysykkelStasjoner(Request req, spark.Response res) throws IOException {
    List<Station> stations = fetchBysykkelStations();

    /*if (stations == null || stations.size() == 0) {
      JsonObject emptyStations = new JsonObject();
      emptyStations.add("stations", new JsonArray());
      return emptyStations;
    }*/

    RestSerializer serializer = new RestSerializer();
    JsonElement je = serializer.serialize(stations, new ArrayList<>(), false);

    return je;
  }


  /**
   * Fetch and cache for 10 minutes the response from the urbansharing API. The data is stored in a temporary map
   * to make combining the data via the station_id a fast operation.
   *
   * If the data is stale, it is fetched and combined again. If the data is fresh, the data is simply returned as-is.
   *
   * @return List of Bysykkel stations with availability
   * @throws IOException
   */
  private static List<Station> fetchBysykkelStations() throws IOException {
    //Simple caching of data every 10 minutes
    if (bysykkelStations == null || System.currentTimeMillis() - (10 * 60 * 60 * 1000) > bysykkelStationsLastFetched) {
      bysykkelStationsLastFetched = System.currentTimeMillis();

      //Map for å sammenstille stasjoner og tilgjengelighet
      Map<Integer, Station> stationMap = new HashMap<>();

      JsonObject stasjoner = null;
      JsonObject tilgjengelighet = null;
      //try {
        stasjoner = BysykkelJsonUtil.readJsonFromUrl("https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json");
        tilgjengelighet = BysykkelJsonUtil.readJsonFromUrl("https://gbfs.urbansharing.com/oslobysykkel.no/station_status.json");

        if (stasjoner != null
          && stasjoner.has("data")
          && stasjoner.getAsJsonObject("data").has("stations")) {

          logger.info("FETCHING STATIONS");

          bysykkelStations = new ArrayList<>();
          JsonArray stasjonerArray = stasjoner.getAsJsonObject("data").getAsJsonArray("stations");
          for (int index = 0; index < stasjonerArray.size(); index++) {
            //convert from station_id to just id
            JsonObject stationObject = stasjonerArray.get(index).getAsJsonObject();
            stationObject.add("id", stationObject.get("station_id"));
            stationObject.remove("station_id");

            Station station = new Gson().fromJson(stationObject.toString(), Station.class);

            stationMap.put(station.getId(), station);
          }
        }

        if (tilgjengelighet != null
          && tilgjengelighet.has("data")
          && tilgjengelighet.getAsJsonObject("data").has("stations")) {

          logger.info("FETCHING STATION ACCESS");

          JsonArray tilgjengelighetArray = tilgjengelighet.getAsJsonObject("data").getAsJsonArray("stations");
          for (int index = 0; index < tilgjengelighetArray.size(); index++) {
            //convert from station_id to just id
            JsonObject tilgjengelighetObject = tilgjengelighetArray.get(index).getAsJsonObject();
            tilgjengelighetObject.add("id", tilgjengelighetObject.get("station_id"));
            tilgjengelighetObject.remove("station_id");

            StationAccess stationAccess = new Gson().fromJson(tilgjengelighetObject.toString(), StationAccess.class);

            if (stationAccess != null && stationMap.containsKey(stationAccess.getId())) {
              stationMap.get(stationAccess.getId()).addAccessInfo(stationAccess);

            }
          }
        }

        bysykkelStations.addAll(stationMap.values());
      //} catch (IOException e) {
      //  e.printStackTrace();
      //}


    }


    return bysykkelStations;

  }
}
