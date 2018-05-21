package uk.ac.cam.mcksj.front;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;
import uk.ac.cam.mcksj.back.Backend;
import uk.ac.cam.mcksj.back.NoWeatherDataException;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

public class WeatherApplication extends Application {
    private SettingsPage settingsPage;
    private HomePage homePage;

    private Scene mainScene;
    private Scene settingsScene;

    private Calendar calendar = Calendar.getInstance();

    private Middle weatherInterface;

    private WeekDay currentWeekDay;
    private int currentDayIndex;
    private int currentTime;

    private Stage primaryStage;
    private Scene retryScene;

    //To load the app on the current day and time
    private void updateDayTime() {
        currentTime = calendar.get(Calendar.HOUR_OF_DAY);
        currentDayIndex = (calendar.get(Calendar.DAY_OF_WEEK)+5)%7;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                currentWeekDay = WeekDay.SUNDAY;
                break;
            case 2:
                currentWeekDay = WeekDay.MONDAY;
                break;
            case 3:
                currentWeekDay = WeekDay.TUESDAY;
                break;
            case 4:
                currentWeekDay = WeekDay.WEDNESDAY;
                break;
            case 5:
                currentWeekDay = WeekDay.THURSDAY;
                break;
            case 6:
                currentWeekDay = WeekDay.FRIDAY;
                break;
            case 7:
                currentWeekDay = WeekDay.SATURDAY;
                break;
            default:
                currentWeekDay = WeekDay.SUNDAY;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        updateDayTime();

        //Retry button
        Pane retryRoot = new Pane();
        retryRoot.setStyle("-fx-background-color: #"+ColourScheme.LIGHT_GREY);

        Text retryText = new Text();
        retryText.setText("RETRY");
        retryText.setFill(Color.WHITE);

        Text messageText = new Text();
        messageText.setText("No internet connection");
        messageText.setFill(Color.WHITE);
        messageText.setLayoutY(200);
        messageText.setLayoutX(180);

        StackPane retryButton = new StackPane();
        retryButton.setStyle("-fx-background-color: #"+ColourScheme.MIDDLE_GREY);
        retryButton.setPrefSize(50,50);
        retryButton.setAlignment(Pos.CENTER);
        retryButton.getChildren().add(retryText);
        retryButton.setLayoutX(215);
        retryButton.setLayoutY(300);
        retryButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                retryButton.setStyle("-fx-background-color: #"+ColourScheme.DARK_GREY);
            }
        });
        retryButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                retryButton.setStyle("-fx-background-color: #"+ColourScheme.MIDDLE_GREY);
                initiate();
            }
        });

        //stage setup
        primaryStage.setTitle("test");
        primaryStage.setResizable(false);
        //without this the window itself isn't 800x480 for me, I have no idea why
        primaryStage.setMinHeight(829);
        primaryStage.setMaxWidth(486);

        retryRoot.getChildren().add(retryButton);
        retryRoot.getChildren().add(messageText);
        retryRoot.setMinSize(400,800);
        retryScene = new Scene(retryRoot, 480, 800);

        initiate();
        primaryStage.show();
    }

    private void initiate() {
        //instantiate weatherInterface
        //Location for Cambridge - in real app would use GPS module
        try {
            weatherInterface = new Backend(52.208816, 0.117754);

            //Main screen
            homePage = new HomePage(primaryStage, currentTime, calendar,weatherInterface);
            mainScene = homePage.getMainScene();

            //Settings screen
            settingsPage = new SettingsPage(primaryStage, homePage, weatherInterface);
            settingsScene = settingsPage.getSettingsScene();

            //make the settings button work on the main screen
            homePage.setSettingsScene(settingsScene);


            primaryStage.setScene(mainScene);
        } catch (IOException | NoWeatherDataException e) {
            primaryStage.setScene(retryScene);
        }
    }

    public void updateNodes() {
        homePage.updateNodes();
    }

    //launch application
    public static void main(String[] args) {
        launch(args);
    }
}
