package com.mockproject.service;

import java.sql.SQLException;
import java.util.List;

import com.mockproject.entity.Users;

public interface UsersService {

	Users doLogin(String username, String password);
	Users save(Users user) throws SQLException;
	Users findByUsername(String username);
	List<Users> findAll();
	void deleteLogical(String username);
	void update(Users user) throws Exception;
}
