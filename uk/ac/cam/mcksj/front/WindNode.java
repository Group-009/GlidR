package uk.ac.cam.mcksj.front;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.ac.cam.mcksj.WeatherState;

import java.text.DecimalFormat;


public class WindNode extends WeatherNode {
    private Label text;
    private ImageView img;
    private Image wind = new Image("uk/ac/cam/mcksj/img/wind.png");

    public WindNode(WeatherState state) {
        //setup the background, text and image, and lay them out vertically
        BackgroundImage bg = new BackgroundImage(new Image("uk/ac/cam/mcksj/img/nodeBackground.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.setBackground(new Background(bg));
        text = new Label();
        text.setFont(new Font("Arial Black", 20));
        text.setTextFill(Color.WHITE);
        img = new ImageView(wind);
        img.setFitWidth(100);
        img.setPreserveRatio(true);
        update(state);

        VBox box = new VBox();
        box.setPrefWidth(180);
        box.setPrefHeight(180);
        box.getChildren().addAll(img, text);
        box.setAlignment(Pos.CENTER);

        this.getChildren().add(box);
    }

    @Override
    public void update(WeatherState state) {
        //Update wind speed
        text.setText(new DecimalFormat("#.# kn").format(state.getTemperature()));
    }
}
