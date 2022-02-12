package pl.kreft.rafal.thegameoflife;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainController {

    @FXML
    private Pane pane;

    private int size = 700;
    private int spots = 100;
    private int squareSize = size / spots;

    @FXML
    public void initialize(){
        for (int i=0; i < size; i+=squareSize){
            for (int j=0; j < size; j+=squareSize){
                Rectangle r = new Rectangle(i, j, squareSize, squareSize);
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                pane.getChildren().add(r);
            }
        }
    }

}