package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import entities.Child;
import entities.LevelEducation;
import entities.Nutritionist;
import entities.School;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.stage.Stage;
import util.Alerts;
import util.Utils;

public class LevelEducationController implements Initializable{
	
	private Nutritionist nutritionist;
	
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
	private TableColumn<Child, Date> tableColumnBirthDate;
	
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
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("date_birth"));
		tableColumnLastZscoreMeasurement.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getLatestZscoreMeasurement()).asObject());
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
				
				button.prefWidth(65.0);
				button.setOnAction(null);
				setGraphic(stackPane);
			}
		});
	}
}
