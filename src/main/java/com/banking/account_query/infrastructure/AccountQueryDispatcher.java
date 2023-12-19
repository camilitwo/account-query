package com.banking.account_query.infrastructure;

import com.banking.cqrs_core.domain.BaseEntity;
import com.banking.cqrs_core.infrastructure.QueryDispatcher;
import com.banking.cqrs_core.queries.BaseQuery;
import com.banking.cqrs_core.queries.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();
    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> queryType, QueryHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(queryType, k -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        var handlers = routes.get(query.getClass());
        if (handlers == null || handlers.isEmpty()) {
            throw new IllegalArgumentException("No handler registered for " + query.getClass());
        }
        if(handlers.size()>1)
            throw new IllegalArgumentException("More than one handler registered for " + query.getClass());

        return handlers.get(0).handle(query);
    }
}
