package tables;

import com.google.gson.annotations.Expose;

/**
 * Created by root on 12/4/16.
 */
public class Teacher {
    private String firstName;
    private String secondName;
    private String middleName;
    private int id;
    private int DepartamentID;
    @Expose private int userID;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartamentID() {
        return DepartamentID;
    }

    public void setDepartamentID(int departamentID) {
        DepartamentID = departamentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
