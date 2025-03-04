package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import entities.Child;
import entities.Nutritionist;
import entities.service.NutritionistService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import util.Utils;

public class MeasurementFormController implements Initializable{
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
	private Child child;
	
	@FXML
	private DatePicker date;
	
	@FXML
	private Label dateError;
	
	@FXML
	private TextField value;
	
	@FXML
	private Label valueError;
	
	@FXML
	private Button save;
	
	@FXML
	private Button cancel;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	public void setChild(Child child) {
		this.child = child;
	}
	
	@FXML
	public void onCancel(ActionEvent event) {
		Utils.getCurrentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		date.getEditor().setDisable(true);
	}
}
