package info.micdm.munin_client.models;

import java.util.HashMap;

/**
 * Сервер с мониторингом.
 * @author Mic, 2011
 *
 */
public class Server {

    /**
     * Имя хоста сервера.
     */
    protected String _host;
    
    /**
     * Порт сервера.
     */
    protected Integer _port;
    
    /**
     * Список ведомых серверов.
     */
    protected HashMap<String, Node> _nodes = new HashMap<String, Node>();
    
    public Server(String host, Integer port) {
        _host = host;
        _port = port;
    }
    
    public Server(String host) {
        this(host, 80);
    }
    
    /**
     * Возвращает хост.
     */
    public String getHost() {
        return _host;
    }
    
    /**
     * Возвращает порт.
     */
    public Integer getPort() {
        return _port;
    }
    
    /**
     * Возвращает ноду.
     */
    public Node getNode(String name) {
        if (!_nodes.containsKey(name)) {
            _nodes.put(name, new Node(name));
        }
        return _nodes.get(name);
    }
}
