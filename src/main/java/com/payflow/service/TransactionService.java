package com.payflow.service;

import com.payflow.dto.request.DepositRequest;
import com.payflow.dto.request.TransferRequest;
import com.payflow.dto.request.WithdrawRequest;
import com.payflow.dto.response.TransactionResponse;
import com.payflow.entity.Account;
import com.payflow.entity.Account.AccountStatus;
import com.payflow.entity.Transaction;
import com.payflow.entity.Transaction.TransactionStatus;
import com.payflow.entity.Transaction.TransactionType;
import com.payflow.exception.AccountFrozenException;
import com.payflow.exception.InsufficientBalanceException;
import com.payflow.exception.ResourceNotFoundException;
import com.payflow.mapper.TransactionMapper;
import com.payflow.repository.AccountRepository;
import com.payflow.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with id: " + request.getAccountId()
                ));

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new AccountFrozenException(
                    "Account is frozen. Cannot deposit."
            );
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .receiverAccount(account)
                .amount(request.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with id: " + request.getAccountId()
                ));

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new AccountFrozenException(
                    "Account is frozen. Cannot withdraw."
            );
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: " + account.getBalance()
            );
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .senderAccount(account)
                .amount(request.getAmount())
                .transactionType(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        if (request.getSenderAccountId().equals(request.getReceiverAccountId())) {
            throw new IllegalArgumentException(
                    "Sender and receiver accounts cannot be the same"
            );
        }

        Account sender = accountRepository.findById(request.getSenderAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sender account not found with id: " + request.getSenderAccountId()
                ));

        Account receiver = accountRepository.findById(request.getReceiverAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receiver account not found with id: " + request.getReceiverAccountId()
                ));

        if (sender.getStatus() == AccountStatus.FROZEN) {
            throw new AccountFrozenException(
                    "Sender account is frozen. Cannot transfer."
            );
        }

        if (receiver.getStatus() == AccountStatus.FROZEN) {
            throw new AccountFrozenException(
                    "Receiver account is frozen. Cannot receive transfers."
            );
        }

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: " + sender.getBalance()
            );
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = Transaction.builder()
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    public Page<TransactionResponse> getTransactionHistory(
            Long accountId, Pageable pageable) {
        return transactionRepository
                .findBySenderAccountIdOrReceiverAccountId(
                        accountId, accountId, pageable)
                .map(transactionMapper::toResponse);
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id: " + id
                ));
        return transactionMapper.toResponse(transaction);
    }
}