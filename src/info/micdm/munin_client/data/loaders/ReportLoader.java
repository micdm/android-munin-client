package info.micdm.munin_client.data.loaders;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Report;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.events.types.Event;
import info.micdm.munin_client.events.types.ReportAvailableEvent;
import info.micdm.munin_client.events.types.ReportLoadedEvent;
import info.micdm.munin_client.events.types.ReportNotAvailableEvent;
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
            EventDispatcher.INSTANCE.dispatch(new ReportNotAvailableEvent(server, node));
        } else {
            node.addReport(report);
            EventDispatcher.INSTANCE.dispatch(new ReportAvailableEvent(server, node, report));
        }
    }
    
    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.INSTANCE.addListener(ReportLoadedEvent.class, new EventListener(this) {
            @Override
            public void notify(Event event) {
                ReportLoadedEvent typed = (ReportLoadedEvent)event;
                _onReportLoaded(typed.getServer(), typed.getNode(), typed.getReport());
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
            EventDispatcher.INSTANCE.dispatch(new ReportAvailableEvent(server, node, report));
        }
    }
}
