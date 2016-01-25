package cat.infoserveis.jordi.projectejordi;

/**
 * Created by jordi on 20/01/16.
 */
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cat.infoserveis.jordi.projectejordi.R;

public class TransaccioAdaptador extends BaseAdapter
{

    static class ViewHolder
    {
        TextView transaccio;
        TextView comentari;
        TextView total;
        TextView date;
    }

    private static final String TAG = "CustomAdapter";
    private static int convertViewCounter = 0;

    private ArrayList<Transaccio> data;
    private LayoutInflater inflater = null;

    private Context c;

    public TransaccioAdaptador(Context c, ArrayList<Transaccio> d)
    {
        Log.v(TAG, "Constructing CustomAdapter");

        this.data = d;
        this.c = c;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount()
    {
        Log.v(TAG, "in getCount()");
        return data.size();
    }

    @Override
    public Object getItem(int position)
    {
        Log.v(TAG, "in getItem() for position " + position);
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        Log.v(TAG, "in getItemId() for position " + position);
        return position;
    }

    @Override
    public int getViewTypeCount()
    {
        Log.v(TAG, "in getViewTypeCount()");
        return 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        Log.v(TAG, "in getItemViewType() for position " + position);
        return 0;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder;

        Log.v(TAG, "in getView for position " + position + ", convertView is "
                + ((convertView == null) ? "null" : "being recycled"));

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.transacciolist, null);

            convertViewCounter++;
            Log.v(TAG, convertViewCounter + " convertViews have been created");

            holder = new ViewHolder();

            holder.transaccio = (TextView) convertView
                    .findViewById(R.id.Transaccio);
            holder.comentari = (TextView) convertView
                    .findViewById(R.id.Comentari);
            holder.total = (TextView) convertView
                    .findViewById(R.id.Total);

            holder.date = (TextView) convertView
                    .findViewById(R.id.Date);

            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();

        //Careguem les preferencies
        SharedPreferences sh = PreferenceManager
                .getDefaultSharedPreferences(c);//Hem dagafar el context de lactivitat. El passem al constructor
        boolean decimals = sh.getBoolean("decimals",false);

        String m = sh.getString("moneda","Euro");

        char moneda = '€';

        switch (m){
            case "Euro":
                moneda = '€';
                break;
            case "Dolar":
                moneda = '$';
                break;
            case "Pound":
                moneda = '£';
                break;

        }

        // Posem tot a les vistes
        double transaccio;
        if(decimals)
        {
            //Transaccio:
            transaccio = data.get(position).getTransaccio();
            holder.transaccio.setText(String.format("%.2f", transaccio)+moneda);//Maxim mostrem 2 decimals
            //total:
            holder.total.setText(String.format("%.2f", data.get(position).getTotal())+moneda);
        }
        else
        {
            //trans
            transaccio = (int)(data.get(position).getTransaccio());
            holder.transaccio.setText(Integer.toString((int)transaccio)+moneda);//No mostrem decimals

            //total
            holder.total.setText(Integer.toString((int)data.get(position).getTotal())+moneda);
        }






        if(transaccio<0)
        {
            holder.transaccio.setTextColor(Color.RED);
        }
        else
        {
            holder.transaccio.setTextColor(Color.rgb(52,154,78));
        }
        holder.comentari.setText(data.get(position).getConcepte());

        String date = "D"+DateFormat.getInstance().format(data.get(position).getDate());

        holder.date.setText(date);

        return convertView;
    }
//    TODO
    /*public void setCheck(int position)
    {
        PostData d = data.get(position);

        d.setChecked(!d.getChecked());
        notifyDataSetChanged();
    }

    public void checkAll(boolean state)
    {
        for (int i = 0; i < data.size(); i++)
            data.get(i).setChecked(state);
    }

    public void cancelSelectedPost()
    {

        int i = 0;
        while (i < getCount())
        {
            if (data.get(i).getChecked())
            {
                data.remove(data.indexOf(data.get(i)));
            } else
                i++;
        }
        notifyDataSetChanged();

    }

    public boolean haveSomethingSelected()
    {
        for (int i = 0; i < data.size(); i++)
            if (data.get(i).getChecked())
                return true;
        return false;
    }

    /**
     * Este método es para poder seleccionar una fila directamente con el
     * checkbox en lugar de tener que pulsar en la liste en sí
     *
    private OnClickListener checkListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            PostData d = (PostData) v.getTag();
            d.setChecked(!d.getChecked());
        }
    };*/
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}