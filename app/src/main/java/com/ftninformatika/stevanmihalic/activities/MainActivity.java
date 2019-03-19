package com.ftninformatika.stevanmihalic.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ftninformatika.stevanmihalic.R;
import com.ftninformatika.stevanmihalic.adapters.DrawerAdapter;
import com.ftninformatika.stevanmihalic.db.DatabaseHelper;
import com.ftninformatika.stevanmihalic.db.model.Grupa;
import com.ftninformatika.stevanmihalic.dialogs.AboutDialog;
import com.ftninformatika.stevanmihalic.model.NavigationItems;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TOAST_MESSAGE = "toast";

    private int position = 1;
    private DatabaseHelper databaseHelper = null;
    private Grupa grupa = null;

    private ListView listViewMain = null;
    private List<Grupa> grupaList = null;
    private ArrayAdapter<Grupa> grupaArrayAdapter = null;

    private SharedPreferences sharedPreferences = null;
    private boolean showMessage = false;

    private Spannable message1 = null;
    private Spannable message2 = null;


    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItems> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawer();



    }

    private void prikaziListuGrupa() {

    }

    private void dodajGrupu() {

    }

    private void refresh() {

    }



    private void consultPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        showMessage = sharedPreferences.getBoolean(TOAST_MESSAGE, true);
    }

    /**
     * Navigaciona Fioka
     */
    private void navigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar_MAIN);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_list);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        drawerItems.add(new NavigationItems("Grupe", "Prikazuje listu Grupa", R.drawable.ic_show_all));
        drawerItems.add(new NavigationItems("Podesavanja", "Otvara Podesavanja Aplikacije", R.drawable.ic_settings));
        drawerItems.add(new NavigationItems("Informacije", "Informacije o Aplikaciji", R.drawable.ic_about_app));

        DrawerAdapter drawerAdapter = new DrawerAdapter(this, drawerItems);
        drawerListView = findViewById(R.id.nav_list_MAIN);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.drawer_layout_MAIN);
        drawerPane = findViewById(R.id.drawer_pane_MAIN);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerClosed(drawerView);
            }
        };

    }

    /**
     * OnItemClick iz NavigacioneFioke.
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                refresh();
            } else if (position == 1) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            } else if (position == 2) {
                AlertDialog aboutDialog = new AboutDialog(MainActivity.this).prepareDialog();
                aboutDialog.show();
            }

            drawerLayout.closeDrawer(drawerPane);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_dodaj_grupu:
                dodajGrupu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Proveravamo datum
     */
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    @Override
    protected void onRestart() {
        refresh();
        super.onRestart();
    }


    @Override
    protected void onResume() {
        refresh();
        consultPreferences();


        super.onResume();
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position", position);
    }

}
