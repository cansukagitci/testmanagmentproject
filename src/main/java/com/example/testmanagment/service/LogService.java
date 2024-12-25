package com.example.testmanagment.service;

import com.example.testmanagment.model.LogEntry;
import com.example.testmanagment.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void logInfo(String message){
        LogEntry logEntry=new LogEntry();

        logEntry.getTimestamp(new Date());
        logEntry.setLevel("INFO");
        logEntry.setMessage(message);

        logRepository.save(logEntry);

    }

    public void logError(String message){
        LogEntry logEntry = new LogEntry();

        logEntry.setTimestamp(new Date());
        logEntry.setLevel("ERROR");
        logEntry.setMessage(message);

        logRepository.save(logEntry);
    }

}
