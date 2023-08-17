package org.jivesoftware.openfire.plugin;

import org.glassfish.jersey.server.ResourceConfig;
import org.jivesoftware.openfire.plugin.rest.AuthFilter;
import org.jivesoftware.openfire.plugin.rest.CORSFilter;
import org.jivesoftware.openfire.plugin.rest.CustomJacksonMapperProvider;
import org.jivesoftware.openfire.plugin.rest.StatisticsFilter;

import org.jivesoftware.util.JiveGlobals;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ifsoft.openfire.SparkWebAPI;

/**
 * The Class JerseyWrapper.
 */
public class JerseyWrapper extends ResourceConfig {

    /** The Constant CUSTOM_AUTH_PROPERTY_NAME */
    private static final String CUSTOM_AUTH_PROPERTY_NAME = "org.jivesoftware.openfire.plugin.rest.AuthFilter";
    
    /** The Constant REST_AUTH_TYPE */
    private static final String REST_AUTH_TYPE  = "plugin.restapi.httpAuth";
    
    /** The Constant JERSEY_LOGGER. */
    private final static Logger JERSEY_LOGGER = Logger.getLogger("org.glassfish.jersey");
    
    private static String loadingStatusMessage = null;
    
    static {
        JERSEY_LOGGER.setLevel(Level.SEVERE);
    }

    public static String tryLoadingAuthenticationFilter(String customAuthFilterClassName) {
        
        try {
            if(customAuthFilterClassName != null) {
                Class.forName(customAuthFilterClassName, false, JerseyWrapper.class.getClassLoader());
                loadingStatusMessage = null;
            }
        } catch (ClassNotFoundException e) {
            loadingStatusMessage = "No custom auth filter found for restAPI plugin with name " + customAuthFilterClassName;
        }
        
        if(customAuthFilterClassName == null || customAuthFilterClassName.isEmpty())
            loadingStatusMessage = "Classname field can't be empty!";
        return loadingStatusMessage;
    }
    
    public String loadAuthenticationFilter() {
            
        // Check if custom AuthFilter is available
        String customAuthFilterClassName = JiveGlobals.getProperty(CUSTOM_AUTH_PROPERTY_NAME);
        String restAuthType = JiveGlobals.getProperty(REST_AUTH_TYPE);
        Class<?> pickedAuthFilter = AuthFilter.class;
        
        try {
            if(customAuthFilterClassName != null && "custom".equals(restAuthType)) {
                pickedAuthFilter = Class.forName(customAuthFilterClassName, false, JerseyWrapper.class.getClassLoader());
                loadingStatusMessage = null;
            }
        } catch (ClassNotFoundException e) {
            loadingStatusMessage = "No custom auth filter found for restAPI plugin! " + customAuthFilterClassName + " " + restAuthType;
        }
        
        register(pickedAuthFilter);
        return loadingStatusMessage;
    }
    
    /**
     * Instantiates a new jersey wrapper.
     */
    public JerseyWrapper(@Context ServletConfig servletConfig) {

        // Filters
		register(CORSFilter.class);
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		
        loadAuthenticationFilter();
        register(StatisticsFilter.class);

        // Services
        registerClasses(
            SparkWebAPI.class
        );

        register(RESTExceptionMapper.class);
        register(CustomJacksonMapperProvider.class);
		loadingStatusMessage = "ok";
    }
    
    /*
     * Returns the loading status message.
     *
     * @return the loading status message.
     */
    public static String getLoadingStatusMessage() {
        return loadingStatusMessage;
    }
    
}
