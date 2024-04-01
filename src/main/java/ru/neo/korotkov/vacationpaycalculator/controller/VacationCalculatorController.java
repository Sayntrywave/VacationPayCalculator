package ru.neo.korotkov.vacationpaycalculator.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neo.korotkov.vacationpaycalculator.service.VacationCalculatorService;
import ru.neo.korotkov.vacationpaycalculator.util.exception.BadCalculateParamsException;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VacationCalculatorController {

    VacationCalculatorService vacationCalculatorService;

    @GetMapping("/calculate")
    public ResponseEntity<Double> calculate(@RequestParam("avg_salary") double salary,
                                            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {

        if (salary < 0) {
            throw new BadCalculateParamsException("Зарплата не может быть отрицательной");
        }

        double vacationPay = vacationCalculatorService.calculateVacationPay(salary, fromDate, toDate);

        return ResponseEntity.ok(vacationPay);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(JsonProcessingException e) {
        return ResponseEntity.internalServerError().body("Не удалось получить список нерабочих дней");
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(BadCalculateParamsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
