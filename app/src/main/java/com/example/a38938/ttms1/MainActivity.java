package com.example.a38938.ttms1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.a38938.ttms1.data.Data;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.ScheduleData;
import com.example.a38938.ttms1.store.StoreManager;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MFragment mCurrent = null;
    private MFragment mMainFragment = null;
    private MFragment mPlayFragment = null;
    private MFragment mStudioManageFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions()| ActionBar.DISPLAY_SHOW_CUSTOM);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mMainFragment = mCurrent = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace((R.id.main_fragment), mCurrent).commit();
//        List<PlayData> ds;
//        PlayData dd = new PlayData();
//        dd.score = 3.3f;
//        dd.intro = "test";
//        dd.name = "test";
//        dd.actors = dd.director = "test";
//        ds = StoreManager.get().getPlays();
//        Log.e("xx", " " + ds.size());
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

        if (id == R.id.usrs_management) {
            Intent user_intent = new Intent(MainActivity.this,Users_Activity.class);
            startActivity(user_intent);
                        // Handle the camera action
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
            Intent schedule_intent = new Intent(MainActivity.this,Schedule_Activity.class);
            startActivity(schedule_intent);

        } else if (id == R.id.tickets_management) {
            Intent tickets_intent = new Intent(MainActivity.this,Tickets_Activity.class);
            startActivity(tickets_intent);

        } else if (id == R.id.analysis_management) {
            Intent analysis_intent = new Intent(MainActivity.this,Analysis_Activity.class);
            startActivity(analysis_intent);

        }else if(id == R.id.login){
            Intent login_intent = new Intent(MainActivity.this,Login_Activity.class);
            startActivity(login_intent);
        }else if(id == R.id.main){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mCurrent = mMainFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
