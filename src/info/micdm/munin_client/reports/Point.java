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
    protected int _time;
    
    /**
     * Значение в точке.
     */
    protected float _value;
    
    public String toString() {
        return "point (" + _time + ":" + _value + ")";
    }
    
    /**
     * Устанавливает время в точке.
     */
    public void setTime(int value) {
        _time = value;
    }
    
    /**
     * Возвращает время в точке.
     */
    public int getTime() {
        return _time;
    }
    
    /**
     * Устанавливает значение в точке.
     */
    public void setValue(float value) {
        _value = value;
    }
    
    /**
     * Возвращает значение в точке.
     */
    public float getValue() {
        return _value;
    }
}
