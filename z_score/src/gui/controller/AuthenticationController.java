package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import entities.Nutritionist;
import entities.service.NutritionistService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Utils;

public class AuthenticationController implements Initializable{
	@FXML
	private ImageView logo = new ImageView();
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Button login;
	
	@FXML
	private Hyperlink register;
	
	@FXML
	public void onLogin() {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/Main_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			Stage currentStage = (Stage) Main.getScene().getWindow();
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("Painel Principal");
			currentStage.setResizable(false);
		} catch (IOException e) {
			e.printStackTrace();
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

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Image logoImage = new Image(getClass().getResource("../../resources/logo.png").toString());
		
		logo.setImage(logoImage);
	}
}
