//Author: Jianfu Zhang; Email: jianfuz@andrew.cmu.edu
package Personal_Workout_Trainer;

import java.io.File;

import javafx.collections.ObservableList;

public abstract class DataFiler {

	/** reads the data from the filename provided and loads into a list. 
	 * It returns the list to the calling method
	 * @param filename
	 * @return
	 */
	public abstract ObservableList<Exercise> readData(String filename);


	/** write the data from the selectedExercises list onto file.
	 * This method need not be implemented for PersonalTrainer version 2.0.
	 * @param filename
	 * @return
	 */
	public abstract void writeData(ObservableList<Exercise> selectedExercises, File file);

}
