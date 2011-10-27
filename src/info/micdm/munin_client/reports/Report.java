package info.micdm.munin_client.reports;

import info.micdm.munin_client.events.EventExtra;

import java.util.ArrayList;

import android.os.Parcel;

/**
 * Набор данных для визуализации.
 * @author Mic, 2011
 *
 */
public class Report implements EventExtra {

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel source) {
            return new Report(source);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
    
    /**
     * Набор точек.
     */
    protected ArrayList<Point> _points = new ArrayList<Point>();
    
    public Report() {
        
    }
    
    public Report(Parcel source) {
        _readFromParcel(source);
    }
    
    public String toString() {
        if (_points == null) {
            return "report with no points";
        }
        return "report with " + _points.size() + " points";
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(_points);
    }
    
    protected void _readFromParcel(Parcel source) {
        source.readTypedList(_points, Point.CREATOR);
    }
    
    public ArrayList<Point> getPoints() {
        return _points;
    }
    
    public void addPoint(Point point) {
        _points.add(point);
    }
}
