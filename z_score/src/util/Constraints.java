package util;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Constraints {
	
	private Constraints() {
		
	}
	
	public static void setTextFieldMaxLength(TextField tf, int max) {
		tf.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.length() > max) {
				tf.setText(oldValue);
			}
		});
	}
	
	public static void setPasswordFieldMaxLength(PasswordField pf, int max) {
		pf.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.length() > max) {
				pf.setText(oldValue);
			}
		});
	}
	
	public static void setNumericTextField(TextField tf) {
		tf.textProperty().addListener((observavle, oldValue, newValue) -> {
			if(newValue != null && !newValue.matches("^\\d*(,|,\\d+)?$")) {
				tf.setText(oldValue);
			}
		});
	}
}
