package info.micdm.munin_client.reports;

import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.tasks.DownloadReportTask;

/**
 * Загрузчик отчетов.
 * @author Mic, 2011
 *
 */
public class ReportLoader {

    /**
     * Типы отчетов.
     * @author Mic, 2011
     *
     */
    public enum Type {
        LOAD,
        USERS;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    /**
     * Периоды отчетов.
     * @author Mic, 2011
     *
     */
    public enum Period {
        HOUR,
        DAY;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    /**
     * Загружает отчет с указанного сервера.
     */
    public static void load(Server server, Type type, Period period) {
        DownloadReportTask task = new DownloadReportTask(server, type.toString(), period.toString());
        task.execute();
    }
}
