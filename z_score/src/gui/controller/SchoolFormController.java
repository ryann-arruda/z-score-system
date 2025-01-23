package gui.controller;

import entities.Nutritionist;
import entities.service.NutritionistService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SchoolFormController {
	
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField nationalRegistryLegalEntities;
	
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
}
