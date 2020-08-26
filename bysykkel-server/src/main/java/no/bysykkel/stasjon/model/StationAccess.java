package no.bysykkel.stasjon.model;

/**
 * Data model for storing availability when fetching from external API
 */
public class StationAccess {
  private int id;
  private int num_bikes_available;
  private int num_docks_available;

  public StationAccess() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getNum_bikes_available() {
    return num_bikes_available;
  }

  public void setNum_bikes_available(int num_bikes_available) {
    this.num_bikes_available = num_bikes_available;
  }

  public int getNum_docks_available() {
    return num_docks_available;
  }

  public void setNum_docks_available(int num_docks_available) {
    this.num_docks_available = num_docks_available;
  }
}
