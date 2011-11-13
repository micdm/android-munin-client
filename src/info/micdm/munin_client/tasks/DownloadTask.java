package info.micdm.munin_client.tasks;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import info.micdm.utils.Log;

/**
 * Абстрактный загрузчик данных с веб-сервера.
 * @author Mic, 2011
 *
 */
public abstract class DownloadTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    /**
     * Возвращает адрес страницы, которую надо скачать.
     */
    protected abstract String _getUri();
    
    /**
     * Загружает данные.
     */
    protected String _downloadData() {
        try {
            String uri = _getUri();
            if (Log.isEnabled) {
                Log.debug("downloading page " + uri);
            }
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android Munin Client");
            HttpGet request = new HttpGet(uri);
            BasicHttpResponse response = (BasicHttpResponse)client.execute(request);
            String body = EntityUtils.toString(response.getEntity(), "utf8");
            client.close();
            if (Log.isEnabled) {
                Log.debug("downloaded: " + body.length());
            }
            return body;
        } catch (IOException e) {
            if (Log.isEnabled) {
                Log.error("failed to download page: " + e.toString());
            }
            return null;
        }
    }
}
