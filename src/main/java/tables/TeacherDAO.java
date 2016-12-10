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
public class TeacherDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    class TeacherRowMapper implements RowMapper<Teacher> {
        public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
            Teacher teacher = new Teacher();
            teacher.setId(resultSet.getInt("ID"));
            teacher.setFirstName(resultSet.getString("FirstName"));
            teacher.setSecondName(resultSet.getString("SecondName"));
            teacher.setMiddleName(resultSet.getString("MiddleName"));
            teacher.setDepartamentID(resultSet.getInt("DepartmentID"));
            teacher.setUserID(resultSet.getInt("UserID"));
            return teacher;
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Teacher getTeacherByID(int id) {
        String sql = "Select UserInfo.ID, Teacher.DepartmentID, " +
                "UserInfo.FirstName, UserInfo.SecondName, UserInfo.MiddleName, Teacher.UserID from Teacher, UserInfo " +
                "Where Teacher.UserID = UserInfo.ID and UserInfo.ID = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{id}, new TeacherRowMapper());
    }

    public List<Teacher> getTeachersByName(String name) {
        name = "%" + name + "%";
        String sql = "Select UserInfo.ID, Teacher.DepartmentID, " +
                "UserInfo.FirstName, UserInfo.SecondName, UserInfo.MiddleName, UserInfo.UserID from Teacher, UserInfo" +
                " Where (UserInfo.FirstName LIKE ? or UserInfo.SecondName LIKE ? or UserInfo.MiddleName LIKE ?)" +
                " and (Teacher.UserID = UserInfo.ID)";

        return this.jdbcTemplate.query(sql, new Object[] {name, name, name}, new TeacherRowMapper());
    }
}
