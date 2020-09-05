package in.xplorelogic.inveck.models;

public class StudentListDAO {
    String itemName;
    String itemCode;

    public StudentListDAO() {
    }

    public StudentListDAO(String itemName, String itemCode) {
        this.itemName = itemName;
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public String toString() {
        return itemName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StudentListDAO) {
            StudentListDAO c = (StudentListDAO) obj;
            if (c.getItemName().equals(itemName))
                return true;
        }

        return false;
    }
}
