package util;

import entities.enums.PersonSex;
import entities.enums.ZscoreClassification;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
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
	
	public static String formatSex(PersonSex sex) {
		
		if(sex == PersonSex.MALE) {
			return "Masculino";
		}
		
		return "Feminino";
	}
	
	public static String formatZscoreClassification(ZscoreClassification classification) {
		
		if(classification == ZscoreClassification.OBESITY) {
			return "Obesidade";
		}
		else if(classification == ZscoreClassification.OVERWEIGHT) {
			return "Sobrepeso";
		}
		else if(classification == ZscoreClassification.NORMAL) {
			return "Normal";
		}
		else if(classification == ZscoreClassification.MODERATE_MALNUTRITION) {
			return "Baixo Peso";
		}
		else {
			return "Desnutrição Severa";
		}
	}
	
	public static void addIcon(Stage stage, String path) {
		Image icon = new Image(Utils.class.getResource(path).toString());
		
		stage.getIcons().add(icon);
	}
	
	public static boolean validateRegistrationName(String name) {
		if(name == null) {
			return true;
		}
		else if(name.trim().equals("")) {
			return true;
		}
		else {
			return !name.matches("([a-zA-Z]+(\\s|\\.\\s)?)*");
		}
	}
}
