package domain;

import java.util.ArrayList;
import java.util.List;

public class DataForReportSalary {

    private final List<Employee> employees = new ArrayList<>();

    public List<Employee> getListItems() {
        return employees;
    }


    public void add(String name, double salary) {
        employees.add(new Employee(name, salary));
    }


    private static class Employee {
        private final String nameEmp;

        @FieldOperation(OperationWithField.TOTAL)
        private final double salaryEmp;

        public Employee(String nameEmp, double salaryEmp) {
            this.nameEmp = nameEmp;
            this.salaryEmp = salaryEmp;
        }

        public String getNameEmp() {
            return nameEmp;
        }

        public double getSalaryEmp() {
            return salaryEmp;
        }

        @Override
        public String toString() {
            return "<td>" +
                    nameEmp +
                    "</td><td>" + salaryEmp +
                    "</td>";
        }
    }
}
