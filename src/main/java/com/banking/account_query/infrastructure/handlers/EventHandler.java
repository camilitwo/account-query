package com.banking.account_query.infrastructure.handlers;

import com.banking.account_command.events.AccountClosedEvent;
import com.banking.account_command.events.AccountOpenedEvent;
import com.banking.account_command.events.FundsDepositedEvent;
import com.banking.account_command.events.FundsWithdrawnEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
    void on(FundsDepositedEvent event);
    void on(FundsWithdrawnEvent event);
    void on(AccountClosedEvent event);
}
