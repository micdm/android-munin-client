package info.micdm.munin_client.models;

import java.util.ArrayList;

/**
 * Список серверов.
 * @author Mic, 2011
 *
 */
public class ServerList {

    /**
     * Список серверов.
     */
    protected ArrayList<Server> _servers;
    
    /**
     * Возвращает сервер.
     */
    public Server getServer() {
        return new Server("192.168.1.3");
    }
}
