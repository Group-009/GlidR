package uk.ac.cam.mcksj.front;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TimeButton { //Buttons for time navigation
    private StackPane pane = new StackPane();
    private int  time;
    private boolean validTime = true;

    public TimeButton(int time, int hour) {
        Text text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font(20));
        text.setText(time + ":00");

        pane.getChildren().add(text);
        pane.setMinSize(480.0 / 7.0, 68.0);
        pane.setStyle("-fx-background-color: #"+ColourScheme.MIDDLE_GREY);
        if (time == hour) pane.setStyle("-fx-background-color: #"+ColourScheme.LIGHT_GREY);

        this.time = time;
    }

    public Pane getPane() {
        return pane;
    }

    public int getTime() {
        return time;
    }

    public void setValid(boolean valid) {
        validTime = valid;
    }

    public boolean isValidTime() {
        return validTime;
    }

    public void setColor(String hex) {
        pane.setStyle("-fx-background-color: #"+hex);
    }
}
