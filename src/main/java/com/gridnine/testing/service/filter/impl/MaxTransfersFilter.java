package com.gridnine.testing.service.filter.impl;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.filter.FlightFilter;

import java.util.ArrayList;
import java.util.List;

public class MaxTransfersFilter implements FlightFilter {

    private final int maxTransfers;

    public MaxTransfersFilter(int maxTransfers) {
        this.maxTransfers = maxTransfers;
    }

    @Override
    public List<Flight> filter(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return new ArrayList<>();
        }

        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            int transfers = flight.getSegments().size() - 1;
            if (transfers <= maxTransfers) {
                result.add(flight);
            }
        }
        return result;
    }
}
