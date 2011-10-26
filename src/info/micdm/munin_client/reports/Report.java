package info.micdm.munin_client.reports;

import java.util.ArrayList;

public class Report {

    protected ArrayList<Point> _points;
    
    public String toString() {
        if (_points == null) {
            return "report with no points";
        }
        return "report with " + _points.size() + " points";
    }
    
    public ArrayList<Point> getPoints() {
        return _points;
    }
    
    public void addPoint(Point point) {
        if (_points == null) {
            _points = new ArrayList<Point>();
        }
        _points.add(point);
    }
}
