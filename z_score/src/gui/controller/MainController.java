package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import entities.Nutritionist;
import entities.School;
import entities.service.NutritionistService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController implements Initializable{
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnSchoolIdentifier.setCellValueFactory(new PropertyValueFactory<>("nationalRegistryLegalEntities"));
		tableColumnNumberStudents.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(cellData.getValue().getNumberStudents()).asObject());
	}
}
