package in.xplorelogic.inveck.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import in.xplorelogic.inveck.models.FilesModel;
import in.xplorelogic.inveck.models.LocationListDAO;
import in.xplorelogic.inveck.models.LocationNameDAO;
import in.xplorelogic.inveck.models.Milestone;
import in.xplorelogic.inveck.models.PersonDetails;
import in.xplorelogic.inveck.models.Sample;
import in.xplorelogic.inveck.models.Stock;
import in.xplorelogic.inveck.models.TakeSignOffDAO;
import in.xplorelogic.inveck.utils.Constant;

import static in.xplorelogic.inveck.utils.Constant.ASSIGNUSEREMAIL;
import static in.xplorelogic.inveck.utils.Constant.EXTENTEDQTY;
import static in.xplorelogic.inveck.utils.Constant.ID;
import static in.xplorelogic.inveck.utils.Constant.IMILESTONEID;
import static in.xplorelogic.inveck.utils.Constant.ISQTYSYNC;
import static in.xplorelogic.inveck.utils.Constant.ITEM_NAME;
import static in.xplorelogic.inveck.utils.Constant.ITEM_NO;
import static in.xplorelogic.inveck.utils.Constant.IU_SYNC_STATUS;
import static in.xplorelogic.inveck.utils.Constant.MILE_STONEID;
import static in.xplorelogic.inveck.utils.Constant.M_ID;
import static in.xplorelogic.inveck.utils.Constant.QUANTITY;
import static in.xplorelogic.inveck.utils.Constant.RESET_STATUS;
import static in.xplorelogic.inveck.utils.Constant.SHRINKQTY;
import static in.xplorelogic.inveck.utils.Constant.STOCKID;
import static in.xplorelogic.inveck.utils.Constant.STOCKSTATUS;
import static in.xplorelogic.inveck.utils.Constant.STOCK_LOCATION;
import static in.xplorelogic.inveck.utils.Constant.SYNC_STATUS;
import static in.xplorelogic.inveck.utils.Constant.TABLE_MILESTONEDETAILSALL;
import static in.xplorelogic.inveck.utils.Constant.TABLE_STOCKLOCATION;
import static in.xplorelogic.inveck.utils.Constant.TABLE_UPDATESTOCKLOCATION;
import static in.xplorelogic.inveck.utils.Constant.TOTAL_QUANTITY;
import static in.xplorelogic.inveck.utils.Constant.VALUE_UNRESTRICTED;


public class DatabaseQueryClass {

