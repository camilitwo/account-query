package com.banking.account_query.api.controllers;

import com.banking.account_query.api.dto.AccountLookupResponse;
import com.banking.account_query.api.dto.EqualityType;
import com.banking.account_query.api.queries.FindAccountByHolderQuery;
import com.banking.account_query.api.queries.FindAccountByIdQuery;
import com.banking.account_query.api.queries.FindAccountWithBalanceQuery;
import com.banking.account_query.api.queries.FindAllAccountsQuery;
import com.banking.account_query.domain.BankAccount;
import com.banking.cqrs_core.infrastructure.QueryDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/accounts-lookup")
public class AccountLookupController {

    private final QueryDispatcher queryDispatcher;

    public AccountLookupController(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    @GetMapping(path = "/")
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        List<BankAccount> accounts =
                queryDispatcher.send(new FindAllAccountsQuery());
        if (accounts == null || accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(AccountLookupResponse.builder()
                .accounts(accounts)
                .message("Successfully returned " + accounts.size() + " accounts")
                .build());
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable(value = "id") String id) {
        List<BankAccount> account = queryDispatcher.send(new FindAccountByIdQuery(id));
        if (account == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(AccountLookupResponse.builder()
                .accounts(account)
                .message("Successfully returned account with id " + id)
                .build());
    }

    @GetMapping(path = "/holder")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@RequestParam(value = "holder") String holder) {
        List<BankAccount> account = queryDispatcher.send(new FindAccountByHolderQuery(holder));
        if (account == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(AccountLookupResponse.builder()
                .accounts(account)
                .message("Successfully returned account with holder " + holder)
                .build());
    }

    @GetMapping(path = "/balance/{balance-type}")
    public ResponseEntity<AccountLookupResponse> getAccountByBalanceType(@PathVariable(value = "balance-type") String balanceType,
                                                                         @RequestParam(value = "balance") double balance) {
        List<BankAccount> account = queryDispatcher.send(
                new FindAccountWithBalanceQuery(balance, convertToEqualityType(balanceType)));
        if (account == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(AccountLookupResponse.builder()
                .accounts(account)
                .message("Successfully returned account with balance type " + balanceType)
                .build());
    }

    private EqualityType convertToEqualityType(String balanceType) {
        return Arrays.stream(EqualityType.values())
                .filter(type -> type.name().equalsIgnoreCase(balanceType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo de balance no válido: " + balanceType));
    }
}
