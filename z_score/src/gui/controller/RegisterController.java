package gui.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import entities.Nutritionist;
import entities.service.NutritionistService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
	
	private NutritionistService service;
	
	@FXML
	private TextField name;
	
	@FXML
	private DatePicker dateBirth;
	
	@FXML
	private TextField regionalCouncilNutritionists;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private PasswordField passwordConfirmation;
	
	@FXML
	private Label errorDisplay;
	
	@FXML
	private Button register;
	
	@FXML
	private Button cancel;
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	private boolean comparePasswords() {
		return (password.getText() != null) && (passwordConfirmation.getText() != null) &&
				(!password.getText().trim().equals("")) && (!passwordConfirmation.getText().trim().equals("")) &&
				(password.getText().equals(passwordConfirmation.getText()));
	}
	
	private void validateData() {
		
		
		if(name.getText() == null || name.getText().trim().equals("")) {
			// TODO Exception to validate
		}
		
		if(dateBirth.getValue() == null) {
			// TODO Exception to validate
		}
		
		if(regionalCouncilNutritionists.getText() == null ||
		   regionalCouncilNutritionists.getText().trim().equals("")) {
			// TODO Exception to validate
		}
		
		if(username.getText() == null || username.getText().trim().equals("")) {
			// TODO Exception to validate
		}
		
		if(!comparePasswords()) {
			// TODO Exception to validate
		}
	}
	
	private Date getDateBirth() {
		LocalDate localDate = dateBirth.getValue();
		
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public Nutritionist getFormData() {
		Nutritionist nutritionist = new Nutritionist();
		
		return null;
	}
}
