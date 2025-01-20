package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	private static Scene scene;
	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("../gui/Authentication_view.fxml"));
			
			scene = new Scene(parent);
			
			stage.setTitle("NutriData");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getScene() {
		return scene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
