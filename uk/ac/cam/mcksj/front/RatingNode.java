package uk.ac.cam.mcksj.front;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import uk.ac.cam.mcksj.WeatherState;

import java.text.DecimalFormat;


public class RatingNode extends WeatherNode {
    private Line line;
    private int rating;

    public RatingNode(WeatherState state) {
        //Set up line for gauge needle.
        rating = 0;
        line = new Line(180, 180, 0, 180);
        line.setStrokeWidth(4);
        line.setStrokeType(StrokeType.OUTSIDE);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setOnMouseClicked(e -> wobble());
        update(state);

        this.getChildren().add(line);
    }

    @Override
    public void update(WeatherState state) {
        //This function animates the needle to go to the next rating.
        boolean larger = rating < state.getStarRating();
        long start = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Delta is the angle by which the needle is moved.
                double delta = (now - start) / 1000000000.0;
                //If needle in correct position, stop the function.
                if ((larger && rating + delta > state.getStarRating()) |
                        (!larger && rating - delta < state.getStarRating())) {
                    rating = state.getStarRating();
                    stop();
                    System.out.println("STOPPING gauge animation");
                } else {
                    double newRating;
                    if (larger) newRating = rating + delta;
                    else newRating = rating - delta;
                    line.setEndX(180 * (1 - Math.cos(Math.PI * (newRating) / 5.0)));
                    line.setEndY(180 * (1 - Math.sin(Math.PI * (newRating) / 5.0)));
                }
            }
        };
        System.out.println("Stating gauge animation");
        timer.start();
    }

    private void wobble(){
        //Fun little feature for when you knock the gauge!
        long start = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double delta = (now - start) / 1000000.0;
                if (delta > 2 * Math.PI){
                    line.setEndX(180 * (1 - Math.cos(Math.PI * (rating) / 5.0)));
                    line.setEndY(180 * (1 - Math.sin(Math.PI * (rating) / 5.0)));
                    stop();
                }
                line.setEndX(180 * (1 - Math.cos(Math.PI * (rating) / 5.0 + Math.sin(delta) / 30.0)));
                line.setEndY(180 * (1 - Math.sin(Math.PI * (rating) / 5.0 + Math.sin(delta) / 30.0)));
            }

        };
        timer.start();
    }

}
