package pl.kreft.rafal.thegameoflife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Game of Life");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}