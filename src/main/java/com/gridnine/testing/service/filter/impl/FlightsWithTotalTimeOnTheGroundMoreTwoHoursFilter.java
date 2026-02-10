package com.gridnine.testing.service.filter.impl;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.service.filter.FlightFilter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FlightsWithTotalTimeOnTheGroundMoreTwoHoursFilter implements FlightFilter {

    @Override
    public List<Flight> filter(List<Flight> flights) {
        if (flights == null || flights.isEmpty()) {
            return new ArrayList<>();
        }

        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            List<Segment> segments = flight.getSegments();

            if (segments.size() <= 1) {
                result.add(flight);
                continue;
            }

            long totalGroundTimeMinutes = 0;
            boolean hasNegativeGroundTime = false;

            for (int i = 0; i < segments.size() - 1; i++) {
                Segment currentSegment = segments.get(i);
                Segment nextSegment = segments.get(i + 1);

                Duration groundTime = Duration.between(
                        currentSegment.getArrivalDate(),
                        nextSegment.getDepartureDate()
                );

                if (groundTime.isNegative()) {
                    hasNegativeGroundTime = true;
                    break;
                }

                totalGroundTimeMinutes += groundTime.toMinutes();
            }

            if (!hasNegativeGroundTime &&   totalGroundTimeMinutes <= 120) {
                result.add(flight);
            }
        }
        return result;
    }
}
