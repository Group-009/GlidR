package uk.ac.cam.mcksj.front;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import uk.ac.cam.mcksj.WeatherState;

import java.text.DecimalFormat;


public class RainNode extends WeatherNode {
    private Label text;
    private ImageView img;
    private Image cloud = new Image("uk/ac/cam/mcksj/img/cloud.png");

    public RainNode(WeatherState state) {
        //Set up the text, image and layout
        text = new Label();
        text.setFont(new Font("Arial Black", 20));
        img = new ImageView(cloud);
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
        //Update the rain probability
        text.setText(new DecimalFormat("#%").format(state.getRain()));
    }
}
