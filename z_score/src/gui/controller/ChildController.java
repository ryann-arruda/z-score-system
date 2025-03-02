package gui.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import entities.Child;
import entities.MeasurementZscore;
import entities.Nutritionist;
import entities.service.NutritionistService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ChildController {
	
	private Nutritionist nutritionist;
	
	private Child child;
	
	private NutritionistService service;
	
	@FXML
	private Label childName;
	
	@FXML
	private Label birthDate;
	
	@FXML
	private TableView<MeasurementZscore> tableViewMeasures; 
	
	@FXML
	private TableColumn<MeasurementZscore, LocalDate> dateMeasurement;
	
	@FXML
	private TableColumn<MeasurementZscore, Double> value;
	
	@FXML
	private TableColumn<MeasurementZscore, MeasurementZscore> tableColumnEDIT;
	
	@FXML
	private TableColumn<MeasurementZscore, MeasurementZscore> tableColumnREMOVE;
	
	@FXML
	private Button goBack;
	
	@FXML
	private Button addNewMeasurement;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	public void setChild(Child child) {
		this.child = child;
		
		childName.setText(child.getName());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		birthDate.setText(sdf.format(child.getDateBirth()));
	}
}
