package com.ftninformatika.stevanmihalic.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ftninformatika.stevanmihalic.R;
import com.ftninformatika.stevanmihalic.adapters.DrawerAdapter;
import com.ftninformatika.stevanmihalic.adapters.MainAdapter;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TOAST_MESSAGE = "toast";

    private int position = 1;
    private DatabaseHelper databaseHelper = null;
    private Grupa grupa = null;
    private Task task = null;

    private ListView listViewMain = null;
    private List<Grupa> grupaList = null;
    private MainAdapter mainAdapter = null;

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

        prikaziListuGrupa();



    }

    private void prikaziListuGrupa() {
        listViewMain = findViewById(R.id.list_view_MAIN);
        try {
            grupaList = getDatabaseHelper().getGrupa().queryForAll();
            mainAdapter = new MainAdapter(this, grupaList);
            listViewMain.setAdapter(mainAdapter);
            listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    grupa = (Grupa) listViewMain.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", grupa.getId());
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dodajGrupu() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dodaj_grupu);
        dialog.show();

        final EditText editNaziv = dialog.findViewById(R.id.dodaj_grupu_naziv);
        final EditText editVremeKreiranja = dialog.findViewById(R.id.dodaj_grupu_vreme_kreiranja);
        final EditText editOznaka1 = dialog.findViewById(R.id.dodaj_grupu_oznaka1);
        final EditText editOznaka2 = dialog.findViewById(R.id.dodaj_grupu_oznaka2);

        Button confirm = dialog.findViewById(R.id.dodaj_grupu_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNaziv.getText().toString().isEmpty()) {
                    editNaziv.setError("Polje Naziv ne sme biti prazno!");
                    return;
                }
                if (editVremeKreiranja.getText().toString().isEmpty()) {
                    editVremeKreiranja.setError("Polje Vreme Kreiranja ne sme biti prazno!");
                    return;
                }

                String naziv = editNaziv.getText().toString();
                String vremeKreiranja = editVremeKreiranja.getText().toString();
                String oznaka1 = editOznaka1.getText().toString();
                String oznaka2 = editOznaka2.getText().toString();

                grupa = new Grupa();
                grupa.setNaziv(naziv);
                grupa.setVremeKreiranjaGrupe(vremeKreiranja);
                grupa.setOznake(oznaka1);
                grupa.setOznake(oznaka2);


                try {
                    getDatabaseHelper().getGrupa().create(grupa);
                    refresh();
                    dialog.dismiss();

                    message1 = new SpannableString("Uspesno kreirana Grupa:  ");
                    message2 = new SpannableString(grupa.getNaziv());

                    message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    message2.setSpan(new ForegroundColorSpan(Color.RED), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (showMessage) {
                        Toast toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG);
                        View toastView = toast.getView();

                        TextView toastText = toastView.findViewById(android.R.id.message);
                        toastText.setText(message1);
                        toastText.append(message2);
                        toast.show();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });


        Button cancel = dialog.findViewById(R.id.dodaj_grupu_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void refresh() {
        listViewMain = findViewById(R.id.list_view_MAIN);
        if (listViewMain != null) {
            mainAdapter = (MainAdapter) listViewMain.getAdapter();
            if (mainAdapter != null) {
                try {
                    grupaList = getDatabaseHelper().getGrupa().queryForAll();
                    mainAdapter.refreshList(grupaList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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
