package sample.shapes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.ShapeType;

public abstract class Shape implements Cloneable {
    private DoubleProperty xPos = new SimpleDoubleProperty();
    private DoubleProperty yPos = new SimpleDoubleProperty();
    private ObjectProperty<Color> color = new SimpleObjectProperty<>();

    public Shape(double xPos, double yPos, Color color) {
        setXPos(xPos);
        setYPos(yPos);
        this.color.setValue(color);
    }

    public double getXPos() {
        return xPos.get();
    }

    public double getYPos() {
        return yPos.get();
    }

    public Color getColor() {
        return color.get();
    }

    public void setXPos(double xPos) {
        this.xPos.set(xPos);
    }
    
    public void setYPos(double yPos) {
        this.yPos.set(yPos);
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public DoubleProperty xPosProperty() {
        return xPos;
    }
    
    public DoubleProperty yPosProperty() {
        return yPos;
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public abstract void draw(GraphicsContext gc);

    public abstract boolean isIntersecting(double x, double y);

    public abstract String toSvg();

    public abstract ShapeType getShapeType();

    @Override
    public abstract Object clone();
}
