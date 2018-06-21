package com.example.a38938.ttms1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a38938.ttms1.data.UserData;
import com.example.a38938.ttms1.store.OnDataGetListener;
import com.example.a38938.ttms1.store.StoreManager;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MFragment mCurrent = null;
    private MFragment mMainFragment = null;
    private MFragment mPlayFragment = null;
    private MFragment mStudioManageFragment = null;
    private MFragment mScheduleManageFragment = null;
    private MFragment mSaleFragment = null;
    private MFragment mUserFragment = null;
    private MFragment mLoginFragment = null;

    private int lastPage = -1;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private OnDataGetListener<UserData> mLoginListener = new OnDataGetListener<UserData>() {
        @Override
        public void onReceive(List<UserData> datas) {
            if (datas.size() != 0) {
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                if (lastPage != -1 && lastPage != R.id.login) {
                    onGoto(lastPage);
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        checkPermission();
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions()| ActionBar.DISPLAY_SHOW_CUSTOM);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        onGoto(R.id.schedule_management);

        startService(new Intent(this, ServerServcice.class));
//        List<PlayData> ds;
//        PlayData dd = new PlayData();
//        dd.score = 3.3f;
//        dd.intro = "test";
//        dd.name = "test";
//        dd.actors = dd.director = "test";
//        ds = StoreManager.get().getPlays();
//        Log.e("xx", " " + ds.size());
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 16) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!mCurrent.onBackPressed()) {
                super.onBackPressed();
            }
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        onGoto(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onGoto(int id) {
        lastPage = id;
        if (!StoreManager.get().isLoged()) {
            id = R.id.login;
        } else if (id == R.id.login) {
            Toast.makeText(this, "已经登录了", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.usrs_management) {
            if (mUserFragment == null) {
                mUserFragment = new UserFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mCurrent = mUserFragment).commit();
        } else if (id == R.id.play_management) {
            if (mPlayFragment == null) {
                mPlayFragment = new PlayManageFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mCurrent = mPlayFragment).commit();
        } else if (id == R.id.stdio_management) {
            if (mStudioManageFragment == null) {
                mStudioManageFragment = new StudioManagerFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mCurrent = mStudioManageFragment).commit();

        } else if (id == R.id.schedule_management) {
            if (mScheduleManageFragment == null) {
                mScheduleManageFragment = new ScheduleManageFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mCurrent = mScheduleManageFragment).commit();
        } else if (id == R.id.tickets_management) {
            if (mSaleFragment == null) {
                mSaleFragment = new SaleFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mCurrent = mSaleFragment).commit();
        }else if(id == R.id.login){
            if (mLoginFragment == null) {
                mLoginFragment = new LoginFragment();
                ((LoginFragment)mLoginFragment).setListener(mLoginListener);
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mCurrent = mLoginFragment).commit();
        }
    }
}
