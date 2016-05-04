package com.excilys.cdb.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.User;
import com.excilys.cdb.model.UserRole;

/**
 * Retrieve an user by his username, and check the password with BCrypt
 */
@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {
 
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageSource messageSource;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException(messageSource.getMessage("security.error.login", null, LocaleContextHolder.getLocale()));
		}
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), getGrantedAuthorities(user));
	}
 
     
    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (UserRole role : user.getUserRole()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
        }
        return authorities;
    }
}