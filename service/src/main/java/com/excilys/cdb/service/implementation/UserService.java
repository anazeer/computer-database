package com.excilys.cdb.service.implementation;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.implementation.UserDao;
import com.excilys.cdb.model.User;
import com.excilys.cdb.pagination.AbstractPage;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.util.Constraint;

/**
 * Service implementation for User
 */
@Transactional
@Service
public class UserService implements IService<User>, UserDetailsService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDao dao;
     
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
 
	@Override
	public User findById(Long id) {
		return dao.findById(id);
	}
	
	/**
	 * Find an User by his username
	 * @param username the User username
	 * @return the User associated to username, null otherwise
	 */
	public User findByUserName(String username) {
		return dao.findByUsername(username);
	}

	@Override
	public AbstractPage<User> getPage(PageRequest page) {
		throw new NoSuchMethodError("Call to non implemented method");
	}

	@Override
	public List<User> list(Constraint constraint) {
		return dao.find(constraint);
	}

	@Override
	public int count(Constraint constraint) {
		return dao.count(constraint);
	}
	
	@Override
	public User create(User obj) {
        obj.setPassword(passwordEncoder().encode(obj.getPassword()));
        return dao.create(obj);
	}

	@Override
	public boolean delete(Long id) {
		return dao.delete(id);
	}

    /**
     * Retrieve an user by his username, and check the password with BCrypt
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException(messageSource.getMessage("security.error.login", null, LocaleContextHolder.getLocale()));
		}
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), getGrantedAuthorities(user));
	}

    /**
     * Retrieve the user roles
     * @param user the username
     * @return the list of the user roles
     */
	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (UserRole role : user.getUserRole()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
		}
		return authorities;
	}
}