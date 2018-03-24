//Author: Jianfu Zhang; Email: jianfuz@andrew.cmu.edu
package Personal_Workout_Trainer;

import javafx.scene.input.KeyEvent;
import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class PersonalTrainer extends Application {

	/******* all HW2 member variables here ********/
	PTViewer ptView = new PTViewer();  //will perform all view-related operations that do not need data-components
	PTData ptData = new PTData(); //will perform all data-related operations that do not need view-components
	Stage mainStage; //to be used for FileChooser in OpenHandler
	GridPane workoutGrid; //will hold the central grid populated with GUI components and will be attached to root in New and Open handlers
	DataFiler dataFiler; //will hold CSVFiler or XMLFiler
	Exercise currentExercise; //this points to whichever exercise is selected in exerciseComboBox or in exercisetableView 
	/***********************************/

	/** New or changed member variables here */
	static final String PT_DATA_PATH = "resources"; //relative path for all data files to reside 
	static final String PT_IMAGE = "personaltrainer.jpg";	//Welcome image
	static final String PT_MUSIC = "Kalimba.mp3";	//audio played in background for images
	static final String PT_YOUDIDIT_IMAGE = "youdidit.jpg";	//workout completion image

	WorkoutPlayer player = new WorkoutPlayer();  //Used in Play or Close handlers 
	static MediaPlayer videoPlayer, audioPlayer;
	/****************************/

	public static void main(String[] args) {
		launch(args);
	}

	/** start() method creates the opening screen with menus. 
	 * It also creates the screenGrid by invoking setupScreen() method
	 * of PTViewer class but doesn't attach it to the root yet	 * 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		mainStage.setTitle("Personal Trainer");
		ptView.setupMenus();
		ptView.setupWelcomeScreen();
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		ptView.root.setBackground(b);
		Scene scene = new Scene(ptView.root, 875, 500);
		mainStage.setScene(scene);
		workoutGrid = ptView.setupScreen();  //populate the grid, but don't attach to the root yet
		setActions();
		mainStage.show();
	}

	/** loadInputGrid() populates various components in the inputGrid. 
	 * It links exerciseComboBox with masterObservables
	 * It also attaches listeners to timeSlider and exerciseComboBox to change the labels for 
	 * timeValue, caloriesValue, and repsCountValue. 
	 * One new handler is for notesTextArea when user types in some text into it. 
	 * Use setOnKeyTyped() handler for this. 
	 * You may attach it here as anonymous class or as a member class in setupActions method
	 */
	private void loadInputGrid() { 
		ptView.exerciseComboBox.setItems(ptData.masterData);
		ptView.exerciseComboBox.valueProperty().addListener(new ChangeListener<Exercise>() {
			@Override
			public void changed(ObservableValue<? extends Exercise> observable, Exercise oldValue, Exercise newValue) {
				if(newValue != null){
					ptView.timeSlider.setValue(newValue.getRepTime());
					ptView.timeValue.setText(String.format("%d", newValue.getRepTime()));
					ptView.repsCountValue.setText(String.format("%d",  newValue.getRepCount()));
					ptView.caloriesValue.setText(String.format("%d", newValue.getCalories()));	
				}	
			}
		});
		//add listener to timeSlider
		ptView.timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				int sliderQty = newValue.intValue();
				int caloriesValue,repCount,repTime;
				//if an item is selected, pull its values into variables for display
				if (ptView.exerciseComboBox.getSelectionModel().getSelectedIndex() >= 0) { 
					repCount = ptView.exerciseComboBox.getSelectionModel().getSelectedItem().getRepCount();
					caloriesValue = ptView.exerciseComboBox.getSelectionModel().getSelectedItem().getCalories();
					repTime = ptView.exerciseComboBox.getSelectionModel().getSelectedItem().getRepTime();
				}
				else {
					repCount = 0;
					caloriesValue = 0;
					repTime = 0;
				}
				if(repTime!=0){
					ptView.timeValue.setText(String.format("%d",sliderQty));
					ptView.repsCountValue.setText(String.format("%d",(int)(sliderQty / repTime * repCount)));
					ptView.caloriesValue.setText(String.format("%d",(int)(sliderQty / repTime * caloriesValue)));
				}
				else{
					ptView.timeValue.setText(String.format("%d",0));
					ptView.repsCountValue.setText(String.format("%d",0));
					ptView.caloriesValue.setText(String.format("%d",0));
				}
			}
		});
	}

	/**loadSelectionGrid() reads the data from data file and populates the components in selectionGrid.  
	 * This method is called in two scenarios. One - when user opens a data file, and two- when the user invokes
	 * the menu option Tools-Suggest. The workout loaded from the data file or as suggested in the SuggestMenuItemHandler is passed as a 
	 * parameter selectedExercises to this method. This method first clears the table-view from past-data, if any, and then
	 * adds all exercises from selectedExercises array to various components.   
	 * @param selectedExercises
	 */
	void loadSelectionGrid(ObservableList<Exercise> selectedExercises) {
		ptView.notesTextArea.clear();
		//use FileChooser to choose the work-out file.
		FileChooser fc = new FileChooser();
		fc.setTitle("Select file");
		fc.setInitialDirectory(new File(PT_DATA_PATH));
		fc.getExtensionFilters().addAll(
				new ExtensionFilter("CSV Files", "*.csv"),
				new ExtensionFilter("XML Files", "*.xml"),
				new ExtensionFilter("All Files", "*.*"));
		File f=null;

		try{
			if ((f = fc.showOpenDialog(mainStage)) != null){ //Open file-dialog control, and load the data
				ptData.loadData(f.getAbsolutePath());
				ptView.workoutNameValue.setText(f.getName());
				ptView.exerciseTableView.setItems(ptData.selectedExercises);
				ptView.exerciseTableView.getSelectionModel().selectFirst();//select the first exercise in selectedExercises by default

				//show the image and notes of the first exercise in selectedExercises
				String image = ptData.selectedExercises.get(0).getImageFile();
				WorkoutViewer.createViewer(image).view(ptView.imageStackPane);
				ptView.notesTextArea.setText(ptData.selectedExercises.get(0).getExerciseNotes());

				//calculate the totalTimeValue and totalCaloriesValue
				int totalTimeValue = 0;
				int totalCaloriesValue = 0;
				for(Exercise i : ptData.selectedExercises){
					totalTimeValue = totalTimeValue + i.repTime.intValue();
					totalCaloriesValue = totalCaloriesValue + i.calories.intValue();
				}
				ptView.totalTimeValue.setText(String.format("%d", totalTimeValue));
				ptView.totalCaloriesValue.setText(String.format("%d", totalCaloriesValue));
			}
		}catch(ArrayIndexOutOfBoundsException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("File Format Error");
			alert.setHeaderText("The Personal Trainer");
			alert.setContentText("Invalid format in "+new File(f.getAbsolutePath()).getName()+"\nExpected CSV format: String, String, int, int, int, String, String");
			alert.showAndWait();
			ptView.root.setCenter(null);
			ptView.setupWelcomeScreen();
			//clear the screen
			ptView.clearScreen();
			ptView.openWorkoutMenuItem.setDisable(false);
			ptView.closeWorkoutMenuItem.setDisable(true);
			ptView.suggestWorkoutMenuItem.setDisable(true);
			ptView.saveWorkoutMenuItem.setDisable(true);
			ptView.playWorkoutMenuItem.setDisable(true);
		}
	}

	/** setActions() method attaches all action-handlers to their respective
	 * GUI components. All GUI has been defined in PTViewer.
	 */
	private void setActions() {
		//set the open and close options
		ptView.openWorkoutMenuItem.setDisable(false);
		ptView.closeWorkoutMenuItem.setDisable(true);
		ptView.saveWorkoutMenuItem.setDisable(true);
		ptView.playWorkoutMenuItem.setDisable(true);
		ptView.suggestWorkoutMenuItem.setDisable(true);
		ptView.openWorkoutMenuItem.setOnAction(new OpenWorkoutHandler());
		ptView.aboutHelpMenuItem.setOnAction(new AboutHandler());
		
		//set the event handlers
		ptView.closeWorkoutMenuItem.setOnAction(new CloseWorkoutHandler());
		ptView.addButton.setOnAction(new AddButtonHandler());
		ptView.removeButton.setOnAction(new RemoveButtonHandler());
		ptView.exitWorkoutMenuItem.setOnAction(actionEvent->Platform.exit());
		ptView.exerciseTableView.setOnMouseClicked(new SelectTableRowHandler());

		ptView.updateButton.setOnAction(new UpdateButtonHandler());
		ptView.playWorkoutMenuItem.setOnAction(new PlayWorkoutHandler());
		ptView.saveWorkoutMenuItem.setOnAction(new SaveMenuItemHandler());
		ptView.searchButton.setOnAction(new SearchButtonHandler());
		ptView.suggestWorkoutMenuItem.setOnAction(new SuggestMenuItemHandler());

		ptView.notesTextArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				ptView.updateButton.setTextFill(Color.RED);	
			}	
		});	
		ptView.searchTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				ptView.searchTextField.setStyle("-fx-text-inner-color: black;");
			}	
		});	
	}

	//write your event handlers' inner classes here

	public class AddButtonHandler implements EventHandler<ActionEvent> {	
		@Override
		public void handle(ActionEvent event) {
			Exercise selectedExcercise =	ptView.exerciseComboBox.getSelectionModel().getSelectedItem();  //get pointer to item selected in exerciseComboBox
			//create the currentExercise to be added to selectedExercises
			if(selectedExcercise!=null){
				currentExercise = new Exercise (selectedExcercise.getName(), selectedExcercise.getLevel(), (int)ptView.timeSlider.getValue(), 
						(int)ptView.timeSlider.getValue()/selectedExcercise.getRepTime(),selectedExcercise.getCalories(),
						selectedExcercise.getImageFile(),selectedExcercise.getExerciseNotes());
				ptView.exerciseTableView.getItems().add(currentExercise); //then add new list to observables
				int totalTime=0;
				int totalCalories=0;
				for (Exercise i : ptView.exerciseTableView.getItems()) {
					totalTime += i.getRepTime();
					totalCalories += i.getCalories();
				}
				ptView.totalTimeValue.setText(String.format("%d", totalTime));
				ptView.totalCaloriesValue.setText(String.format("%d", totalCalories));
			}
			//select the first exercise in selectedExercises by default
			ptView.exerciseTableView.getSelectionModel().selectFirst();
			if(ptView.exerciseTableView.getItems().size()!=0){
				currentExercise = ptView.exerciseTableView.getItems().get(0);
				String image = currentExercise.getImageFile();
				WorkoutViewer.createViewer(image).view(ptView.imageStackPane);
				ptView.notesTextArea.setText(currentExercise.getExerciseNotes());
			}
			else{
				//Image image = new Image(getClass().getClassLoader().getResource(PersonalTrainer.PT_IMAGE).toString());
				WorkoutViewer.createViewer(PersonalTrainer.PT_IMAGE).view(ptView.imageStackPane);
				ptView.notesTextArea.clear();
			}
		}
	}
	public class RemoveButtonHandler implements EventHandler<ActionEvent> {	
		@Override
		public void handle(ActionEvent event) {
			//remove the selected exercise
			Exercise selectedExcercise =	ptView.exerciseTableView.getSelectionModel().getSelectedItem();
			ptView.exerciseTableView.getItems().remove(selectedExcercise);
			//re-calculate totalTime and totalCalories
			int totalTime=0;
			int totalCalories=0;
			for (Exercise i : ptView.exerciseTableView.getItems()) {
				totalTime += i.getRepTime();
				totalCalories += i.getCalories();
			}
			ptView.totalTimeValue.setText(String.format("%d", totalTime));
			ptView.totalCaloriesValue.setText(String.format("%d", totalCalories));

			//select the first exercise in selectedExercises by default
			ptView.exerciseTableView.getSelectionModel().selectFirst();
			if(ptView.exerciseTableView.getItems().size()!=0){
				currentExercise = ptView.exerciseTableView.getItems().get(0);
				String image = currentExercise.getImageFile();
				WorkoutViewer.createViewer(image).view(ptView.imageStackPane);
				ptView.notesTextArea.setText(currentExercise.getExerciseNotes());
			}
			else{
				WorkoutViewer.createViewer(PersonalTrainer.PT_IMAGE).view(ptView.imageStackPane);
				ptView.notesTextArea.clear();
			}
		}
	}

	public class UpdateButtonHandler implements EventHandler<ActionEvent> {	
		@Override
		public void handle(ActionEvent event) {
			ptView.updateButton.setTextFill(Color.BLACK);
			Exercise selectedExcercise = ptView.exerciseTableView.getSelectionModel().getSelectedItem();
			selectedExcercise.setExerciseNotes(ptView.notesTextArea.getText());
			int i = ptView.exerciseTableView.getItems().indexOf(selectedExcercise);
			ptView.exerciseTableView.getItems().get(i).setExerciseNotes(selectedExcercise.getExerciseNotes());
		}
	}

	public class SearchButtonHandler implements EventHandler<ActionEvent> {	
		@Override
		public void handle(ActionEvent event) {
			ptView.searchTextField.setStyle("-fx-text-inner-color: black;");
			ObservableList<Exercise> searchResult = FXCollections.observableArrayList();
			String keyword = ptView.searchTextField.getText().toLowerCase();
			for(Exercise exercise : ptData.masterData){
				if(exercise.getName().toLowerCase().contains(keyword) || 
						exercise.getLevel().toLowerCase().contains(keyword) || 
						exercise.getExerciseNotes().toLowerCase().contains(keyword)){
					searchResult.add(exercise);
				}
			}
			if(keyword==null){
				ptView.searchTextField.setStyle("-fx-text-inner-color: black;");
				ptView.exerciseTableView.setItems(ptData.selectedExercises);
				ptView.exerciseTableView.getSelectionModel().selectFirst();//select the first exercise in selectedExercises by default

				//show the image and notes of the first exercise in selectedExercises
				String image = ptData.selectedExercises.get(0).getImageFile();
				WorkoutViewer.createViewer(image).view(ptView.imageStackPane);
				ptView.notesTextArea.setText(ptData.selectedExercises.get(0).getExerciseNotes());

				//calculate the totalTimeValue and totalCaloriesValue
				int totalTimeValue = 0;
				int totalCaloriesValue = 0;
				for(Exercise i : ptData.selectedExercises){
					totalTimeValue = totalTimeValue + i.repTime.intValue();
					totalCaloriesValue = totalCaloriesValue + i.calories.intValue();
				}
				ptView.totalTimeValue.setText(String.format("%d", totalTimeValue));
				ptView.totalCaloriesValue.setText(String.format("%d", totalCaloriesValue));
			}
			else if(searchResult.size()!=0){
				ptView.exerciseTableView.setItems(searchResult);
				ptView.exerciseTableView.getSelectionModel().selectFirst();//select the first exercise in selectedExercises by default

				String image = searchResult.get(0).getImageFile();
				WorkoutViewer.createViewer(image).view(ptView.imageStackPane);
				ptView.notesTextArea.setText(searchResult.get(0).getExerciseNotes());

				//calculate the totalTimeValue and totalCaloriesValue
				int totalTimeValue = 0;
				int totalCaloriesValue = 0;
				for(Exercise i : searchResult){
					totalTimeValue = totalTimeValue + i.repTime.intValue();
					totalCaloriesValue = totalCaloriesValue + i.calories.intValue();
				}
				ptView.totalTimeValue.setText(String.format("%d", totalTimeValue));
				ptView.totalCaloriesValue.setText(String.format("%d", totalCaloriesValue));
			}
			else{
				ptView.searchTextField.setText(keyword+" not found");
				ptView.searchTextField.setStyle("-fx-text-inner-color: red;");
				ptView.exerciseTableView.setItems(ptData.selectedExercises);
				ptView.exerciseTableView.getSelectionModel().selectFirst();//select the first exercise in selectedExercises by default

				//show the image and notes of the first exercise in selectedExercises
				String image = ptData.selectedExercises.get(0).getImageFile();
				WorkoutViewer.createViewer(image).view(ptView.imageStackPane);
				ptView.notesTextArea.setText(ptData.selectedExercises.get(0).getExerciseNotes());

				//calculate the totalTimeValue and totalCaloriesValue
				int totalTimeValue = 0;
				int totalCaloriesValue = 0;
				for(Exercise i : ptData.selectedExercises){
					totalTimeValue = totalTimeValue + i.repTime.intValue();
					totalCaloriesValue = totalCaloriesValue + i.calories.intValue();
				}
				ptView.totalTimeValue.setText(String.format("%d", totalTimeValue));
				ptView.totalCaloriesValue.setText(String.format("%d", totalCaloriesValue));
			}

		}
	}

	/**OpenWorkoutHandler has been provided as a dummy to display the
	 * workoutGrid. The workoutGrid needs to be populated with data
	 * in this handler
	 */
	private class OpenWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ptData.selectedExercises.clear();
			ptData.masterData.clear();
			ptView.root.setBottom(null);
			ptView.root.setCenter(workoutGrid);
			//Both populates the contents of inputGrid and selectionGrid
			loadSelectionGrid(ptData.masterData);
			loadInputGrid();
			ptView.exerciseTableView.getSelectionModel().selectFirst();//select the first exercise by default when opening the file
			if(ptData.selectedExercises.size()!=0){
				//set the open and close options
				ptView.openWorkoutMenuItem.setDisable(true);
				ptView.closeWorkoutMenuItem.setDisable(false);
				ptView.suggestWorkoutMenuItem.setDisable(false);
				ptView.saveWorkoutMenuItem.setDisable(false);
				ptView.playWorkoutMenuItem.setDisable(false);

				ptView.notesTextArea.setText(ptData.selectedExercises.get(0).getExerciseNotes());
			}	
		}
	}

	private class CloseWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ptView.root.setCenter(null);
			ptView.setupWelcomeScreen();
			//clear the screen
			ptView.clearScreen();
			//set the open and close options
			ptView.openWorkoutMenuItem.setDisable(false);
			ptView.closeWorkoutMenuItem.setDisable(true);
			ptView.suggestWorkoutMenuItem.setDisable(true);
			ptView.saveWorkoutMenuItem.setDisable(true);
			ptView.playWorkoutMenuItem.setDisable(true);
			ptView.searchTextField.clear();
			ptView.exerciseTableView.getItems().clear();
		}
	}

	public class SelectTableRowHandler implements EventHandler<MouseEvent> {	
		@Override
		public void handle(MouseEvent event) {
			ptView.updateButton.setTextFill(Color.BLACK);
			Exercise selectedExcercise = ptView.exerciseTableView.getSelectionModel().getSelectedItem();  //get pointer to item selected in exerciseTableView
			if (ptView.exerciseTableView.getSelectionModel().getSelectedIndex() >= 0) { 

				String image = selectedExcercise.getImageFile();
				WorkoutViewer.createViewer(image).view(ptView.imageStackPane);
				ptView.notesTextArea.setText(selectedExcercise.getExerciseNotes());
			}

		}
	}
	private class AboutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("The Personal Trainer");
			alert.setContentText("Version 1.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResourceAsStream(PT_IMAGE));
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);

			alert.showAndWait();
		}
	}

	//To be attached to Play menu item that will be activated only when a file is open.
	//You should not need to make any changes to this handler. 
	private class PlayWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			player.pauseButton.setDisable(false);  //if last playlist was completed, the button would be disabled
			player.pauseButton.setText("Pause");  //if the player was closed with this button on Resume status, it needs to be reset back to "Pause"
			player.playWorkout(ptData.selectedExercises); //play the exercises stored in selectedExercises
		}
	}

	private class SaveMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//use FileChooser to save the work-out file.
			FileChooser fc = new FileChooser();
			fc.setTitle("Save file");
			fc.setInitialDirectory(new File(PersonalTrainer.PT_DATA_PATH));
			fc.getExtensionFilters().addAll(
					new ExtensionFilter("Text Files", "*.csv"),
					new ExtensionFilter("Text Files", "*.xml"),
					new ExtensionFilter("Text Files", "*.txt"),
					new ExtensionFilter("All Files", "*.*"));
			fc.setInitialFileName("NewWorkout.csv");
			CSVFiler cf = new CSVFiler();
			File file;
			if((file = fc.showSaveDialog(mainStage))!=null){
				cf.writeData(ptView.exerciseTableView.getItems(), file);
			}
			ptData.masterData = FXCollections.observableArrayList(ptView.exerciseTableView.getItems());
			if(file!=null){
				ptView.workoutNameValue.setText(file.getName());
			}
			ptView.exerciseComboBox.setItems(ptData.masterData);
		}
	}

	private class SuggestMenuItemHandler implements EventHandler<ActionEvent> {
		Stage suggestStage = new Stage();
		TextField timeTextField = new TextField("0");
		TextField caloriesTextField = new TextField("0");
		@Override
		public void handle(ActionEvent event) {
			GridPane suggestRoot = new GridPane();
			Scene suggestScene = new Scene(suggestRoot, 300, 250);
			Text Message = new Text("0 input will be ignored");
			Text timeText = new Text("Enter time in minutes");
			Text caloriesText = new Text("Enter calories to burn");
			Button suggestBtn = new Button();
			Button cancelBtn = new Button();
			suggestRoot.setAlignment(Pos.CENTER);
			suggestRoot.setHgap(10);
			suggestRoot.setVgap(10);
			suggestBtn.setText("Suggest");
			cancelBtn.setText("Cancel");
			suggestBtn.setOnAction(new suggestButtonHandler());
			cancelBtn.setOnAction(new cancelButtonHandler());
			suggestRoot.add(timeText, 0, 0);
			suggestRoot.add(timeTextField,  1, 0);
			suggestRoot.add(caloriesText, 0, 1);
			suggestRoot.add(caloriesTextField, 1, 1);
			suggestRoot.add(Message, 0, 2);
			suggestRoot.add(suggestBtn, 0, 3);
			suggestRoot.add(cancelBtn, 1, 3);
			suggestStage.setTitle("Workout Suggestion");
			suggestStage.setX(150);
			suggestStage.setY(120);
			suggestStage.setScene(suggestScene);
			suggestStage.show();
		}
		private class cancelButtonHandler implements EventHandler<ActionEvent>{
			@Override
			public void handle(ActionEvent event) {
				suggestStage.close();
			}
		}

		private class suggestButtonHandler implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) {
				int timeInput = Integer.parseInt(timeTextField.getText());
				int caloriesInput = Integer.parseInt(caloriesTextField.getText());
				Workout w = new Workout();
				suggestStage.close();
				ObservableList<Exercise> suggestedExercises = w.buildWorkoutPlan(ptData.masterData, timeInput, caloriesInput);
				ptView.exerciseTableView.setItems(suggestedExercises);
				ptView.exerciseTableView.getSelectionModel().selectFirst();//select the first exercise in selectedExercises by default

				String image = suggestedExercises.get(0).getImageFile();
				WorkoutViewer.createViewer(image).view(ptView.imageStackPane);
				ptView.notesTextArea.setText(suggestedExercises.get(0).getExerciseNotes());

				//calculate the totalTimeValue and totalCaloriesValue
				int totalTimeValue = 0;
				int totalCaloriesValue = 0;
				for(Exercise i : suggestedExercises){
					totalTimeValue = totalTimeValue + i.repTime.intValue();
					totalCaloriesValue = totalCaloriesValue + i.calories.intValue();
				}
				ptView.totalTimeValue.setText(String.format("%d", totalTimeValue));
				ptView.totalCaloriesValue.setText(String.format("%d", totalCaloriesValue));
				timeTextField.setText("0");
				caloriesTextField.setText("0");
			}
		}
	}
}
