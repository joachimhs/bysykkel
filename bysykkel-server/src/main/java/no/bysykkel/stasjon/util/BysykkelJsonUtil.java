package no.bysykkel.stasjon.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Ths simple utility class will fetch JSON from a URL and return a GSON JsonObject.
 */
public class BysykkelJsonUtil {
  private static final Logger logger = Logger.getLogger(BysykkelJsonUtil.class.getName());

  public static JsonObject readJsonFromUrl(String url) throws IOException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JsonObject jsonObject = new JsonParser().parse(jsonText).getAsJsonObject();

      return jsonObject;
    } finally {
      is.close();
    }
  }

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }
}
