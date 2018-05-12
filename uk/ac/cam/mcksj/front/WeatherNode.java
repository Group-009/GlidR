package uk.ac.cam.mcksj.front;

import javafx.scene.layout.Pane;
import uk.ac.cam.mcksj.WeatherState;

public abstract class WeatherNode extends Pane {
    public abstract void update(WeatherState state);
}