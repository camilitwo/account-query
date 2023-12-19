package com.banking.account_query;

import com.banking.account_query.api.queries.*;
import com.banking.cqrs_core.infrastructure.QueryDispatcher;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountQueryApplication {

	private final QueryDispatcher queryDispatcher;
	private final QueryHandler queryHandler;

    public AccountQueryApplication(QueryDispatcher queryDispatcher, QueryHandler queryHandler) {
        this.queryDispatcher = queryDispatcher;
        this.queryHandler = queryHandler;
    }

    public static void main(String[] args) {
		SpringApplication.run(AccountQueryApplication.class, args);
	}

	@PostConstruct
	public void init() {
		queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
	}
}
