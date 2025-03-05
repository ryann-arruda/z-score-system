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
import entities.MeasurementZscore;
import entities.Nutritionist;
import entities.service.NutritionistService;
import exceptions.FieldValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import util.Alerts;
import util.Constraints;
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
	
	private void validateFields() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(date.getValue() == null) {
			exception.addError("dateError", "Insira uma data válida!");
		}
		
		if(value.getText() == null || value.getText().trim().equals("") || Utils.tryParseToDouble(value.getText()) == null) {
			exception.addError("valueError", "Insira um valor válido! Ex.: 3,7");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
	}
	
	@FXML
	public void onSave(ActionEvent event) {
		try {
			validateFields();
			
			MeasurementZscore measurementZscore = getFormData();
			child.addZscore(measurementZscore);
			
			if(service.update(nutritionist)) {
				Alerts.showAlert("Sucesso", null, "Medida z-score cadastrada com sucesso!", AlertType.CONFIRMATION);
				Utils.getCurrentStage(event).close();
			}
		}
		catch(FieldValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlert("Erro", null, "Não foi possível cadastrar uma nova medida z-score. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	private MeasurementZscore getFormData() {
		Double zscoreValue = Utils.tryParseToDouble(value.getText());
		Date measurementDate = getDate();
		
		return new MeasurementZscore(zscoreValue, measurementDate);
	}

	private Date getDate() {
		LocalDate localDate = date.getValue();
		
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	@FXML
	public void onCancel(ActionEvent event) {
		Utils.getCurrentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		date.getEditor().setDisable(true);
		Constraints.setNumericTextField(value);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("dateError")) {
			dateError.setText(errors.get("dateError"));
		}
		else {
			dateError.setText("");
		}
		
		if(fields.contains("valueError")) {
			valueError.setText(errors.get("valueError"));
		}
		else {
			valueError.setText("");
		}
	}	
}
