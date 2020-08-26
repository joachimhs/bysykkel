import Model, { attr } from '@ember-data/model';
import { tracked } from '@glimmer/tracking';


export default class StationModel extends Model {
  @attr('string') name;
  @attr('string') address;
  @attr('string') lat;
  @attr('string') lon;
  @attr('string') capacity;
  @attr('string') numBikesAvailable;
  @attr('string') numDocksAvailable;

  @tracked showMap = false;

  get streetMapUrl() {
    return "https://www.openstreetmap.org/export/embed.html?bbox=" + this.lon + "%2C" + this.lat + "%2C" + (this.lon - 0.0001) + "%2C" + (this.lat - 0.0001) + "&amp;layers=mapnik&amp;marker=" + this.lat + "%2C" + this.lon;
  }

  get streetMapLinkUrl() {
    return "https://www.openstreetmap.org/?mlat=" + this.lat + "&amp;mlon=" + this.lon + "#map=18/" + this.lat + "/" + this.lon;
  }

}
