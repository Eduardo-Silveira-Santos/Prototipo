package com.eduardosantos.prototipo;

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

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter vpAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        vpAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(vpAdapter);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

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

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("user_name", null);
        String userEmail = sharedPreferences.getString("user_email", null);

        Toast.makeText(this, "Bem-vindo!", Toast.LENGTH_SHORT).show();

        FragmentAccount fragmentAccount = new FragmentAccount();
        Bundle bundle = new Bundle();
        bundle.putString("user_name", userName);
        bundle.putString("user_email", userEmail);
        fragmentAccount.setArguments(bundle);

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
            Toast.makeText(this, "Configurações Selecionadas", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.share) {
            Toast.makeText(this, "Compartilhamento Selecionado", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.aboutus) {
            Toast.makeText(this, "Informações Selecionadas", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.logout) {
            Toast.makeText(this, "Sair do Aplicativo", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
