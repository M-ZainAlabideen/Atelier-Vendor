package app.atelier.vendor.webService;

import app.atelier.vendor.classes.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadAPiConfig {
     public static Retrofit retrofit = null;

        public static Retrofit getClient() {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL+ "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            return retrofit;

        }
    }

