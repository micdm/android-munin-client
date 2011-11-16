package info.micdm.munin_client.events.types;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Report;
import info.micdm.munin_client.data.Server;

/**
 * Отчет доступен.
 * @author Mic, 2011
 *
 */
public class ReportAvailableEvent extends Event {

    /**
     * Сервер.
     */
    private Server _server;
    
    /**
     * Нода.
     */
    public Node _node;
    
    /**
     * Отчет.
     * Может быть null, если не загрузился.
     */
    public Report _report;
    
    public ReportAvailableEvent(Server server, Node node, Report report) {
        _server = server;
        _node = node;
        _report = report;
    }

    public Server getServer() {
        return _server;
    }
    
    public Node getNode() {
        return _node;
    }
    
    public Report getReport() {
        return _report;
    }
}
