package com.gridnine.testing.service.filter.impl;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.filter.FlightFilter;

import java.util.ArrayList;
import java.util.List;

public class OnlyDirectFlightsFilter implements FlightFilter {

    @Override
    public List<Flight> filter(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return new ArrayList<>();
        }

        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            if (flight.getSegments().size() == 1) {
                result.add(flight);
            }
        }
        return result;
    }
}
