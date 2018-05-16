package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;


public class Backend implements Middle {

    private int latitude, longitude;

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



        return null;
    }


    //change location by specifying lat - long
    //returns true for successful location change
    public boolean changeLocation(int latitude, int longitude){
        this.latitude = latitude;
        this.longitude = longitude;

        // TODO check if valid location

        return true;
    }



}
