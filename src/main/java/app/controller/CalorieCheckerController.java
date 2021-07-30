package app.controller;

import app.CalorieChecker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import pkgLogic.Food;
import pkgLogic.ServerInfo;
import pkgLogic.SqlConverter;
import pkgEnums.eModes;

import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class CalorieCheckerController implements Initializable {
	private eModes mode = eModes.INSERT;
	private CalorieChecker CC = null;

	@FXML
	private ComboBox command;

	@FXML
	private TextField foodName;

	@FXML
	private TextField calories;

	@FXML
	private TextField protein;

	@FXML
	private TextField carbohydrates;

	@FXML
	private TextField fat;
	
	@FXML
	private Label lblFoodName;
	
	@FXML
	private Label lblCalories;
	
	@FXML
	private Label lblProtein;
	
	@FXML
	private Label lblCarbohydrates;
	
	@FXML
	private Label lblFat;

	@FXML
	private ListView allFoods;
	
	@FXML
	private GridPane nutritionalFacts;
	
	@FXML
	private Label foodNameOutput;
	
	@FXML
	private Label caloriesOutput;
	
	@FXML
	private Label proteinOutput;
	
	@FXML
	private Label carbohydratesOutput;
	
	@FXML
	private Label fatOutput;
	
	@FXML
	private Label insertSuccess;

	private String table = "dbo.Food";
	private String database = "testDatabase";
	private String columns = "(Name, Calories, [Protein (g)], [Carbohydrates (g)], [Fat (g)])";
	String username = "REDACTED";
	String password = "REDACTED";
	String ip = "REDACTED";
	private ServerInfo server = new ServerInfo(ip, "49172", "testDatabase", username, password);
	private SqlConverter sqlConverter = new SqlConverter(database, table, columns, server);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		insertSuccess.setVisible(false);
		btnClearFields(null);
        initializeCommand();
		addListenerToCommand();
		addListenerToAllFoods();
	}
	
	private void initializeCommand() {
		command.getItems().addAll("Insert A Food Item", "Show All Food Names", "Search For Foods", "Find Nutritional Facts About a Food Item", "Turbo Add");
		command.getSelectionModel().select(command.getItems().get(0));
		toggleCommands(command.getItems().get(0).toString());
	}
	
	private void addListenerToCommand() {
		command.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				toggleLabelsAndTextFields(t1);
				toggleCommands(t1);
				btnClearFields(null);
				changeMode(t1);
			}
		});
	}
	
	private void addListenerToAllFoods() {
		allFoods.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue != null) {
			    	command.getSelectionModel().select(command.getItems().get(3));
			    	changeMode("Find Nutritional Facts About a Food Item");
			    	foodName.setText(newValue);
			    	btnCalcFood(null);
		    	}
		    }
		});
	}

	public void setMainApp1(CalorieChecker sc) {
		this.CC = sc;
	}

	@FXML
	private void btnClearFields(ActionEvent event) {
		btnClearResults(event);
		foodName.clear();
		calories.clear();
		protein.clear();
		carbohydrates.clear();
		fat.clear();
		foodNameOutput.setText("");
		caloriesOutput.setText("");
		proteinOutput.setText("");
		carbohydratesOutput.setText("");
		fatOutput.setText("");
	}
	
	private void toggleCommands(String command) {
		allFoods.setVisible(command.equals("Show All Food Names") || command.equals("Search For Foods"));
		nutritionalFacts.setVisible(command.equals("Find Nutritional Facts About a Food Item"));
		if(command.equals("Show All Food Names")) {
			ArrayList<String> foods = sqlConverter.getLikeFoodNames("");
			allFoods.getItems().clear();
			allFoods.getItems().addAll(foods);
		}
		else if(command.equals("Search For Foods")) {
			allFoods.getItems().clear();
		}
		else if(command.equals("Find Nutritional Facts About a Food Item")) {
		}
		else if(command.equals("Insert A Food Item")) {
		}
	}
	
	private void toggleLabelsAndTextFields(String command) {
		lblFoodName.setVisible(!command.equals("Show All Food Names"));
		lblCalories.setVisible(command.equals("Insert A Food Item"));
		lblProtein.setVisible(command.equals("Insert A Food Item"));
		lblCarbohydrates.setVisible(command.equals("Insert A Food Item"));
		lblFat.setVisible(command.equals("Insert A Food Item"));
		allFoods.setVisible(command.equals("Show All Food Names"));
		foodName.setVisible(!command.equals("Show All Food Names"));
		calories.setVisible(command.equals("Insert A Food Item"));
		protein.setVisible(command.equals("Insert A Food Item"));
		carbohydrates.setVisible(command.equals("Insert A Food Item"));
		fat.setVisible(command.equals("Insert A Food Item"));
		insertSuccess.setVisible(false);
	}
	
	private void changeMode(String command) {
		if(command.equals("Insert A Food Item")) {
			mode = eModes.INSERT;
		}
		else if(command.equals("Show All Food Names")) {
			mode = eModes.VIEWALL;
		}
		else if(command.equals("Find Nutritional Facts About a Food Item")) {
			mode = eModes.VIEWONE;
		}
		else if(command.equals("Search For Foods")) {
			mode = eModes.VIEWSOME;
		}
		//TODO: add turboaddmode
		else {
			mode = eModes.ERROR;
		}
	}

	@FXML
	private void btnClearResultsKeyPress(KeyEvent event) {
		btnClearResults(null);
	}

	/**
	 * btnClearFields - Clear the input fields and output chart
	 * 
	 * @param event
	 */

	@FXML
	private void btnClearResults(ActionEvent event) {		
	}

	private boolean ValidateData() {
		StringBuilder errors = new StringBuilder();  //StringBuilder for error message
		Alert err = new Alert(AlertType.ERROR);
		err.setHeaderText("Incorrectly Entered Data");
		errors.append(checkIfFieldIsEmpty(foodName));
		if(mode == eModes.INSERT) {
			errors.append(validateNutritionInformation(calories));
			errors.append(validateNutritionInformation(protein));
			errors.append(validateNutritionInformation(carbohydrates));
			errors.append(validateNutritionInformation(fat));
		}
		if(errors.length() > 0) {
			err.setContentText(errors.toString());
			err.showAndWait();
			return false;
		}
		return true;
	}
	
	private String checkIfFieldIsEmpty(TextField textField) {
		String errorMessage = "";
		if(textField.getText().trim().isEmpty()) {
			errorMessage = textField.getId().substring(0,1).toUpperCase() + textField.getId().substring(1) + " is required\n";
		}
		return errorMessage;
	}
	
	private String validateNutritionInformation(TextField textField) {
		String errorMessage = checkIfFieldIsEmpty(textField);
		if(errorMessage.length() == 0) {
			try {
				if(Integer.parseInt(textField.getText().trim()) < 0) {
					errorMessage = "Positive " + textField.getId().substring(0,1).toUpperCase() + textField.getId().substring(1) + " is required\n";
				}
			}
			catch(NumberFormatException e) {
				errorMessage = textField.getId().substring(0,1).toUpperCase() + textField.getId().substring(1) + " must be a positive integer\n";
			}
		}
		return errorMessage;
	}

	@FXML
	private void btnCalcFood(ActionEvent event) {
		btnClearResults(event);
		if (ValidateData() == false)
			return;
		switch(mode) {
			case VIEWONE:
				ArrayList<String> food = sqlConverter.getFoodByName(foodName.getText());
				if(food.size() > 0) {
					foodNameOutput.setText(food.get(0));
					caloriesOutput.setText(food.get(1));
					proteinOutput.setText(food.get(2));
					carbohydratesOutput.setText(food.get(3));
					fatOutput.setText(food.get(4));
				}
				else {
					String notFound = foodName.getText() + " Is Not Found";
					btnClearFields(null);
					foodNameOutput.setText(notFound);
				}
				break;
			case VIEWSOME:
				ArrayList<String> foods = sqlConverter.getLikeFoodNames(foodName.getText());
				allFoods.getItems().clear();
				allFoods.getItems().addAll(foods);
				break;
			case INSERT:
				Food foodInsert = new Food(foodName.getText(), Integer.parseInt(calories.getText()), Integer.parseInt(protein.getText()), Integer.parseInt(carbohydrates.getText()), Integer.parseInt(fat.getText()));
				sqlConverter.insertFoodIntoTable(foodInsert);
				insertSuccess.setVisible(true);
				btnClearFields(null);
				break;
			//TODO add turboadder
			case ERROR:
				System.out.println("error in combobox selection");
				break;
			default:
				break;		
		}
	}
}
