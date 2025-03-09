package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import entities.Child;
import entities.MeasurementZscore;
import entities.Nutritionist;
import entities.service.NutritionistService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Alerts;
import util.Utils;

public class ChildController implements Initializable{
	
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
	
	@FXML
	public void onGoBack(ActionEvent event) {
		Utils.getCurrentStage(event).close();
	}
	
	private void createDialogForm(String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			AnchorPane anchorPane = loader.load();
			
			if(nutritionist == null) {
				throw new IllegalStateException("Nutritionist entity was null");
			}
			
			if(child == null) {
				throw new IllegalStateException("Child entity was null");
			}
			
			MeasurementFormController measurementFormController = loader.getController();
			measurementFormController.setNutritionist(nutritionist);
			measurementFormController.setChild(child);
			measurementFormController.setNutritionistService(new NutritionistService());
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro de Nova Medida Z-score");
			dialogStage.setScene(new Scene(anchorPane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.show();
		}
		catch(IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela de cadastro de nova medida Z-score. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	public void updateTableViewMeasures() {
		if(child == null) {
			throw new IllegalStateException("Child entity was null");
		}
		
		ObservableList<MeasurementZscore> obsList = FXCollections.observableArrayList(child.getAllZscores());
		tableViewMeasures.setItems(obsList);
	}
	
	@FXML
	public void onAddNewMeasurement(ActionEvent event) {
		createDialogForm("../../gui/MeasurementForm_view.fxml", Utils.getCurrentStage(event));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TableColumn Date of Measurement
		dateMeasurement.setCellFactory(cell -> new TableCell<MeasurementZscore, LocalDate>(){
			private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
			@Override
			protected void updateItem(LocalDate localDate, boolean empty) {
				super.updateItem(localDate, empty);
				
				if(localDate == null) {
					setGraphic(null);
					return;
				}
				
				setText(localDate.format(dtf));
				setAlignment(Pos.CENTER);
			}
		});
		
		dateMeasurement.setCellValueFactory(param -> new SimpleObjectProperty<LocalDate>(param.getValue().getDate()
																							  .toInstant()
																							  .atZone(ZoneId.systemDefault())
																							  .toLocalDate()));
		
		// TableColumn Z-score value
		value.setCellFactory(cell -> new TableCell<MeasurementZscore, Double>(){
			@Override
			protected void updateItem(Double value, boolean empty) {
				super.updateItem(value, empty);
				
				if(value == null) {
					setGraphic(null);
					return;
				}
				
				setText(value.toString());
				setAlignment(Pos.CENTER);
			}
		});
		
		value.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getzScore()));
	}
}
