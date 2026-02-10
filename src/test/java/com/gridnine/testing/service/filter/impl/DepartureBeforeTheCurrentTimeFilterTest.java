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

@DisplayName("Тестирование фильтра: вылет до текущего времени")
class DepartureBeforeTheCurrentTimeFilterTest {

    private DepartureBeforeTheCurrentTimeFilter filter;

    @BeforeEach
    void setUp() {
        filter = new DepartureBeforeTheCurrentTimeFilter();
    }

    @Test
    @DisplayName("Фильтр должен исключать рейсы с вылетом в прошлом")
    void shouldExcludeFlightsWithPastDeparture() {
        LocalDateTime now = LocalDateTime.now();
        Flight pastFlight = new Flight(Arrays.asList(new Segment(now.minusHours(5), now.plusHours(1))));
        Flight futureFlight = new Flight(Arrays.asList(new Segment(now.plusHours(2), now.plusHours(4))));

        List<Flight> flights = Arrays.asList(pastFlight, futureFlight);

        List<Flight> result = filter.filter(flights);

        assertEquals(1, result.size());
        assertTrue(result.contains(futureFlight));
        assertFalse(result.contains(pastFlight));
    }

    @Test
    @DisplayName("Фильтр должен исключать рейс, если хотя бы один сегмент имеет вылет в прошлом")
    void shouldExcludeFlightIfAnySegmentHasPastDeparture() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight = new Flight(Arrays.asList(new Segment(now.plusHours(1), now.plusHours(3)),
                new Segment(now.minusHours(5), now.plusHours(1))));

        List<Flight> result = filter.filter(List.of(flight));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Фильтр должен сохранять рейсы, где все сегменты в будущем")
    void shouldKeepFlightsWithAllFutureSegments() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight = new Flight(Arrays.asList(new Segment(now.plusHours(1), now.plusHours(3)),
                new Segment(now.plusHours(4), now.plusHours(6))));


        List<Flight> result = filter.filter(List.of(flight));

        assertEquals(1, result.size());
        assertTrue(result.contains(flight));
    }

    @Test
    @DisplayName("Фильтр должен корректно обрабатывать null список")
    void shouldHandleNullList() {
        List<Flight> result = filter.filter(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}