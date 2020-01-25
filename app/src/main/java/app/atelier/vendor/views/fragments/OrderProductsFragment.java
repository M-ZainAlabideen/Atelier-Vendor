package app.atelier.vendor.views.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import app.atelier.vendor.R;
import app.atelier.vendor.adapters.OrdersAdapter;
import app.atelier.vendor.adapters.ProductsAdapter;
import app.atelier.vendor.classes.Constants;
import app.atelier.vendor.classes.GlobalFunctions;
import app.atelier.vendor.classes.SessionManager;
import app.atelier.vendor.views.activities.MainActivity;
import app.atelier.vendor.webService.DownloadAPiConfig;
import app.atelier.vendor.webService.DownloadAPiService;
import app.atelier.vendor.webService.RetrofitConfig;
import app.atelier.vendor.webService.response.OrdersResponse;
import app.atelier.vendor.webService.webModels.Order;
import app.atelier.vendor.webService.webModels.Product;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderProductsFragment extends Fragment {
    public static FragmentActivity activity;
    public static OrderProductsFragment fragment;
    private SessionManager sessionManager;
    @BindView(R.id.fragment_order_products_rv_products)
    RecyclerView products;
    @BindView(R.id.loading)
    ProgressBar loading;

    private ArrayList<Product> ordersProductsList = new ArrayList<>();
    ProductsAdapter productsAdapter;
    LinearLayoutManager layoutManager;

    int orderId;
    Order order;

    public static OrderProductsFragment newInstance(FragmentActivity activity, int orderId) {
        fragment = new OrderProductsFragment();
        OrderProductsFragment.activity = activity;
        fragment.orderId = orderId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_order_products, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.appbar.setVisibility(View.VISIBLE);
        MainActivity.filter.setVisibility(View.GONE);
        MainActivity.search.setVisibility(View.GONE);
        MainActivity.back.setVisibility(View.VISIBLE);
        MainActivity.all.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        MainActivity.title.setText(getString(R.string.orderName) + " " + orderId);
        sessionManager = new SessionManager(activity);

        layoutManager = new LinearLayoutManager(activity);
        productsAdapter = new ProductsAdapter(activity,ordersProductsList, orderId);
        products.setLayoutManager(layoutManager);
        products.setAdapter(productsAdapter);

        if( ordersProductsList.size() == 0){
            orderProductsApi(orderId);
        }
    }


    @OnClick(R.id.fragment_order_products_btn_pdfInvoice)
    public void pdfInvoice() {
        {
                if (GlobalFunctions.isWriteExternalStorageAllowed(activity)) {

                    String filename = Calendar.getInstance().getTimeInMillis() + ".pdf";
                    String outPath = Environment.getExternalStorageDirectory()
                            + File.separator
                            + "Download"
                            + File.separator
                            + filename;
                    startDownload(Constants.DOWNLOAD_INVOICE.replace("{orderId}", String.valueOf(order.id)), outPath);

                    return;
                }

                if (!GlobalFunctions.isWriteExternalStorageAllowed(activity)) {

                    GlobalFunctions.requestWriteExternalStoragePermission(activity);

                }
            }

        }
    private void startDownload(String fileUrl, final String filePath) {
        loading.setVisibility(View.VISIBLE);

        DownloadAPiService downloadService = DownloadAPiConfig.getClient().create(DownloadAPiService.class);

        Call<ResponseBody> call = downloadService.DOWNLOAD_FILE_CALL(
                Constants.AUTHORIZATION_VALUE,
                sessionManager.getLanguage(),
                "application/x-www-form-urlencoded",
                fileUrl);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            loading.setVisibility(View.GONE);
                            File file = new File(filePath);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            Uri uri = FileProvider.getUriForFile
                                    (activity, "app.atelier.vendor.fileprovider", file);
                            target.setDataAndType(uri, "application/pdf");
                            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            Intent intent = Intent.createChooser(target, "Open File");
                            try {
                                activity.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {

                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), filePath);
                            return null;

                        }

                    }.execute();

                } else {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }

        });

    }
    private boolean writeResponseBodyToDisk(ResponseBody body, String filePath) {
        try {
            // todo change the file location/name according to your needs
            File f = new File(Environment.getExternalStorageDirectory(), File.separator + "Download");

            f.mkdirs();


            File futureStudioIconFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("startDownload 1 ->", "file download: " + fileSizeDownloaded + " of " + fileSize);

                    if (futureStudioIconFile.exists())
                        Log.d("startDownload 1 ->", "file path: " + futureStudioIconFile.getAbsolutePath());
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void orderProductsApi(int id){
        loading.setVisibility(View.VISIBLE);
        RetrofitConfig.getServices(activity).ORDERS_PRODUCTS_RESPONSE_CALL(id).enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                if(response.code() == 200){
                    ordersProductsList.clear();
                    ordersProductsList.addAll(response.body().orders.get(0).orderProducts);
                    productsAdapter.notifyDataSetChanged();
                    order = response.body().orders.get(0);
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<OrdersResponse> call, Throwable t) {

            }
        });

    }
}


