package tables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by root on 12/4/16.
 * Класс для работы с таблицей Subject
 */
public class SubjectDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Subject getSubjectByID(int id) {
        String sql = "Select * from `Subject` Where ID = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<Subject>() {
            public Subject mapRow(ResultSet resultSet, int i) throws SQLException {
                Subject subject = new Subject();
                subject.setName(resultSet.getString("Name"));
                return subject;
            }
        });
    }
}
