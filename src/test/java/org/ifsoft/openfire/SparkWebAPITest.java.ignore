package org.ifsoft.openfire;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.jivesoftware.openfire.IQRouter;
import org.jivesoftware.openfire.MessageRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.XMPPServerInfo;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.user.UserProvider;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.TestUserManager;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.plugin.rest.CustomJacksonMapperProvider;
import org.jivesoftware.openfire.plugin.rest.exceptions.RESTExceptionMapper;
import org.jivesoftware.openfire.plugin.rest.exceptions.ServiceException;
import org.jivesoftware.openfire.plugin.rest.CORSFilter;
import org.jivesoftware.openfire.plugin.rest.AuthFilter;
import org.jivesoftware.openfire.user.property.UserPropertyProvider;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.cache.CacheFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import org.xmpp.packet.JID;
import net.sf.json.*;
import com.google.gson.JsonObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SparkWebAPITest extends JerseyTest {
    private static UserManager userManager;	
	private static User user1, user2;
    private static final String XMPP_DOMAIN = "test.xmpp.domain";	
    private static final Map<String, String> properties = new HashMap<>();	
	private static SparkWeb sparkWebPlugin;
	
    public static void constructSparkWeb() throws ServiceException {
		sparkWebPlugin = new SparkWeb();
		sparkWebPlugin.setupHashMaps();
    }

    public static XMPPServer constructMockXmppServer() {
        final PluginManager pluginManager = mock(PluginManager.class, withSettings().lenient());
        final XMPPServer xmppServer = mock(XMPPServer.class, withSettings().lenient());

        doAnswer(invocationOnMock -> pluginManager).when(xmppServer).getPluginManager();

        doAnswer(invocationOnMock -> {
            final JID jid = invocationOnMock.getArgument(0);
            return jid.getDomain().equals(XMPP_DOMAIN);
        }).when(xmppServer).isLocal(any(JID.class));

        doAnswer(invocationOnMock -> new JID(invocationOnMock.getArgument(0), XMPP_DOMAIN, invocationOnMock.getArgument(1)))
            .when(xmppServer).createJID(any(String.class), nullable(String.class));
			
        doAnswer(invocationOnMock -> new JID(invocationOnMock.getArgument(0), XMPP_DOMAIN, invocationOnMock.getArgument(1), invocationOnMock.getArgument(2)))
            .when(xmppServer).createJID(any(String.class), nullable(String.class), any(Boolean.class));
			
        doReturn(mockXMPPServerInfo()).when(xmppServer).getServerInfo();
        doReturn(mockIQRouter()).when(xmppServer).getIQRouter();		
        doReturn(mockMessageRouter()).when(xmppServer).getMessageRouter();		

        return xmppServer;		
    }
	
    public static XMPPServerInfo mockXMPPServerInfo() {
        final XMPPServerInfo xmppServerInfo = mock(XMPPServerInfo.class, withSettings().lenient());
        doReturn(XMPP_DOMAIN).when(xmppServerInfo).getXMPPDomain();
        return xmppServerInfo;
    }	

    public static MessageRouter mockMessageRouter() {
        final MessageRouter messageRouter = mock(MessageRouter.class, withSettings().lenient());
        return messageRouter;
    }	
	
    public static IQRouter mockIQRouter() {
        final IQRouter iqRouter = mock(IQRouter.class, withSettings().lenient());
        return iqRouter;
    }	
	
    public static void reconfigureOpenfireHome() throws Exception {
        final URL configFile = ClassLoader.getSystemResource("conf/openfire.xml");
        if (configFile == null) {
            throw new IllegalStateException("Unable to read openfire.xml file; does conf/openfire.xml exist in the test classpath, i.e. test/resources?");
        }
        final File openfireHome = new File(configFile.toURI()).getParentFile().getParentFile();
        JiveGlobals.setHomeDirectory(openfireHome.toString());
        JiveGlobals.setXMLProperty("setup", "true");
        JiveGlobals.setXMLProperty("database.maxRetries", "0");
        JiveGlobals.setXMLProperty("database.retryDelay", "0");
 		
        clearExistingProperties();
    }	
	
    public static void clearExistingProperties() {
        JiveGlobals.getXMLPropertyNames().stream()
            .filter(name -> !"setup".equals(name))
            .filter(name -> !"database.maxRetries".equals(name))
            .filter(name -> !"database.retryDelay".equals(name))	
            .forEach(JiveGlobals::deleteXMLProperty);
			
        JiveGlobals.getPropertyNames()
            .forEach(JiveGlobals::deleteProperty);
    }	

    @BeforeClass
    public static void setUpClass() throws ServiceException, Exception {
		reconfigureOpenfireHome();
        final XMPPServer xmppServer = constructMockXmppServer();
		XMPPServer.setInstance(xmppServer);

        constructSparkWeb();

        CacheFactory.createCache("User").clear();
        CacheFactory.createCache("Remote Users Existence").clear();
		
        JiveGlobals.setProperty("provider.user.className", StubUserProvider.class.getName());
        JiveGlobals.setProperty("usermanager.remote-disco-info-timeout-seconds", "0");
		
        userManager = TestUserManager.getUserManager(xmppServer);		
        user1 = userManager.createUser("testuser1", "change me", "Test User Name", "test-email@example.com");
		SparkWeb.userManager = userManager;		
    }	

    @Before
    public void setUpTest() throws Exception {
		SparkWeb.self = sparkWebPlugin;		
        SparkWeb.groupManager = mock(GroupManager.class, withSettings().lenient());	
		
		Collection<JID> administrators = new ArrayList<>();
		Collection<JID> members = new ArrayList<>();
		members.add(new JID("testuser1@domain"));
		
		Group group = new Group("testgroup", "Test Group", members, administrators);
		Collection<Group> groups = new ArrayList<>();
		groups.add(group);		
        doReturn(groups).when(SparkWeb.groupManager).getGroups(user1);	
		
		Map<String, String> userProperties1 = user1.getProperties();
		userProperties1.clear();
		userProperties1.put("vapid.private.key", "fsWZ_ZUQ3BJKYw3Pv2Tzed0tspsiJCAq4urgPP1FhMM=");
		userProperties1.put("vapid.public.key", "BAhA2LKSdqFYvPebdNzFBMrFCISUHCHpBu81EphOP9cmPQc5xwfdgqSKVBKLHEcWighav1Si3XRQvA1MRF5wMGg="); 						
	}		
	
    @Override
    protected Application configure() {
        return new ResourceConfig(SparkWebAPI.class, RESTExceptionMapper.class, CustomJacksonMapperProvider.class, CORSFilter.class/*, AuthFilter.class*/);
    }


	//-------------------------------------------------------
	//
	//	Openfire Bookmarks API
	//
	//-------------------------------------------------------

    @Test
    public void createBookmarkTest() {
        Response response = target("rest/bookmark").request(MediaType.APPLICATION_JSON).post(Entity.json("{   \"type\": \"url\",   \"name\": \"test bookmark\",   \"value\": \"tel:+447825589457\",   \"users\": [     \"dele\"   ],   \"groups\": [     \"dev\"   ],   \"globalBookmark\": true }"));
        String content = response.readEntity(String.class);	
		System.out.println("createBookmarkTest\n" + content);
        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
    }
	
    @Test
    public void getBookmarksTest() {
        Response response = target("rest/bookmarks").request(MediaType.APPLICATION_JSON).get();
        String content = response.readEntity(String.class);	
		System.out.println("getBookmarksTest\n" + content);
        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getBookmarkTest() {
		Response response = target("rest/bookmark").request(MediaType.APPLICATION_JSON).post(Entity.json("{   \"type\": \"url\",   \"name\": \"test bookmark\",   \"value\": \"tel:+447825589457\",   \"users\": [     \"dele\"   ],   \"groups\": [     \"dev\"   ],   \"globalBookmark\": true }"));		
        response = target("rest/bookmark/0").request(MediaType.APPLICATION_JSON).get();
        String content = response.readEntity(String.class);	
		System.out.println("getBookmarkTest\n" + content);
        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteBookmarkTest() {
		Response response = target("rest/bookmark").request(MediaType.APPLICATION_JSON).post(Entity.json("{   \"type\": \"url\",   \"name\": \"test bookmark\",   \"value\": \"tel:+447825589457\",   \"users\": [     \"dele\"   ],   \"groups\": [     \"dev\"   ],   \"globalBookmark\": true }"));		
        response = target("rest/bookmark/0").request(MediaType.APPLICATION_JSON).delete();
        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void updateBookmarkTest() {
        Response response = target("rest/bookmark/0").request(MediaType.APPLICATION_JSON).put(Entity.json("{   \"type\": \"url\",   \"name\": \"test bookmark\",   \"value\": \"tel:+447825589457\",   \"users\": [     \"dele\"   ],   \"groups\": [     \"dev\"   ],   \"globalBookmark\": true }"));
        String content = response.readEntity(String.class);	
		System.out.println("updateBookmarkTest\n" + content);
        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void bookmarkPropertyTest() {
        Response response = target("rest/bookmark").request(MediaType.APPLICATION_JSON).post(Entity.json("{   \"type\": \"url\",   \"name\": \"test bookmark\",   \"value\": \"tel:+447825589457\",   \"users\": [     \"dele\"   ],   \"groups\": [     \"dev\"   ],   \"globalBookmark\": true }"));		
        response = target("rest/bookmark/0/property_name").request(MediaType.APPLICATION_JSON).post(Entity.text("property_value"));
        String content = response.readEntity(String.class);	
		System.out.println("createBookmarkPropertyTest\n" + content);
        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
		
        response = target("rest/bookmark/0/property_name").request(MediaType.APPLICATION_JSON).delete();
        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());		
    }	

	//-------------------------------------------------------
	//
	//	Global and User Properties
	//
	//-------------------------------------------------------

    @Test
    public void getGlobalParametersTest() {
        Response response = target("rest/config/global").request(MediaType.APPLICATION_JSON).get();
        String content = response.readEntity(String.class);	
		System.out.println("getGlobalParametersTest\n" + content);
        assertEquals("Http Response should be 500: ", 500, response.getStatus());
    }
	
    @Test
    public void postUserConfigTest() throws ServiceException, Exception {			
 		Response response = target("rest/config/properties").request(MediaType.APPLICATION_JSON).header("authorization", "mytoken").post(Entity.json(createMockUserConfig().toString()));	
        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(properties.get("prop1_name"), "prop1_value");
		assertEquals(properties.get("prop2_name"), "prop2_value");		
		System.out.println("postUserConfigTest\n" + properties);
    }	

    @Test
    public void getUserConfigTest() throws ServiceException, Exception {			
 		Response response = target("rest/config/properties").request(MediaType.APPLICATION_JSON).header("authorization", "mytoken").get();	
        String content = response.readEntity(String.class);	
		System.out.println("getUserConfigTest\n" + content);
		
		JSONObject props = new JSONObject(content);
		assertEquals(props.getString("vapid.private.key"), "fsWZ_ZUQ3BJKYw3Pv2Tzed0tspsiJCAq4urgPP1FhMM=");			
		assertEquals(props.getString("vapid.public.key"), "BAhA2LKSdqFYvPebdNzFBMrFCISUHCHpBu81EphOP9cmPQc5xwfdgqSKVBKLHEcWighav1Si3XRQvA1MRF5wMGg=");			
    }	
	
	//-------------------------------------------------------
    //
    //  Web Authentication
    //
    //-------------------------------------------------------

    @Test
    public void webauthnRegisterStartTest() throws ServiceException, Exception {		
 		Response response = target("rest/webauthn/register/start/testuser1").request(MediaType.APPLICATION_JSON).header("authorization", "mytoken").post(Entity.text("change me"));				
        String content = response.readEntity(String.class);	
		System.out.println("webauthnRegisterStartTest\n" + content);
		JSONObject error = new JSONObject(content);
        assertEquals("IllegalArgumentException", error.getString("exception"));
    }

    @Test
    public void webauthnRegisterfinishTest() throws ServiceException, Exception {		
 		Response response = target("rest/webauthn/register/start/testuser1").request(MediaType.APPLICATION_JSON).header("authorization", "mytoken").post(Entity.json("{}"));				
       assertEquals("Http Response should be 400: ", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());			
    }

    @Test
    public void webauthnAuthenticateStartTest() throws ServiceException, Exception {		
 		Response response = target("rest/webauthn/authenticate/start/testuser1").request(MediaType.APPLICATION_JSON).header("authorization", "mytoken").post(Entity.text(""));				
        String content = response.readEntity(String.class);	
		System.out.println("webauthnAuthenticateStartTest\n" + content);
        assertEquals("Http Response should be 500: ", 500, response.getStatus());
    }

    @Test
    public void webauthnAuthenticatefinishTest() throws ServiceException, Exception {		
 		Response response = target("rest/webauthn/authenticate/start/testuser1").request(MediaType.APPLICATION_JSON).header("authorization", "mytoken").post(Entity.json("{}"));				
        assertEquals("Http Response should be 500: ", 500, response.getStatus());
	}	
	
	//-------------------------------------------------------
	//
	//	Chat
	//
	//-------------------------------------------------------

    @Test
    public void getChatThreadIdTest() throws ServiceException, Exception {		
	
    }

    @Test
    public void postChatTest() throws ServiceException, Exception {		
		
    }


	//-------------------------------------------------------
	//
	//	Presence
	//
	//-------------------------------------------------------
	
    @Test
    public void postPresenceTest() throws ServiceException, Exception {		
	
    }

    @Test
    public void getPresenceTest() throws ServiceException {	
		
    }	


	//-------------------------------------------------------
	//
	//	Utitlities
	//
	//-------------------------------------------------------

	private JSONArray createMockUserConfig() {
		JSONArray props = new JSONArray();
		
		JSONObject prop1 = new JSONObject();
        prop1.put("name", "prop1_name");
        prop1.put("value", "prop1_value");
		props.put(0, prop1);
		
		JSONObject prop2 = new JSONObject();
        prop2.put("name", "prop2_name");
        prop2.put("value", "prop2_value");
		props.put(1, prop2);		
			
        return props;		
	}

	private JSONObject createChatMessage() {
		JSONObject json = new JSONObject();
	
        return json;		
	}
	
	private JSONObject createMockToken() {
		JSONObject json = new JSONObject();
			
        return json;		
	}
	
    public static class StubUserProvider implements UserProvider {
        final Map<String, User> users = new HashMap<>();

        @Override
        public User loadUser(final String username) throws UserNotFoundException {
            return Optional.ofNullable(users.get(username)).orElseThrow(UserNotFoundException::new);
        }

        @Override
        public User createUser(final String username, final String password, final String name, final String email) throws UserAlreadyExistsException {
            if (users.containsKey(username)) {
                throw new UserAlreadyExistsException();
            }
            final User user = mock(User.class, withSettings().lenient());
            doReturn(username).when(user).getUsername();
            doReturn(name).when(user).getName();
            doReturn(email).when(user).getEmail();
            doReturn(new Date()).when(user).getCreationDate();
            doReturn(new Date()).when(user).getModificationDate();
            doReturn(properties).when(user).getProperties();			
            users.put(username, user);
            return user;
        }

        @Override
        public void deleteUser(final String username) {
            users.remove(username);
        }

        @Override
        public int getUserCount() {
            return users.size();
        }

        @Override
        public Collection<User> getUsers() {
            return users.values();
        }

        @Override
        public Collection<String> getUsernames() {
            return users.keySet();
        }

        @Override
        public Collection<User> getUsers(final int startIndex, final int numResults) {
            return null;
        }

        @Override
        public void setName(final String username, final String name) throws UserNotFoundException {
            final User user = loadUser(username);
            doReturn(name).when(user).getName();
        }

        @Override
        public void setEmail(final String username, final String email) throws UserNotFoundException {
            final User user = loadUser(username);
            doReturn(email).when(user).getEmail();
        }

        @Override
        public void setCreationDate(final String username, final Date creationDate) throws UserNotFoundException {
            final User user = loadUser(username);
            doReturn(new Date()).when(user).getCreationDate();
        }

        @Override
        public void setModificationDate(final String username, final Date modificationDate) throws UserNotFoundException {
            final User user = loadUser(username);
            doReturn(new Date()).when(user).getModificationDate();
        }

        @Override
        public Set<String> getSearchFields() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<User> findUsers(final Set<String> fields, final String query) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<User> findUsers(final Set<String> fields, final String query, final int startIndex, final int numResults) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isReadOnly() {
            return false;
        }

        @Override
        public boolean isNameRequired() {
            return false;
        }

        @Override
        public boolean isEmailRequired() {
            return false;
        }
    }	
}
