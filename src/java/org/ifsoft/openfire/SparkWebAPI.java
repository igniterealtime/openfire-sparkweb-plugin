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

import java.util.*;
import java.util.concurrent.CompletableFuture;  
import java.net.*;
import java.security.*;
import java.time.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.*;
import javax.annotation.PostConstruct;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jivesoftware.util.*;
import org.jivesoftware.openfire.container.PluginMetadataHelper;
import org.jivesoftware.openfire.admin.AdminManager;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.openfire.plugin.rest.BasicAuth;
import org.jivesoftware.openfire.plugin.rest.exceptions.*;
import org.jivesoftware.openfire.user.*;
import org.jivesoftware.openfire.group.*;
import org.jivesoftware.openfire.plugin.spark.*;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.AuthFactory;

import org.jivesoftware.openfire.plugin.spark.Bookmark;
import org.jivesoftware.openfire.plugin.spark.Bookmarks;
import org.jivesoftware.openfire.plugin.spark.BookmarkManager;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.ifsoft.webauthn.UserRegistrationStorage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.*;
import org.jitsi.util.OSUtils;
import org.ifsoft.openfire.SparkWeb;
import org.xmpp.packet.*;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import io.swagger.annotations.*;

@SwaggerDefinition(tags = {@Tag(name = "Global and User Properties", description = "Access global and user properties"), @Tag(name = "Presence", description = "Perform XMPP Prsence functions"), @Tag(name = "Chat", description = "Perform XMPP Chat functions"), @Tag(name = "Bookmarks", description = "Create, update and delete Openfire bookmarks"), @Tag(name = "Web Authentication", description = "provide server-side Web Authentication services") }, info = @Info(description = "SparkWeb REST API adds support for a whole range of modern web service connections to Openfire/XMPP", version = "0.0.1", title = "SparkWeb API"), schemes = {SwaggerDefinition.Scheme.HTTPS, SwaggerDefinition.Scheme.HTTP}, securityDefinition = @SecurityDefinition(apiKeyAuthDefinitions = {@ApiKeyAuthDefinition(key = "authorization", name = "authorization", in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER)}))
@Api(authorizations = {@Authorization("authorization")})
@Path("rest")
@Produces(MediaType.APPLICATION_JSON)

public class SparkWebAPI {   
	private static final Logger Log = LogManager.getLogger(SparkWebAPI.class);	
	private static RelyingParty relyingParty = null;
	private static UserRegistrationStorage userRegistrationStorage;	

	public static final HashMap<String, Object> requests = new HashMap<>();	
	
	@Context
	private HttpServletRequest httpRequest;
	
	@PostConstruct
	public void init() 	{

	}	


