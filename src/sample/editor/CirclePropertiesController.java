package sample.editor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CirclePropertiesController {
    @FXML
    public TextField radiusTextField;
    @FXML
    public ColorPicker colorPicker;

    private double radius;
    private Color color;
    private IEditCircleDone editCircleDone;

    public CirclePropertiesController(double radius, Color color, IEditCircleDone editCircleDone) {
        this.radius = radius;
        this.color = color;
        this.editCircleDone = editCircleDone;
    }

    public void initialize() {
        radiusTextField.setText(String.valueOf(radius));
        colorPicker.setValue(color);
    }

    public void onOkClicked(MouseEvent mouseEvent) {
        try {
            var text = radiusTextField.getText();
            if (text == null || text.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please enter a valid radius").show();
            } else {
                var r = Double.valueOf(radiusTextField.getText());
                var c = colorPicker.getValue();
                if (c == null) {
                    new Alert(Alert.AlertType.ERROR, "Please select a color").show();
                } else {
                    editCircleDone.onDone(r, c);
                }
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid radius").show();
        }
    }
}
