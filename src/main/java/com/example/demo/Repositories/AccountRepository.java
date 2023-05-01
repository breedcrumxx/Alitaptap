package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{
    
    @Query(value = "select * from account where username=:username", nativeQuery = true)
    Account findAccount(@Param("username") String username);

    @Query(value = "select * from account where id=?", nativeQuery = true)
    Account findAccountById(int id);

    @Query(value = "select * from account where rfid=:rfid", nativeQuery = true)
    Account getByRFID(@Param("rfid") int rfid);

    @Query(value = "select * from account where id=:id", nativeQuery = true)
    Account getAccountById(@Param("id") int instructor);

    @Query(value = "select * from account where role='Instructor'", nativeQuery = true)
	List<Account> getAllInstructors();

}
