package com.booking.domain;

public class RecurrenceRule {
    private int totalOccurrences;
    private int daysBetween; // how often do we want the occurrence

    public RecurrenceRule(int totalOccurrences, int daysBetween) {
        this.totalOccurrences = totalOccurrences;
        this.daysBetween = daysBetween;
    }

    public int getDaysBetween() {
        return daysBetween;
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }
}
