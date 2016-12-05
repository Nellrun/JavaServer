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
 */
public class GroupDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    class GroupMapper implements RowMapper<Group> {
        public Group mapRow(ResultSet resultSet, int i) throws SQLException {
            Group group = new Group();
            group.setId(resultSet.getInt("ID"));
            group.setNameLong(resultSet.getString("NameLong"));
            group.setNameShort(resultSet.getString("NameShort"));
            group.setDegree(resultSet.getString("Degree"));
            group.setFormOfEducation(resultSet.getString("FormOfEducation"));
            group.setStreamID(resultSet.getInt("StreamID"));
            return group;
        }
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void create(String nameShort, String nameLong, String degree, String formOfEducation, int streamID){
        String sql = "insert into `Group` (NameShort, NameLong, Degree, FormOfEducation, StreamID) values (?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(sql, nameShort, nameLong, degree, formOfEducation, streamID);
    }

    public Group getGroupByID(int id) {
        String sql = "Select * from `Group` Where ID = ?";
        Group group = this.jdbcTemplate.queryForObject(sql, new Object[] {id}, new GroupMapper());

        return group;
    }

    public List<Group> getGroupsByName(String name) {
        String sql = "Select * from `Group` Where NameShort Like ?";
        List<Group> gl = this.jdbcTemplate.query(sql, new Object[]{"%" + name + "%"}, new GroupMapper());

        return gl;
    }

    public void delete(int id) {
        String sql = "Delete from Student where ID = ?";

        this.jdbcTemplate.update(sql, id);
    }

}
