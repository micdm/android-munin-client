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
}
