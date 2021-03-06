package tables;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by root on 12/1/16.
 * Класс для работы с таблицей User
 */
public class UserDAO {

    private DataSource  dataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void create(String login, String password) throws MySQLIntegrityConstraintViolationException {
        String sql = "Insert into User(Login, Password) values(?, ?)";

        this.jdbcTemplate.update(sql, login, password);
    }

    public void update(String login, String password) {
        String sql = "Update User set password = ? where login = ?";

        this.jdbcTemplate.update(sql, password, login);
    }

    public Integer getIDByLogin(String login) {
        String sql = "Select ID from `User` where login = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[] {login}, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return Integer.valueOf(resultSet.getInt("ID"));
            }
        });
    }

    public User getUserByLogin(String login){
        String sql = "Select * from User Where login = ?";

        User user = this.jdbcTemplate.queryForObject(sql, new Object[]{login}, new RowMapper<User>() {
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();

                user.setLogin(resultSet.getString("Login"));
                user.setPassword(resultSet.getString("Password"));
                return user;
            }
        });

        return user;
    }
}
