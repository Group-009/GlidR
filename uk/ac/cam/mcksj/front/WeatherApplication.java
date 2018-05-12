package uk.ac.cam.mcksj.front;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;

import java.util.Calendar;
import java.util.LinkedList;

public class WeatherApplication extends Application {
    private LinkedList<WeatherNode> weatherNodes = new LinkedList<>();
    //TODO: don't define this here u donut
    private WeatherState selectedWeather = new WeatherState(2,18,5, 0.12f,WeekDay.SATURDAY, 15);

    private Calendar calendar = Calendar.getInstance();

    private WeekdayButton[] weekdayPanes = new WeekdayButton[7];
    private int currentPane = 0;

    private TimeButton[] timePanes = new TimeButton[24];
    private int currentTimePane;

    private Scene mainScene;
    private Scene settingsScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //grid for days of the week
        GridPane dayGridPane = new GridPane();
        dayGridPane.setLayoutY(800-68);

        //add weekday buttons to weekday grid
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i<7; i++) {
            WeekdayButton button = new WeekdayButton(i, dayOfWeek);
            button.getPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    weekdayPanes[currentPane].setColor(ColourScheme.DARK_BROWN);
                    button.setColor(ColourScheme.LIGHT_BROWN);
                    currentPane = button.getDay();
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
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        currentTimePane = hour;
        System.out.println(hour);
        for (int i=0; i<24; i++) {
            TimeButton time = new TimeButton(i, hour);
            timeGrid.add(time.getPane(),i,0);
            timePanes[i] = time;
            time.getPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //TODO: update time
                    selectedWeather = new WeatherState(5,30,5,0.12f,WeekDay.SATURDAY,10);
                    updateNodes();

                    final Timeline timeline = new Timeline();
                    final KeyValue kv = new KeyValue(timeBarPane.hvalueProperty(), ((time.getTime()-3.0))*(1.0/17.0));
                    final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
                    timeline.getKeyFrames().add(kf);

                    time.setColor(ColourScheme.LIGHT_BROWN);
                    timePanes[currentTimePane].setColor(ColourScheme.DARK_BROWN);
                    currentTimePane = time.getTime();
                    timeline.play();
                }
            });

        }

        //scroll to current hour
        final Timeline timeline = new Timeline();
        final KeyValue kv = new KeyValue(timeBarPane.hvalueProperty(), ((timePanes[hour].getTime()-3.0))*(1.0/17.0));
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        timeBarPane.setContent(timeGrid);
        timeBarPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timeBarPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //middle grid of weather conditions
        GridPane quadGrid = new GridPane();
        quadGrid.setGridLinesVisible(true);
        quadGrid.setLayoutX(60);
        quadGrid.setLayoutY(124);

        //Insert weather nodes
        WeatherNode rating = new RatingNode(selectedWeather);
        quadGrid.add(rating, 0,0,2,1);
        weatherNodes.add(rating);

        WeatherNode temperature = new TemperatureNode(selectedWeather);
        quadGrid.add(temperature, 0,1);
        weatherNodes.add(temperature);

        WeatherNode wind = new WindNode(selectedWeather);
        quadGrid.add(wind, 0,2);
        weatherNodes.add(wind);

        WeatherNode rain = new RainNode(selectedWeather);
        quadGrid.add(rain, 1,1);
        weatherNodes.add(rain);

        WeatherNode visibility = new VisibilityNode(selectedWeather);
        quadGrid.add(visibility, 1,2);
        weatherNodes.add(visibility);


        //pane for the settings icon
        Pane settingsPane = new Pane();
        settingsPane.setLayoutX(0);
        settingsPane.setLayoutY(0);
        settingsPane.setMinSize(68,68);
        settingsPane.setMaxSize(68,68);
        settingsPane.setStyle("-fx-background-color: #111");
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

        //TREE BUILD
        //pane not root group so background can be set
        Pane root = new Pane();
        root.getChildren().add(dayGridPane);
        root.getChildren().add(quadGrid);
        root.getChildren().add(timeBarPane);
        root.getChildren().add(settingsPane);

        Group borders = new Group();
        borders.getChildren().add(timeDayBorder);
        root.getChildren().add(borders);

        //root.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/scaleMockUp.png');");

        //MAIN SCREEN
        mainScene = new Scene(root, 480, 800);
        mainScene.getStylesheets().add("uk/ac/cam/mcksj/front/styles.css");

        //SETTINGS SCREEN
        Pane settingsRoot = new Pane();

        Pane settingsClose = new Pane();
        settingsClose.setMinSize(68,68);
        settingsClose.setLayoutX(0);
        settingsClose.setLayoutY(0);
        settingsClose.setStyle("-fx-background-color: #111");
        settingsClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(mainScene);
            }
        });

        //Text box
        TextField textInput = new TextField();
        textInput.setPrefWidth(100);
        textInput.setLayoutX(240-textInput.getPrefWidth()/2.0);
        textInput.setLayoutY(300);

        //Set Location Button
        Pane locButton = new Pane();
        locButton.setPrefSize(68,68);
        locButton.setLayoutX(240-34);
        locButton.setLayoutY(360);
        locButton.setStyle("-fx-background-color: #000");
        locButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(textInput.getCharacters());
                //TODO: UPDATE WEATHER INFORMATION - remember to sanitise input, provide confirmation of click, display current loc on main screen?
                locButton.setStyle("-fx-background-color: #362");
            }
        });

        locButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                locButton.setStyle("-fx-background-color: #000");
            }
        });

        settingsRoot.getChildren().add(settingsClose);
        settingsRoot.getChildren().add(textInput);
        settingsRoot.getChildren().add(locButton);
        settingsScene = new Scene(settingsRoot, 480, 800);

        //stage setup
        primaryStage.setTitle("test");
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void updateNodes() {
        for (WeatherNode node : weatherNodes) {
            node.update(selectedWeather);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
