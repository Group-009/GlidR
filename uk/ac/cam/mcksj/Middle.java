package uk.ac.cam.mcksj;

public interface Middle {

    /*
    This should take a location argument but I'm not sure
    what format location should be in
     */
    public void updateWeather();

    //time is an int ranging from 0-23 inclusive
    //Should return a WeatherState object which includes conditions for specified day/time
    public WeatherState getWeather(WeekDay day, int time);
}
