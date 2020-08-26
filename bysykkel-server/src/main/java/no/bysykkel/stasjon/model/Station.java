package no.bysykkel.stasjon.model;

import com.google.gson.annotations.Expose;

/**
 * Data model that stores the data for each station. Used in both the urbansharing API and this servers API
 */
public class Station {
  @Expose private int id;
  @Expose private String name;
  @Expose private String address;
  @Expose private double lat;
  @Expose private double lon;
  @Expose private int capacity;
  @Expose private int numBikesAvailable;
  @Expose private int numDocksAvailable;

  public Station() {
  }

  public void addAccessInfo(StationAccess access) {
    if (access != null) {
      this.numBikesAvailable = access.getNum_bikes_available();
      this.numDocksAvailable = access.getNum_docks_available();
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public int getNumBikesAvailable() {
    return numBikesAvailable;
  }

  public void setNumBikesAvailable(int numBikesAvailable) {
    this.numBikesAvailable = numBikesAvailable;
  }

  public int getNumDocksAvailable() {
    return numDocksAvailable;
  }

  public void setNumDocksAvailable(int numDocksAvailable) {
    this.numDocksAvailable = numDocksAvailable;
  }
}
