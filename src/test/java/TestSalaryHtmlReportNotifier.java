import bilderreport.ReportBuilder;
import bilderreport.html.HTMLReportBuilder;
import datareport.DataForReportSalary;
import dao.QueryReader;
import dao.jdbc.QuerySalaryReader;
import notifier.Notifier;
import notifier.mailnotifier.MailNotifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import utils.Range;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MailNotifier.class)
public class TestSalaryHtmlReportNotifier {
    @Test
    public void test() throws Exception {
        Connection someFakeConnection = mockConnection();
        MimeMessageHelper mockMimeMessageHelper = getMockedMimeMessageHelper();

        LocalDate dateFrom = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate dateTo = LocalDate.of(2014, Month.DECEMBER, 31);

        QueryReader queryBase = new QuerySalaryReader(someFakeConnection);
        DataForReportSalary dataReport = queryBase.readQuery(Range.of(dateFrom, dateTo), "1");

        ReportBuilder report = new HTMLReportBuilder( dataReport);
        Notifier notifier = new MailNotifier("mail.google.com");
        notifier.notifyReport("somebody@gmail.com", report.createReport("Employee", "Salary"));

        String expectedReportPath = "src/test/resources/expectedReport.html";
        assertActualReportEqualsTo(mockMimeMessageHelper, expectedReportPath);
    }

    @Test
    public void testFailQuery() throws Exception {
        Connection someFakeConnection = mockConn();

        MimeMessageHelper mockMimeMessageHelper = getMockedMimeMessageHelper();

        LocalDate dateFrom = LocalDate.of(2014, Month.JANUARY, 1);
        LocalDate dateTo = LocalDate.of(2014, Month.DECEMBER, 31);

        QueryReader queryBase = new QuerySalaryReader(someFakeConnection);
        DataForReportSalary dataReport = queryBase.readQuery(Range.of(dateFrom, dateTo), "1");

        ReportBuilder report = new HTMLReportBuilder((DataForReportSalary) dataReport);
        Notifier notifier = new MailNotifier("mail.google.com");
        notifier.notifyReport("somebody@gmail.com", report.createReport("Employee", "Salary"));

        String expectedReportPath = "src/test/resources/failQuery.html";
        assertActualReportEqualsTo(mockMimeMessageHelper, expectedReportPath);
    }


    private void assertActualReportEqualsTo(MimeMessageHelper mockMimeMessageHelper, String expectedReportPath) throws MessagingException, IOException {
        ArgumentCaptor<String> messageTextArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockMimeMessageHelper).setText(messageTextArgumentCaptor.capture(), anyBoolean());
        Path path = Paths.get(expectedReportPath);
        String expectedReportContent = new String(Files.readAllBytes(path));
        assertEquals(messageTextArgumentCaptor.getValue(), expectedReportContent);
    }

    private ResultSet getMockedResultSet(Connection someFakeConnection) throws SQLException {
        PreparedStatement someFakePreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(someFakePreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(someFakeConnection.prepareStatement(anyString())).thenReturn(someFakePreparedStatement);
        when(mockResultSet.next()).thenReturn(true, true, false);
        return mockResultSet;
    }

    private MimeMessageHelper getMockedMimeMessageHelper() throws Exception {
        JavaMailSenderImpl mockMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mockMailSender.createMimeMessage()).thenReturn(mockMimeMessage);
        whenNew(JavaMailSenderImpl.class).withNoArguments().thenReturn(mockMailSender);
        MimeMessageHelper mockMimeMessageHelper = mock(MimeMessageHelper.class);
        whenNew(MimeMessageHelper.class).withArguments(any(), eq(true))
                .thenReturn(mockMimeMessageHelper);
        return mockMimeMessageHelper;
    }

    private Connection mockConnection() throws Exception {
        Connection someFakeConnection = mock(Connection.class);
        ResultSet mockResultSet = getMockedResultSet(someFakeConnection);
        when(mockResultSet.getString("emp_name")).thenReturn("John Doe", "Jane Dow");
        when(mockResultSet.getDouble("salary")).thenReturn(100.0, 50.0);
        return someFakeConnection;
    }
    private Connection mockConn() throws Exception {
        Connection someFakeConnection = mock(Connection.class);
        ResultSet mockResultSet = getMockedResultSet(someFakeConnection);
        when(mockResultSet.getString("emp_name")).thenThrow(new SQLException());
        when(mockResultSet.getDouble("salary")).thenReturn(100.0, 50.0);
        return someFakeConnection;
    }
}
