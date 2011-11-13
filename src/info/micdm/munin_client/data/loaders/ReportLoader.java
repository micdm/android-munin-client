package info.micdm.munin_client.data.loaders;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Report;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.tasks.DownloadReportTask;

/**
 * Загрузчик отчетов.
 * @author Mic, 2011
 *
 */
public class ReportLoader {

    /**
     * Синглтон.
     */
    public final static ReportLoader INSTANCE = new ReportLoader();

    public ReportLoader() {
        _addListeners();
    }
    
    /**
     * Выполняется, когда загрузится отчет.
     */
    protected void _onReportLoaded(Server server, Node node, Report report) {
        if (report == null) {
            EventDispatcher.dispatch(new Event(Event.Type.REPORT_NOT_AVAILABLE, node));
        } else {
            node.addReport(report);
            EventDispatcher.dispatch(new Event(Event.Type.REPORT_AVAILABLE, node, report));
        }
    }
    
    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.addListener(Event.Type.REPORT_LOADED, new EventListener(this) {
            @Override
            public void notify(Event event) {
                Object[] extra = event.getExtra();
                _onReportLoaded((Server)extra[0], (Node)extra[1], (Report)extra[2]);
            }
        });
    }
    
    /**
     * Загружает отчет с указанного сервера.
     */
    public void load(Server server, Node node, Report.Type type, Report.Period period) {
        Report report = node.getReport(type, period);
        if (report == null || report.isOutdated()) {
            DownloadReportTask task = new DownloadReportTask(server, node, type, period);
            task.execute();
        } else {
            EventDispatcher.dispatch(new Event(Event.Type.REPORT_AVAILABLE, node, report));
        }
    }
}
