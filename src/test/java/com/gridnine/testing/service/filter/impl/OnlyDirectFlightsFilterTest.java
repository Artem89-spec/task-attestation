package com.gridnine.testing.service.filter.impl;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Фильтр проверки только на прямые рейсы")
class OnlyDirectFlightsFilterTest {

    private OnlyDirectFlightsFilter filter;

    @BeforeEach
    void setUp() {
        filter = new OnlyDirectFlightsFilter();
    }

    @Test
    @DisplayName("Фильтр должен корректно отбирать только прямые рейсы")
    void shouldKeepOnlyDirectFlights() {
        LocalDateTime now = LocalDateTime.now();
        Flight directFlight = new Flight(Arrays.asList(new Segment(now.plusHours(1), now.plusHours(3))));
        Flight multiSegmentFlight = new Flight(Arrays.asList(
                new Segment(now.plusHours(1), now.plusHours(3)),
                new Segment(now.plusHours(4), now.plusHours(6))
        ));

        List<Flight> flights = Arrays.asList(directFlight, multiSegmentFlight);

        List<Flight> result = filter.filter(flights);

        assertEquals(1, result.size());
        assertTrue(result.contains(directFlight));
        assertFalse(result.contains(multiSegmentFlight));
    }

        @Test
        @DisplayName("Фильтр должен корректно обрабатывать null список")
        void shouldHandleNullList() {
            List<Flight> result = filter.filter(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
}