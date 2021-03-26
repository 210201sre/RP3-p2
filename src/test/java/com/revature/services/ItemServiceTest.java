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

import com.revature.models.Item;
import com.revature.repositories.ItemDAO;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

	@InjectMocks
	ItemService itemService;
	
	@Mock
	ItemDAO itemDAO;
	
	@Test
	public void getItemsTest() {
		
		System.out.println("Testing getItems method");
		
		List<Item> items = new ArrayList<Item>();
		Item item1 = new Item(1, "chair", 10.0);
		Item item2 = new Item(2, "table", 20.0);
		Item item3 = new Item(3, "plate", 3.0);
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		
		when(itemDAO.findAll()).thenReturn(items);
		
		//test
		List<Item> itemList = itemService.getItems();
		
		System.out.println(itemList);
		
		assertEquals(3, itemList.size());
		verify(itemDAO, times(1)).findAll();
		
	}

}
