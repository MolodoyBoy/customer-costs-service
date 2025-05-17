package com.oleg.customer.costs.user_management;

import com.oleg.customer.costs.common.ToRecordMapper;
import com.oleg.customer.costs.user_management.value_object.IdentifiedUser;
import com.oleg.customer.costs.user_management.value_object.RegisterUserCommand;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.Tables.USER_DETAILS;

@Component
public class UserDetailsMapper implements RecordMapper<Record, IdentifiedUser>, ToRecordMapper<RegisterUserCommand> {

    private final DSLContext dslContext;

    public UserDetailsMapper(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public IdentifiedUser map(Record rc) {
        return new IdentifiedUser(
            rc.get(USER_DETAILS.ID),
            rc.get(USER_DETAILS.USERNAME),
            rc.get(USER_DETAILS.PASSWORD)
        );
    }

    public Record toRecord(RegisterUserCommand command) {
        Record rc = dslContext.newRecord(USER_DETAILS);
        rc.set(USER_DETAILS.EMAIL, command.email());
        rc.set(USER_DETAILS.USERNAME, command.username());
        rc.set(USER_DETAILS.PASSWORD, command.password());

        return rc;
    }
}