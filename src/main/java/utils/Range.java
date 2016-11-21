package utils;

import java.time.LocalDate;

public class Range {
    private final LocalDate from;
    private final LocalDate to;

    private Range(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public static Range of(LocalDate from, LocalDate to) {
        return new Range(from, to);
    }
}
