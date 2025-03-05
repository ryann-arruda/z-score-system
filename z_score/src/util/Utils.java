package util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	private Utils() {
		
	}
	
	public static Stage getCurrentStage(ActionEvent event) {
		return (Stage)((Node) event.getSource()).getScene().getWindow();
	}
	
	public static Double tryParseToDouble(String str) {
		try {
			return Double.parseDouble(str.replace(",", "."));
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
}
