package com.gridnine.testing.service.filter.impl;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.service.filter.FlightFilter;

import java.util.ArrayList;
import java.util.List;

public class SegmentsWithArrivalBeforeDepartureFilter implements FlightFilter {

    @Override
    public List<Flight> filter(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return new ArrayList<>();
        }

        List<Flight> result =new ArrayList<>();

        for (Flight flight : flights) {
            boolean allSegmentsValid = true;

            for (Segment segment : flight.getSegments()) {
                if (segment.getArrivalDate().isBefore(segment.getDepartureDate()) ||
                        segment.getArrivalDate().isEqual(segment.getDepartureDate())) {
                    allSegmentsValid = false;
                    break;
                }
            }

            if (allSegmentsValid) {
                result.add(flight);
            }
        }
        return result;
    }
}
