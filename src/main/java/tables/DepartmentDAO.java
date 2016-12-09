package tables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by root on 12/9/16.
 */
public class DepartmentDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String getSecretKeyByDepartmentID(int id) {
        String sql = "Select SecretKey from Department Where ID = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<String>() {
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("SecretKey");
            }
        });
    }
}
