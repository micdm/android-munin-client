package info.micdm.munin_client;

/**
 * События для приложения.
 * @author Mic, 2011
 *
 */
public enum Event {

    /**
     * Данные загружены.
     */
    REPORT_LOADED;

    public String toString() {
        return "info.micdm.munin_client." + name().toLowerCase();
    }
    
    /**
     * Находит событие по его типу.
     */
    public static Event get(String eventString) {
        for (Event event: values()) {
            if (event.toString().equals(eventString)) {
                return event;
            }
        }
        return null;
    }
}
