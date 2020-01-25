package app.atelier.vendor.webService;

import app.atelier.vendor.webService.response.LoginResponse;
import app.atelier.vendor.webService.response.OrdersResponse;
import app.atelier.vendor.webService.response.VendorStatusResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("customers/login")
    Call<LoginResponse> LOGIN_RESPONSE_CALL(@Query("UsernameOrEmail") String UsernameOrEmail,
                                            @Query("Password") String Password);


    @FormUrlEncoded
    @POST("customers/recoverypassword")
    Call<ResponseBody> FORGET_PASSWORD_RESPONSE_CALL(@Field("email") String email);

    @GET("vendororders")
    Call<OrdersResponse> ORDERS_RESPONSE_CALL(@Query("vendor_id") String vendorId,
                                              @Query("page") int page,
                                              @Query("limit") int limit,
                                              @Query("order_number") String orderNumber,
                                              @Query("vendor_status_id") String vendorStatusId,
                                              @Query("from_date") String fromDate,
                                              @Query("to_date") String toDate);

    @GET("orders/{id}")
    Call<OrdersResponse> ORDERS_PRODUCTS_RESPONSE_CALL(@Path("id") int id);

    @GET("vendorStatus")
    Call<VendorStatusResponse> VENDOR_STATUS_CALL();

    @GET("orders/VendorOrderStatus")
    Call<ResponseBody> CHANGE_ORDER_STATUS(
            @Query("orderId") int orderId,
            @Query("statusId") int statusId);

    @POST("customers/devicetoken")
    Call<ResponseBody> INSERT_TOKEN(@Query("token") String token,
                     @Query("device_type") String deviceType,
                     @Query("device_id") String deviceId,
                     @Query("customer_id") String userId);
}
