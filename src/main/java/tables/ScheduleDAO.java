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

            schedule.setId(resultSet.getInt("ID"));
            schedule.setSubjectID(resultSet.getInt("SubjectID"));
            schedule.setSubjectName(resultSet.getString("Name"));
            schedule.setType(resultSet.getString("Type"));
            schedule.setTeacherID(resultSet.getInt("TeacherID"));
            schedule.setFirstName(resultSet.getString("FirstName"));
            schedule.setSecondName(resultSet.getString("SecondName"));
            schedule.setMiddleName(resultSet.getString("MiddleName"));
            schedule.setnPair(resultSet.getInt("Npair"));
            schedule.setRoom(resultSet.getString("Room"));
            schedule.setDate(resultSet.getDate("Date"));

            return schedule;

        }
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void create(int subjectID, String type, int groupID, int teacherID, int nPair, String room, String date) {
        String sql = "Insert into " +
                "Schedule(`SubjectID`, `Type`, `TeacherID`, `Npair`, `Room`, `Date`) " +
                "Values(?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(sql, subjectID, type, teacherID, nPair, room, date);

        sql = "SELECT ID from Schedule ORDER BY ID DESC LIMIT 1";

        int lastID = this.jdbcTemplate.queryForObject(sql, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("ID");
            }
        });

        this.addGroup(lastID, groupID);
    }

    public void addGroup(int scheduleID, int groupID) {
        String sql = "Insert into ScheduleToGroup(ScheduleID, GroupID) values (?, ?)";

        this.jdbcTemplate.update(sql, scheduleID, groupID);
    }

    public void delGroup(int scheduleID, int groupID) {
        String sql = "Delete from ScheduleToGroup where ScheduleID = ? and GroupId = ?";
        this.jdbcTemplate.update(sql, scheduleID, groupID);
    }

    public void deleteByID(int id) {
        String sql = "Delete From Schedule Where Schedule.ID = ?";
        this.jdbcTemplate.update(sql, id);
    }

    private List<Group> getGroupsByScheduleID(int scheduleID) {
        String sql = "Select `Group`.* from `Group`, ScheduleToGroup " +
                "where ScheduleToGroup.scheduleID = ? and `Group`.ID = ScheduleToGroup.GroupID";

        return this.jdbcTemplate.query(sql, new Object[]{scheduleID}, new RowMapper<Group>() {
            public Group mapRow(ResultSet resultSet, int i) throws SQLException {
                Group group = new Group();
                group.setId(resultSet.getInt("ID"));
                group.setNameLong(resultSet.getString("NameLong"));
                group.setNameShort(resultSet.getString("NameShort"));
                group.setDegree(resultSet.getString("Degree"));
                group.setFormOfEducation(resultSet.getString("FormOfEducation"));
                return group;
            }
        });
    }

    public List<Schedule> getScheduleByGroup(int groupID) {
        String sql = "Select `Schedule`.*, " +
                "`UserInfo`.FirstName, `UserInfo`.SecondName, `UserInfo`.MiddleName, " +
                "`Subject`.Name " +
                " from `Schedule`, `Teacher`, `ScheduleToGroup`, `Subject`, `UserInfo` " +
                "Where (ScheduleToGroup.GroupID = ?) and " +
                "(UserInfo.ID = Teacher.UserID) and (ScheduleToGroup.ScheduleID = Schedule.ID) and " +
                "(Schedule.TeacherID = Teacher.ID) and (Subject.ID = Schedule.SubjectID)";

        List<Schedule> schedules = this.jdbcTemplate.query(sql, new Object[] {groupID}, new ScheduleRowMapper());

        return schedules;
    }

    public List<Schedule> getScheduleByTeacher(int teacherID) {
        String sql = "Select `Schedule`.*, " +
                "`UserInfo`.FirstName, `UserInfo`.SecondName, `UserInfo`.MiddleName, " +
                "`Subject`.Name " +
                " from `Schedule`, `Teacher`, `Subject`, `UserInfo` " +
                "Where (Schedule.TeacherID = ?) and (UserInfo.ID = Teacher.UserID) and (Schedule.TeacherID = Teacher.ID) " +
                "and (Subject.ID = Schedule.SubjectID)";

        List<Schedule> schedules = this.jdbcTemplate.query(sql, new Object[] {teacherID}, new ScheduleRowMapper());

        for (Schedule s: schedules) {
            s.setGroups(this.getGroupsByScheduleID(s.getId()));
        }

        return schedules;
    }

    public Schedule getScheduleByID(int id) {
        String sql = "Select `Schedule`.*, " +
                "`UserInfo`.FirstName, `UserInfo`.SecondName, `UserInfo`.MiddleName, " +
                "`Subject`.Name " +
                " from `Schedule`, `Teacher`, `Subject`, `UserInfo` " +
                "Where (Schedule.ID = ?) and (UserInfo.ID = Teacher.UserID) and (Schedule.TeacherID = Teacher.ID) " +
                "and (Subject.ID = Schedule.SubjectID)";

        return this.jdbcTemplate.queryForObject(sql, new Object[] {id}, new ScheduleRowMapper());
    }

    public Schedule getScheduleByTeacherIDAndN(int teacherID, int n) {
        String sql = "Select `Schedule`.*, " +
                "`UserInfo`.FirstName, `UserInfo`.SecondName, `UserInfo`.MiddleName, " +
                "`Subject`.Name " +
                " from `Schedule`, `Teacher`, `Subject`, `UserInfo` " +
                "Where (Schedule.TeacherID = ?) and (UserInfo.ID = Teacher.UserID) and (Schedule.TeacherID = Teacher.ID) " +
                "and (Subject.ID = Schedule.SubjectID) and (Schedule.Npair = ?)";

        return this.jdbcTemplate.queryForObject(sql, new Object[] {teacherID, n}, new ScheduleRowMapper());
    }




}
