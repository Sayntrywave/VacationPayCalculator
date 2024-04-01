package ru.neo.korotkov.vacationpaycalculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neo.korotkov.vacationpaycalculator.utils.Holiday;
import ru.neo.korotkov.vacationpaycalculator.utils.HolidayResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class VacationCalculatorServiceImpl implements VacationCalculatorService {

    @Override
    public double calculateVacationPay(double salary, LocalDate fromDate, LocalDate toDate) {
        long weekendsCount = fromDate.datesUntil(toDate.plusDays(1))
                .filter(date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                .count();

        long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate) + 1 - weekendsCount;

        List<Holiday> holidays;
        try {
            holidays = getDaysOff(fromDate.getYear());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        long publicHolidaysCount = holidays.stream()
                .filter(holiday -> {
                    LocalDate date = holiday.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return !date.isBefore(fromDate) && !date.isAfter(toDate)
                            && date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY;
                })
                .count();

        double averageDailySalary = salary / 21.75;

        return (daysBetween - publicHolidaysCount) * averageDailySalary;
    }


    private List<Holiday> getDaysOff(int year) throws JsonProcessingException {

        //todo hide api key
        String url = "https://holidayapi.com/v1/holidays" +
                "?key=" + "7968cb11-20ae-42e4-b7b2-e677feb5fa8c" +
                "&country=RU" +
                "&year=" + year +
                "&pretty" +
                "&public=true";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, HolidayResponse.class).getHolidays();
    }
}


