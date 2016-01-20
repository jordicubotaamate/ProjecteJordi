package cat.infoserveis.jordi.projectejordi;

/**
 * Created by jordi on 20/01/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Transaccio implements Parcelable {

    private double transaccio;
    private String concepte;
    private double total;

    public Transaccio(String concepte, double transaccio, double total) {
        this.concepte = concepte;
        this.transaccio = transaccio;
        this.total = total;
    }

    public Transaccio(Parcel in){
        this.transaccio= in.readDouble();
        this.concepte = in.readString();
        this.total = in.readDouble();
    }

    public double getTransaccio() {
        return transaccio;
    }

    public void setTransaccio(int transaccio) {
        this.transaccio = transaccio;
    }

    public String getConcepte() {
        return concepte;
    }

    public void setConcepte(String concepte) {
        this.concepte = concepte;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(getTransaccio());
        dest.writeString(getConcepte());
        dest.writeDouble(getTotal());
    }

    public static final Parcelable.Creator<Transaccio> CREATOR = new Parcelable.Creator<Transaccio>() {
        public Transaccio createFromParcel(Parcel in) {
            return new Transaccio(in);
        }

        public Transaccio[] newArray(int size) {
            return new Transaccio[size];
        }
    };
}