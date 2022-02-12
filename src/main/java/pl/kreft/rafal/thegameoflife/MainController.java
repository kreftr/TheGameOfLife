package pl.kreft.rafal.thegameoflife;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MainController {

    @FXML
    private Pane pane;

    private int size = 700;
    private int spots = 100;
    private int squareSize = size / spots;

    private ArrayList<Cell> cells = new ArrayList<>();


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

        for (int i=0; i < 5; i++){
            Circle c = new Circle();
            double radius = squareSize / 3.0;
            int x = squareSize / 2 + squareSize * (int)(Math.random() * spots);
            int y = squareSize / 2 + squareSize * (int)(Math.random() * spots);
            Cell cell = new Cell(x, y, radius, c);
            cells.add(cell);
            pane.getChildren().add(c);
            cell.draw();
        }
    }

}