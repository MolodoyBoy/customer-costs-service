package com.oleg.customer.costs.user_management;

import com.oleg.customer.costs.user_management.source.UserManagementSource;
import com.oleg.customer.costs.user_management.value_object.IdentifiedUser;
import com.oleg.customer.costs.user_management.value_object.RegisterUserCommand;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.oleg.fund.customer.costs.analytics.Tables.USER_DETAILS;
import static com.oleg.fund.customer.costs.analytics.tables.UserTokens.USER_TOKENS;

@Repository
public class DbUserManagementSource implements UserManagementSource, UserTokenSource {

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

    @Override
    public String getUserToken(int userId, int bankId) {
        return dslContext.select(USER_TOKENS.TOKEN)
            .from(USER_TOKENS)
            .where(USER_TOKENS.USER_ID.eq(userId))
            .and(USER_TOKENS.BANK_ID.eq(bankId))
            .fetchOne(USER_TOKENS.TOKEN);
    }

    @Override
    public void addToken(int userId, int bankId, String token) {
        dslContext.insertInto(USER_TOKENS)
            .values(USER_TOKENS.USER_ID, userId)
            .values(USER_TOKENS.BANK_ID, bankId)
            .values(USER_TOKENS.TOKEN, token)
            .onConflict(USER_TOKENS.USER_ID, USER_TOKENS.BANK_ID)
            .doUpdate()
            .set(USER_TOKENS.TOKEN, token)
            .execute();
    }
}