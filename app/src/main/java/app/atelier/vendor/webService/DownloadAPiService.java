package app.atelier.vendor.webService;

import app.atelier.vendor.classes.Constants;
import app.atelier.vendor.webService.response.VendorStatusResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadAPiService {
    @Streaming
    @GET
    Call<ResponseBody> DOWNLOAD_FILE_CALL(
            @Header(Constants.AUTHORIZATION) String authorization,
            @Header(Constants.ACCEPT_LANGUAGE) String language,
            @Header(Constants.CONTENT_TYPE) String content_type,
            @Url String fileUrl);
}
