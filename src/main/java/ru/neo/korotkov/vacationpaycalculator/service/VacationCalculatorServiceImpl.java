package ru.neo.korotkov.vacationpaycalculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neo.korotkov.vacationpaycalculator.util.Holiday;
import ru.neo.korotkov.vacationpaycalculator.util.HolidayResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class VacationCalculatorServiceImpl implements VacationCalculatorService {

    @Value("${holiday-api.key}")
    private String API_KEY;

    @SneakyThrows
    @Override
    public double calculateVacationPay(double salary, LocalDate fromDate, LocalDate toDate) {
        long weekendsCount = fromDate.datesUntil(toDate.plusDays(1))
                .filter(date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                .count();

        long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate) + 1 - weekendsCount;

        List<Holiday> holidays;


        holidays = getDaysOff(fromDate.getYear());

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

        String url = "https://holidayapi.com/v1/holidays" +
                "?key=" + API_KEY +
                "&country=RU" +
                "&year=" + year +
                "&public=true";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, HolidayResponse.class).getHolidays();
    }
}


