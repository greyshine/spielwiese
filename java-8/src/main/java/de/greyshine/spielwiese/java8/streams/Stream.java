package de.greyshine.spielwiese.java8.streams;

import java.util.Arrays;

import org.junit.Test;

public class Stream {
	
	@Test
	public void forEach() {
		
		final Character[] cs = {'A','B'};
		
		Arrays.stream( cs ).forEach( c -> {
			System.out.println( c );
		} );
	}
}
