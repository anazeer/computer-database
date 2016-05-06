package com.excilys.cdb.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.implementation.UserDAO;
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
public class UserService implements IService<User> {
	
    @Autowired
    private UserDAO dao;
     
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
	public void delete(Long id) {
		dao.delete(id);
	}
}