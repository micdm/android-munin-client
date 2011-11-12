package info.micdm.munin_client.reports;

import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.models.Node;
import info.micdm.munin_client.models.Server;
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
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.addListener(Event.Type.REPORT_LOADED, new EventListener(this) {
            @Override
            public void notify(Event event) {
                Object[] extra = event.getExtra();
                Node node = (Node)extra[1];
                Report report = (Report)extra[2];
                node.addReport(report);
                EventDispatcher.dispatch(new Event(Event.Type.REPORT_AVAILABLE, node, report));
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
