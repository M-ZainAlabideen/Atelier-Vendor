package app.atelier.vendor.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

import app.atelier.vendor.R;
import app.atelier.vendor.adapters.OrdersAdapter;
import app.atelier.vendor.classes.FixControl;
import app.atelier.vendor.classes.SessionManager;
import app.atelier.vendor.views.activities.MainActivity;
import app.atelier.vendor.webService.RetrofitConfig;
import app.atelier.vendor.webService.response.OrdersResponse;
import app.atelier.vendor.webService.response.VendorStatusResponse;
import app.atelier.vendor.webService.webModels.Order;
import app.atelier.vendor.webService.webModels.Status;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {
    public static FragmentActivity activity;
    public static OrdersFragment fragment;

    @BindView(R.id.fragment_orders_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_orders_tv_total)
    TextView total;
    @BindView(R.id.fragment_orders_tv_fromDate)
    TextView fromDate;
    @BindView(R.id.fragment_orders_tv_toDate)
    TextView toDate;
    @BindView(R.id.fragment_orders_rv_orders)
    RecyclerView orders;
    @BindView(R.id.loading)
    ProgressBar loading;

    SessionManager sessionManager;

    ArrayList<Order> ordersList = new ArrayList<>();
    OrdersAdapter ordersAdapter;
    LinearLayoutManager layoutManager;

    ArrayList<Status> statusList = new ArrayList<>();
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int pageIndex = 1;
    private final int limit = 10;

    private Integer statusSelectedId = null;
    private int currentStatusId = -1;
    private String vendorId;
    private String fromDateStr;
    private String toDateStr;
    private String orderNumber;
    private boolean isStartTime;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private int orderPosition;
    private boolean check;

    public static OrdersFragment newInstance(FragmentActivity activity) {
        fragment = new OrdersFragment();
        OrdersFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.appbar.setVisibility(View.VISIBLE);
        MainActivity.filter.setVisibility(View.VISIBLE);
        MainActivity.search.setVisibility(View.VISIBLE);
        MainActivity.back.setVisibility(View.INVISIBLE);
        MainActivity.all.setVisibility(View.VISIBLE);
        MainActivity.title.setText(getString(R.string.atelier));
        container.setVisibility(View.GONE);
        sessionManager = new SessionManager(activity);
        vendorId = sessionManager.getVendorId();
        ordersAdapter = new OrdersAdapter(activity, ordersList, new OrdersAdapter.onItemClickListener() {
            @Override
            public void changeStatusClick(int position) {
                currentStatusId = ordersList.get(position).vendorStatusId;
                orderPosition = position;
                vendorStatusApi(false);
            }
        });
        layoutManager = new LinearLayoutManager(activity);
        orders.setLayoutManager(layoutManager);
        orders.setAdapter(ordersAdapter);
        if (ordersList.size() == 0) {
            if(!check) {
                getOrdersApi(null,null , null, null);
                check = true;
            }
        } else {
            loading.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }

        MainActivity.search.setQueryHint(getString(R.string.searchByOrderNum));
        MainActivity.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MainActivity.search.onActionViewCollapsed();
                ordersList.clear();
                ordersAdapter.notifyDataSetChanged();
                pageIndex = 1;
                orderNumber = query;
                getOrdersApi(orderNumber, String.valueOf(statusSelectedId), fromDateStr, toDateStr);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        MainActivity.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorStatusApi(true);
            }
        });


        MainActivity.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersList.clear();
                ordersAdapter.notifyDataSetChanged();
                pageIndex = 1;
                getOrdersApi(null,null, null, null);
            }
        });

        //open calendar to select birth date
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate(dayOfMonth, monthOfYear + 1, year, isStartTime);
            }

        };
    }

    @OnClick(R.id.fragment_orders_tv_fromDate)
    public void pickStartDate() {
        new DatePickerDialog(activity, R.style.DialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        isStartTime = true;
    }

    @OnClick(R.id.fragment_orders_tv_toDate)
    public void pickEndDate() {
        new DatePickerDialog(activity, R.style.DialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        isStartTime = false;
    }

    @OnClick(R.id.fragment_orders_iv_dateFilter)
    public void dateFilterClick() {
         fromDateStr = fromDate.getText().toString();
         toDateStr = toDate.getText().toString();
        if (fromDateStr.equals("yyyy-mm-dd")) {
            Snackbar.make(loading, getString(R.string.selectStartDate), Snackbar.LENGTH_SHORT).show();
        } else if (toDateStr.equals("yyyy-mm-dd")) {
            Snackbar.make(loading, getString(R.string.selectEndDate), Snackbar.LENGTH_SHORT).show();
        } else {
            ordersList.clear();
            ordersAdapter.notifyDataSetChanged();
            pageIndex = 1;
            getOrdersApi(orderNumber, String.valueOf(statusSelectedId), fromDateStr, toDateStr);
        }
    }

    private void updateDate(int day, int month, int year, boolean isStartTime) {
        if (isStartTime) {
            fromDate.setText(year + "-" + month + "-" + day);
        } else {
            toDate.setText(year + "-" + month + "-" + day);
        }
    }

    public void filterPopUp(final ArrayList<Status> statusList, boolean isFilter) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View alertDialogView = ((Activity) activity).getLayoutInflater().inflate(R.layout.custom_filter, null);
        ConstraintLayout container = (ConstraintLayout) alertDialogView.findViewById(R.id.custom_filter_cl_container);
        final View delivered = (View) alertDialogView.findViewById(R.id.custom_filter_v_delivered);
        final View processing = (View) alertDialogView.findViewById(R.id.custom_filter_v_processing);
        final View ready = (View) alertDialogView.findViewById(R.id.custom_filter_v_ready);
        final View shipped = (View) alertDialogView.findViewById(R.id.custom_filter_v_shipped);
        View pending = (View) alertDialogView.findViewById(R.id.custom_filter_v_pending);
        //View all = (View) alertDialogView.findViewById(R.id.custom_filter_v_all);


        final TextView deliveredText = (TextView) alertDialogView.findViewById(R.id.custom_filter_tv_deliveredText);
        final TextView processingText = (TextView) alertDialogView.findViewById(R.id.custom_filter_tv_processingText);
        final TextView readyText = (TextView) alertDialogView.findViewById(R.id.custom_filter_tv_readyText);
        final TextView shippedText = (TextView) alertDialogView.findViewById(R.id.custom_filter_tv_shippedText);
        final TextView pendingText = (TextView) alertDialogView.findViewById(R.id.custom_filter_tv_pendingText);
        //final TextView allText = (TextView) alertDialogView.findViewById(R.id.custom_filter_tv_allText);


        final ImageView pendingBg = (ImageView) alertDialogView.findViewById(R.id.custom_filter_iv_pendingBg);
        final ImageView processingBg = (ImageView) alertDialogView.findViewById(R.id.custom_filter_iv_processingBg);
        final ImageView readyBg = (ImageView) alertDialogView.findViewById(R.id.custom_filter_iv_readyBg);
        final ImageView shippedBg = (ImageView) alertDialogView.findViewById(R.id.custom_filter_iv_shippedBg);
        final ImageView deliveredBg = (ImageView) alertDialogView.findViewById(R.id.custom_filter_iv_deliveredBg);
        //final ImageView allBg = (ImageView) alertDialogView.findViewById(R.id.custom_filter_iv_allBg);


        TextView confirm = (TextView) alertDialogView.findViewById(R.id.custom_filter_tv_confirm);
        TextView title = (TextView) alertDialogView.findViewById(R.id.custom_filter_tv_title);
        if (!isFilter) {
            title.setText(getString(R.string.changeStatus));
//            all.setVisibility(View.GONE);
//            allBg.setVisibility(View.GONE);
//            allText.setVisibility(View.GONE);
        }

        pendingText.setText(statusList.get(0).getStatus());
        processingText.setText(statusList.get(1).getStatus());
        readyText.setText(statusList.get(2).getStatus());
        shippedText.setText(statusList.get(3).getStatus());
        deliveredText.setText(statusList.get(4).getStatus());

        builder.setCancelable(true);
        builder.setView(alertDialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setGravity(Gravity.CENTER);

        if (!isFilter) {
            switch (currentStatusId) {
                case 10:
                    pendingBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_pending));
                    pendingText.setTextColor(Color.parseColor("#ffffff"));

                    processingBg.setBackgroundColor(0x00000000);
                    processingText.setTextColor(Color.parseColor("#D8A228"));

                    readyBg.setBackgroundColor(0x00000000);
                    readyText.setTextColor(Color.parseColor("#4FC12D"));

                    shippedBg.setBackgroundColor(0x00000000);
                    shippedText.setTextColor(Color.parseColor("#BB2CE6"));

                    deliveredBg.setBackgroundColor(0x00000000);
                    deliveredText.setTextColor(Color.parseColor("#007AFF"));
                    break;
                case 20:
                    processingBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_processing));
                    processingText.setTextColor(Color.parseColor("#ffffff"));

                    readyBg.setBackgroundColor(0x00000000);
                    readyText.setTextColor(Color.parseColor("#4FC12D"));

                    shippedBg.setBackgroundColor(0x00000000);
                    shippedText.setTextColor(Color.parseColor("#BB2CE6"));

                    deliveredBg.setBackgroundColor(0x00000000);
                    deliveredText.setTextColor(Color.parseColor("#007AFF"));

                    pendingBg.setBackgroundColor(0x00000000);
                    pendingText.setTextColor(Color.parseColor("#E62C2C"));
                    break;
                case 30:
                    readyBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_ready));
                    readyText.setTextColor(Color.parseColor("#ffffff"));

                    shippedBg.setBackgroundColor(0x00000000);
                    shippedText.setTextColor(Color.parseColor("#BB2CE6"));

                    deliveredBg.setBackgroundColor(0x00000000);
                    deliveredText.setTextColor(Color.parseColor("#007AFF"));

                    pendingBg.setBackgroundColor(0x00000000);
                    pendingText.setTextColor(Color.parseColor("#E62C2C"));

                    processingBg.setBackgroundColor(0x00000000);
                    processingText.setTextColor(Color.parseColor("#D8A228"));
                    break;
                case 40:
                    shippedBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_shipped));
                    shippedText.setTextColor(Color.parseColor("#ffffff"));

                    deliveredBg.setBackgroundColor(0x00000000);
                    deliveredText.setTextColor(Color.parseColor("#007AFF"));

                    pendingBg.setBackgroundColor(0x00000000);
                    pendingText.setTextColor(Color.parseColor("#E62C2C"));

                    processingBg.setBackgroundColor(0x00000000);
                    processingText.setTextColor(Color.parseColor("#D8A228"));

                    readyBg.setBackgroundColor(0x00000000);
                    readyText.setTextColor(Color.parseColor("#4FC12D"));
                    break;
                case 50:
                    deliveredBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_delivered));
                    deliveredText.setTextColor(Color.parseColor("#ffffff"));

                    pendingBg.setBackgroundColor(0x00000000);
                    pendingText.setTextColor(Color.parseColor("#E62C2C"));

                    processingBg.setBackgroundColor(0x00000000);
                    processingText.setTextColor(Color.parseColor("#D8A228"));

                    readyBg.setBackgroundColor(0x00000000);
                    readyText.setTextColor(Color.parseColor("#4FC12D"));

                    shippedBg.setBackgroundColor(0x00000000);
                    shippedText.setTextColor(Color.parseColor("#BB2CE6"));
                    break;
            }
        }
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_pending));
                pendingText.setTextColor(Color.parseColor("#ffffff"));
                if (isFilter) {
                    statusSelectedId = statusList.get(0).statusId;
                } else {
                    currentStatusId = statusList.get(0).statusId;
                }

                processingBg.setBackgroundColor(0x00000000);
                processingText.setTextColor(Color.parseColor("#D8A228"));

                readyBg.setBackgroundColor(0x00000000);
                readyText.setTextColor(Color.parseColor("#4FC12D"));

                shippedBg.setBackgroundColor(0x00000000);
                shippedText.setTextColor(Color.parseColor("#BB2CE6"));

                deliveredBg.setBackgroundColor(0x00000000);
                deliveredText.setTextColor(Color.parseColor("#007AFF"));

