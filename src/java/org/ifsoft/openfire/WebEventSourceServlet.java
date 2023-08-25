package org.ifsoft.openfire;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterItem;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.StringUtils;
import org.jivesoftware.smack.OpenfireConnection;

import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Presence;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;


public class WebEventSourceServlet extends EventSourceServlet {
    private static final Logger Log = LogManager.getLogger(WebEventSourceServlet.class);
    private static final long serialVersionUID = 2L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeHeader(response);
        super.doGet(request, response);
    }

    @Override
    protected EventSource newEventSource(final HttpServletRequest req) {
        WebEventSource source = null;
        User user = getEndUser(req);

        if (user != null) {
            String username = user.getUsername();
            ArrayList sources = SparkWeb.self.webSources.get(username);

            if (sources == null) {
				sources = new ArrayList();
                SparkWeb.self.webSources.put(username, sources);
            } 
			
			source = new WebEventSource(user);
			sources.add(source);			
        }
        return source;
    }

    private void writeHeader(HttpServletResponse response) {
        try {
            response.setHeader("Content-Type", "text/event-stream");

            HttpBindManager boshManager = HttpBindManager.getInstance();

            response.setHeader("Access-Control-Allow-Origin", boshManager.getCORSAllowOrigin());
            response.setHeader("Access-Control-Allow-Headers", HttpBindManager.HTTP_BIND_CORS_ALLOW_HEADERS_DEFAULT + ", Authorization");
            //response.setHeader("Access-Control-Allow-Credentials", "false");
            response.setHeader("Access-Control-Allow-Methods", HttpBindManager.HTTP_BIND_CORS_ALLOW_METHODS_DEFAULT);
            response.setHeader("Access-Control-Max-Age", HttpBindManager.HTTP_BIND_CORS_MAX_AGE_DEFAULT);

        } catch (Exception e) {
            Log.error("download - servlet writeHeader Error: ", e);
        }
    }

    public class WebEventSource implements EventSource {
        public boolean closed = true;
		
        private Emitter emitter;
        private User user;

        public WebEventSource(User user) {
            this.user = user;
            String username = user.getUsername();
            Log.debug("WebEventSource created session for " + username);
        }

        public void onClose() {
			String username = user.getUsername();
			ArrayList sources = SparkWeb.self.webSources.get(username);
			sources.remove(this);

			if (sources.size() == 0) { // token expires when last SSE is removed by user
				closed = true;
				SparkWeb.self.webSources.remove(username);
				
				try {
					OpenfireConnection connection = OpenfireConnection.removeConnection(username);
					SparkWeb.self.tokens.remove(connection.token);					
				} catch (Exception e) {
					Log.error("WebEventSource onClose", e);
				}					
			}

			Log.debug("WebEventSource onClose " + username + " " + closed + " " + sources.size());
        }

        public void onOpen(Emitter emitter) {
			String username = user.getUsername();	
			ArrayList sources = SparkWeb.self.webSources.get(username);			
            closed = false;
            this.emitter = emitter;
			
            try {			
				JSONObject profile = new JSONObject();
				profile.put("email", user.getEmail());
				profile.put("name", user.getName());				
				profile.put("username", username);
				emitEvent("onConnect", profile);
			} catch (Exception e) {
				Log.error("WebEventSource onOpen", e);
			}
			
			Log.debug("WebEventSource onOpen " + username + " " + closed + " " + sources.size());			
        }

        public void emitEvent(String event, JSONObject payload) throws IOException {
			String username = user.getUsername();				
			Log.debug("WebEventSource emitEvent " + username + " " + event + "\n" + payload);			
            this.emitter.event(event, payload.toString());
            closed = false;
        }
    }

    //-------------------------------------------------------
    //
    //	Utitlities
    //
    //-------------------------------------------------------

    private User getEndUser(HttpServletRequest httpRequest) {
        User theUser = null;

        try {
            String token = httpRequest.getParameter("token");
            Log.debug("getEndUser " + token);

            if (token != null) {
				theUser = SparkWeb.self.tokens.get(token);
            }

        } catch (Exception e) {
            Log.error("getEndUser failed", e);
        }
        return theUser;
    }
}
