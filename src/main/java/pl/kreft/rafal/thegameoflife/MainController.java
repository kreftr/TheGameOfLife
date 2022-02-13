package pl.kreft.rafal.thegameoflife;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private Pane pane;
    @FXML
    private Button button;


    private int size = 700;
    private int spots = 100;
    private int squareSize = size / spots;

    private ArrayList<Cell> cells = new ArrayList<>();
    private boolean isRunning = false;


    public int countNeighbours(Cell c){
        int neighbours = 0;
        for (Cell cell : cells){
            if (c.getX()==cell.getX() && c.getY()-squareSize==cell.getY()) neighbours++;
            else if (c.getX()+squareSize==cell.getX() && c.getY()-squareSize==cell.getY()) neighbours++;
            else if (c.getX()+squareSize==cell.getX() && c.getY()==cell.getY()) neighbours++;
            else if (c.getX()+squareSize==cell.getX() && c.getY()+squareSize==cell.getY()) neighbours++;
            else if (c.getX()==cell.getX() && c.getY()+squareSize==cell.getY()) neighbours++;
            else if (c.getX()-squareSize==cell.getX() && c.getY()+squareSize==cell.getY()) neighbours++;
            else if (c.getX()-squareSize==cell.getX() && c.getY()==cell.getY()) neighbours++;
            else if (c.getX()-squareSize==cell.getX() && c.getY()-squareSize==cell.getY()) neighbours++;
        }
        return neighbours;
    }

    @FXML
    public void initialize(){

        Thread simulation = new Thread(this::runSimulation);

        for (int i=0; i < size; i+=squareSize){
            for (int j=0; j < size; j+=squareSize){
                Rectangle r = new Rectangle(i, j, squareSize, squareSize);
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                pane.getChildren().add(r);
            }
        }

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x = (int)(mouseEvent.getX() / squareSize) * squareSize + squareSize / 2;
                int y = (int)(mouseEvent.getY() / squareSize) * squareSize + squareSize / 2;
                if (cells.stream().noneMatch(cell -> cell.getX() == x && cell.getY() == y)){
                    Circle c = new Circle();
                    double radius = squareSize / 3.0;
                    Cell cell = new Cell(x, y, radius, c);
                    cells.add(cell);
                    pane.getChildren().add(c);
                    cell.draw();
                }
                else {
                    Cell cellToRemove =
                            cells.stream().filter(cell -> cell.getX() == x && cell.getY() == y).findAny().get();
                    pane.getChildren().remove(cellToRemove.getCircle());
                    cells.removeIf(cell -> cell.equals(cellToRemove));
                }
            }
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isRunning = true;
                simulation.start();
            }
        });

    }

    void runSimulation(){

        while (isRunning) {

            Platform.runLater(()->{
                List<Cell> cellsToRemove =
                        cells.stream().filter(cell -> !(countNeighbours(cell) == 2 || countNeighbours(cell) == 3)).toList();
                System.out.println(cellsToRemove.size());
                cellsToRemove.forEach(cell -> pane.getChildren().remove(cell.getCircle()));
                cells.removeAll(cellsToRemove);
            });

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {

            }
        }
    }

}