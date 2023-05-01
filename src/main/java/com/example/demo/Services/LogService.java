package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Log;
import com.example.demo.Repositories.LogRepository;

@Service
public class LogService {
    
    @Autowired
    LogRepository logRepository;

    public Log create(Log log){
        return logRepository.save(log);
    }

    public Log getRecentLogByRfid(String rfid, String date){
        return logRepository.getRecentLogByRfid(rfid);
    }

    public int getLogCountToday(String currentDate) {
        return logRepository.countLogsToday(currentDate);
    }

    public List<Log> getRecentLogs() {
        return logRepository.recentLogs();
    }

    public List<Log> getAllLogs() {
        return logRepository.getLogs();
    }
}
