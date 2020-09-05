package in.xplorelogic.inveck.models;

public class TakeSignOffDAO {
    int id;
    int mileStonId;
    String fullName = "";
    String designation = "";
    String email = "";
    String contactNo = "";

    public TakeSignOffDAO() {
    }

    public TakeSignOffDAO(int id, int mileStonId, String fullName, String designation, String email, String contactNo) {
        this.id = id;
        this.mileStonId = mileStonId;
        this.fullName = fullName;
        this.designation = designation;
        this.email = email;
        this.contactNo = contactNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMileStonId() {
        return mileStonId;
    }

    public void setMileStonId(int mileStonId) {
        this.mileStonId = mileStonId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
