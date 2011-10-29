package info.micdm.munin_client.tasks;

import info.micdm.munin_client.custom.CustomApplication;
import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.reports.Point;
import info.micdm.munin_client.reports.Report;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.net.http.AndroidHttpClient;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

/**
 * Парсер данных.
 * @author Mic, 2011
 *
 */
class DataParser {
    
    /**
     * Промежуточная переменная для отчета.
     */
    protected Report _report;
    
    /**
     * Промежуточная переменная для точки графика.
     */
    protected Point _point;
    
    /**
     * Настраивает парсер для времени точки.
     */
    protected void _setupPointTime(Element row) {
        Element time = row.getChild("t");
        time.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                _point.setTime(Integer.parseInt(body));
            }
        });
    }
    
    /**
     * Настраивает парсер для значения точки.
     */
    protected void _setupPointValue(Element row) {
        Element value = row.getChild("v");
        value.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                _point.setValue(Float.parseFloat(body));
            }
        });
    }
    
    /**
     * Настраивает парсер для точки.
     */
    protected void _setupRow(Element rows) {
        Element row = rows.getChild("row");
        row.setElementListener(new ElementListener() {
            @Override
            public void start(Attributes attributes) {
                _point = new Point();
            }

            @Override
            public void end() {
                _report.addPoint(_point);
                _point = null;
            }
        });
        _setupPointTime(row);
        _setupPointValue(row);
    }
    
    /**
     * Настраивает парсер для корневого элемента.
     */
    protected RootElement _setupRootElement() {
        RootElement root = new RootElement("xport");
        root.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                _report = new Report();
            }
        });
        _setupRow(root.getChild("data"));
        return root;
    }
    
    /**
     * Парсит данные.
     */
    public Report parse(String data) {
        try {
            RootElement root = _setupRootElement();
            Xml.parse(data, root.getContentHandler());
            return _report;
        } catch (SAXException e) {
            Log.e(toString(), "can not parse data: " + e.toString());
            return null;
        }
    }
}


/**
 * Загрузчик данных.
 * @author Mic, 2011
 *
 */
public class DownloadDataTask {

    /**
     * Сервер, с которого грузим данные.
     */
    protected Server _server;
    
    /**
     * Тип отчета.
     */
    protected String _type;
    
    /**
     * Период отчета.
     */
    protected String _period;
    
    public DownloadDataTask(Server server, String type, String period) {
        _server = server;
        _type = type;
        _period = period;
    }
    
    /**
     * Возвращает адрес страницы, которую надо скачать.
     */
    protected String _getUri() {
        String url = "http://" + _server.getHost() + ":" + _server.getPort();
        String urn = "/munin-export/export.py?type=" + _type + "&period=" + _period;
        return url + urn;
    }
    
    /**
     * Загружает данные.
     */
    protected String _downloadData() {
        try {
            String uri = _getUri();
            Log.d(toString(), "downloading page " + uri);
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android Munin Client");
            HttpGet request = new HttpGet(uri);
            BasicHttpResponse response = (BasicHttpResponse)client.execute(request);
            String body = EntityUtils.toString(response.getEntity(), "utf8");
            client.close();
            Log.d(toString(), "downloaded: " + body.length());
            return body;
        } catch (IOException e) {
            Log.e(toString(), "failed to download page: " + e.toString());
            return null;
        }
    }
    
    /**
     * Запускается в отдельном потоке, делает всю работу.
     */
    protected void _runOnThread() {
        String data = _downloadData();
        if (data != null) {
            DataParser parser = new DataParser();
            Report report = parser.parse(data);
            Log.d(toString(), "parsed: " + report);
            Log.d(toString(), report.getPoints().toString());
            CustomApplication.sendEvent(Event.REPORT_LOADED, report);
        }
    }
    
    /**
     * Запускает задачу.
     */
    public void execute() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                _runOnThread();
            }
        });
        thread.start();
    }
}
