package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import db.DBException;
import entities.Child;
import entities.LevelEducation;
import entities.Nutritionist;
import entities.School;
import entities.service.NutritionistService;
import gui.listeners.DataChangeListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Alerts;
import util.Utils;

public class LevelEducationController implements Initializable, DataChangeListener{
	
	private Nutritionist nutritionist;
	
	private NutritionistService service;
	
	private School school;
	
	private LevelEducation levelEducation;
	
	@FXML
	private Label nutritionistName;
	
	@FXML
	private Label nutritionistIdentifier;
	
	@FXML
	private Label schoolName;
	
	@FXML
	private Label schoolIdentifier;
	
	@FXML
	private Label levelEducationName;
	
	@FXML 
	private Label numberStudentsLevelEducation;
	
	@FXML
	private TableView<Child> tableViewChild;
	
	@FXML
	private TableColumn<Child, String> tableColumnName;
	
	@FXML
	private TableColumn<Child, LocalDate> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Child, String> tableColumnSex;
	
	@FXML
	private TableColumn<Child, Double> tableColumnLastZscoreMeasurement;
	
	@FXML
	private TableColumn<Child, Child> tableColumnSEE;
	
	@FXML
	private TableColumn<Child, Child> tableColumnEDIT;
	
	@FXML
	private TableColumn<Child, Child> tableColumnREMOVE;
	
	@FXML
	private Button logout;
	
	@FXML
	private Button goBack;
	
