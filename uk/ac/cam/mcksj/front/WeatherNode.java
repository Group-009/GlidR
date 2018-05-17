package uk.ac.cam.mcksj.front;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import uk.ac.cam.mcksj.WeatherState;

import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public abstract class WeatherNode extends Pane {
    private Image backgroundImage = new Image("uk/ac/cam/mcksj/img/nodeBackground.png");

    WeatherNode(){
        this.setBackground(new Background(new BackgroundImage(backgroundImage, NO_REPEAT, NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    public abstract void update(WeatherState state);
}