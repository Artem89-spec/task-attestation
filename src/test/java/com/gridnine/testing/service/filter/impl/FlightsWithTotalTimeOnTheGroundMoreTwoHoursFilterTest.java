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

@DisplayName("Тестирование фильтра: время на земле > 2 часов")
class FlightsWithTotalTimeOnTheGroundMoreTwoHoursFilterTest {

    private FlightsWithTotalTimeOnTheGroundMoreTwoHoursFilter filter;

    @BeforeEach
    void setUp() {
        filter = new FlightsWithTotalTimeOnTheGroundMoreTwoHoursFilter();
    }

    @Test
    @DisplayName("Фильтр должен сохранять прямые рейсы")
    void shouldKeepDirectFlights() {
        LocalDateTime now = LocalDateTime.now();
        Flight directFlight = new Flight(Arrays.asList(new Segment(now.plusHours(1), now.plusHours(1))));

        List<Flight> result = filter.filter(List.of(directFlight));

        assertEquals(1, result.size());
        assertTrue(result.contains(directFlight));
    }

    @Test
    @DisplayName("Фильтр должен исключать рейсы с общим временем на земле > 2 часов")
    void shouldExcludeFlightsWithGroundTimeMoreThanTwoHours() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight = new Flight(Arrays.asList(new Segment(now.plusHours(1),
                now.plusHours(3)), new Segment(now.plusHours(6), now.plusHours(8))));

        List<Flight> result = filter.filter(List.of(flight));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Фильтр должен сохранять рейсы с общим временем на земле = 2 часа")
    void shouldKeepFlightsWithGroundTimeExactlyTwoHours() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight = new Flight(Arrays.asList(new Segment(now.plusHours(1),
                now.plusHours(3)), new Segment(now.plusHours(5), now.plusHours(7))));

        List<Flight> result = filter.filter(List.of(flight));

        assertEquals(1, result.size());
        assertTrue(result.contains(flight));
    }

    @Test
    @DisplayName("Фильтр должен исключать рейсы с отрицательным временем на земле")
    void shouldExcludeFlightsWithNegativeGroundTime() {
        LocalDateTime now = LocalDateTime.now();
        Flight flight = new Flight(Arrays.asList(new Segment(now.plusHours(1),
                now.plusHours(4)), new Segment(now.plusHours(3), now.plusHours(6))));

        List<Flight> result = filter.filter(List.of(flight));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Фильтр должен корректно считать время на земле для нескольких сегментов")
    void shouldCorrectlyCalculateGroundTimeForMultipleSegments() {
        LocalDateTime now = LocalDateTime.now();
        Flight invalidFlight = new Flight(Arrays.asList(
                new Segment(now.plusHours(1), now.plusHours(2)),
                new Segment(now.plusHours(3), now.plusHours(4)),
                new Segment(now.plusHours(6), now.plusHours(7))
        ));
        Flight validFlight = new Flight(Arrays.asList(
                new Segment(now.plusHours(1), now.plusHours(2)),
                new Segment(now.plusHours(3), now.plusHours(4)),
                new Segment(now.plusHours(5), now.plusHours(6))
        ));

        List<Flight> flights = Arrays.asList(invalidFlight, validFlight);

        List<Flight> result = filter.filter(flights);

        assertEquals(1, result.size());
        assertTrue(result.contains(validFlight));
        assertFalse(result.contains(invalidFlight));
    }

    @Test
    @DisplayName("Фильтр должен корректно обрабатывать null список")
    void shouldHandleNullList() {
        List<Flight> result = filter.filter(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}