package uk.ac.cam.mcksj.front;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import uk.ac.cam.mcksj.WeatherState;

import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public abstract class WeatherNode extends Pane {
    public abstract void update(WeatherState state);
}