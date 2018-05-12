package uk.ac.cam.mcksj.front;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import uk.ac.cam.mcksj.WeatherState;

import java.text.DecimalFormat;


public class TemperatureNode extends WeatherNode {
    private Label text;
    private ImageView img;
    //TODO: Thermometer at different stages of heat
    private Image thermometer = new Image("uk/ac/cam/mcksj/img/thermMax.png");

    public TemperatureNode(WeatherState state) {
        //Set up image and text
        text = new Label();
        text.setFont(new Font("Arial Black", 40));
        img = new ImageView();
        img.setFitHeight(150);
        img.setPreserveRatio(true);
        update(state);

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(img, text);
        HBox.setMargin(img, new Insets(15,0,0,0));

        this.getChildren().add(box);
    }

    @Override
    public void update(WeatherState state) {
        //Update the text to match the temperature within the current state
        text.setText(new DecimalFormat("#°").format(state.getTemperature()));
        img.setImage(thermometer);
    }
}
