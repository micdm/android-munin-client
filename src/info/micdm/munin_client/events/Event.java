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
        NODE_LIST_LOADED,
        NODE_LIST_AVAILABLE,
        NODE_LIST_NOT_AVAILABLE,
        REPORT_LOADED,
        REPORT_AVAILABLE,
        REPORT_NOT_AVAILABLE
    }
    
    /**
     * Тип события.
     */
    protected Type _type;
    
    /**
     * Дополнительные данные события.
     */
    protected Object[] _extra;
    
    public Event(Type type, Object... extra) {
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
    public Object[] getExtra() {
        return _extra;
    }
}
