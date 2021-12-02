package Model.td;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Menu.fxml")));
        primaryStage.setTitle("All2You by TeamDesk (Beta)");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        primaryStage.setResizable(false);

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/" + fxml + ".fxml"));
        return fxmlLoader.load();

    }
    public static boolean readInt(String text) {
       double d= 0;
        try {
            d =Double.parseDouble(text) ;
            if(d < 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
        return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}