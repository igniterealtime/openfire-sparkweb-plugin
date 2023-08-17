package org.jivesoftware.openfire.plugin.rest;

import org.ifsoft.openfire.SparkWeb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jitsi.util.OSUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.AuthFactory;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.util.JiveGlobals;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import net.sf.json.JSONObject;

/**
 * The Class AuthFilter.
 */
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class AuthFilter implements ContainerRequestFilter {
    private static Logger Log = LogManager.getLogger(AuthFilter.class);
    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequest) throws IOException {
        Log.debug("authenticating " + containerRequest.getUriInfo().getRequestUri().getPath());

        if (containerRequest.getUriInfo().getRequestUri().getPath().endsWith("swagger.json") || containerRequest.getUriInfo().getRequestUri().getPath().endsWith("swagger.yaml")) {
            Log.debug("AuthFilter swagger definition");
            return;
		
        } else if (containerRequest.getUriInfo().getRequestUri().getPath().contains("rest/config/global")) {
            Log.debug("AuthFilter global config pass-thru");
            return;
			
        } else if (containerRequest.getUriInfo().getRequestUri().getPath().contains("rest/webauthn")) {
            Log.debug("AuthFilter webauthn pass-thru");
            return;
        }

        try {
            if (OSUtils.IS_WINDOWS && JiveGlobals.getBooleanProperty("acs.enable.sso", false)) {
                String sessionName = request.getRemoteUser();

                if (request.getUserPrincipal() != null) {
                    String windowsName = request.getUserPrincipal().getName();

                    if (windowsName != null && sessionName != null && sessionName.equals(windowsName)) {
                        String userName = windowsName;
                        int pos = windowsName.indexOf("\\");
                        if (pos > -1) userName = windowsName.substring(pos + 1).toLowerCase();

                        Log.debug("AuthFilter SSO check ok " + userName);
                        return;
                    }
                }
            }
        } catch (Exception e5) {
            Log.error("AuthFilter SSO check error", e5);
        }

        // Let the preflight request through the authentication
        if ("OPTIONS".equals(containerRequest.getMethod())) {
            return;
        }

		String remoteAddr = request.getHeader("X-FORWARDED-FOR");
		
		if (remoteAddr == null || "".equals(remoteAddr)) {
			remoteAddr = request.getRemoteAddr();
		}
		
		Log.debug("found IP Address " + remoteAddr);
		
		if (remoteAddr != null && !("".equals(remoteAddr))) 
		{
			if (SparkWeb.self.getUsersByProperty("ip_address", remoteAddr).size() == 1) {
				return;    // allow ip address access
			}			
		}

        // Get the authentification passed in HTTP headers parameters
        String auth = request.getHeader("authorization");

        if (auth == null) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

		User user = SparkWeb.self.tokens.get(auth);
		
        if (user != null) {
			// WebAuthn
			
			if (!auth.startsWith("ey")) { 
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
			
			String[] parts = auth.split("\\.");	
			
			if (parts.length != 3) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
			
			JSONObject payload = new JSONObject(decode(parts[1]));
			
			if (!payload.has("sub") || !user.getUsername().equals(payload.getString("sub"))) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		
            return;    // allow webauthn token access
        }

        String[] usernameAndPassword = BasicAuth.decode(auth);

        // If no username nor password, fail
        if (usernameAndPassword == null || usernameAndPassword.length != 2) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

		// Digest authenntication

        try {
            user = XMPPServer.getInstance().getUserManager().getUser(usernameAndPassword[0]);

            // if master password, allow superuser to impersonate anyone

            if (usernameAndPassword[1].equals(JiveGlobals.getProperty("sparkweb_access_token", "5ba912b1a9ab321f2d2acd4e67118d5d"))) {
                return;
            }

            // fallback on username/password authentication

            AuthFactory.authenticate(usernameAndPassword[0], usernameAndPassword[1]);
            return;
        } catch (Exception e) {
            Log.error("Wrong HTTP Basic Auth authorization", e);
        }

        throw new WebApplicationException(Status.UNAUTHORIZED);
    }
	
    private String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }	
}