//                allBg.setBackgroundColor(0x00000000);
//                allText.setTextColor(Color.parseColor("#454C53"));
            }
        });

        processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processingBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_processing));
                processingText.setTextColor(Color.parseColor("#ffffff"));
                if (isFilter) {
                    statusSelectedId = statusList.get(1).statusId;
                } else {
                    currentStatusId = statusList.get(1).statusId;
                }


                readyBg.setBackgroundColor(0x00000000);
                readyText.setTextColor(Color.parseColor("#4FC12D"));

                shippedBg.setBackgroundColor(0x00000000);
                shippedText.setTextColor(Color.parseColor("#BB2CE6"));

                deliveredBg.setBackgroundColor(0x00000000);
                deliveredText.setTextColor(Color.parseColor("#007AFF"));

                pendingBg.setBackgroundColor(0x00000000);
                pendingText.setTextColor(Color.parseColor("#E62C2C"));

//                allBg.setBackgroundColor(0x00000000);
//                allText.setTextColor(Color.parseColor("#454C53"));
            }
        });

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_ready));
                readyText.setTextColor(Color.parseColor("#ffffff"));
                if (isFilter) {
                    statusSelectedId = statusList.get(2).statusId;
                } else {
                    currentStatusId = statusList.get(2).statusId;
                }

                shippedBg.setBackgroundColor(0x00000000);
                shippedText.setTextColor(Color.parseColor("#BB2CE6"));

                deliveredBg.setBackgroundColor(0x00000000);
                deliveredText.setTextColor(Color.parseColor("#007AFF"));

                pendingBg.setBackgroundColor(0x00000000);
                pendingText.setTextColor(Color.parseColor("#E62C2C"));

                processingBg.setBackgroundColor(0x00000000);
                processingText.setTextColor(Color.parseColor("#D8A228"));

