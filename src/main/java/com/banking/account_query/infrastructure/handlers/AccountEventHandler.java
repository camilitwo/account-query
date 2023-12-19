package com.banking.account_query.infrastructure.handlers;

import com.banking.account_command.events.AccountClosedEvent;
import com.banking.account_command.events.AccountOpenedEvent;
import com.banking.account_command.events.FundsDepositedEvent;
import com.banking.account_command.events.FundsWithdrawnEvent;
import com.banking.account_query.domain.AccountRepository;
import com.banking.account_query.domain.BankAccount;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler{

    private final AccountRepository accountRepository;

    public AccountEventHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void on(AccountOpenedEvent event) {
        var account = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreateDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();
        accountRepository.save(account);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        var account = accountRepository.findById(event.getId()).orElseThrow(
                () -> new RuntimeException("Account not found with id: " + event.getId())
        );
        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        var account = accountRepository.findById(event.getId()).orElseThrow(
                () -> new RuntimeException("Account not found with id: " + event.getId())
        );
        if (account.getBalance() < event.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - event.getAmount());
        accountRepository.save(account);
    }

    @Override
    public void on(AccountClosedEvent event) {
        var account = accountRepository.findById(event.getId()).orElseThrow(
                () -> new RuntimeException("Account not found with id: " + event.getId())
        );
        accountRepository.delete(account);
    }
}
