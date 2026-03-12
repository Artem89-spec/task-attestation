package com.gridnine.testing.service;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.filter.FlightFilter;

import java.util.ArrayList;
import java.util.List;

public class FlightFilterService {
    private final List<FlightFilter> filters = new ArrayList<>();

    public void addFilter(FlightFilter filter) {
        filters.add(filter);
    }

    public List<Flight> applyFilter(FlightFilter flightFilter, List<Flight> flights) {
        return flightFilter.filter(flights);
    }

    public List<Flight> applyFilters(List<Flight> flights) {
        List<Flight> result = new ArrayList<>(flights);
        for (FlightFilter filter : filters) {
            result = filter.filter(result);
        }
        return result;
    }
}
