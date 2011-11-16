package info.micdm.munin_client.events.types;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Server;

/**
 * Отчет недоступен.
 * @author Mic, 2011
 *
 */
public class ReportNotAvailableEvent extends Event {

    /**
     * Сервер.
     */
    private Server _server;
    
    /**
     * Нода.
     */
    public Node _node;
    
    public ReportNotAvailableEvent(Server server, Node node) {
        _server = server;
        _node = node;
    }

    public Server getServer() {
        return _server;
    }
    
    public Node getNode() {
        return _node;
    }
}
