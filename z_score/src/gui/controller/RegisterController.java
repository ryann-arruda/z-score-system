package gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
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
}
