package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;

import java.io.IOException;
import java.util.Calendar;


public class Backend implements Middle {

    public static int SUPPORTED_DAYS = 6;

    private double latitude, longitude;

    // Indexed first by day, then by time
    private WeatherState[][] weatherCache;

    private RaspAPI rasp;

    public Backend() throws IOException, NoWeatherDataException {
        longitude = 51.0;
        latitude = 0.0;
        rasp = new RaspAPI(51.0, 0.0);
        weatherCache = new WeatherState[6][24];
        updateWeather();
    }

    public Backend(double lat, double lon) throws IOException, NoWeatherDataException{
        longitude = lon;
        latitude = lat;
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
    public boolean updateWeather() throws IOException, NoWeatherDataException{
        weatherCache = OpenWeatherMapAPI.update(latitude, longitude);
        rasp.updateThermalData();
        for(int dIndex = 0; dIndex < 6; dIndex++) {
            for(int time = 0; time <= 23; time++) {
                WeatherState state = weatherCache[dIndex][time];
                if(state == null)
                    continue;
                int starRating = calculateStarRating(rasp.getThermalUpdraft(dIndex, time), state.getVisibility(), state.getWind());
                weatherCache[dIndex][time] = new WeatherState(
                        starRating,
                        state.getTemperature(),
                        state.getVisibility(),
                        state.getRain(),
                        state.getWind(),
                        state.getDay(),
                        state.getTime()
                );
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

    /**
     *
     * @param day The days ahead of where we are (0=today, 1=tomorrow)
     * @return Day of week index
     */
    public static int getWeekDay(int day) {
        // We start counting from today, so increment day appropriately
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }


    /**
     *
     * @param thermal The thermal speed parameter
     * @param visibility Visibility parameter
     * @param wind Wind parameter
     * @return A average of the normalised values
     */
    private static int calculateStarRating(int thermal, float visibility, float wind) {
        // Thermals 0-bad 600-good
        // Visibility 0-bad 1-good
        // Wind 0-good 26-very bad

        float tNorm = Math.min(thermal / 600f, 1);
        float vNorm = visibility / 100f;
        float wNorm = (26f - wind) / 26f;

        if(wNorm < 0)
            return 0;

        return (int) ((tNorm + vNorm + wNorm) / 3f * 5f + 0.5f);
    }
}
