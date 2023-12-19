package com.banking.account_query.infrastructure.consumers;

import com.banking.account_command.events.AccountClosedEvent;
import com.banking.account_command.events.AccountOpenedEvent;
import com.banking.account_command.events.FundsDepositedEvent;
import com.banking.account_command.events.FundsWithdrawnEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload AccountOpenedEvent event, Acknowledgment ack);//ACK is used to commit the offset
    void consume(@Payload FundsDepositedEvent event, Acknowledgment ack);
    void consume(@Payload FundsWithdrawnEvent event, Acknowledgment ack);
    void consume(@Payload AccountClosedEvent event, Acknowledgment ack);
}
