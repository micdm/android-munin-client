package info.micdm.munin_client.models;

import java.util.Collection;
import java.util.HashMap;

/**
 * Список серверов.
 * @author Mic, 2011
 *
 */
public class ServerList {

    /**
     * Синглтон.
     */
    protected final static ServerList _instance = new ServerList();
    
    /**
     * Синглтон.
     */
    public static ServerList getInstance() {
        return _instance;
    }
    
    /**
     * Список серверов.
     */
    protected HashMap<String, Server> _servers = new HashMap<String, Server>();
    
    protected ServerList() {
        _servers.put("mic-dm.tom.ru", new Server("mic-dm.tom.ru", 82));
    }
    
    /**
     * Возвращает сервер.
     */
    public Server getServer(String name) {
        return _servers.get(name);
    }
    
    /**
     * Возвращает список всех серверов.
     */
    public Collection<Server> getServers() {
        return _servers.values();
    }
}
