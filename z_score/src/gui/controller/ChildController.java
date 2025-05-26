package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import db.DBException;
import entities.Calculator;
import entities.Child;
import entities.MeasurementZscore;
import entities.Nutritionist;
import entities.service.NutritionistService;
import entities.service.ZscoreTableService;
import gui.listeners.DataChangeListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Alerts;
import util.Utils;

public class ChildController implements Initializable, DataChangeListener{
	
	private Nutritionist nutritionist;
	
	private Child child;
	
	private NutritionistService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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
	private TableColumn<MeasurementZscore, String> classification;
	
	@FXML
	private TableColumn<MeasurementZscore, Double> weight;
	
	@FXML
	private TableColumn<MeasurementZscore, Double> length;
	
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
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onGoBack(ActionEvent event) {
		notifyDataChangeListeners();
		Utils.getCurrentStage(event).close();
	}
	
	private void createDialogForm(MeasurementZscore measurementZscore, String absoluteName, Stage parentStage) {
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
			measurementFormController.setMeasurementZscore(measurementZscore);
			measurementFormController.setNutritionistService(new NutritionistService());
			measurementFormController.setZscoreCalculator(new Calculator(new ZscoreTableService()));
			measurementFormController.updateFormData();
			measurementFormController.subscribeDataChangeListener(this);
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados da Medida Z-score");
			dialogStage.setScene(new Scene(anchorPane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			Utils.addIcon(dialogStage, "../resources/form.png");
			
			// Removing the title bar
			Stage currentStage = (Stage) dialogStage.getScene().getWindow();
			currentStage.initStyle(StageStyle.UNDECORATED);
			
			dialogStage.show();
		}
		catch(IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela para inserção de dados da medida Z-score. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	public void updateTableViewMeasures() {
		if(child == null) {
			throw new IllegalStateException("Child entity was null");
		}
		
		ObservableList<MeasurementZscore> obsList = FXCollections.observableArrayList(child.getAllZscores());
		tableViewMeasures.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	@FXML
	public void onAddNewMeasurement(ActionEvent event) {
		createDialogForm(new MeasurementZscore(), "../../gui/MeasurementForm_view.fxml", Utils.getCurrentStage(event));
	}
	
	@Override
	public void onDataChanged() {
		updateTableViewMeasures();
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
				
				setText(String.format("%.4f", value));
				setAlignment(Pos.CENTER);
			}
		});
		
		value.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getzScore()));
		
		// TableColumn Classification
		classification.setCellFactory(cell -> new TableCell<MeasurementZscore, String>() {
			@Override
			protected void updateItem(String classification, boolean empty) {
				super.updateItem(classification, empty);
				
				if(classification == null) {
					setGraphic(null);
					return;
				}
				
				setText(classification);
				setAlignment(Pos.CENTER);
			}
		});
		
		classification.setCellValueFactory(param -> new SimpleStringProperty(Utils.formatZscoreClassification(param.getValue().getClassification())));
		
		
		// TableColumn Weight
		weight.setCellFactory(cell -> new TableCell<MeasurementZscore, Double>() {
			@Override
			protected void updateItem(Double weight, boolean empty) {
				super.updateItem(weight, empty);
				
				if(weight == null) {
					setGraphic(null);
					return;
				}
				
				setText(String.format("%.4f", weight));
				setAlignment(Pos.CENTER);
			}
		});
		
		weight.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getWeight()));
		
		// TableColumn Height
		length.setCellFactory(cell -> new TableCell<MeasurementZscore, Double>() {
			@Override
			protected void updateItem(Double length, boolean empty) {
				super.updateItem(length, empty);
				
				if(length == null) {
					setGraphic(null);
					return;
				}
				
				setText(String.format("%.4f", length));
				setAlignment(Pos.CENTER);
			}
		});
		
		length.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getHeight()));
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnEDIT.setCellFactory(cell -> new TableCell<MeasurementZscore, MeasurementZscore>(){
			private final Button button = new Button("Editar");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(MeasurementZscore measurementZscore, boolean empty) {
				super.updateItem(measurementZscore, empty);
				
				if(measurementZscore == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> createDialogForm(measurementZscore, "../../gui/MeasurementForm_view.fxml", Utils.getCurrentStage(event)));
				setGraphic(stackPane);
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnREMOVE.setCellFactory(cell -> new TableCell<MeasurementZscore, MeasurementZscore>(){
			private final Button button = new Button("Excluir");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(MeasurementZscore measurementZscore, boolean empty) {
				super.updateItem(measurementZscore, empty);
				
				if(measurementZscore == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> removeMeasurementZscore(measurementZscore));
				setGraphic(stackPane);
			}
		});
	}
	
	private void removeMeasurementZscore(MeasurementZscore measurementZscore) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Você tem certeza que deseja excluir?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
				child.removeZscore(measurementZscore);
				
				if(service.update(nutritionist)) {
					Alerts.showAlert("Sucesso", null, "Medida de z-score removida com sucesso!", AlertType.CONFIRMATION);
					updateTableViewMeasures();
				}
			}
			catch(DBException e) {
				Alerts.showAlert("Erro", null, "Não foi possível realizar a ação solicitada. Tente novamente mais tarde.", AlertType.ERROR);
			}
		}
	}
}
