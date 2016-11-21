import bilderreport.ReportBuilder;
import bilderreport.html.HTMLReportBuilder;
import dao.QueryReader;
import dao.jdbc.QuerySalaryReader;
import domain.DataForReportSalary;
import notifier.Notifier;
import notifier.mailnotifier.MailNotifier;

import java.sql.Connection;

public class SalaryHtmlReportNotifier implements ReportCreator{
    private final Connection connection;

    public SalaryHtmlReportNotifier(Connection connection) {
        this.connection = connection;
    }

    public QueryReader createData() {
        return new QuerySalaryReader(connection);
    }

    public ReportBuilder createReport(DataForReportSalary dataReport) {
        return new HTMLReportBuilder(dataReport);
    }

    public Notifier createNotifier(String host) {
        return new MailNotifier(host);
    }

}
