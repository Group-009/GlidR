package uk.ac.cam.mcksj.front;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SettingsPage {

    Scene settingsScene;

    public SettingsPage(Stage primaryStage, Scene mainScene) {
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

        //Latitude box
        TextField latBox = new TextField();
        latBox.setPrefWidth(50);
        latBox.setLayoutX(240-latBox.getPrefWidth()-10);
        latBox.setLayoutY(300);

        //Longitude box
        TextField longBox = new TextField();
        longBox.setPrefWidth(50);
        longBox.setLayoutX(240+10);
        longBox.setLayoutY(300);

        //Message box for confirmation
        Text messageText = new Text();
        messageText.setWrappingWidth(400);
        messageText.setLayoutX(40);
        messageText.setLayoutY(250);
        messageText.setTextAlignment(TextAlignment.CENTER);

        //Set Location Button
        Pane locButton = new Pane();
        locButton.setPrefSize(68,68);
        locButton.setLayoutX(240-34);
        locButton.setLayoutY(360);
        locButton.setStyle("-fx-background-color: #"+ColourScheme.MIDDLE_GREY);
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

                    //TODO call API and handle false return
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

                locButton.setStyle("-fx-background-color: #"+ColourScheme.DARK_GREY);
            }
        });

        locButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                locButton.setStyle("-fx-background-color: #"+ColourScheme.MIDDLE_GREY);
            }
        });

        settingsRoot.getChildren().add(settingsClose);
        settingsRoot.getChildren().add(latBox);
        settingsRoot.getChildren().add(longBox);
        settingsRoot.getChildren().add(locButton);
        settingsRoot.getChildren().add(messageText);
        settingsRoot.setStyle("-fx-background-color: #"+ColourScheme.LIGHT_GREY);
        settingsScene = new Scene(settingsRoot, 480, 800);
    }

    public Scene getSettingsScene() {
        return settingsScene;
    }
}
