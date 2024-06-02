package com.eduardosantos.prototipo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;

public class UserMainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private UserViewPagerAdapter userViewPagerAdapter;
    private UserDatabaseHelper userDatabaseHelper;
    private String userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDatabaseHelper = new UserDatabaseHelper(this);

        setupViews();
        loadUserInfo();
    }

    private void setupViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        userViewPagerAdapter = new UserViewPagerAdapter(this, userEmail, userName);
        viewPager2.setAdapter( userViewPagerAdapter );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userName = sharedPreferences.getString("user_name", null);
        userEmail = sharedPreferences.getString("user_email", null);

        if (userName != null && userEmail != null) {
            Toast.makeText(this, getString(R.string.welcome_message, userName), Toast.LENGTH_SHORT).show();
            setupViewPagerAdapter();
        }
    }

    private void setupViewPagerAdapter() {
        userViewPagerAdapter = new UserViewPagerAdapter(this, userEmail, userName);
        viewPager2.setAdapter( userViewPagerAdapter );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Toast.makeText(this, R.string.settings_selected, Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.share) {
            Toast.makeText(this, R.string.share_selected, Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.aboutus) {
            Toast.makeText(this, R.string.about_selected, Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.logout) {
            Toast.makeText(this, R.string.logout_message, Toast.LENGTH_SHORT).show();
            logout();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
