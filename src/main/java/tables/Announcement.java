package tables;

import java.sql.Date;

/**
 * Created by root on 12/17/16.
 */
public class Announcement {
    private int id;
    private int scheduleID;
    private String firstName;
    private String secondName;
    private String middleName;
    private String text;
    private Date announceTime;
    private Date pairDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getAnnounceTime() {
        return announceTime;
    }

    public void setAnnounceTime(Date announceTime) {
        this.announceTime = announceTime;
    }

    public Date getPairDate() {
        return pairDate;
    }

    public void setPairDate(Date pairDate) {
        this.pairDate = pairDate;
    }
}
