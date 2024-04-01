package ru.neo.korotkov.vacationpaycalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VacationPayCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VacationPayCalculatorApplication.class, args);

//		try {
//			new VacationCalculatorServiceImpl().getDaysOff(LocalDate.of(2023,1,1),LocalDate.of(2023,12,31));
//		} catch (JsonProcessingException e) {
//			throw new RuntimeException(e);
//		}
    }

}
