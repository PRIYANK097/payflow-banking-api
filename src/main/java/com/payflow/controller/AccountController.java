package com.payflow.controller;

import com.payflow.dto.request.CreateAccountRequest;
import com.payflow.dto.response.AccountResponse;
import com.payflow.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
        @Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }


    public ResponseEntity<AccountResponse> getAccountByUserId(
            @PathVariable Long userId){
        return ResponseEntity.ok(accountService.getAccountByUserId(userId));
    }

}
