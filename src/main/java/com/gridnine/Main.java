package com.gridnine;

import com.gridnine.testing.factory.FlightBuilder;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.FlightFilterService;
import com.gridnine.testing.service.filter.impl.DepartureBeforeTheCurrentTimeFilter;
import com.gridnine.testing.service.filter.impl.FlightsWithTotalTimeOnTheGroundMoreTwoHoursFilter;
import com.gridnine.testing.service.filter.impl.MaxTransfersFilter;
import com.gridnine.testing.service.filter.impl.OnlyDirectFlightsFilter;
import com.gridnine.testing.service.filter.impl.SegmentsWithArrivalBeforeDepartureFilter;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        System.out.println("=== ВСЕ РЕЙСЫ ===");
        flights.forEach(System.out::println);
        System.out.println();

        FlightFilterService filterService = new FlightFilterService();

        System.out.println("=== 1. ИСКЛЮЧИТЬ ВЫЛЕТЫ ДО ТЕКУЩЕГО ВРЕМЕНИ ===");
        List<Flight> result1 = filterService.applyFilter(new DepartureBeforeTheCurrentTimeFilter(), flights);
        result1.forEach(System.out::println);
        System.out.println();

        System.out.println("=== 2. ИСКЛЮЧИТЬ СЕГМЕНТЫ С ПРИЛЕТОМ РАНЬШЕ ВЫЛЕТА ===");
        List<Flight> result2 = filterService.applyFilter(new SegmentsWithArrivalBeforeDepartureFilter(), flights);
        result2.forEach(System.out::println);
        System.out.println();

        System.out.println("=== 3. ИСКЛЮЧИТЬ РЕЙСЫ С ВРЕМЕНЕМ НА ЗЕМЛЕ > 2 ЧАСОВ ===");
        List<Flight> result3 = filterService.applyFilter(new FlightsWithTotalTimeOnTheGroundMoreTwoHoursFilter(), flights);
        result3.forEach(System.out::println);
        System.out.println();

        System.out.println("=== 4. ТОЛЬКО ПРЯМЫЕ РЕЙСЫ (дополнительно) ===");
        List<Flight> result4 = filterService.applyFilter(new OnlyDirectFlightsFilter(), flights);
        result4.forEach(System.out::println);
        System.out.println();

        System.out.println("=== 5. МАКСИМУМ 1 ПЕРЕСАДКА (дополнительно) ===");
        List<Flight> result5 = filterService.applyFilter(new MaxTransfersFilter(1), flights);
        result5.forEach(System.out::println);
        System.out.println();

        System.out.println("=== 6. КОМБИНИРОВАННАЯ ФИЛЬТРАЦИЯ (3 основных фильтра) ===");
        filterService = new FlightFilterService();

        filterService.addFilter(new DepartureBeforeTheCurrentTimeFilter());
        filterService.addFilter(new SegmentsWithArrivalBeforeDepartureFilter());
        filterService.addFilter(new FlightsWithTotalTimeOnTheGroundMoreTwoHoursFilter());

        List<Flight> combinedResult = filterService.applyFilters(flights);
        combinedResult.forEach(System.out::println);
        System.out.println();

    }
}