    public List<Milestone> getAllStudent() {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Constant.TABLE_MILESTONE, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Milestone> milestoneList = new ArrayList<>();
                    do {
                        int custid = cursor.getInt(cursor.getColumnIndex(Constant.CUSTID));
                        int milestoneid = cursor.getInt(cursor.getColumnIndex(Constant.MILESTONEID));
                        String custname = cursor.getString(cursor.getColumnIndex(Constant.CUSTNAME));
                        String fromdate = cursor.getString(cursor.getColumnIndex(Constant.FROMDATE));
                        String inchargestatus = cursor.getString(cursor.getColumnIndex(Constant.INCHARGESTATUS));
                        String milestonename = cursor.getString(cursor.getColumnIndex(Constant.MILESTONENAME));
                        String todate = cursor.getString(cursor.getColumnIndex(Constant.TODATE));
                        String milestonestatus = cursor.getString(cursor.getColumnIndex(Constant.MILESTONESTATUS));
                        int sync_status = cursor.getInt(cursor.getColumnIndex(SYNC_STATUS));
                        milestoneList.add(new Milestone(custid, custname, fromdate, inchargestatus, milestoneid, milestonename, todate, milestonestatus, sync_status));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }


    public long insertPersonDetails(PersonDetails student) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.MILESTONID, student.getMileStonId());
        contentValues.put(Constant.FULLNAME, student.getFullName());
        contentValues.put(Constant.DESIGNATION, student.getDesignation());
        contentValues.put(Constant.EMAIL, student.getEmail());
        contentValues.put(Constant.CONTACTNO, student.getContactNo());
        contentValues.put(Constant.CREATEDBY, student.getCreatedBy());
        contentValues.put(SYNC_STATUS, 0);
        contentValues.put(Constant.CREATE_AT, dateToStr);
        try {
            id = sqLiteDatabase.insertOrThrow(Constant.TABLE_PERSONALDETAILS, null, contentValues);
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public long updatePersonDetails(PersonDetails student) {

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.INCHARGESTATUS, "Y");
        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_MILESTONE, contentValues, Constant.MILESTONEID + " = ? ", new String[]{String.valueOf(student.getMileStonId())});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public List<Stock> getAllSample(String user_email, int mid) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_MILESTONEDETAILSALL, null, ASSIGNUSEREMAIL + " =? and " + MILE_STONEID + " =?", new String[]{String.valueOf(user_email), String.valueOf(mid)}, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Stock> milestoneList = new ArrayList<>();
                    do {
                        int m_id = cursor.getInt(cursor.getColumnIndex(M_ID));
                        int customerId = cursor.getInt(cursor.getColumnIndex(Constant.CUSTOMERID));
                        int clientId = cursor.getInt(cursor.getColumnIndex(Constant.CLIENTID));
                        int milestoneId = cursor.getInt(cursor.getColumnIndex(MILE_STONEID));
                        int assignUserId = cursor.getInt(cursor.getColumnIndex(Constant.ASSIGNUSERID));
                        String uoM = cursor.getString(cursor.getColumnIndex(Constant.UOM));
                        String bun = cursor.getString(cursor.getColumnIndex(Constant.BUN));
                        String item_Type = cursor.getString(cursor.getColumnIndex(Constant.ITEM_TYPE));
                        String item_No = cursor.getString(cursor.getColumnIndex(ITEM_NO));
                        String batch = cursor.getString(cursor.getColumnIndex(Constant.BATCH));
                        int blocked_quantity = cursor.getInt(cursor.getColumnIndex(Constant.BLOCKED_QUANTITY));
                        String container_no = cursor.getString(cursor.getColumnIndex(Constant.CONTAINER_NO));
                        String df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.DF_STORE_LOCATION_LEVEL));
                        String eun = cursor.getString(cursor.getColumnIndex(Constant.EUN));
                        String factory = cursor.getString(cursor.getColumnIndex(Constant.FACTORY));
                        String item_name = cursor.getString(cursor.getColumnIndex(ITEM_NAME));
                        String lot_no = cursor.getString(cursor.getColumnIndex(Constant.LOT_NO));
                        String make = cursor.getString(cursor.getColumnIndex(Constant.MAKE));
                        String name = cursor.getString(cursor.getColumnIndex(Constant.NAME));
                        String stock_location = cursor.getString(cursor.getColumnIndex(STOCK_LOCATION));
                        String stock_segment = cursor.getString(cursor.getColumnIndex(Constant.STOCK_SEGMENT));
                        String stock_zone = cursor.getString(cursor.getColumnIndex(Constant.STOCK_ZONE));
                        int total_quantity = cursor.getInt(cursor.getColumnIndex(TOTAL_QUANTITY));
                        int unrestricted_quantity = cursor.getInt(cursor.getColumnIndex(Constant.UNRESTRICTED_QUANTITY));
                        int value_blocked_stock = cursor.getInt(cursor.getColumnIndex(Constant.VALUE_BLOCKED_STOCK));
                        int value_unrestricted = cursor.getInt(cursor.getColumnIndex(VALUE_UNRESTRICTED));
                        String warehouse = cursor.getString(cursor.getColumnIndex(Constant.WAREHOUSE));
                        String whitem = cursor.getString(cursor.getColumnIndex(Constant.WHITEM));
                        String isqtysync = cursor.getString(cursor.getColumnIndex(ISQTYSYNC));
                        int inputqty = cursor.getInt(cursor.getColumnIndex(Constant.INPUTQTY));
                        int extentedqty = cursor.getInt(cursor.getColumnIndex(EXTENTEDQTY));
                        int shrinkqty = cursor.getInt(cursor.getColumnIndex(SHRINKQTY));
                        String stockstatus = cursor.getString(cursor.getColumnIndex(STOCKSTATUS));
                        String statuscode = cursor.getString(cursor.getColumnIndex(Constant.STATUSCODE));
                        String assignuseremail = cursor.getString(cursor.getColumnIndex(ASSIGNUSEREMAIL));
                        String isfloortosheet = cursor.getString(cursor.getColumnIndex(Constant.ISFLOORTOSHEET));
                        int sync_status = cursor.getInt(cursor.getColumnIndex(SYNC_STATUS));
                        String floortosheetby = cursor.getString(cursor.getColumnIndex(Constant.FLOORTOSHEETBY));
                        String remark = cursor.getString(cursor.getColumnIndex(Constant.REMARK));
                        String is_uom = cursor.getString(cursor.getColumnIndex(Constant.IS_UOM));
                        String is_item_type = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_TYPE));
                        String is_item_no = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NO));
                        String is_item_name = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NAME));
                        String is_batch = cursor.getString(cursor.getColumnIndex(Constant.IS_BATCH));
                        String is_bun = cursor.getString(cursor.getColumnIndex(Constant.IS_BUN));
                        String is_eun = cursor.getString(cursor.getColumnIndex(Constant.IS_EUN));
                        String is_lot_no = cursor.getString(cursor.getColumnIndex(Constant.IS_LOT_NO));
                        String is_container_no = cursor.getString(cursor.getColumnIndex(Constant.IS_CONTAINER_NO));
                        String is_total_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_TOTAL_QUANTITY));
                        String is_make = cursor.getString(cursor.getColumnIndex(Constant.IS_MAKE));
                        String is_unrestricted_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_UNRESTRICTED_QUANTITY));
                        String is_value_unrestricted = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_UNRESTRICTED));
                        String is_blocked_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_BLOCKED_QUANTITY));
                        String is_value_blocked_stock = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_BLOCKED_STOCK));
                        String is_rate = cursor.getString(cursor.getColumnIndex(Constant.IS_RATE));
                        String is_name = cursor.getString(cursor.getColumnIndex(Constant.IS_NAME));
                        String is_factory = cursor.getString(cursor.getColumnIndex(Constant.IS_FACTORY));
                        String is_warehouse = cursor.getString(cursor.getColumnIndex(Constant.IS_WAREHOUSE));
                        String is_whitem = cursor.getString(cursor.getColumnIndex(Constant.IS_WHITEM));
                        String is_stock_zone = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_ZONE));
                        String is_stock_location = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_LOCATION));
                        String is_df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.IS_DF_STORE_LOCATION_LEVEL));
                        String is_stock_segment = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_SEGMENT));
                        String is_gl_account_code = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_CODE));
                        String is_gl_account_name = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_NAME));
                        String is_currency = cursor.getString(cursor.getColumnIndex(Constant.IS_CURRENCY));
                        String is_material_group = cursor.getString(cursor.getColumnIndex(Constant.IS_MATERIAL_GROUP));
                        milestoneList.add(new Stock(m_id, customerId, clientId, milestoneId, assignUserId, bun, batch, blocked_quantity, container_no, df_store_location_level, eun, factory, item_name, item_No, item_Type, lot_no, make, name, stock_location, stock_segment, stock_zone, total_quantity, unrestricted_quantity, uoM, value_blocked_stock, value_unrestricted, warehouse, whitem, isqtysync, inputqty, extentedqty, shrinkqty, stockstatus, statuscode, assignuseremail, isfloortosheet, sync_status, floortosheetby, remark, is_uom, is_item_type, is_item_no, is_item_name, is_batch, is_bun, is_eun, is_lot_no, is_container_no, is_total_quantity, is_make, is_unrestricted_quantity, is_value_unrestricted, is_blocked_quantity, is_value_blocked_stock, is_rate, is_name, is_factory, is_warehouse, is_whitem, is_stock_zone, is_stock_location, is_df_store_location_level, is_stock_segment, is_gl_account_code, is_gl_account_name, is_currency, is_material_group));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public List<Stock> getAllPending(int mid) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_MILESTONEDETAILSALL, null, STOCKSTATUS + " =? and " + MILE_STONEID + " =?", new String[]{String.valueOf("P"), String.valueOf(mid)}, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Stock> milestoneList = new ArrayList<>();
                    do {
                        int m_id = cursor.getInt(cursor.getColumnIndex(M_ID));
                        int customerId = cursor.getInt(cursor.getColumnIndex(Constant.CUSTOMERID));
                        int clientId = cursor.getInt(cursor.getColumnIndex(Constant.CLIENTID));
                        int milestoneId = cursor.getInt(cursor.getColumnIndex(MILE_STONEID));
                        int assignUserId = cursor.getInt(cursor.getColumnIndex(Constant.ASSIGNUSERID));
                        String uoM = cursor.getString(cursor.getColumnIndex(Constant.UOM));
                        String bun = cursor.getString(cursor.getColumnIndex(Constant.BUN));
                        String item_Type = cursor.getString(cursor.getColumnIndex(Constant.ITEM_TYPE));
                        String item_No = cursor.getString(cursor.getColumnIndex(ITEM_NO));
                        String batch = cursor.getString(cursor.getColumnIndex(Constant.BATCH));
                        int blocked_quantity = cursor.getInt(cursor.getColumnIndex(Constant.BLOCKED_QUANTITY));
                        String container_no = cursor.getString(cursor.getColumnIndex(Constant.CONTAINER_NO));
                        String df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.DF_STORE_LOCATION_LEVEL));
                        String eun = cursor.getString(cursor.getColumnIndex(Constant.EUN));
                        String factory = cursor.getString(cursor.getColumnIndex(Constant.FACTORY));
                        String item_name = cursor.getString(cursor.getColumnIndex(ITEM_NAME));
                        String lot_no = cursor.getString(cursor.getColumnIndex(Constant.LOT_NO));
                        String make = cursor.getString(cursor.getColumnIndex(Constant.MAKE));
                        String name = cursor.getString(cursor.getColumnIndex(Constant.NAME));
                        String stock_location = cursor.getString(cursor.getColumnIndex(STOCK_LOCATION));
                        String stock_segment = cursor.getString(cursor.getColumnIndex(Constant.STOCK_SEGMENT));
                        String stock_zone = cursor.getString(cursor.getColumnIndex(Constant.STOCK_ZONE));
                        int total_quantity = cursor.getInt(cursor.getColumnIndex(TOTAL_QUANTITY));
                        int unrestricted_quantity = cursor.getInt(cursor.getColumnIndex(Constant.UNRESTRICTED_QUANTITY));
                        int value_blocked_stock = cursor.getInt(cursor.getColumnIndex(Constant.VALUE_BLOCKED_STOCK));
                        int value_unrestricted = cursor.getInt(cursor.getColumnIndex(VALUE_UNRESTRICTED));
                        String warehouse = cursor.getString(cursor.getColumnIndex(Constant.WAREHOUSE));
                        String whitem = cursor.getString(cursor.getColumnIndex(Constant.WHITEM));
                        String isqtysync = cursor.getString(cursor.getColumnIndex(ISQTYSYNC));
                        int inputqty = cursor.getInt(cursor.getColumnIndex(Constant.INPUTQTY));
                        int extentedqty = cursor.getInt(cursor.getColumnIndex(EXTENTEDQTY));
                        int shrinkqty = cursor.getInt(cursor.getColumnIndex(SHRINKQTY));
                        String stockstatus = cursor.getString(cursor.getColumnIndex(STOCKSTATUS));
                        String statuscode = cursor.getString(cursor.getColumnIndex(Constant.STATUSCODE));
                        String assignuseremail = cursor.getString(cursor.getColumnIndex(ASSIGNUSEREMAIL));
                        String isfloortosheet = cursor.getString(cursor.getColumnIndex(Constant.ISFLOORTOSHEET));
                        int sync_status = cursor.getInt(cursor.getColumnIndex(SYNC_STATUS));
                        String floortosheetby = cursor.getString(cursor.getColumnIndex(Constant.FLOORTOSHEETBY));
                        String remark = cursor.getString(cursor.getColumnIndex(Constant.REMARK));
                        String is_uom = cursor.getString(cursor.getColumnIndex(Constant.IS_UOM));
                        String is_item_type = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_TYPE));
                        String is_item_no = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NO));
                        String is_item_name = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NAME));
                        String is_batch = cursor.getString(cursor.getColumnIndex(Constant.IS_BATCH));
                        String is_bun = cursor.getString(cursor.getColumnIndex(Constant.IS_BUN));
                        String is_eun = cursor.getString(cursor.getColumnIndex(Constant.IS_EUN));
                        String is_lot_no = cursor.getString(cursor.getColumnIndex(Constant.IS_LOT_NO));
                        String is_container_no = cursor.getString(cursor.getColumnIndex(Constant.IS_CONTAINER_NO));
                        String is_total_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_TOTAL_QUANTITY));
                        String is_make = cursor.getString(cursor.getColumnIndex(Constant.IS_MAKE));
                        String is_unrestricted_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_UNRESTRICTED_QUANTITY));
                        String is_value_unrestricted = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_UNRESTRICTED));
                        String is_blocked_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_BLOCKED_QUANTITY));
                        String is_value_blocked_stock = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_BLOCKED_STOCK));
                        String is_rate = cursor.getString(cursor.getColumnIndex(Constant.IS_RATE));
                        String is_name = cursor.getString(cursor.getColumnIndex(Constant.IS_NAME));
                        String is_factory = cursor.getString(cursor.getColumnIndex(Constant.IS_FACTORY));
                        String is_warehouse = cursor.getString(cursor.getColumnIndex(Constant.IS_WAREHOUSE));
                        String is_whitem = cursor.getString(cursor.getColumnIndex(Constant.IS_WHITEM));
                        String is_stock_zone = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_ZONE));
                        String is_stock_location = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_LOCATION));
                        String is_df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.IS_DF_STORE_LOCATION_LEVEL));
                        String is_stock_segment = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_SEGMENT));
                        String is_gl_account_code = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_CODE));
                        String is_gl_account_name = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_NAME));
                        String is_currency = cursor.getString(cursor.getColumnIndex(Constant.IS_CURRENCY));
                        String is_material_group = cursor.getString(cursor.getColumnIndex(Constant.IS_MATERIAL_GROUP));
                        milestoneList.add(new Stock(m_id, customerId, clientId, milestoneId, assignUserId, bun, batch, blocked_quantity, container_no, df_store_location_level, eun, factory, item_name, item_No, item_Type, lot_no, make, name, stock_location, stock_segment, stock_zone, total_quantity, unrestricted_quantity, uoM, value_blocked_stock, value_unrestricted, warehouse, whitem, isqtysync, inputqty, extentedqty, shrinkqty, stockstatus, statuscode, assignuseremail, isfloortosheet, sync_status, floortosheetby, remark, is_uom, is_item_type, is_item_no, is_item_name, is_batch, is_bun, is_eun, is_lot_no, is_container_no, is_total_quantity, is_make, is_unrestricted_quantity, is_value_unrestricted, is_blocked_quantity, is_value_blocked_stock, is_rate, is_name, is_factory, is_warehouse, is_whitem, is_stock_zone, is_stock_location, is_df_store_location_level, is_stock_segment, is_gl_account_code, is_gl_account_name, is_currency, is_material_group));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public List<Stock> getAllExShortage(int mid) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            //   cursor = sqLiteDatabase.query(TABLE_MILESTONEDETAILSALL, null, EXTENTEDQTY + " =>? and " + MILE_STONEID + " =?", new String[]{String.valueOf("0.0"), String.valueOf(mid)}, null, null, null, null);
            // SELECT * from MileStoneDetailsAll where milestoneId=1 and(extentedQty>0.0 or shrinkQty<0.0)

            // If you want to execute raw query then uncomment below 2 lines. And comment out above line.
            String SELECT_QUERY = "SELECT * from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid + " and " + EXTENTEDQTY + " > " + 0.0 + " or " + SHRINKQTY + " < " + 0.0 + "";

            //   String SELECT_QUERY = String.format("SELECT * from MileStoneDetailsAll where milestoneId=", mid, " and(extentedQty>0.0 or shrinkQty<0.0)");
            cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);


            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Stock> milestoneList = new ArrayList<>();
                    do {
                        int m_id = cursor.getInt(cursor.getColumnIndex(M_ID));
                        int customerId = cursor.getInt(cursor.getColumnIndex(Constant.CUSTOMERID));
                        int clientId = cursor.getInt(cursor.getColumnIndex(Constant.CLIENTID));
                        int milestoneId = cursor.getInt(cursor.getColumnIndex(MILE_STONEID));
                        int assignUserId = cursor.getInt(cursor.getColumnIndex(Constant.ASSIGNUSERID));
                        String uoM = cursor.getString(cursor.getColumnIndex(Constant.UOM));
                        String bun = cursor.getString(cursor.getColumnIndex(Constant.BUN));
                        String item_Type = cursor.getString(cursor.getColumnIndex(Constant.ITEM_TYPE));
                        String item_No = cursor.getString(cursor.getColumnIndex(ITEM_NO));
                        String batch = cursor.getString(cursor.getColumnIndex(Constant.BATCH));
                        int blocked_quantity = cursor.getInt(cursor.getColumnIndex(Constant.BLOCKED_QUANTITY));
                        String container_no = cursor.getString(cursor.getColumnIndex(Constant.CONTAINER_NO));
                        String df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.DF_STORE_LOCATION_LEVEL));
                        String eun = cursor.getString(cursor.getColumnIndex(Constant.EUN));
                        String factory = cursor.getString(cursor.getColumnIndex(Constant.FACTORY));
                        String item_name = cursor.getString(cursor.getColumnIndex(ITEM_NAME));
                        String lot_no = cursor.getString(cursor.getColumnIndex(Constant.LOT_NO));
                        String make = cursor.getString(cursor.getColumnIndex(Constant.MAKE));
                        String name = cursor.getString(cursor.getColumnIndex(Constant.NAME));
                        String stock_location = cursor.getString(cursor.getColumnIndex(STOCK_LOCATION));
                        String stock_segment = cursor.getString(cursor.getColumnIndex(Constant.STOCK_SEGMENT));
                        String stock_zone = cursor.getString(cursor.getColumnIndex(Constant.STOCK_ZONE));
                        int total_quantity = cursor.getInt(cursor.getColumnIndex(TOTAL_QUANTITY));
                        int unrestricted_quantity = cursor.getInt(cursor.getColumnIndex(Constant.UNRESTRICTED_QUANTITY));
                        int value_blocked_stock = cursor.getInt(cursor.getColumnIndex(Constant.VALUE_BLOCKED_STOCK));
                        int value_unrestricted = cursor.getInt(cursor.getColumnIndex(VALUE_UNRESTRICTED));
                        String warehouse = cursor.getString(cursor.getColumnIndex(Constant.WAREHOUSE));
                        String whitem = cursor.getString(cursor.getColumnIndex(Constant.WHITEM));
                        String isqtysync = cursor.getString(cursor.getColumnIndex(ISQTYSYNC));
                        int inputqty = cursor.getInt(cursor.getColumnIndex(Constant.INPUTQTY));
                        int extentedqty = cursor.getInt(cursor.getColumnIndex(EXTENTEDQTY));
                        int shrinkqty = cursor.getInt(cursor.getColumnIndex(SHRINKQTY));
                        String stockstatus = cursor.getString(cursor.getColumnIndex(STOCKSTATUS));
                        String statuscode = cursor.getString(cursor.getColumnIndex(Constant.STATUSCODE));
                        String assignuseremail = cursor.getString(cursor.getColumnIndex(ASSIGNUSEREMAIL));
                        String isfloortosheet = cursor.getString(cursor.getColumnIndex(Constant.ISFLOORTOSHEET));
                        int sync_status = cursor.getInt(cursor.getColumnIndex(SYNC_STATUS));
                        String floortosheetby = cursor.getString(cursor.getColumnIndex(Constant.FLOORTOSHEETBY));
                        String remark = cursor.getString(cursor.getColumnIndex(Constant.REMARK));
                        String is_uom = cursor.getString(cursor.getColumnIndex(Constant.IS_UOM));
                        String is_item_type = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_TYPE));
                        String is_item_no = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NO));
                        String is_item_name = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NAME));
                        String is_batch = cursor.getString(cursor.getColumnIndex(Constant.IS_BATCH));
                        String is_bun = cursor.getString(cursor.getColumnIndex(Constant.IS_BUN));
                        String is_eun = cursor.getString(cursor.getColumnIndex(Constant.IS_EUN));
                        String is_lot_no = cursor.getString(cursor.getColumnIndex(Constant.IS_LOT_NO));
                        String is_container_no = cursor.getString(cursor.getColumnIndex(Constant.IS_CONTAINER_NO));
                        String is_total_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_TOTAL_QUANTITY));
                        String is_make = cursor.getString(cursor.getColumnIndex(Constant.IS_MAKE));
                        String is_unrestricted_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_UNRESTRICTED_QUANTITY));
                        String is_value_unrestricted = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_UNRESTRICTED));
                        String is_blocked_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_BLOCKED_QUANTITY));
                        String is_value_blocked_stock = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_BLOCKED_STOCK));
                        String is_rate = cursor.getString(cursor.getColumnIndex(Constant.IS_RATE));
                        String is_name = cursor.getString(cursor.getColumnIndex(Constant.IS_NAME));
                        String is_factory = cursor.getString(cursor.getColumnIndex(Constant.IS_FACTORY));
                        String is_warehouse = cursor.getString(cursor.getColumnIndex(Constant.IS_WAREHOUSE));
                        String is_whitem = cursor.getString(cursor.getColumnIndex(Constant.IS_WHITEM));
                        String is_stock_zone = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_ZONE));
                        String is_stock_location = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_LOCATION));
                        String is_df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.IS_DF_STORE_LOCATION_LEVEL));
                        String is_stock_segment = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_SEGMENT));
                        String is_gl_account_code = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_CODE));
                        String is_gl_account_name = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_NAME));
                        String is_currency = cursor.getString(cursor.getColumnIndex(Constant.IS_CURRENCY));
                        String is_material_group = cursor.getString(cursor.getColumnIndex(Constant.IS_MATERIAL_GROUP));
                        milestoneList.add(new Stock(m_id, customerId, clientId, milestoneId, assignUserId, bun, batch, blocked_quantity, container_no, df_store_location_level, eun, factory, item_name, item_No, item_Type, lot_no, make, name, stock_location, stock_segment, stock_zone, total_quantity, unrestricted_quantity, uoM, value_blocked_stock, value_unrestricted, warehouse, whitem, isqtysync, inputqty, extentedqty, shrinkqty, stockstatus, statuscode, assignuseremail, isfloortosheet, sync_status, floortosheetby, remark, is_uom, is_item_type, is_item_no, is_item_name, is_batch, is_bun, is_eun, is_lot_no, is_container_no, is_total_quantity, is_make, is_unrestricted_quantity, is_value_unrestricted, is_blocked_quantity, is_value_blocked_stock, is_rate, is_name, is_factory, is_warehouse, is_whitem, is_stock_zone, is_stock_location, is_df_store_location_level, is_stock_segment, is_gl_account_code, is_gl_account_name, is_currency, is_material_group));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public List<Stock> getAllTotalStock(int mid) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_MILESTONEDETAILSALL, null, MILE_STONEID + " =?", new String[]{String.valueOf(mid)}, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Stock> milestoneList = new ArrayList<>();
                    do {
                        int m_id = cursor.getInt(cursor.getColumnIndex(M_ID));
                        int customerId = cursor.getInt(cursor.getColumnIndex(Constant.CUSTOMERID));
                        int clientId = cursor.getInt(cursor.getColumnIndex(Constant.CLIENTID));
                        int milestoneId = cursor.getInt(cursor.getColumnIndex(MILE_STONEID));
                        int assignUserId = cursor.getInt(cursor.getColumnIndex(Constant.ASSIGNUSERID));
                        String uoM = cursor.getString(cursor.getColumnIndex(Constant.UOM));
                        String bun = cursor.getString(cursor.getColumnIndex(Constant.BUN));
                        String item_Type = cursor.getString(cursor.getColumnIndex(Constant.ITEM_TYPE));
                        String item_No = cursor.getString(cursor.getColumnIndex(ITEM_NO));
                        String batch = cursor.getString(cursor.getColumnIndex(Constant.BATCH));
                        int blocked_quantity = cursor.getInt(cursor.getColumnIndex(Constant.BLOCKED_QUANTITY));
                        String container_no = cursor.getString(cursor.getColumnIndex(Constant.CONTAINER_NO));
                        String df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.DF_STORE_LOCATION_LEVEL));
                        String eun = cursor.getString(cursor.getColumnIndex(Constant.EUN));
                        String factory = cursor.getString(cursor.getColumnIndex(Constant.FACTORY));
                        String item_name = cursor.getString(cursor.getColumnIndex(ITEM_NAME));
                        String lot_no = cursor.getString(cursor.getColumnIndex(Constant.LOT_NO));
                        String make = cursor.getString(cursor.getColumnIndex(Constant.MAKE));
                        String name = cursor.getString(cursor.getColumnIndex(Constant.NAME));
                        String stock_location = cursor.getString(cursor.getColumnIndex(STOCK_LOCATION));
                        String stock_segment = cursor.getString(cursor.getColumnIndex(Constant.STOCK_SEGMENT));
                        String stock_zone = cursor.getString(cursor.getColumnIndex(Constant.STOCK_ZONE));
                        int total_quantity = cursor.getInt(cursor.getColumnIndex(TOTAL_QUANTITY));
                        int unrestricted_quantity = cursor.getInt(cursor.getColumnIndex(Constant.UNRESTRICTED_QUANTITY));
                        int value_blocked_stock = cursor.getInt(cursor.getColumnIndex(Constant.VALUE_BLOCKED_STOCK));
                        int value_unrestricted = cursor.getInt(cursor.getColumnIndex(VALUE_UNRESTRICTED));
                        String warehouse = cursor.getString(cursor.getColumnIndex(Constant.WAREHOUSE));
                        String whitem = cursor.getString(cursor.getColumnIndex(Constant.WHITEM));
                        String isqtysync = cursor.getString(cursor.getColumnIndex(ISQTYSYNC));
                        int inputqty = cursor.getInt(cursor.getColumnIndex(Constant.INPUTQTY));
                        int extentedqty = cursor.getInt(cursor.getColumnIndex(EXTENTEDQTY));
                        int shrinkqty = cursor.getInt(cursor.getColumnIndex(SHRINKQTY));
                        String stockstatus = cursor.getString(cursor.getColumnIndex(STOCKSTATUS));
                        String statuscode = cursor.getString(cursor.getColumnIndex(Constant.STATUSCODE));
                        String assignuseremail = cursor.getString(cursor.getColumnIndex(ASSIGNUSEREMAIL));
                        String isfloortosheet = cursor.getString(cursor.getColumnIndex(Constant.ISFLOORTOSHEET));
                        int sync_status = cursor.getInt(cursor.getColumnIndex(SYNC_STATUS));
                        String floortosheetby = cursor.getString(cursor.getColumnIndex(Constant.FLOORTOSHEETBY));
                        String remark = cursor.getString(cursor.getColumnIndex(Constant.REMARK));
                        String is_uom = cursor.getString(cursor.getColumnIndex(Constant.IS_UOM));
                        String is_item_type = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_TYPE));
                        String is_item_no = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NO));
                        String is_item_name = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NAME));
                        String is_batch = cursor.getString(cursor.getColumnIndex(Constant.IS_BATCH));
                        String is_bun = cursor.getString(cursor.getColumnIndex(Constant.IS_BUN));
                        String is_eun = cursor.getString(cursor.getColumnIndex(Constant.IS_EUN));
                        String is_lot_no = cursor.getString(cursor.getColumnIndex(Constant.IS_LOT_NO));
                        String is_container_no = cursor.getString(cursor.getColumnIndex(Constant.IS_CONTAINER_NO));
                        String is_total_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_TOTAL_QUANTITY));
                        String is_make = cursor.getString(cursor.getColumnIndex(Constant.IS_MAKE));
                        String is_unrestricted_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_UNRESTRICTED_QUANTITY));
                        String is_value_unrestricted = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_UNRESTRICTED));
                        String is_blocked_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_BLOCKED_QUANTITY));
                        String is_value_blocked_stock = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_BLOCKED_STOCK));
                        String is_rate = cursor.getString(cursor.getColumnIndex(Constant.IS_RATE));
                        String is_name = cursor.getString(cursor.getColumnIndex(Constant.IS_NAME));
                        String is_factory = cursor.getString(cursor.getColumnIndex(Constant.IS_FACTORY));
                        String is_warehouse = cursor.getString(cursor.getColumnIndex(Constant.IS_WAREHOUSE));
                        String is_whitem = cursor.getString(cursor.getColumnIndex(Constant.IS_WHITEM));
                        String is_stock_zone = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_ZONE));
                        String is_stock_location = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_LOCATION));
                        String is_df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.IS_DF_STORE_LOCATION_LEVEL));
                        String is_stock_segment = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_SEGMENT));
                        String is_gl_account_code = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_CODE));
                        String is_gl_account_name = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_NAME));
                        String is_currency = cursor.getString(cursor.getColumnIndex(Constant.IS_CURRENCY));
                        String is_material_group = cursor.getString(cursor.getColumnIndex(Constant.IS_MATERIAL_GROUP));
                        milestoneList.add(new Stock(m_id, customerId, clientId, milestoneId, assignUserId, bun, batch, blocked_quantity, container_no, df_store_location_level, eun, factory, item_name, item_No, item_Type, lot_no, make, name, stock_location, stock_segment, stock_zone, total_quantity, unrestricted_quantity, uoM, value_blocked_stock, value_unrestricted, warehouse, whitem, isqtysync, inputqty, extentedqty, shrinkqty, stockstatus, statuscode, assignuseremail, isfloortosheet, sync_status, floortosheetby, remark, is_uom, is_item_type, is_item_no, is_item_name, is_batch, is_bun, is_eun, is_lot_no, is_container_no, is_total_quantity, is_make, is_unrestricted_quantity, is_value_unrestricted, is_blocked_quantity, is_value_blocked_stock, is_rate, is_name, is_factory, is_warehouse, is_whitem, is_stock_zone, is_stock_location, is_df_store_location_level, is_stock_segment, is_gl_account_code, is_gl_account_name, is_currency, is_material_group));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public List<Sample> getAllSampleR() {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_MILESTONEDETAILSALL, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Sample> milestoneList = new ArrayList<>();
                    do {
                        int m_id = cursor.getInt(cursor.getColumnIndex(M_ID));
                        int customerId = cursor.getInt(cursor.getColumnIndex(Constant.CUSTOMERID));
                        int clientId = cursor.getInt(cursor.getColumnIndex(Constant.CLIENTID));
                        int milestoneId = cursor.getInt(cursor.getColumnIndex(MILE_STONEID));
                        int assignUserId = cursor.getInt(cursor.getColumnIndex(Constant.ASSIGNUSERID));
                        String uoM = cursor.getString(cursor.getColumnIndex(Constant.UOM));
                        String bun = cursor.getString(cursor.getColumnIndex(Constant.BUN));
                        String item_Type = cursor.getString(cursor.getColumnIndex(Constant.ITEM_TYPE));
                        String item_No = cursor.getString(cursor.getColumnIndex(ITEM_NO));
                        String batch = cursor.getString(cursor.getColumnIndex(Constant.BATCH));
                        int blocked_quantity = cursor.getInt(cursor.getColumnIndex(Constant.BLOCKED_QUANTITY));
                        String container_no = cursor.getString(cursor.getColumnIndex(Constant.CONTAINER_NO));
                        String df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.DF_STORE_LOCATION_LEVEL));
                        String eun = cursor.getString(cursor.getColumnIndex(Constant.EUN));
                        String factory = cursor.getString(cursor.getColumnIndex(Constant.FACTORY));
                        String item_name = cursor.getString(cursor.getColumnIndex(ITEM_NAME));
                        String lot_no = cursor.getString(cursor.getColumnIndex(Constant.LOT_NO));
                        String make = cursor.getString(cursor.getColumnIndex(Constant.MAKE));
                        String name = cursor.getString(cursor.getColumnIndex(Constant.NAME));
                        String stock_location = cursor.getString(cursor.getColumnIndex(STOCK_LOCATION));
                        String stock_segment = cursor.getString(cursor.getColumnIndex(Constant.STOCK_SEGMENT));
                        String stock_zone = cursor.getString(cursor.getColumnIndex(Constant.STOCK_ZONE));
                        int total_quantity = cursor.getInt(cursor.getColumnIndex(TOTAL_QUANTITY));
                        int unrestricted_quantity = cursor.getInt(cursor.getColumnIndex(Constant.UNRESTRICTED_QUANTITY));
                        int value_blocked_stock = cursor.getInt(cursor.getColumnIndex(Constant.VALUE_BLOCKED_STOCK));
                        int value_unrestricted = cursor.getInt(cursor.getColumnIndex(VALUE_UNRESTRICTED));
                        String warehouse = cursor.getString(cursor.getColumnIndex(Constant.WAREHOUSE));
                        String whitem = cursor.getString(cursor.getColumnIndex(Constant.WHITEM));
                        String isqtysync = cursor.getString(cursor.getColumnIndex(ISQTYSYNC));
                        int inputqty = cursor.getInt(cursor.getColumnIndex(Constant.INPUTQTY));
                        int extentedqty = cursor.getInt(cursor.getColumnIndex(EXTENTEDQTY));
                        int shrinkqty = cursor.getInt(cursor.getColumnIndex(SHRINKQTY));
                        String stockstatus = cursor.getString(cursor.getColumnIndex(STOCKSTATUS));
                        String statuscode = cursor.getString(cursor.getColumnIndex(Constant.STATUSCODE));
                        String assignuseremail = cursor.getString(cursor.getColumnIndex(ASSIGNUSEREMAIL));
                        String isfloortosheet = cursor.getString(cursor.getColumnIndex(Constant.ISFLOORTOSHEET));
                        int sync_status = cursor.getInt(cursor.getColumnIndex(SYNC_STATUS));
                        String floortosheetby = cursor.getString(cursor.getColumnIndex(Constant.FLOORTOSHEETBY));
                        String remark = cursor.getString(cursor.getColumnIndex(Constant.REMARK));
                        String is_uom = cursor.getString(cursor.getColumnIndex(Constant.IS_UOM));
                        String is_item_type = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_TYPE));
                        String is_item_no = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NO));
                        String is_item_name = cursor.getString(cursor.getColumnIndex(Constant.IS_ITEM_NAME));
                        String is_batch = cursor.getString(cursor.getColumnIndex(Constant.IS_BATCH));
                        String is_bun = cursor.getString(cursor.getColumnIndex(Constant.IS_BUN));
                        String is_eun = cursor.getString(cursor.getColumnIndex(Constant.IS_EUN));
                        String is_lot_no = cursor.getString(cursor.getColumnIndex(Constant.IS_LOT_NO));
                        String is_container_no = cursor.getString(cursor.getColumnIndex(Constant.IS_CONTAINER_NO));
                        String is_total_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_TOTAL_QUANTITY));
                        String is_make = cursor.getString(cursor.getColumnIndex(Constant.IS_MAKE));
                        String is_unrestricted_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_UNRESTRICTED_QUANTITY));
                        String is_value_unrestricted = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_UNRESTRICTED));
                        String is_blocked_quantity = cursor.getString(cursor.getColumnIndex(Constant.IS_BLOCKED_QUANTITY));
                        String is_value_blocked_stock = cursor.getString(cursor.getColumnIndex(Constant.IS_VALUE_BLOCKED_STOCK));
                        String is_rate = cursor.getString(cursor.getColumnIndex(Constant.IS_RATE));
                        String is_name = cursor.getString(cursor.getColumnIndex(Constant.IS_NAME));
                        String is_factory = cursor.getString(cursor.getColumnIndex(Constant.IS_FACTORY));
                        String is_warehouse = cursor.getString(cursor.getColumnIndex(Constant.IS_WAREHOUSE));
                        String is_whitem = cursor.getString(cursor.getColumnIndex(Constant.IS_WHITEM));
                        String is_stock_zone = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_ZONE));
                        String is_stock_location = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_LOCATION));
                        String is_df_store_location_level = cursor.getString(cursor.getColumnIndex(Constant.IS_DF_STORE_LOCATION_LEVEL));
                        String is_stock_segment = cursor.getString(cursor.getColumnIndex(Constant.IS_STOCK_SEGMENT));
                        String is_gl_account_code = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_CODE));
                        String is_gl_account_name = cursor.getString(cursor.getColumnIndex(Constant.IS_GL_ACCOUNT_NAME));
                        String is_currency = cursor.getString(cursor.getColumnIndex(Constant.IS_CURRENCY));
                        String is_material_group = cursor.getString(cursor.getColumnIndex(Constant.IS_MATERIAL_GROUP));
                        milestoneList.add(new Sample(m_id, customerId, clientId, milestoneId, assignUserId, bun, batch, blocked_quantity, container_no, df_store_location_level, eun, factory, item_name, item_No, item_Type, lot_no, make, name, stock_location, stock_segment, stock_zone, total_quantity, unrestricted_quantity, uoM, value_blocked_stock, value_unrestricted, warehouse, whitem, isqtysync, inputqty, extentedqty, shrinkqty, stockstatus, statuscode, assignuseremail, isfloortosheet, sync_status, floortosheetby, remark, is_uom, is_item_type, is_item_no, is_item_name, is_batch, is_bun, is_eun, is_lot_no, is_container_no, is_total_quantity, is_make, is_unrestricted_quantity, is_value_unrestricted, is_blocked_quantity, is_value_blocked_stock, is_rate, is_name, is_factory, is_warehouse, is_whitem, is_stock_zone, is_stock_location, is_df_store_location_level, is_stock_segment, is_gl_account_code, is_gl_account_name, is_currency, is_material_group));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public int getSumTotalQValue(int mid) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            String sumQuery = "select SUM(" + TOTAL_QUANTITY + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid;
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }

    public int getSumTotalQuaValue(int mid) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            String sumQuery = "select count(" + M_ID + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid;
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }
    public int getSumTotalUNValue(int mid) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            String sumQuery = "select SUM(" + VALUE_UNRESTRICTED + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid;
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }
    public int getSumTotalUQValue(int mid, String stockStatus) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String sumQuery = "select SUM(" + TOTAL_QUANTITY + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid + " and " + STOCKSTATUS + " = '" + stockStatus + "'";
            //String sumQuery = String.format("SELECT SUM(" + TOTAL_QUANTITY + ") as Total FROM " + TABLE_MILESTONEDETAILSALL+" where "+STOCKSTATUS+"=W");
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }
    public int getSumTotalUQGValue(int mid, String stockStatus) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String sumQuery = "select count(" + STOCKSTATUS + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid + " and " + STOCKSTATUS + " = '" + stockStatus + "'";
            //String sumQuery = String.format("SELECT SUM(" + TOTAL_QUANTITY + ") as Total FROM " + TABLE_MILESTONEDETAILSALL+" where "+STOCKSTATUS+"=W");
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }
    public int getSumTotalUQUaValue(int mid, String stockStatus) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String sumQuery = "select count(" + STOCKSTATUS + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid + " and " + STOCKSTATUS + " = '" + stockStatus + "'";
            //String sumQuery = String.format("SELECT SUM(" + TOTAL_QUANTITY + ") as Total FROM " + TABLE_MILESTONEDETAILSALL+" where "+STOCKSTATUS+"=W");
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }

    public int getSumTotaEQValue(int mid) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            String sumQuery = "select SUM(" + EXTENTEDQTY + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid;
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }

    public int getSumTotaSQValue(int mid) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            String sumQuery = "select SUM(" + SHRINKQTY + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + "= " + mid;
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }

    public long insertTakeOffPersonDetails(PersonDetails student) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.MILESTONID, student.getMileStonId());
        contentValues.put(Constant.FULLNAME, student.getFullName());
        contentValues.put(Constant.DESIGNATION, student.getDesignation());
        contentValues.put(Constant.EMAIL, student.getEmail());
        contentValues.put(Constant.CONTACTNO, student.getContactNo());
        contentValues.put(Constant.CREATEDBY, student.getCreatedBy());
        contentValues.put(SYNC_STATUS, 0);
        contentValues.put(Constant.CREATE_AT, dateToStr);
        try {
            id = sqLiteDatabase.insertOrThrow(Constant.TABLE_TAKESIGNOFFPERSONALDETAILS, null, contentValues);
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<TakeSignOffDAO> getTakeOffSignPersion() {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Constant.TABLE_TAKESIGNOFFPERSONALDETAILS, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<TakeSignOffDAO> milestoneList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(ID));
                        int milestoneid = cursor.getInt(cursor.getColumnIndex(Constant.MILESTONID));
                        String fullName = cursor.getString(cursor.getColumnIndex(Constant.FULLNAME));
                        String designation = cursor.getString(cursor.getColumnIndex(Constant.DESIGNATION));
                        String email = cursor.getString(cursor.getColumnIndex(Constant.EMAIL));
                        String contactno = cursor.getString(cursor.getColumnIndex(Constant.CONTACTNO));
                        //  String todate = cursor.getString(cursor.getColumnIndex(Constant.TODATE));
                        //   String milestonestatus = cursor.getString(cursor.getColumnIndex(Constant.MILESTONESTATUS));
                        milestoneList.add(new TakeSignOffDAO(id, milestoneid, fullName, designation, email, contactno));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public long UpdateStockQuantity(int Q, int mid, int sh, int ex, int ss, String fisync, int reset) {

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.INPUTQTY, Q);
        contentValues.put(Constant.STOCKSTATUS, "W");
        contentValues.put(ISQTYSYNC, fisync);
        contentValues.put(SYNC_STATUS, ss);
        contentValues.put(RESET_STATUS, reset);
        contentValues.put(SHRINKQTY, -sh);
        contentValues.put(EXTENTEDQTY, ex);
        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_MILESTONEDETAILSALL, contentValues, M_ID + " = ? ", new String[]{String.valueOf(mid)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    //syncing data
    public Cursor getUnsyncedMilestoneUpdate() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        //where " + MILE_STONEID + "= " + mid + " and " + STOCKSTATUS + " = '" + stockStatus + "'";
        String sql = "SELECT * FROM " + Constant.TABLE_MILESTONEDETAILSALL + " WHERE " + SYNC_STATUS + " = 0 " + " and " + IU_SYNC_STATUS + " = 0 ";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        return c;
    }

    public long UpdateSyncStatusQuantity(int mid) {
        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 1);

        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_MILESTONEDETAILSALL, contentValues, M_ID + " = ? and " + IU_SYNC_STATUS + " =?", new String[]{String.valueOf(mid), String.valueOf(0)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public Cursor getSyncUserPerDeaUpdate() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constant.TABLE_PERSONALDETAILS + " WHERE " + SYNC_STATUS + " = 0";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        return c;
    }

    public long UpdateSyncUserPerDeaUpdate(int mid) {
        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 1);

        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_PERSONALDETAILS, contentValues, Constant.MILESTONID + " = ? ", new String[]{String.valueOf(mid)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public Cursor getSyncUserSOPerDeaUpdate() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constant.TABLE_TAKESIGNOFFPERSONALDETAILS + " WHERE " + SYNC_STATUS + " = 0";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        return c;
    }

    public long UpdateSyncUserSOPerDeaUpdate(int mid) {
        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 1);

        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_TAKESIGNOFFPERSONALDETAILS, contentValues, Constant.MILESTONID + " = ? ", new String[]{String.valueOf(mid)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long insertTakeImageDetails(PersonDetails student) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMILESTONEID, student.getMileStonId());
        contentValues.put(STOCKID, student.getFullName());
        contentValues.put(Constant.IMAGENAME, student.getDesignation());
        contentValues.put(SYNC_STATUS, 0);
        contentValues.put(Constant.CREATE_AT, dateToStr);
        try {
            id = sqLiteDatabase.insertOrThrow(Constant.TABLE_IMAGESDETAILS, null, contentValues);
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public long UpdateUpdateSyncOfflineStatus(int mid) {

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 1);

        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_MILESTONE, contentValues, Constant.MILESTONEID + " = ? ", new String[]{String.valueOf(mid)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public int getMaxNo() {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String sumQuery = "select Max(" + M_ID + ") as Total from " + TABLE_MILESTONEDETAILSALL;
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            // sqLiteDatabase.close();
        }
        return sum;
    }

    public long UpdateFtoSheet(int mid, String f) {

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 0);
        contentValues.put(Constant.ISFLOORTOSHEET, f);
        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_MILESTONEDETAILSALL, contentValues, M_ID + " = ? ", new String[]{String.valueOf(mid)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long UpdateRemark(int mid, String f) {

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 0);
        contentValues.put(Constant.REMARK, f);
        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_MILESTONEDETAILSALL, contentValues, M_ID + " = ? ", new String[]{String.valueOf(mid)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public Cursor getUnsyncedFtoSheetUpdate() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constant.TABLE_MILESTONEDETAILSALL + " WHERE " + Constant.RESET_STATUS + " = 1";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        return c;
    }

    public long UpdateSyncStatusFtoSheet(int mid) {
        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 2);
        contentValues.put(Constant.RESET_STATUS, 0);
        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_MILESTONEDETAILSALL, contentValues, M_ID + " = ? ", new String[]{String.valueOf(mid)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public Cursor getUnsyncedFtoSheetInsert() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constant.TABLE_MILESTONEDETAILSALL + " WHERE " + Constant.IU_SYNC_STATUS + " = 1";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        return c;
    }

    public long UpdateSyncStatusFtoSheetInsert(int mid, int sid) {
        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.M_ID, sid);
        contentValues.put(SYNC_STATUS, 1);
        contentValues.put(Constant.IU_SYNC_STATUS, 0);
        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_MILESTONEDETAILSALL, contentValues, ID + " = ? and " + IU_SYNC_STATUS + " =?", new String[]{String.valueOf(mid), String.valueOf(1)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long insertImagesDetails(FilesModel student) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMILESTONEID, student.getM_id());
        contentValues.put(STOCKID, student.getStock_id());
        contentValues.put(Constant.IMAGENAME, student.getFile_name());
        contentValues.put(Constant.IMAGEURI, student.getFile_path());
        contentValues.put(SYNC_STATUS, 0);
        contentValues.put(Constant.CREATE_AT, dateToStr);
        try {
            id = sqLiteDatabase.insertOrThrow(Constant.TABLE_IMAGESDETAILS, null, contentValues);
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<FilesModel> getAllImages(int sid, int mid) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Constant.TABLE_IMAGESDETAILS, null, IMILESTONEID + " =? and " + STOCKID + " =?", new String[]{String.valueOf(mid), String.valueOf(sid)}, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<FilesModel> milestoneList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(ID));
                        int imilestoneid = cursor.getInt(cursor.getColumnIndex(IMILESTONEID));
                        String imagename = cursor.getString(cursor.getColumnIndex(Constant.IMAGENAME));
                        String imageuri = cursor.getString(cursor.getColumnIndex(Constant.IMAGEURI));
                        milestoneList.add(new FilesModel(id, imilestoneid, imagename, imageuri));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Cursor getUnsyncedImagesInsert() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constant.TABLE_IMAGESDETAILS + " WHERE " + Constant.SYNC_STATUS + " = 0";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        return c;
    }

    public long UpdateSyncStatusInsertImages(int id) {
        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 1);
        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_IMAGESDETAILS, contentValues, ID + " = ? ", new String[]{String.valueOf(id)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public List<LocationNameDAO> getAllLocationStock(int mid) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_STOCKLOCATION, null, IMILESTONEID + " =?", new String[]{String.valueOf(mid)}, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<LocationNameDAO> milestoneList = new ArrayList<>();
                    do {

                        String locationname = cursor.getString(cursor.getColumnIndex(Constant.LOCATIONNAME));
                        milestoneList.add(new LocationNameDAO("", locationname));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public long insertStockLocationDetails(LocationListDAO student) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.MILE_STONEID, student.getMileStoneId());
        contentValues.put(Constant.STOCKID, student.getStockId());
        contentValues.put(QUANTITY, student.getQuantity());
        contentValues.put(Constant.LOCATION, student.getLocation());
        contentValues.put(SYNC_STATUS, 0);
        contentValues.put(Constant.CREATE_AT, dateToStr);
        try {
            id = sqLiteDatabase.insertOrThrow(TABLE_UPDATESTOCKLOCATION, null, contentValues);
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<LocationListDAO> getAllLocationListStock(int mid) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_UPDATESTOCKLOCATION, null, STOCKID + " =?", new String[]{String.valueOf(mid)}, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<LocationListDAO> milestoneList = new ArrayList<>();
                    do {
                        int quntity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                        int mile_stoneid = cursor.getInt(cursor.getColumnIndex(Constant.MILE_STONEID));
                        int stockid = cursor.getInt(cursor.getColumnIndex(Constant.STOCKID));
                        String location = cursor.getString(cursor.getColumnIndex(Constant.LOCATION));
                        milestoneList.add(new LocationListDAO(1, quntity, mile_stoneid, stockid, location));
                    } while (cursor.moveToNext());

                    return milestoneList;
                }
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public int getSumTotalLQValue(int mid, int stockId) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String sumQuery = "select SUM(" + QUANTITY + ") as Total from " + TABLE_UPDATESTOCKLOCATION + " where " + MILE_STONEID + "= " + mid + " and " + STOCKID + " = " + stockId + "";
            //String sumQuery = String.format("SELECT SUM(" + TOTAL_QUANTITY + ") as Total FROM " + TABLE_MILESTONEDETAILSALL+" where "+STOCKSTATUS+"=W");
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }

    public Cursor getSyncStockLocation() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constant.TABLE_UPDATESTOCKLOCATION + " WHERE " + SYNC_STATUS + " = 0";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        return c;
    }

    public long UpdateSyncStockLocation(int mid) {
        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, 1);

        try {
            rowCount = sqLiteDatabase.update(Constant.TABLE_UPDATESTOCKLOCATION, contentValues, Constant.ID + " = ? ", new String[]{String.valueOf(mid)});
        } catch (SQLiteException e) {
            // Logger.d("Exception: " + e.getMessage());
            // Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public Cursor getSerachBatches(String search, int m_id) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String sql;
        sql = "SELECT item_Name,item_No FROM " + TABLE_MILESTONEDETAILSALL + " where " + MILE_STONEID + " = " + m_id + "" + " and " + ITEM_NAME + " like '" + search + "%'";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        return c;
    }

    public int checkData(String itemname, String location) {
        int sum = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String sumQuery = "select count(" + M_ID + ") as Total from " + TABLE_MILESTONEDETAILSALL + " where " + ITEM_NAME + " = '" + itemname + "'" + " and " + STOCK_LOCATION + " = '" + location + "'";
            cursor = sqLiteDatabase.rawQuery(sumQuery, null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
        } catch (Exception e) {
            //Logger.d("Exception: " + e.getMessage());
            //  Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return sum;
    }
}
