package ru.neo.korotkov.vacationpaycalculator.service;

import java.time.LocalDate;

public interface VacationCalculatorService {
    double calculateVacationPay(double salary, LocalDate fromDate, LocalDate toDate);
}
