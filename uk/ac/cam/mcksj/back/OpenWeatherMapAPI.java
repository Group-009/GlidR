package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenWeatherMapAPI {

    public static WeatherState[][] update(float lat, float lon){
        String hourly_data = "";
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=289d10d3384fffe3eaec60ea7d27f8c6");
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

        catch(Exception e){
            System.out.println(e);
        }

        WeatherState[][] weatherCache = parse(hourly_data);
        interpolateWeather(weatherCache);


        return weatherCache;
    }

    private static WeatherState[][] parse(String data){
        char[] charArray = data.toCharArray();

        int bracketDepth = 0;
        int index = 0;
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
        for(int i=0; i<charArray.length; i++) {
            index+=1;
            if(charArray[i] == '{' || charArray[i] == '['){
                bracketDepth += 1;
            }
            else if (charArray[i] == '}' || charArray[i] == ']'){
                bracketDepth -= 1;
            }

            currString += charArray[i];

            if (bracketDepth == 2 && (charArray[i] == '}' || charArray[i] == ']')) {
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
        System.out.println(firstUnixDay);
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

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date dateFormat = new java.util.Date(unixTime * 1000);
                String weekday = sdf.format(dateFormat);
                System.out.println();

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

                WeatherState currWeatherState = new WeatherState(0, temp, vis, rain, wind, day, time);

                System.out.println(s);
                System.out.println(weekday);
                System.out.println(unixDeltaDay);
                weatherCache[unixDeltaDay][time] = currWeatherState;
            }
        }
        return weatherCache;
    }

    private static void interpolateWeather(WeatherState[][] weatherCache){
        WeatherState mostRecentState = new WeatherState(0,0.0f,0.0f,0,0,WeekDay.MONDAY, 0);
        for(int day=0; day<5; day++){
            for(int time=0; time<24; time++){
                if (weatherCache[day][time]==null){
                    weatherCache[day][time] = mostRecentState;
                }
                else{
                    mostRecentState = weatherCache[day][time];
                }
            }
        }
        return;
    }

    public static void printWeatherCache(WeatherState[][] weatherCache){
        for(int day=0; day<5; day++){
            for(int time=0; time<24; time++){
                System.out.print(weatherCache[day][time].getStarRating() + " ,");
                System.out.print(weatherCache[day][time].getTemperature() + " ,");
                System.out.print(weatherCache[day][time].getVisibility() + " ,");
                System.out.print(weatherCache[day][time].getRain() + " ,");
                System.out.print(weatherCache[day][time].getWind() + " ,");
                System.out.print(weatherCache[day][time].getDay() + " ,");
                System.out.println(weatherCache[day][time].getTime() + " ,");
            }
        }
    }

        // To do, Saaras:
        //  Connect to backend
        //  Improve interpolation
        //  Test/ debug

}
