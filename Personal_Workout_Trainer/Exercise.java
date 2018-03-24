package hw3;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


//Do not change this file
public class Exercise {
	StringProperty name;
	StringProperty level;
	IntegerProperty repTime;
	IntegerProperty repCount;
	IntegerProperty calories;
	StringProperty imageFile;
	StringProperty exerciseNotes;

	Exercise(String name, String level, int repTime, int repCount, int calories, String imageFile, String exerciseNotes) {
		this.name = new SimpleStringProperty(name);
		this.level = new SimpleStringProperty(level);
		this.repTime = new SimpleIntegerProperty(repTime);
		this.repCount = new SimpleIntegerProperty(repCount);
		this.calories = new SimpleIntegerProperty(calories);
		this.imageFile = new SimpleStringProperty(imageFile);
		this.exerciseNotes = new SimpleStringProperty(exerciseNotes);
	}

	@Override
	public String toString() {
		return getName() + "-" + getLevel();
	}

	public String getName() { return name.get();}
	public void setName(String exName) { name.set(exName); }
	public StringProperty nameProperty() { return name;}

	public String getLevel() { return level.get();}
	public void setLevel(String exLevel) { name.set(exLevel); }
	public StringProperty levelProperty() { return level;}

	public Integer getRepTime() { return repTime.get();}
	public void setRepTime(Integer exTime) { repTime.set(exTime); }
	public IntegerProperty repTimeProperty() { return repTime;}

	public Integer getRepCount() { return repCount.get();}
	public void setRepCount(Integer exCount) { repCount.set(exCount); }
	public IntegerProperty repCountProperty() { return repCount;}

	public Integer getCalories() { return calories.get();}
	public void setCalories(Integer exCalories) { calories.set(exCalories); }
	public IntegerProperty caloriesProperty() { return calories;}

	public String getImageFile() { return imageFile.get();}
	public void setImageFile(String exImage) { imageFile.set(exImage); }
	public StringProperty imageFileProperty() { return imageFile;}

	public String getExerciseNotes() { return exerciseNotes.get();}
	public void setExerciseNotes(String exNotes) { exerciseNotes.set(exNotes); }
	public StringProperty exerciseNotesProperty() { return exerciseNotes;}
}
