package de.greyshine.spielwiese.thymeleaf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.templatemode.TemplateMode;

public class GeneratorTester {
	
	@Test
	public void testText() {
		
		String theTemplate = "key=[# th:text=\"${value}\" /]"; 
		
		Map<String,Object> m = new HashMap<>();
		m.put( "value" , "value");
		
		final Generator g = Generator.create( TemplateMode.TEXT );
		
		final String theResult = g.generate( theTemplate, m );
		Assert.assertEquals( "key=value" , theResult);

	}

	@Test
	public void testHtml() {
		
		String theTemplate = "<html><body>Hello <span th:text=\"${value}\"></span>!</body></html>"; 
		
		Map<String,Object> m = new HashMap<>();
		m.put( "value" , "World");
		
		final Generator g = Generator.create( TemplateMode.HTML );
		final String theResult = g.generate( theTemplate, m );
		
		System.out.println( theResult );
		
		Assert.assertEquals( "<html><body>Hello <span>World</span>!</body></html>" , theResult);
		
	}

	@Test
	public void testCached() {
		
		final Map<String,Object> m = new HashMap<>();
		m.put( "value" , "World");
		
		final Generator g = Generator.create();
		g.cacheTemplate("text", "key=[# th:text=\"${value}\" /]", TemplateMode.TEXT);
		g.cacheTemplate("html", "<html><body>Hello <span th:text=\"${value}\"></span>!</body></html>", TemplateMode.HTML);
		
		String theResult = g.generateCached("text", m);
		
		System.out.println( theResult );
		Assert.assertEquals( "key=World" , theResult);
		
		theResult = g.generateCached("html", m);
		Assert.assertEquals( "<html><body>Hello <span>World</span>!</body></html>" , theResult);
		
	}
	
}
