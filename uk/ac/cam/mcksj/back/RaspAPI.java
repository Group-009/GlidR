package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.WeekDay;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RaspAPI {

    /*
    Relationship between lat-long and i-k can be represented with a matrix
    These values were determined from tests in uk.ac.cam.mcksj.RASPMatrix
     */
    private static final double[][] TRANSFORM_MAT = new double[][]{
            {67.56489211778053, 85.10523333124404, -2476.333653945825},
            {-176.6055996643138, 33.34100162276581, 10723.69639714363},
            {0.0, 0.0, 1.0}
    };

    private int i = 1000, k = 1000;

    public static void main(String[] args) throws IOException {
        RaspAPI api = new RaspAPI();
        for(int j = 6; j <= 18; j++)
            System.out.println(api.getThermalUpdraft(WeekDay.FRIDAY, j));
    }

    /**
     *
     * @param lat latitude
     * @param lon longitude
     * @return Whether the co-ordinates are valid
     */
    public boolean setIK(double lat, double lon) {
        int i = (int) (lat * TRANSFORM_MAT[0][0] + lon * TRANSFORM_MAT[0][1] + 1 * TRANSFORM_MAT[0][2]);
        int k = (int) (lat * TRANSFORM_MAT[1][0] + lon * TRANSFORM_MAT[1][1] + 1 * TRANSFORM_MAT[1][2]);
        return (i >= 0) && (i <= 2000) && (k >= 0) && (k <= 2000);
    }

    /**
     *
     * @param day The day that we want the data for
     * @param time The time in hours (between 06:00 and 18:00 inclusive)
     * @return The thermal updraft for that time and day in //TODO
     */
    public int getThermalUpdraft(WeekDay day, int time) throws IOException {
        if(time < 6 || time > 18)
            throw new IllegalArgumentException("time " + time + " is not supported by RASP.  "
            + "Please use the range 06:00-18:00");

        String dayStr = day.toString().toLowerCase();
        dayStr = dayStr.substring(0, 1).toUpperCase() + dayStr.substring(1);
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new URL(generateRASPURL(dayStr)).openStream());
            byte[] dataRaw = in.readAllBytes();
            String data = new String(dataRaw, StandardCharsets.UTF_8);
            String thermalData = data.split("\n")[0];
            return parseThermalData(thermalData, time);
        }
        finally {
            if(in != null)
                in.close();
        }

    }

    /**
     *
     * @param data Line of thermal data
     * @param time Time in hours
     * @return The thermal data
     */
    private int parseThermalData(String data, int time) {
        String[] comps = data.split(" ");
        // Data starts at 06:00, so normalise time to this
        // First two components are padding
        int nTime = 2 * (time - 6) + 2;
        return Integer.valueOf(comps[nTime]);
    }


    /**
     *
     * @param region NOT an actual region, but the day of the week
     * @return The URL to access the RASP data for the given parameters
     */
    private String generateRASPURL(String region) {
        return "http://rasp.mrsap.org/cgi-bin/get_rasp_blipspot.cgi?region=" +
                region +
                "&grid=d2&day=0&i=" +
                i +
                "&k=" +
                k +
                "&width=2000&height=2000&linfo=0";

    }
}
