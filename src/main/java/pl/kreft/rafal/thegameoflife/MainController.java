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
import java.util.List;

import static pl.kreft.rafal.thegameoflife.Config.SIZE;
import static pl.kreft.rafal.thegameoflife.Config.SQUARE_SIZE;

public class MainController {

    @FXML
    private Pane pane;
    @FXML
    private Button button;
    @FXML
    private Button clearBtn;
    @FXML
    private Button speedUpBtn;
    @FXML
    private Button slowDownBtn;
    @FXML
    private Button save;
    @FXML
    private Button load;
    @FXML
    private Label statusLabel;
    @FXML
    private Label cyclesLabel;
    @FXML
    private Label speedLabel;
    @FXML
    private Label livingLabel;
    @FXML
    private Label deadLabel;

    private int cycles = 0;
    private int speed = 1000;
    private final ArrayList<Cell> cells = new ArrayList<>();
    private boolean isRunning = false;
    private boolean stop = false;
    private final FileService fileService = new FileService();


    public int countNeighbours(Cell c){
        int neighbours = 0;
        for (Cell cell : cells){
            if (!cell.equals(c) &&
                    Math.sqrt(
                            Math.pow(cell.getX()-c.getX(),2)+Math.pow(cell.getY()-c.getY(),2)
                    ) <= SQUARE_SIZE*Math.sqrt(2))
                neighbours++;
        }
        return neighbours;
    }

    public ArrayList<Cell> cellsToCreate(){
        ArrayList<Cell> newCells = new ArrayList();
        for (int i = 0; i < SIZE; i+=SQUARE_SIZE){
            for (int j = 0; j < SIZE; j+=SQUARE_SIZE){
                int x = i+SQUARE_SIZE/2;
                int y = j+SQUARE_SIZE/2;
                Cell newCell = new Cell(x, y, SQUARE_SIZE/3.0, new Circle());
                if (cells.stream().noneMatch(cell -> cell.getX()== x && cell.getY()== y) &&
                        countNeighbours(newCell) == 3) newCells.add(newCell);
            }
        }
        return newCells;
    }

    public void clear() {
        stop = isRunning;
        cells.forEach(cell -> pane.getChildren().remove(cell.getCircle()));
        cells.clear();
        cycles = 0;
        statusLabel.setText("Status: Stopped");
        cyclesLabel.setText("Cycles: 0");
        livingLabel.setText("Living: 0");
        deadLabel.setText("Dead: 0");
    }

    @FXML
    public void initialize(){

        Thread simulation = new Thread(this::runSimulation);

        for (int i=0; i < SIZE; i+=SQUARE_SIZE){
            for (int j=0; j < SIZE; j+=SQUARE_SIZE){
                Rectangle r = new Rectangle(i, j, SQUARE_SIZE, SQUARE_SIZE);
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                pane.getChildren().add(r);
            }
        }

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x = (int)(mouseEvent.getX() / SQUARE_SIZE) * SQUARE_SIZE + SQUARE_SIZE / 2;
                int y = (int)(mouseEvent.getY() / SQUARE_SIZE) * SQUARE_SIZE + SQUARE_SIZE / 2;
                if (cells.stream().noneMatch(cell -> cell.getX() == x && cell.getY() == y)){
                    Circle c = new Circle();
                    double radius = SQUARE_SIZE / 3.0;
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
                livingLabel.setText("Living: "+cells.size());
            }
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!simulation.isAlive()&&!isRunning){
                    isRunning = true;
                    statusLabel.setText("Status: Running");
                    simulation.start();
                }
                else {
                    if (stop) statusLabel.setText("Status: Running");
                    else statusLabel.setText("Status: Stopped");
                    stop = !stop;
                }
            }
        });

        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clear();
            }
        });

        speedUpBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (speed > 100) speed-=100;
                speedLabel.setText("Speed: "+speed+"ms");
            }
        });

        slowDownBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (speed < 2000) speed+=100;
                speedLabel.setText("Speed: "+speed+"ms");
            }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                fileService.saveCellsState(cells);
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clear();
                fileService.loadCellsState(cells);
                cells.forEach(cell -> {pane.getChildren().add(cell.getCircle()); cell.draw();});
                livingLabel.setText("Living: "+cells.size());
            }
        });
    }

    void runSimulation(){

        while (isRunning) {
            if (!stop) {
                Platform.runLater(()->{
                    ArrayList<Cell> cellsToCreate = cellsToCreate();
                    List<Cell> cellsToRemove =
                            cells.stream().filter(cell -> !(countNeighbours(cell) == 2 ||
                                    countNeighbours(cell) == 3)).toList();
                    deadLabel.setText("Dead: "+cellsToRemove.size());
                    cellsToRemove.forEach(cell -> pane.getChildren().remove(cell.getCircle()));
                    cells.removeAll(cellsToRemove);
                    cellsToCreate.forEach(cell -> {pane.getChildren().add(cell.getCircle()); cell.draw();});
                    cells.addAll(cellsToCreate);
                    cellsToCreate.clear();
                    livingLabel.setText("Living: "+cells.size());
                    cycles++;
                    cyclesLabel.setText("Cycles: "+cycles);
                });
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {}
        }
    }

}