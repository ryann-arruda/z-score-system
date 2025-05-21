package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import db.DBException;
import entities.LevelEducation;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

public class SchoolController implements Initializable, DataChangeListener{
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
	private School school;
	
	@FXML
	private Label nutritionistName;
	
	@FXML
	private Label nutritionistIdentifier;
	
	@FXML
	private Label schoolName;
	
	@FXML
	private Label schoolIdentifier;
	
	@FXML
	private TableView<LevelEducation> tableViewLevelEducation;
	
	@FXML
	private TableColumn<LevelEducation, String> tableColumnName;
	
	@FXML
	private TableColumn<LevelEducation, Integer> tableColumnNumberStudents;
	
	@FXML
	private TableColumn<LevelEducation, LevelEducation> tableColumnSEE;
	
	@FXML
	private TableColumn<LevelEducation, LevelEducation> tableColumnEDIT;
	
	@FXML
	private TableColumn<LevelEducation, LevelEducation> tableColumnREMOVE;
	
	@FXML
	private Button logout;
	
	@FXML
	private Button goBack;
	
	@FXML
	private Button addNewEducationLevel;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
		this.nutritionistName.setText(nutritionist.getName());
		this.nutritionistIdentifier.setText(nutritionist.getRegionalCouncilNutritionists());
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	public void setSchool(School school) {
		this.school = school;
		this.schoolName.setText(school.getName());
		this.schoolIdentifier.setText(school.getNationalRegistryLegalEntities());
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
	public void onGoBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/Main_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			MainController mainController = loader.getController();
			mainController.setNutritionist(nutritionist);
			mainController.setNutritionistService(new NutritionistService());
			mainController.updateTableViewSchool();
			
			Stage currentStage = Utils.getCurrentStage(event);
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("Painel Principal");
			currentStage.setResizable(false);
		} catch (IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível carregar o painel principal. Tente novamente mais tarde.", AlertType.ERROR);
		}	
	}
	
	private void createDialogForm(LevelEducation levelEducation, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			AnchorPane anchorPane = loader.load();
			
			if(nutritionist == null) {
				throw new IllegalStateException("Nutritionist entity was null");
			}
			
			if(school == null) {
				throw new IllegalStateException("School entity was null");
			}
			
			LevelEducationFormController levelEducationFormController = loader.getController();
			levelEducationFormController.setNutritionist(nutritionist);
			levelEducationFormController.setNutritionistService(new NutritionistService());
			levelEducationFormController.setSchool(school);
			levelEducationFormController.setLevelEducation(levelEducation);
			levelEducationFormController.subscribeDataChangeListener(this);
			levelEducationFormController.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados dos Níveis Educacionais");
			dialogStage.setScene(new Scene(anchorPane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			Utils.addIcon(dialogStage, "../resources/form.png");
			dialogStage.show();
		}
		catch(IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela solicitada. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	@FXML
	public void onAddNewEducationLevel(ActionEvent event) {
		createDialogForm(new LevelEducation(), "../../gui/LevelEducationForm_view.fxml", Utils.getCurrentStage(event));
	}
	
	public void updateTableViewLevelEducation() {
		if(school == null) {
			throw new IllegalStateException("School entity was null");
		}
		
		ObservableList<LevelEducation> obsList = FXCollections.observableArrayList(school.getAllEducationLevels());
		tableViewLevelEducation.setItems(obsList);
		initSeeButtons();
		initEditButtons();
		initRemoveButtons();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TableColumn Name
		tableColumnName.setCellFactory(cell -> new TableCell<LevelEducation, String>(){
			
			@Override
			protected void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				
				if(name == null) {
					setGraphic(null);
					return;
				}
				
				setText(name);
				setAlignment(Pos.CENTER);
			}
		});
		
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		// TableColumn Number of Students
		tableColumnNumberStudents.setCellFactory(cell -> new TableCell<LevelEducation, Integer>(){
			
			@Override
			protected void updateItem(Integer numberStudents, boolean empty) {
				super.updateItem(numberStudents, empty);
				
				if(numberStudents == null) {
					setGraphic(null);
					return;
				}
				
				setText(numberStudents.toString());
				setAlignment(Pos.CENTER);
			}
		});
		
		tableColumnNumberStudents.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(cellData.getValue().getNumberStudents()).asObject());
	}
	
	@Override
	public void onDataChanged() {
		updateTableViewLevelEducation();
	}
	
	private void loadLevelEducationView(ActionEvent event, LevelEducation levelEducation) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/LevelEducation_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			LevelEducationController levelEducationController = loader.getController();
			levelEducationController.setNutritionist(nutritionist);
			levelEducationController.setNutritionistService(new NutritionistService());
			levelEducationController.setSchool(school);
			levelEducationController.setLevelEducation(levelEducation);
			levelEducationController.updateTableViewChild();
			
			Stage currentStage = Utils.getCurrentStage(event);
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("Visualização do Nível Educacional");
			currentStage.setResizable(false);
		} catch (IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível carregar a visualização do nível educacional selecionado. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	private void initSeeButtons() {
		tableColumnSEE.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnSEE.setCellFactory(cell -> new TableCell<LevelEducation, LevelEducation>(){
			private final Button button = new Button("Ver");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(LevelEducation levelEducation, boolean empty) {
				super.updateItem(levelEducation, empty);
				
				if(levelEducation == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> loadLevelEducationView(event, levelEducation));
				setGraphic(stackPane);
			}
		});
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnEDIT.setCellFactory(cell -> new TableCell<LevelEducation, LevelEducation>() {
			private final Button button = new Button("Editar");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(LevelEducation levelEducation, boolean empty) {
				super.updateItem(levelEducation, empty);
				
				if(levelEducation == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> createDialogForm(levelEducation, "../../gui/LevelEducationForm_view.fxml", Utils.getCurrentStage(event))); 
				setGraphic(stackPane);
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnREMOVE.setCellFactory(cell -> new TableCell<LevelEducation, LevelEducation>() {
			private final Button button = new Button("Excluir");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(LevelEducation levelEducation, boolean empty) {
				super.updateItem(levelEducation, empty);
				
				if(levelEducation == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> removeLevelEducation(levelEducation)); 
				setGraphic(stackPane);
			}
		});
	}
	
	private void removeLevelEducation(LevelEducation levelEducation) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Você tem certeza que deseja excluir?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
				school.removeEducationLevel(levelEducation);
				
				if(service.update(nutritionist)) {
					Alerts.showAlert("Sucesso", null, "Nível Educacional removido com sucesso!", AlertType.CONFIRMATION);
					updateTableViewLevelEducation();
				}
			}
			catch(DBException e) {
				Alerts.showAlert("Erro", null, "Não foi possível realizar a ação solicitada. Tente novamente mais tarde.", AlertType.ERROR);
			}
		}
	}
}
