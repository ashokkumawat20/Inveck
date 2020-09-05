package in.xplorelogic.inveck.models;

public class PersonDetails {
    private int mileStonId;
    private int createdBy;
    private String fullName;
    private String designation;
    private String contactNo;
    private String email;

    public PersonDetails(int mileStonId, int createdBy, String fullName, String designation, String contactNo, String email) {
        this.mileStonId = mileStonId;
        this.createdBy = createdBy;
        this.fullName = fullName;
        this.designation = designation;
        this.contactNo = contactNo;
        this.email = email;
    }

    public int getMileStonId() {
        return mileStonId;
    }

    public void setMileStonId(int mileStonId) {
        this.mileStonId = mileStonId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
