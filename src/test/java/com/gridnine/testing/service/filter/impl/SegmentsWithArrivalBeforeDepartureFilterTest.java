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

@DisplayName("Тестирование фильтра: прилет раньше вылета")
class SegmentsWithArrivalBeforeDepartureFilterTest {

    private SegmentsWithArrivalBeforeDepartureFilter filter;

    @BeforeEach
    void setUp() {
        filter = new SegmentsWithArrivalBeforeDepartureFilter();
    }

    @Test
    @DisplayName("Фильтр должен исключать рейсы с прилетом раньше вылета")
    void shouldExcludeFlightsWithArrivalBeforeDeparture() {
        LocalDateTime now = LocalDateTime.now();
        Flight invalidFlight = new Flight(Arrays.asList(new Segment(now.plusHours(5), now.plusHours(1))));
        Flight validFlight = new Flight(Arrays.asList(new Segment(now.plusHours(2), now.plusHours(4))));

        List<Flight> flights = Arrays.asList(invalidFlight, validFlight);

        List<Flight> result = filter.filter(flights);

        assertEquals(1, result.size());
        assertTrue(result.contains(validFlight));
        assertFalse(result.contains(invalidFlight));
    }

    @Test
    @DisplayName("Фильтр должен исключать рейсы с прилетом равным вылету")
    void shouldExcludeFlightsWithArrivalEqualsDeparture() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight = new Flight(Arrays.asList(new Segment(now.plusHours(1), now.plusHours(1))));

        List<Flight> result = filter.filter(List.of(flight));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Фильтр должен исключать рейс, если хотя бы один сегмент некорректен")
    void shouldExcludeFlightIfAnySegmentIsInvalid() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight = new Flight(Arrays.asList(new Segment(now.plusHours(1), now.plusHours(3)),
                new Segment(now.plusHours(5), now.plusHours(1))));

        List<Flight> result = filter.filter(List.of(flight));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Фильтр должен сохранять рейсы с корректными сегментами")
    void shouldKeepFlightsWithValidSegments() {
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