package info.micdm.munin_client;

import info.micdm.munin_client.custom.CustomActivity;
import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.tasks.DownloadDataTask;
import android.os.Bundle;

public class MainActivity extends CustomActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Server server = new Server("192.168.1.3", 82);
        DownloadDataTask task = new DownloadDataTask(server);
        task.execute();
    }
}
