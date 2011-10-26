package info.micdm.munin_client.models;

import java.util.ArrayList;

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
    protected ArrayList<Node> _nodes;
    
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
    public Node getNode() {
        return new Node("localhost.localdomain");
    }
}