	//-------------------------------------------------------
	//
	//	Openfire Bookmarks API
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"Bookmarks"}, value="Bookmark API - Create a new bookmark", notes="This endpoint is used to create a new bookmark")
    @Path("/bookmark")
    @POST	
    public Bookmark createBookmark(Bookmark newBookmark) throws ServiceException     {
        Log.debug("createBookmark " + newBookmark);

        try {
            return new Bookmark(newBookmark.getType(), newBookmark.getName(), newBookmark.getValue(), newBookmark.getUsers(), newBookmark.getGroups());

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }
	
	@ApiOperation(tags = {"Bookmarks"}, value="Bookmark API - Get all bookmark", notes="This endpoint is used to retrieve all bookmarks")
    @Path("/bookmarks")	
    @GET
    public Bookmarks getBookmarks() throws ServiceException     {
        Log.debug("getBookmarks ");
        try {
            return new Bookmarks(BookmarkManager.getBookmarks());

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }	
	
	@ApiOperation(tags = {"Bookmarks"}, value="Bookmark API - Get a specific bookmark", notes="This endpoint is used to retrieve a specific bookmark")
    @Path("/bookmark/{bookmarkID}")	
    @GET
    public Bookmark getBookmark(@PathParam("bookmarkID") String bookmarkID) throws ServiceException     {
        Log.debug("getBookmark " + bookmarkID);

        try {
            return new Bookmark(Long.parseLong(bookmarkID));

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }
	
	@ApiOperation(tags = {"Bookmarks"}, value="Bookmark API - Delete a specific bookmark", notes="This endpoint is used to delete a specific bookmark")
    @Path("/bookmark/{bookmarkID}")		
    @DELETE
    public Response deleteBookmark(@PathParam("bookmarkID") String bookmarkID) throws ServiceException    {
        Log.debug("deleteBookmark " + bookmarkID);

        try {
            BookmarkManager.deleteBookmark(Long.parseLong(bookmarkID));

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }
	
	@ApiOperation(tags = {"Bookmarks"}, value="Bookmark API - Update a specific bookmark", notes="This endpoint is used to update a specific bookmark")
    @Path("/bookmark/{bookmarkID}")			
    @PUT
    public Bookmark updateBookmark(@PathParam("bookmarkID") String bookmarkID, Bookmark newBookmark) throws ServiceException    {
        Log.debug("updateBookmark " + bookmarkID + " " + newBookmark.getType() + " " + newBookmark.getName() + " " + newBookmark.getValue() + " " + newBookmark.getUsers() + " " + newBookmark.getGroups() + " " + newBookmark.getProperties());

        try {
            Bookmark bookmark = BookmarkManager.getBookmark(Long.parseLong(bookmarkID));
            bookmark.setType(newBookmark.getType());
            bookmark.setName(newBookmark.getName());
            bookmark.setValue(newBookmark.getValue());
            bookmark.setUsers(newBookmark.getUsers());
            bookmark.setGroups(newBookmark.getGroups());
            return bookmark;

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }
	
	@ApiOperation(tags = {"Bookmarks"}, value="Bookmark API - Create/Update a bookmark property", notes="This endpoint is used to create or update a bookmark property value")
    @Path("/bookmark/{bookmarkID}/{name}")		
    @POST
    public Bookmark updateBookmarkProperty(@PathParam("bookmarkID") String bookmarkID, @PathParam("name") String name, String value) throws ServiceException    {
        Log.debug("updateBookmarkProperty " + bookmarkID + " " + name + "\n" + value);

        try {
            Bookmark bookmark = BookmarkManager.getBookmark(Long.parseLong(bookmarkID));
            bookmark.setProperty(name, value);
            return bookmark;

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }	
	
	@ApiOperation(tags = {"Bookmarks"}, value="Bookmark API - Delete a bookmark property", notes="This endpoint is used to delete a bookmark property")
    @Path("/bookmark/{bookmarkID}/{name}")	
    @DELETE
    public Bookmark deleteBookmarkProperty(@PathParam("bookmarkID") String bookmarkID, @PathParam("name") String name) throws ServiceException    {
        Log.debug("deleteBookmarkProperty " + bookmarkID + " " + name);

        try {
            Bookmark bookmark = BookmarkManager.getBookmark(Long.parseLong(bookmarkID));
            bookmark.deleteProperty(name);
            return bookmark;

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }	

	//-------------------------------------------------------
	//
	//	Global and User Properties
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"Global and User Properties"}, value="Global and User Properties - List global properties affecting this user", notes="Endpoint will retrieve all Openfire Global properties that are used by this authenticated user.")	
    @GET
    @Path("/config/global")
    public String getGlobalConfig() throws ServiceException 	{
		JSONObject json = new JSONObject();		
		json.put("version", PluginMetadataHelper.getVersion( SparkWeb.self ).getVersionString());	
		json.put("about", PluginMetadataHelper.getName( SparkWeb.self ));				
        return json.toString();
    }

	@ApiOperation(tags = {"Global and User Properties"}, value="Global and User Properties - Update user properties", notes="Endpoint will update user properties from an array of name/value pairs")
    @POST
    @Path("/config/properties")
    public Response postUserConfig(String json) throws ServiceException 	{
		String username = getEndUser();
		String blockList = "";
		
		if (username != null) {
			User user = ensureUserExists(username);		
			
			if (user != null) {
				Map<String, String> properties = user.getProperties();
				JSONArray props = new JSONArray(json);	

				for (int i=0; i<props.length(); i++)
				{
					JSONObject property = props.getJSONObject(i);
					String name = property.getString("name");
					String value = property.getString("value");
					
					if (!blockList.contains(name)) {
						
						if ("name".equals(name)) {
							user.setName(value);
						}
						else
							
						if ("email".equals(name)) {
							user.setEmail(value);
						}
						else {												
							properties.put(name, value);
						}
					}
				}
				return Response.status(Response.Status.OK).build();					
			}				
		}
        return Response.status(Response.Status.BAD_REQUEST).build();		
	}

	@ApiOperation(tags = {"Global and User Properties"}, value="Global and User Properties - List User Properties", notes="Endpoint to retrieve a list of all config properties for the authenticated user.")
    @GET
    @Path("/config/properties")
    public String getUserConfig() throws ServiceException 	{
		JSONObject json = new JSONObject();			
		String username = getEndUser();
		
		if (username != null) {
			json.put("domain", XMPPServer.getInstance().getServerInfo().getXMPPDomain());				
			
			User user = ensureUserExists(username);					
			
			if (user != null) {	
				json.put("username", username);				
				json.put("name", user.getName());	
				json.put("email", user.getEmail());				
				
				Map<String, String> properties = user.getProperties();
				
				for (String key : properties.keySet()) {
					json.put(key, properties.get(key));	
				}	

				Collection<Group> userGroups = SparkWeb.groupManager.getGroups(user);
				JSONArray jsonGroups = new JSONArray();	
				int i = 0;

				for (Group userGroup : userGroups) {
					String groupName = userGroup.getName();
					
					JSONObject jsonUserGroup = new JSONObject();						
					jsonUserGroup.put("group", groupName);					
					Map<String, String> groupProperties = userGroup.getProperties();
					
					for (String key : groupProperties.keySet()) {
						jsonUserGroup.put(key, groupProperties.get(key));	
					}	

					JSONArray jsonMembers = new JSONArray();	
					int j = 0;					

					for (JID memberJID : userGroup.getMembers()) {
						JSONObject jsonMember = new JSONObject();	
						String memberName = memberJID.getNode();
						
						User memberUser = ensureUserExists(memberName);		
						
						if (memberUser != null) {
							jsonMember.put("username", memberName);								
							jsonMember.put("name", memberUser.getName());	
							jsonMember.put("email", memberUser.getEmail());							
							
							Map<String, String> memberProperties = memberUser.getProperties();
							
							for (String key : memberProperties.keySet()) {
								jsonMember.put(key, memberProperties.get(key));	
							}

							jsonMembers.put(j++, jsonMember);	
						}							
					}
					
					jsonUserGroup.put("members", jsonMembers);
					jsonGroups.put(i++, jsonUserGroup);	
				}
				
				json.put("groups", jsonGroups);	
			}
		}
		
        return json.toString();
    }	
	
	//-------------------------------------------------------
    //
    //  Web Authentication
    //
    //-------------------------------------------------------

	@ApiOperation(tags = {"Web Authentication"}, value="Web Authentication - Start Registration", notes="This endpoint is used to start the webauthn registration proces")
    @POST
    @Path("/webauthn/register/start/{username}")
    public String webauthnRegisterStart(@PathParam("username") String username, String password) throws ServiceException     {	
		JSONObject json = new JSONObject();	
		
        try {
 			User user = SparkWeb.self.getUser(username);	
			
			if (user == null) {	// create a new user
				String name = username.substring(0, 1).toUpperCase() + username.substring(1);
				String email = username + "@" + XMPPServer.getInstance().getServerInfo().getXMPPDomain();
				user = SparkWeb.userManager.createUser( username, password, name, email);
			} else {
				AuthFactory.authenticate(username, password);		// authenticate user first before creating credentials	
			}			
			
			URL url = getReferer();			
			Log.debug("webauthnRegisterStart " + username + " " + url);			
			json.put("credentials", new JSONObject(startRegisterWebAuthn(username, user.getName(), url)));				

        } catch (Exception e) {
            Log.error("webauthnRegisterStart", e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
        
		return json.toString();
    }	

	@ApiOperation(tags = {"Web Authentication"}, value="Web Authentication - Finish Registration", notes="This endpoint is used to finish the webauthn registration proces")	
    @POST
    @Path("/webauthn/register/finish/{username}")
    public String webauthnRegisterFinish(@PathParam("username") String username, String credentials) throws ServiceException    {
		JSONObject json = new JSONObject();				
        Log.debug("webauthnRegisterFinish " + username + "\n" + credentials);
			
        try {
			if (finishRegisterWebAuthn(credentials, username) != null) {
				User user = SparkWeb.self.getUser(username);
				json.put("token", SparkWeb.self.makeAccessToken(user));
				return json.toString();	
			}			

        } catch (Exception e) {
            Log.error("webauthnRegisterFinish", e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
        
       throw new ServiceException("Exception", "WebAuthn registration failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
    }	

	@ApiOperation(tags = {"Web Authentication"}, value="Web Authentication - Start Authentication", notes="This endpoint is used to start the webauthn authentication proces")	
    @POST
    @Path("/webauthn/authenticate/start/{username}")
    public String webauthnAuthenticateStart(@PathParam("username") String username) throws ServiceException  {		
		JSONObject json = new JSONObject();		
		String auth = httpRequest.getHeader("authorization");		
		
		if (auth != null) { // active token, refresh
			User user = SparkWeb.self.tokens.get(auth);
			
			if (user != null && user.getUsername().equals(username)) {
				Log.debug("webauthnAuthenticateStart - refresh " + username);				
				json.put("token", SparkWeb.self.makeAccessToken(user));
				return json.toString();				
			}
		}
		
		// no active token, start webauthn procedure	
	
		try {
			URL url = getReferer();		
			Log.debug("webauthnAuthenticateStart - assertion " + username + " " + url);		
			json.put("assertion", new JSONObject(startAuthentication(username, url)));			

		} catch (Exception e) {
			Log.error("webauthnAuthenticateStart", e);
			throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
		}

		return json.toString();
    }	

	@ApiOperation(tags = {"Web Authentication"}, value="Web Authentication - End Authentication", notes="This endpoint is used to finish the webauthn authentication proces")	
    @POST
    @Path("/webauthn/authenticate/finish/{username}")
    public String webauthnAuthenticateFinish(@PathParam("username") String username, String assertion) throws ServiceException     {
		JSONObject json = new JSONObject();		
        Log.debug("webauthnAuthenticateFinish " + username + "\n" + assertion);
			
        try {
			if (finishAuthentication(assertion, username)) {
				User user = SparkWeb.self.getUser(username);
				json.put("token", SparkWeb.self.makeAccessToken(user));
				return json.toString();								
			}			

        } catch (Exception e) {
            Log.error("webauthnAuthenticateFinish", e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
        
        throw new ServiceException("Exception", "WebAuthn authentication failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
    }
	
	//-------------------------------------------------------
	//
	//	Chat
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"Chat"}, value="Chat - Send ACS chat message to XMPP destination", notes="")	
    @POST
    @Path("/chat")
    public Response postChat(String json) throws ServiceException 	{
		String username = getEndUser();
		
		if (username != null) {
			User user = ensureUserExists(username);	
			
			if (user != null) {	
				try {
					JSONObject msg = new JSONObject(json);
					Log.debug("postChat json \n" + msg);
					
					Message message = new Message();
					message.setFrom(username + "@" + XMPPServer.getInstance().getServerInfo().getXMPPDomain());
					message.setThread(msg.getString("id"));				
					message.setBody(msg.getString("body"));
					
					String sender = msg.getString("sender");
					String to = null;
					
					if (sender.startsWith("chat:")) {
						to = sender.substring(5);				
						message.setType(Message.Type.chat);					
					
					} else if (sender.startsWith("groupchat:")) {
						to = sender.substring(10);					
						message.setType(Message.Type.groupchat);						
					}

					if (to != null) {
						message.setTo(to);		
						XMPPServer.getInstance().getMessageRouter().route(message);			
						return Response.status(Response.Status.OK).build();
					}
					else {
						Log.error("postChat - invalid sender " + sender);
					}
				} catch (Exception e) {
					Log.error("postChat -  " + e, e);					
				}
			}			
		}
        return Response.status(Response.Status.BAD_REQUEST).build();		
	}			
	

	//-------------------------------------------------------
	//
	//	Presence
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"Presence"}, value="Presence - Store the presence of a user", notes="This endpoint is used to store the presence of a user")	
	@POST
    @Path("/presence")
    public Response postPresence(String json) throws ServiceException 	{
		try {	
			String username = getEndUser();

			if (username != null) {
				User user = ensureUserExists(username);	
	
			}
		} catch (Exception e) {
			Log.error("postPresence " + e, e);
		}					
	
        return Response.status(Response.Status.BAD_REQUEST).build();
	}

	@ApiOperation(tags = {"Presence"}, value="Presence - Query presence of a Teams user", notes="Endpoint to retrieve a the presence of a specific user.")	
    @GET
    @Path("/presence/{userid}")
    public String getPresence(@PathParam("userid") String userid) throws ServiceException {
		JSONObject presence = new JSONObject();		
			
		try {

		} catch (Exception e) {
			Log.error("getPresence " + e, e);
			//throw new ServiceException("Exception", e.toString(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);							
		}			
		
		return presence.toString();
    }	
		
	//-------------------------------------------------------
	//
	//	Utitlities
	//
	//-------------------------------------------------------

	private URL getReferer() throws MalformedURLException 	{
		if (httpRequest != null) {
			String url = (String) httpRequest.getHeader("referer");
			if (url == null) url = httpRequest.getRequestURL().toString();
			return new URL(url);
		}
		return null;
	}

	public String startRegisterWebAuthn(String username, String name, URL url) 	{
        Log.debug("startRegisterWebAuthn " + username + " " + name);
		
		if (relyingParty == null) createRelyingParty(url, username);
		userRegistrationStorage.removeCredential(username);
		
		SecureRandom random = new SecureRandom();
		byte[] userHandle = new byte[64];
		random.nextBytes(userHandle);

		PublicKeyCredentialCreationOptions request = relyingParty.startRegistration(StartRegistrationOptions.builder()
			.user(UserIdentity.builder()
			.name(username).displayName(name).id(new ByteArray(userHandle)).build())
			.build());		
		
		String json = "{}";
		
		try {
			requests.put(username, request);
			json = request.toCredentialsCreateJson();
			
		} catch (Exception e) {
		   Log.error( "finishAuthentication exception occurred", e );	
		}			
		return json;		
	}
	
	public RegistrationResult finishRegisterWebAuthn(String responseJson, String username) 	{	
        Log.debug("finishRegisterWebAuthn " + username + "\n" + responseJson);	
		PublicKeyCredentialCreationOptions request = (PublicKeyCredentialCreationOptions) requests.get(username);
		RegistrationResult result = null;	

		if (request != null) {		
			try {			
				PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc = PublicKeyCredential.parseRegistrationResponseJson(responseJson);			
				result = relyingParty.finishRegistration(FinishRegistrationOptions.builder()
					.request(request)
					.response(pkc)
					.build());

				userRegistrationStorage.addCredential(username, request.getUser().getId().getBytes(), result.getKeyId().getId().getBytes(), result.getPublicKeyCose().getBytes(), result.getSignatureCount());											
				
			} catch (Exception e) {
				Log.error( "finishRegisterWebAuthn exception occurred", e );			
			}
		} else {
			Log.error( "finishRegisterWebAuthn - startRegisterWebAuthn did not create a request " + requests.values());	
		}

		return result;
	}	

	public String startAuthentication(String username, URL url) 	{
        Log.debug("startAuthentication " + username);

		if (relyingParty == null) createRelyingParty(url, username);
		
		AssertionRequest request = relyingParty.startAssertion(StartAssertionOptions.builder()
			.username(Optional.of(username))
			.userHandle(userRegistrationStorage.getUserHandleForUsername(username))
			.build());

		String json = "{}";
		
		try {
			requests.put(username, request);
			json = request.toCredentialsGetJson();
			
		} catch (Exception e) {
		   Log.error( "finishAuthentication exception occurred", e );	
		}			
		return json;			
	}
	
	public boolean finishAuthentication(String responseJson, String username) 	{	
        Log.debug("finishAuthentication " + username + "\n" + responseJson);		
		AssertionRequest request = (AssertionRequest) requests.get(username);
		boolean response = false;
		
		if (request != null) {
			try {
				PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc = PublicKeyCredential.parseAssertionResponseJson(responseJson);
				AssertionResult result = relyingParty.finishAssertion(FinishAssertionOptions.builder()
					.request(request)
					.response(pkc).build());

				userRegistrationStorage.storeSignatureCount(username, result.getSignatureCount());					
				response = result.isSuccess();		
				
			} catch (Exception e) {
				Log.error( "finishAuthentication exception occurred", e );			   
			}
		} else {
			Log.error( "finishAuthentication - startAuthentication did not create a request");		
		}
		
		return response;
	}
	
	private void createRelyingParty(URL url, String username) 	{
		String hostname = XMPPServer.getInstance().getServerInfo().getHostname();
		Log.debug("Creating webauthn RelyingParty " + url);	
		Set<String> origins = new HashSet<>();	
		origins.add("https://" + hostname + ":" + JiveGlobals.getProperty("httpbind.port.secure", "7443"));		
		origins.add("http://" + hostname + ":" + JiveGlobals.getProperty("httpbind.port.plain", "7070"));
		RelyingPartyIdentity rpIdentity = RelyingPartyIdentity.builder().id(hostname).name("sparkweb").build();
		userRegistrationStorage = new UserRegistrationStorage();
		relyingParty = RelyingParty.builder().identity(rpIdentity).credentialRepository(userRegistrationStorage).origins(origins).build();				
	}
	
	private String requestToJson(Object request, String username) 	{				
		ObjectMapper jsonMapper = new ObjectMapper()
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.setSerializationInclusion(Include.NON_ABSENT)
			.registerModule(new Jdk8Module());
		
		String json = "{}";
        try
        {
			requests.put(username, request);
			json = jsonMapper.writeValueAsString(request);
			Log.debug("requestToJson " + username + "\n" + json);	
			
        } catch ( Exception ex ){
            Log.error( "requestToJson exception occurred", ex );
        }
		return json;		
	}

	public String getEndUser() throws ServiceException 	{				
        try {			
            if (OSUtils.IS_WINDOWS && JiveGlobals.getBooleanProperty("enable.sso", false)) {
                String sessionName = httpRequest.getRemoteUser();
				
				if (httpRequest.getUserPrincipal() != null)
				{				
					String windowsName = httpRequest.getUserPrincipal().getName();

					if (windowsName != null && sessionName != null && sessionName.equals(windowsName))
					{
						String userName = windowsName;							
						int pos = windowsName.indexOf("\\");
						if (pos > -1) userName = windowsName.substring(pos + 1).toLowerCase();				
						return userName;					
					}
				}
            }
			
			if (httpRequest != null) {			
				String remoteAddr = httpRequest.getHeader("X-FORWARDED-FOR");
				
				if (remoteAddr == null || "".equals(remoteAddr)) {
					remoteAddr = httpRequest.getRemoteAddr();
				}

				Log.debug("found IP Address " + remoteAddr);
			
				if (remoteAddr != null && !("".equals(remoteAddr))) {	
					List<User> users = SparkWeb.self.getUsersByProperty("ip_address", remoteAddr);
					
					if (users.size() > 1) {
						Log.error("getEndUser failed multiple users with same ip address");
						return null;					
					}
					else 
						
					if (users.size() == 1) {
						return users.get(0).getUsername();	
					}
				}

				String auth = httpRequest.getHeader("authorization");

				if (!isNull(auth)) 	{	
					User user = SparkWeb.self.tokens.get(auth);
					
					if (user != null) {
						return user.getUsername();					
					}
					else {
						String[] usernameAndPassword = BasicAuth.decode(auth);	
						return usernameAndPassword[0];						
					}			
				}
			} else return "testuser1"; // for SparkWebAPITest (unit tests)
			
        } catch(Exception e) {
			Log.error("getEndUser failed", e);
        }
		return null;
	}
	
	private boolean isNull(String value)   {
        return (value == null || "undefined".equals(value)  || "null".equals(value) || "".equals(value.trim()) || "unknown".equals(value) || "none".equals(value));
    }
	
	private String removeLastChar(String str) {
		return removeLastChars(str, 1);
	}

	private String removeLastChars(String str, int chars) {
		return str.substring(0, str.length() - chars);
	}
	
	private User ensureUserExists(String username) 	{
		return SparkWeb.self.getUser(username);
	}	
	
	private Date validateDate(String stringDate) {
		Date date = null;

		if (stringDate != null && stringDate.length() > 0) {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			try {
				date = formatter.parse(stringDate);
			}
			catch (Exception e) {
				Log.error("validateDate", e);
			}

		}	
		return date;		
	}

	private JSONObject bookmarkToJson(Bookmark bookmark) {
		JSONObject json = new JSONObject();						
		long id = bookmark.getBookmarkID();
		 				
		json.put("id", id);								
		json.put("name", bookmark.getName());	
		json.put("value", bookmark.getValue());						
		
		for (UserProperty userProperty : bookmark.getProperties()) {
			String key = userProperty.getKey();								
			json.put(key, userProperty.getValue());
		}
		return json;
	}
}
