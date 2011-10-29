package info.micdm.munin_client.models;

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
    
    /**
     * Возвращает сервер.
     */
    public Server getServer(String name) {
        if (!_servers.containsKey(name)) {
            if (name.equals("main")) {
                _servers.put(name, new Server("192.168.1.3", 82));
            }
        }
        return _servers.get(name);
    }
}