	@FXML
	private Button addNewChild;
	
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
		nutritionistName.setText(nutritionist.getName());
		nutritionistIdentifier.setText(nutritionist.getRegionalCouncilNutritionists());
	}
	
	public void setNutritionistService(NutritionistService service) {
		this.service = service;
	}
	
	public void setSchool(School school) {
		this.school = school;
		schoolName.setText(school.getName());
		schoolIdentifier.setText(school.getNationalRegistryLegalEntities());
	}
	
	public void setLevelEducation(LevelEducation levelEducation) {
		this.levelEducation = levelEducation;
		levelEducationName.setText(levelEducation.getName());
		numberStudentsLevelEducation.setText(String.valueOf(levelEducation.getAllChildren().size()));
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../gui/School_view.fxml"));
			AnchorPane achorPane = loader.load();
			
			SchoolController schoolController = loader.getController();
			schoolController.setNutritionist(nutritionist);
			schoolController.setSchool(school);
			schoolController.setNutritionistService(new NutritionistService());
			schoolController.updateTableViewLevelEducation();
			
			Stage currentStage = Utils.getCurrentStage(event);
			currentStage.setScene(new Scene(achorPane));
			
			currentStage.setTitle("Visualização da Escola");
			currentStage.setResizable(false);
		} catch (IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível carregar a visualização da escola. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TableColumn Name
		tableColumnName.setCellFactory(cell -> new TableCell<Child, String>(){
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
		
		// TableColumn Zscore Measurement
		tableColumnLastZscoreMeasurement.setCellFactory(cell -> new TableCell<Child, Double>(){
			@Override
			protected void updateItem(Double zscoreMeasurement, boolean empty) {
				super.updateItem(zscoreMeasurement, empty);
				
				if(zscoreMeasurement == null) {
					setGraphic(null);
					return;
				}
				
				setText(String.format("%.4f", zscoreMeasurement));
				setAlignment(Pos.CENTER);
			}
		});
		
		tableColumnLastZscoreMeasurement.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getLatestZscoreMeasurement()).asObject());
		
		// TableColumn Date of Birth
		
		tableColumnBirthDate.setCellFactory(cell -> new TableCell<Child, LocalDate>() {
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
		
		tableColumnBirthDate.setCellValueFactory(param -> new SimpleObjectProperty<LocalDate>(param.getValue().getDateBirth()
																									 .toInstant()
																									 .atZone(ZoneId.systemDefault())
																									 .toLocalDate()));
		
		// TableColumn Sex
		
		tableColumnSex.setCellFactory(cell -> new TableCell<Child, String>() {
			@Override
			protected void updateItem(String sex, boolean empty) {
				super.updateItem(sex, empty);
				
				if(sex == null) {
					setGraphic(null);
					return;
				}
				
				setText(sex);
				setAlignment(Pos.CENTER);
			}
		});
		
		tableColumnSex.setCellValueFactory(param -> new SimpleStringProperty(Utils.formatSex(param.getValue().getSex())));
	}
	
	private void createDialogForm(Child child, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			AnchorPane anchorPane = loader.load();
			
			if(nutritionist == null) {
				throw new IllegalStateException("Nutritionist entity was null");
			}
			
			if(school == null) {
				throw new IllegalStateException("School entity was null");
			}
			
			if(levelEducation == null) {
				throw new IllegalStateException("LevelEducation entity was null");
			}
			
			ChildFormController childFormController = loader.getController();
			childFormController.setNutritionist(nutritionist);
			childFormController.setLevelEducation(levelEducation);
			childFormController.setNutritionistService(new NutritionistService());
			childFormController.setChild(child);
			childFormController.subscribeDataChangeListener(this);
			childFormController.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro de Aluno(a)");
			dialogStage.setScene(new Scene(anchorPane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			
			// Removing the title bar
			Stage currentStage = (Stage) dialogStage.getScene().getWindow();
			currentStage.initStyle(StageStyle.UNDECORATED);
			
			Utils.addIcon(dialogStage, "../resources/form.png");
			dialogStage.show();
		}
		catch(IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela solicitada. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	@FXML
	public void onAddNewChild(ActionEvent event) {
		createDialogForm(new Child(), "../../gui/ChildForm_view.fxml", Utils.getCurrentStage(event));
	}
	
	public void updateTableViewChild() {
		if(levelEducation == null) {
			throw new IllegalStateException("LevelEducation entity was null");
		}
		
		ObservableList<Child> obsList = FXCollections.observableArrayList(levelEducation.getAllChildren());
		tableViewChild.setItems(obsList);
		initSeeButtons();
		initEditButtons();
		initRemoveButtons();
	}
	
	@Override
	public void onDataChanged() {
		updateTableViewChild();
	}
	
	private void createDialogView(String absoluteName, Stage parentStage, Child child) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			AnchorPane anchorPane = loader.load();
			
			if(nutritionist == null) {
				throw new IllegalStateException("Nutritionist entity was null");
			}
			
			if(school == null) {
				throw new IllegalStateException("School entity was null");
			}
			
			if(levelEducation == null) {
				throw new IllegalStateException("LevelEducation entity was null");
			}
			
			ChildController childController = loader.getController();
			childController.setNutritionist(nutritionist);
			childController.setChild(child);
			childController.setNutritionistService(new NutritionistService());
			childController.updateTableViewMeasures();
			childController.subscribeDataChangeListener(this);
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Visualização de Medições do Aluno(a)");
			dialogStage.setScene(new Scene(anchorPane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			
			// Removing the title bar
			Stage currentStage = (Stage) dialogStage.getScene().getWindow();
			currentStage.initStyle(StageStyle.UNDECORATED);
			
			dialogStage.show();
		}
		catch(IOException e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela de visualização de medidas. Tente novamente mais tarde.", AlertType.ERROR);
		}
	}
	
	private void initSeeButtons() {
		tableColumnSEE.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnSEE.setCellFactory(cell -> new TableCell<Child, Child>(){
			private final Button button = new Button("Ver");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(Child child, boolean empty) {
				super.updateItem(child, empty);
				
				if(child == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> createDialogView("../../gui/Child_view.fxml", Utils.getCurrentStage(event), child));
				setGraphic(stackPane);
			}
		});
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnEDIT.setCellFactory(cell -> new TableCell<Child, Child>(){
			private final Button button = new Button("Editar");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(Child child, boolean empty) {
				super.updateItem(child, empty);
				
				if(child == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> createDialogForm(child, "../../gui/ChildForm_view.fxml", Utils.getCurrentStage(event)));
				setGraphic(stackPane);
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
		tableColumnREMOVE.setCellFactory(cell -> new TableCell<Child, Child>(){
			private final Button button = new Button("Excluir");
			private final StackPane stackPane = new StackPane(button);
			
			@Override
			protected void updateItem(Child child, boolean empty) {
				super.updateItem(child, empty);
				
				if(child == null) {
					setGraphic(null);
					return;
				}
				
				button.setPrefWidth(65.0);
				button.setOnAction(event -> removeChild(child));
				setGraphic(stackPane);
			}
		});
	}
	
	private void removeChild(Child child) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Você tem certeza que deseja excluir?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
				levelEducation.removeChild(child);
				
				if(service.update(nutritionist)) {
					Alerts.showAlert("Sucesso", null, "Aluno removido com sucesso!", AlertType.CONFIRMATION);
					updateTableViewChild();
				}
			}
			catch(DBException e) {
				Alerts.showAlert("Erro", null, "Não foi possível realizar a ação solicitada. Tente novamente mais tarde.", AlertType.ERROR);
			}
		}
	}
}
