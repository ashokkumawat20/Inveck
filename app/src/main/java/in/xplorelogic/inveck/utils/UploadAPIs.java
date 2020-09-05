package in.xplorelogic.inveck.utils;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UploadAPIs {

    @Multipart
    @POST("MileStone/UpdateStockFile/{user}")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody, @Path("user") String user);

    @Multipart
    @POST("MileStone/UpdateStockMultipleFile/{user}")
    Call<ResponseBody> uploadMultipleFilesDynamic(@Part List<MultipartBody.Part> files,@Path("user") String user);

    @POST("MileStone/DeleteStockFile/{id}")
    Call<ResponseBody> deleteFile(@Path("id") int id);

    @POST("MileStone/StockLocation/{id}")
    Call<ResponseBody> getLocationList(@Path("id") int id);
}

