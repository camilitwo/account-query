package com.banking.account_query.api.queries;

import com.banking.account_query.domain.AccountRepository;
import com.banking.account_query.domain.BankAccount;
import com.banking.cqrs_core.domain.BaseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountQueryHandler implements QueryHandler{

    private final AccountRepository accountQueryRepository;

    public AccountQueryHandler(AccountRepository accountQueryRepository) {
        this.accountQueryRepository = accountQueryRepository;
    }

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = accountQueryRepository.findAll();
        List<BaseEntity> accounts = new ArrayList<>();
        bankAccounts.forEach(accounts::add);
        return accounts;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        var bankAccounts = accountQueryRepository.findById(query.getId());
        if (bankAccounts.isEmpty()){
            return null;
        }
        List<BaseEntity> accounts = new ArrayList<>();
        bankAccounts.ifPresent(accounts::add);
        return accounts;
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {

        return switch (query.getEqualityType()) {
            case GREATER_THAN -> accountQueryRepository.findByBalanceGreaterThan(query.getBalance());
            case LESS_THAN -> accountQueryRepository.findByBalanceLessThan(query.getBalance());
            default ->
                // Manejar otros casos de igualdad según sea necesario
                    Collections.emptyList(); // O lanzar una excepción, según la lógica de tu aplicación.
        };
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        Optional<List<BankAccount>> bankAccounts = accountQueryRepository.findByAccountHolder(query.getAccountHolder());
        if (bankAccounts.isEmpty()){
            return null;
        }
        List<BaseEntity> accounts = new ArrayList<>();
        bankAccounts.ifPresent(accounts::addAll);
        return accounts;
    }
}
