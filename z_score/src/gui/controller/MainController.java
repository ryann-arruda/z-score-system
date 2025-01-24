package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entities.Nutritionist;
import entities.School;
import entities.service.NutritionistService;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Alerts;
import util.Utils;

public class MainController implements Initializable{
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
				throw new IllegalStateException("Nutritionist was null");
			}
			
			SchoolFormController schoolFormController = loader.getController();
			schoolFormController.setNutritionist(nutritionist);
			schoolFormController.setNutritionistService(new NutritionistService());
			
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
			throw new IllegalStateException("Nutritionist was null");
		}
		
		ObservableList<School> obsList = FXCollections.observableArrayList(nutritionist.getAllSchools());
		tableViewSchool.setItems(obsList);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnSchoolIdentifier.setCellValueFactory(new PropertyValueFactory<>("nationalRegistryLegalEntities"));
		tableColumnNumberStudents.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(cellData.getValue().getNumberStudents()).asObject());
	}
}
