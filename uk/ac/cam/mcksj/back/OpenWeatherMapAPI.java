package uk.ac.cam.mcksj.back;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class OpenWeatherMapAPI {
    void update(){
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=51.0&lon=1.0&appid=289d10d3384fffe3eaec60ea7d27f8c6");
            InputStream in = url.openConnection().getInputStream();

            ReadableByteChannel readChannel = Channels.newChannel(in);
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            readChannel.read(buffer);

            int bracketDepth = 0;
            for(int i=0; i<buffer.array().length; i++) {
                int iAsciiValue = buffer.array()[i];
                String strAsciiTab = Character.toString((char) iAsciiValue);
                if(strAsciiTab.equals("{")){
                    bracketDepth += 1;
                }
                else if (strAsciiTab.equals("}")){
                    bracketDepth -= 1;
                }

                System.out.print(strAsciiTab);
                if (bracketDepth == 0) {
                    break;
                }
                if (strAsciiTab.equals(",") && bracketDepth<=1){
                    System.out.println();
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
//        Backend back = new Backend();
//        back.updateWeather();
    }
}
