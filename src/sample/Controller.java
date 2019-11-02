package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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

    public void initialize() {
        // Canvas
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        // Radio buttons
        modeGroup.selectedToggleProperty().addListener((ob, o, n) -> {
            RadioButton rb = (RadioButton) modeGroup.getSelectedToggle();

            if (rb == rbSelect) {
                System.out.println("Selecting");
            } else if (rb == rbCircle) {
                System.out.println("Drawing circle");
            } else if (rb == rbSquare) {
                System.out.println("Drawing square");
            }
        });
    }

    public void canvasOnMouseClicked(MouseEvent event) {
        System.out.println(event.getX() + ", " + event.getY());
    }
}
