package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;

import java.io.IOException;


public class Backend implements Middle {


    public static void main(String[] args) throws IOException, NoWeatherDataException {
        Backend back = new Backend(52, 0);
        System.out.println(back.getWeather(0, 12).getStarRating());
    }


    private double latitude, longitude;

    // Indexed first by day, then by time
    private WeatherState[][] weatherCache;

    private RaspAPI rasp;

    public Backend() throws IOException, NoWeatherDataException {
        rasp = new RaspAPI(51.0, 0);
        weatherCache = new WeatherState[6][24];
        updateWeather();
    }

    public Backend(double lat, double lon) throws IOException, NoWeatherDataException {
        rasp = new RaspAPI(lat, lon);
        weatherCache = new WeatherState[6][24];
        updateWeather();
    }


    /**
     * Updates the weather for next 120 hours;
     * Times before the current time on the same day will have the same weather as the current weather
     * Times after 120 hours past the current time will contain null
     * @return True for a successful update
     */
    public boolean updateWeather() throws IOException, NoWeatherDataException {
        weatherCache = OpenWeatherMapAPI.update(latitude, longitude);
        rasp.updateThermalData();
        for(int dIndex = 0; dIndex < 6; dIndex++) {
            for(int time = 0; time <= 23; time++) {
                //TODO
                WeekDay day = WeekDay.values()[dIndex];

                // TODO

                float temperature = 0;
                float visibility = 0;
                float rain = 0;
                float wind = 0;
                int starRating = 0;
                //


                weatherCache[dIndex][time] = new WeatherState(starRating, temperature, visibility, rain, wind, day, time);
            }
        }
        return true;
    }


    //time is an int ranging from 0-23 inclusive
    //Should return a WeatherState object which includes conditions for specified day/time
    public WeatherState getWeather(int day, int time){
        if(time < 0 || time >= 24)
            throw new IllegalArgumentException("Time " + time + " is not supported");
        return weatherCache[day][time];
    }


    @Override
    public boolean changeLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;

        if(!rasp.setIK(latitude, longitude))
            return false;

        return true;
    }

}
