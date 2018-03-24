//Name: Jianfu Zhang; Andrew ID: jianfuz
package hw3;

import java.net.URL;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;

/** VideoViewer takes a StackPane, creates a MediaView object with a media,
 * attaches it to StackPane, and starts it to play. 
 * It gets the name of media file from its constructor, invoked by its parent class WorkoutViewer.
 * If media file is not found,  it catches the MediaException 
 * creates a PicViewer with a default PersonalTrainer.PT_IMAGE image file, 
 * and invokes its view() method.
 */
public class VideoViewer extends WorkoutViewer{

	private MediaView mediaView;
	private String filename;

	VideoViewer(String filename) {
		this.filename = filename;
	}

	@Override
	public void view(StackPane pane)  {
		try {
			URL video = ClassLoader.getSystemResource(filename);
			Media media = new Media (video.toString());
			if (PersonalTrainer.videoPlayer != null && PersonalTrainer.videoPlayer.getStatus().equals(Status.PLAYING)) 
				PersonalTrainer.videoPlayer.stop();
			PersonalTrainer.videoPlayer = new MediaPlayer(media);
			mediaView = new MediaView(PersonalTrainer.videoPlayer);
			pane.getChildren().clear();
			pane.getChildren().add(mediaView);

			mediaView.setFitWidth(pane.getPrefWidth());
			mediaView.setFitHeight(pane.getPrefHeight());
			mediaView.setPreserveRatio(true);
			mediaView.setSmooth(true);

			//For short repetitive media files e.g. crunches, play indefinitely.
			//Its actual time will be determined in the WorkoutPlayer according to reptime set by user
			PersonalTrainer.videoPlayer.setCycleCount(MediaPlayer.INDEFINITE); 	

			//If audio is playing, mute it
			if (PersonalTrainer.audioPlayer != null && !PersonalTrainer.audioPlayer.isMute()) PersonalTrainer.audioPlayer.setMute(true);
			PersonalTrainer.videoPlayer.play();
		} catch (MediaException | NullPointerException e) {
			WorkoutViewer.createViewer(PersonalTrainer.PT_IMAGE).view(pane);  //ask WorkoutViewer to create a viewer for standard image
		}

	}
}
