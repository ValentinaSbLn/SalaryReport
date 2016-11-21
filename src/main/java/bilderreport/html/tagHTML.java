package bilderreport.html;

/**
 * Created by Valentina on 21.11.2016.
 */
public abstract class tagHTML {
    String beginHTML() {
        return "<html>";
    }

    String endHTML() { return "</html>"; }

    String beginBody() {
        return "<body>";
    }

    String endBody() {
        return "</body>";
    }

    String beginTable() {
        return "<table>";
    }

    String endTable() {
        return "</table>";
    }

    String beginRow() {
        return "<tr>";
    }

    String endRow() {
        return "</tr>";
    }

    String beginColumn() {
        return "<td>";
    }

    String endColumn() {
        return "</td>";
    }

}
