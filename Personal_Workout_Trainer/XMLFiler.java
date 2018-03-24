//Author: Jianfu Zhang; Email: jianfuz@andrew.cmu.edu
package Personal_Workout_Trainer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class XMLFiler extends DataFiler {

	public ObservableList<Exercise> readData(String filename) {
		List<Exercise> workouts = new ArrayList<>();

		File file = new File(filename);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(file);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			return null;
		}
		Element root = doc.getRootElement();
		List<Element> workoutElements = root.getChildren("exercise");
		if (workoutElements.size() > 0)
			for (Element e: workoutElements) {
				Exercise ex = new Exercise(e.getChildText("exName"), e.getChildText("level"), Integer.parseInt(e.getChildText("repTime")),
						Integer.parseInt(e.getChildText("repCount")), Integer.parseInt(e.getChildText("calories")), e.getChildText("image"), e.getChildText("notes"));
				workouts.add(ex);
			}
		return FXCollections.observableArrayList(workouts);
	}

	public void writeData(ObservableList<Exercise> selectedExercises, File file) {
		Document doc = new Document();
		Element xmlRoot = new Element("exercises");
		doc.addContent(xmlRoot);
		for (Exercise ex: selectedExercises) {
			Element exerciseElement = new Element("exercise");
			xmlRoot.addContent(exerciseElement);
			addChildElement(exerciseElement, "exName", ex.getName());
			addChildElement(exerciseElement, "level", ex.getLevel());
			addChildElement(exerciseElement, "repTime", Integer.toString(ex.getRepTime()));
			addChildElement(exerciseElement, "repCount", Integer.toString(ex.getRepCount()));
			addChildElement(exerciseElement, "calories", Integer.toString(ex.getCalories()));
			addChildElement(exerciseElement, "image", ex.getImageFile());
			addChildElement(exerciseElement, "notes", ex.getExerciseNotes());

		}
		XMLOutputter outputter = new XMLOutputter (Format.getPrettyFormat());

		try {
			FileWriter writer = new FileWriter(file);
			outputter.output(doc, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addChildElement(Element parent, String elementName, String textValue) {
		Element child = new Element(elementName);
		child.setText(textValue);
		parent.addContent(child);
	}
}
