import bilderreport.ReportBuilder;
import dao.QueryReader;
import datareport.DataForReportSalary;
import notifier.Notifier;


public interface ReportCreator {

    QueryReader createData();
    Notifier createNotifier();
}
