package pl.kreft.rafal.thegameoflife;

import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static pl.kreft.rafal.thegameoflife.Config.SQUARE_SIZE;

public class FileService {

    public void saveCellsState(ArrayList<Cell> cells) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save cells");
        fileChooser.setInitialFileName("positions.cells");
        try {
            File file = fileChooser.showSaveDialog(null).getAbsoluteFile();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Cell cell : cells) {
                bw.write(cell.getX()+","+cell.getY());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCellsState(ArrayList<Cell> cells){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load cells");
        try {
            cells.clear();
            File file = fileChooser.showOpenDialog(null);
            FileReader fr = new FileReader(file);
            Scanner reader = new Scanner(fr);
            while (reader.hasNextLine()) {
                String pos = reader.nextLine();
                cells.add(
                        new Cell(
                                Double.parseDouble(pos.split(",")[0]),
                                Double.parseDouble(pos.split(",")[1]),
                                SQUARE_SIZE / 3.0,
                                new Circle()
                        )
                );
            }
            reader.close();
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
