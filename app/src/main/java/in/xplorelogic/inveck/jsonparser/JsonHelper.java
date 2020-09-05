package in.xplorelogic.inveck.jsonparser;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import in.xplorelogic.inveck.database.DatabaseHelper;
import in.xplorelogic.inveck.models.FilesModel;
import in.xplorelogic.inveck.models.LocationListDAO;
import in.xplorelogic.inveck.models.Milestone;
import in.xplorelogic.inveck.models.Sample;
import in.xplorelogic.inveck.models.Stock;
import in.xplorelogic.inveck.models.TakeSignOffDAO;
import in.xplorelogic.inveck.utils.Constant;
import in.xplorelogic.inveck.utils.GetFileFromServer;
import in.xplorelogic.inveck.utils.MyApp;

public class JsonHelper {


    private ArrayList<Milestone> milestoneArrayList = new ArrayList<Milestone>();
    private Milestone milestone;

    private ArrayList<Stock> stockArrayList = new ArrayList<Stock>();
    private Stock stock;

    private ArrayList<Sample> sampleArrayList = new ArrayList<Sample>();
    private Sample sample;

    private ArrayList<FilesModel> filesModelArrayList = new ArrayList<FilesModel>();
    private FilesModel filesModel;

    private ArrayList<LocationListDAO> locationListDAOArrayList = new ArrayList<LocationListDAO>();
    private LocationListDAO locationListDAO;

    private ArrayList<TakeSignOffDAO> takeSignOffDAOArrayList = new ArrayList<TakeSignOffDAO>();
    private TakeSignOffDAO takeSignOffDAO;
    DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

    public ArrayList<Milestone> parseMileStonesList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            long id = -1;
            for (int i = 0; i < leadJsonObj.length(); i++) {
                milestone = new Milestone();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                milestone.setMilestoneName(json_data.getString("milestoneName"));
                milestone.setFromDate(json_data.getString("startDate"));
                milestone.setToDate(json_data.getString("endDate"));
                milestone.setCustName(json_data.getString("customerName"));
                milestone.setMilestoneId(json_data.getInt("id"));
                milestone.setCustId(json_data.getInt("customerId"));
                milestone.setInchargeStatus(json_data.getString("isMileStoneIncharge"));
                milestone.setMileStoneStatus(json_data.getString("mileStoneStatus"));
                milestoneArrayList.add(milestone);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String dateToStr = format.format(today);
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constant.MILESTONEID, json_data.getInt("id"));
                contentValues.put(Constant.CUSTID, json_data.getInt("customerId"));
                contentValues.put(Constant.CUSTNAME, json_data.getString("customerName"));
                contentValues.put(Constant.FROMDATE, json_data.getString("startDate"));
                contentValues.put(Constant.INCHARGESTATUS, json_data.getString("isMileStoneIncharge"));
                contentValues.put(Constant.MILESTONENAME, json_data.getString("milestoneName"));
                contentValues.put(Constant.TODATE, json_data.getString("endDate"));
                contentValues.put(Constant.MILESTONESTATUS, json_data.getString("mileStoneStatus"));
                contentValues.put(Constant.SYNC_STATUS, 0);
                contentValues.put(Constant.CREATE_AT, dateToStr);
                try {
                    id = sqLiteDatabase.insertOrThrow(Constant.TABLE_MILESTONE, null, contentValues);
                } catch (SQLiteException e) {
                    //Logger.d("Exception: " + e.getMessage());
                    //Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    sqLiteDatabase.close();
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return milestoneArrayList;
    }


    public ArrayList<Stock> parseStockList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONObject jsonObject = new JSONObject(rawLeadListResponse);

