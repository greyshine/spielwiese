package de.greyshine.spielwiese.thymeleaf;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Adapter for simplier usage of Thymeleaf's {@link TemplateEngine}. 
 */
public class Generator {
	
	public static final Charset UTF8 = Charset.forName( "UTF-8" );
	
	private static final OutputStreamWriter WRITER_SYSTEMOUT = new OutputStreamWriter( System.out );
	private static Map<String,Object> EMPTY_MAP_STRING_OBJECT = Collections.unmodifiableMap( new HashMap<>() );
	
	
	private final TemplateEngine templateEngine = new TemplateEngine();
	private TemplateMode templateMode;
	private Charset outputCharset = UTF8;
	
	private Map<String,CachedTemplate> templates = new HashMap<>();
	private CachedTemplate EMPTY_CACHED_TEMPLATE = new CachedTemplate("", TemplateMode.RAW);
	
	
	private Generator(TemplateMode inTemplateMode) {
		templateMode = inTemplateMode == null ? TemplateMode.RAW : inTemplateMode;
	}

	public static Generator create() {
		return new Generator(TemplateMode.RAW);
	}
	public static Generator create( TemplateMode inTemplateMode ) {
		return new Generator(inTemplateMode);
	}
	
	public Generator cacheTemplate(String inName, String inTemplate, TemplateMode inTemplateMode) {
		templates.put(inName, new CachedTemplate( inTemplate, inTemplateMode ) );
		return this;
	}
	
	public String generateCached(String inCachedTemplateName, Map<String,Object> inVariables) {
		
		CachedTemplate theCt = templates.get( inCachedTemplateName );
		theCt = theCt == null ? EMPTY_CACHED_TEMPLATE : theCt;
		
		return generate(theCt.template, theCt.templateMode, inVariables);
	}
	
	public String generate(String inTemplate, Map<String,Object> inVariables) {
		return generate(inTemplate, templateMode, inVariables);
	}
	
	public String generate(String inTemplate, TemplateMode inTemplateMode, Map<String,Object> inVariables) {
		
		final StringWriter out = new StringWriter();
		
		generate(inTemplate, inTemplateMode, out, inVariables);
		
		return out.toString();
	}

	public Generator outputEncoding(String inOutputCharset) {
		outputCharset = inOutputCharset == null ? Charset.defaultCharset() : Charset.forName( inOutputCharset );
		return this;
	}
	
	public InputStream generate(URL inUrl, String inInputCharset, Map<String,Object> inVariables ) throws IOException {
		
		final StringBuilder sb = new StringBuilder();
		
		InputStream is = null;
		
		try {
		
			final Reader r = new InputStreamReader( is = inUrl.openStream() , inInputCharset == null ? Charset.defaultCharset() : Charset.forName(inInputCharset));

			while( r.ready() ) {
				sb.append( (char)r.read() );
			}
			
		} finally {
			close( is );
		}
		
		final String theResult = generate(sb.toString(), inVariables);
		
		return new ByteArrayInputStream( theResult.getBytes( outputCharset ) );
	}
	
	private static void close(Closeable c) {
		try {
			c.close();
		} catch (Exception e) {
			// swallow
		}
	}

	public void generate(String inTemplate, Writer inWriter, Map<String,Object> inVariables) {
		generate(inTemplate, templateMode, inWriter, inVariables  );
	}
	
	public void generate(String inTemplate, TemplateMode inTemplateMode, Writer inWriter, Map<String,Object> inVariables) {

		inTemplate = inTemplate == null ? "" : inTemplate;
		inTemplateMode = inTemplateMode == null ? TemplateMode.RAW : inTemplateMode;
		
		inWriter = inWriter != null ? inWriter : WRITER_SYSTEMOUT;
		
		final Context theContext = new Context();
		theContext.setVariables( inVariables == null ? EMPTY_MAP_STRING_OBJECT : inVariables );
		
		final TemplateSpec theTemplateSpec = new TemplateSpec(inTemplate , inTemplateMode);
		
		templateEngine.process( theTemplateSpec, theContext, inWriter );
	}
	
	private static class CachedTemplate {
		
		final TemplateMode templateMode;
		final String template;
		
		public CachedTemplate(String template, TemplateMode templateMode) {
			this.template = template;
			this.templateMode = templateMode;
		}
	}

	
}
