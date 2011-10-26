package info.micdm.munin_client;

import info.micdm.munin_client.custom.CustomActivity;
import android.os.Bundle;

public class MainActivity extends CustomActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
