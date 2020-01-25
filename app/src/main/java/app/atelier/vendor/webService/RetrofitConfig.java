package app.atelier.vendor.webService;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.atelier.vendor.classes.Constants;
import app.atelier.vendor.classes.SessionManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static final Map<String, RetrofitService> mService = new HashMap<>();
    private SessionManager sessionManager;
    private RetrofitConfig(Context context) {
        sessionManager = new SessionManager(context);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader(Constants.AUTHORIZATION, Constants.AUTHORIZATION_VALUE)
                                .addHeader(Constants.ACCEPT_LANGUAGE,sessionManager.getLanguage())
                                .addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE)
                                .build();
                        return chain.proceed(request);
                    }
                }).writeTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService.put(Constants.BASE_URL, retrofit.create(RetrofitService.class));
    }

    public static RetrofitService getServices(Context context) {
        if (mService.get(Constants.BASE_URL) == null) {
            new RetrofitConfig(context);
        }
        return mService.get(Constants.BASE_URL);
    }
}
