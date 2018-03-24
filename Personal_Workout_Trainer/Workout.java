//Author: Jianfu Zhang; Email: jianfuz@andrew.cmu.edu
package Personal_Workout_Trainer;

import java.util.Iterator;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Workout {
	int timeSpent, caloriesBurned;
	public ObservableList<Exercise>  buildWorkoutPlan(List<Exercise> exercisesAll, int timeInput, int caloriesInput) {
		ObservableList<Exercise> suggestedExercises = FXCollections.observableArrayList();	
		timeSpent = 0;
		caloriesBurned = 0;
		for(int i=0;i<exercisesAll.size();i++){//This loop aims to initialize suggestedExercises.
			suggestedExercises.add(new Exercise(exercisesAll.get(i).getName(),
					exercisesAll.get(i).getLevel(),0,0,0,
					exercisesAll.get(i).getImageFile(),
					exercisesAll.get(i).getExerciseNotes()));
		}
		int i=0;//The variable i serves as a signal of whether the loop has reached the last row of allExercises.
		if(timeInput>0 && caloriesInput>0){//This conditional statement deals with the (1) scenario.
			while(timeSpent<timeInput || caloriesBurned<caloriesInput){//This loop aims to take adequate passes of the sequence
				//in order to meet the time or calories limit.
				if(i<exercisesAll.size()){
					suggestedExercises.get(i).repTime = new SimpleIntegerProperty(
							suggestedExercises.get(i).repTime.intValue()+exercisesAll.get(i).repTime.intValue());
					suggestedExercises.get(i).repCount = new SimpleIntegerProperty(
							suggestedExercises.get(i).repCount.intValue()+exercisesAll.get(i).repCount.intValue());
					suggestedExercises.get(i).calories = new SimpleIntegerProperty(
							suggestedExercises.get(i).calories.intValue()+exercisesAll.get(i).calories.intValue());
					timeSpent+=exercisesAll.get(i).repTime.intValue();
					caloriesBurned+=exercisesAll.get(i).calories.intValue();
					i++;
				}
				else{
					i=0;
				}

			}
		}
		else if(timeInput==0 && caloriesInput>0){//This conditional statement deals with the (2) scenario.
			while(caloriesBurned<caloriesInput){
				if(i<exercisesAll.size()){
					suggestedExercises.get(i).repTime = new SimpleIntegerProperty(
							suggestedExercises.get(i).repTime.intValue()+exercisesAll.get(i).repTime.intValue());
					suggestedExercises.get(i).repCount = new SimpleIntegerProperty(
							suggestedExercises.get(i).repCount.intValue()+exercisesAll.get(i).repCount.intValue());
					suggestedExercises.get(i).calories = new SimpleIntegerProperty(
							suggestedExercises.get(i).calories.intValue()+exercisesAll.get(i).calories.intValue());
					timeSpent+=exercisesAll.get(i).repTime.intValue();
					caloriesBurned+=exercisesAll.get(i).calories.intValue();
					i++;
				}
				else{
					i=0;
				}

			}
		}
		else if(timeInput>0 && caloriesInput==0){//This conditional statement deals with the (3) scenario.
			while(timeSpent<timeInput){
				if(i<exercisesAll.size()){					
					suggestedExercises.get(i).repTime = new SimpleIntegerProperty(
							suggestedExercises.get(i).repTime.intValue()+exercisesAll.get(i).repTime.intValue());
					suggestedExercises.get(i).repCount = new SimpleIntegerProperty(
							suggestedExercises.get(i).repCount.intValue()+exercisesAll.get(i).repCount.intValue());
					suggestedExercises.get(i).calories = new SimpleIntegerProperty(
							suggestedExercises.get(i).calories.intValue()+exercisesAll.get(i).calories.intValue());
					timeSpent+=exercisesAll.get(i).repTime.intValue();
					caloriesBurned+=exercisesAll.get(i).calories.intValue();
					i++;
				}
				else{
					i=0;
				}

			}
		}
		else{//This conditional statement deals with the (4) scenario.
			return (ObservableList<Exercise>)exercisesAll;
		}
		Iterator<Exercise> iter = suggestedExercises.iterator();
		while (iter.hasNext()) {
			Exercise e = iter.next();
			if (e.getRepCount()==0)
				iter.remove();
		}
		return suggestedExercises;
	}
}
