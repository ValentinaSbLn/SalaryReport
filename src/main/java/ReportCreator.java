import bilderreport.ReportBuilder;
import dao.QueryReader;
import domain.DataForReportSalary;
import notifier.Notifier;


public interface ReportCreator {

    QueryReader createData();
    ReportBuilder createReport(DataForReportSalary dataReport);
    Notifier createNotifier(String host);
}
