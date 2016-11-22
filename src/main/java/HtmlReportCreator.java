import bilderreport.ReportBuilder;
import bilderreport.html.HTMLReportBuilder;
import dao.QueryReader;
import dao.jdbc.QuerySalaryReader;
import datareport.DataForReportSalary;
import notifier.Notifier;
import notifier.mailnotifier.MailNotifier;

import java.sql.Connection;

public class HtmlReportCreator implements ReportCreator{
    private final Connection connection;
    private String host;
    public HtmlReportCreator(Connection connection, String host) {
        this.connection = connection;
        this.host=host;
    }

    public QueryReader createData() {
        return new QuerySalaryReader(connection);
    }

    public Notifier createNotifier() {
        return new MailNotifier(host);
    }

}
