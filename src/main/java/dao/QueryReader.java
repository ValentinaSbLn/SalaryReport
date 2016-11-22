package dao;

import datareport.DataForReportSalary;
import utils.Range;

public interface QueryReader {
     DataForReportSalary readQuery(Range date, String... fields);
}
