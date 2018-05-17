package uk.ac.cam.mcksj.front;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import uk.ac.cam.mcksj.WeatherState;
import java.text.DecimalFormat;

public class TemperatureNode extends WeatherNode {
    private Label text;
    private Thermometer thermometer;

    public TemperatureNode(WeatherState state) {
        //Set up background, thermometer and text
        BackgroundImage bg = new BackgroundImage(new Image("uk/ac/cam/mcksj/img/nodeBackground.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.setBackground(new Background(bg));
        thermometer = new Thermometer();
        text = new Label();
        text.setFont(new Font("Arial Black", 40));
        text.setTextFill(Color.WHITE);
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
