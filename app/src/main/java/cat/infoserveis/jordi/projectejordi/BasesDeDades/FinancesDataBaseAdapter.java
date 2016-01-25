package cat.infoserveis.jordi.projectejordi.BasesDeDades;

/**
 * Created by Jordi on 05/01/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import cat.infoserveis.jordi.projectejordi.Transaccio;

public class FinancesDataBaseAdapter
{
    static final String DATABASE_NAME = "finances.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"FINANCES"+
            "( " +"ID"+" integer primary key autoincrement,"+ "IDOWNER  integer, CONCEPT text, AMOUNT real, DATE integer, TOTALMONEY real); ";
    // Variable to hold the database instance*/
    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private FinancesDataBaseHelper dbHelper;
    public FinancesDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new FinancesDataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public FinancesDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String concept, double amount, int IDowner, long date, double totalMoney)
    {
        ContentValues newValues = new ContentValues();
        // Assignem valors
        newValues.put("CONCEPT",concept);
        newValues.put("IDOWNER",IDowner);
        newValues.put("AMOUNT", amount);
        newValues.put("DATE",date);
        newValues.put("TOTALMONEY",totalMoney);


        // Insertem la columna a la BBDD
        db.insert("FINANCES", null, newValues);
        //DEBUGGING Toast.makeText(context, "Sessió iniciada correctament", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Transaccio> getTransaccions(int IDowner)
    {
        //Cursor cursor=db.query("FINANCES", null, " ID=?", new String[]{Integer.toString(IDowner)}, null, null, null);
        //TEST
        String[] whereArgs = new String[] {
                Integer.toString(IDowner)
        };
        String queryString = "select * from FINANCES where IDOWNER = ?";
        Cursor cursor = db.rawQuery(queryString, whereArgs);

        //cursor.moveToFirst();

        ArrayList<Transaccio> ar = new ArrayList<>();
        while (cursor.moveToNext()) {
            Transaccio t = new Transaccio(cursor.getString(cursor.getColumnIndex("CONCEPT")),cursor.getDouble(cursor.getColumnIndex("AMOUNT")),cursor.getDouble(cursor.getColumnIndex("TOTALMONEY")),cursor.getLong(cursor.getColumnIndex("DATE")),IDowner);//new Transaccio("CONCEPTE",cursor.getDouble(cursor.getColumnIndex("AMOUNT")),cursor.getDouble(cursor.getColumnIndex("TOTAL")),cursor.getInt(cursor.getColumnIndex("IDOWNER")));
            ar.add(t);
            System.out.print("HEEEE");
        }
        cursor.close();
        return ar;
    }

    public Double getTotalUltima(int IDowner)
    {
        //Cursor cursor=db.query("FINANCES", null, " ID=?", new String[]{Integer.toString(IDowner)}, null, null, null);
        //TEST
        String[] whereArgs = new String[] {
                Integer.toString(IDowner)
        };
        String queryString = "select TOTALMONEY from FINANCES where IDOWNER = ? order by ID DESC;";
        Cursor cursor = db.rawQuery(queryString, whereArgs);
        cursor.moveToFirst();

        Double total;
        Double resul = null;
        try {
        resul = cursor.getDouble(cursor.getColumnIndex("TOTALMONEY"));

        }catch (CursorIndexOutOfBoundsException c){
            System.err.println(c);
        }
        if(resul == null)//Si es la seva primera transaccio la BBDD ens tornara null, per tant té 0€
        {
            total =  new Double(0);
        }
        else
        {
            total = resul;
        }
        cursor.close();
        return total;
    }

    public void deleteAll(int IDowner)
    {
        String[] whereArgs = new String[] {
                Integer.toString(IDowner)
        };
        String queryString = "delete from FINANCES where IDOWNER = ?;";
        //db.rawQuery(queryString, whereArgs);
        db.delete("FINANCES","IDOWNER = ?", whereArgs);
    }

}