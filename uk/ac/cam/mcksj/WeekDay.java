package uk.ac.cam.mcksj;

public enum WeekDay {
    MONDAY(0),TUESDAY(1),WEDNESDAY(2),THURSDAY(3),FRIDAY(4),SATURDAY(5),SUNDAY(6);

    // Assert that WeekDay.values()[w.index] = w (i.e. don't change these)
    public final int index;

    WeekDay(int index) {
        this.index = index;
    }
}
