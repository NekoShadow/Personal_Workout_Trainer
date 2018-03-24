//Author: Jianfu Zhang; Email: jianfuz@andrew.cmu.edu
package Personal_Workout_Trainer;

import javafx.scene.layout.StackPane;

public abstract class WorkoutViewer {

	//accepts only mp4 for video files, and treats all else as image files.
	public static WorkoutViewer createViewer(String filename) {
		if (filename.endsWith("mp4")) return (new VideoViewer(filename));
		else return (new PicViewer(filename));
	}

	public abstract void view(StackPane pane) ;
}
