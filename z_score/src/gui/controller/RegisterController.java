package gui.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import entities.Nutritionist;
import entities.service.NutritionistService;
import exceptions.ValidateException;
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
	private Label nameError;
	
	@FXML
	private Label dateBirthError;
	
	@FXML
	private Label regionalCouncilNutritionistsError;
	
	@FXML
	private Label usernameError;
	
	@FXML
	private Label passwordError;
	
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
		ValidateException exception = new ValidateException("Erros when filling in fields");
		
		if(name.getText() == null || name.getText().trim().equals("")) {
			exception.addError("nameError", "Insira um nome válido!");
		}
		
		if(dateBirth.getValue() == null) {
			exception.addError("dateBirthError", "Insira uma data de nascimento válida!");
		}
		
		if(regionalCouncilNutritionists.getText() == null ||
		   regionalCouncilNutritionists.getText().trim().equals("")) {
			exception.addError("regionalCouncilNutritionistsError", "Insira um número de CRN válido!");
		}
		
		if(username.getText() == null || username.getText().trim().equals("")) {
			exception.addError("usernameError", "Insira um nome de usuário válido!");
		}
		
		if(!comparePasswords()) {
			exception.addError("passwordError", "Insira uma senha válida!");
		}
		
		// TODO Finalize the implementation of the method
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
