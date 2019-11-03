package sample;

import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.editor.CirclePropertiesController;
import sample.editor.SquarePropertiesController;
import sample.shapes.Circle;
import sample.shapes.Shape;
import sample.shapes.Square;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    AnchorPane root;
    @FXML
    Canvas canvas;
    @FXML
    ToggleGroup modeGroup;
    @FXML
    RadioButton rbSelect;
    @FXML
    RadioButton rbCircle;
    @FXML
    RadioButton rbSquare;

    private Model model;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        // Canvas
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        canvas.widthProperty().addListener(observable -> drawShapes());
        canvas.heightProperty().addListener(observable -> drawShapes());

        // Radio buttons
        modeGroup.selectedToggleProperty().addListener((ob, o, n) -> {
            RadioButton rb = (RadioButton) modeGroup.getSelectedToggle();

            if (rb == rbSelect) {
                model.setDrawMode(DrawMode.SELECT);
            } else if (rb == rbCircle) {
                model.setDrawMode(DrawMode.DRAW_CIRCLE);
            } else if (rb == rbSquare) {
                model.setDrawMode(DrawMode.DRAW_SQUARE);
            }
        });

        // Model
        model = new Model();
        model.getShapes().addListener(this::onDrawablesChanged);
        model.setDrawMode(DrawMode.SELECT);
    }

    public void init() {
        canvas.getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<>() {
                    final KeyCombination ctrlZ = new KeyCodeCombination(KeyCode.Z,
                            KeyCombination.CONTROL_DOWN);
                    final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

                    public void handle(KeyEvent ke) {
                        if (ctrlZ.match(ke)) {
                            model.undo();
                            drawShapes();
                        } else if (ctrlS.match(ke)) {
                            saveToFile();
                        }
                        ke.consume();
                    }
                });
    }

    private void onDrawablesChanged(Observable observable) {
        drawShapes();
    }

    private void drawShapes() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //gc.setFill(Color.WHITE);
        //gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Shape shape : model.getShapes()) {
            shape.draw(gc);
        }
    }

    public void canvasOnMouseClicked(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (model.getDrawMode() == DrawMode.SELECT) {
            Optional<Shape> shape = model.findSelectedShape(x, y);
            if (shape.isPresent()) {
                try {
                    Shape s = shape.get();
                    Stage stage = new Stage();
                    FXMLLoader loader;

                    if (s.getShapeType() == ShapeType.CIRCLE) {
                        var circle = (Circle) s;
                        loader = new FXMLLoader(getClass().getResource("editor/circleProperties.fxml"));
                        loader.setControllerFactory(param ->
                                new CirclePropertiesController(circle.getRadius(), circle.getColor(), (r, c) -> {
                                    stage.close();
                                    model.updateShape(circle, r, c);
                                    drawShapes();
                                }));
                        stage.setTitle("Circle");
                    } else {
                        var square = (Square) s;
                        loader = new FXMLLoader(getClass().getResource("editor/squareProperties.fxml"));
                        loader.setControllerFactory(param ->
                                new SquarePropertiesController(square.getSideLength(), square.getColor(), (l, c) -> {
                                    stage.close();
                                    model.updateShape(square, l, c);
                                    drawShapes();
                                }));
                        stage.setTitle("Square");
                    }
                    Parent root = loader.load();

                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(
                            ((Node)event.getSource()).getScene().getWindow() );
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Stage stage = new Stage();

                FXMLLoader loader;
                if (model.getDrawMode() == DrawMode.DRAW_CIRCLE) {
                    loader = new FXMLLoader(getClass().getResource("editor/circleProperties.fxml"));
                    loader.setControllerFactory(param ->
                            new CirclePropertiesController(20, Color.BLUE, (r, c) -> {
                                stage.close();
                                Shape shape = ShapeFactory.createShape(model.getShapeTypeToDraw(), x, y, r, c);
                                model.addShape(shape);
                                drawShapes();
                            }));
                    stage.setTitle("Circle");
                } else {
                    loader = new FXMLLoader(getClass().getResource("editor/squareProperties.fxml"));
                    loader.setControllerFactory(param ->
                            new SquarePropertiesController(20, Color.BLUE, (l, c) -> {
                                stage.close();
                                Shape shape = ShapeFactory.createShape(model.getShapeTypeToDraw(), x, y, l, c);
                                model.addShape(shape);
                                drawShapes();
                            }));
                    stage.setTitle("Square");
                }
                Parent root = loader.load();

                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(
                        ((Node)event.getSource()).getScene().getWindow() );
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToFile() {
        showSaveAsFileDialog(stage).ifPresent(path -> {
            try (FileWriter fileWriter = new FileWriter(path)) {
                fileWriter.write("<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                        "<svg width=\"" + canvas.getWidth() + "\" height=\"" + canvas.getHeight() + "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n");
                for (Shape shape : model.getShapes()) {
                    fileWriter.write(shape.toSvg());
                }
                fileWriter.write("</svg>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static Optional<File> showSaveAsFileDialog(Stage stage) {
        //Show a file dialog that returns a selected file for opening or null if no file was selected.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SVG (*.svg)", "*.svg"));

        File path = fileChooser.showSaveDialog(stage);

        //Path can be null if abort was selected
        if (path != null) {
            //We have a valid File object. Use with FileReader or FileWriter
            return Optional.of(path);
        }
        //No file selected
        return Optional.empty();
    }
}
