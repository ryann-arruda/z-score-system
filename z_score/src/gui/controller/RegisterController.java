package gui.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import entities.Nutritionist;
import entities.service.NutritionistService;
import exceptions.FormValidationException;
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
	
	@FXML
	public void onRegister() {
		getFormData();
	}
	
	private boolean comparePasswords() {
		return (password.getText() != null) && (passwordConfirmation.getText() != null) &&
				(!password.getText().trim().equals("")) && (!passwordConfirmation.getText().trim().equals("")) &&
				(password.getText().equals(passwordConfirmation.getText()));
	}
	
	private void validateData() {
		FormValidationException exception = new FormValidationException("Erros when filling in fields");
		
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
		
		if(exception.getErros().size() > 0) {
			throw exception;
		}
	}
	
	private Date getDateBirth() {
		LocalDate localDate = dateBirth.getValue();
		
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public Nutritionist getFormData() {
		Nutritionist nutritionist = new Nutritionist();
		
		try {
			validateData();
		}
		catch(FormValidationException e) {
			setErrorMessages(e.getErros());
		}
		
		return null;
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("nameError")) {
			nameError.setText(errors.get("nameError"));
		}
		else {
			nameError.setText("");
		}
		
		if(fields.contains("dateBirthError")) {
			dateBirthError.setText(errors.get("dateBirthError"));
		}
		else {
			dateBirthError.setText("");
		}
		
		if(fields.contains("regionalCouncilNutritionistsError")) {
			regionalCouncilNutritionistsError.setText(errors.get("regionalCouncilNutritionistsError"));
		}
		else {
			regionalCouncilNutritionistsError.setText("");
		}
		
		if(fields.contains("usernameError")) {
			usernameError.setText(errors.get("usernameError"));
		}
		else {
			usernameError.setText("");
		}
		
		if(fields.contains("passwordError")) {
			passwordError.setText(errors.get("passwordError"));
		}
		else {
			passwordError.setText("");
		}
	}
}
