package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;

import java.io.IOException;


public class Backend implements Middle {

    public static void main(String[] args) throws IOException {
        Backend back = new Backend();
    }


    private double latitude, longitude;

    // Indexed first by day, then by time (note time=0..5 are null)
    private WeatherState[][] weatherCache;

    private RaspAPI rasp;

    public Backend() throws IOException {
        rasp = new RaspAPI(52, 0);
        weatherCache = new WeatherState[7][24];
        updateWeather();
    }

    @Override
    public boolean updateWeather() throws IOException {
        rasp.updateThermalData();
        for(int dIndex = 0; dIndex < 7; dIndex++) {
            for(int time = 0; time <= 23; time++) {
                WeekDay day = WeekDay.values()[dIndex];

                // TODO
                float temperature = 0;
                float visibility = 0;
                float rain = 0;

                //int starRating = rasp.getThermalUpdraft(WeekDay.values()[dIndex], time);
                int starRating = 0;

                weatherCache[dIndex][time] = new WeatherState(starRating, temperature, visibility, rain, day, time);
            }
        }
        return true;
    }

    @Override
    public WeatherState getWeather(int day, int time){
        if(time < 0 || time >= 24 || day < 0 || day > 7)
            throw new IllegalArgumentException("Time " + time + " is not supported");
        return weatherCache[day][time];
    }


    @Override
    public boolean changeLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;

        if(!rasp.setIK(latitude, longitude))
            return false;

        WeatherState currWeatherState = new WeatherState(0,0,0,0,WeekDay.MONDAY,0);

        return true;
    }

}
