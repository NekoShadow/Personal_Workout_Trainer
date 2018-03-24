//Name: Jianfu Zhang; Andrew ID: jianfuz
package hw3;

import java.net.URL;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;

public class PicViewer extends WorkoutViewer{

	private ImageView imageView;
	private String filename;

	PicViewer(String filename) {
		this.filename = filename;
	}

	@Override
	public void view(StackPane pane) {
		if (imageView == null) imageView = new ImageView(); //create only one instance
		DropShadow dropShadow = new DropShadow(20, Color.GRAY);

		imageView.setFitHeight(pane.getPrefHeight()-10);
		imageView.setFitWidth(pane.getPrefWidth()-10);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);
		imageView.setEffect(dropShadow);
		Image image = null;

		try {
			image = new Image(getClass().getClassLoader().getResource(filename).toString());
		} catch (NullPointerException e) {
			filename = PersonalTrainer.PT_IMAGE;
			image = new Image(getClass().getClassLoader().getResource(filename).toString());
		}

		imageView.setImage(image);
		pane.getChildren().clear();
		pane.getChildren().add(imageView);
		if (PersonalTrainer.videoPlayer != null && PersonalTrainer.videoPlayer.getStatus().equals(Status.PLAYING)) 
			PersonalTrainer.videoPlayer.stop();
		if (PersonalTrainer.audioPlayer != null )  {
			if (PersonalTrainer.audioPlayer.getStatus().equals(Status.STOPPED)) PersonalTrainer.audioPlayer.play();
			PersonalTrainer.audioPlayer.setMute(false);
		}
		else {
			String audiofile = PersonalTrainer.PT_MUSIC;
			URL audio = ClassLoader.getSystemResource(audiofile);
			Media media = new Media(audio.toString());
			PersonalTrainer.audioPlayer = new MediaPlayer(media);
			PersonalTrainer.audioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			PersonalTrainer.audioPlayer.play();

		}
	}
}
