package com.gridnine.testing.service.filter.impl;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование фильтра: максимальное количество пересадок")
class MaxTransfersFilterTest {

    @Test
    @DisplayName("Фильтр должен сохранять рейсы с количеством пересадок меньше максимума")
    void shouldKeepFlightsWithTransfersLessThanMax() {
        LocalDateTime now = LocalDateTime.now();
        Flight directFlight = new Flight(Arrays.asList(new Segment(now.plusHours(1), now.plusHours(3))));
        Flight oneTransferFlight = new Flight(Arrays.asList(
                new Segment(now.plusHours(1), now.plusHours(3)),
                new Segment(now.plusHours(4), now.plusHours(6))
        ));
        Flight twoTransfersFlight = new Flight(Arrays.asList(
                new Segment(now.plusHours(1), now.plusHours(3)),
                new Segment(now.plusHours(4), now.plusHours(6)),
                new Segment(now.plusHours(7), now.plusHours(9))
        ));

        List<Flight> flights = Arrays.asList(directFlight, oneTransferFlight, twoTransfersFlight);

        MaxTransfersFilter filter = new MaxTransfersFilter(2);

        List<Flight> result = filter.filter(flights);

        assertEquals(2, result.size());
        assertTrue(result.contains(directFlight));
        assertTrue(result.contains(oneTransferFlight));
        assertFalse(result.contains(twoTransfersFlight));
    }
}