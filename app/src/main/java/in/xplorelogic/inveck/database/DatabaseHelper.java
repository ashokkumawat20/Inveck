package in.xplorelogic.inveck.database;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import in.xplorelogic.inveck.utils.MyApp;

import static in.xplorelogic.inveck.utils.Constant.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inveckdb.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper databaseHelper;

    private DatabaseHelper() {
        super(MyApp.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance() {

        if (databaseHelper == null) {
            synchronized (DatabaseHelper.class) { //thread safe singleton
                if (databaseHelper == null)
                    databaseHelper = new DatabaseHelper();
            }
        }

        return databaseHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MILESTONE_TABLE = "CREATE TABLE " + TABLE_MILESTONE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MILESTONEID + " INTEGER NOT NULL UNIQUE, "
                + CUSTID + " INTEGER, "
                + CUSTNAME + " TEXT, " //nullable
                + FROMDATE + " TEXT, " //nullable
                + INCHARGESTATUS + " TEXT, " //nullable
                + MILESTONENAME + " TEXT, " //nullable
                + TODATE + " TEXT, " //nullable
                + MILESTONESTATUS + " TEXT, " //nullable
                + SYNC_STATUS + " INTEGER, "
                + CREATE_AT + " TEXT " //nullable
                + ")";

        String CREATE_PERSONALDETAILS_TABLE = "CREATE TABLE " + TABLE_PERSONALDETAILS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MILESTONID + " INTEGER NOT NULL UNIQUE, "
                + CREATEDBY + " INTEGER, "
                + FULLNAME + " TEXT, " //nullable
                + DESIGNATION + " TEXT, " //nullable
                + EMAIL + " TEXT, " //nullable
                + CONTACTNO + " TEXT, " //nullable
                + SYNC_STATUS + " INTEGER, "
                + CREATE_AT + " TEXT " //nullable
                + ")";
        String CREATE_MILESTONEDETAILSALL_TABLE = "CREATE TABLE " + TABLE_MILESTONEDETAILSALL + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + M_ID + " INTEGER NOT NULL UNIQUE, "
                + CUSTOMERID + " INTEGER, "
                + CLIENTID + " INTEGER, "
                + MILE_STONEID + " INTEGER, "
                + ASSIGNUSERID + " INTEGER, "
                + UOM + " TEXT, " //nullable
                + ITEM_TYPE + " TEXT, " //nullable
                + ITEM_NO + " TEXT, " //nullable
                + ITEM_NAME + " TEXT, " //nullable
                + BATCH + " TEXT, " //nullable
                + BUN + " TEXT, " //nullable
                + EUN + " TEXT, " //nullable
                + LOT_NO + " TEXT, " //nullable
                + CONTAINER_NO + " TEXT, " //nullable
                + TOTAL_QUANTITY + " REAL, "
                + MAKE + " TEXT, " //nullable
                + UNRESTRICTED_QUANTITY + " REAL, "
                + VALUE_UNRESTRICTED + " REAL, "
                + BLOCKED_QUANTITY + " REAL, "
                + VALUE_BLOCKED_STOCK + " REAL, "
                + NAME + " TEXT, " //nullable
                + FACTORY + " TEXT, " //nullable
                + WAREHOUSE + " TEXT, " //nullable
                + WHITEM + " TEXT, " //nullable
                + STOCK_ZONE + " TEXT, " //nullable
                + STOCK_LOCATION + " TEXT, " //nullable
                + DF_STORE_LOCATION_LEVEL + " TEXT, " //nullable
                + STOCK_SEGMENT + " TEXT, " //nullable
                + ASSIGNUSEREMAIL + " TEXT, " //nullable
                + STATUSCODE + " TEXT, " //nullable
                + INPUTQTY + " REAL, "
                + EXTENTEDQTY + " REAL, "
                + SHRINKQTY + " REAL, "
                + ISQTYSYNC + " TEXT, " //nullable
                + STOCKSTATUS + " TEXT, " //nullable
                + ISFLOORTOSHEET + " TEXT, " //nullable
                + MILESTONE_FILE_ID + " INTEGER, "
                + FLOORTOSHEETDATE + " TEXT, " //nullable
                + FLOORTOSHEETBY + " TEXT, " //nullable
                + ISOUTOFSCOPE + " TEXT, " //nullable
                + REMARK + " TEXT, " //nullable
                + SYNC_STATUS + " INTEGER, "
                + IU_SYNC_STATUS + " INTEGER, "
                + RESET_STATUS + " INTEGER, "
                + IS_UOM + " TEXT, " //nullable
                + IS_ITEM_TYPE + " TEXT, " //nullable
                + IS_ITEM_NO + " TEXT, " //nullable
                + IS_ITEM_NAME + " TEXT, " //nullable
                + IS_BATCH + " TEXT, " //nullable
                + IS_BUN + " TEXT, " //nullable
                + IS_EUN + " TEXT, " //nullable
                + IS_LOT_NO + " TEXT, " //nullable
                + IS_CONTAINER_NO + " TEXT, " //nullable
                + IS_TOTAL_QUANTITY + " TEXT, " //nullable
                + IS_MAKE + " TEXT, " //nullable
                + IS_UNRESTRICTED_QUANTITY + " TEXT, " //nullable
                + IS_VALUE_UNRESTRICTED + " TEXT, " //nullable
                + IS_BLOCKED_QUANTITY + " TEXT, " //nullable
                + IS_VALUE_BLOCKED_STOCK + " TEXT, " //nullable
                + IS_RATE + " TEXT, " //nullable
                + IS_NAME + " TEXT, " //nullable
                + IS_FACTORY + " TEXT, " //nullable
                + IS_WAREHOUSE + " TEXT, " //nullable
                + IS_WHITEM + " TEXT, " //nullable
                + IS_STOCK_ZONE + " TEXT, " //nullable
                + IS_STOCK_LOCATION + " TEXT, " //nullable
                + IS_DF_STORE_LOCATION_LEVEL + " TEXT, " //nullable
                + IS_STOCK_SEGMENT + " TEXT, " //nullable
                + IS_GL_ACCOUNT_CODE + " TEXT, " //nullable
                + IS_GL_ACCOUNT_NAME + " TEXT, " //nullable
                + IS_CURRENCY + " TEXT, " //nullable
                + IS_MATERIAL_GROUP + " TEXT, " //nullable
                + CREATE_AT + " TEXT " //nullable
                + ")";
        String CREATE_TAKESIGNOFFPERSONALDETAILS = "CREATE TABLE " + TABLE_TAKESIGNOFFPERSONALDETAILS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MILESTONID + " INTEGER NOT NULL UNIQUE, "
                + CREATEDBY + " INTEGER, "
                + FULLNAME + " TEXT, " //nullable
                + DESIGNATION + " TEXT, " //nullable
                + EMAIL + " TEXT, " //nullable
                + CONTACTNO + " TEXT, " //nullable
                + SYNC_STATUS + " INTEGER, "
                + CREATE_AT + " TEXT " //nullable
                + ")";
        String CREATE_IMAGESDETAILS_TABLE = "CREATE TABLE " + TABLE_IMAGESDETAILS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMILESTONEID + " INTEGER , "
                + STOCKID + " INTEGER, "
                + IMAGENAME + " TEXT, " //nullable
                + IMAGEURI + " TEXT, " //nullable
                + SYNC_STATUS + " INTEGER, "
                + CREATE_AT + " TEXT " //nullable
                + ")";

        String CREATE_STOCKLOCATION_TABLE = "CREATE TABLE " + TABLE_STOCKLOCATION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMILESTONEID + " INTEGER , "
                + LOCATIONNAME + " TEXT, " //nullable
                + SYNC_STATUS + " INTEGER, "
                + CREATE_AT + " TEXT, " //nullable
                + "UNIQUE(" + IMILESTONEID + "," + LOCATIONNAME + ") ON CONFLICT REPLACE"
                + ")";

        String CREATE_UPDATESTOCKLOCATION_TABLE = "CREATE TABLE " + TABLE_UPDATESTOCKLOCATION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MILE_STONEID + " INTEGER , "
                + STOCKID + " INTEGER, "
                + QUANTITY + " INTEGER, " //nullable
                + LOCATION + " TEXT, " //nullable
                + SYNC_STATUS + " INTEGER, "
                + CREATE_AT + " TEXT " //nullable
                + ")";
        db.execSQL(CREATE_MILESTONE_TABLE);
        db.execSQL(CREATE_PERSONALDETAILS_TABLE);
        db.execSQL(CREATE_MILESTONEDETAILSALL_TABLE);
        db.execSQL(CREATE_TAKESIGNOFFPERSONALDETAILS);
        db.execSQL(CREATE_IMAGESDETAILS_TABLE);
        db.execSQL(CREATE_STOCKLOCATION_TABLE);
        db.execSQL(CREATE_UPDATESTOCKLOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MILESTONE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONALDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MILESTONEDETAILSALL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAKESIGNOFFPERSONALDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGESDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKLOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPDATESTOCKLOCATION);
        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
