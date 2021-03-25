package com.revature.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.models.Cart;
import com.revature.models.Item;
import com.revature.models.User;
import com.revature.repositories.CartDAO;
import com.revature.repositories.OrderDAO;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

	@InjectMocks
	CartService cartService;
	
	@Mock
	CartDAO cartDAO;
	
	@Mock
	OrderDAO orderDAO;
	
	User u = new User(1, "jdoe@gmail.com", "password", null);
	Item i = new Item(1, "chair", 10.0);
	
	@Test
	public void addItemToCartTest() {
		//Cart c = new Cart(1, 1, 1);
		//Item i = new Item(1, "chair", 5.0);
		
		cartService.addItemToCart(i.getId(), u.getId());
		
		verify(cartDAO, times(1)).insertToCarts(1, 1);
	}
	
//	@Test
//	public void addItemToOrderTest() {
//		Cart c = new Cart(1, 1, 1);
//		
//		cartService.addItemToOrder(u.getId());
//		
//		verify(cartDAO, times(1)).deleteById(u.getId());
//		verify(orderDAO, times(1)).insertToOrders(i.getId(), u.getId());;
//		
//	}

}
