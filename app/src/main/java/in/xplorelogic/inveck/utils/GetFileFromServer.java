package in.xplorelogic.inveck.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class GetFileFromServer {


    public static Bitmap goforIt(String file_name, String file_path) {
        FTPClient con = null;
        Bitmap myBitmap = null;
        try {
            con = new FTPClient();
            con.connect("67.23.166.125");

            if (con.login("inveckftp", "h1Ybp%49")) {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);
               // String data = "/sdcard/" + file_name;

              //  OutputStream out = new FileOutputStream(new File(data));
                //   boolean result = con.retrieveFile("/UploadFiles/1590140798347.jpg", out);
                InputStream input = con.retrieveFileStream(file_path);
                myBitmap = BitmapFactory.decodeStream(input);
              //  out.close();
                //  if (result) Log.v("download result", "succeeded");
                con.logout();
                con.disconnect();
            }
        } catch (Exception e) {
            Log.v("download result", "failed");
            e.printStackTrace();
        }

        return myBitmap;

    }
}
