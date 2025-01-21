package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import application.Main;
import entities.Nutritionist;
import entities.service.NutritionistService;
import exceptions.FieldValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Alerts;
import util.Utils;

public class AuthenticationController implements Initializable{
	
	private NutritionistService service = new NutritionistService();
	
	@FXML
	private ImageView logo = new ImageView();
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Label loginError;
	
	@FXML
	private Button login;
	
	@FXML
	private Hyperlink register;
	
	@FXML
	public void onLogin() {
		try {
			validateLoginFields();
			
			Nutritionist user = service.login(username.getText(), password.getText());
			
			if(user != null){
				loadMainView(user);
			}
		}
		catch(FieldValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}
	
	@FXML
	public void onRegister(ActionEvent event) {
		Stage parentStage = Utils.getCurrentStage(event);
		
		createDialogForm("../../gui/Register_view.fxml", parentStage);
	}

	private void createDialogForm(String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			AnchorPane anchorPane = loader.load();
			
			RegisterController registerController = loader.getController();
			registerController.setNutritionist(new Nutritionist());
			registerController.setNutritionistService(new NutritionistService());
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro de Nutricionista");
			dialogStage.setScene(new Scene(anchorPane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.show();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadMainView(Nutritionist nutritionist) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/Main_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			MainController mainController = loader.getController();
			mainController.setNutritionist(nutritionist);
			mainController.setNutritionistService(new NutritionistService());
			
			Stage currentStage = (Stage) Main.getScene().getWindow();
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("Painel Principal");
			currentStage.setResizable(false);
		} catch (IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível carregar o painel principal. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	private void validateLoginFields() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(username.getText() == null || username.getText().trim().equals("")) {
			exception.addError("loginError", "As informações fornecidas não são válidas!");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		if(password.getText() == null || password.getText().trim().equals("")) {
			exception.addError("loginError", "As informações fornecidas não são válidas!");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {		
		if(errors.containsKey("loginError")) {
			loginError.setText(errors.get("loginError"));
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Image logoImage = new Image(getClass().getResource("../../resources/logo.png").toString());
		
		logo.setImage(logoImage);
	}
}
