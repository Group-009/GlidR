package uk.ac.cam.mcksj.front;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uk.ac.cam.mcksj.WeekDay;

public class WeekdayButton {
    private StackPane pane = new StackPane();
    private int barIndex;
    private String days = "SMTWTFS";
    private String iconLabel;
    private Text text = new Text();
    private WeekDay weekDay;

    public WeekdayButton(int barIndex, int dayOfWeek) {
        this.barIndex = barIndex;
        this.weekDay = weekDay;
        updateDayOfWeek(dayOfWeek);

        text.setFont(Font.font(30));
        pane.getChildren().add(text);
        pane.setMinSize(480.0 / 6.0, 68);

        pane.setStyle("-fx-background-color: #"+ColourScheme.DARK_BROWN);
        if (barIndex == 0) pane.setStyle("-fx-background-color: #"+ColourScheme.LIGHT_BROWN);
    }

    public void updateDayOfWeek(int dayOfWeek) {
        this.iconLabel = Character.toString(days.charAt((dayOfWeek+ barIndex -1)%7));
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
