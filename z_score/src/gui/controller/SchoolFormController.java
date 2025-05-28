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

public class SchoolFormController{
	
	private Nutritionist nutritionist;
	
	private School school;
	
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
	
	public void setSchool(School school) {
		this.school = school;
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private boolean validateNationalRegistryLegalEntities() {
		String registry = nationalRegistryLegalEntities.getText();
		
		if(registry == null) {
			return true;
		}
		else if(registry.trim().equals("")) {
			return true;
		}
		else if(!registry.matches("\\d{2}\\.\\d{3}.\\d{3}/\\d{4}-\\d{2}")) {
			return true;
		}
		
		return false;
	}
	
	private void validateFields() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(name.getText() == null || name.getText().trim().equals("")) {
			exception.addError("nameError", "Insira um nome válido!");
		}
		
		if(validateNationalRegistryLegalEntities()) {
			exception.addError("nationalRegistryLegalEntitiesError", "Insira um CNPJ válido!\nFormato: XX.XXX.XXX/XXXX-XX");
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
			
			getFormData();
			nutritionist.addSchool(school);
			notifyDataChangeListeners();
			
			if(service.update(nutritionist)) {
				Alerts.showAlert("Sucesso", null, "Dados da escola inseridos com sucesso!", AlertType.CONFIRMATION);
				Utils.getCurrentStage(event).close();
			}
		}
		catch(FieldValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlert("Erro", null, "Não foi possível inserir os dados da escola. Tente novamente mais tarde.", AlertType.ERROR);
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
	
	private void getFormData() {
		school.setName(name.getText());
		school.setNationalRegistryLegalEntities(nationalRegistryLegalEntities.getText());
	}
	
	public void updateFormData() {
		name.setText(school.getName());
		nationalRegistryLegalEntities.setText(school.getNationalRegistryLegalEntities());
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
