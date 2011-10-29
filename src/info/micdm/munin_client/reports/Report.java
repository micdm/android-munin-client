package info.micdm.munin_client.reports;

import java.util.ArrayList;

/**
 * Набор данных для визуализации.
 * @author Mic, 2011
 *
 */
public class Report {

    /**
     * Набор точек.
     */
    protected ArrayList<Point> _points = new ArrayList<Point>();
    
    public String toString() {
        if (_points == null) {
            return "report with no points";
        }
        return "report with " + _points.size() + " points";
    }
    
    /**
     * Возвращает список точек.
     */
    public ArrayList<Point> getPoints() {
        return _points;
    }
    
    /**
     * Добавляет точку.
     */
    public void addPoint(Point point) {
        if (!point.getValue().equals(Float.NaN)) {
            _points.add(point);
        }
    }
    
    /**
     * Возвращает время начала отчета.
     */
    public Integer getStartTime() {
        return _points.get(0).getTime();
    }
    
    /**
     * Возвращает время окончания отчета.
     */
    public Integer getEndTime() {
        return _points.get(_points.size() - 1).getTime();
    }
    
    /**
     * Находит максимальное значение среди точек.
     */
    public Float getMaxValue() {
        Float result = Float.NEGATIVE_INFINITY;
        for (Point point: _points) {
            result = result > point.getValue() ? result : point.getValue();
        }
        return result;
    }
}
