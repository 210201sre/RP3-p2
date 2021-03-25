package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.models.User;
import com.revature.repositories.UserDAO;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	UserService userService;
	
	@Mock
	UserDAO userDAO;
	
	
	@Test
	public void registerUserTest() {
		User u = new User(1, "jdoe@gmail.com", "password", null);
		
		userService.register(u);
				
		verify(userDAO, times(1)).insertToUsers(u.getEmail(), u.getPassword());;
		
	}
	
//	@Test
//	public void loginUserTest() {
//		User u = new User(1, "jdoe@gmail.com", "password", null);
//		
//		when(userDAO.findByEmail()).thenReturn(u);
//		
//		userService.login(u);
//		
//		
//		verify(userDAO, times(1)).findByEmailAndPassword(u.getEmail(), u.getPassword());
//		
//	}
	
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