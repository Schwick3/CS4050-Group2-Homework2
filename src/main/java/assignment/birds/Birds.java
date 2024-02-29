package assignment.birds;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Group 2 - Kene, Isaiah, Skylar
 * @author Ouda
 *
 * Implemented and utilized a dictionary to store a collection of
 * languages and displayed their information on a GUI using JavaFX. The
 * GUI allows for operations like searching for languages in the collection,
 * playing their sounds, traversing to the next or previous languages, etc.
 */
public class Birds extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("birds-view.fxml"));

        Scene scene = new Scene(root);

        stage.getIcons().add(new Image("file:src/main/resources/assignment/birds/images/UMIcon.png"));
        stage.setTitle("Birds Portal");

        stage.setScene(scene);
        stage.show();


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
