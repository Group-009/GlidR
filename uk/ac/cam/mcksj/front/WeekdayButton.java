package uk.ac.cam.mcksj.front;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WeekdayButton {
    private StackPane pane = new StackPane();
    private int day;
    private String days = "SMTWTFS";
    private String dayOfWeek;
    Text text = new Text();

    public WeekdayButton(int day, int dayOfWeek) {
        text.setFont(Font.font(30));
        this.day = day;
        updateDayOfWeek(dayOfWeek);
        pane.getChildren().add(text);
        pane.setMinSize(480.0 / 7.0, 68);
        pane.setStyle("-fx-background-color: #"+ColourScheme.DARK_BROWN);
        if (day == 0) pane.setStyle("-fx-background-color: #"+ColourScheme.LIGHT_BROWN);
    }

    public void updateDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = Character.toString(days.charAt((dayOfWeek+day-1)%7));
        text.setText(this.dayOfWeek);
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

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}
