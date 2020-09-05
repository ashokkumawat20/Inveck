package in.xplorelogic.inveck.models;

/* renamed from: com.app.xplorelogic.inveck.UI.auth.Stock */
public class Stock {
    private  int id;
    private  int customerId;
    private  int clientId;
    private  int milestoneId;
    private  int assignUserId;
    private String bUn;
    private String batch;
    private int blocked_Quantity;
    private String container_No;
    private String dF_store_location_level;
    private String eun;
    private String factory;
    private String item_Name;
    private String item_Nom;
    private String item_Type;
    private String lot_No;
    private String make;
    private String name;
    private String stock_Location;
    private String stock_Segment;
    private String stock_Zone;
    private int total_Quantity;
    private int unrestricted_Quantity;
    private String uoM;
    private int value_Blocked_Stock;
    private int value_Unrestricted;
    private String warehouse;
    private String whitem;
    private String isQtySync;
    private int inputQty;
    private int extentedQty;
    private int shrinkQty;
    private String stockStatus="";
    private String statusCode="";
    private String assignUserEmail="";
    private String isFloorToSheet="";
    private String floorToSheetBy="";
    private int sync_status;
    private String remark="";
    private String is_UoM = "";
    private String is_Item_Type = "";
    private String is_Item_No = "";
    private String is_Item_Name = "";
    private String is_Batch = "";
    private String is_BUn = "";
    private String is_Eun = "";
    private String is_Lot_No = "";
    private String is_Container_No = "";
    private String is_Total_Quantity = "";
    private String is_Make = "";
    private String is_Unrestricted_Quantity = "";
    private String is_Value_Unrestricted = "";
    private String is_Blocked_Quantity="";
    private String is_Value_Blocked_Stock="";
    private String is_Rate="";
    private String is_Name = "";
    private String is_Factory = "";
    private String is_Warehouse = "";
    private String is_Whitem = "";
    private String is_Stock_Zone = "";
    private String is_Stock_Location = "";
    private String is_DF_store_location_level = "";
    private String is_Stock_Segment = "";
    private String is_GL_Account_Code = "";
    private String is_GL_Account_Name = "";
    private String is_Currency = "";
    private String is_Material_Group = "";
    public Stock()
    {}



