package tables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 12/4/16.
 * Класс для работы с таблицей Subject
 */
public class SubjectDAO {

    class SubjectRowMapper implements RowMapper<Subject> {

        public Subject mapRow(ResultSet resultSet, int i) throws SQLException {
            Subject subject = new Subject();
            subject.setId(resultSet.getInt("ID"));
            subject.setName(resultSet.getString("Name"));
            return subject;
        }
    }

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Subject getSubjectByID(int id) {
        String sql = "Select * from `Subject` Where ID = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{id}, new SubjectRowMapper());
    }

    public List<Subject> getSubjectsByTeacherID(int id) {
        String sql = "Select Distinct Subject.* from (Subject, Schedule, Teacher) " +
                "where Teacher.ID = ? and Teacher.ID = Schedule.TeacherID and " +
                "Schedule.SubjectID = Subject.ID order by ID";
        return this.jdbcTemplate.query(sql, new Object[] {id}, new SubjectRowMapper());
    }


}
