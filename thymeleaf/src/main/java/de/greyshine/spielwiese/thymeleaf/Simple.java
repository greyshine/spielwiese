package de.greyshine.spielwiese.thymeleaf;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class Simple {
	
	public static void main(String[] args) {
		
		System.out.println("HTML");
		System.out.println("----");
		System.out.println("");
		
		htmlExample();
		
		System.out.println("");
		System.out.println("Properties");
		System.out.println("----------");
		System.out.println("");
		
		textExample();
	}
	
	public static void htmlExample() {
		
		final List<String> thePersons = new ArrayList<>();
		thePersons.add( "Kaiser Franz" );
		thePersons.add( "Buddha" );

		final Map<String,String> theItems = new LinkedHashMap<>();
		theItems.put( "key-item-1","Value-Item-1" );
		theItems.put( "key-item-2","Value-Item-2" );
		
		final TemplateEngine theTemplateEngine = new TemplateEngine();
		
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		theTemplateEngine.setTemplateResolver(templateResolver);
		templateResolver.setTemplateMode( TemplateMode.HTML );
		templateResolver.setCharacterEncoding( "UTF-8" );
		
		final Context theContext = new Context();
		theContext.setVariable("name", "World");
		theContext.setVariable("persons", thePersons);
		theContext.setVariable("items", theItems);
		
		final StringWriter theOutputWriter = new StringWriter();
		
		theTemplateEngine.process("simple/template.html", theContext, theOutputWriter);

		System.out.println(theOutputWriter);
	}

	public static void textExample() {
		
		final TemplateEngine theTemplateEngine = new TemplateEngine();
		
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		theTemplateEngine.setTemplateResolver(templateResolver);
		templateResolver.setTemplateMode( TemplateMode.TEXT );
		templateResolver.setCharacterEncoding( "UTF-8" );
		
		final Context theContext = new Context();
		theContext.setVariable("value", "hello world");
		
		final StringWriter theOutputWriter = new StringWriter();
		
		theTemplateEngine.process("simple/test.properties", theContext, theOutputWriter);
		
		System.out.println(theOutputWriter);
	}
	
}

