package info.micdm.munin_client.events.types;

import info.micdm.munin_client.data.Server;

/**
 * Список нод доступен.
 * @author Mic, 2011
 *
 */
public class NodeListAvailableEvent extends Event {

    /**
     * Сервер.
     */
    private Server _server;

    public NodeListAvailableEvent(Server server) {
        _server = server;
    }

    public Server getServer() {
        return _server;
    }
}
