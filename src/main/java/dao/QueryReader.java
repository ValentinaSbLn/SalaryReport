package dao;

import domain.DataForReportSalary;
import utils.Range;

public interface QueryReader {
     DataForReportSalary readQuery(Range date, String... fields);
}
