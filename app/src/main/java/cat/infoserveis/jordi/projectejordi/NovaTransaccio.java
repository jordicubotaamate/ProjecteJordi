package cat.infoserveis.jordi.projectejordi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cat.infoserveis.jordi.projectejordi.BasesDeDades.FinancesDataBaseAdapter;

public class NovaTransaccio extends AppCompatActivity {
    int id;
    FinancesDataBaseAdapter bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_transaccio);

        Intent intent = getIntent();

        bbdd = new FinancesDataBaseAdapter(this);
        bbdd = bbdd.open();

        id = intent.getIntExtra("id",1000);

//Careguem les preferencies
        SharedPreferences sh = PreferenceManager
                .getDefaultSharedPreferences(this);
        final boolean negatiu = sh.getBoolean("negatiu",false);



        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        final EditText quant = (EditText) findViewById(R.id.quantitat);
        quant.requestFocus();

        final EditText concepte = (EditText) findViewById(R.id.concepte);

        Button aplicar = (Button) findViewById(R.id.aplicar);



        aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Agafemdades dels views
                String conceptet = concepte.getText().toString();
                Double quantitat = null;
                Double total = null;
                try{
                    quantitat = Double.parseDouble(quant.getText().toString());//podria ser un camp buit i causar una excepcio
                    total = bbdd.getTotalUltima(id)+Double.parseDouble(quant.getText().toString());

                }catch (NumberFormatException n){
                    System.out.println(n);
                }
                if(quantitat != null) {
                    if (total >=0 || total < 0 && negatiu) {
                        bbdd.insertEntry(conceptet, quantitat, id, System.currentTimeMillis(), total);
                        finish();
                    } else {
                        /*Snackbar.make(v, R.string.insufficient, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                         No es veu amb el teclat obert...  Toast si */
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.insufficient, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }


            }
        });
    }
}
