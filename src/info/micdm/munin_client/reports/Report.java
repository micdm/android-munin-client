package info.micdm.munin_client.reports;

import java.util.ArrayList;
import java.util.Date;

/**
 * Набор данных для визуализации.
 * @author Mic, 2011
 *
 */
public class Report {

    /**
     * Время жизни отчета (в секундах).
     */
    protected final int LIFETIME = 300;
    
    /**
     * Типы отчетов.
     * @author Mic, 2011
     *
     */
    public enum Type {
        LOAD,
        USERS;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    /**
     * Периоды отчетов.
     * @author Mic, 2011
     *
     */
    public enum Period {
        HOUR,
        DAY;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    /**
     * Дата загрузки отчета.
     */
    protected Date _loaded;
    
    /**
     * Тип отчета.
     */
    protected Type _type;
    
    /**
     * Период отчета.
     */
    protected Period _period;
    
    /**
     * Набор точек.
     */
    protected ArrayList<Point> _points = new ArrayList<Point>();
    
    public Report(Type type, Period period) {
        _loaded = new Date();
        _type = type;
        _period = period;
    }
    
    @Override
    public String toString() {
        if (_points == null) {
            return "report with no points";
        }
        return "report with " + _points.size() + " points";
    }
    

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        Report report = (Report)o;
        return report.getType().equals(_type) && report.getPeriod().equals(_period);
    }
    
    /**
     * Возвращает, устарел ли отчет.
     */
    public boolean isOutdated() {
        return new Date().getTime() - _loaded.getTime() > LIFETIME * 1000;
    }
    
    /**
     * Возвращает тип отчета.
     */
    public Type getType() {
        return _type;
    }
    
    /**
     * Возвращает период отчета.
     */
    public Period getPeriod() {
        return _period;
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
        if (point.getValue() != Float.NaN) {
            _points.add(point);
        }
    }
    
    /**
     * Возвращает время начала отчета.
     */
    public Point getStart() {
        return _points.get(0);
    }
    
    /**
     * Возвращает время окончания отчета.
     */
    public Point getEnd() {
        return _points.get(_points.size() - 1);
    }
    
    /**
     * Возвращает минимальную точку.
     */
    public Point getMin() {
        Point min = _points.get(0);
        for (Point point: _points) {
            if (point.getValue() < min.getValue()) {
                min = point;
            }
        }
        return min;
    }
    
    /**
     * Возвращает максимальную точку.
     */
    public Point getMax() {
        Point max = _points.get(0);
        for (Point point: _points) {
            if (point.getValue() > max.getValue()) {
                max = point;
            }
        }
        return max;
    }
}
