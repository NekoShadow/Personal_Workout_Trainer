//Author: Jianfu Zhang; Email: jianfuz@andrew.cmu.edu
package Personal_Workout_Trainer;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestPersonalTrainer {

	static PTData ptData;
	static Workout w;

	@BeforeClass
	public static void setupBeforeClass() {
		ptData = new PTData();
		ptData.loadData("resources/MasterWorkouts2.csv");
		w = new Workout();
	}

	@Test
	public void testDataSize() {
		assertEquals("Test master data size", 7, ptData.masterData.size());
	}

	@Test
	public void testMasterDataSequence() {
		assertEquals("Index-0", "Crunches", ptData.masterData.get(0).getName());
		assertEquals("Index-1", "Plank", ptData.masterData.get(1).getName());
		assertEquals("Index-2", "Spin", ptData.masterData.get(2).getName());
		assertEquals("Index-3", "Bicep-curls", ptData.masterData.get(3).getName());
		assertEquals("Index-4", "Squats", ptData.masterData.get(4).getName());
		assertEquals("Index-5", "Crunches", ptData.masterData.get(5).getName());
		assertEquals("Index-6", "Squats", ptData.masterData.get(6).getName());
	}

	@Test
	public void testBuildWorkoutPlanExactTimeCalories() {
		w.buildWorkoutPlan(ptData.masterData, 13, 190);
		assertEquals("Test less time", 13, w.timeSpent);
		assertEquals("Test all calories", 190, w.caloriesBurned);
	}
	@Test
	public void testBuildWorkoutPlanLessTimeAllCalories() {
		w.buildWorkoutPlan(ptData.masterData, 10, 100);
		assertEquals("Test less time", 11, w.timeSpent);
		assertEquals("Test all calories", 170, w.caloriesBurned);
	}

	@Test
	public void testBuildWorkoutPlanMoreTimeLessCalories() {
		w.buildWorkoutPlan(ptData.masterData, 20, 50);
		assertEquals("Test more time", 20, w.timeSpent);
		assertEquals("Test less calories", 270, w.caloriesBurned);
	}

	@Test
	public void testBuildWorkoutPlanLessTimeMoreCalories() {
		w.buildWorkoutPlan(ptData.masterData, 10, 150);
		assertEquals("Test less time", 11, w.timeSpent);
		assertEquals("Test more calories", 170, w.caloriesBurned);
	}

	@Test
	public void testBuildWorkoutPlanMoreTimeMoreCalories() {
		w.buildWorkoutPlan(ptData.masterData, 75, 400);
		assertEquals("Test more time", 76, w.timeSpent);
		assertEquals("Test more calories", 1120, w.caloriesBurned);
	}

	@Test
	public void testBuildWorkoutPlanIgnoreTime() {
		w.buildWorkoutPlan(ptData.masterData, 0, 200);
		assertEquals("Test zero time", 14, w.timeSpent);
		assertEquals("Test non-zero calories", 205, w.caloriesBurned);
	}

	@Test
	public void testBuildWorkoutPlanIgnoreCalories() {
		w.buildWorkoutPlan(ptData.masterData, 20, 0);
		assertEquals("Test non-zero time", 20, w.timeSpent);
		assertEquals("Test zero calories", 270, w.caloriesBurned);
	}

	@Test
	public void testBuildWorkoutPlanNoTimeNoCalories() {
		w.buildWorkoutPlan(ptData.masterData, 0, 0);
		assertEquals("Test less time", 0, w.timeSpent);
		assertEquals("Test less calories", 0, w.caloriesBurned);
	}

}
