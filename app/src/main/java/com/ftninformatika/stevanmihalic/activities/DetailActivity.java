package com.ftninformatika.stevanmihalic.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ftninformatika.stevanmihalic.R;
import com.ftninformatika.stevanmihalic.adapters.DrawerAdapter;
import com.ftninformatika.stevanmihalic.adapters.TaskAdapter;
import com.ftninformatika.stevanmihalic.db.DatabaseHelper;
import com.ftninformatika.stevanmihalic.db.model.Grupa;
import com.ftninformatika.stevanmihalic.db.model.Oznake;
import com.ftninformatika.stevanmihalic.db.model.Task;
import com.ftninformatika.stevanmihalic.dialogs.AboutDialog;
import com.ftninformatika.stevanmihalic.model.NavigationItems;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TOAST_MESSAGE = "toast";

    private int position = 1;
    private DatabaseHelper databaseHelper = null;

    private Grupa grupa = null;
    private Task task = null;

    private ForeignCollection<Task> taskForeignCollection = null;
    private List<Task> taskList = null;
    private ListView listViewDetail = null;
    private TaskAdapter taskAdapter = null;
    private Intent intentPosition = null;
    private int idPosition = 0;

    private ForeignCollection<Oznake> oznakeForeignCollection = null;
    private List<Oznake> oznakeList = null;
    private ArrayAdapter<Oznake> arrayAdapterOznake = null;

    private String prioritet = null;
    private Spinner spinner = null;
    private SpinnerAdapter spinnerAdapter = null;
    private String[] nizPrioritet = null;

    private String status = null;
    private Spinner spinnerStatus = null;
    private SpinnerAdapter spinnerAdapterStatus = null;
    private String[] nizStatus = null;


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
        setContentView(R.layout.activity_detail);

        navigationDrawer();

        prikaziDetaljeGrupe();


    }

    private void prikaziDetaljeGrupe() {
        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            grupa = getDatabaseHelper().getGrupa().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView naziv = findViewById(R.id.detail_naziv_grupe);
        message1 = new SpannableString("Naziv Grupe: ");
        message2 = new SpannableString(grupa.getNaziv());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView biografija = findViewById(R.id.detail_datum_kreiranja);
        message1 = new SpannableString("Datum Kreiranja: ");
        message2 = new SpannableString(grupa.getVremeKreiranjaGrupe());
        spannableStyle();
        biografija.setText(message1);
        biografija.append(message2);

        ListView listView = findViewById(R.id.list_view_tagovi);
        try {
            oznakeForeignCollection = getDatabaseHelper().getGrupa().queryForId(idPosition).getOznake();
            oznakeList = new ArrayList<>(oznakeForeignCollection);
            arrayAdapterOznake = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, oznakeList);
            listView.setAdapter(arrayAdapterOznake);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        listViewDetail = findViewById(R.id.list_view_DETAIL);
        try {
            taskForeignCollection = getDatabaseHelper().getGrupa().queryForId(idPosition).getToDoZadaci();
            taskList = new ArrayList<>(taskForeignCollection);
            taskAdapter = new TaskAdapter(this, taskList);
            listViewDetail.setAdapter(taskAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void addTask() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.add_task);
        dialog.show();

        nizPrioritet = getResources().getStringArray(R.array.PRIORITET);

        spinner = dialog.findViewById(R.id.add_task_spinner_prioritet);
        spinnerAdapter = new ArrayAdapter<>(DetailActivity.this, android.R.layout.simple_spinner_item, nizPrioritet);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritet = String.valueOf(spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nizStatus = getResources().getStringArray(R.array.STATUS);

        spinnerStatus = dialog.findViewById(R.id.add_task_spinner_status);
        spinnerAdapterStatus = new ArrayAdapter<>(DetailActivity.this, android.R.layout.simple_spinner_item, nizStatus);
        spinnerStatus.setAdapter(spinnerAdapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = String.valueOf(spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final EditText editNaziv = dialog.findViewById(R.id.add_task_naziv);
        final EditText editOpis = dialog.findViewById(R.id.add_task_opis);
        final EditText editKreirano = dialog.findViewById(R.id.add_task_vreme_kreiranja);
        final EditText editZavrseno = dialog.findViewById(R.id.add_task_vreme_zavrsetka);

        confirm = dialog.findViewById(R.id.add_task_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNaziv.getText().toString().isEmpty()) {
                    editNaziv.setError("Polje Naziv ne sme biti prazno");
                    return;
                }
                if (editOpis.getText().toString().isEmpty()) {
                    editOpis.setError("Polje Opis ne sme biti prazno");
                    return;
                }

                String naziv = editNaziv.getText().toString();
                String opis = editOpis.getText().toString();
                String kreirano = editKreirano.getText().toString();
                String zavrseno = editZavrseno.getText().toString();


                try {
                    intentPosition = getIntent();
                    idPosition = intentPosition.getExtras().getInt("id");
                    grupa = getDatabaseHelper().getGrupa().queryForId(idPosition);


                    task = new Task();
                    task.setNaziv(naziv);
                    task.setOpis(opis);
                    task.setVremeKreiranja(kreirano);
                    task.setVremeZavrsetka(zavrseno);
                    task.setTipPrioriteta(prioritet);
                    task.setStatus(status);

                    getDatabaseHelper().getToDoZadatak().create(task);
                    dialog.dismiss();

                    message1 = new SpannableString("Uspesno kreiran Zadatak sa nazivom: ");
                    message2 = new SpannableString(task.getNaziv());
                    spannableStyle();

                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message1);
                        textToast.append(message2);
                        toast.show();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        cancel = dialog.findViewById(R.id.add_task_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void izbrisiGrupu() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.izbrisi_grupu);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            grupa = getDatabaseHelper().getGrupa().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView text = dialog.findViewById(R.id.izbrisi_grupu_text);
        message1 = new SpannableString("Da li ste sigurni da zelite da izbrisete Grupu pod nazivom: ");
        message2 = new SpannableString(grupa.getNaziv());
        spannableStyle();
        text.setText(message1);
        text.append(message2);

        confirm = dialog.findViewById(R.id.izbrisi_grupu_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                try {

                    taskForeignCollection = getDatabaseHelper().getGrupa().queryForId(idPosition).getToDoZadaci();
                    taskList = new ArrayList<>(taskForeignCollection);

                    getDatabaseHelper().getToDoZadatak().delete(taskList);
                    getDatabaseHelper().getGrupa().delete(grupa);
                    onBackPressed();

                    message1 = new SpannableString("Uspesno izbrisana Grupa sa nazivom:  ");
                    message2 = new SpannableString(grupa.getNaziv());
                    spannableStyle();

                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message1);
                        textToast.append(message2);
                        toast.show();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        cancel = dialog.findViewById(R.id.izbrisi_grupu_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void consultPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
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
        Toolbar toolbar = findViewById(R.id.toolbar_DETAIL);
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
        drawerListView = findViewById(R.id.nav_list_DETAIL);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DetailActivity.DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.drawer_layout_DETAIL);
        drawerPane = findViewById(R.id.drawer_pane_DETAIL);

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
                Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
                startActivity(intent);
            } else if (position == 2) {
                AlertDialog aboutDialog = new AboutDialog(DetailActivity.this).prepareDialog();
                aboutDialog.show();
            }

            drawerLayout.closeDrawer(drawerPane);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_detail_delete:
                izbrisiGrupu();
                break;
            case R.id.menu_detail_add_task:
                addTask();
                break;
            case R.id.menu_detail_update:

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
