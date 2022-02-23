package com.mockproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mockproject.entity.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long>{

	Users findByUsername(String username);
	
	// SELECT * FROM users WHERE isDeleted = 0
	List<Users> findByIsDeleted(Boolean isDeleted);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET isDeleted = 1 WHERE username = ?", nativeQuery = true)
	void deleteLogical(String username);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET fullname = ?1, email = ?2 WHERE username = ?3", nativeQuery = true)
	void updateNonPass(String fullname, String email, String username);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET fullname = ?1, email = ?2, hashPassword = ?3 WHERE username = ?4", 
		nativeQuery = true)
	void update(String fullname, String email, String hashPassword, String username);
}
