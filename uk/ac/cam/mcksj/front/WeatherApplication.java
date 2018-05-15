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
import uk.ac.cam.mcksj.WeatherState;
import uk.ac.cam.mcksj.WeekDay;

import java.util.Calendar;
import java.util.LinkedList;

public class WeatherApplication extends Application {
    private SettingsPage settingsPage;
    private HomePage homePage;

    private Scene mainScene;
    private Scene settingsScene;

    @Override
    public void start(Stage primaryStage) throws Exception {


        //root.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/scaleMockUp.png');");

        //Main screen
        homePage = new HomePage(primaryStage);
        mainScene = homePage.getMainScene();

        //Settings screen
        settingsPage = new SettingsPage(primaryStage, mainScene);
        settingsScene = settingsPage.getSettingsScene();

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
