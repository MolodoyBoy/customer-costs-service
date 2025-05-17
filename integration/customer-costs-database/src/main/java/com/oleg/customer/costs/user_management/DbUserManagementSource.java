package com.oleg.customer.costs.user_management;

import com.oleg.customer.costs.user_management.source.UserManagementSource;
import com.oleg.customer.costs.user_management.value_object.IdentifiedUser;
import com.oleg.customer.costs.user_management.value_object.RegisterUserCommand;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.oleg.fund.customer.costs.analytics.Tables.USER_DETAILS;

@Repository
public class DbUserManagementSource implements UserManagementSource {

    private final DSLContext dslContext;
    private final UserDetailsMapper userDetailsMapper;

    public DbUserManagementSource(DSLContext dslContext, UserDetailsMapper userDetailsMapper) {
        this.dslContext = dslContext;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public Integer save(RegisterUserCommand command) {
        return dslContext.insertInto(USER_DETAILS)
            .set(userDetailsMapper.toRecord(command))
            .onConflict(USER_DETAILS.USERNAME)
            .doNothing()
            .returning(USER_DETAILS.ID)
            .fetch(USER_DETAILS.ID)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public IdentifiedUser loadUserByUsername(String username) {
        return dslContext.select()
            .from(USER_DETAILS)
            .where(USER_DETAILS.USERNAME.eq(username))
            .fetch(userDetailsMapper)
            .stream()
            .findFirst()
            .orElse(null);
    }
}