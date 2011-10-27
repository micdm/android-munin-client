package info.micdm.munin_client.reports;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Точка в данных.
 * @author Mic, 2011
 *
 */
public class Point implements Parcelable {

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel source) {
            return new Point(source);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };
    
    /**
     * Время в точке.
     */
    protected Integer _time;
    
    /**
     * Значение в точке.
     */
    protected Float _value;
    
    public Point() {
        
    }
    
    public Point(Parcel source) {
        _readFromParcel(source);
    }
    
    public String toString() {
        return "point (" + _time + ":" + _value + ")";
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_time);
        dest.writeFloat(_value);
    }
    
    protected void _readFromParcel(Parcel source) {
        _time = source.readInt();
        _value = source.readFloat();
    }
    
    /**
     * Устанавливает время в точке.
     */
    public void setTime(Integer value) {
        _time = value;
    }
    
    /**
     * Устанавливает значение в точке.
     */
    public void setValue(Float value) {
        _value = value;
    }
}
