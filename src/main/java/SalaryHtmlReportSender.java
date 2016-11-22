import bilderreport.ReportBuilder;
import bilderreport.html.HTMLReportBuilder;
import dao.QueryReader;
import datareport.DataForReportSalary;
import notifier.Notifier;
import utils.Range;


public class SalaryHtmlReportSender {
    private final QueryReader readData;
    private final Notifier notifier;

    public SalaryHtmlReportSender(ReportCreator createReport) {
        readData = createReport.createData();
        this.notifier = createReport.createNotifier();
    }

    public void generateAndSendHtmlSalaryReport(String departmentId, Range dateRange, String recipients) {
        DataForReportSalary dataReport = readData.readQuery(dateRange, departmentId);
        ReportBuilder reportBuild = new HTMLReportBuilder(dataReport);
        String report = reportBuild.createReport("Employee", "Salary");
        notifier.notifyReport(recipients, report);
    }
}
