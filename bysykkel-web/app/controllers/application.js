import Controller from '@ember/controller';
import { action } from '@ember/object';
import { filter } from '@ember/object/computed';
import {tracked} from "@glimmer/tracking";

export default class ApplicationController extends Controller {
  @tracked filterString = "";
  @action viewMap(station) {
    station.showMap = true;
  }

  @action hideMap(station) {
    station.showMap = false;
  }

  @action updateFilter() {
    console.log("UPDATING");
  }

  @filter('model.stations.@each.name', ['filterString'], function(station, index, array) {
    console.log(station);
    console.log(station.name);
    console.log(station.name.toLowerCase().indexOf(this.filterString.toLowerCase()) > -1);

    return station.name.toLowerCase().indexOf(this.filterString.toLowerCase()) > -1;
  })
  remainingStations;
}
