package info.micdm.munin_client.tasks;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Point;
import info.micdm.munin_client.data.Report;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.types.ReportLoadedEvent;
import info.micdm.utils.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
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
                _point.setTime(Float.parseFloat(body));
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
    public void parse(Report report, String data) throws RuntimeException {
        try {
            _report = report;
            RootElement root = _setupRootElement();
            Xml.parse(data, root.getContentHandler());
        } catch (SAXException e) {
            if (Log.isEnabled) {
                Log.error("can not parse data: " + e.toString());
            }
            throw new RuntimeException("can not parse data");
        }
    }
}


/**
 * Загрузчик отчетов.
 * @author Mic, 2011
 *
 */
public class DownloadReportTask extends DownloadTask<Void, Void, Report> {

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
    
    @Override
    protected String _getUri() {
        return _server.getUri() + "/report/" + _node.getName() + "/" + _type + "/" + _period + "/";
    }

    @Override
    protected Report doInBackground(Void... params) {
        String data = _downloadData();
        if (data == null) {
            return null;
        }
        ReportParser parser = new ReportParser();
        try {
            Report report = new Report(_type, _period);
            parser.parse(report, data);
            if (Log.isEnabled) {
                Log.debug("parsed: " + report);
            }
            return report;
        } catch (RuntimeException e) {
            return null;
        }
    }
    
    @Override
    protected void onPostExecute(Report report) {
        ReportLoadedEvent event = new ReportLoadedEvent(_server, _node, report);
        EventDispatcher.INSTANCE.dispatch(event);
    }
}
