package sample.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.ShapeType;

public class Circle extends Shape {
    private double radius;

    public Circle(double xPos, double yPos, Color color, double radius) {
        super(xPos, yPos, color);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillOval(getXPos() - getRadius(), getYPos() - getRadius(), getRadius() * 2.0, getRadius() * 2.0);
    }

    @Override
    public boolean isIntersecting(double x, double y) {
        double r = getRadius();
        double x2 = (x - getXPos());
        double y2 = (y - getYPos());

        return (x2 * x2 + y2 * y2) < r * r;
    }

    @Override
    public String toSvg() {
        return "<circle cx=\"" + getXPos() + "\" cy=\"" + getYPos() + "\" r=\"" + getRadius() + "\"/>";
    }

    @Override
    public ShapeType getShapeType() {
        return ShapeType.CIRCLE;
    }

    @Override
    public Object clone() {
        return new Circle(getXPos(), getYPos(), getColor(), getRadius());
    }
}
