package assignment.languages;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Group 2 - Kene, Isaiah, Skylar
 * @author Reference - Ouda
 *
 * This class carries out the functionality of the GUI.
 */
public class LanguagesController implements Initializable {

    @FXML
    private MenuBar mainMenu;
    @FXML
    private ImageView image;
    @FXML
    private BorderPane LanguagePortal;
    @FXML
    private Label title;
    @FXML
    private Label about;
    @FXML
    private Button play;
    @FXML
    private Button puase;
    @FXML
    private ComboBox size;
    @FXML
    private TextField name;
    Media media;
    MediaPlayer player;
    OrderedDictionary database = null;
    LanguageRecord language = null;
    int languageSize = 1;

    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    public void find() {
        DataKey key = new DataKey(this.name.getText(), languageSize);
        try {
            language = database.find(key);
            showLanguage();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    public void delete() {
        LanguageRecord previousLanguage = null;
        try {
            previousLanguage = database.predecessor(language.getDataKey());
        } catch (DictionaryException ex) {

        }
        LanguageRecord nextLanguage = null;
        try {
            nextLanguage = database.successor(language.getDataKey());
        } catch (DictionaryException ex) {

        }
        DataKey key = language.getDataKey();
        try {
            database.remove(key);
        } catch (DictionaryException ex) {
            System.out.println("Error in delete "+ ex);
        }
        if (database.isEmpty()) {
            this.LanguagePortal.setVisible(false);
            displayAlert("No more languages in the database to show");
        } else {
            if (previousLanguage != null) {
                language = previousLanguage;
                showLanguage();
            } else if (nextLanguage != null) {
                language = nextLanguage;
                showLanguage();
            }
        }
    }

    private void showLanguage() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
        String img = language.getImage();
        Image languageImage = new Image("file:src/main/resources/assignment/languages/images/" + img);
        image.setImage(languageImage);
        title.setText(language.getDataKey().getLanguageName());
        about.setText(language.getAbout());
    }

    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/assignment/languages/images/UMIcon.png"));
            stage.setTitle("Dictionary Exception");
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    public void getSize() {
        switch (this.size.getValue().toString()) {
            case "Small":
                this.languageSize = 1;
                break;
            case "Medium":
                this.languageSize = 2;
                break;
            case "Large":
                this.languageSize = 3;
                break;
            default:
                break;
        }
    }

    public void first() {
        // Write this method

        LanguageRecord initialLanguage = null;
        try {
            initialLanguage = database.smallest();
        }
        catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }

        if (database.isEmpty()) {
            this.LanguagePortal.setVisible(false);
            displayAlert("No more languages in the database to show");
        } else {
            if (initialLanguage != null) {
                language = initialLanguage;
                showLanguage();
            }
        }
    }

    public void last() {
        // Write this method

        LanguageRecord lastLanguage = null;
        try {
            lastLanguage = database.largest();
        }
        catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }

        if (database.isEmpty()) {
            this.LanguagePortal.setVisible(false);
            displayAlert("No more languages in the database to show");
        } else {
            if (lastLanguage != null) {
                language = lastLanguage;
                showLanguage();
            }
        }
    }

    public void next() {
        // Write this method;

        LanguageRecord nextLanguage = null;
        try {
            nextLanguage = database.successor(language.getDataKey());
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }

        if (database.isEmpty()) {
            this.LanguagePortal.setVisible(false);
            displayAlert("No more languages in the database to show");
        } else {
            if (nextLanguage != null) {
                language = nextLanguage;
                showLanguage();
            }
        }
    }

    public void previous() {
        // Write this method

        LanguageRecord previousLanguage = null;
        try {
            previousLanguage = database.predecessor(language.getDataKey());
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
        if (database.isEmpty()) {
            this.LanguagePortal.setVisible(false);
            displayAlert("No more languages in the database to show");
        } else {
            if (previousLanguage != null) {
                language = previousLanguage;
                showLanguage();
            }
        }
    }

    public void play() {
        String filename = "src/main/resources/assignment/languages/sounds/" + language.getSound();
        media = new Media(new File(filename).toURI().toString());
        player = new MediaPlayer(media);
        play.setDisable(true);
        puase.setDisable(false);
        player.play();
    }

    public void puase() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
    }

    public void loadDictionary() {
        Scanner input;
        int line = 0;
        try {
            String languageName = "";
            String description;
            int size = 0;
            //System.out.println(new File(".").getAbsolutePath());
            //input = new Scanner(new File("LanguagesDatabase.txt"));
            input = new Scanner(new File("LanguagesDatabase.txt"));
            while (input.hasNext()) // read until  end of file
            {
                String data = input.nextLine();
                switch (line % 3) {
                    case 0:
                        size = Integer.parseInt(data);
                        break;
                    case 1:
                        languageName = data;
                        break;
                    default:
                        description = data;
                        database.insert(new LanguageRecord(new DataKey(languageName, size), description, languageName + ".mp3", languageName + ".jpg"));
                        break;
                }
                line++;
            }
        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: LanguagesDatabase.txt");
            System.out.println(e.getMessage());
        } catch (DictionaryException ex) {
            Logger.getLogger(LanguagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.LanguagePortal.setVisible(true);
        this.first();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = new OrderedDictionary();
        size.setItems(FXCollections.observableArrayList(
                "Small", "Medium", "Large"
        ));
        size.setValue("Small");
    }

}
