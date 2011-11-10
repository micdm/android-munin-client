package info.micdm.munin_client;

import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.graph.GraphView;
import info.micdm.munin_client.models.Node;
import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.models.ServerList;
import info.micdm.munin_client.reports.Report;
import info.micdm.munin_client.reports.ReportLoader;
import android.app.Activity;
import android.os.Bundle;

/**
 * Экран с информацией о ноде.
 * @author Mic, 2011
 *
 */
public class NodeActivity extends Activity {

    /**
     * Отображаемый сервер.
     */
    protected Server _server;
    
    /**
     * Отображаемая нода.
     */
    protected Node _node;
    
    /**
     * Запоминает сервер и ноду для отображения.
     */
    protected void _setServerAndNode(Bundle bundle) {
        String serverName = bundle.getString("server");
        String nodeName = bundle.getString("node");
        _server = ServerList.INSTANCE.get(serverName);
        _node = _server.getNode(nodeName);
    }
    
    /**
     * Загружает отчет за час.
     */
    protected void _loadByHour() {
        ReportLoader.INSTANCE.load(_server, _node, Report.Type.LOAD, Report.Period.HOUR);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.node);
        _setServerAndNode(getIntent().getExtras());
    }
    
    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.addListener(Event.Type.REPORT_AVAILABLE, new EventListener(this) {
            @Override
            public void notify(Event event) {
                Object[] extra = event.getExtra();
                Report report = (Report)extra[0];
                GraphView view = (GraphView)findViewById(R.id.graph);
                view.setReport(report);
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        _addListeners();
        _loadByHour();
    }
    
    /**
     * Удаляет все слушатели событий.
     */
    protected void _removeListeners() {
        EventDispatcher.removeAllListeners(this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        _removeListeners();
    }
}
