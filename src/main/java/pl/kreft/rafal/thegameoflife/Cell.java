package pl.kreft.rafal.thegameoflife;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Cell {

    private double x;
    private double y;
    private double radius;
    private Circle circle;

    public Cell(double x, double y, double radius, Circle circle) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.circle = circle;
        this.circle.setFill(Color.BLACK);
        this.circle.setStroke(Color.BLACK);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void draw(){
        circle.setRadius(radius);
        circle.setTranslateX(x);
        circle.setCenterY(y);
    }
}
