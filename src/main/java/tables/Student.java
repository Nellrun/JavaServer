package tables;

/**
 * Created by root on 12/4/16.
 */
public class Student {
    private int id;
    private String firstName;
    private String secondName;
    private String middleName;
    private int groupID;
    private String groupShortName;
    private int levelOfAccess;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getGroupShortName() {
        return groupShortName;
    }

    public void setGroupShortName(String groupShortName) {
        this.groupShortName = groupShortName;
    }

    public int getLevelOfAccess() {
        return levelOfAccess;
    }

    public void setLevelOfAccess(int levelOfAccess) {
        this.levelOfAccess = levelOfAccess;
    }
}
