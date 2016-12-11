package tables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 12/1/16.
 * Класс осуществляющий доступ к таблице Schedule
 */
public class ScheduleDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    class ScheduleRowMapper implements RowMapper<Schedule> {
        public Schedule mapRow(ResultSet resultSet, int i) throws SQLException {
            Schedule schedule = new Schedule();

            schedule.setSubjectID(resultSet.getInt("SubjectID"));
            schedule.setSubjectName(resultSet.getString("Name"));
            schedule.setType(resultSet.getString("Type"));
            schedule.setTeacherID(resultSet.getInt("TeacherID"));
            schedule.setFirstName(resultSet.getString("FirstName"));
            schedule.setSecondName(resultSet.getString("SecondName"));
            schedule.setMiddleName(resultSet.getString("MiddleName"));
            schedule.setGroupID(resultSet.getInt("GroupID"));
            schedule.setGroupNameShort(resultSet.getString("NameShort"));
            schedule.setnPair(resultSet.getInt("Npair"));
            schedule.setRoom(resultSet.getString("Room"));

            return schedule;

        }
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Schedule> getScheduleByGroup(int groupID) {
        String sql = "Select `Schedule`.*, " +
                "`UserInfo`.FirstName, `UserInfo`.SecondName, `UserInfo`.MiddleName, " +
                "`Group`.NameShort, " +
                "`Subject`.Name " +
                " from `Schedule`, `Teacher`, `Group`, `Subject`, `UserInfo` " +
                "Where (Schedule.GroupID = ?) and (Schedule.GroupID = `Group`.ID) and " +
                "(UserInfo.ID = Teacher.UserID) and " +
                "(Schedule.TeacherID = Teacher.ID) and (Subject.ID = Schedule.SubjectID)";

        List<Schedule> schedules = this.jdbcTemplate.query(sql, new Object[] {groupID}, new ScheduleRowMapper());

        return schedules;
    }

    public List<Schedule> getScheduleByTeacher(int teacherID) {
        String sql = "Select `Schedule`.*, " +
                "`UserInfo`.FirstName, `UserInfo`.SecondName, `UserInfo`.MiddleName, " +
                "`Group`.NameShort, " +
                "`Subject`.Name " +
                " from `Schedule`, `Teacher`, `Group`, `Subject`, `UserInfo` " +
                "Where (Schedule.TeacherID = ?) and (UserInfo.ID = Teacher.UserID) and (Schedule.TeacherID = Teacher.ID) " +
                "and (Subject.ID = Schedule.SubjectID) and (`Group`.ID = Schedule.GroupID)";

        List<Schedule> schedules = this.jdbcTemplate.query(sql, new Object[] {teacherID}, new ScheduleRowMapper());

        return schedules;
    }


}
