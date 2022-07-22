package com.iiplabs.nns.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeUtilTest {

    @Test
    public void getDelayUntilNextSmsSendWindowTest() {
        String testSendWindow = "09:00-22:00";

        LocalDateTime before = LocalTime.of(5, 00).atDate(LocalDate.now());
        assertEquals(14400, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, before));

        LocalDateTime in = LocalTime.of(11, 00).atDate(LocalDate.now());
        assertEquals(0, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, in));

        LocalDateTime inOnEdgeLeft = LocalTime.of(9, 00).atDate(LocalDate.now());
        assertEquals(0, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, inOnEdgeLeft));

        LocalDateTime inOnEdgeRight = LocalTime.of(22, 00).atDate(LocalDate.now());
        assertEquals(0, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, inOnEdgeRight));

        LocalDateTime after = LocalTime.of(23, 00).atDate(LocalDate.now());
        assertEquals(36000, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, after));

        // revert
        testSendWindow = "22:00-09:00";

        LocalDateTime revertBefore = LocalTime.of(21, 00).atDate(LocalDate.now());
        assertEquals(3600, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, revertBefore));

        LocalDateTime revertAfter = LocalTime.of(10, 00).atDate(LocalDate.now());
        assertEquals(43200, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, revertAfter));

        LocalDateTime revertIn = LocalTime.of(23, 00).atDate(LocalDate.now());
        assertEquals(0, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, revertIn));

        LocalDateTime revertInOnEdgeLeft = LocalTime.of(22, 00).atDate(LocalDate.now());
        assertEquals(0, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, revertInOnEdgeLeft));

        LocalDateTime revertInOnEdgeRight = LocalTime.of(9, 00).atDate(LocalDate.now().plusDays(1));
        assertEquals(0, DateTimeUtil.getDelayUntilNextSmsSendWindow(testSendWindow, revertInOnEdgeRight));
    }

}