//                allBg.setBackgroundColor(0x00000000);
//                allText.setTextColor(Color.parseColor("#454C53"));
            }
        });

        shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shippedBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_shipped));
                shippedText.setTextColor(Color.parseColor("#ffffff"));
                if (isFilter) {
                    statusSelectedId = statusList.get(3).statusId;
                } else {
                    currentStatusId = statusList.get(3).statusId;
                }

                deliveredBg.setBackgroundColor(0x00000000);
                deliveredText.setTextColor(Color.parseColor("#007AFF"));

                pendingBg.setBackgroundColor(0x00000000);
                pendingText.setTextColor(Color.parseColor("#E62C2C"));

                processingBg.setBackgroundColor(0x00000000);
                processingText.setTextColor(Color.parseColor("#D8A228"));

                readyBg.setBackgroundColor(0x00000000);
                readyText.setTextColor(Color.parseColor("#4FC12D"));

//                allBg.setBackgroundColor(0x00000000);
//                allText.setTextColor(Color.parseColor("#454C53"));
            }
        });

        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveredBg.setBackground(ContextCompat.getDrawable(activity, R.mipmap.bg_status_delivered));
                deliveredText.setTextColor(Color.parseColor("#ffffff"));
                if (isFilter) {
                    statusSelectedId = statusList.get(4).statusId;
                } else {
                    currentStatusId = statusList.get(4).statusId;
                }

                pendingBg.setBackgroundColor(0x00000000);
                pendingText.setTextColor(Color.parseColor("#E62C2C"));

                processingBg.setBackgroundColor(0x00000000);
                processingText.setTextColor(Color.parseColor("#D8A228"));

                readyBg.setBackgroundColor(0x00000000);
                readyText.setTextColor(Color.parseColor("#4FC12D"));

                shippedBg.setBackgroundColor(0x00000000);
                shippedText.setTextColor(Color.parseColor("#BB2CE6"));

