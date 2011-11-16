package info.micdm.munin_client.events.types;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Server;

import java.util.ArrayList;

/**
 * Список нод загружен.
 * @author Mic, 2011
 *
 */
public class NodeListLoadedEvent extends Event {

    /**
     * Сервер.
     */
    private Server _server;
    
    /**
     * Список нод.
     * Может быть null, если не загрузился.
     */
    public ArrayList<Node> _nodes; 
    
    public NodeListLoadedEvent(Server server, ArrayList<Node> nodes) {
        _server = server;
        _nodes = nodes;
    }

    public Server getServer() {
        return _server;
    }
    
    public ArrayList<Node> getNodes() {
        return _nodes;
    }
}
