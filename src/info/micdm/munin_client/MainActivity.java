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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends CustomActivity {

    protected void _loadByHour() {
        Server server = ServerList.getInstance().getServer("main");
        Node node = server.getNode("localdomain", "localhost.localdomain");
        ReportLoader.getInstance().load(server, node, Report.Type.LOAD, Report.Period.HOUR);
    }
    
    protected void _loadByDay() {
        Server server = ServerList.getInstance().getServer("main");
        Node node = server.getNode("localdomain", "localhost.localdomain");
        ReportLoader.getInstance().load(server, node, Report.Type.LOAD, Report.Period.DAY);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.getByHour).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _loadByHour();
            }
        });
        
        findViewById(R.id.getByDay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _loadByDay();
            }
        });
        
        _loadByHour();
    }
    
    @Override
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
}
