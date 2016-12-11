package tables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 12/4/16.
 * Класс, в котором определены методы для взаимодействия с таблице Student
 */
public class StudentDAO {

    class StudentRowMapper implements RowMapper<Student> {
        public Student mapRow(ResultSet resultSet, int i) throws SQLException {
            Student student = new Student();
            student.setId(resultSet.getInt("ID"));
            student.setFirstName(resultSet.getString("FirstName"));
            student.setSecondName(resultSet.getString("SecondName"));
            student.setMiddleName(resultSet.getString("MiddleName"));
            student.setGroupID(resultSet.getInt("GroupID"));
            student.setGroupShortName(resultSet.getString("NameShort"));
            student.setLevelOfAccess(resultSet.getInt("LevelOfAccess"));
            return student;
        }
    }

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void create(int groupID, int levelOfAccess, int userID) {
        String sql = "Insert Into `Student`(GroupID, LevelOfAccess, UserID) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(sql, groupID, levelOfAccess, userID);
    }

    public void updateGroupID(int userID, int groupID) {
        String sql = "Update Student set GroupID = ? where UserID = ?";
        this.jdbcTemplate.update(sql, groupID, userID);
    }

    public void updateLevelOfAccess(int userID, int levelOfAccess) {
        String sql = "Update Student set LevelOfAccess = ? where UserID = ?";
        this.jdbcTemplate.update(sql, levelOfAccess, userID);
    }

    public Student getStudentByID(int id) {
        String sql = "Select Student.ID, UserInfo.FirstName, UserInfo.SecondName, UserInfo.MiddleName, " +
                "Group.NameShort, Student.GroupID, Student.LevelOfAccess " +
                "From `UserInfo`, `Group`, `Student`" +
                " Where (`Student`.ID = ?) and (`Student`.UserID = `UserInfo`.ID) and (`Student`.GroupID = `Group`.ID) ";

        return this.jdbcTemplate.queryForObject(sql, new Object[] {id}, new StudentRowMapper());
    }

    public List<Student> getStudentsByName(String name) {
        name = "%" + name + "%";
        String sql = "Select Student.ID, UserInfo.FirstName, UserInfo.SecondName, UserInfo.MiddleName, " +
                "Group.NameShort, Student.GroupID, Student.LevelOfAccess " +
                "From `UserInfo`, `Group`, `Student`" +
                " Where (UserInfo.FirstName like ? or UserInfo.SecondName like ? or UserInfo.MiddleName like ?)" +
                " and (UserInfo.ID = Student.UserID) and (Student.groupID = `Group`.ID)";

        return this.jdbcTemplate.query(sql, new Object[] {name, name, name}, new StudentRowMapper());
    }

    public List<Student> getStudentsByGroupID(int id) {
        String sql = "Select Student.ID, UserInfo.FirstName, UserInfo.SecondName, UserInfo.MiddleName, " +
                "Group.NameShort, Student.GroupID, Student.LevelOfAccess " +
                "From `UserInfo`, `Group`, `Student`" +
                " Where (UserInfo.ID = Student.UserID) and (`Group`.ID = ?) and (Student.GroupID = `Group`.ID)";

        return this.jdbcTemplate.query(sql, new Object[] {id}, new StudentRowMapper());
    }
}
