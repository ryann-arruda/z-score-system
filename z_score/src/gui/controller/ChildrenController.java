package gui.controller;

import java.io.IOException;
import java.util.Date;

import entities.Child;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.Alerts;
import util.Utils;

public class ChildrenController {
	@FXML
	private Label nutritionistName;
	
	@FXML
	private Label nutritionistIdentifier;
	
	@FXML
	private Label schoolName;
	
	@FXML
	private Label schoolIdentifier;
	
	@FXML
	private Label levelEducationName;
	
	@FXML 
	private Label numberStudentsLevelEducation;
	
	@FXML
	private TableView<Child> tableViewChild;
	
	@FXML
	private TableColumn<Child, String> tableColumnName;
	
	@FXML
	private TableColumn<Child, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Child, Double> tableColumnLastZscoreMeasurement;
	
	@FXML
	private TableColumn<Child, Child> tableColumnSEE;
	
	@FXML
	private TableColumn<Child, Child> tableColumnEDIT;
	
	@FXML
	private TableColumn<Child, Child> tableColumnREMOVE;
	
	@FXML
	private Button logout;
	
	@FXML
	private Button goBack;
	
	@FXML
	private Button addNewChild;
	
	@FXML
	public void onLogout(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/Authentication_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			Stage currentStage = Utils.getCurrentStage(event);
			
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("NutriData");
			currentStage.setResizable(false);
		} catch (IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível fazer log out.", AlertType.ERROR);
		}
	}
}
