package uk.ac.cam.mcksj.back;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class OpenWeatherMapAPI {
    String update(){
        String hourly_data = "";
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?lat=51.0&lon=1.0&appid=289d10d3384fffe3eaec60ea7d27f8c6");
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

        return hourly_data;
    }

    void parse(String data){
        char[] charArray = data.toCharArray();

        int bracketDepth = 0;
        int index = 0;
        String currString = "";
        String[] stringArray = new String[50];

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
        
        for(String s: stringArray) {
            if (s != null && s.contains("temp")) {
                int tempIndex = s.indexOf("temp\":");
                int rainIndex = s.indexOf("\"rain\":{\"3h\":");
                int visIndex = s.indexOf("\"clouds\":{\"all\":");
                int windIndex = s.indexOf("\"speed\":");
                int timeIndex = s.indexOf("\"dt_txt\":");
                int unixTimeIndex = s.indexOf("\"dt\":");

                Double temp = Double.valueOf(s.substring(tempIndex + 6, tempIndex + 11).replaceAll("[^0-9.]",""))-273.15;
                Double rain = 0.0;
                if(s.contains("\"rain\":{\"3h\":")){
                    rain = Double.valueOf(s.substring(rainIndex + 13, rainIndex + 17).replaceAll("[^0-9.]",""));
                }
                Double vis = 100.00 - Double.valueOf(s.substring(visIndex + 16, visIndex + 18).replaceAll("[^0-9.]",""));
                Double wind = Double.valueOf(s.substring(windIndex + 8, windIndex + 14).replaceAll("[^0-9.]",""));
                int time = Integer.valueOf(s.substring(timeIndex + 20, timeIndex + 24).replaceAll("[^0-9.]",""));


                System.out.println(s);
                System.out.println(time);
            }
        }
    }



    public static void main(String[] args){
        OpenWeatherMapAPI owmapi = new OpenWeatherMapAPI();
        String data = owmapi.update();
        owmapi.parse(data);

    }
}
