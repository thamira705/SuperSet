package comp3350.superset.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import comp3350.superset.objects.Exercise;
import comp3350.superset.objects.ExerciseList;
import comp3350.superset.objects.Routine;
import comp3350.superset.persistence.RoutinePersistence;

public class RoutinePersistenceHSQLDB implements RoutinePersistence {

    private final String dbPath;

    public RoutinePersistenceHSQLDB(final String dbPath) {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    private Routine fromResultSet(final ResultSet rs) throws SQLException {
        final int curID = rs.getInt("ROUTINEID");
        final String name = rs.getString("NAME_R");
        final ExerciseList exerciseList = getExerciseListFromResultSet(rs, curID);

        return new Routine(name, exerciseList);
    }

    public ExerciseList getExerciseListFromResultSet(ResultSet rs, int curRoutineID) throws SQLException {

        ExerciseList exerciseList = new ExerciseList();
        boolean isNext = true;

        while(isNext && rs.getInt("ROUTINEID") == curRoutineID) {
            String name = rs.getString("NAME_E");
            int dur = rs.getInt("DURATION_SEC");
            int numReps = rs.getInt("NUMREPS");
            exerciseList.add(new Exercise(name, dur, numReps));
            isNext = rs.next();
        }
        return exerciseList;
    }

    @Override
    public List<Routine> getRoutineSequential() {
        final List<Routine> routines = new ArrayList<>();

        try(final Connection c = connection()) {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM routine JOIN exercise on routine.routineid = exercise.routineid");
            while(rs.next())
            {
                final Routine routine = fromResultSet(rs);
                routines.add(routine);
            }
            rs.close();
            st.close();

            return routines;
        }
        catch(final SQLException e)
        {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean insertRoutine(Routine currentRoutine) {
        if(currentRoutine == null) {
            return false;
        }
        try (final Connection c = connection()) {
            final PreparedStatement rt = c.prepareStatement("INSERT INTO routine VALUES(?, ?)");
            rt.setString(1, currentRoutine.getName());
            rt.setString(2, exerciseListToString(currentRoutine.getExercises()));
            rt.executeUpdate();
            return true;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public String exerciseListToString(ExerciseList exerciseList) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < exerciseList.size(); i++) {
            Exercise exercise = exerciseList.get(i);
            sb.append("Name: ").append(exercise.getName()).append("\n");
            sb.append("DurationSecond: ").append(exercise.getDurationSec()).append("\n");
            sb.append("Number of reps: ").append(exercise.getNumReps()).append("\n\n");
        }

        return sb.toString();
    }

    @Override
    public boolean removeRoutine(Routine currentRoutine) {
        if(currentRoutine == null) {
            return false;
        }
        try (final Connection c = connection()) {
            final PreparedStatement n = c.prepareStatement("DELETE FROM routine WHERE name = ?");
            n.setString(1, currentRoutine.getName());
            n.executeUpdate();
            final PreparedStatement el = c.prepareStatement("DELETE FROM routine WHERE exercise list = ?");
            el.setString(1, exerciseListToString(currentRoutine.getExercises()));
            el.executeUpdate();
            return true;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }


}