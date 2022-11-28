package com.vmg.scrum;

import com.vmg.scrum.excel.DataExcelCalculation;
import com.vmg.scrum.repository.LogDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;

@SpringBootApplication
@EnableScheduling
public class Application {
   
    public static void main(String[] args) {
        SpringApplication.run(Application.class);

    }


}