//Author: Jianfu Zhang; Email: jianfuz@andrew.cmu.edu
package Personal_Workout_Trainer;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WorkoutPlayer {
	SequentialTransition sequence = new SequentialTransition();;
	double totalBurned;
	int totalTime;

	//GUI components
	Stage playStage = new Stage();
	StackPane imageStackPane = new StackPane();  		//holds the image or video that gets plugged in scene
	BorderPane workoutBorderPane = new BorderPane();	//holds entire play screen
	GridPane counterGrid = new GridPane();				//holds total time and calories counters at the bottom
	GridPane buttonGrid = new GridPane();				//holds Play/Pause/Resume/Stop buttons 
	VBox bottomBox = new VBox();						//holds counterGrid and buttonGrid

	Label timerLabel = new Label();									//timer label at the top
	StringProperty timeSeconds = new SimpleStringProperty("0");		//carries the time count-down at the top
	Button stopButton = new Button("Stop");
	Button pauseButton = new Button("Pause");
	Label exerciseNameLabel = new Label();
	Label caloriesLabel = new Label("Calories"); 
	Label caloriesValueLabel = new Label("0");
	Label totalTimeLabel = new Label("Total time");		//timer label at the bottom
	Label totalTimeValueLabel = new Label("0 : 0 : 0");	//time counter at the bottom


	//takes list of exercises to played and gets the play screen started
	void playWorkout(ObservableList<Exercise> selectedExercises) {
		if (workoutBorderPane.getChildren().isEmpty()) { //if coming here first time
			setupPlayerScreen();  
			setupActions();
			setupTimelines(selectedExercises);
			playStage.initModality(Modality.APPLICATION_MODAL);		//to disable control from going to parent stage
			playStage.setScene(new Scene(workoutBorderPane, 475, 500));
			playStage.setTitle("Enjoy the workout!");
		} else {		//if player was played earlier, reset pause button and audio player
			PersonalTrainer.audioPlayer.seek(Duration.ZERO);
			PersonalTrainer.audioPlayer.play();
			pauseButton.setDisable(false);  //if last playlist was completed, the button would be disabled
			pauseButton.setText("Pause");  //if the player was closed with this button on Resume status, it needs to be reset back to "Pause"
		}
		sequence.play();
		playStage.show();
	}

	//sets up GUI of play screen
	void setupPlayerScreen() {
		GridPane topGrid = new GridPane();
		DropShadow dropShadow = new DropShadow(15, Color.GRAY);

		imageStackPane.setCenterShape(true);
		imageStackPane.setEffect(dropShadow);

		workoutBorderPane.setTop(topGrid);
		workoutBorderPane.setCenter(imageStackPane); 
		workoutBorderPane.setBottom(bottomBox);

		topGrid.setAlignment(Pos.CENTER);
		buttonGrid.setAlignment(Pos.CENTER);
		counterGrid.setAlignment(Pos.CENTER);
		caloriesLabel.setAlignment(Pos.CENTER_RIGHT);
		caloriesValueLabel.setAlignment(Pos.CENTER);

		topGrid.setVgap(10);
		topGrid.setHgap(10);
		buttonGrid.setVgap(10);
		buttonGrid.setHgap(10);

		topGrid.add(exerciseNameLabel, 0, 0);
		topGrid.add(timerLabel, 1, 0);
		bottomBox.getChildren().addAll(counterGrid, buttonGrid);
		buttonGrid.add(stopButton, 0, 1);
		buttonGrid.add(pauseButton, 1, 1);

		imageStackPane.setPrefSize(400, 300);
		timerLabel.setPrefSize(150, 30);
		totalTimeLabel.setPrefSize(150, 30);
		totalTimeValueLabel.setPrefSize(150,30);
		caloriesLabel.setPrefSize(150, 30);
		caloriesValueLabel.setPrefSize(150, 30);
		stopButton.setPrefSize(150, 30);
		pauseButton.setPrefSize(150, 30);

		topGrid.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		counterGrid.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		buttonGrid.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		imageStackPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		exerciseNameLabel.setTextFill(Color.BLACK);
		exerciseNameLabel.setStyle("-fx-font-size: 1.5em;");
		timerLabel.setTextFill(Color.RED);
		timerLabel.setStyle("-fx-font-size: 2em;");
		totalTimeLabel.setStyle("-fx-font-size: 1.5em;");
		totalTimeLabel.setTextFill(Color.BLACK);
		totalTimeValueLabel.setStyle("-fx-font-size: 2em;");
		totalTimeValueLabel.setTextFill(Color.RED);
		caloriesLabel.setTextFill(Color.BLACK);
		caloriesLabel.setStyle("-fx-font-size: 1.5em;");
		caloriesValueLabel.setTextFill(Color.RED);
		caloriesValueLabel.setStyle("-fx-font-size: 2em;");
		stopButton.setTextFill(Color.RED);
		pauseButton.setTextFill(Color.BLUE);
		stopButton.setStyle("-fx-font-size: 2em; -fx-background-radius:0.5;");
		pauseButton.setStyle("-fx-font-size: 2em; -fx-background-radius:0.5;");
		stopButton.setEffect(dropShadow);
		pauseButton.setEffect(dropShadow);
	}

	//Ties up eventhandlers for Stop and Pause buttons
	void setupActions() {
		//Pause button changes its text based on Player status. 
		//When it is playing, the button has "Pause" on it
		//When it is paused, it has "Resume" on it.
		pauseButton.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent event) {
				String buttonText = ((Button)event.getSource()).getText();
				switch (buttonText) {
				case "Pause" : {
					pauseButton.setText("Resume");
					pauseButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
					sequence.pause();
					if (PersonalTrainer.videoPlayer != null && PersonalTrainer.videoPlayer.getStatus().equals(Status.PLAYING)) PersonalTrainer.videoPlayer.pause();;
					if (!PersonalTrainer.audioPlayer.isMute()) PersonalTrainer.audioPlayer.setMute(true);
					break;
				}
				case "Resume" : {
					pauseButton.setText("Pause");
					pauseButton.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
					sequence.play();
					if (PersonalTrainer.videoPlayer != null && PersonalTrainer.videoPlayer.getStatus().equals(Status.PAUSED)) PersonalTrainer.videoPlayer.play();
					else if (PersonalTrainer.audioPlayer.isMute()) PersonalTrainer.audioPlayer.setMute(false);
					break;
				}
				}
			}
		});

		//Close the player when window-x button on top-right is clicked, or Stop button is pressed
		playStage.setOnHidden(event -> closePlayer());
		stopButton.setOnAction(event -> closePlayer());
	}		


	void closePlayer() {
		totalTime = 0;  //reset total time counter to zero
		totalBurned = 0; //reset calories to zero
		playStage.close();
		sequence.stop();
		sequence.jumpTo(Duration.ZERO);
		if (!PersonalTrainer.audioPlayer.isMute()) PersonalTrainer.audioPlayer.stop();
		if (PersonalTrainer.videoPlayer != null && PersonalTrainer.videoPlayer.getStatus().equals(Status.PLAYING)) PersonalTrainer.videoPlayer.stop();
	}

	void setupTimelines(ObservableList<Exercise> selectedExercises) {
		Timeline[] timeline = new Timeline[selectedExercises.size()];  //create one timeline for each exercise

		ArrayList<KeyFrame> startupFrames = new ArrayList<>();		//this will have frames for each break
		ArrayList<KeyFrame> keyFrames = new ArrayList<>();			//this will have frames for each exercise

		timerLabel.textProperty().bind(timeSeconds);  				//tie the label with its value
		KeyFrame kf;
		for (int i = 0; i < selectedExercises.size(); i++) {   		//traverse through all exercises one by one
			final int f = i;
			timeline[i] = new Timeline();  							//a new timeline for each exercise
			kf = new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent> () {  // set startup frame image 
				@Override
				public void handle(ActionEvent event) {
					exerciseNameLabel.setText("Get ready for " + selectedExercises.get(f).getLevel() + " " + selectedExercises.get(f).getName() + " in ");
					WorkoutViewer.createViewer(PersonalTrainer.PT_IMAGE).view(imageStackPane);  //load startup image
					counterGrid.getChildren().remove(caloriesLabel);		//clear old labels
					counterGrid.getChildren().remove(caloriesValueLabel);
					counterGrid.getChildren().remove(totalTimeLabel);
					counterGrid.getChildren().remove(totalTimeValueLabel);
				}
			});
			startupFrames.add(kf);

			// set countdown timer frames at the beginning of each exercise 
			for (int j = 10; j >= 0 ; j--)  {	
				final int h = j;
				kf = new KeyFrame(Duration.seconds(10-j), new EventHandler<ActionEvent> () { //countdown from 10 to 0
					public void handle(ActionEvent event) {
						WorkoutViewer.createViewer(PersonalTrainer.PT_IMAGE);
						timeSeconds.setValue(Integer.toString(h).concat(" sec"));
					}
				});
				startupFrames.add(kf);	
			}

			// set viewer frame for exercise starting at 10th second after countdown
			kf = new KeyFrame(Duration.seconds(11), new EventHandler<ActionEvent> () {  
				@Override
				public void handle(ActionEvent event) {
					WorkoutViewer.createViewer(selectedExercises.get(f).getImageFile()).view(imageStackPane);
					exerciseNameLabel.setText(selectedExercises.get(f).getRepCount() + " " + selectedExercises.get(f).getLevel() + " " + selectedExercises.get(f).getName());
					counterGrid.add(totalTimeLabel, 0, 0);
					counterGrid.add(totalTimeValueLabel, 1, 0);
					counterGrid.add(caloriesLabel, 2, 0);
					counterGrid.add(caloriesValueLabel, 3, 0);
				}
			});
			keyFrames.add(kf);
			double calBurnRate = (double)selectedExercises.get(i).getCalories()/(selectedExercises.get(i).getRepTime()*60);
			Duration timerDuration = Duration.seconds(11); //starting point for next frame is after 10 seconds

			// set exercise timer frames
			for (int j = 0; j <= selectedExercises.get(i).getRepTime()*60 ; j++ ) {  // from 0 to 59 seconds for each reptime min
				totalTime = 0;
				final int k = j;
				kf = new KeyFrame(timerDuration , new EventHandler<ActionEvent>() {  //
					@Override
					public void handle(ActionEvent event) {
						int time = selectedExercises.get(f).getRepTime()*60  - k;
						timeSeconds.setValue(String.format("%2d : %2d : %2d", (int)time / 3600,(int)(time%3600)/60, time % 60));
						totalTimeValueLabel.setText(String.format("%2d : %2d : %2d", (int)totalTime / 3600,(int)(totalTime%3600)/60, totalTime % 60));
						if (time != 0) totalTime += 1;   //do not increment after last second
						totalBurned += calBurnRate;
						caloriesValueLabel.setText(Integer.toString((int)totalBurned));
					}
				});
				keyFrames.add(kf);
				timerDuration = timerDuration.add(Duration.seconds(1));
			}
			timeline[i].getKeyFrames().addAll(startupFrames);
			timeline[i].getKeyFrames().addAll(keyFrames);
			startupFrames.clear();
			keyFrames.clear();
		}

		//add last timeline with only one frame that has PT_YOUDIDIT_IMAGE  
		ArrayList<KeyFrame> lastFrames = new ArrayList<>();
		kf = new KeyFrame(Duration.ONE, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				WorkoutViewer.createViewer(PersonalTrainer.PT_YOUDIDIT_IMAGE).view(imageStackPane);
				exerciseNameLabel.setText("Done!");
				pauseButton.setDisable(true);
			}
		});
		lastFrames.add(kf);

		Timeline lastTimeline = new Timeline();
		lastTimeline.getKeyFrames().addAll(lastFrames);

		//String all timelines together to form a full sequence
		for (int i = 0; i < timeline.length; i++) sequence.getChildren().add(timeline[i]);
		sequence.getChildren().add(lastTimeline);
	}
}


