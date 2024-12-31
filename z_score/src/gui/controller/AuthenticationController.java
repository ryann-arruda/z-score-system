package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class AuthenticationController implements Initializable{
	@FXML
	private ImageView logo = new ImageView();
	
	@FXML
	private TextField username;
	
	@FXML
	private TextField password;
	
	@FXML
	private Button login;
	
	@FXML
	private Hyperlink register;
	
	@FXML
	public void onPasswordEntered(KeyEvent event){
		String key = event.getText();
		System.out.println(key);
	}
	
	@FXML
	public void onLogin() {
		System.out.println("Ok");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image logoImage = new Image(getClass().getResource("../../resources/logo.png").toString());
		
		logo.setImage(logoImage);
	}
}
