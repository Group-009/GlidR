package uk.ac.cam.mcksj.front;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import uk.ac.cam.mcksj.Middle;
import uk.ac.cam.mcksj.back.NoWeatherDataException;

import java.io.IOException;

public class SettingsPage {

    private Scene settingsScene;
    private Middle weatherInterface;
    private Image mapImage = new Image("uk/ac/cam/mcksj/img/map.png");
    private double mapAspectRatio = mapImage.getWidth()/mapImage.getHeight();

    /**
     * @param primaryStage Primary stage for switching scene to HomePage
     * @param homePage Reference to return to the HomePage
     * @param weatherInterface For updating location information
     */
    public SettingsPage(Stage primaryStage, HomePage homePage, Middle weatherInterface, Scene retryScene) {
        this.weatherInterface = weatherInterface;

        //SETTINGS SCREEN
        Pane settingsRoot = new Pane();

        Pane settingsClose = new Pane();
        settingsClose.setMinSize(68,68);
        settingsClose.setLayoutX(0);
        settingsClose.setLayoutY(0);
        settingsClose.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/settings_option3_on.png');");
        settingsClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(homePage.getMainScene());
            }
        });

        //Latitude box
        TextField latBox = new TextField();
        latBox.setPrefWidth(150);
        latBox.setLayoutX(240-latBox.getPrefWidth()-10);
        latBox.setLayoutY(160);


        //Longitude box
        TextField longBox = new TextField();
        longBox.setPrefWidth(150);
        longBox.setLayoutX(240+10);
        longBox.setLayoutY(160);

        //Message box for confirmation
        Text messageText = new Text();
        messageText.setWrappingWidth(400);
        messageText.setLayoutX(40);
        messageText.setLayoutY(680);
        messageText.setFill(Color.WHITE);
        messageText.setTextAlignment(TextAlignment.CENTER);

        //Set Location Button
        Pane locButton = new Pane();
        locButton.setPrefSize(320,68);
        locButton.setLayoutX(240-locButton.getPrefWidth()/2);
        locButton.setLayoutY(700);
        locButton.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/settings_submit_button.png');");
        locButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String latInput = latBox.getCharacters().toString();
                String longInput = longBox.getCharacters().toString();


                try {
                    double latitude = Double.parseDouble(latInput);
                    double longitude = Double.parseDouble(longInput);
                    if (latitude < -90 || latitude > 90) throw new LocationFormatException("Latitude out of bounds");
                    if (longitude < -180 || longitude > 180) throw new LocationFormatException("Longitude out of bounds");

                    //call API for location update and handle false return
                    if (!weatherInterface.changeLocation(latitude, longitude)) throw new LocationFormatException(" Invalid location");

                    try {
                        weatherInterface.updateWeather();
                        //update the dials
                        homePage.updateNodes();
                    } catch (IOException|NoWeatherDataException e) {
                        primaryStage.setScene(retryScene);
                    }
                    messageText.setText("Location updated to: ("+latitude+", "+longitude+")");

                } catch (NumberFormatException e) {
                    messageText.setText("Please input two numbers, one for latitude and one for longitude");
                    latBox.setText("");
                    longBox.setText("");
                } catch (LocationFormatException e) {
                    messageText.setText("Please enter correct latitude and longitude: " + e.getMessage());
                    latBox.setText("");
                    longBox.setText("");
                }

                locButton.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/settings_submit_button_pressed.png');");
            }
        });

        locButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                locButton.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/settings_submit_button.png');");
            }
        });

        settingsRoot.getChildren().add(settingsClose);
        settingsRoot.getChildren().add(latBox);
        settingsRoot.getChildren().add(longBox);
        settingsRoot.getChildren().add(locButton);
        settingsRoot.getChildren().add(messageText);
        settingsRoot.setStyle("-fx-background-image: url('uk/ac/cam/mcksj/img/settings_background.png');");
        settingsScene = new Scene(settingsRoot, 480, 800);


        // Iteractive map for selecting latitude and longitude

        ImageView map = new ImageView(mapImage);
        map.setPreserveRatio(true);
        map.setFitWidth(320);
        map.setLayoutX(240-map.getFitWidth()/2);
        map.setLayoutY(240);
        map.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                latBox.setText(Double.toString(59.0-9.0*event.getY()*mapAspectRatio/map.getFitWidth() ));
                longBox.setText(Double.toString(-11.0+13.0*event.getX()/map.getFitWidth() ));
            }
        });


        settingsRoot.getChildren().add(map);
    }

    public Scene getSettingsScene() {
        return settingsScene;
    }
}
