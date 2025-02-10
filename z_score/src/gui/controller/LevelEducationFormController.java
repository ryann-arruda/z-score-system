package gui.controller;

import java.util.Map;
import java.util.Set;

import entities.Nutritionist;
import entities.School;
import entities.service.NutritionistService;
import exceptions.FieldValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LevelEducationFormController {
	private Nutritionist nutritionist;
	
	private School school;
	
	private NutritionistService service;
	
	@FXML
	private TextField levelEducationName;
	
	@FXML
	private TextField levelEducationNameError;
	
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
	
	public void setSchool(School school) {
		this.school = school;
	}
	
	private void validateFields() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(levelEducationName.getText() == null || levelEducationName.getText().trim().equals("")) {
			exception.addError("nameError", "Insira um nome válido!");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("nameError")) {
			levelEducationNameError.setText(errors.get("nameError"));
		}
	}	
}
