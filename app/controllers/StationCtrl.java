package controllers;

import models.Member;
import models.Reading;
import models.Station;
import play.Logger;
import play.mvc.Controller;
import utils.StationDetails;

import java.util.*;

import static utils.StationUtils.*;

public class StationCtrl extends Controller {

    public static void stations() {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            List<Station> stations = loggedInUser.stations;
            for (Station station : stations) {
                calcStationDetails(station);
            }
            render("stations.html", loggedInUser, stations);
        } catch (Exception e) {
            Logger.info("Failed to load all stations: " + e.toString());
            render("errors/404.html");
        }
    }

    public static void station(Long id) {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            Station station = Station.findById(id);
            StationDetails stats = calcStationDetails(station);
            render("station.html", station, stats);
        } catch (Exception e) {
            Logger.info("Failed to load station: " + e.toString());
            render("errors/404.html");
        }
    }

    public static void addStation(String station_name, double latitude, double longitude) {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            Station station = new Station(station_name, latitude, longitude);
            station.save();
            loggedInUser.stations.add(station);
            loggedInUser.save();
            Logger.info("adding station" + station + " to user " + loggedInUser.lastName);
            redirect("/stations");
        } catch (Exception e) {
            Logger.info("Failed to add station: " + e.toString());
            redirect("errors/404.html");
        }
    }
}