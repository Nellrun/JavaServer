package tables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 12/4/16.
 * Класс для работы с таблицей UserInfo
 */
public class UserInfoDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    class UserInfoMapper implements RowMapper<UserInfo> {
        public UserInfo mapRow(ResultSet resultSet, int i) throws SQLException {
            UserInfo userInfo = new UserInfo();

//            userInfo.setUserID(resultSet.getInt("UserID"));
            userInfo.setId(resultSet.getInt("ID"));
            userInfo.setFirstName(resultSet.getString("FirstName"));
            userInfo.setSecondName(resultSet.getString("SecondName"));
            userInfo.setMiddleName(resultSet.getString("MiddleName"));
            userInfo.setGender(resultSet.getString("Gender"));

            return userInfo;
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void create(String firstName, String secondName, String middleName, String gender) {
        String sql = "Insert into `UserInfo`(FirstName, SecondName, MiddleName, Gender) VALUES(?, ?, ?, ?)";
        this.jdbcTemplate.update(sql, firstName, secondName, middleName, gender);
    }

    public void create(int userID, String firstName, String secondName, String middleName, String gender) {
        String sql = "Insert into `UserInfo`(UserID, FirstName, SecondName, MiddleName, Gender) VALUES(?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(sql, userID, firstName, secondName, middleName, gender);
    }

    public void create(int userID, String firstName, String secondName, String middleName) {
        String sql = "Insert into `UserInfo`(UserID, FirstName, SecondName, MiddleName) VALUES(?, ?, ?, ?)";
        this.jdbcTemplate.update(sql, userID, firstName, secondName, middleName);
    }

    public void update(int userID, String firstName, String secondName, String middleName) {
        String sql = "UPDATE `UserInfo` set FirstName = ?, SecondName = ?, MiddleName = ? where UserID = ?";
        this.jdbcTemplate.update(sql, firstName, secondName, middleName, userID);
    }

    public void update(int id, int userID) {
        String sql = "UPDATE `UserInfo` set UserID = ? Where ID = ?";
        this.jdbcTemplate.update(sql, userID, id);
    }

    public UserInfo getUserInfoByUserID(int id) {
        String sql = "Select * from `UserInfo` where UserID = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[] {id}, new UserInfoMapper());
    }

    public UserInfo getUserInfoByID(int id) {
        String sql = "Select * from `UserInfo` where ID = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[] {id}, new UserInfoMapper());
    }

    public List<UserInfo> getUserInfoByName(String name) {
        String sql = "Select * From `UserInfo` where FirstName like ? or SecondName like ? or MiddleName like ?";

        return this.jdbcTemplate.query(sql, new Object[]{name, name, name}, new UserInfoMapper());
    }


}
