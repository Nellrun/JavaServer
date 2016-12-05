package tables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 12/4/16.
 */
public class StudentDAO {

    class StudentRowMapper implements RowMapper<Student> {
        public Student mapRow(ResultSet resultSet, int i) throws SQLException {
            Student student = new Student();
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

    public List<Student> getStudentByName(String name) {
        name = "%" + name + "%";
        String sql = "Select UserInfo.FirstName, UserInfo.SecondName, UserInfo.MiddleName, " +
                "Group.NameShort, Student.GroupID, Student.LevelOfAccess " +
                "From `UserInfo`, `Group`, `Student`" +
                " Where (UserInfo.FirstName like ? or UserInfo.SecondName like ? or UserInfo.MiddleName like ?)" +
                " and (UserInfo.ID = Student.UserID)";

        return this.jdbcTemplate.query(sql, new Object[] {name, name, name}, new StudentRowMapper());
    }
}
