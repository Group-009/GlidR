package uk.ac.cam.mcksj.front;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import uk.ac.cam.mcksj.WeatherState;

import java.text.DecimalFormat;

import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;


public class RatingNode extends WeatherNode {
    private Line line;
    private int rating;
    private Image gauge = new Image("uk/ac/cam/mcksj/img/gaugeBackground.png");
    Rotate rotation;

    public RatingNode(WeatherState state) {
        //Set up line for gauge needle.
        rating = 0;
        this.setMinHeight(180);
        this.setMaxHeight(180);
        line = new Line(180, 160, 50, 160);
        line.setStrokeWidth(4);
        line.setStrokeType(StrokeType.OUTSIDE);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStroke(Color.WHITE);

        //Change rotation such that it pivots around the start of the line
        rotation = new Rotate();
        rotation.pivotXProperty().bind(line.startXProperty());
        rotation.pivotYProperty().bind(line.startYProperty());
        line.getTransforms().add(rotation);

        update(state);

        this.setBackground(new Background(new BackgroundImage(gauge, NO_REPEAT, NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.getChildren().add(line);
    }

    @Override
    public void update(WeatherState state) {
        //This function animates the needle to go to the next rating.
        KeyValue kv = new KeyValue(rotation.angleProperty(), 180 / 5 * state.getStarRating(), Interpolator.EASE_BOTH);
        final KeyFrame kf = new KeyFrame(Duration.millis(750), kv);
        Timeline timeLine = new Timeline(kf);
        timeLine.play();

    }
}
