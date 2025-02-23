package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entities.Nutritionist;
import entities.School;
import entities.service.NutritionistService;
import gui.listeners.DataChangeListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Alerts;
import util.Utils;

public class MainController implements Initializable, DataChangeListener{	
	private Nutritionist nutritionist;
	
	@FXML
	private TableView<School> tableViewSchool;
	
	@FXML
	private TableColumn<School, String> tableColumnName;
	
	@FXML
	private TableColumn<School, String> tableColumnSchoolIdentifier;
	
	@FXML
	private TableColumn<School, Integer> tableColumnNumberStudents;
	
	@FXML
	private TableColumn<School, School> tableColumnSEE;
	
	@FXML
	private TableColumn<School, School> tableColumnEDIT;
	
	@FXML
	private TableColumn<School, School> tableColumnREMOVE;
	
	@FXML
	private Label nutritionistName;
	
	@FXML
	private Label nutritionistIdentifier;
	
	@FXML
	private Button logout;
	
	@FXML
	private Button addNewSchool;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
		this.nutritionistName.setText(nutritionist.getName());
		this.nutritionistIdentifier.setText(nutritionist.getRegionalCouncilNutritionists());
	}
	
	@FXML
	public void onLogout(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/Authentication_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			Stage currentStage = Utils.getCurrentStage(event);
			
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("NutriData");
			currentStage.setResizable(false);
		} catch (IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível fazer log out.", AlertType.ERROR);
		}
	}
	
	@FXML
	public void onAddNewSchool(ActionEvent event) {
		createDialogForm("../../gui/SchoolForm_view.fxml", Utils.getCurrentStage(event));
	}
	
	private void createDialogForm(String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			AnchorPane anchorPane = loader.load();
			
			if(nutritionist == null) {
				throw new IllegalStateException("Nutritionist entity was null");
			}
			
			SchoolFormController schoolFormController = loader.getController();
			schoolFormController.setNutritionist(nutritionist);
			schoolFormController.setNutritionistService(new NutritionistService());
			schoolFormController.subscribeDataChangeListener(this);
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro de Escola");
			dialogStage.setScene(new Scene(anchorPane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.show();
		}
		catch(IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela de cadastro. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	public void updateTableViewSchool() {
		if(nutritionist == null) {
			throw new IllegalStateException("Nutritionist entity was null");
		}
		
		ObservableList<School> obsList = FXCollections.observableArrayList(nutritionist.getAllSchools());
		tableViewSchool.setItems(obsList);
		initSeeButtons();
		initEditButtons();
		initRemoveButtons();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnSchoolIdentifier.setCellValueFactory(new PropertyValueFactory<>("nationalRegistryLegalEntities"));
		tableColumnNumberStudents.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(cellData.getValue().getNumberStudents()).asObject());
	}

	@Override
	public void onDataChanged() {
		updateTableViewSchool();
	}
	
	private void loadSchoolView(ActionEvent event, School school) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/School_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			SchoolController schoolController = loader.getController();
			schoolController.setNutritionist(nutritionist);
			schoolController.setSchool(school);
			schoolController.updateTableViewLevelEducation();
			
			Stage currentStage = Utils.getCurrentStage(event);
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("Visualização da Escola");
			currentStage.setResizable(false);
		} catch (IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível carregar a visualização da escola. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	private void initSeeButtons() {
		tableColumnSEE.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnSEE.setCellFactory(cell -> new TableCell<School, School>(){
			private final Button button = new Button("Ver");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(School school, boolean empty) {
				super.updateItem(school, empty);
				
				if(school == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> loadSchoolView(event, school));
				setGraphic(stackPane);
			}
		});
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnEDIT.setCellFactory(cell -> new TableCell<School, School>() {
			private final Button button = new Button("Editar");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(School school, boolean empty) {
				super.updateItem(school, empty);
				
				if(school == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(null); // Implementing opening a new view
				setGraphic(stackPane);
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnREMOVE.setCellFactory(cell -> new TableCell<School, School>() {
			private final Button button = new Button("Excluir");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(School school, boolean empty) {
				super.updateItem(school, empty);
				
				if(school == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(null); // Implementing opening a new view
				setGraphic(stackPane);
			}
		});
	}
}
