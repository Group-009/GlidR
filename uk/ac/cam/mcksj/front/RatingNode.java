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
    private Rotate rotation;
    private ImageView[] stars;

    public RatingNode(WeatherState state) {
        //Setup gauge background
        BackgroundImage bg = new BackgroundImage(new Image("uk/ac/cam/mcksj/img/gaugeBackground.png"),  NO_REPEAT,
                NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        this.setBackground(new Background(bg));

        //Set up line for gauge needle.
        rating = 0;
        this.setMinHeight(180);
        this.setMaxHeight(180);
        line = new Line(180, 160, 50, 144);
        line.setStrokeWidth(4);
        line.setStrokeType(StrokeType.OUTSIDE);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStroke(Color.WHITE);

        //Change rotation such that it pivots around the start of the line
        rotation = new Rotate();
        rotation.pivotXProperty().bind(line.startXProperty());
        rotation.pivotYProperty().bind(line.startYProperty());
        line.getTransforms().add(rotation);

        //Create the 5 stars in the background
        stars = new ImageView[5];

        for (int i = 0; i < 5; i++){
            stars[i] = new ImageView(new Image("uk/ac/cam/mcksj/img/yellowStar.png"));
            stars[i].setY(102);
            stars[i].setX(78+41*i);
            this.getChildren().add(stars[i]);
        }


        update(state);


        this.getChildren().add(line);
    }

    /**
     * Updates the rating display
     *
     * @param state new weather state
     */
    @Override
    public void update(WeatherState state) {
        //This function animates the needle to go to the next rating.
        KeyValue kv = new KeyValue(rotation.angleProperty(), 173 / 5 * state.getStarRating(), Interpolator.EASE_BOTH);
        final KeyFrame kf = new KeyFrame(Duration.millis(750), kv);
        Timeline timeLine = new Timeline(kf);
        timeLine.play();

        //update the number of stars being shown, hide the rest
        //there are 6 possible states, from 0 to 5 stars. Thus
        for (int i = 0; i < 5; i++){
            if (state.getStarRating() >= i+1){
                stars[i].setVisible(true);
            } else {
                stars[i].setVisible(false);
            }
        }
    }
}
