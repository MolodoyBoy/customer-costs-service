package com.oleg.customer.costs.user_management.service;

import com.oleg.customer.costs.user_management.entity.UserDetailsImpl;
import com.oleg.customer.costs.user_management.source.UserManagementSource;
import com.oleg.customer.costs.user_management.value_object.IdentifiedUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserManagementSource userManagementSource;

    public UserDetailsServiceImpl(UserManagementSource userManagementSource) {
        this.userManagementSource = userManagementSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        IdentifiedUser identifiedUser = userManagementSource.loadUserByUsername(username);
        if (identifiedUser == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new UserDetailsImpl(identifiedUser.id(), identifiedUser.username(), identifiedUser.password());
    }
}