package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;


public class Backend implements Middle {

    private static final double[][] RASP_TRANSFORMATION_MAT = new double[][]{
            {0.00192915, -0.00492429, 57.58381},
            {0.01021861, 0.003909385, -16.61837}
    };

    private int latitude, longitude;

    private int Rasp_i, Rasp_k;

    /*
    This should take a location argument but I'm not sure
    what format location should be in
    Return true for successful update
     */
    public boolean updateWeather(){

        return true;
    }

    //time is an int ranging from 0-23 inclusive
    //Should return a WeatherState object which includes conditions for specified day/time
    public WeatherState getWeather(WeekDay day, int time){



        return null;
    }


    //change location by specifying lat - long
    //returns true for successful location change
    public boolean changeLocation(int latitude, int longitude){
        this.latitude = latitude;
        this.longitude = longitude;

        // TODO check if valid location

        return true;
    }

    private void setRaspIK() {

    }


    private int getThermalUpdraft(WeekDay day, int time) {
        return 0;
    }


    /*
    Generates a URL for RASP
    region - the day (don't ask me)
    i - RASP's own 'x' coordinate
    k - RASP's own 'y' coordinate
    */
    public static String generateRASPURL(String region, int i, int k) {
        return "http://rasp.mrsap.org/cgi-bin/get_rasp_blipspot.cgi?region=" +
                region +
                "&grid=d2&day=0&i=" +
                i +
                "&k=" +
                k +
                "&width=2000&height=2000&linfo=0";

    }

}
