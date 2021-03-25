package com.revature.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

//import com.revature.models.Cart;
import com.revature.models.Item;
import com.revature.models.User;
import com.revature.repositories.CartDAO;
import com.revature.repositories.OrderDAO;

import io.micrometer.core.instrument.MeterRegistry;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

	@Mock
	MeterRegistry meterRegistery = Mockito.mock(MeterRegistry.class);
	
	@Mock
	private CartService cartService;
	
	@Mock
	CartDAO cartDAO;

	@Mock
	OrderDAO orderDAO;
	
	@BeforeEach
	void init() {
		this.cartService = new CartService(meterRegistery);
	}
	
//	@Test
//	public void addItemToCartTest() {
//		
//		User u = new User(1, "jdoe@gmail.com", "password", null);
//		Item i = new Item(1, "chair", 10.0);
//		
//		cartService.addItemToCart(i.getId(), u.getId());
//		
//		verify(cartDAO, times(1)).insertToCarts(1, 1);
//	}
	
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
