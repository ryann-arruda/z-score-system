package gui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import db.DBException;
import entities.LevelEducation;
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

public class LevelEducationFormController {
	private Nutritionist nutritionist;
	
	private School school;
	
	private LevelEducation levelEducation;
	
	private NutritionistService service;
	
	private List<DataChangeListener> listeners = new ArrayList<>();
	
	@FXML
	private TextField levelEducationName;
	
	@FXML
	private Label levelEducationNameError;
	
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
	
	public void setSchool(School school) {
		this.school = school;
	}
	
	public void setLevelEducation(LevelEducation levelEducation) {
		this.levelEducation = levelEducation;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		listeners.add(listener);
	}
	
	private void validateFields() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(levelEducationName.getText() == null || levelEducationName.getText().trim().equals("")) {
			exception.addError("nameError", "Insira um nome válido!");
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
		
		if(school == null) {
			throw new IllegalStateException("School entity was null");
		}
		
		try {
			validateFields();

			getFormData();
			school.addEducationLevel(levelEducation);
			notifyDataChangeListeners();
			
			if(service.update(nutritionist)) {
				Alerts.showAlert("Sucesso", null, "Dados de nível educacional inseridos com sucesso!", AlertType.CONFIRMATION);
				Utils.getCurrentStage(event).close();
			}
		}
		catch(FieldValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlert("Erro", null, "Não foi possível inserir os dados do nível educacional. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	private void getFormData() {
		levelEducation.setName(levelEducationName.getText());
	}

	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : listeners) {
			listener.onDataChanged();
		}
	}
	
	@FXML
	public void onCancel(ActionEvent event) {
		Utils.getCurrentStage(event).close();
	}
	
	public void updateFormData() {
		levelEducationName.setText(levelEducation.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("nameError")) {
			levelEducationNameError.setText(errors.get("nameError"));
		}
	}	
}
