package uk.ac.cam.mcksj.front;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    public void start(Stage primaryStage) throws Exception {

        updateDayTime();

        //instantiate weatherInterface
        weatherInterface = new Backend();

        //TODO lat and long in constructor
        //Location for Cambridge - in real app would use GPS module
        weatherInterface.changeLocation(52.208816, 0.117754);

        //for debugging using background measurements image
        //root.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/scaleMockUp.png');");

        //Main screen
        homePage = new HomePage(primaryStage, currentTime, calendar,weatherInterface);
        mainScene = homePage.getMainScene();

        //Settings screen
        settingsPage = new SettingsPage(primaryStage, mainScene);
        settingsScene = settingsPage.getSettingsScene();

        //make the settings button work on the main screen
        homePage.setSettingsScene(settingsScene);

        //stage setup
        primaryStage.setTitle("test");
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public void updateNodes() {
        homePage.updateNodes();
    }

    //launch application
    public static void main(String[] args) {
        launch(args);
    }
}
