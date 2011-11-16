package info.micdm.munin_client.events;

import info.micdm.munin_client.events.types.Event;
import info.micdm.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Диспетчер событий. Следит за рассылкой, подпиской и отпиской.
 * @author Mic, 2011
 *
 */
public class EventDispatcher {
    
    public final static EventDispatcher INSTANCE = new EventDispatcher();
    
    /**
     * Сгруппированные по типу события слушатели.
     */
    protected HashMap<Class<?>, ArrayList<EventListener>> _listeners = new HashMap<Class<?>, ArrayList<EventListener>>();
    
    /**
     * Рассылает событие.
     */
    public void dispatch(Event event) {
        if (Log.isEnabled) {
            Log.debug("dispatching event " + event.toString());
        }
        ArrayList<EventListener> listeners = _listeners.get(event.getClass());
        if (listeners != null) {
            for (EventListener listener: listeners) {
                listener.notify(event);
            }
        }
    }
    
    /**
     * Добавляет слушателя.
     */
    public void addListener(Class<?> eventType, EventListener listener) {
        if (!_listeners.containsKey(eventType)) {
            _listeners.put(eventType, new ArrayList<EventListener>());
        }
        _listeners.get(eventType).add(listener);
    }
    
    /**
     * Удаляет слушателей для получателя.
     */
    protected void _removeListenersByRecipientHash(Class<?> eventType, int hash) {
        ArrayList<EventListener> listeners = _listeners.get(eventType);
        for (int i = listeners.size() - 1; i >= 0; i -= 1) {
            if (listeners.get(i).getRecipient() == hash) {
                listeners.remove(i);
            }
        }
    }
    
    /**
     * Удаляет слушателя.
     */
    public void removeListener(Object recipient, Class<?> eventType) {
        if (!_listeners.containsKey(eventType)) {
            if (Log.isEnabled) {
                Log.warning("no listeners for event " + eventType);
            }
        } else {
            int hash = EventListener.getRecipientHash(recipient);
            _removeListenersByRecipientHash(eventType, hash);
        }
    }
    
    /**
     * Удаляет всех слушателей.
     */
    public void removeAllListeners(Object recipient) {
        int hash = EventListener.getRecipientHash(recipient);
        for (Class<?> eventType: _listeners.keySet()) {
            _removeListenersByRecipientHash(eventType, hash);
        }
    }
}
