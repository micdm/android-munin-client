package info.micdm.munin_client.events;

/**
 * Событие.
 * @author Mic, 2011
 *
 */
public class Event {

    /**
     * Типы событий.
     * @author Mic, 2011
     *
     */
    public enum Type {
        REPORT_LOADED,
        REPORT_AVAILABLE
    }
    
    /**
     * Тип события.
     */
    protected Type _type;
    
    /**
     * Дополнительные данные события.
     */
    protected Object _extra;
    
    public Event(Type type, Object extra) {
        _type = type;
        _extra = extra;
    }
    
    /**
     * Возвращает тип события.
     */
    public Type getType() {
        return _type;
    }
    
    /**
     * Возвращает дополнительные данные события.
     */
    public Object getExtra() {
        return _extra;
    }
}
