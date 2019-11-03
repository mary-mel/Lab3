package sample;

import javafx.scene.paint.Color;
import sample.shapes.Circle;
import sample.shapes.Shape;
import sample.shapes.Square;

public class ShapeFactory {
    public static Shape createShape(ShapeType type, double x, double y, double dimen, Color color) {
        switch (type) {
            case CIRCLE:
                return new Circle(x, y, color, dimen);
            case SQUARE:
                return new Square(x, y, color, dimen);
        }

        return null;
    }
}