            if (!jsonObject.isNull("results")) {
                JSONArray leadJsonObj = jsonObject.getJSONArray("results");
                for (int i = 0; i < leadJsonObj.length(); i++) {
                    stock = new Stock();
                    String sequence = String.format("%03d", i + 1);
                    JSONObject json_data = leadJsonObj.getJSONObject(i);
                    stock.setId(json_data.getInt("id"));
                    stock.setCustomerId(json_data.getInt("customerId"));
                    stock.setClientId(json_data.getInt("clientId"));
                    stock.setMilestoneId(json_data.getInt("milestoneId"));
                    stock.setAssignUserId(json_data.getInt("assignUserId"));
                    stock.setUoM(json_data.getString("uoM"));
                    stock.setItem_Type(json_data.getString("item_Type"));
                    stock.setItem_Nom(json_data.getString("item_No"));
                    stock.setItem_Name(json_data.getString("item_Name"));
                    stock.setBatch(json_data.getString("batch"));
                    stock.setbUn(json_data.getString("bUn"));
                    stock.setEun(json_data.getString("eun"));
                    stock.setLot_No(json_data.getString("lot_No"));
                    stock.setContainer_No(json_data.getString("container_No"));
                    stock.setTotal_Quantity(json_data.getInt("total_Quantity"));
                    stock.setMake(json_data.getString("make"));
                    stock.setUnrestricted_Quantity(json_data.getInt("unrestricted_Quantity"));
                    stock.setValue_Unrestricted(json_data.getInt("value_Unrestricted"));
                    stock.setBlocked_Quantity(json_data.getInt("blocked_Quantity"));
                    stock.setValue_Blocked_Stock(json_data.getInt("value_Blocked_Stock"));
                    stock.setName(json_data.getString("name"));
                    stock.setFactory(json_data.getString("factory"));
                    stock.setWarehouse(json_data.getString("warehouse"));
                    stock.setWhitem(json_data.getString("whitem"));
                    stock.setStock_Zone(json_data.getString("stock_Zone"));
                    stock.setStock_Location(json_data.getString("stock_Location"));
                    stock.setdF_store_location_level(json_data.getString("dF_store_location_level"));
                    stock.setStock_Segment(json_data.getString("stock_Segment"));
                    stock.setIsQtySync(json_data.getString("isQtySync"));
                    stock.setInputQty(json_data.getInt("inputQty"));
                    stock.setExtentedQty(json_data.getInt("extentedQty"));
                    stock.setShrinkQty(json_data.getInt("shrinkQty"));
                    stock.setStatusCode(json_data.getString("statusCode"));
                    stock.setAssignUserEmail(json_data.getString("assignUserEmail"));
                    stock.setStockStatus(json_data.getString("stockStatus"));
                    stock.setIsFloorToSheet(json_data.getString("isFloorToSheet"));
                    stock.setRemark(json_data.getString("remark"));
                    stock.setIs_UoM(json_data.getString("is_UoM"));
                    stock.setIs_Item_Type(json_data.getString("is_Item_Type"));
                    stock.setIs_Item_No(json_data.getString("is_Item_No"));
                    stock.setIs_Item_Name(json_data.getString("is_Item_Name"));
                    stock.setIs_Batch(json_data.getString("is_Batch"));
                    stock.setIs_BUn(json_data.getString("is_BUn"));
                    stock.setIs_Eun(json_data.getString("is_Eun"));
                    stock.setIs_Lot_No(json_data.getString("is_Lot_No"));
                    stock.setIs_Container_No(json_data.getString("is_Container_No"));
                    stock.setIs_Total_Quantity(json_data.getString("is_Total_Quantity"));
                    stock.setIs_Make(json_data.getString("is_Make"));
                    stock.setIs_Unrestricted_Quantity(json_data.getString("is_Unrestricted_Quantity"));
                    stock.setIs_Value_Unrestricted(json_data.getString("is_Value_Unrestricted"));
                    stock.setIs_Blocked_Quantity(json_data.getString("is_Blocked_Quantity"));
                    stock.setIs_Value_Blocked_Stock(json_data.getString("is_Value_Blocked_Stock"));
                    stock.setIs_Rate(json_data.getString("is_Rate"));
                    stock.setIs_Name(json_data.getString("is_Name"));
                    stock.setIs_Factory(json_data.getString("is_Factory"));
                    stock.setIs_Warehouse(json_data.getString("is_Warehouse"));
                    stock.setIs_Whitem(json_data.getString("is_Whitem"));
                    stock.setIs_Stock_Zone(json_data.getString("is_Stock_Zone"));
                    stock.setIs_Stock_Location(json_data.getString("is_Stock_Location"));
                    stock.setIs_DF_store_location_level(json_data.getString("is_DF_store_location_level"));
                    stock.setIs_Stock_Segment(json_data.getString("is_Stock_Segment"));
                    stock.setIs_GL_Account_Code(json_data.getString("is_GL_Account_Code"));
                    stock.setIs_GL_Account_Name(json_data.getString("is_GL_Account_Name"));
                    stock.setIs_Currency(json_data.getString("is_Currency"));
                    stock.setIs_Material_Group(json_data.getString("is_Material_Group"));
                    stock.setSync_status(1);
                    stockArrayList.add(stock);

                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stockArrayList;
    }

    public ArrayList<Sample> parseSampleList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONObject jsonObject = new JSONObject(rawLeadListResponse);

            if (!jsonObject.isNull("results")) {
                JSONArray leadJsonObj = jsonObject.getJSONArray("results");

                for (int i = 0; i < leadJsonObj.length(); i++) {
                    sample = new Sample();
                    String sequence = String.format("%03d", i + 1);
                    JSONObject json_data = leadJsonObj.getJSONObject(i);
                    sample.setId(json_data.getInt("id"));
                    sample.setCustomerId(json_data.getInt("customerId"));
                    sample.setClientId(json_data.getInt("clientId"));
                    sample.setMilestoneId(json_data.getInt("milestoneId"));
                    sample.setAssignUserId(json_data.getInt("assignUserId"));
                    sample.setUoM(json_data.getString("uoM"));
                    sample.setItem_Type(json_data.getString("item_Type"));
                    sample.setItem_Nom(json_data.getString("item_No"));
                    sample.setItem_Name(json_data.getString("item_Name"));
                    sample.setBatch(json_data.getString("batch"));
                    sample.setbUn(json_data.getString("bUn"));
                    sample.setEun(json_data.getString("eun"));
                    sample.setLot_No(json_data.getString("lot_No"));
                    sample.setContainer_No(json_data.getString("container_No"));
                    sample.setTotal_Quantity(json_data.getInt("total_Quantity"));
                    sample.setMake(json_data.getString("make"));
                    sample.setUnrestricted_Quantity(json_data.getInt("unrestricted_Quantity"));
                    sample.setValue_Unrestricted(json_data.getInt("value_Unrestricted"));
                    sample.setBlocked_Quantity(json_data.getInt("blocked_Quantity"));
                    sample.setValue_Blocked_Stock(json_data.getInt("value_Blocked_Stock"));
                    sample.setName(json_data.getString("name"));
                    sample.setFactory(json_data.getString("factory"));
                    sample.setWarehouse(json_data.getString("warehouse"));
                    sample.setWhitem(json_data.getString("whitem"));
                    sample.setStock_Zone(json_data.getString("stock_Zone"));
                    sample.setStock_Location(json_data.getString("stock_Location"));
                    sample.setdF_store_location_level(json_data.getString("dF_store_location_level"));
                    sample.setStock_Segment(json_data.getString("stock_Segment"));
                    sample.setIsQtySync(json_data.getString("isQtySync"));
                    sample.setInputQty(json_data.getInt("inputQty"));
                    sample.setExtentedQty(json_data.getInt("extentedQty"));
                    sample.setShrinkQty(json_data.getInt("shrinkQty"));
                    sample.setStatusCode(json_data.getString("statusCode"));
                    sample.setAssignUserEmail(json_data.getString("assignUserEmail"));
                    sample.setStockStatus(json_data.getString("stockStatus"));
                    sample.setIsFloorToSheet(json_data.getString("isFloorToSheet"));
                    sample.setRemark(json_data.getString("remark"));
                    sample.setIs_UoM(json_data.getString("is_UoM"));
                    sample.setIs_Item_Type(json_data.getString("is_Item_Type"));
                    sample.setIs_Item_No(json_data.getString("is_Item_No"));
                    sample.setIs_Item_Name(json_data.getString("is_Item_Name"));
                    sample.setIs_Batch(json_data.getString("is_Batch"));
                    sample.setIs_BUn(json_data.getString("is_BUn"));
                    sample.setIs_Eun(json_data.getString("is_Eun"));
                    sample.setIs_Lot_No(json_data.getString("is_Lot_No"));
                    sample.setIs_Container_No(json_data.getString("is_Container_No"));
                    sample.setIs_Total_Quantity(json_data.getString("is_Total_Quantity"));
                    sample.setIs_Make(json_data.getString("is_Make"));
                    sample.setIs_Unrestricted_Quantity(json_data.getString("is_Unrestricted_Quantity"));
                    sample.setIs_Value_Unrestricted(json_data.getString("is_Value_Unrestricted"));
                    sample.setIs_Blocked_Quantity(json_data.getString("is_Blocked_Quantity"));
                    sample.setIs_Value_Blocked_Stock(json_data.getString("is_Value_Blocked_Stock"));
                    sample.setIs_Rate(json_data.getString("is_Rate"));
                    sample.setIs_Name(json_data.getString("is_Name"));
                    sample.setIs_Factory(json_data.getString("is_Factory"));
                    sample.setIs_Warehouse(json_data.getString("is_Warehouse"));
                    sample.setIs_Whitem(json_data.getString("is_Whitem"));
                    sample.setIs_Stock_Zone(json_data.getString("is_Stock_Zone"));
                    sample.setIs_Stock_Location(json_data.getString("is_Stock_Location"));
                    sample.setIs_DF_store_location_level(json_data.getString("is_DF_store_location_level"));
                    sample.setIs_Stock_Segment(json_data.getString("is_Stock_Segment"));
                    sample.setIs_GL_Account_Code(json_data.getString("is_GL_Account_Code"));
                    sample.setIs_GL_Account_Name(json_data.getString("is_GL_Account_Name"));
                    sample.setIs_Currency(json_data.getString("is_Currency"));
                    sample.setIs_Material_Group(json_data.getString("is_Material_Group"));
                    sample.setSync_status(1);
                    sampleArrayList.add(sample);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sampleArrayList;
    }

    public ArrayList<FilesModel> parseMileFilesList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                filesModel = new FilesModel();
                String sequence = String.format("%03d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                filesModel.setId(json_data.getInt("id"));
                filesModel.setFile_name(json_data.getString("fileName").replace("UploadFiles/", ""));
                filesModel.setFile_path("/" + json_data.getString("fileName"));
                filesModel.setBitmap(GetFileFromServer.goforIt(json_data.getString("fileName").replace("UploadFiles/", ""), "/" + json_data.getString("fileName")));
                filesModelArrayList.add(filesModel);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return filesModelArrayList;
    }

    public ArrayList<LocationListDAO> parseLocationList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            // JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            JSONObject jsonObject = new JSONObject(rawLeadListResponse);

            if (!jsonObject.isNull("stockLocationParams")) {
                JSONArray leadJsonObj = jsonObject.getJSONArray("stockLocationParams");
                for (int i = 0; i < leadJsonObj.length(); i++) {
                    locationListDAO = new LocationListDAO();
                    String sequence = String.format("%03d", i + 1);
                    JSONObject json_data = leadJsonObj.getJSONObject(i);
                    locationListDAO.setId(json_data.getInt("id"));
                    locationListDAO.setStockId(json_data.getInt("stockId"));
                    locationListDAO.setMileStoneId(json_data.getInt("mileStoneId"));
                    locationListDAO.setQuantity(json_data.getInt("quantity"));
                    locationListDAO.setLocation(json_data.getString("location"));
                    locationListDAOArrayList.add(locationListDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return locationListDAOArrayList;
    }

    public ArrayList<TakeSignOffDAO> parseSignOffList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                takeSignOffDAO = new TakeSignOffDAO();
                String sequence = String.format("%03d", i + 1);
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                takeSignOffDAO.setId(json_data.getInt("id"));
                takeSignOffDAO.setMileStonId(json_data.getInt("mileStonId"));
                takeSignOffDAO.setFullName(json_data.getString("fullName"));
                takeSignOffDAO.setDesignation(json_data.getString("designation"));
                takeSignOffDAO.setEmail(json_data.getString("email"));
                takeSignOffDAO.setContactNo(json_data.getString("contactNo"));
                takeSignOffDAOArrayList.add(takeSignOffDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return takeSignOffDAOArrayList;
    }

    public ArrayList<Sample> parseOfflineMiletonesList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        // Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            long id = -1;
            for (int i = 0; i < leadJsonObj.length(); i++) {
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                sample = new Sample();
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String dateToStr = format.format(today);
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constant.M_ID, json_data.getInt("id"));
                contentValues.put(Constant.CUSTOMERID, json_data.getInt("customerId"));
                contentValues.put(Constant.CLIENTID, json_data.getInt("clientId"));
                contentValues.put(Constant.MILE_STONEID, json_data.getInt("milestoneId"));
                contentValues.put(Constant.ASSIGNUSERID, json_data.getInt("assignUserId"));
                contentValues.put(Constant.UOM, json_data.getString("uoM"));
                contentValues.put(Constant.ITEM_TYPE, json_data.getString("item_Type"));
                contentValues.put(Constant.ITEM_NO, json_data.getString("item_No"));
                contentValues.put(Constant.ITEM_NAME, json_data.getString("item_Name"));
                contentValues.put(Constant.BATCH, json_data.getString("batch"));
                contentValues.put(Constant.BUN, json_data.getString("bUn"));
                contentValues.put(Constant.EUN, json_data.getString("eun"));
                contentValues.put(Constant.LOT_NO, json_data.getString("lot_No"));
                contentValues.put(Constant.CONTAINER_NO, json_data.getString("container_No"));
                contentValues.put(Constant.TOTAL_QUANTITY, json_data.getInt("total_Quantity"));
                contentValues.put(Constant.MAKE, json_data.getString("make"));
                contentValues.put(Constant.UNRESTRICTED_QUANTITY, json_data.getInt("unrestricted_Quantity"));
                contentValues.put(Constant.VALUE_UNRESTRICTED, json_data.getInt("value_Unrestricted"));
                contentValues.put(Constant.BLOCKED_QUANTITY, json_data.getInt("blocked_Quantity"));
                contentValues.put(Constant.VALUE_BLOCKED_STOCK, json_data.getInt("value_Blocked_Stock"));
                contentValues.put(Constant.NAME, json_data.getString("name"));
                contentValues.put(Constant.FACTORY, json_data.getString("factory"));
                contentValues.put(Constant.WAREHOUSE, json_data.getString("warehouse"));
                contentValues.put(Constant.WHITEM, json_data.getString("whitem"));
                contentValues.put(Constant.STOCK_ZONE, json_data.getString("stock_Zone"));
                contentValues.put(Constant.STOCK_LOCATION, json_data.getString("stock_Location"));
                contentValues.put(Constant.DF_STORE_LOCATION_LEVEL, json_data.getString("dF_store_location_level"));
                contentValues.put(Constant.STOCK_SEGMENT, json_data.getString("stock_Segment"));
                contentValues.put(Constant.ASSIGNUSEREMAIL, json_data.getString("assignUserEmail"));
                contentValues.put(Constant.STATUSCODE, json_data.getString("statusCode"));
                contentValues.put(Constant.INPUTQTY, json_data.getInt("inputQty"));
                contentValues.put(Constant.EXTENTEDQTY, json_data.getInt("extentedQty"));
                contentValues.put(Constant.SHRINKQTY, json_data.getInt("shrinkQty"));
                contentValues.put(Constant.ISQTYSYNC, json_data.getString("isQtySync"));
                contentValues.put(Constant.STOCKSTATUS, json_data.getString("stockStatus"));
                contentValues.put(Constant.ISFLOORTOSHEET, json_data.getString("isFloorToSheet"));
                contentValues.put(Constant.MILESTONE_FILE_ID, json_data.getInt("mileStone_File_Id"));
                contentValues.put(Constant.FLOORTOSHEETDATE, json_data.getString("floorToSheetDate"));
                contentValues.put(Constant.FLOORTOSHEETBY, json_data.getString("floorToSheetBy"));
                contentValues.put(Constant.ISOUTOFSCOPE, json_data.getString("isOutOfScope"));
                contentValues.put(Constant.REMARK, json_data.getString("remark"));
                contentValues.put(Constant.IS_UOM, json_data.getString("is_UoM"));
                contentValues.put(Constant.IS_ITEM_TYPE, json_data.getString("is_Item_Type"));
                contentValues.put(Constant.IS_ITEM_NO, json_data.getString("is_Item_No"));
                contentValues.put(Constant.IS_ITEM_NAME, json_data.getString("is_Item_Name"));
                contentValues.put(Constant.IS_BATCH, json_data.getString("is_Batch"));
                contentValues.put(Constant.IS_BUN, json_data.getString("is_BUn"));
                contentValues.put(Constant.IS_EUN, json_data.getString("is_Eun"));
                contentValues.put(Constant.IS_LOT_NO, json_data.getString("is_Lot_No"));
                contentValues.put(Constant.IS_CONTAINER_NO, json_data.getString("is_Container_No"));
                contentValues.put(Constant.IS_TOTAL_QUANTITY, json_data.getString("is_Total_Quantity"));
                contentValues.put(Constant.IS_MAKE, json_data.getString("is_Make"));
                contentValues.put(Constant.IS_UNRESTRICTED_QUANTITY, json_data.getString("is_Unrestricted_Quantity"));
                contentValues.put(Constant.IS_VALUE_UNRESTRICTED, json_data.getString("is_Value_Unrestricted"));
                contentValues.put(Constant.IS_BLOCKED_QUANTITY, json_data.getString("is_Blocked_Quantity"));
                contentValues.put(Constant.IS_VALUE_BLOCKED_STOCK, json_data.getString("is_Value_Blocked_Stock"));
                contentValues.put(Constant.IS_RATE, json_data.getString("is_Rate"));
                contentValues.put(Constant.IS_NAME, json_data.getString("is_Name"));
                contentValues.put(Constant.IS_FACTORY, json_data.getString("is_Factory"));
                contentValues.put(Constant.IS_WAREHOUSE, json_data.getString("is_Warehouse"));
                contentValues.put(Constant.IS_WHITEM, json_data.getString("is_Whitem"));
                contentValues.put(Constant.IS_STOCK_ZONE, json_data.getString("is_Stock_Zone"));
                contentValues.put(Constant.IS_STOCK_LOCATION, json_data.getString("is_Stock_Location"));
                contentValues.put(Constant.IS_DF_STORE_LOCATION_LEVEL, json_data.getString("is_DF_store_location_level"));
                contentValues.put(Constant.IS_STOCK_SEGMENT, json_data.getString("is_Stock_Segment"));
                contentValues.put(Constant.IS_GL_ACCOUNT_CODE, json_data.getString("is_GL_Account_Code"));
                contentValues.put(Constant.IS_GL_ACCOUNT_NAME, json_data.getString("is_GL_Account_Name"));
                contentValues.put(Constant.IS_CURRENCY, json_data.getString("is_Currency"));
                contentValues.put(Constant.IS_MATERIAL_GROUP, json_data.getString("is_Material_Group"));
                if (json_data.getString("isQtySync").equals("N")) {
                    contentValues.put(Constant.SYNC_STATUS, 2);
                } else {
                    contentValues.put(Constant.SYNC_STATUS, 1);
                }
                contentValues.put(Constant.IU_SYNC_STATUS, 0);
                contentValues.put(Constant.RESET_STATUS, 0);
                contentValues.put(Constant.CREATE_AT, dateToStr);
                sample.setAssignUserId(json_data.getInt("assignUserId"));
                sampleArrayList.add(sample);
                try {
                    id = sqLiteDatabase.insertOrThrow(Constant.TABLE_MILESTONEDETAILSALL, null, contentValues);
                    //  Toast.makeText(MyApp.context, "Sending mail  " + (count++) + "/" + studentMailIdArrayList.size(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MyApp.context, "Insert Record  " + (i++) + "/" + leadJsonObj.length(), Toast.LENGTH_SHORT).show();

                } catch (SQLiteException e) {
                    //Logger.d("Exception: " + e.getMessage());
                    //Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    // sqLiteDatabase.close();
                }
            }
            sqLiteDatabase.close();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sampleArrayList;
    }
}
