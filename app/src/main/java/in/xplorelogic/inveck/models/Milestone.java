package in.xplorelogic.inveck.models;

/* renamed from: com.app.xplorelogic.inveck.UI.auth.Milestone */
public class Milestone {

    private int milestoneId;
    private int custId;
    private String custName;
    private String fromDate;
    private String inchargeStatus;
    private String milestoneName;
    private String toDate;
    private String mileStoneStatus;
    int sync_status;
    public Milestone() {
    }

    public Milestone(int custId, String custName, String fromDate, String inchargeStatus, int milestoneId, String milestoneName, String toDate, String mileStoneStatus,int sync_status) {
        this.custId = custId;
        this.custName = custName;
        this.fromDate = fromDate;
        this.inchargeStatus = inchargeStatus;
        this.milestoneId = milestoneId;
        this.milestoneName = milestoneName;
        this.toDate = toDate;
        this.mileStoneStatus = mileStoneStatus;
        this.sync_status=sync_status;
    }

    public String getMilestoneName() {
        return this.milestoneName;
    }

    public void setMilestoneName(String milestoneName2) {
        this.milestoneName = milestoneName2;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(String fromDate2) {
        this.fromDate = fromDate2;
    }

    public String getToDate() {
        return this.toDate;
    }

    public void setToDate(String toDate2) {
        this.toDate = toDate2;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String custName2) {
        this.custName = custName2;
    }

    public int getMilestoneId() {
        return this.milestoneId;
    }

    public void setMilestoneId(int milestoneId2) {
        this.milestoneId = milestoneId2;
    }

    public int getCustId() {
        return this.custId;
    }

    public void setCustId(int custId2) {
        this.custId = custId2;
    }

    public String getInchargeStatus() {
        return this.inchargeStatus;
    }

    public void setInchargeStatus(String inchargeStatus2) {
        this.inchargeStatus = inchargeStatus2;
    }

    public String getMileStoneStatus() {
        return mileStoneStatus;
    }

    public void setMileStoneStatus(String mileStoneStatus) {
        this.mileStoneStatus = mileStoneStatus;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.milestoneName);
        sb.append(" ");
        sb.append(this.custName);
        return sb.toString();
    }
}
