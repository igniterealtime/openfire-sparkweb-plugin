/*
 * Copyright (C) 2017 Ignite Realtime Foundation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.ifsoft.openfire;

import java.io.File;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.*;
import java.time.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.security.Security;
import java.nio.file.*;
import java.nio.charset.Charset;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.apache.http.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.container.*;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.openfire.sasl.AnonymousSaslServer;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.*;
import org.jivesoftware.openfire.muc.*;
import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterItem;
import org.jivesoftware.openfire.roster.RosterManager;
import org.jivesoftware.openfire.plugin.rest.BasicAuth;
import org.jivesoftware.openfire.plugin.spark.*;
import org.jivesoftware.openfire.cluster.ClusterEventListener;
import org.jivesoftware.openfire.cluster.ClusterManager;
import org.jivesoftware.openfire.net.SASLAuthentication;
import org.jivesoftware.openfire.pubsub.PubSubInfo;
import org.jivesoftware.openfire.pubsub.PubSubServiceInfo;
import org.jivesoftware.openfire.pubsub.Node;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.smack.OpenfireConnection;
import org.jivesoftware.util.cache.Cache;
import org.jivesoftware.util.cache.CacheFactory;
import org.jivesoftware.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.util.security.*;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.*;
import org.eclipse.jetty.websocket.servlet.*;
import org.eclipse.jetty.websocket.server.*;
import org.eclipse.jetty.websocket.server.pathmap.ServletPathSpec;
import org.eclipse.jetty.servlets.EventSource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;

import org.xmpp.packet.Packet;
import org.xmpp.packet.JID;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import org.xmpp.packet.Presence;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.sf.json.*;
import org.jitsi.util.OSUtils;

import waffle.servlet.NegotiateSecurityFilter;
import waffle.servlet.WaffleInfoServlet;
import de.mxro.process.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import org.jsoup.Jsoup;


public class SparkWeb implements Plugin, ProcessListener, ClusterEventListener, MUCEventListener, PacketInterceptor
{
    private static final Logger Log = LogManager.getLogger(SparkWeb.class);
    private static final String MUC_NAME = "conference";
	
    public static SparkWeb self;
	public static UserManager userManager;
	public static PresenceManager presenceManager;
	public static GroupManager groupManager;
    public static String webRoot;		
	
	private long startTime = System.currentTimeMillis();
    private WebAppContext context;
    private MultiUserChatService mucService;		
	
    public Map<String, ArrayList<WebEventSourceServlet.WebEventSource>> webSources;		
    public Map<String, User> tokens;		

    // -------------------------------------------------------
    //
    //  Plugin initialize/destroy
    //
    // -------------------------------------------------------
	
    public void destroyPlugin() {
		self = null;
		webRoot = null;
		
		try {																
			HttpBindManager.getInstance().removeJettyHandler(context);
			
			ClusterManager.removeListener(this);
            MUCEventDispatcher.removeListener(this);
			InterceptorManager.getInstance().removeInterceptor(this);					
		
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}		
    }

    public void initializePlugin(final PluginManager manager, final File pluginDirectory) {
        self = this;
		XMPPServer server = XMPPServer.getInstance();
		userManager = server.getUserManager();
		presenceManager = server.getPresenceManager();
		
		groupManager = GroupManager.getInstance();
        webRoot = pluginDirectory.getPath() + "/classes";
		
		InterceptorManager.getInstance().addInterceptor(this);
		ClusterManager.addListener(this);
        MUCEventDispatcher.addListener(this);	
		mucService = server.getMultiUserChatManager().getMultiUserChatService(MUC_NAME);

		try {
			setupHashMaps();
			setupWebContext(pluginDirectory);	
			generateDocumentation();			
			
		} catch (Exception e) {
			Log.error("initializePlugin", e);
		}

		Log.info("SparkWeb plugin enabled");
		startTime = System.currentTimeMillis();
    }
	

    //-------------------------------------------------------
    //
    //      clustering
    //
    //-------------------------------------------------------

    @Override public void joinedCluster()  {
        Log.debug("joinedCluster");
		
        try
        {

        }
        catch ( Exception ex )
        {
            Log.error( "joinedCluster", ex );
        }
    }

    @Override public void joinedCluster(byte[] arg0)  {
    }

    @Override public void leftCluster() {
        Log.debug("leftCluster");
		
        try
        {

        }
        catch ( Exception ex )
        {
            Log.error( "joinedCluster", ex );
        }
    }

    @Override public void leftCluster(byte[] arg0) {
    }

    @Override public void markedAsSeniorClusterMember() {
        Log.debug("markedAsSeniorClusterMember");
		
        try
        {

        }
        catch ( Exception ex )
        {
            Log.error( "markedAsSeniorClusterMember", ex );
        }
    }

    // -------------------------------------------------------
    //
    //  Web Application
    //
    // -------------------------------------------------------

	public boolean postAction(String username, JSONObject payload) 	{
		boolean response = false;
			
		try {
			emitEvent(username, "onAction", payload);				
			response = true;
		} catch (Exception e) {
			Log.warn("postAction - Unable to deliver " + username + "\n" + payload, e);
		}			

		return response;
	}
	
	private void addSecurityToWebContext(WebAppContext contextHandler) {
		if (OSUtils.IS_WINDOWS && JiveGlobals.getBooleanProperty("enable.sso", false))
		{			
			NegotiateSecurityFilter securityFilter = new NegotiateSecurityFilter();
			FilterHolder filterHolder = new FilterHolder();
			filterHolder.setFilter(securityFilter);
			EnumSet<DispatcherType> enums = EnumSet.of(DispatcherType.REQUEST);
			enums.add(DispatcherType.REQUEST);

			contextHandler.addFilter(filterHolder, "/*", enums);
			contextHandler.addServlet(new ServletHolder(new WaffleInfoServlet()), "/waffle");
		}	
	}

    // -------------------------------------------------------
    //
    //  Global Handlers
    //
    // -------------------------------------------------------
	
    public void onOutputLine(final String line)     {
        Log.debug("onOutputLine " + line);
    }

    public void onProcessQuit(int code)     {
        Log.debug("onProcessQuit " + code);
    }

    public void onOutputClosed() {
        Log.error("onOutputClosed");
    }

    public void onErrorLine(final String line)     {
        Log.debug(line);
    }	

    public void onError(Throwable error)     {
        Log.error("onError", error);
    }
	
    // -------------------------------------------------------
    //
    //  Utilitiy functions
    //
    // -------------------------------------------------------		

	public void broadcast(String username, String topic, String event) {
		if ((event.startsWith("{") || event.startsWith("[")) && (event.endsWith("]") || event.endsWith("}"))) {
			emitEvent(username, topic, new JSONObject(event));
		}
	}
		
	public void emitEvent(String username, String event, JSONObject json) {
		if (webSources.containsKey(username)) {
			try {				
				for (WebEventSourceServlet.WebEventSource source : webSources.get(username)) {
					source.emitEvent(event, json);
				}				
			} catch (Exception e) {
				Log.warn("unable to emit message to " + username);
			}
		} else { // TODO web push		
		
		}
	}
	
    public Language getLanguage()  {
        final String sparkWebLanguage = JiveGlobals.getProperty( "sparkweb.config.language" );

        if ( sparkWebLanguage != null ) {
            final Language result = Language.byConverseCode( sparkWebLanguage );
			
            if ( result != null ) {
                return result;
            }
            else {
                Log.warn( "The value '{}' of property 'inverse.config.language' cannot be mapped to a language code that is understood by Inverse.", sparkWebLanguage );
            }
        }

        final Language result = Language.byLocale( JiveGlobals.getLocale() );
		
        if ( result != null ) {
            return result;
        }
        else {
            Log.warn( "The Openfire locale '{}' cannot be mapped to a language code that is understood by Inverse.", JiveGlobals.getLocale() );
        }

        return Language.English;
    }
	
	private void setupWebContext(File pluginDirectory) {
        context = new WebAppContext(null, pluginDirectory.getPath() + "/classes/apps", "/sparkweb");
        context.setClassLoader(this.getClass().getClassLoader());		
		boolean anonLogin = AnonymousSaslServer.ENABLED.getValue();

		if (!anonLogin)
		{

		}
		
		addSecurityToWebContext(context);
		
        final List<ContainerInitializer> initializers = new ArrayList<>();
        initializers.add(new ContainerInitializer(new JettyJasperInitializer(), null));
        context.setAttribute("org.eclipse.jetty.containerInitializers", initializers);
        context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        context.setWelcomeFiles(new String[]{"index.html"});
		context.addServlet(new ServletHolder(new WebEventSourceServlet()), "/sse");
		
        HttpBindManager.getInstance().addJettyHandler(context);	
		Log.info( "Web Service ready");
	}	
		
	public String makeAccessToken(User user) {
		JSONArray permissions = new JSONArray();		
		permissions.put(0, "full");	
			
		JSONObject jwtPayload = new JSONObject();		
		LocalDateTime iat = LocalDateTime.now().minusDays(1);
		LocalDateTime ldt = iat.plusDays(2);	

		String username = user.getUsername();	
		jwtPayload.put("sub", username);
		jwtPayload.put("aud", XMPPServer.getInstance().getServerInfo().getXMPPDomain());
		jwtPayload.put("permissions", permissions);			
		jwtPayload.put("iat", iat.toEpochSecond(ZoneOffset.UTC));
		jwtPayload.put("exp", ldt.toEpochSecond(ZoneOffset.UTC));
		jwtPayload.put("iss", "https://" + XMPPServer.getInstance().getServerInfo().getHostname() + ":" + JiveGlobals.getProperty("httpbind.port.secure", "7443"));			
				
		String token = new JWebToken(jwtPayload).toString();	
		tokens.put(token, user);
		OpenfireConnection.createConnection(username);		
		return token;
	}
	
	public void setupHashMaps() {	
		webSources = new ConcurrentHashMap<>();				
		tokens = new ConcurrentHashMap<>();				
	}
	
	public void generateDocumentation() {
	
	}
	
	public String getDuration() {
        long current_time = System.currentTimeMillis();
        String duration = "";

        try {
             duration = StringUtils.getFullElapsedTime(System.currentTimeMillis() - startTime);
        } catch (Exception e) {}

        return duration;		
	}

	public String createPairing(Roster roster, String username, String nickName) {
		RosterItem gwitem = null;
		String response = null;
		
		try {
			JID jid = XMPPServer.getInstance().createJID(username, null);
			ArrayList<String> groups = new ArrayList<String>();
			
			if (roster.isRosterItem(jid) == false) {
				gwitem = roster.createRosterItem(jid, true, true);

			} else {
				gwitem = roster.getRosterItem(jid);
			}

			if (gwitem != null) {
				gwitem.setSubStatus(RosterItem.SUB_BOTH);
				gwitem.setAskStatus(RosterItem.ASK_NONE);
				gwitem.setNickname(nickName);
				gwitem.setGroups((List<String>) groups);
				roster.updateRosterItem(gwitem);
				roster.broadcast(gwitem, true);
			}
        }
        catch (Exception e) {
        	response = "Error: " + e;
        }
		return response;		
	}
	
    private void makeFileExecutable(String path) {
        File file = new File(path);
        file.setReadable(true, true);
        file.setWritable(true, true);
        file.setExecutable(true, true);
        Log.debug("checkNatives galene executable path " + path);
    }
	
    public static String getIpAddress()  {
        String ourHostname = XMPPServer.getInstance().getServerInfo().getHostname();
        String ourIpAddress = "127.0.0.1";

        try {
            ourIpAddress = InetAddress.getByName(ourHostname).getHostAddress();
        } catch (Exception e) {

        }

        return ourIpAddress;
    }
	
	private String removeLastChar(String str) {
		return removeLastChars(str, 1);
	}

	private String removeLastChars(String str, int chars) {
		return str.substring(0, str.length() - chars);
	}
	
    private String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    public ArrayList<User> getUsersByProperty(String propertyName, String propertyValue) { 
        ArrayList<User> users = new ArrayList<User>();
        Cache<String, ArrayList<User>> propCache = CacheFactory.createLocalCache("User Properties");

        if (propertyValue != null)
        {
            users = propCache.get(propertyName + propertyValue);
        } else {
            users = propCache.get(propertyName);
        }

        if (users != null) {
            return users;
        }

        users = new ArrayList<User>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DbConnectionManager.getConnection();
            // Load property by key and value
            if (isNull(propertyValue) == false) {
                pstmt = con.prepareStatement("SELECT username FROM ofUserProp WHERE name=? AND propValue=?");
                pstmt.setString(1, propertyName);
                pstmt.setString(2, propertyValue);
            } else {
                // Load property by key
                pstmt = con.prepareStatement("SELECT username FROM ofUserProp WHERE name LIKE ?");
                pstmt.setString(1, propertyName);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(getUser(rs.getString(1)));
            }
        } catch (SQLException sqle) {
            Log.error("getUsersByProperty", sqle);
        } finally {
            DbConnectionManager.closeConnection(rs, pstmt, con);
        }

		if (users.size() > 0) {
			if (propertyValue != null)
			{
				propCache.put(propertyName + propertyValue, users);
			} else {
				propCache.put(propertyName, users);
			}
		}
        return users;	
    }

    private boolean isNull(String value)   {
        return (value == null || "undefined".equals(value)  || "null".equals(value) || "".equals(value.trim()) || "unknown".equals(value) || "none".equals(value));
    }

    private void ensureSparkWebUser(String username, String email) {

        if ( !userManager.isRegisteredUser( username ) )
        {
            Log.debug( "No pre-existing sparkweb user. Generating one." );
            String password = JiveGlobals.getProperty("sparkweb.user.password", null);			

            if ( password == null || password.isEmpty() )
            {
                password = StringUtils.randomString( 40 );
				JiveGlobals.setProperty("sparkweb.user.password", password);				
            }

            try
            {
                userManager.createUser( username, password, "SparkWeb User (generated)", email);
            }
            catch ( Exception e )
            {
                Log.error( "Unable to provision sparkweb user", e );
            }
        }
    }
	
    public User getUserFromUid(String uid) {
		
        User xmppUser = null;
		
		for (User user : getUsersByProperty("sparkweb_id", uid)) 
		{							
			xmppUser = user;			
			break;
		}
		
        return xmppUser;
    }	
	
    public User getUser(String username) {
        User xmppUser = null;
        try
        {
            xmppUser = userManager.getUser(JID.escapeNode(username));
        }
        catch ( UserNotFoundException e )
        {
            Log.debug( "getUser - Not a recognized user " + username);
        }
        return xmppUser;
    }	
	
    public boolean isBookmarkForUser(String username, Bookmark bookmark) {
        if (bookmark.getUsers().contains(username)) {
            return true;
        }

        Collection<String> groups = bookmark.getGroups();

        if (groups != null && !groups.isEmpty()) 
		{
            for (String groupName : groups) {
                try {
                    org.jivesoftware.openfire.group.Group group = groupManager.getGroup(groupName);
					
                    if (group.isUser(username)) {
                        return true;
                    }
                }
                catch (GroupNotFoundException e) {
                    Log.warn("Group name not found - " + groupName);
                }
            }
        }
        return false;
    }
	
    // -------------------------------------------------------
    //
    //  MUCEventListener
    //
    // -------------------------------------------------------

    public void roomCreated(JID roomJID) {

    }

    public void roomDestroyed(JID roomJID) {

    }

    public void occupantJoined(JID roomJID, JID user, String nickname) {

    }

    public void nicknameChanged(JID roomJID, JID user, String oldNickname, String newNickname) {

    }

    public void messageReceived(JID roomJID, JID user, String nickname, Message message) {
		final String body = message.getBody();
		MUCRoom mucRoom = mucService.getChatRoom(roomJID.getNode());
		
		if (mucRoom != null) {

		}
    }

    public void roomSubjectChanged(JID roomJID, JID user, String newSubject)  {

    }

    public void privateMessageRecieved(JID a, JID b, Message message)  {

    }

	public void occupantNickKicked(JID roomJID, String nickname) {
		
	}
	
    public void occupantLeft(JID roomJID, JID user, String nickname) {
		
	}
	
    // -------------------------------------------------------
    //
    //  PacketInterceptor
    //
    // -------------------------------------------------------	

	public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
    if (!processed && incoming && packet.getTo() != null && packet instanceof IQ) {
		IQ iq = (IQ)packet;
		if (iq.getType() != IQ.Type.set) return; 	

		Element eIq = iq.getElement();
		String to = eIq.attributeValue("to");
		String from = eIq.attributeValue("from");		
		
	}
  }	
		
	public class ColorHelper {
		public final int MAX = 200;
		public final int MIN = 80;
		final float BIG_FONT_FACTOR = 4f / 5f;

		public String getRandomClearColor(final String seedString) {
			final int red = getRandomInt(seedString, -1);
			final int green = getRandomInt(seedString, 5);
			final int blue = getRandomInt(seedString, 2);
			return rgbToHex(red, green, blue);
		}

		public int getRandomClearColorInt(final String seedString) {
			final int red = getRandomInt(seedString, -1);
			final int green = getRandomInt(seedString, 5);
			final int blue = getRandomInt(seedString, 2);
			final int rgb = ((red & 0x0ff) << 16) | ((green & 0x0ff) << 8) | (blue & 0x0ff);
			return rgb;
		}

		private int getRandomInt(final String seedString, final int seed) {
			return MIN + (int) (Math.random() * ((MAX - MIN) + 1));
		}

		public String rgbToHex(final int r, final int g, final int b) {
			final StringBuilder sb = new StringBuilder();
			sb.append('#').append(Integer.toHexString(r)).append(Integer.toHexString(g)).append(Integer.toHexString(b));
			return sb.toString();
		}

		public String getInitials(final int width,  final int height, final String name)  throws IOException {
			final String initial = name.substring(0, 1).toUpperCase();

			final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g2 = img.createGraphics();

			// Rectangle of random color
			final Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 0, width, height);
			g2.setPaint(new Color(getRandomClearColorInt(name)));
			g2.fill(rectangle);

			// Antialiassing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Font scale
			final int fontSize = Math.round((Math.min(width, height) * BIG_FONT_FACTOR));
			final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
			g2.setFont(font);
			final FontMetrics fm = g2.getFontMetrics();
			final float fontSizeLast = width / (float) fm.stringWidth(initial) * fontSize;
			font.deriveFont(fontSizeLast);

			// Font color
			g2.setColor(Color.WHITE);

			// Center font
			final Rectangle2D fontRect = fm.getStringBounds(initial, g2);
			final int x = (width - (int) fontRect.getWidth()) / 2;
			final int y = (height - (int) fontRect.getHeight()) / 2 + fm.getAscent();

			// Draw font
			g2.drawString(initial, x, y);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();	
			ImageIO.write(img, "png", baos);
			String data = DatatypeConverter.printBase64Binary(baos.toByteArray());	
			return "data:image/png;base64," + data;	
		}	  
	}	
}	
