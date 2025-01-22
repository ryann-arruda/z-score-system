package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import entities.Nutritionist;
import entities.School;
import entities.service.NutritionistService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.Alerts;

public class MainController implements Initializable{
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
	private Stage currentStage;
	
	@FXML
	private TableView<School> tableViewSchool;
	
	@FXML
	private TableColumn<School, String> tableColumnName;
	
	@FXML
	private TableColumn<School, String> tableColumnSchoolIdentifier;
	
	@FXML
	private TableColumn<School, Integer> tableColumnNumberStudents;
	
	@FXML
	private TableColumn<School, School> tableColumnSEE;
	
	@FXML
	private TableColumn<School, School> tableColumnEDIT;
	
	@FXML
	private TableColumn<School, School> tableColumnREMOVE;
	
	@FXML
	private Label nutritionistName;
	
	@FXML
	private Label nutritionistIdentifier;
	
	@FXML
	private Button logout;
	
	@FXML
	private Button addNewSchool;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
		this.nutritionistName.setText(nutritionist.getName());
		this.nutritionistIdentifier.setText(nutritionist.getRegionalCouncilNutritionists());
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	public void setCurrentStage(Stage stage) {
		this.currentStage = stage;
	}
	
	@FXML
	public void onLogout() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/Authentication_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			if(this.currentStage == null) {
				throw new IllegalStateException("Stage was null");
			}
			
			AuthenticationController authenticationController = loader.getController();
			authenticationController.setCurrentStage(currentStage);
			
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("NutriData");
			currentStage.setResizable(false);
		} catch (IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível fazer log out.", AlertType.ERROR);
		}
	}
	
	@FXML
	public void onAddNewSchool() {

	}
	
	public void updateTableViewSchool() {
		if(nutritionist == null) {
			throw new IllegalStateException("Nutritionist was null");
		}
		
		ObservableList<School> obsList = FXCollections.observableArrayList(nutritionist.getAllSchools());
		tableViewSchool.setItems(obsList);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnSchoolIdentifier.setCellValueFactory(new PropertyValueFactory<>("nationalRegistryLegalEntities"));
		tableColumnNumberStudents.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(cellData.getValue().getNumberStudents()).asObject());
	}
}
