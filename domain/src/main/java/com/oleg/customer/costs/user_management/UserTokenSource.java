package com.oleg.customer.costs.user_management;

public interface UserTokenSource {

    String getUserToken(int userId, int bankId);

    void addToken(int userId, int bankId, String token);
}
