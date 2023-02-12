package comp3350.srsys.application;

import comp3350.srsys.objects.Workout;
import comp3350.srsys.persistence.CoursePersistence;
import comp3350.srsys.persistence.RoutinePersistence;
import comp3350.srsys.persistence.SCPersistence;
import comp3350.srsys.persistence.StudentPersistence;
import comp3350.srsys.persistence.WorkoutPersistence;
import comp3350.srsys.persistence.stubs.CoursePersistenceStub;
import comp3350.srsys.persistence.stubs.RoutinePersistenceStub;
import comp3350.srsys.persistence.stubs.SCPersistenceStub;
import comp3350.srsys.persistence.stubs.StudentPersistenceStub;
import comp3350.srsys.persistence.stubs.WorkoutPersistenceStub;

public class Services
{
	private static StudentPersistence studentPersistence = null;
	private static CoursePersistence coursePersistence = null;
	private static SCPersistence scPersistence = null;
    private static WorkoutPersistence workoutPersistence = null;
    private static RoutinePersistence routinePersistence = null;

	public static synchronized StudentPersistence getStudentPersistence()
    {
		if (studentPersistence == null)
		{
		    studentPersistence = new StudentPersistenceStub();
        }

        return studentPersistence;
	}

    public static synchronized CoursePersistence getCoursePersistence()
    {
        if (coursePersistence == null)
        {
            coursePersistence = new CoursePersistenceStub();
        }

        return coursePersistence;
    }

	public static synchronized SCPersistence getScPersistence() {
        if (scPersistence == null) {
            scPersistence = new SCPersistenceStub();
        }

        return scPersistence;
    }

    public static synchronized WorkoutPersistence getWorkoutPersistence() {
        if(workoutPersistence == null) {
            workoutPersistence = new WorkoutPersistenceStub();
        }
        return workoutPersistence;
    }

    public static RoutinePersistence getRoutinePersistence() {
        if(routinePersistence == null) {
            routinePersistence = new RoutinePersistenceStub();
        }
        return routinePersistence;
    }
}
