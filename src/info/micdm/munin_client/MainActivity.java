package info.micdm.munin_client;

import info.micdm.munin_client.custom.CustomActivity;
import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventExtra;
import info.micdm.munin_client.graph.GraphView;
import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.reports.Report;
import info.micdm.munin_client.reports.ReportLoader;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends CustomActivity {

    protected void _loadByHour() {
        Server server = new Server("192.168.1.3", 82);
        ReportLoader.load(server, ReportLoader.Type.LOAD, ReportLoader.Period.HOUR);
    }
    
    protected void _loadByDay() {
        Server server = new Server("192.168.1.3", 82);
        ReportLoader.load(server, ReportLoader.Type.LOAD, ReportLoader.Period.DAY);
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
    }
    
    @Override
    protected void _onEvent(Event event, EventExtra extra) {
        if (event.equals(Event.REPORT_LOADED)) {
            Report report = (Report)extra;
            GraphView view = (GraphView)findViewById(R.id.graph);
            view.setReport(report);
        }
    }
}
