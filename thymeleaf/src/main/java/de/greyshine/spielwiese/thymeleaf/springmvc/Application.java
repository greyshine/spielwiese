package de.greyshine.spielwiese.thymeleaf.springmvc;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.UrlTemplateResource;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		
		final int port = 8080;
	
		args = new String[] {"--server.port="+port};
		
		SpringApplication.run( Application.class, args )  ;
		
		System.out.println( "start in browser with: http://localhost:"+ port );
	}
	
	/**
	 * I decided to implement my own 'ClasspathTemplateResolver' since the examples were throwing {@link FileNotFoundException} at me and I wanted to serve out of a classpath resource. The exception was either misleading or I knew I was on my own track. 
	 * Implementing it myself also gave me some inside look and feel into the frameworks used.   
	 * @param inServletContext
	 * @return the resolver for the {@link Controller}'s method {@link String} return-values
	 */
	@Bean
	public ITemplateResolver templateResolver(ServletContext inServletContext) {
	    
		return new ITemplateResolver() {
	    	
	    	private TemplateMode evalTemplateMode(String template) {
	    		
	    		if ( template == null ) { return TemplateMode.TEXT; }
	    		
	    		template = template.toLowerCase();
	    		
	    		if ( template.endsWith( ".html" ) ) {
	    			return TemplateMode.HTML; 
	    		}
	    		if ( template.endsWith( ".htm" ) ) {
	    			return TemplateMode.HTML; 
	    		}
	    		if ( template.endsWith( ".xml" ) ) {
	    			return TemplateMode.XML; 
	    		}
	    		if ( template.endsWith( ".xhtml" ) ) {
	    			return TemplateMode.XML; 
	    		}
	    		if ( template.endsWith( ".css" ) ) {
	    			return TemplateMode.CSS; 
	    		}
	    		if ( template.endsWith( ".js" ) ) {
	    			return TemplateMode.JAVASCRIPT; 
	    		}
	    		
	    		return TemplateMode.TEXT;
	    	}
	    	
	    	@Override
			public TemplateResolution resolveTemplate(IEngineConfiguration configuration, String ownerTemplate, String template,
					Map<String, Object> templateResolutionAttributes) {
				
				final URL theUrl = Thread.currentThread().getContextClassLoader().getResource( template );
				if ( theUrl == null ) {
					return null;
				}
				
				final ITemplateResource theTemplateResource = new UrlTemplateResource( theUrl , "UTF-8");
				
				final TemplateMode theTemplateMode = evalTemplateMode(template);
				
				return new TemplateResolution( theTemplateResource, theTemplateMode, NonCacheableCacheEntryValidity.INSTANCE);
			}
			
			@Override
			public Integer getOrder() {
				return 0;
			}
			
			@Override
			public String getName() {
				return getClass().getCanonicalName();
			}
		};
	}
	
	@Bean
	public SpringTemplateEngine templateEngine(ServletContext inServletContext) {
	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver(inServletContext));
	    templateEngine.addDialect(new SpringSecurityDialect());
	    templateEngine.addDialect(new Java8TimeDialect());
	    return templateEngine;
	}
	
	@Bean
	public ViewResolver viewResolver(ServletContext inServletContext) {
	    ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
	    thymeleafViewResolver.setTemplateEngine(templateEngine(inServletContext));
	    thymeleafViewResolver.setCharacterEncoding("UTF-8");
	    return thymeleafViewResolver;
	}
}
