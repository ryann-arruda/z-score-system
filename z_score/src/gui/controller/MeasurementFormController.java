package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import entities.Child;
import entities.MeasurementZscore;
import entities.Nutritionist;
import entities.ZscoreCalculator;
import entities.enums.ZscoreClassification;
import entities.service.NutritionistService;
import exceptions.FieldValidationException;
import gui.listeners.DataChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import util.Alerts;
import util.Constraints;
import util.Utils;

public class MeasurementFormController implements Initializable{
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
	private Child child;
	
	private MeasurementZscore measurementZscore;
	
	private ZscoreCalculator calculator;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private DatePicker date;
	
	@FXML
	private Label dateError;
	
	@FXML
	private TextField weight;
	
	@FXML
	private Label weightError;
	
	@FXML
	private TextField height;
	
	@FXML
	private Label heightError;
	
	@FXML
	private Button save;
	
	@FXML
	private Button cancel;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}
	
	public void setChild(Child child) {
		this.child = child;
	}
	
	public void setMeasurementZscore(MeasurementZscore measurementZscore) {
		this.measurementZscore = measurementZscore;
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	public void setZscoreCalculator(ZscoreCalculator calculator) {
		this.calculator = calculator;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private boolean validateMeasurementDate() {
		
		if(date.getValue() == null) {
			return true;
		}
		
		LocalDate dateMeasure = date.getValue();
		LocalDate dateBirth = child.getDateBirth().toInstant()
												  .atZone(ZoneId.systemDefault())
												  .toLocalDate();
		
		return dateMeasure.isBefore(dateBirth);
	}
	
	private void validateFields() {
		FieldValidationException exception = new FieldValidationException("Erros when filling in fields");
		
		if(validateMeasurementDate()) {
			exception.addError("dateError", "Insira uma data válida!");
		}
		
		if(weight.getText() == null || weight.getText().trim().equals("") || Utils.tryParseToDouble(weight.getText()) == null) {
			exception.addError("weightError", "Insira um peso válido! Ex.: 57,5");
		}
		
		if(height.getText() == null || height.getText().trim().equals("") || Utils.tryParseToDouble(height.getText()) == null) {
			exception.addError("heightError", "Insira uma altura válida! Ex.: 57,5");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
	}
	
	@FXML
	public void onSave(ActionEvent event) {
		try {
			validateFields();
			
			getFormData();
			child.addZscore(measurementZscore);
			
			if(service.update(nutritionist)) {
				Alerts.showAlert("Sucesso", null, "Dados da medida z-score inseridos com sucesso com sucesso!", AlertType.CONFIRMATION);
				notifyDataChangeListeners();
				Utils.getCurrentStage(event).close();
			}
		}
		catch(FieldValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlert("Erro", null, "Não foi possível inserir os dados da medida z-score. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	private void getFormData() {
		measurementZscore.setWeight(Utils.tryParseToDouble(weight.getText()));
		measurementZscore.setHeight(Utils.tryParseToDouble(height.getText()));
		measurementZscore.setDate(getDate());
		
		Map<String, Object> result = calculator.calculateZscore(child, measurementZscore.getWeight());
		
		Double zscoreValue = (Double)result.get("zscoreValue");
		measurementZscore.setzScore(zscoreValue);
		
		ZscoreClassification classification = (ZscoreClassification)result.get("zscoreClassification");
		measurementZscore.setClassification(classification);
	}
	
	public void updateFormData() {
		
		if(measurementZscore.getId() != null) {
			weight.setText(measurementZscore.getWeight().toString().replace(".", ","));
			height.setText(measurementZscore.getHeight().toString().replace(".", ","));		
			date.setValue(measurementZscore.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
	}

	private Date getDate() {
		LocalDate localDate = date.getValue();
		
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	@FXML
	public void onCancel(ActionEvent event) {
		Utils.getCurrentStage(event).close();
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		date.getEditor().setDisable(true);
		Constraints.setNumericTextField(weight);
		Constraints.setNumericTextField(height);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("dateError")) {
			dateError.setText(errors.get("dateError"));
		}
		else {
			dateError.setText("");
		}
		
		if(fields.contains("weightError")) {
			weightError.setText(errors.get("weightError"));
		}
		else {
			weightError.setText("");
		}
		
		if(fields.contains("heightError")) {
			heightError.setText(errors.get("heightError"));
		}
		else {
			heightError.setText("");
		}
	}	
}
