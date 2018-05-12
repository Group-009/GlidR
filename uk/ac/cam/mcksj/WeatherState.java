package uk.ac.cam.mcksj;

public final class WeatherState {

    private final int starRating; //1-5;
    private final float temperature; //1-5;
    private final float visibility; //in km;
    private final float rain; //chance 0-1;

    //these are here for convenience
    private final WeekDay day;
    private final int time;

    public WeatherState(int starRating, float temperature, float visibility, float rain, WeekDay day, int time) {
        if (starRating<0||starRating>5) throw new IndexOutOfBoundsException("Invalid Star Rating");
        if (rain<0||rain>1) throw new IndexOutOfBoundsException("Invalid Rain Chance");
        if (time<0||time>23) throw new IndexOutOfBoundsException("Invalid Time");
        this.starRating = starRating;
        this.temperature = temperature;
        this.visibility = visibility;
        this.rain = rain;
        this.day = day;
        this.time = time;
    }

    public int getStarRating() {
        return starRating;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getVisibility() {
        return visibility;
    }

    public float getRain() {
        return rain;
    }

    public WeekDay getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }
}