    public Stock(int id, int customerId, int clientId, int milestoneId, int assignUserId, String bUn, String batch, int blocked_Quantity, String container_No, String dF_store_location_level, String eun, String factory, String item_Name, String item_Nom, String item_Type, String lot_No, String make, String name, String stock_Location, String stock_Segment, String stock_Zone, int total_Quantity, int unrestricted_Quantity, String uoM, int value_Blocked_Stock, int value_Unrestricted, String warehouse, String whitem, String isQtySync, int inputQty, int extentedQty, int shrinkQty, String stockStatus, String statusCode, String assignUserEmail, String isFloorToSheet, int sync_status, String floorToSheetBy, String remark, String is_UoM, String is_Item_Type, String is_Item_No, String is_Item_Name, String is_Batch, String is_BUn, String is_Eun, String is_Lot_No, String is_Container_No, String is_Total_Quantity, String is_Make, String is_Unrestricted_Quantity, String is_Value_Unrestricted, String is_Blocked_Quantity, String is_Value_Blocked_Stock, String is_Rate, String is_Name, String is_Factory, String is_Warehouse, String is_Whitem, String is_Stock_Zone, String is_Stock_Location, String is_DF_store_location_level, String is_Stock_Segment, String is_GL_Account_Code, String is_GL_Account_Name, String is_Currency, String is_Material_Group) {
        this.id = id;
        this.customerId = customerId;
        this.clientId = clientId;
        this.milestoneId = milestoneId;
        this.assignUserId = assignUserId;
        this.bUn = bUn;
        this.batch = batch;
        this.blocked_Quantity = blocked_Quantity;
        this.container_No = container_No;
        this.dF_store_location_level = dF_store_location_level;
        this.eun = eun;
        this.factory = factory;
        this.item_Name = item_Name;
        this.item_Nom = item_Nom;
        this.item_Type = item_Type;
        this.lot_No = lot_No;
        this.make = make;
        this.name = name;
        this.stock_Location = stock_Location;
        this.stock_Segment = stock_Segment;
        this.stock_Zone = stock_Zone;
        this.total_Quantity = total_Quantity;
        this.unrestricted_Quantity = unrestricted_Quantity;
        this.uoM = uoM;
        this.value_Blocked_Stock = value_Blocked_Stock;
        this.value_Unrestricted = value_Unrestricted;
        this.warehouse = warehouse;
        this.whitem = whitem;
        this.isQtySync = isQtySync;
        this.inputQty = inputQty;
        this.extentedQty = extentedQty;
        this.shrinkQty = shrinkQty;
        this.stockStatus = stockStatus;
        this.statusCode = statusCode;
        this.assignUserEmail = assignUserEmail;
        this.isFloorToSheet = isFloorToSheet;
        this.sync_status = sync_status;
        this.floorToSheetBy = floorToSheetBy;
        this.remark = remark;
        this.is_UoM = is_UoM;
        this.is_Item_Type = is_Item_Type;
        this.is_Item_No = is_Item_No;
        this.is_Item_Name = is_Item_Name;
        this.is_Batch = is_Batch;
        this.is_BUn = is_BUn;
        this.is_Eun = is_Eun;
        this.is_Lot_No = is_Lot_No;
        this.is_Container_No = is_Container_No;
        this.is_Total_Quantity = is_Total_Quantity;
        this.is_Make = is_Make;
        this.is_Unrestricted_Quantity = is_Unrestricted_Quantity;
        this.is_Value_Unrestricted = is_Value_Unrestricted;
        this.is_Blocked_Quantity = is_Blocked_Quantity;
        this.is_Value_Blocked_Stock = is_Value_Blocked_Stock;
        this.is_Rate = is_Rate;
        this.is_Name = is_Name;
        this.is_Factory = is_Factory;
        this.is_Warehouse = is_Warehouse;
        this.is_Whitem = is_Whitem;
        this.is_Stock_Zone = is_Stock_Zone;
        this.is_Stock_Location = is_Stock_Location;
        this.is_DF_store_location_level = is_DF_store_location_level;
        this.is_Stock_Segment = is_Stock_Segment;
        this.is_GL_Account_Code = is_GL_Account_Code;
        this.is_GL_Account_Name = is_GL_Account_Name;
        this.is_Currency = is_Currency;
        this.is_Material_Group = is_Material_Group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public int getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(int assignUserId) {
        this.assignUserId = assignUserId;
    }

    public String getbUn() {
        return bUn;
    }

    public void setbUn(String bUn) {
        this.bUn = bUn;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public int getBlocked_Quantity() {
        return blocked_Quantity;
    }

    public void setBlocked_Quantity(int blocked_Quantity) {
        this.blocked_Quantity = blocked_Quantity;
    }

    public String getContainer_No() {
        return container_No;
    }

    public void setContainer_No(String container_No) {
        this.container_No = container_No;
    }

    public String getdF_store_location_level() {
        return dF_store_location_level;
    }

    public void setdF_store_location_level(String dF_store_location_level) {
        this.dF_store_location_level = dF_store_location_level;
    }

    public String getEun() {
        return eun;
    }

    public void setEun(String eun) {
        this.eun = eun;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getItem_Name() {
        return item_Name;
    }

    public void setItem_Name(String item_Name) {
        this.item_Name = item_Name;
    }

    public String getItem_Nom() {
        return item_Nom;
    }

    public void setItem_Nom(String item_Nom) {
        this.item_Nom = item_Nom;
    }

    public String getItem_Type() {
        return item_Type;
    }

    public void setItem_Type(String item_Type) {
        this.item_Type = item_Type;
    }

    public String getLot_No() {
        return lot_No;
    }

    public void setLot_No(String lot_No) {
        this.lot_No = lot_No;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStock_Location() {
        return stock_Location;
    }

    public void setStock_Location(String stock_Location) {
        this.stock_Location = stock_Location;
    }

    public String getStock_Segment() {
        return stock_Segment;
    }

    public void setStock_Segment(String stock_Segment) {
        this.stock_Segment = stock_Segment;
    }

    public String getStock_Zone() {
        return stock_Zone;
    }

    public void setStock_Zone(String stock_Zone) {
        this.stock_Zone = stock_Zone;
    }

    public int getTotal_Quantity() {
        return total_Quantity;
    }

    public void setTotal_Quantity(int total_Quantity) {
        this.total_Quantity = total_Quantity;
    }

    public int getUnrestricted_Quantity() {
        return unrestricted_Quantity;
    }

    public void setUnrestricted_Quantity(int unrestricted_Quantity) {
        this.unrestricted_Quantity = unrestricted_Quantity;
    }

    public String getUoM() {
        return uoM;
    }

    public void setUoM(String uoM) {
        this.uoM = uoM;
    }

    public int getValue_Blocked_Stock() {
        return value_Blocked_Stock;
    }

    public void setValue_Blocked_Stock(int value_Blocked_Stock) {
        this.value_Blocked_Stock = value_Blocked_Stock;
    }

    public int getValue_Unrestricted() {
        return value_Unrestricted;
    }

    public void setValue_Unrestricted(int value_Unrestricted) {
        this.value_Unrestricted = value_Unrestricted;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getWhitem() {
        return whitem;
    }

    public void setWhitem(String whitem) {
        this.whitem = whitem;
    }

    public String getIsQtySync() {
        return isQtySync;
    }

    public void setIsQtySync(String isQtySync) {
        this.isQtySync = isQtySync;
    }

    public int getInputQty() {
        return inputQty;
    }

    public void setInputQty(int inputQty) {
        this.inputQty = inputQty;
    }

    public int getExtentedQty() {
        return extentedQty;
    }

    public void setExtentedQty(int extentedQty) {
        this.extentedQty = extentedQty;
    }

    public int getShrinkQty() {
        return shrinkQty;
    }

    public void setShrinkQty(int shrinkQty) {
        this.shrinkQty = shrinkQty;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getAssignUserEmail() {
        return assignUserEmail;
    }

    public void setAssignUserEmail(String assignUserEmail) {
        this.assignUserEmail = assignUserEmail;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    public String getIsFloorToSheet() {
        return isFloorToSheet;
    }

    public void setIsFloorToSheet(String isFloorToSheet) {
        this.isFloorToSheet = isFloorToSheet;
    }

    public String getFloorToSheetBy() {
        return floorToSheetBy;
    }

    public void setFloorToSheetBy(String floorToSheetBy) {
        this.floorToSheetBy = floorToSheetBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIs_UoM() {
        return is_UoM;
    }

    public void setIs_UoM(String is_UoM) {
        this.is_UoM = is_UoM;
    }

    public String getIs_Item_Type() {
        return is_Item_Type;
    }

    public void setIs_Item_Type(String is_Item_Type) {
        this.is_Item_Type = is_Item_Type;
    }

    public String getIs_Item_No() {
        return is_Item_No;
    }

    public void setIs_Item_No(String is_Item_No) {
        this.is_Item_No = is_Item_No;
    }

    public String getIs_Item_Name() {
        return is_Item_Name;
    }

    public void setIs_Item_Name(String is_Item_Name) {
        this.is_Item_Name = is_Item_Name;
    }

    public String getIs_Batch() {
        return is_Batch;
    }

    public void setIs_Batch(String is_Batch) {
        this.is_Batch = is_Batch;
    }

    public String getIs_BUn() {
        return is_BUn;
    }

    public void setIs_BUn(String is_BUn) {
        this.is_BUn = is_BUn;
    }

    public String getIs_Eun() {
        return is_Eun;
    }

    public void setIs_Eun(String is_Eun) {
        this.is_Eun = is_Eun;
    }

    public String getIs_Lot_No() {
        return is_Lot_No;
    }

    public void setIs_Lot_No(String is_Lot_No) {
        this.is_Lot_No = is_Lot_No;
    }

    public String getIs_Container_No() {
        return is_Container_No;
    }

    public void setIs_Container_No(String is_Container_No) {
        this.is_Container_No = is_Container_No;
    }

    public String getIs_Total_Quantity() {
        return is_Total_Quantity;
    }

    public void setIs_Total_Quantity(String is_Total_Quantity) {
        this.is_Total_Quantity = is_Total_Quantity;
    }

    public String getIs_Make() {
        return is_Make;
    }

    public void setIs_Make(String is_Make) {
        this.is_Make = is_Make;
    }

    public String getIs_Unrestricted_Quantity() {
        return is_Unrestricted_Quantity;
    }

    public void setIs_Unrestricted_Quantity(String is_Unrestricted_Quantity) {
        this.is_Unrestricted_Quantity = is_Unrestricted_Quantity;
    }

    public String getIs_Value_Unrestricted() {
        return is_Value_Unrestricted;
    }

    public void setIs_Value_Unrestricted(String is_Value_Unrestricted) {
        this.is_Value_Unrestricted = is_Value_Unrestricted;
    }

    public String getIs_Blocked_Quantity() {
        return is_Blocked_Quantity;
    }

    public void setIs_Blocked_Quantity(String is_Blocked_Quantity) {
        this.is_Blocked_Quantity = is_Blocked_Quantity;
    }

    public String getIs_Value_Blocked_Stock() {
        return is_Value_Blocked_Stock;
    }

    public void setIs_Value_Blocked_Stock(String is_Value_Blocked_Stock) {
        this.is_Value_Blocked_Stock = is_Value_Blocked_Stock;
    }

    public String getIs_Rate() {
        return is_Rate;
    }

    public void setIs_Rate(String is_Rate) {
        this.is_Rate = is_Rate;
    }

    public String getIs_Name() {
        return is_Name;
    }

    public void setIs_Name(String is_Name) {
        this.is_Name = is_Name;
    }

    public String getIs_Factory() {
        return is_Factory;
    }

    public void setIs_Factory(String is_Factory) {
        this.is_Factory = is_Factory;
    }

    public String getIs_Warehouse() {
        return is_Warehouse;
    }

    public void setIs_Warehouse(String is_Warehouse) {
        this.is_Warehouse = is_Warehouse;
    }

    public String getIs_Whitem() {
        return is_Whitem;
    }

    public void setIs_Whitem(String is_Whitem) {
        this.is_Whitem = is_Whitem;
    }

    public String getIs_Stock_Zone() {
        return is_Stock_Zone;
    }

    public void setIs_Stock_Zone(String is_Stock_Zone) {
        this.is_Stock_Zone = is_Stock_Zone;
    }

    public String getIs_Stock_Location() {
        return is_Stock_Location;
    }

    public void setIs_Stock_Location(String is_Stock_Location) {
        this.is_Stock_Location = is_Stock_Location;
    }

    public String getIs_DF_store_location_level() {
        return is_DF_store_location_level;
    }

    public void setIs_DF_store_location_level(String is_DF_store_location_level) {
        this.is_DF_store_location_level = is_DF_store_location_level;
    }

    public String getIs_Stock_Segment() {
        return is_Stock_Segment;
    }

    public void setIs_Stock_Segment(String is_Stock_Segment) {
        this.is_Stock_Segment = is_Stock_Segment;
    }

    public String getIs_GL_Account_Code() {
        return is_GL_Account_Code;
    }

    public void setIs_GL_Account_Code(String is_GL_Account_Code) {
        this.is_GL_Account_Code = is_GL_Account_Code;
    }

    public String getIs_GL_Account_Name() {
        return is_GL_Account_Name;
    }

    public void setIs_GL_Account_Name(String is_GL_Account_Name) {
        this.is_GL_Account_Name = is_GL_Account_Name;
    }

    public String getIs_Currency() {
        return is_Currency;
    }

    public void setIs_Currency(String is_Currency) {
        this.is_Currency = is_Currency;
    }

    public String getIs_Material_Group() {
        return is_Material_Group;
    }

    public void setIs_Material_Group(String is_Material_Group) {
        this.is_Material_Group = is_Material_Group;
    }
}
