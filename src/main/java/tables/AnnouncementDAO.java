package tables;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by root on 12/17/16.
 */
public class AnnouncementDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private String[] importanceList = {"Low", "Medium", "High"};

    class AnnouncementRowMapper implements RowMapper<Announcement> {
        public Announcement mapRow(ResultSet resultSet, int i) throws SQLException {
            Announcement announcement = new Announcement();

            announcement.setId(resultSet.getInt("ID"));
            announcement.setScheduleID(resultSet.getInt("ScheduleID"));
            announcement.setFirstName(resultSet.getString("FirstName"));
            announcement.setSecondName(resultSet.getString("SecondName"));
            announcement.setMiddleName(resultSet.getString("MiddleName"));
            announcement.setText(resultSet.getString("Text"));
            announcement.setPairDate(resultSet.getDate("PairDate"));
            announcement.setAnnounceTime(resultSet.getDate("AnnounceTime"));

            return announcement;

        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void create(int scheduleID, String pairDate, int importance, int creatorID, String text) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());

        String sql = "Insert Into Annoucement(ScheduleID, AnnounceTime, PairDate, Importance, CreatorID, Text)" +
                " Values (?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(sql, scheduleID, time, pairDate, importanceList[importance], creatorID, text);
    }

    public List<Announcement> getByTeacherID(int teacherID, int count) {
        String sql = "Select Annoucement.*," +
                " UserInfo.* " +
                "from Annoucement, UserInfo, Schedule, Teacher where " +
                "Schedule.TeacherID = ? and Annoucement.ScheduleID = Schedule.ID " +
                "and Teacher.UserID = UserInfo.ID and Teacher.ID = Schedule.TeacherID " +
                "Limit ?";

        return this.jdbcTemplate.query(sql, new Object[] {teacherID, count}, new AnnouncementRowMapper());
    }

    public List<Announcement> getByGroupID(int groupID, int count) {
        String sql = "Select Annoucement.*," +
                " UserInfo.* " +
                "from Annoucement, UserInfo, ScheduleToGroup where " +
                "ScheduleToGroup.GroupID = ? and Annoucement.ScheduleID = ScheduleToGroup.ScheduleID " +
                "Limit ?";

        return this.jdbcTemplate.query(sql, new Object[] {groupID, count}, new AnnouncementRowMapper());
    }

    public void deleteByID(int id) {
        String sql = "Delete From Annoucement Where Annoucement.ID = ?";
        this.jdbcTemplate.update(sql, id);
    }
}
