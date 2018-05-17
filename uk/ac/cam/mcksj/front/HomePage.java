package uk.ac.cam.mcksj.front;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;
import uk.ac.cam.mcksj.back.Backend;

import java.util.Calendar;
import java.util.LinkedList;

public class HomePage {

    private double backgroundChangeThreshold = 0.5;

    private LinkedList<WeatherNode> weatherNodes = new LinkedList<>();

    private Calendar calendar = Calendar.getInstance();

    private WeekdayButton[] weekdayPanes = new WeekdayButton[7];
    private int currentDayPane = 0;

    //up to date hour for greying out invalid times
    private int currentHour;

    private TimeButton[] timePanes = new TimeButton[24];
    private int currentTimePane;

    private Scene mainScene;
    private Scene settingsScene;

    private Middle weatherInteface;
    private WeatherState focusState;

    public HomePage(Stage primaryStage, int currentHour, Calendar calendar, Middle weatherInterface) {
        this.weatherInteface = weatherInterface;

        focusState = weatherInterface.getWeather(0,currentHour);
        //grid for days of the week
        GridPane dayGridPane = new GridPane();
        dayGridPane.setLayoutY(800-68);

        //add weekday buttons to weekday grid
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i<6; i++) {
            WeekdayButton button = new WeekdayButton(i, dayOfWeek);
            button.getPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //update weather with API call using current time and button.getWeekDay() and update nodes
                    focusState = weatherInterface.getWeather(button.getBarIndex(),currentTimePane);
                    updateNodes();



                    //change colour of weekday buttons
                    if (currentDayPane == 0 && button.getBarIndex() != 0) {
                        //colour buttons brown
                        for (int i = 0; i < 24; i++) {
                            timePanes[i].setColor(ColourScheme.DARK_BROWN);
                        }
                        timePanes[currentTimePane].setColor(ColourScheme.LIGHT_BROWN);
                    } else if (currentDayPane != 0 && button.getBarIndex() == 0) {
                        //colour grey
                        for (int i = 0; i < currentHour; i++) {
                            timePanes[i].setColor(ColourScheme.DARK_GREY);
                        }

                        //select least valid time
                        if (currentTimePane < currentHour) currentTimePane = currentHour;
                        focusState = weatherInterface.getWeather(currentDayPane, currentHour);
                        timePanes[currentHour].setColor(ColourScheme.LIGHT_BROWN);
                        updateNodes();
                    }

                    weekdayPanes[currentDayPane].setColor(ColourScheme.DARK_BROWN);
                    button.setColor(ColourScheme.LIGHT_BROWN);
                    currentDayPane = button.getBarIndex();
                }
            });
            dayGridPane.add(button.getPane(),i,0);
            weekdayPanes[i] = button;
        }

        //scrolling times pane
        ScrollPane timeBarPane = new ScrollPane();
        timeBarPane.setMinSize(480, 68);
        timeBarPane.setMaxSize(480,68);
        timeBarPane.setLayoutX(0);
        timeBarPane.setLayoutY(664);

        //to place inside the scrollPane
        GridPane timeGrid = new GridPane();

        //add panes to timeGrid with mouseClick event handlers to move the scrollPane
        currentTimePane = currentHour;
        for (int i=0; i<24; i++) {
            TimeButton time = new TimeButton(i, currentHour);
            timeGrid.add(time.getPane(),i,0);
            timePanes[i] = time;
            time.getPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    if (!(currentDayPane == 0 && !time.isValidTime())) {
                        //update time by querying API with selectedTime and current Weekday
                        int selectedTime = time.getTime();
                        focusState = weatherInterface.getWeather(currentDayPane, selectedTime);
                        updateNodes();

                        final Timeline timeline = new Timeline();
                        final KeyValue kv = new KeyValue(timeBarPane.hvalueProperty(), ((time.getTime() - 3.0)) * (1.0 / 17.0));
                        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
                        timeline.getKeyFrames().add(kf);

                        time.setColor(ColourScheme.LIGHT_BROWN);
                        timePanes[currentTimePane].setColor(ColourScheme.DARK_BROWN);
                        currentTimePane = selectedTime;
                        timeline.play();
                    }
                }
            });

        }

        //scroll to current hour
        final Timeline timeline = new Timeline();
        final KeyValue kv = new KeyValue(timeBarPane.hvalueProperty(), ((timePanes[currentHour].getTime()-3.0))*(1.0/17.0));
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        timeBarPane.setContent(timeGrid);
        timeBarPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timeBarPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //middle grid of weather conditions
        GridPane quadGrid = new GridPane();
        //quadGrid.setGridLinesVisible(true);
        quadGrid.setLayoutX(60);
        quadGrid.setLayoutY(124);

        //Insert weather nodes
        WeatherNode rating = new RatingNode(focusState);
        quadGrid.add(rating, 0,0,2,1);
        weatherNodes.add(rating);

        WeatherNode temperature = new TemperatureNode(focusState);
        quadGrid.add(temperature, 0,1);
        weatherNodes.add(temperature);

        WeatherNode wind = new WindNode(focusState);
        quadGrid.add(wind, 0,2);
        weatherNodes.add(wind);

        WeatherNode rain = new RainNode(focusState);
        quadGrid.add(rain, 1,1);
        weatherNodes.add(rain);

        WeatherNode visibility = new VisibilityNode(focusState);
        quadGrid.add(visibility, 1,2);
        weatherNodes.add(visibility);


        //pane for the settings icon
        Pane settingsPane = new Pane();
        settingsPane.setLayoutX(0);
        settingsPane.setLayoutY(0);
        settingsPane.setMinSize(68,68);
        settingsPane.setMaxSize(68,68);
        settingsPane.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/settings_option3_on.png');");
        settingsPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(settingsScene);
            }
        });

        //Border Panes
        Pane timeDayBorder = new Pane();
        timeDayBorder.setLayoutX(0);
        timeDayBorder.setLayoutY(800-69);
        timeDayBorder.setPrefSize(480,2);
        timeDayBorder.setStyle("-fx-background-color: #000");

        Pane timeUpBorder = new Pane();
        timeUpBorder.setLayoutX(0);
        timeUpBorder.setLayoutY(800-69*2);
        timeUpBorder.setPrefSize(480,2);
        timeUpBorder.setStyle("-fx-background-color: #000");

        //TREE BUILD
        //pane not root group so background can be set
        Pane root = new Pane();
        root.getChildren().add(dayGridPane);
        root.getChildren().add(quadGrid);
        root.getChildren().add(timeBarPane);
        root.getChildren().add(settingsPane);
        //root.setStyle("-fx-background-color: #"+ColourScheme.LIGHT_GREY);
        if (selectedWeather.getRain() > backgroundChangeThreshold){
            root.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/background_storm.png');");
        } else {
            root.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/background_sunny.png');");
        }

        Group borders = new Group();
        borders.getChildren().add(timeDayBorder);
        borders.getChildren().add(timeUpBorder);
        root.getChildren().add(borders);

        mainScene = new Scene(root, 480, 800);
        mainScene.getStylesheets().add("uk/ac/cam/mcksj/front/styles.css");

        updatePage();
    }

    public void updateNodes() {
        for (WeatherNode node : weatherNodes) {
            node.update(focusState);
        }
    }

    public void updatePage() {
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        for (int i = 0; i < currentHour; i++) {
            timePanes[i].setValid(false);
        }
        for (int i = currentHour; i < 24; i++) {
            timePanes[i].setValid(true);
        }

    }

    public Scene getMainScene() {
        return mainScene;
    }

    public void setSettingsScene(Scene settingsScene) {
        this.settingsScene = settingsScene;
    }
}