//                allBg.setBackgroundColor(0x00000000);
//                allText.setTextColor(Color.parseColor("#454C53"));
            }
        });

//        all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                allBg.setBackground(ContextCompat.getDrawable(activity, R.color.darkGray));
//                allText.setTextColor(Color.parseColor("#ffffff"));
//                statusSelectedId = 60;
//
//
//                pendingBg.setBackgroundColor(0x00000000);
//                pendingText.setTextColor(Color.parseColor("#E62C2C"));
//
//                processingBg.setBackgroundColor(0x00000000);
//                processingText.setTextColor(Color.parseColor("#D8A228"));
//
//                readyBg.setBackgroundColor(0x00000000);
//                readyText.setTextColor(Color.parseColor("#4FC12D"));
//
//                shippedBg.setBackgroundColor(0x00000000);
//                shippedText.setTextColor(Color.parseColor("#BB2CE6"));
//
//                deliveredBg.setBackgroundColor(0x00000000);
//                deliveredText.setTextColor(Color.parseColor("#007AFF"));
//            }
//        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (isFilter) {
                    ordersList.clear();
                    ordersAdapter.notifyDataSetChanged();
                    pageIndex = 1;
                        getOrdersApi(null, String.valueOf(statusSelectedId), fromDateStr, toDateStr);
                } else {
                    changeOrderStatusApi(ordersList.get(orderPosition).id, currentStatusId);

                }

            }
        });

    }


    private void vendorStatusApi(boolean isFilter) {
        loading.setVisibility(View.VISIBLE);
        RetrofitConfig.getServices().VENDOR_STATUS_CALL().enqueue(new Callback<VendorStatusResponse>() {
            @Override
            public void onResponse(Call<VendorStatusResponse> call, Response<VendorStatusResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().vendorStatus != null && response.body().vendorStatus.size() > 0) {
                        statusList.addAll(response.body().vendorStatus);
                        filterPopUp(statusList, isFilter);
                    }
                }
            }

            @Override
            public void onFailure(Call<VendorStatusResponse> call, Throwable t) {

            }
        });
    }

    private void getOrdersApi(String searchOrderNum, String vendorStatusId, String fromDate, String toDate) {
        loading.setVisibility(View.VISIBLE);
        RetrofitConfig.getServices().ORDERS_RESPONSE_CALL(vendorId, pageIndex, limit, searchOrderNum, vendorStatusId, fromDate, toDate)
                .enqueue(new Callback<OrdersResponse>() {
                    @Override
                    public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                        loading.setVisibility(View.GONE);
                        container.setVisibility(View.VISIBLE);
                        if (response.code() == 200) {
                            if (response.body().orders != null && response.body().orders.size() > 0) {
                                ordersList.clear();
                                ordersList.addAll(response.body().orders);
                                ordersAdapter.notifyDataSetChanged();
                                total.setText(response.body().total.orderTotal + " " + getString(R.string.currency));
                                //add Scroll listener to the recycler , for pagination
                                orders.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (!isLastPage) {
                                            int visibleItemCount = layoutManager.getChildCount();

                                            int totalItemCount = layoutManager.getItemCount();

                                            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                                /*isLoading variable used for check if the user send many requests
                                for pagination(make many scrolls in the same time)
                                1- if isLoading true >> there is request already sent so,
                                no more requests till the response of last request coming
                                2- else >> send new request for load more data (News)*/
                                            if (!isLoading) {

                                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                    isLoading = true;

                                                    pageIndex = pageIndex + 1;

                                                    getMoreOrders(vendorId, limit, searchOrderNum, vendorStatusId, fromDate, toDate);

                                                }
                                            }
                                        }
                                    }
                                });
                            }

                        } else {
                            Snackbar.make(loading, getString(R.string.noOrders), Snackbar.LENGTH_SHORT).show();
                            //if the size of newsList equal 0 , it's mean no data and make lastPage true
                            isLastPage = true;
                        }

                    }

                    @Override
                    public void onFailure(Call<OrdersResponse> call, Throwable t) {

                    }
                });
    }

    private void getMoreOrders(String vendorId, int limit, String searchOrderNum, String vendorStatusId, String fromDate, String toDate) {
        RetrofitConfig.getServices().ORDERS_RESPONSE_CALL(vendorId, pageIndex, limit, searchOrderNum, vendorStatusId, fromDate, toDate)
                .enqueue(new Callback<OrdersResponse>() {
                    @Override
                    public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                        loading.setVisibility(View.GONE);
                        if (response.body() != null) {

                            if (response.body().orders.isEmpty()) {

                                isLastPage = true;
                                pageIndex = pageIndex - 1;
                            } else {
                                ordersList.addAll(response.body().orders);
                                ordersAdapter.notifyDataSetChanged();
                            }
                            isLoading = false;


                        }
                    }

                    @Override
                    public void onFailure(Call<OrdersResponse> call, Throwable t) {

                    }
                });
    }

    private void changeOrderStatusApi(int orderId, int statusId) {
        RetrofitConfig.getServices().CHANGE_ORDER_STATUS(orderId, statusId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    ordersList.get(orderPosition).vendorStatusId = Integer.valueOf(statusId);
                    ordersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}


