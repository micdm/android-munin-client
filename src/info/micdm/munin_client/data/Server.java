package info.micdm.munin_client.data;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;

/**
 * Сервер с мониторингом.
 * @author Mic, 2011
 *
 */
public class Server {

    /**
     * Адрес, где находится экспортер.
     */
    protected URI _uri;
    
    /**
     * Список ведомых серверов.
     */
    protected HashMap<String, Node> _nodes = null;
    
    public Server(URI uri) {
        _uri = uri;
    }

    @Override
    public String toString() {
        return _uri.toString();
    }
    
    /**
     * Возвращает URI.
     */
    public URI getUri() {
        return _uri;
    }
    
    /**
     * Возвращает имя сервера.
     */
    public String getName() {
        return _uri.toString();
    }
    
    /**
     * Возвращает ноду.
     */
    public Node getNode(String name) {
        return _nodes.get(name);
    }
    
    /**
     * Возвращает список из всех нод.
     * Если ноды пока не добавлены, возвращает null.
     */
    public Collection<Node> getNodes() {
        return _nodes == null ? null : _nodes.values();
    }
    
    /**
     * Добавляет ноду.
     */
    public void addNode(Node node) {
        if (_nodes == null) {
            _nodes = new HashMap<String, Node>();
        }
        _nodes.put(node.getName(), node);
    }
}
