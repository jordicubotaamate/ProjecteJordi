package cat.infoserveis.jordi.projectejordi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cat.infoserveis.jordi.projectejordi.BasesDeDades.FinancesDataBaseAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FinancesDataBaseAdapter financesBBDD;
    int id;
    String email;
    String nom;
    Intent novaTrans;
    ListView l;//transaccions mostrades.

    public boolean decimals;
    public boolean negatiu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Restaurem si cliquem enrere
        if (savedInstanceState != null){
            id = savedInstanceState.getInt("IDb");
            email = savedInstanceState.getString("emailb");
        }
        else
        {
            //Agafem dades d lintent
            Intent intent = getIntent();

            id = intent.getIntExtra("ID",1000);
            email = intent.getStringExtra("mail");

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        novaTrans = new Intent(MainActivity.this, NovaTransaccio.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                novaTrans.putExtra("id",id);
                MainActivity.this.startActivity(novaTrans);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //CARREGUEM LES PREFERENCIES
        SharedPreferences sh = PreferenceManager
                .getDefaultSharedPreferences(this);
        decimals = sh.getBoolean("decimals",false);
        negatiu = sh.getBoolean("negatiu",false);
        nom = sh.getString("userName","Nicolás");




        //A StackOverflow m'han recomanat fer aixo per evitar un nullPointer a la barra lateral:
        View header = navigationView.getHeaderView(0);

        TextView nomtv = (TextView) header.findViewById(R.id.nomTV);
        TextView emailtv = (TextView) header.findViewById(R.id.emailTV);
        nomtv.setText(nom);
        emailtv.setText(email);


        //BBDD
        financesBBDD = new FinancesDataBaseAdapter(this);
        financesBBDD = financesBBDD.open();

        /*financesBBDD.insertEntry(10*id,id,100.12);
        financesBBDD.insertEntry(-12.233*id,id,104.12);*/
        //financesBBDD.insertEntry("TEST",649.2*id,id,1004.12);

        l = (ListView) findViewById(R.id.listView);





        //TransaccioAdaptador adap = creaTransaccio(financesBBDD);
        //l.setAdapter(adap);

        l.setAdapter(creaTransaccions(financesBBDD));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentSettings = new Intent(this, SettingsActivity.class);
            MainActivity.this.startActivity(intentSettings);
            return true;
        }
        else if (id == R.id.JordiCubotaAmate) {
            Intent intentSettings = new Intent(this, ScrollingActivity.class);
            MainActivity.this.startActivity(intentSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_transactions) {
            // Handle the camera action
        } else if (id == R.id.nav_resetTOT) {
            resetDialog(this,getString(R.string.reset),getString(R.string.resetText));
            return false;
        } else if (id == R.id.nav_manage) {
            Intent intentSettings = new Intent(this, SettingsActivity.class);
            intentSettings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MainActivity.this.startActivity(intentSettings);
            return false;
        } else if (id == R.id.nav_share) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public TransaccioAdaptador creaTransaccions(FinancesDataBaseAdapter bbdd){
        ArrayList<Transaccio> ArrTran = bbdd.getTransaccions(id);
        return  new TransaccioAdaptador(this, ArrTran);
    }

    @Override
    protected void onResume() {//Recarreguem la llista cada cop que tornem a aquesta activitat, per exemple: quan acabem de crear una transaccio
        l.setAdapter(creaTransaccions(financesBBDD));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView nomtv = (TextView) header.findViewById(R.id.nomTV);
        TextView emailtv = (TextView) header.findViewById(R.id.emailTV);
        nomtv.setText(nom);
        emailtv.setText(email);
        super.onResume();
    }

    public void resetDialog(Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                financesBBDD.deleteAll(id);
                l.setAdapter(creaTransaccions(financesBBDD));//Per recarregar les transaccions (buides)

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putInt("IDb", id);
        icicle.putString("emailb",email);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
