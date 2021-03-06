package bilderreport.html;


import bilderreport.ReportBuilder;
import datareport.DataForReportSalary;
import datareport.FieldOperation;
import datareport.OperationWithField;

import java.lang.reflect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static datareport.OperationWithField.*;


public class HTMLReportBuilder extends tagHTML implements ReportBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(HTMLReportBuilder.class);
    private final StringBuilder resultingHtml = new StringBuilder();
    private final DataForReportSalary dataReport;
    private static final  OperationWithField VALUE = TOTAL;

    public HTMLReportBuilder(DataForReportSalary dataReport) {
        this.dataReport = dataReport;
    }

    @Override
    public String createReport(String... fields) {
        beginReport(fields);
        tableBodyReport();
        addRowWithAnnotation();
        endReport();
        return resultingHtml.toString();
    }

    private void beginReport(String... titles) {
        resultingHtml.append(beginHTML() + beginBody() + beginTable() + beginRow() + titleTable(titles) + endRow());
    }

    private void tableBodyReport() {
        for (int i = 0; i < dataReport.getListItems().size(); i++) {
            resultingHtml.append(beginRow());
            resultingHtml.append((dataReport.getListItems()).get(i));
            resultingHtml.append(endRow());
        }
    }

    private String titleTable(String... titles) {

        StringBuilder titleLine = new StringBuilder();
        for (String title : titles) {
            titleLine.append( beginColumn() + title + endColumn());
        }
        return titleLine.toString();
    }

    private void addRowWithAnnotation() {
        Type[] typeArguments = getTypeReportList();
        for (Type typeArgument : typeArguments) {
            Class typeArgClass = (Class) typeArgument;

            Field[] fields = typeArgClass.getDeclaredFields();
            for (Field field : fields) {

                if (field.isAnnotationPresent(FieldOperation.class) && field.getAnnotation(FieldOperation.class).value() == VALUE) {
                    resultingHtml.append(beginRow() +
                            beginColumn() + VALUE + endColumn() +
                            beginColumn() + getTotal() + endColumn() +
                            endRow());
                }
            }
        }
    }

    private void endReport() {
        resultingHtml.append(endTable() + endBody() + endHTML());
    }

    @SuppressWarnings("unchecked")
    private <T> T getEmployeeValue(String nameMethod, int numEmp) {
        T t = null;
        try {
            Type[] typeArguments = getTypeReportList();
            for (Type typeArgument : typeArguments) {
                Class typeArgClass = (Class) typeArgument;
                Method method = typeArgClass.getDeclaredMethod(nameMethod);
                method.setAccessible(true);
                t = (T) method.invoke((dataReport.getListItems()).get(numEmp));
                method.setAccessible(false);
            }
            return t;

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Error in method witw reflaction", e);
            return t;
        }
    }

    private Type[] getTypeReportList() {
        Field[] fields = dataReport.getClass().getDeclaredFields();
        Type typeField = fields[0].getGenericType();

        if (typeField instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) typeField;
            return type.getActualTypeArguments();
        }
        return new Type[0];
    }


    private double getTotal() {
        double totals = 0;
        for (int j = 0; j < dataReport.getListItems().size(); j++) {
            totals += (Double) getEmployeeValue("getSalaryEmp", j);
        }
        return totals;
    }
}