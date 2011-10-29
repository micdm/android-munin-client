package info.micdm.munin_client.events;

/**
 * Слушатель событий.
 * @author Mic, 2011
 *
 */
public abstract class EventListener {

    /**
     * Получатель события.
     */
    protected Integer _recipient;
    
    public EventListener(Object recipient) {
        _recipient = getRecipientHash(recipient);
    }
    
    /**
     * Вычисляет хэш получателя.
     */
    public static Integer getRecipientHash(Object recipient) {
        return recipient.hashCode();
    }
    
    /**
     * Возвращает своего получателя.
     */
    public Integer getRecipient() {
        return _recipient;
    }
    
    /**
     * Уведомляет получателя.
     */
    public abstract void notify(Event event);
}
