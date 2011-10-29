package info.micdm.munin_client;

import info.micdm.munin_client.custom.CustomActivity;
import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventExtra;
import info.micdm.munin_client.graph.GraphView;
import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.reports.Report;
import info.micdm.munin_client.tasks.DownloadDataTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends CustomActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(toString(), findViewById(R.id.graph).toString());
        
        Server server = new Server("192.168.1.3", 82);
        DownloadDataTask task = new DownloadDataTask(server);
        task.execute();
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
