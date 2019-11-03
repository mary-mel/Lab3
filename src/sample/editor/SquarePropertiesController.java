package sample.editor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SquarePropertiesController {
    @FXML
    public TextField sideLengthTextField;
    @FXML
    public ColorPicker colorPicker;
    
    private double sideLength;
    private Color color;
    private IEditSquareDone editSquareDone;

    public SquarePropertiesController(double sideLength, Color color, IEditSquareDone editSquareDone) {
        this.sideLength = sideLength;
        this.color = color;
        this.editSquareDone = editSquareDone;
    }

    public void initialize() {
        sideLengthTextField.setText(String.valueOf(sideLength));
        colorPicker.setValue(color);
    }

    public void onOkClicked(MouseEvent mouseEvent) {
        try {
            var text = sideLengthTextField.getText();
            if (text == null || text.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please enter a valid length").show();
            } else {
                var l = Double.valueOf(sideLengthTextField.getText());
                var c = colorPicker.getValue();
                if (c == null) {
                    new Alert(Alert.AlertType.ERROR, "Please select a color").show();
                } else {
                    editSquareDone.onDone(l, c);
                }
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid length").show();
        }
    }
}
