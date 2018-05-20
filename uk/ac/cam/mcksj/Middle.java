package uk.ac.cam.mcksj;

import uk.ac.cam.mcksj.back.NoWeatherDataException;

import java.io.IOException;

public interface Middle {

    /*
    This should take a location argument but I'm not sure
    what format location should be in
    Return true for successful update
     */
    public boolean updateWeather() throws IOException, NoWeatherDataException;

    //time is an int ranging from 0-23 inclusive
    //Should return a WeatherState object which includes conditions for specified day/time
    public WeatherState getWeather(int day, int time);

    //change location by specifying lat - long
    //returns true for successful location change
    public boolean changeLocation(double latitude, double longitude);
}
