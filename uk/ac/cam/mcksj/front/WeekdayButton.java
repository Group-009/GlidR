package uk.ac.cam.mcksj.front;

import javafx.scene.layout.Pane;

public class WeekdayButton {
    private Pane pane = new Pane();
    private int day;

    public WeekdayButton(int day) {
        pane.setMinSize(480.0 / 7.0, 68);
        pane.setStyle("-fx-background-color: #000");
        if (day == 0) pane.setStyle("-fx-background-color: #FFF");
        this.day = day;
    }

    public Pane getPane() {
        return pane;
    }

    public int getDay() {
        return day;
    }

    public void setColor(String hex) {
        pane.setStyle("-fx-background-color: #"+hex);
    }
}
