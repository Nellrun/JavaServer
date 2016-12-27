package tables;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by root on 12/17/16.
 */
public class AnnouncementDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private String[] importanceList = {"Low", "Medium", "High"};

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void create(int scheduleID, String pairDate, int importance, int creatorID, String text) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());

        String sql = "Inser Into Annoucement(ScheduleID, AnnounceTime, PairDate, Importance, CreatorID, Text)" +
                " Values (?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(sql, scheduleID, time, pairDate, importanceList[importance], creatorID, text);
    }
}
