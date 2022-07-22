package com.iiplabs.nns.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public final class DateTimeUtil {

    public static long getDelayUntilNextSmsSendWindow(String sendWindow, LocalDateTime moment) {
        long seconds = 0;

        if (sendWindow != null) {
            String[] window = sendWindow.split("-");
            if (window.length == 2) {
                DateTimeFormatter hoursMinutesFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

                LocalTime begin = LocalTime.parse(window[0], hoursMinutesFormatter);
                LocalTime end = LocalTime.parse(window[1], hoursMinutesFormatter);

                LocalDate today = LocalDate.now();
                LocalDateTime beginDateTime = begin.atDate(today);
                LocalDateTime endDateTime = end.atDate(today);

                if (begin.isAfter(end)) {
                    // tomorrow
                    endDateTime = endDateTime.plusDays(1);
                }

                LocalDateTime midnight = LocalTime.of(0, 0).atDate(today);

                if (moment.isBefore(beginDateTime)) {
                    seconds = ChronoUnit.SECONDS.between(moment, beginDateTime);
                } else if (moment.isAfter(endDateTime)) {
                    seconds = ChronoUnit.SECONDS.between(midnight, moment)
                            - ChronoUnit.SECONDS.between(beginDateTime, endDateTime);
                }
            }
        }
        return seconds;
    }

    private DateTimeUtil() {
        throw new AssertionError();
    }

}
