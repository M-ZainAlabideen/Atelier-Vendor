package app.atelier.vendor.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import app.atelier.vendor.R;
import app.atelier.vendor.classes.GlobalFunctions;
import app.atelier.vendor.classes.LocaleHelper;
import app.atelier.vendor.classes.Navigator;
import app.atelier.vendor.classes.SessionManager;
import app.atelier.vendor.views.fragments.LoginFragment;
import app.atelier.vendor.views.fragments.OrdersFragment;
import app.atelier.vendor.webService.RetrofitConfig;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.appcompat.widget.SearchView;
public class MainActivity extends AppCompatActivity {

    public static AppBarLayout appbar;
    public static Toolbar toolbar;
    public static TextView title;
    public static ImageView back;
    public static TextView all;
    public static SearchView search;
    public static ImageView filter;
    public static SessionManager sessionManager;
    public static boolean isEnglish;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GlobalFunctions.setDefaultLanguage(this);
        sessionManager = new SessionManager(this);

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.activity_main_tv_title);
        back = (ImageView) findViewById(R.id.activity_main_iv_back);
        all = (TextView) findViewById(R.id.activity_main_tv_all);
        search = (SearchView) findViewById(R.id.activity_main_sv_search);
        filter = (ImageView) findViewById(R.id.activity_main_iv_filter);

        if (sessionManager.isLoggedIn()) {
            Navigator.loadFragment(this, OrdersFragment.newInstance(this), R.id.activity_main_fl_container, false);
        } else {
            Navigator.loadFragment(this, LoginFragment.newInstance(this), R.id.activity_main_fl_container, false);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            sessionManager.logout();
            Navigator.loadFragment(this, LoginFragment.newInstance(this), R.id.activity_main_fl_container, false);
        }
        else if(id == R.id.changeLang){
            changeLanguage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage() {

        if (sessionManager.getLanguage().equals("en")) {
            sessionManager.setLanguage("ar");
            MainActivity.isEnglish = false;
        } else {
            sessionManager.setLanguage("en");
            MainActivity.isEnglish = true;
        }
        LocaleHelper.setLocale(this, sessionManager.getLanguage());

        finish();
        overridePendingTransition(0, 0);
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.activity_main_iv_back)
    public void backCLick() {
        if (!search.isIconified()) {
            search.onActionViewCollapsed();
        } else
            onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            backCLick();
        }
        return true;
    }

}
