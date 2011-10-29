package info.micdm.munin_client.tasks;

import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.models.Node;
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
import android.os.AsyncTask;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

/**
 * Парсер данных для отчета.
 * @author Mic, 2011
 *
 */
class ReportParser {
    
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
        _setupRow(root.getChild("data"));
        return root;
    }
    
    /**
     * Парсит данные, заполняет отчет.
     */
    public void parse(Report report, String data) {
        try {
            _report = report;
            RootElement root = _setupRootElement();
            Xml.parse(data, root.getContentHandler());
        } catch (SAXException e) {
            Log.e(toString(), "can not parse data: " + e.toString());
        }
    }
}


/**
 * Загрузчик данных.
 * @author Mic, 2011
 *
 */
public class DownloadReportTask extends AsyncTask<Void, Void, Report> {

    /**
     * Сервер, с которого грузим данные.
     */
    protected Server _server;
    
    /**
     * Нода, для которой нужен отчет.
     */
    protected Node _node;
    
    /**
     * Тип отчета.
     */
    protected Report.Type _type;
    
    /**
     * Период отчета.
     */
    protected Report.Period _period;
    
    public DownloadReportTask(Server server, Node node, Report.Type type, Report.Period period) {
        _server = server;
        _node = node;
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

    @Override
    protected Report doInBackground(Void... params) {
        String data = _downloadData();
        if (data == null) {
            return null;
        }
        ReportParser parser = new ReportParser();
        Report report = new Report(_type, _period);
        parser.parse(report, data);
        Log.d(toString(), "parsed: " + report);
        return report;
    }
    
    @Override
    protected void onPostExecute(Report report) {
        Event event = new Event(Event.Type.REPORT_AVAILABLE, report);
        EventDispatcher.dispatch(event);
    }
}
