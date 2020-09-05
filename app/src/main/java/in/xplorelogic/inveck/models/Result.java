package in.xplorelogic.inveck.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("customerId")
    @Expose
    private Integer customerId;

    @SerializedName("clientId")
    @Expose
    private Integer clientId;

    @SerializedName("milestoneId")
    @Expose
    private Integer milestoneId;

    @SerializedName("assignUserId")
    @Expose
    private Integer assignUserId;

    @SerializedName("uoM")
    @Expose
    private String uoM;

    @SerializedName("item_Type")
    @Expose
    private String item_Type;

    @SerializedName("item_No")
    @Expose
    private String item_No;

    @SerializedName("item_Name")
    @Expose
    private String item_Name;

    @SerializedName("batch")
    @Expose
    private String batch;

    @SerializedName("bUn")
    @Expose
    private String bUn;

    @SerializedName("eun")
    @Expose
    private String eun;

    @SerializedName("lot_No")
    @Expose
    private String lot_No;

    @SerializedName("container_No")
    @Expose
    private String container_No;

    @SerializedName("total_Quantity")
    @Expose
    private Integer total_Quantity;

    @SerializedName("make")
    @Expose
    private String make;

    @SerializedName("unrestricted_Quantity")
    @Expose
    private Integer unrestricted_Quantity;

    @SerializedName("value_Unrestricted")
    @Expose
    private Integer value_Unrestricted;

    @SerializedName("blocked_Quantity")
    @Expose
    private Integer blocked_Quantity;

    @SerializedName("value_Blocked_Stock")
    @Expose
    private Integer value_Blocked_Stock;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("factory")
    @Expose
    private String factory;

    @SerializedName("warehouse")
    @Expose
    private String warehouse;

    @SerializedName("whitem")
    @Expose
    private String whitem;
    @SerializedName("stock_Zone")
    @Expose
    private String stock_Zone;

    @SerializedName("stock_Location")
    @Expose
    private String stock_Location;
    @SerializedName("dF_store_location_level")
    @Expose
    private String dF_store_location_level;

    @SerializedName("stock_Segment")
    @Expose
    private String stock_Segment;

    @SerializedName("assignUserEmail")
    @Expose
    private String assignUserEmail;

    @SerializedName("statusCode")
    @Expose
    private String statusCode;

    @SerializedName("inputQty")
    @Expose
    private Integer inputQty;

    @SerializedName("extentedQty")
    @Expose
    private Integer extentedQty;

    @SerializedName("shrinkQty")
    @Expose
    private Integer shrinkQty;

    @SerializedName("isQtySync")
    @Expose
    private String isQtySync;


    @SerializedName("stockStatus")
    @Expose
    private String stockStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Integer milestoneId) {
        this.milestoneId = milestoneId;
    }

    public Integer getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(Integer assignUserId) {
        this.assignUserId = assignUserId;
    }

    public String getUoM() {
        return uoM;
    }

    public void setUoM(String uoM) {
        this.uoM = uoM;
    }

    public String getItem_Type() {
        return item_Type;
    }

    public void setItem_Type(String item_Type) {
        this.item_Type = item_Type;
    }

    public String getItem_No() {
        return item_No;
    }

    public void setItem_No(String item_No) {
        this.item_No = item_No;
    }

    public String getItem_Name() {
        return item_Name;
    }

    public void setItem_Name(String item_Name) {
        this.item_Name = item_Name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getbUn() {
        return bUn;
    }

    public void setbUn(String bUn) {
        this.bUn = bUn;
    }

    public String getEun() {
        return eun;
    }

    public void setEun(String eun) {
        this.eun = eun;
    }

    public String getLot_No() {
        return lot_No;
    }

    public void setLot_No(String lot_No) {
        this.lot_No = lot_No;
    }

    public String getContainer_No() {
        return container_No;
    }

    public void setContainer_No(String container_No) {
        this.container_No = container_No;
    }

    public Integer getTotal_Quantity() {
        return total_Quantity;
    }

    public void setTotal_Quantity(Integer total_Quantity) {
        this.total_Quantity = total_Quantity;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Integer getUnrestricted_Quantity() {
        return unrestricted_Quantity;
    }

    public void setUnrestricted_Quantity(Integer unrestricted_Quantity) {
        this.unrestricted_Quantity = unrestricted_Quantity;
    }

    public Integer getValue_Unrestricted() {
        return value_Unrestricted;
    }

    public void setValue_Unrestricted(Integer value_Unrestricted) {
        this.value_Unrestricted = value_Unrestricted;
    }

    public Integer getBlocked_Quantity() {
        return blocked_Quantity;
    }

    public void setBlocked_Quantity(Integer blocked_Quantity) {
        this.blocked_Quantity = blocked_Quantity;
    }

    public Integer getValue_Blocked_Stock() {
        return value_Blocked_Stock;
    }

    public void setValue_Blocked_Stock(Integer value_Blocked_Stock) {
        this.value_Blocked_Stock = value_Blocked_Stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
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

    public String getStock_Zone() {
        return stock_Zone;
    }

    public void setStock_Zone(String stock_Zone) {
        this.stock_Zone = stock_Zone;
    }

    public String getStock_Location() {
        return stock_Location;
    }

    public void setStock_Location(String stock_Location) {
        this.stock_Location = stock_Location;
    }

    public String getdF_store_location_level() {
        return dF_store_location_level;
    }

    public void setdF_store_location_level(String dF_store_location_level) {
        this.dF_store_location_level = dF_store_location_level;
    }

    public String getStock_Segment() {
        return stock_Segment;
    }

    public void setStock_Segment(String stock_Segment) {
        this.stock_Segment = stock_Segment;
    }

    public String getAssignUserEmail() {
        return assignUserEmail;
    }

    public void setAssignUserEmail(String assignUserEmail) {
        this.assignUserEmail = assignUserEmail;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getInputQty() {
        return inputQty;
    }

    public void setInputQty(Integer inputQty) {
        this.inputQty = inputQty;
    }

    public Integer getExtentedQty() {
        return extentedQty;
    }

    public void setExtentedQty(Integer extentedQty) {
        this.extentedQty = extentedQty;
    }

    public Integer getShrinkQty() {
        return shrinkQty;
    }

    public void setShrinkQty(Integer shrinkQty) {
        this.shrinkQty = shrinkQty;
    }

    public String getIsQtySync() {
        return isQtySync;
    }

    public void setIsQtySync(String isQtySync) {
        this.isQtySync = isQtySync;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }
}



