package cat.infoserveis.jordi.projectejordi.BasesDeDades;

/**
 * Created by Jordi on 05/01/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class FinancesDataBaseAdapter
{
    static final String DATABASE_NAME = "finances.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"FINANCES"+
            "( " +"ID"+" integer primary key autoincrement,"+ "IDOWNER  integer, AMOUNT real, "/*DATE DATE,*/+" TOTALMONEY real); ";
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

    public void insertEntry(double amount, int IDowner/*, Date date*/, double totalMoney)
    {
        ContentValues newValues = new ContentValues();
        // Assignem valors
        newValues.put("IDOWNER",IDowner);
        newValues.put("AMOUNT", amount);
        //newValues.put("DATE",);
        newValues.put("TOTALMONEY",totalMoney);

        // Insertem la columna a la BBDD
        db.insert("FINANCES", null, newValues);
        Toast.makeText(context, "Sessió iniciada correctament", Toast.LENGTH_SHORT).show();
    }
}