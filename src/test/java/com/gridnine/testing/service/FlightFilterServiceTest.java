package com.gridnine.testing.service;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.service.filter.FlightFilter;
import com.gridnine.testing.service.filter.impl.DepartureBeforeTheCurrentTimeFilter;
import com.gridnine.testing.service.filter.impl.OnlyDirectFlightsFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование сервиса фильтрации рейсов")
class FlightFilterServiceTest {

    private FlightFilterService service;
    private Flight directFlight;
    private Flight multiSegmentFlight;

    @BeforeEach
    void setUp() {
        service = new FlightFilterService();
        LocalDateTime now = LocalDateTime.now();

        directFlight = new Flight(Arrays.asList(new Segment(now.plusHours(1), now.plusHours(3))));

        multiSegmentFlight = new Flight(Arrays.asList(
                new Segment(now.plusHours(1), now.plusHours(3)),
                new Segment(now.plusHours(4), now.plusHours(6))
                ));
    }

    @Test
    @DisplayName("Сервис должен применять одиночный фильтр")
    void shouldApplySingleFilter() {
        FlightFilter filter = new OnlyDirectFlightsFilter();
        List<Flight> flights = Arrays.asList(directFlight, multiSegmentFlight);

        List<Flight> result = service.applyFilter(filter, flights);

        assertEquals(1, result.size());
        assertTrue(result.contains(directFlight));
        assertFalse(result.contains(multiSegmentFlight));
    }

    @Test
    @DisplayName("Сервис должен последовательно применять несколько фильтров")
    void shouldApplyMultipleFiltersSequentially() {
        LocalDateTime now = LocalDateTime.now();
        Flight pastFlight = new Flight(Arrays.asList(new Segment(now.minusHours(5), now.plusHours(1))));

        List<Flight> flights = Arrays.asList(directFlight, multiSegmentFlight);

        service.addFilter(new DepartureBeforeTheCurrentTimeFilter());
        service.addFilter(new OnlyDirectFlightsFilter());

        List<Flight> result = service.applyFilters(flights);

        assertEquals(1, result.size());
        assertTrue(result.contains(directFlight));
        assertFalse(result.contains(pastFlight));
    }

    @Test
    @DisplayName("Сервис должен корректно обрабатывать пустой список фильтров")
    void shouldHandleEmptyFilterList() {

        List<Flight> flights = Arrays.asList(directFlight, multiSegmentFlight);

        List<Flight> result = service.applyFilters(flights);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(flights));
    }

    @Test
    @DisplayName("Сервис должен корректно обрабатывать пустой список рейсов")
    void shouldHandleEmptyFlightList() {

        service.addFilter(new OnlyDirectFlightsFilter());

        List<Flight> result = service.applyFilters(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Порядок фильтров должен влиять на результат")
    void testFilterOrderMatters() {
        LocalDateTime now = LocalDateTime.now();

        Flight invalidFlight = new Flight(Arrays.asList(new Segment(now.minusHours(5), now.plusHours(1))));

        List<Flight> flights = Arrays.asList(directFlight, invalidFlight);

        FlightFilterService service1 = new FlightFilterService();
        service1.addFilter(new DepartureBeforeTheCurrentTimeFilter());
        service1.addFilter(new OnlyDirectFlightsFilter());

        FlightFilterService service2 = new FlightFilterService();
        service2.addFilter(new OnlyDirectFlightsFilter());
        service2.addFilter(new DepartureBeforeTheCurrentTimeFilter());

        List<Flight> result1 = service1.applyFilters(flights);
        List<Flight> result2 = service2.applyFilters(flights);

        assertEquals(1, result1.size());
        assertEquals(1, result2.size());
        assertTrue(result1.contains(directFlight));
        assertTrue(result2.contains(directFlight));

    }
}