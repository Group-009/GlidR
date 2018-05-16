package uk.ac.cam.mcksj.back;

import uk.ac.cam.mcksj.WeekDay;

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

    private int i, k;

    /**
     *
     * @param lat latitude
     * @param lon longitude
     */
    public void setRaspIK(double lat, double lon) {
        int i = (int) (lat * TRANSFORM_MAT[0][0] + lon * TRANSFORM_MAT[0][1] + 1 * TRANSFORM_MAT[0][2]);
        int k = (int) (lat * TRANSFORM_MAT[1][0] + lon * TRANSFORM_MAT[1][1] + 1 * TRANSFORM_MAT[1][2]);
    }

    /**
     *
     * @param day The day that we want the data for
     * @param time The time in //TODO
     * @return The thermal updraft for that time and day in //TODO
     */
    private int getThermalUpdraft(WeekDay day, int time) {
        return 0;
    }


    /**
     *
     * @param region NOT an actual region, but the day of the week
     * @param i The i co-ordinate
     * @param k The k co-ordinate
     * @return The URL to access the RASP data for the given parameters
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
