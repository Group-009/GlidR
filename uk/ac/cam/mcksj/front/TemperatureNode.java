package uk.ac.cam.mcksj.front;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import uk.ac.cam.mcksj.WeatherState;
import java.text.DecimalFormat;

public class TemperatureNode extends WeatherNode {
    private Label text;
    private Thermometer thermometer;

    public TemperatureNode(WeatherState state) {
        //Set up thermometer and text
        thermometer = new Thermometer();
        text = new Label();
        text.setFont(new Font("Arial Black", 40));
        update(state);

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(thermometer, text);

        this.getChildren().add(box);
    }

    @Override
    public void update(WeatherState state) {
        //Update the text to match the temperature within the current state, and update thermometer
        text.setText(new DecimalFormat("#°").format(state.getTemperature()));
        thermometer.update(state.getTemperature());
    }
}
