package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;

import java.io.IOException;


public class Backend implements Middle {

    public static void main(String[] args) throws IOException {
        Backend back = new Backend();
        System.out.println(back.getWeather(WeekDay.THURSDAY, 12).getStarRating());
    }


    private int latitude, longitude;

    // Indexed first by day, then by time (note time=0..5 are null)
    private WeatherState[][] weatherCache;

    private RaspAPI rasp;

    private OpenWeatherMapAPI owm;


    public Backend() {
        rasp = new RaspAPI();
        weatherCache = new WeatherState[7][MAX_SUPPORTED_TIME + 1];
    public Backend() throws IOException {
        rasp = new RaspAPI(52, 0);
        weatherCache = new WeatherState[7][24];
        updateWeather();
    }

    /*
    This should take a location argument but I'm not sure
    what format location should be in
    Return true for successful update
     */
    public boolean updateWeather() throws IOException {
        rasp.updateThermalData();
        for(int dIndex = 0; dIndex < 7; dIndex++) {
            for(int time = 0; time <= 23; time++) {
                WeekDay day = WeekDay.values()[dIndex];

                // TODO
                float temperature = 0;
                float visibility = 0;
                float rain = 0;

                int starRating = rasp.getThermalUpdraft(WeekDay.values()[dIndex], time);
                //

                weatherCache[dIndex][time] = new WeatherState(starRating, temperature, visibility, rain, day, time);
            }
        }
        return true;
    }

    //time is an int ranging from 0-23 inclusive
    //Should return a WeatherState object which includes conditions for specified day/time
    public WeatherState getWeather(WeekDay day, int time){
        if(time < 0 || time >= 24)
            throw new IllegalArgumentException("Time " + time + " is not supported");
        return weatherCache[day.index][time];
    }


    //change location by specifying lat - long
    //returns true for successful location change
    public boolean changeLocation(int latitude, int longitude){
        this.latitude = latitude;
        this.longitude = longitude;

        if(!rasp.setIK(latitude, longitude))
            return false;

        WeatherState currWeatherState = new WeatherState(0,0,0,0,WeekDay.MONDAY,0);

        return true;
    }

}
