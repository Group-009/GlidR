package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OpenWeatherMapAPI {

    private static final String APIKey = "289d10d3384fffe3eaec60ea7d27f8c6";

    /**
     * Returns an up to date WeatherState[day][hour]
     * 0<=day<=5
     * 0<=hour<=23
     * @param lat latitude
     * @param lon longitude
     * @return weatherCache
     */
    public static WeatherState[][] update(double lat, double lon){
        String hourly_data = getJSONData(lat, lon);

        WeatherState[][] weatherCache = parse(hourly_data);
        interpolateWeather(weatherCache);

        return weatherCache;
    }


    /**
     * Returns the JSON response from https://openweathermap.org/ for a given longitude and latitude (hourly data)
     *
     * @param lat latitude: -90<=lat<=90
     * @param lon longitude: -180<=lon<=180
     * @return the JSON response in form of a string
     */
    private static String getJSONData(double lat, double lon){
        // Sanitise:
        if (!checkCoord(lat, lon)){
            throw new IllegalArgumentException("Invalid lat/lon: " + lat + " " + lon);
        }

        String hourly_data = "";
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + APIKey);
            InputStream in = url.openConnection().getInputStream();

            ReadableByteChannel readChannel = Channels.newChannel(in);
            ByteBuffer buffer = ByteBuffer.allocate(20000);
            readChannel.read(buffer);
            readChannel.read(buffer);

            for (int i = 0; i < buffer.array().length; i++) {
                int iAsciiValue = buffer.array()[i];
                hourly_data += Character.toString((char) iAsciiValue);
            }
        }

        catch(MalformedURLException e1){
            System.out.println(e1);
        }
        catch(IOException e2){
            System.out.println(e2);
        }
        return hourly_data;
    }


    /**
     * Converts unix time to WeekDay for UTC, does not take timezones into account
     *
     * @param lat latitude: -90<=lat<=90
     * @param lon longitude: -180<=lon<=180
     * @return false if the coords are valid, true if they are valid
     */
    private static boolean checkCoord(double lat, double lon){
        return (lat>=-90 && lat<=90 && lon>-180 && lon<=180);
    }

    private static WeatherState[][] parse(String JSONData){
        char[] charJSONData = JSONData.toCharArray();

        int bracketDepth = 0;
        String currString = "";
        String[] stringArray = new String[50];

        WeatherState[][] weatherCache = new WeatherState[6][24];
        float temp = 0.0f;
        float rain = 0.0f;
        float vis = 0.0f;
        float wind = 0.0f;
        int time = 0;
        int unixTime = 0;
        int unixDeltaDay = 0;

        int stringArrayIndex = 0;
        for(char c: charJSONData) {
            if(c == '{' || c == '['){
                bracketDepth += 1;
            }
            else if (c == '}' || c == ']'){
                bracketDepth -= 1;
            }

            currString += c;

            if (bracketDepth == 2 && (c == '}' || c == ']')) {
                stringArray[stringArrayIndex] = currString;
                stringArrayIndex += 1;
                currString = "";
            }

            if (bracketDepth==0){
                break;
            }
        }

        int firstUnixDayIndex = stringArray[0].indexOf("\"dt\":");
        int firstUnixDay = Integer.valueOf(stringArray[0].substring(firstUnixDayIndex + 5, firstUnixDayIndex + 15).replaceAll("[^0-9.]",""))/86400;
        for(String s: stringArray) {
            if (s != null && s.contains("temp")) {
                int tempIndex = s.indexOf("temp\":");
                int rainIndex = s.indexOf("\"rain\":{\"3h\":");
                int visIndex = s.indexOf("\"clouds\":{\"all\":");
                int windIndex = s.indexOf("\"speed\":");
                int timeIndex = s.indexOf("\"dt_txt\":");
                int unixTimeIndex = s.indexOf("\"dt\":");

                temp = Float.valueOf(s.substring(tempIndex + 6, tempIndex + 11).replaceAll("[^0-9.]",""))-273.15f;
                rain = 0.0f;
                if(s.contains("\"rain\":{\"3h\":")){
                    rain = Float.valueOf(s.substring(rainIndex + 13, rainIndex + 17).replaceAll("[^0-9.]",""));
                }
                vis = 100.00f - Float.valueOf(s.substring(visIndex + 16, visIndex + 18).replaceAll("[^0-9.]",""));
                wind = Float.valueOf(s.substring(windIndex + 8, windIndex + 14).replaceAll("[^0-9.]",""));
                time = Integer.valueOf(s.substring(timeIndex + 20, timeIndex + 24).replaceAll("[^0-9.]",""));
                unixTime = Integer.valueOf(s.substring(unixTimeIndex + 5, unixTimeIndex + 15).replaceAll("[^0-9.]",""));
                unixDeltaDay = unixTime/86400-firstUnixDay;

                WeekDay day = cnvrtUnixToWeekDay(unixTime);

                WeatherState currWeatherState = new WeatherState(0, temp, vis, rain, wind, day, time);

                weatherCache[unixDeltaDay][time] = currWeatherState;
            }
        }
        return weatherCache;
    }


    /**
     * Converts unix time to WeekDay for UTC, does not take timezones into account
     *
     * @param unixTime a given unix time stamp
     * @return the corresponding Weekday
     */
    private static WeekDay cnvrtUnixToWeekDay(int unixTime){

        long bigUnixTime = unixTime;

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date dateFormat = new java.util.Date(bigUnixTime * 1000);
        String weekday = sdf.format(dateFormat);

        WeekDay day = WeekDay.MONDAY;

        if(weekday.equals("Tuesday")){
            day = WeekDay.TUESDAY;
        }
        else if(weekday.equals("Wednesday")){
            day = WeekDay.WEDNESDAY;
        }
        else if(weekday.equals("Thursday")){
            day = WeekDay.THURSDAY;
        }
        else if(weekday.equals("Friday")){
            day = WeekDay.FRIDAY;
        }
        else if(weekday.equals("Saturday")){
            day = WeekDay.SATURDAY;
        }
        else if(weekday.equals("Sunday")){
            day = WeekDay.SUNDAY;
        }

        return day;
    }

    /**
     * Fills in any missing data by using linear interpolation.
     *
     * @param weatherCache the 2D array of weather states
     * @return pass by reference
     */
    private static void interpolateWeather(WeatherState[][] weatherCache){
        for(int day=0; day<6; day++){
            for(int time=0; time<24; time++){
                if (weatherCache[day][time]==null){
                    weatherCache[day][time]=getWeatherState(weatherCache, day, time);
                }
            }
        }
        return;
    }

    /**
     * Gets the weather state for a day/time, and interpolates new data if necessary
     *
     * @param weatherCache the 2D array of weather states
     * @param day the day for which the weather data is requested
     * @param time the time for which the weather data is requested
     * @return the weather state of the required day/time
     */
    private static WeatherState getWeatherState(WeatherState[][] weatherCache, int day, int time){
        if (weatherCache[day][time]!=null){
            return weatherCache[day][time];
        }
        else{
            if (day==0 && time==0){
                for(WeatherState[] dayWeather: weatherCache){
                    for(WeatherState timeWeather: dayWeather){
                        if (timeWeather != null){
                            return timeWeather;
                        }
                    }
                }
                return getWeatherState(weatherCache, 0, 1);
            }
            else{
                WeatherState prevState;
                if(time==0) {
                    prevState = weatherCache[day - 1][23];
                }
                else{
                    prevState = weatherCache[day][time-1];
                }
                for(int timeW=time; timeW<24;timeW++){
                    if (weatherCache[day][timeW] != null){
                        return calcNtileWeatherState(prevState,weatherCache[day][timeW], (timeW-time), time);
                    }
                }
                for(int dayW=day+1; dayW<6; dayW++){
                    for(int timeW=0; timeW<24;timeW++){
                        if (weatherCache[dayW][timeW] != null){
                            return calcNtileWeatherState(prevState,weatherCache[dayW][timeW], (dayW-day)*24 + (timeW-time), time);
                        }
                    }
                }
                return null;
            }
        }
    }


    /**
     * Determines the the weather state at a specific day/time, based on the previous and next available weather state
     *
     * @param prevWeatherState the weather state for the previous hour
     * @param nextWeatherState the weather state for the next available weather state
     * @param timeDiff the number of hours between prev and next WeatherStates
     * @param time the time for which the weather data is requested
     * @return the predicted weather conditions (predicted by linear interpolation)
     */
    private static WeatherState calcNtileWeatherState(WeatherState prevWeatherState, WeatherState nextWeatherState, int timeDiff, int time){
        int starRating = prevWeatherState.getStarRating();
        if(nextWeatherState.getStarRating()-nextWeatherState.getStarRating()>0){
            starRating -= 1;
        }
        else if(nextWeatherState.getStarRating()-nextWeatherState.getStarRating()<0){
            starRating += 1;
        }
        float temperature = ((nextWeatherState.getTemperature()-prevWeatherState.getTemperature())/(timeDiff+1))+prevWeatherState.getTemperature();
        float visibility = ((nextWeatherState.getVisibility()-prevWeatherState.getVisibility())/(timeDiff+1))+prevWeatherState.getVisibility();
        float rain = ((nextWeatherState.getRain()-prevWeatherState.getRain())/(timeDiff+1))+prevWeatherState.getRain();
        float wind = ((nextWeatherState.getWind()-prevWeatherState.getWind())/(timeDiff+1))+prevWeatherState.getWind();
        WeekDay day;
        if (prevWeatherState.getTime()>22){
            if (prevWeatherState.getDay()==WeekDay.SUNDAY){
                day=WeekDay.MONDAY;
            }
            else {
                day = WeekDay.values()[prevWeatherState.getDay().ordinal()+1];
            }
        }
        else{
            day=prevWeatherState.getDay();
        }
        return new WeatherState(starRating, temperature, visibility, rain, wind, day, time);
    }

    /**
     * Prints the weatherCache in CSV format
     *
     * @param weatherCache the 2D array of weather states
     */
    public static void printCSVWeatherCache(WeatherState[][] weatherCache){
        for(int day=0; day<6; day++){
            for(int time=0; time<24; time++){
                if (weatherCache[day][time] != null) {
                    System.out.print(weatherCache[day][time].getStarRating() + " ,");
                    System.out.print(weatherCache[day][time].getTemperature() + " ,");
                    System.out.print(weatherCache[day][time].getVisibility() + " ,");
                    System.out.print(weatherCache[day][time].getRain() + " ,");
                    System.out.print(weatherCache[day][time].getWind() + " ,");
                    System.out.print(weatherCache[day][time].getDay() + " ,");
                    System.out.println(weatherCache[day][time].getTime());
                }
                else{
                    System.out.println("null");
                }
            }
        }
    }
}

// To do, Saaras:
//  Change 0<=day<=5
//  Refactor
//  JSON parser
//  Fix other parts of code
//  Test/ debug
