package info.micdm.munin_client.reports;


/**
 * Точка в данных.
 * @author Mic, 2011
 *
 */
public class Point {

    /**
     * Время в точке.
     */
    protected Integer _time;
    
    /**
     * Значение в точке.
     */
    protected Float _value;
    
    public String toString() {
        return "point (" + _time + ":" + _value + ")";
    }
    
    /**
     * Устанавливает время в точке.
     */
    public void setTime(Integer value) {
        _time = value;
    }
    
    /**
     * Возвращает время в точке.
     */
    public Integer getTime() {
        return _time;
    }
    
    /**
     * Устанавливает значение в точке.
     */
    public void setValue(Float value) {
        _value = value;
    }
    
    /**
     * Возвращает значение в точке.
     */
    public Float getValue() {
        return _value;
    }
}
