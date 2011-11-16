package info.micdm.munin_client.events.types;

import info.micdm.munin_client.data.Server;

/**
 * Список нод недоступен.
 * @author Mic, 2011
 *
 */
public class NodeListNotAvailableEvent extends Event {

    /**
     * Сервер.
     */
    private Server _server;

    public NodeListNotAvailableEvent(Server server) {
        _server = server;
    }

    public Server getServer() {
        return _server;
    }
}
