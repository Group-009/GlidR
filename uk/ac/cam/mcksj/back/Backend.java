package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;


public class Backend implements Middle {

    private int latitude, longitude;

    private RaspAPI rasp;
    
    public Backend() {
        rasp = new RaspAPI();
    }

    /*
    This should take a location argument but I'm not sure
    what format location should be in
    Return true for successful update
     */
    public boolean updateWeather(){

        return true;
    }

    //time is an int ranging from 0-23 inclusive
    //Should return a WeatherState object which includes conditions for specified day/time
    public WeatherState getWeather(WeekDay day, int time){
        // TODO
        float temperature = 0;
        float visibility = 0;
        float rain = 0;

        int starRating = 0;

        return new WeatherState(starRating, temperature, visibility, rain, day, time);
    }


    //change location by specifying lat - long
    //returns true for successful location change
    public boolean changeLocation(int latitude, int longitude){
        this.latitude = latitude;
        this.longitude = longitude;

        if(!rasp.setRaspIK(latitude, longitude))
            return false;

        return true;
    }



}
