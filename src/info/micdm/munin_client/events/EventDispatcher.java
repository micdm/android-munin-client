package info.micdm.munin_client.events;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

/**
 * Диспетчер событий. Следит за рассылкой, подпиской и отпиской.
 * @author Mic, 2011
 *
 */
public class EventDispatcher {
    
    /**
     * Сгруппированные по типу события слушатели.
     */
    protected static HashMap<Event.Type, ArrayList<EventListener>> _listeners = new HashMap<Event.Type, ArrayList<EventListener>>();
    
    /**
     * Рассылает событие.
     */
    public static void dispatch(Event event) {
        Log.d("", "dispatching event " + event.getType());
        ArrayList<EventListener> listeners = _listeners.get(event.getType());
        if (listeners != null) {
            for (EventListener listener: listeners) {
                listener.notify(event);
            }
        }
    }
    
    /**
     * Добавляет слушателя.
     */
    public static void addListener(Event.Type event, EventListener listener) {
        if (!_listeners.containsKey(event)) {
            _listeners.put(event, new ArrayList<EventListener>());
        }
        _listeners.get(event).add(listener);
    }
    
    protected static void _removeListenersByRecipientHash(Event.Type event, int hash) {
        ArrayList<EventListener> listeners = _listeners.get(event);
        for (int i = listeners.size() - 1; i >= 0; i -= 1) {
            if (listeners.get(i).getRecipient() == hash) {
                listeners.remove(i);
            }
        }
    }
    
    /**
     * Удаляет слушателя.
     */
    public static void removeListener(Object recipient, Event.Type event) {
        if (!_listeners.containsKey(event)) {
            Log.w("", "no listeners for event " + event);
        } else {
            int hash = EventListener.getRecipientHash(recipient);
            _removeListenersByRecipientHash(event, hash);
        }
    }
    
    /**
     * Удаляет всех слушателей.
     */
    public static void removeAllListeners(Object recipient) {
        int hash = EventListener.getRecipientHash(recipient);
        for (Event.Type event: _listeners.keySet()) {
            _removeListenersByRecipientHash(event, hash);
        }
    }
}
