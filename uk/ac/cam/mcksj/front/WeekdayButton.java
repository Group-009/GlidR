package uk.ac.cam.mcksj.front;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uk.ac.cam.mcksj.WeekDay;

public class WeekdayButton { //Buttons for weekday navigation
    private StackPane pane = new StackPane();
    private int barIndex;
    private String[] days = {"Su","Mo","Tu","We","Th","Fr","Sa"};
    private String iconLabel;
    private Text text = new Text();
    private WeekDay weekDay;

    public WeekdayButton(int barIndex, int dayOfWeek) {
        this.barIndex = barIndex;
        this.weekDay = weekDay;
        updateDayOfWeek(dayOfWeek);

        text.setFont(Font.font(30));
        text.setFill(Color.WHITE);
        pane.getChildren().add(text);
        pane.setMinSize(480.0 / 5.0, 68);

        pane.setStyle("-fx-background-color: #"+ColourScheme.MIDDLE_GREY);
        if (barIndex == 0) pane.setStyle("-fx-background-color: #"+ColourScheme.LIGHT_GREY);
    }

    //To make sure that first day in bar is the current day of the week
    public void updateDayOfWeek(int dayOfWeek) {
        this.iconLabel = days[(dayOfWeek+ barIndex -1)%7];
        text.setText(this.iconLabel);
    }

    public Pane getPane() {
        return pane;
    }

    public int getBarIndex() {
        return barIndex;
    }

    public void setColor(String hex) {
        pane.setStyle("-fx-background-color: #"+hex);
    }

    //for string labels
    public String getIconLabel() {
        return iconLabel;
    }

    //for API calls
    public WeekDay getWeekDay() {
        return weekDay;
    }


}
