package de.greyshine.spielwiese.thymeleaf.springmvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SomeService {

	public Collection<String> getPersons() {
		final List<String> thePersons = new ArrayList<>();
		thePersons.add( "Donald" );
		thePersons.add( "Ronald" );
		return thePersons;
	}

	public Map<String,String> getItems() {
		final Map<String,String> theItems = new LinkedHashMap<>();
		theItems.put( "Benjamin", "Franklin" );
		theItems.put( "George", "Washington" );
		return theItems;
	}
	
}
