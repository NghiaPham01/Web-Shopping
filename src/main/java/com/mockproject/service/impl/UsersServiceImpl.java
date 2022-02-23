package com.mockproject.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mockproject.entity.Roles;
import com.mockproject.entity.Users;
import com.mockproject.repository.UsersRepo;
import com.mockproject.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {
	
	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

	@Autowired
	private UsersRepo repo;
	
	@Override
	public Users doLogin(String username, String password) {
		Users user = repo.findByUsername(username);
		
		if (user != null) {
			String hashPassword = user.getHashPassword(); // pw da ma hoa trong DB
			boolean checkPassword = bcrypt.matches(password, hashPassword);
			return checkPassword ? user : null;
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public Users save(Users user) throws SQLException {
		String hashPassword = bcrypt.encode(user.getHashPassword()); // ma hoa
		user.setHashPassword(hashPassword); // luu pw da ma hoa vao lai user
		user.setIsDeleted(Boolean.FALSE);
		user.setRole(new Roles(3L, "user"));
		return repo.saveAndFlush(user);
	}

	@Override
	public List<Users> findAll() {
		return repo.findByIsDeleted(Boolean.FALSE);
	}

	@Transactional
	@Override
	public void deleteLogical(String username) {
		repo.deleteLogical(username);
	}

	@Override
	public Users findByUsername(String username) {
		return repo.findByUsername(username);
	}

	@Transactional
	@Override
	public void update(Users user) throws Exception {
		if (ObjectUtils.isEmpty(user)) {
			throw new Exception("User cannot be null");
		}
		
		if (user.getHashPassword().length() > 0) {
			String hashPassword = bcrypt.encode(user.getHashPassword());
			repo.update(user.getFullname(), user.getEmail(), hashPassword, user.getUsername());
		} else {
			repo.updateNonPass(user.getFullname(), user.getEmail(), user.getUsername());
		}
	}
}
