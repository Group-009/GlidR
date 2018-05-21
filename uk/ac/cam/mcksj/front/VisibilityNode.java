package uk.ac.cam.mcksj.front;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.ac.cam.mcksj.WeatherState;

import java.text.DecimalFormat;


public class VisibilityNode extends WeatherNode {
    private Label text;
    private ImageView img;
    private Image eye = new Image("uk/ac/cam/mcksj/img/eye.png");

    public VisibilityNode(WeatherState state) {
        //Setup background, text and layout
        BackgroundImage bg = new BackgroundImage(new Image("uk/ac/cam/mcksj/img/nodeBackground.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.setBackground(new Background(bg));
        text = new Label();
        text.setFont(new Font("Arial Black", 20));
        text.setTextFill(Color.WHITE);
        img = new ImageView(eye);
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

    /**
     * Updates the visibility display
     *
     * @param state new weather state
     */
    @Override
    public void update(WeatherState state) {
        //Update the text to match current visibility
        text.setText(new DecimalFormat("#%").format(state.getVisibility()/100));
    }
}
