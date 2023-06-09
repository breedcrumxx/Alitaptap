package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Batch;
import com.example.demo.Repositories.BatchRepository;

@Service
public class BatchService {
    
    @Autowired
    BatchRepository batchRepository;

    public Batch getBatch(int batchid) {
        return batchRepository.findBatchById(batchid);
    }

    public List<Batch> getAllBatch() {
        return batchRepository.getAllBatch();
    }


}
