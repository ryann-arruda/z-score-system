package gui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import db.DBException;
import entities.Nutritionist;
import entities.School;
import entities.service.NutritionistService;
import exceptions.FieldValidationException;
import gui.listeners.DataChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import util.Alerts;
import util.Utils;

public class SchoolFormController {
	
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField nationalRegistryLegalEntities;
	
	@FXML
	private Label nameError;
	
	@FXML
	private Label nationalRegistryLegalEntitiesError;
	
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
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private void validateFields() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(name.getText() == null || name.getText().trim().equals("")) {
			exception.addError("nameError", "Insira um nome válido!");
		}
		
		if(nationalRegistryLegalEntities.getText() == null || nationalRegistryLegalEntities.getText().trim().equals("")) {
			exception.addError("nationalRegistryLegalEntitiesError", "Insira um CNPJ válido!");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
	}
	
	@FXML
	public void onSave(ActionEvent event) {
		if(nutritionist == null) {
			throw new IllegalStateException("Nutritionist entity was null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			validateFields();
			
			School school = new School(name.getText(), nationalRegistryLegalEntities.getText());
			nutritionist.addSchool(school);
			notifyDataChangeListeners();
			
			if(service.update(nutritionist)) {
				Alerts.showAlert("Sucesso", null, "Cadastro de escola realizado com sucesso!", AlertType.CONFIRMATION);
				Utils.getCurrentStage(event).close();
			}
		}
		catch(FieldValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlert("Erro", null, "Não foi possível cadastrar uma nova escola. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	@FXML
	public void onCancel(ActionEvent event) {
		Utils.getCurrentStage(event).close();
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("nameError")) {
			nameError.setText(errors.get("nameError"));
		}
		else {
			nameError.setText("");
		}
		
		if(fields.contains("nationalRegistryLegalEntitiesError")) {
			nationalRegistryLegalEntitiesError.setText(errors.get("nationalRegistryLegalEntitiesError"));
		}
		else {
			nationalRegistryLegalEntitiesError.setText("");
		}
	}
}
