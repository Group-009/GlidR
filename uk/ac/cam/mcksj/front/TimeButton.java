package uk.ac.cam.mcksj.front;

import javafx.scene.layout.Pane;

public class TimeButton {
    private Pane pane = new Pane();
    private int  time;

    public TimeButton(int time) {
        pane.setMinSize(480.0 / 7.0, 68.0);
        pane.setStyle("-fx-background-color: #000");
        if (time == 0) pane.setStyle("-fx-background-color: #FFF");

        this.time = time;
    }

    public Pane getPane() {
        return pane;
    }

    public int getTime() {
        return time;
    }

    public void setColor(String hex) {
        pane.setStyle("-fx-background-color: #"+hex);
    }
}
