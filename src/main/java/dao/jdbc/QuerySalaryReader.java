package dao.jdbc;


import bilderreport.html.HTMLReportBuilder;
import dao.QueryReader;
import datareport.DataForReportSalary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Range;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuerySalaryReader implements QueryReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(HTMLReportBuilder.class);
    private final Connection connection;

    public QuerySalaryReader(Connection connection) {
        this.connection = connection;
    }

    @Override
    public DataForReportSalary readQuery(Range date, String... fields) {
        final DataForReportSalary dataReport = new DataForReportSalary();
        final String departmentId = fields[0];
        String sqlQuery = "select emp.id as emp_id, emp.name as amp_name, sum(salary) as salary from employee emp left join" +
                "salary_payments sp on emp.id = sp.employee_id where emp.department_id = ? and" +
                " sp.date >= ? and sp.date <= ? group by emp.id, emp.name";

        try (PreparedStatement ps = connection.prepareStatement(sqlQuery); ResultSet results = ps.executeQuery();) {

            ps.setString(1, departmentId);
            ps.setDate(2, new java.sql.Date(date.getFrom().toEpochDay()));
            ps.setDate(3, new java.sql.Date(date.getTo().toEpochDay()));
            while (results.next()) {
                dataReport.add(results.getString("emp_name"), results.getDouble("salary"));
            }
        } catch (SQLException e) {
            LOGGER.error("Can't create report from DB", e);
         }
        return dataReport;
    }
}
