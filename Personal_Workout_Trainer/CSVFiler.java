//Author: Jianfu Zhang; Email: jianfuz@andrew.cmu.edu
package Personal_Workout_Trainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CSVFiler extends DataFiler{

	@Override
	public ObservableList<Exercise> readData(String filename) {
		StringBuilder fileContent = new StringBuilder(); 
		try {
			Scanner input = new Scanner (new File(filename));
			while (input.hasNextLine()) {
				fileContent.append(input.nextLine() + "\n");
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String[] list = fileContent.toString().trim().split("\n");
		ObservableList<Exercise> exercise = FXCollections.observableArrayList();
		for(int i=0;i<list.length;i++){
			String[] array = list[i].split(",");
			exercise.add(new Exercise(array[0],array[1],Integer.parseInt(array[2]),
					Integer.parseInt(array[3]),Integer.parseInt(array[4]),array[5],array[6]));
		}		
		return exercise;
	}



	public void writeData(ObservableList<Exercise> selectedExercises, File file) {
		try{
			FileWriter fileWriter = new FileWriter(file);
			for(Exercise exercise : selectedExercises){
				fileWriter.append(exercise.getName()+",");
				fileWriter.append(exercise.getLevel()+",");
				fileWriter.append(exercise.getRepTime()+",");
				fileWriter.append(exercise.getRepCount()+",");
				fileWriter.append(exercise.getCalories()+",");
				fileWriter.append(exercise.getImageFile()+",");
				fileWriter.append(exercise.getExerciseNotes()+"\n");		
			}
			fileWriter.close();
		}catch (IOException e1) {
			e1.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
