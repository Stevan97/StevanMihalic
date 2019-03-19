package com.ftninformatika.stevanmihalic.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ftninformatika.stevanmihalic.R;
import com.ftninformatika.stevanmihalic.adapters.DrawerAdapter;
import com.ftninformatika.stevanmihalic.adapters.TaskAdapter;
import com.ftninformatika.stevanmihalic.db.DatabaseHelper;
import com.ftninformatika.stevanmihalic.db.model.Grupa;
import com.ftninformatika.stevanmihalic.db.model.Task;
import com.ftninformatika.stevanmihalic.dialogs.AboutDialog;
import com.ftninformatika.stevanmihalic.model.NavigationItems;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    private static final String TOAST_MESSAGE = "toast";

    private int position = 1;
    private DatabaseHelper databaseHelper = null;


    private Task task = null;
    private Intent intentPosition = null;
    private int idPosition = 0;

    private Spannable message1 = null;
    private Spannable message2 = null;
    private Spannable message3 = null;
    private Toast toast = null;
    private View toastView = null;
    private TextView textToast = null;

    private Button cancel = null;
    private Button confirm = null;

    private boolean showMessage = false;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItems> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //navigationDrawer();

        prikaziDetaljeTaska();


    }

    private void prikaziDetaljeTaska() {
        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            task = getDatabaseHelper().getToDoZadatak().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView naziv = findViewById(R.id.task_naziv_taska);
        message1 = new SpannableString("Naziv Zadatka: ");
        message2 = new SpannableString(task.getNaziv());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView opis = findViewById(R.id.Task_opis_taska);
        message1 = new SpannableString("Opis Zadatka ");
        message2 = new SpannableString(task.getOpis());
        spannableStyle();
        opis.setText(message1);
        opis.append(message2);

        TextView prioritet = findViewById(R.id.Task_prioritet);
        message1 = new SpannableString("Prioritet Zadatka ");
        message2 = new SpannableString(task.getTipPrioriteta());
        spannableStyle();
        prioritet.setText(message1);
        prioritet.append(message2);

        TextView kreiran = findViewById(R.id.task_kreiranje);
        message1 = new SpannableString("Zadatak Kreiran: ");
        message2 = new SpannableString(task.getVremeKreiranja());
        spannableStyle();
        kreiran.setText(message1);
        kreiran.append(message2);

        TextView zavrsetak = findViewById(R.id.task_zavrsetak);
        message1 = new SpannableString("Zadatak Zavrsen: ");
        message2 = new SpannableString(task.getVremeZavrsetka());
        spannableStyle();
        zavrsetak.setText(message1);
        zavrsetak.append(message2);

        TextView status = findViewById(R.id.task_status);
        message1 = new SpannableString("Status zadatka: ");
        message2 = new SpannableString(task.getStatus());
        spannableStyle();
        status.setText(message1);
        status.append(message2);

    }

    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void consultPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TaskActivity.this);
        showMessage = sharedPreferences.getBoolean(TOAST_MESSAGE, true);
    }


    @Override
    protected void onResume() {
        consultPreferences();
        super.onResume();
    }

    /**
     * Navigaciona Fioka
     */
    private void navigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar_task);
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

        DrawerAdapter drawerAdapter = new DrawerAdapter(TaskActivity.this, drawerItems);
        drawerListView = findViewById(R.id.nav_list_task);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.drawer_layout_task);
        drawerPane = findViewById(R.id.drawer_pane_task);

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
                onBackPressed();
                overridePendingTransition(0, 0);
            } else if (position == 1) {
                Intent intent = new Intent(TaskActivity.this, SettingsActivity.class);
                startActivity(intent);
            } else if (position == 2) {
                AlertDialog aboutDialog = new AboutDialog(TaskActivity.this).prepareDialog();
                aboutDialog.show();
            }

            drawerLayout.closeDrawer(drawerPane);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_task_delete:

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Proveravamo datum
     */
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
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


