package uk.ac.cam.mcksj.front;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

public class Thermometer extends StackPane {
    private float minTemp = 0;
    private float maxTemp = 35;
    private Line line;
    private Circle circle;
    //These should not be changed without altering the circle and line position vals.
    private final int width = 83;
    private final int height = 180;

    public Thermometer() {
        //Setup size
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        //Set up all the components of the thermometer
        ImageView background = new ImageView("uk/ac/cam/mcksj/img/thermMax.png");

        Pane drawPane = new Pane();
        drawPane.setMinSize(width, height);
        drawPane.setMaxSize(width, height);

        circle = new Circle(43,131,12, Color.RED);
        line = new Line(43, 131, 43, 43);
        line.setStroke(Color.RED);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStrokeWidth(7);
        drawPane.getChildren().addAll(circle, line);

        this.getChildren().addAll(background, drawPane);
    }


    /**
     * Update the thermometer with a new temperature
     *
     * @param degrees new temperature, in degrees celsius
     */
    public void update(float degrees) {
        //Bounds the temperature as a double between 0 and 1
        double ratio = Math.max(0, Math.min(1,  (degrees - minTemp) / (double) (maxTemp - minTemp)));

        final Timeline timeline = new Timeline();

        //Change length of line in the thermometer
        final KeyValue lenkv = new KeyValue(line.endYProperty(), 110 - ratio * 65);
        final KeyFrame lenkf = new KeyFrame(Duration.millis(750), lenkv);


        //Start the transitions
        timeline.getKeyFrames().addAll(lenkf);
        timeline.play();
    }
}
