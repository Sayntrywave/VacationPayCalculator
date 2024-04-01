package ru.neo.korotkov.vacationpaycalculator.controller;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neo.korotkov.vacationpaycalculator.service.VacationCalculatorService;

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
        //todo validation

        double vacationPay = vacationCalculatorService.calculateVacationPay(salary, fromDate, toDate);

        return ResponseEntity.ok(vacationPay);
    }
}
