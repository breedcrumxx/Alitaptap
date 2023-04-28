package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Account;
import com.example.demo.Repositories.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    AccountRepository accountRepository;

    public Account getAccountByRFID(int rfid) {
        return accountRepository.getByRFID(rfid);
    }

    public Account getAccountById(int instructor) {
        return accountRepository.getAccountById(instructor);
    }



    
}
