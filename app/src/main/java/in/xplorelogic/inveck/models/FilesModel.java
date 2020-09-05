package in.xplorelogic.inveck.models;

import android.graphics.Bitmap;

public class FilesModel {
    int id;
    int stock_id;
    int m_id;
    String file_name="";
    String file_path="";
    Bitmap bitmap;
    public FilesModel()
    {}

    public FilesModel(int id, String file_name, String file_path, Bitmap bitmap) {
        this.id = id;
        this.file_name = file_name;
        this.file_path = file_path;
        this.bitmap = bitmap;
    }

    public FilesModel(int stock_id, int m_id, String file_name, String file_path) {
        this.stock_id = stock_id;
        this.m_id = m_id;
        this.file_name = file_name;
        this.file_path = file_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }
}
