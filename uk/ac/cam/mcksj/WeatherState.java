package uk.ac.cam.mcksj;

public final class WeatherState {

    // To-Do, update meaning comments
    private final int starRating; //0-5;
    private final float temperature; //Celsius;
    private final float visibility; //in percentage visibility;
    private final float rain; //in mm of rainfall in last 3 hours;
    private final float wind; // in knots

    //these are here for convenience
    private final WeekDay day;
    private final int time;

    public WeatherState(){
        this.starRating=0;
        this.temperature=0;
        this.visibility=100;
        this.rain=0;
        this.wind=0;
        this.day=WeekDay.MONDAY;
        this.time=0;
    }

    //To-Do, update acceptable bounds
    public WeatherState(int starRating, float temperature, float visibility, float rain, float wind, WeekDay day, int time) {
        if (starRating<0||starRating>5) throw new IndexOutOfBoundsException("Invalid Star Rating " + starRating);
        if (rain<0) throw new IndexOutOfBoundsException("Invalid Rain Chance");
        if (time<0||time>23) throw new IndexOutOfBoundsException("Invalid Time");
        if (wind<0) throw new IndexOutOfBoundsException("Invalid Wind");
        this.starRating = starRating;
        this.temperature = temperature;
        this.visibility = visibility;
        this.rain = rain;
        this.wind = wind;
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

    public float getRain() { return rain; }

    public float getWind() { return wind; }

    public WeekDay getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }
}
