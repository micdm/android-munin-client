package info.micdm.munin_client.data;


import java.util.ArrayList;

/**
 * Ведомый сервер ("нода").
 * @author Mic, 2011
 *
 */
public class Node {

    /**
     * Название ноды.
     */
    protected String _name;
    
    /**
     * Список доступных для просмотра типов отчетов.
     */
    protected ArrayList<Report.Type> _reportTypes = new ArrayList<Report.Type>();
    
    /**
     * Отчеты для ноды.
     */
    protected ArrayList<Report> _reports = new ArrayList<Report>();
    
    public Node(String name) {
        _name = name;
    }
    
    @Override
    public String toString() {
        return _name;
    }
    
    /**
     * Возвращает название ноды.
     */
    public String getName() {
        return _name;
    }
    
    /**
     * Возвращает список доступных типов отчетов.
     */
    public ArrayList<Report.Type> getReportTypes() {
        return _reportTypes;
    }
    
    /**
     * Добавляет тип отчета.
     */
    public void addReportType(Report.Type reportType) {
        _reportTypes.add(reportType);
    }

    /**
     * Находит отчет.
     */
    public Report getReport(Report.Type type, Report.Period period) {
        for (Report report: _reports) {
            if (report.getType().equals(type) && report.getPeriod().equals(period)) {
                return report;
            }
        }
        return null;
    }

    /**
     * Добавляет новый отчет в список либо перезаписывает старый такой же.
     */
    public void addReport(Report report) {
        int index = _reports.indexOf(report);
        if (index == -1) {
            _reports.add(report);
        } else {
            _reports.set(index, report);
        }
    }
}
