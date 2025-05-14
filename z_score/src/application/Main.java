package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{
	
	@Override
	public void start(Stage stage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/Authentication_view.fxml"));
			Scene scene = new Scene(loader.load());
			
			stage.setTitle("NutriData");
			stage.setScene(scene);
			stage.setResizable(false);
			addIcon(stage);
			stage.show();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addIcon(Stage stage) {
		Image icon = new Image(getClass().getResource("../resources/logo.png").toString());
		
		stage.getIcons().add(icon);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
