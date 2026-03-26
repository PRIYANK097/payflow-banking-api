package com.payflow.service;

import com.payflow.dto.request.CreateAccountRequest;
import com.payflow.dto.response.AccountResponse;
import com.payflow.entity.Account;
import com.payflow.entity.Account.AccountStatus;
import com.payflow.entity.User;
import com.payflow.exception.DuplicateResourceException;
import com.payflow.exception.ResourceNotFoundException;
import com.payflow.mapper.AccountMapper;
import com.payflow.repository.AccountRepository;
import com.payflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getUserId()
                ));

        if (accountRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException(
                    "Account already exists for user id: " + request.getUserId()
            );
        }

        Account account = Account.builder()
                .user(user)
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .accountType(request.getAccountType())
                .status(AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with id: " + id
                ));
        return accountMapper.toResponse(account);
    }

    public AccountResponse getAccountByUserId(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found for user id: " + userId
                ));
        return accountMapper.toResponse(account);
    }

    private String generateAccountNumber() {
        return "PAY" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 9)
                .toUpperCase();
    }
}