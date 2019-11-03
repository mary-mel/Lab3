package sample.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.ShapeType;

public class Square extends Shape {
    private double sideLength;

    public Square(double xPos, double yPos, Color color, double sideLength) {
        super(xPos, yPos, color);
        this.sideLength = sideLength;
    }

    public double getSideLength() {
        return sideLength;
    }

    public void setSideLength(double sideLength) {
        this.sideLength = sideLength;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double hSide = getSideLength() / 2.0;

        gc.setFill(getColor());
        gc.fillRect(getXPos() - hSide, getYPos() - hSide, getSideLength(), getSideLength());
    }

    @Override
    public boolean isIntersecting(double x, double y) {
        double hSide = getSideLength() / 2.0;

        return x > getXPos() - hSide && x < getXPos() + hSide &&
                y > getYPos() - hSide && y < getYPos() + hSide;
    }

    @Override
    public String toSvg() {
        double hSide = getSideLength() / 2.0;

        return "<rect x=\"" + (getXPos() - hSide) + "\" y=\"" + (getYPos() - hSide) + "\" width=\"" +
                getSideLength() + "\" height=\"" + getSideLength() + "\"/>";
    }

    @Override
    public ShapeType getShapeType() {
        return ShapeType.SQUARE;
    }

    @Override
    public Object clone() {
        return new Square(getXPos(), getYPos(), getColor(), getSideLength());
    }
}
