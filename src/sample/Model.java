package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import sample.shapes.Circle;
import sample.shapes.Shape;
import sample.shapes.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Model {
    private final ObservableList<Shape> shapes;
    private DrawMode drawMode;

    private List<Shape> previousState;

    public Model() {
        shapes = FXCollections.observableArrayList(
                param -> new Observable[]{
                        param.xPosProperty(),
                        param.yPosProperty(),
                        param.colorProperty()
                }
        );
        previousState = new ArrayList<>();
    }

    public ObservableList<Shape> getShapes() {
        return shapes;
    }

    public DrawMode getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }

    public Optional<Shape> findSelectedShape(double x, double y) {
        return shapes.stream().filter(s -> s.isIntersecting(x,y)).reduce((first, second) -> second);
    }

    public ShapeType getShapeTypeToDraw() {
        switch (getDrawMode()){
            case SELECT:
                System.out.println("Cannot draw in this mode!");
            case DRAW_CIRCLE:
                return ShapeType.CIRCLE;
            case DRAW_SQUARE:
                return ShapeType.SQUARE;
        }
        return ShapeType.CIRCLE;
    }

    public void undo() {
        // Remove current shapes
        shapes.clear();

        // Add all shapes from previous state
        shapes.addAll(previousState);
    }

    private void saveCurrentState() {
        previousState = shapes.stream().map(s -> (Shape)s.clone()).collect(Collectors.toList());
    }

    public void updateShape(Circle circle, double radius, Color color) {
        saveCurrentState();

        circle.setRadius(radius);
        circle.setColor(color);
    }

    public void updateShape(Square square, double length, Color color) {
        saveCurrentState();

        square.setSideLength(length);
        square.setColor(color);
    }

    public void addShape(Shape shape) {
        saveCurrentState();

        shapes.add(shape);
    }
}
