Problem statement
-----------------------

As health consciousness among individuals is increasing, they are looking for more convenient options to help them maintain a regular workout routine that doesn’t require joining an expensive fitness center or health club. Many of them have already learned the basic fitness exercises and just need some kind of ecosystem that helps them manage and track their workout activities. There are many options available such as workout videos and TV-shows, but their drawback is that they are not customizable and often lose their effectiveness as one’s body begins to plateau doing the same workouts again and again. People also like to keep track of which workout they did, when, and for how long to see what is working well for them, or not.

Installation
----------------------

### Download the data

* Clone this repo to your computer, and open any Java IDE (i.e. Eclipse).
* Create a new package named 'Personal_Workout_Trainer'.
* Right click on 'Personal_Workout_Trainer' package'.
* 'Import' - browse from directory - select 'Personal_Workout_Trainer' folder - 'OK' - check all *.java files - 'Finish'.
* Create a new folder named 'media' under the project folder, then import all the files under the 'media' folder.
* Create a new folder named 'resources' under the project folder, then import all the files under the 'resources' folder.

Note: there are 3 files in this project that are more than 100MB, which are exceeding the limit of Github. So I have shared
them via Google Drive. You can download them through the links as follows:
[spin30minutes.mp4](https://drive.google.com/open?id=1e-MFCYscO_XJ8HE6LCeXkf9wkncIGgw8)
[spinHour.mp4](https://drive.google.com/open?id=18eHGRkN5-r-MYUeVvNhRutl3LzU5fveY)
[step30.mp4](https://drive.google.com/open?id=1HEUuryHHcQaQpAFjmP1FRYmXqn-Yu0bJ)

Usage
-----------------------

* Run 'PeronalTrainer.java' to open the welcome screen. Menu has new menu items. Only Open and Exit options are enabled.
* Choose a file to be opened - MasterWorkouts3.csv in resources ‘Source folder’.
* File opens with first video playing in imageView.
* Search string 'advanced' entered in SearchTextField.
* SearchButton clicked. Table-view now displays only those exercises that have 'advanced' anywhere in their data. First exercise selected, imageView and notesTextField updated, and if it is an image, PT_AUDIO starts playing. Also, Workout time and Workout calories accordingly changed.
* Another search. This time for 'abs'. Found in the 'notes' for three exercises. To go back to original data is to delete the text from searchTextField, and hit searchButton again. This should bring back the original data in table-view.
* Entering a search string that is not found displays a message in searchTextField, and the table-view is restored with original data.
* Making any change in Notes section changes font-color of updateButton to RED. This is required so that user knows that Update button needs to be clicked to save the change.
* Clicking the updateButton changes its font color back to Black. In the background, the UpdateButtonHandler updates exerciseNotes of the selected exercise in table-view.
* However, note that unless the file is saved, the changes are not really saved. If you close the file at this point and reopen it, the notes for Step-exercise are back to what you had before you made the change. In other words, upDateButton only updates the data in selectedExercises data structure, and not in the file.
* Now look at the Tools menu, with one option as Suggest. Note that Suggest menu item should be enabled only when a file is open.
* Selecting Suggest menu item opens up another dialog box, that requires user to enter at least one input - either time or calories or both. Clicking on Suggest button with no input or pressing Cancel or X button should take you back to the previous workout screen. Note that the GUI for Suggest feature has to be designed by you.
* Enter '200' in time and click on Suggest button.
* Notice that exercises in table-view are accordingly changed, along with total workout time and calories.
* Next, save the file. Note that this menu-item should be enabled only when a file is open. Also notice, that I made the change in Notes for Step exercise, as before, and clicked the Update button. Now it should get saved in the file.
* The file-chooser opens up resources folder again where you can save the file. Default format is set to .csv. Use ExtensionFilter to set filters as shown. Setting up ExtensionFilters is optional. Save the file as ‘NewWorkout.csv’.
* Notice that the Workout Name changed at the top as NewWorkout.csv. Also, Notes for Step exercise has updated text.
* Finally, Menu-Close option closes the file and takes you back to Welcome screen.
* Open a file again, this time incorrect CSV format (i.e. MasterWorkouts1.csv)
* Exception handling by showing an Alert message indicating the error. Only CSV-format exceptions need to be handled. We will not test for XML format exceptions at this time. Once the user clicks on OK, the screen should come back to Welcome screen.
* Open a file again, this time Exceptional.csv provided to you, which has correct CSV format but some of the media files listed in it are not available in the media folder.
* The files for Warmup - 'Warmup.mp4', or for Stretch and cool down – ‘Stretch.jpg’ are missing from media folder. But instead of throwing an Exception, the default image is shown. The PicViewer handles NullPointerException and VideoViewer handles MediaException by substituting PersonalTrainer.PT_IMAGE as shown above.
* Open the Masterwordouts3.csv again, this time to play the workout routine. Note that Play menu item should be enabled only when a file is open.
* Workout Player started with countdown for first exercise in exerciseTableView.
* As the first exercise in workout routine starts, timer at the top shows remaining time of current exercise, and lower timer shows total elapsed time so far. The Calories counter shows estimated total calories burned so far.
* Top and bottom time counters shows time remaining in current exercise, and total time spent so far, respectively. Calories accordingly updated.
* Running the entire sequence of exercises in Masterwordkouts3.csv will take you to the final screen. The total time is same as the workout time. The Calories may be a bit off due to int-rounding. You can test it out with a shorter sequence of exercises just to see the whole thing play from start to end.
