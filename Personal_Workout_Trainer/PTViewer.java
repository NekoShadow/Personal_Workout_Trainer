//Name: Jianfu Zhang; Andrew ID: jianfuz
package hw3;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class PTViewer {
	BorderPane root = new BorderPane();
	StackPane imageStackPane = new StackPane();  //for loading images in the selection grid

	/**GUI components for menus */
	MenuItem openWorkoutMenuItem = new MenuItem("Open");
	MenuItem saveWorkoutMenuItem = new MenuItem("Save as...");
	MenuItem playWorkoutMenuItem = new MenuItem("Play");
	MenuItem closeWorkoutMenuItem = new MenuItem("Close");
	MenuItem exitWorkoutMenuItem = new MenuItem("Exit");
	MenuItem suggestWorkoutMenuItem = new MenuItem("Suggest");
	MenuItem aboutHelpMenuItem = new MenuItem("About");

	BooleanProperty workoutStatusProperty = new SimpleBooleanProperty(); //to bind menu-options enable/disable. False indicates no file is open

	/**GUI components for inputGrid */
	Label workoutNameValue = new Label();
	Slider timeSlider = new Slider();
	TextArea notesTextArea = new TextArea();
	ComboBox<Exercise> exerciseComboBox = new ComboBox<>();
	Label repsCountValue = new Label("0");
	Label caloriesValue = new Label("0");
	Label timeValue = new Label("0");
	Label totalTimeValue = new Label("0");
	Label totalCaloriesValue = new Label("0");

	/**GUI components for selectionGrid */
	Button addButton = new Button("Add\nexercise");
	Button removeButton = new Button("Remove\nexercise");
	Button updateButton = new Button("Update notes");
	Button searchButton = new Button("Search exercise");
	TextField searchTextField = new TextField();

	TableView<Exercise> exerciseTableView = new TableView<>();
	TableColumn<Exercise, String> nameColumn = new TableColumn<>("Exercise");
	TableColumn<Exercise, String> levelColumn = new TableColumn<>("Level");
	TableColumn<Exercise, Integer> timeColumn = new TableColumn<>("Time (min.)");
	TableColumn<Exercise, Integer> repsColumn = new TableColumn<>("Reps");
	TableColumn<Exercise, Integer> caloriesColumn = new TableColumn<>("Calories");
	//Image image = new Image(getClass().getResourceAsStream(PersonalTrainer.PT_IMAGE));//this is added from hw2 PTViewer
	ImageView imageView = new ImageView();

	/** setupScreen composes the GUI view and invokes the setActions() method
	 * to tie components to action handlers
	 * @throws MissingMediaException 
	 */
	@SuppressWarnings("unchecked")
	GridPane setupScreen()  {
		exerciseComboBox.setPromptText("Select exercise");//This is added by me to set the default words when no exercise is selected.
		/** define all grids */ 
		GridPane mainGrid = new GridPane();
		mainGrid.setVgap(5);
		mainGrid.setHgap(5);
		BorderPane.setMargin(mainGrid, new Insets(10,10,10,10));

		GridPane inputGrid = new GridPane();
		inputGrid.setVgap(10);
		inputGrid.setHgap(10);
		for (int i = 0; i < 8; i++)
			inputGrid.getColumnConstraints().add(new ColumnConstraints(90)); // set all columns' width constraint

		GridPane selectionGrid = new GridPane();
		selectionGrid.setVgap(5);
		selectionGrid.setHgap(5);

		for (int i = 0; i < exerciseTableView.getColumns().size(); i++) exerciseTableView.getColumns().get(i).setPrefWidth(75);


		/** define all fixed labels */
		Label workoutLabel = new Label("Workout Name");
		Label exerciseLabel = new Label("Select exercises");
		Label timeSliderLabel = new Label("Set exercise time \n(min)");
		Label timeLabel = new Label ("Exercise time \n(min)");
		Label repsLabel = new Label("Reps \n(approx.)");
		Label caloriesLabel = new Label("Calories");
		Label totalTimeLabel = new Label("Workout time");
		Label totalCaloriesLabel = new Label("Workout calories");

		/** bind exerciseTableView columns with Exercise's properties */
		nameColumn.setCellValueFactory(new PropertyValueFactory<Exercise, String>("name"));
		levelColumn.setCellValueFactory(new PropertyValueFactory<Exercise, String>("level"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repTime"));
		repsColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repCount"));
		caloriesColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("calories"));

		/** bind columns to the table view */
		exerciseTableView.getColumns().clear();
		exerciseTableView.getColumns().addAll(nameColumn, levelColumn, timeColumn, repsColumn, caloriesColumn );

		/** setup image container and properties */
		imageStackPane.setPrefHeight(200);;
		imageStackPane.setPrefWidth(200);;
		WorkoutViewer.createViewer(PersonalTrainer.PT_IMAGE).view(imageStackPane);


		/** attach inputGrid components */
		inputGrid.add(workoutLabel, 0, 0);
		inputGrid.add(workoutNameValue, 1, 0, 5, 1);
		inputGrid.add(exerciseLabel, 0, 1);
		inputGrid.add(exerciseComboBox, 1, 1, 5, 1);
		inputGrid.add(timeSliderLabel, 3, 1, 2, 1);
		inputGrid.add(timeSlider, 4, 1, 6, 1);
		inputGrid.add(timeLabel, 3, 2, 2, 1);
		inputGrid.add(timeValue, 4, 2);
		inputGrid.add(repsLabel, 5, 2);
		inputGrid.add(repsCountValue, 6, 2);
		inputGrid.add(caloriesLabel, 7, 2);
		inputGrid.add(caloriesValue, 8, 2);

		/** attach selectionGrid components */
		selectionGrid.add(exerciseTableView, 0, 4, 3, 4);
		selectionGrid.add(new Label("Image"), 4, 4);
		selectionGrid.add(imageStackPane, 4, 5, 3, 3);
		selectionGrid.add(new Label("Notes"), 7, 4);
		selectionGrid.add(notesTextArea, 7, 5, 1, 3);
		selectionGrid.add(addButton, 8, 5, 2, 2);
		selectionGrid.add(removeButton, 8, 7, 2, 2);
		selectionGrid.add(updateButton, 7, 8, 2, 1);
		selectionGrid.add(searchButton, 0, 8);
		selectionGrid.add(searchTextField, 1, 8);

		/** setup mainGrid */
		mainGrid.add(inputGrid, 0, 0, 9, 4);
		mainGrid.add(selectionGrid, 0, 4, 9, 4);
		mainGrid.add(totalTimeLabel, 0, 10);
		mainGrid.add(totalTimeValue, 1, 10);
		mainGrid.add(totalCaloriesLabel, 2, 10);
		mainGrid.add(totalCaloriesValue, 3, 10);

		/** setup timeSlider properties */
		timeSlider.setMin(0);
		timeSlider.setMax(100);
		timeSlider.setBlockIncrement(1);
		timeSlider.setMajorTickUnit(10);
		timeSlider.showTickMarksProperty();
		timeSlider.setShowTickMarks(true);
		timeSlider.setShowTickLabels(true);
		timeSlider.setSnapToTicks(true);

		/** setup various components' sizes*/
		inputGrid.setPrefWidth(875);
		exerciseComboBox.setPrefWidth(100);
		timeSlider.setPrefWidth(450);
		exerciseTableView.setPrefSize(350, 200);
		addButton.setPrefSize(75, 80);
		removeButton.setPrefSize(75, 80);
		notesTextArea.setMaxWidth(150);
		updateButton.setPrefSize(150,  20);
		searchTextField.setPrefWidth(300);

		totalTimeLabel.setPrefWidth(100);
		totalTimeValue.setPrefWidth(50);
		totalCaloriesLabel.setPrefWidth(100);
		totalCaloriesValue.setPrefWidth(50);

		notesTextArea.setWrapText(true);
		notesTextArea.setEditable(true);

		/** setup colors etc. */
		Background b = new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, new Insets(-5,-5,-5,-5)));
		inputGrid.setBackground(b);
		b = new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, new Insets(-5,-5,-5,-5)));
		selectionGrid.setBackground(b);
		imageStackPane.setStyle("-fx-border-color:lightgray");
		notesTextArea.setStyle("-fx-border-color:lightgray");

		return mainGrid;
	}

	/** setupMenus sets us all menus and ties menu-options
	 * with their event handlers
	 */
	public void setupMenus() {
		//create menus
		Menu workoutMenu = new Menu("Workout");
		Menu toolsMenu = new Menu("Tools");
		Menu helpMenu = new Menu("Help");
		MenuBar menuBar = new MenuBar();

		//attach menus
		workoutMenu.getItems().addAll(openWorkoutMenuItem, saveWorkoutMenuItem, playWorkoutMenuItem, closeWorkoutMenuItem, exitWorkoutMenuItem);
		toolsMenu.getItems().addAll(suggestWorkoutMenuItem);
		helpMenu.getItems().addAll(aboutHelpMenuItem);
		menuBar.getMenus().addAll(workoutMenu, toolsMenu, helpMenu);
		root.setTop(menuBar);
	}

	public void setupWelcomeScreen()  {
		Label welcomeLabel = new Label ("Welcome! I am your Personal Trainer");
		welcomeLabel.setStyle("-fx-font-size:30;");
		welcomeLabel.setTextFill(Color.BLUEVIOLET);

		StackPane welcomePane = new StackPane(); //create StackPane to send to WorkoutViewer
		welcomePane.setPrefSize(400, 400);
		WorkoutViewer.createViewer(PersonalTrainer.PT_IMAGE).view(welcomePane); //show PT_IMAGE

		root.setCenter(welcomeLabel);
		root.setBottom(welcomePane);
		BorderPane.setAlignment(imageView, Pos.BOTTOM_CENTER);

	}

	void clearScreen() {
		timeSlider.setValue(0);
		exerciseTableView.getSelectionModel().selectAll();
		exerciseTableView.getSelectionModel().clearSelection();
		imageView.setImage(null);
		totalTimeValue.setText("");
		totalCaloriesValue.setText("");
		notesTextArea.clear();
		workoutNameValue.setText("");;
	}
}
