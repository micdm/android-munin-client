package info.micdm.munin_client.reports;

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
     * Загружает отчет с указанного сервера.
     */
    public static void load(Server server, Node node, Report.Type type, Report.Period period) {
        DownloadReportTask task = new DownloadReportTask(server, node, type, period);
        task.execute();
    }
}
