package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import com.revature.exceptions.LoginUserFailedException;
import com.revature.exceptions.RegisterUserFailedException;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

	@InjectMocks
	UserService userService;
	
	@Mock
	UserDAO userDAO;
	
	@BeforeEach
	public void init( ) {
		User regUser = new User(1, "hjames@gmail.com", "pass", null);
		userService.register(regUser);
	}
	
	@Test
	public void registerUserTest() {
		User u = new User(1, "jdoe@gmail.com", "password", null);
		
		userService.register(u);
				
		verify(userDAO, times(1)).insertToUsers(u.getEmail(), u.getPassword());
		
	}
	
	@Test
	public void registerUserTest2() {
		User u = new User(1, "jdoe@gmail.com", "password", null);
		
		try {
					
			lenient().when(userDAO.findByEmail("jdoe@gmail.com")).thenReturn(Optional.of(u));
					
			userService.register(u);
		} catch(RegisterUserFailedException e) {
			assertEquals(RegisterUserFailedException.class, e.getClass());
		}
		
	}
	

	
	@Test
	public void loginUserTest() {
		
		User regUser = new User(1, "hjames@gmail.com", "pass", null);
		userDAO.insertToUsers(regUser.getEmail(), regUser.getPassword());
		
		when(userDAO.findByEmailAndPassword("hjames@gmail.com", "pass")).thenReturn(Optional.of(regUser));
		
		userService.login(regUser);
		
		verify(userDAO, times(1)).findByEmailAndPassword(regUser.getEmail(), regUser.getPassword());
		
	}
	
	
	@Test
	public void loginUserTest2() {
		
		try {
			User regUser = new User(1, "ajames@gmail.com", "pass", null);
			userDAO.insertToUsers(regUser.getEmail(), regUser.getPassword());
					
			lenient().when(userDAO.findByEmailAndPassword("hjames@gmail.com", "pass")).thenReturn(Optional.of(regUser));
					
			Optional<User> optUser = userService.login(regUser);
		} catch(LoginUserFailedException e) {
			assertEquals(LoginUserFailedException.class, e.getClass());
		}
		
	}
	
	@Test
	public void loginUserTest3() {
		
		try {
			User regUser = new User(1, "hjames@gmail.com", "pass", null);
			userDAO.insertToUsers(regUser.getEmail(), regUser.getPassword());
					
			lenient().when(userDAO.findByEmailAndPassword("hjames@gmail.com", "bass")).thenReturn(Optional.of(regUser));
					
			Optional<User> optUser = userService.login(regUser);
		} catch(LoginUserFailedException e) {
			assertEquals(LoginUserFailedException.class, e.getClass());
		}
		
	}
	
	@Test
	public void loginUserTest4() {
		
		try {
			User regUser = new User(1, "ajames@gmail.com", "pass", null);
			userDAO.insertToUsers(regUser.getEmail(), regUser.getPassword());
					
			lenient().when(userDAO.findByEmailAndPassword("hjames@gmail.com", "bass")).thenReturn(Optional.of(regUser));
					
			Optional<User> optUser = userService.login(regUser);
		} catch(LoginUserFailedException e) {
			assertEquals(LoginUserFailedException.class, e.getClass());
		}
		
	}
	
	@Test
	public void getAllUsersTest() {
	
		List<User> users = new ArrayList<User>();
		User user1 = new User(1, "jdoe@gmail.com", "password", null);
		User user2 = new User(2, "kdoe@gmail.com", "pass", null);
		User user3 = new User(3, "ldoe@gmail.com", "word", null);
		
		users.add(user1);
		users.add(user2);
		users.add(user3);

		when(userDAO.findAll()).thenReturn(users);
		
		//test
		List<User> userList = userService.getAllUsers();
		
		System.out.println(users);
		
		assertEquals(3, userList.size());
		verify(userDAO, times(1)).findAll();
		
	}
	
}