package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{
    
    @Query(value = "SELECT * from account where username=:username", nativeQuery = true)
    Account findAccount(@Param("username") String username);

    @Query(value = "SELECT * from account where id=?", nativeQuery = true)
    Account findAccountById(int id);

}
