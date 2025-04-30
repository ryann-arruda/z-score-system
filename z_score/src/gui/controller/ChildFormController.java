package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import entities.Child;
import entities.LevelEducation;
import entities.Nutritionist;
import entities.enums.PersonSex;
import entities.service.NutritionistService;
import exceptions.FieldValidationException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import util.Alerts;
import util.Utils;

public class ChildFormController implements Initializable{
	
	private Nutritionist nutritionist;
	
	private LevelEducation levelEducation;
	
	private Child child;
	
	private NutritionistService service;
	
	@FXML
	private TextField childName;
	
	@FXML
	private DatePicker childDateBirth;
	
	@FXML
	private ComboBox<String> sex;
	
	@FXML
	private Label childNameError;
	
	@FXML
	private Label childDateBirthError;
	
	@FXML
	private Label sexError;
	
	@FXML
	private Button save;
	
	@FXML
	private Button cancel;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}
	
	public void setLevelEducation(LevelEducation levelEducation) {
		this.levelEducation = levelEducation;
	}
	
	public void setChild(Child child) {
		this.child = child;
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	private void validateFields() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(childName.getText() == null || childName.getText().trim().equals("")) {
			exception.addError("childNameError", "Insira um nome válido!");
		}
		
		if(childDateBirth.getValue() == null) {
			exception.addError("childDateBirthError", "Insira uma data de nascimento válida!");
		}
		
		if(sex.getValue() == null) {
			exception.addError("sexError", "Insira um sexo válido!");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
	}
	
	private Date getDateBirth() {
		LocalDate localDate = childDateBirth.getValue();
		
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	private PersonSex getSex() {
		
		if(sex.getValue().equals("Masculino")) {
			return PersonSex.MALE;
		}
		else {
			return PersonSex.FEMALE;
		}
	}
	
	@FXML
	public void onSave(ActionEvent event) {
		if(nutritionist == null) {
			throw new IllegalStateException("Nutritionist entity was null");
		}
		
		if(levelEducation == null) {
			throw new IllegalStateException("LevelEducation entity was null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			validateFields();
			
			getFormData();
			levelEducation.addChild(child);
			
			if(service.update(nutritionist)) {
				Alerts.showAlert("Sucesso", null, "Dados de aluno inseridos com sucesso!", AlertType.CONFIRMATION);
				Utils.getCurrentStage(event).close();
			}
		}
		catch(FieldValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela solicitada. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	@FXML
	public void onCancel(ActionEvent event) {
		Utils.getCurrentStage(event).close();
	}
	
	private void getFormData() {
		child.setName(childName.getText());
		child.setDateBirth(getDateBirth());
		child.setSex(getSex());
	}
	
	public void updateFormData() {
		childName.setText(child.getName());
		
		if(child.getDateBirth() != null) {
			childDateBirth.setValue(child.getDateBirth().toInstant()
														.atZone(ZoneId.systemDefault())
														.toLocalDate());
		}
		
		if(child.getSex() == PersonSex.FEMALE) {
			sex.setValue("Feminino");
		}
		else if(child.getSex() == PersonSex.MALE){
			sex.setValue("Masculino");
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		childDateBirth.getEditor().setDisable(true);
		
		sex.getItems().addAll(FXCollections.observableArrayList("Masculino", "Feminino"));
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("childNameError")) {
			childNameError.setText(errors.get("childNameError"));
		}
		else {
			childNameError.setText("");
		}
		
		if(fields.contains("childDateBirthError")) {
			childDateBirthError.setText(errors.get("childDateBirthError"));
		}
		else {
			childDateBirthError.setText("");
		}
		
		if(fields.contains("sexError")) {
			sexError.setText(errors.get("sexError"));
		}
		else {
			sexError.setText("");
		}
	}
}
