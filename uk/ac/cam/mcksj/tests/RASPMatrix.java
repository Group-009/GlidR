package uk.ac.cam.mcksj.tests;

public class RASPMatrix {

    public static void main(String[] args) {
        double c = 57.58381;
        double f = -16.61837;

        double a = (61.44211 - c) / 2000d;
        double d = (3.81885 - f) / 2000d;
        double b = (47.73523 - c) / 2000d;
        double e = (-8.79960 - f) / 2000d;

        double i = 1000, k = 1000;

        //double lat = a*i + b*k + c*1;
        //double lon = d*i + e*k + f*1;

        double det = a * e - b * d;

        double[][] A_inverse = new double[][]{
                {e, -b, b*f - c*e},
                {-d, a, -a * f + c * d},
                {0, 0, a * e - b * d}
        };

        for(int p = 0; p < 3; p++) {
            for(int q = 0; q < 3; q++) {
                A_inverse[p][q] /= det;
            }
        }

        double lon = -2.04265,
               lat = 51.48128;

        i = lat * A_inverse[0][0] + lon * A_inverse[0][1] + A_inverse[0][2];
        k = lat * A_inverse[1][0] + lon * A_inverse[1][1] + A_inverse[1][2];
        double t = A_inverse[2][2];

        for(int i1 = 0; i1 < 3; i1++) {
            for(int i2 = 0; i2 < 3; i2++) {
                System.out.print(A_inverse[i1][i2] + ", ");
            }
            System.out.println();
        }

    }

}
