package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import entities.Nutritionist;
import entities.service.NutritionistService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChildFormController implements Initializable{
	
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
	@FXML
	private TextField childName;
	
	@FXML
	private DatePicker childDatebirth;
	
	@FXML
	private Label childNameError;
	
	@FXML
	private Label childDateBirthError;
	
	@FXML
	private Button save;
	
	@FXML
	private Button cancel;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		childDatebirth.getEditor().setDisable(true);
	}
}
