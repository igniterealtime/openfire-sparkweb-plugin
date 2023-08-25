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
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jivesoftware.util.*;
import org.jivesoftware.openfire.handler.IQRegisterHandler;
import org.jivesoftware.openfire.container.PluginMetadataHelper;
import org.jivesoftware.openfire.admin.AdminManager;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.plugin.rest.BasicAuth;
import org.jivesoftware.openfire.plugin.rest.exceptions.*;
import org.jivesoftware.openfire.plugin.rest.entity.PublicKey;
import org.jivesoftware.openfire.plugin.rest.entity.*;
import org.jivesoftware.openfire.plugin.rest.utils.*;
import org.jivesoftware.openfire.plugin.rest.controller.*;

import org.jivesoftware.openfire.user.*;
import org.jivesoftware.openfire.group.*;
import org.jivesoftware.openfire.roster.*;
import org.jivesoftware.openfire.plugin.spark.*;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.AuthFactory;
import org.jivesoftware.openfire.archive.*;
import org.jivesoftware.openfire.plugin.spark.Bookmark;
import org.jivesoftware.openfire.plugin.spark.Bookmarks;
import org.jivesoftware.openfire.plugin.spark.BookmarkManager;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.jivesoftware.smack.OpenfireConnection;
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
import org.broadbear.link.preview.*;
import com.google.common.io.BaseEncoding;
import org.apache.http.HttpResponse;
import nl.martijndwars.webpush.Utils;
import nl.martijndwars.webpush.*;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

@SwaggerDefinition(tags = { @Tag(name = "Authentication", description = "provides server-side authentication services"), @Tag(name = "Chat", description = "provides chat services for the authenticated user"), @Tag(name = "Group Chat", description = "provides groupchat services to manage contacts"), @Tag(name = "Presence", description = "provides presence services"), @Tag(name = "Collaboration", description = "provides meeting and other collaboration services"), @Tag(name = "User Management", description = "provides user services for the authenticated user"), @Tag(name = "Contact Management", description = "provides user roster services to manage contacts"), @Tag(name = "Web Push", description = "provides server-side Web Push services"), @Tag(name = "Bookmarks", description = "Create, update and delete Openfire bookmarks") }, info = @Info(description = "SparkWeb REST API adds support for a whole range of modern web service connections to Openfire/XMPP", version = "0.0.1", title = "SparkWeb API"), schemes = {SwaggerDefinition.Scheme.HTTPS, SwaggerDefinition.Scheme.HTTP}, securityDefinition = @SecurityDefinition(apiKeyAuthDefinitions = {@ApiKeyAuthDefinition(key = "authorization", name = "authorization", in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER)}))
@Api(authorizations = {@Authorization("authorization")})
@Path("rest")
@Produces(MediaType.APPLICATION_JSON)

public class SparkWebAPI {   
	private static final Logger Log = LogManager.getLogger(SparkWebAPI.class);	
	private static RelyingParty relyingParty = null;
	private static UserRegistrationStorage userRegistrationStorage;	
	
    private static final String COULD_NOT_UPDATE_THE_ROSTER = "Could not update the roster";
    private static final String COULD_NOT_CREATE_ROSTER_ITEM = "Could not create roster item";
	
	public static final HashMap<String, Object> requests = new HashMap<>();

	UserServiceController userServiceController;
	MUCRoomController mucRoomController;
	
	@Context
	private HttpServletRequest httpRequest;
	
	@PostConstruct
	public void init() 	{
        userServiceController = UserServiceController.getInstance();
		mucRoomController = MUCRoomController.getInstance();
	}	


	//-------------------------------------------------------
	//
	//	Users
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"User Management"}, value="Get users", notes="Retrieve all users defined in Openfire (with optional filtering)")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A list of Openfire users", response = UserEntities.class)})
    @Path("/users")
    @GET
    public UserEntities getUsers(@ApiParam(value = "Search/Filter by username. This act like the wildcard search %String%", required = false) @QueryParam("search") String userSearch, @ApiParam(value = "Filter by a user property name", required = false) @QueryParam("propertyKey") String propertyKey, @ApiParam(value = "Filter by user property value. Note: This can only be used in combination with a property name parameter", required = false) @QueryParam("propertyValue") String propertyValue) throws ServiceException {
        return userServiceController.getUserEntities(userSearch, propertyKey, propertyValue);
    }

	@ApiOperation(tags = {"User Management"}, value="Get authenticated user", notes="Retrieve a user that is defined in Openfire")		
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Openfire user was retrieved", response = UserEntity.class), @ApiResponse(code = 404, message = "No user with that username was found") })	
    @Path("/user")
    @GET
    public UserEntity getUser() throws ServiceException    {
 		String username = getEndUser();        
		return userServiceController.getUserEntity(username);
    }	
	
	@ApiOperation(tags = {"User Management"}, value="Update authenticated user", notes="Update the authenticated user in Openfire")		
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Openfire user was updated"), @ApiResponse(code = 404, message = "No user with that username was found") })	
    @Path("/user")
    @PUT
    public Response updateUser(@ApiParam(value = "The definition of the authenticated user to update", required = true) UserEntity userEntity)  throws ServiceException    {
 		String username = getEndUser();
		userServiceController.updateUser(username, userEntity);
        return Response.status(Response.Status.OK).build();	
    }

	@ApiOperation(tags = {"User Management"}, value="Delete authenticated user", notes="Delete authenticated user in Openfire")		
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The Openfire user was deleted"), @ApiResponse(code = 404, message = "No user with that username was found") })	
    @Path("/user")
    @DELETE
    public Response deleteUser() throws ServiceException    {
		String username = getEndUser();	
        userServiceController.deleteUser(username);
        return Response.status(Response.Status.OK).build();	
    }	

	@ApiOperation(tags = {"User Management"}, value="Get user's groups", notes="Retrieve names of all groups that a particular user is in")		
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The names of the groups that the user is in", response = UserGroupsEntity.class) })	
    @Path("/user/groups")
    @GET
    public UserGroupsEntity getUserGroups() throws ServiceException    {
 		String username = getEndUser();        
        return new UserGroupsEntity(userServiceController.getUserGroups(username));
    }	

	@ApiOperation(tags = {"User Management"}, value="Add user to groups", notes="Add authenticated user to a collection of groups. When a group that is provided does not exist, it will be automatically created if possible")		
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The user was added to all groups"), @ApiResponse(code = 404, message = "One or more groups could not be found") })	
    @Path("/user/groups")
    @POST
    public Response addUserToGroups(@ApiParam(value = "A collection of names for groups that the user is to be added to", required = true) UserGroupsEntity userGroupsEntity)  throws ServiceException    {
 		String username = getEndUser();
		userServiceController.addUserToGroups(username, userGroupsEntity);
        return Response.status(Response.Status.OK).build();	
    }

	@ApiOperation(tags = {"User Management"}, value="Add user to a group", notes="Add authenticated user to a collection of groups. When a group that is provided does not exist, it will be automatically created if possible")		
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The user was added to all groups"), @ApiResponse(code = 400, message = "The group could not be found") })	
    @Path("/user/groups/{groupName}")
    @POST
    public Response addUserToGroup(@ApiParam(value = "The name of the group that the user is to be added to", required = true) @PathParam("groupName") String groupName)  throws ServiceException    {
 		String username = getEndUser();
		userServiceController.addUserToGroup(username, groupName);
        return Response.status(Response.Status.OK).build();	
    }

	@ApiOperation(tags = {"User Management"}, value="Delete user from group", notes="Removes a user from a group")		
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The user was taken out of the group"), @ApiResponse(code = 400, message = "The group could not be found") })	
    @Path("/user/groups/{groupName}")
    @DELETE
    public Response deleteUserFromGroup(@ApiParam(value = "The name of the group that the user is to be added to", required = true) @PathParam("groupName") String groupName)  throws ServiceException    {
 		String username = getEndUser();
		userServiceController.deleteUserFromGroup(username, groupName);
        return Response.status(Response.Status.OK).build();	
    }	
	
	@ApiOperation(tags = {"User Management"}, value="Delete user from groups", notes="Removes a user from a collection of groups.")		
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The user was taken out of the group"), @ApiResponse(code = 404, message = "One or more groups could not be found") })	
    @Path("/user/groups")
    @DELETE
    public Response deleteUserFromGroups(@ApiParam(value = "A collection of names for groups that the user is to be added to", required = true) UserGroupsEntity userGroupsEntity)  throws ServiceException    {
 		String username = getEndUser();
		userServiceController.deleteUserFromGroups(username, userGroupsEntity);
        return Response.status(Response.Status.OK).build();	
    }	

	@ApiOperation(tags = {"User Management"}, value="List global properties affecting this user", notes="Endpoint will retrieve all Openfire Global properties that are used by this authenticated user")	
    @GET
    @Path("/config/global")
    public String getGlobalConfig() throws ServiceException 	{
		JSONObject json = new JSONObject();		
		json.put("version", PluginMetadataHelper.getVersion( SparkWeb.self ).getVersionString());	
		json.put("about", PluginMetadataHelper.getName( SparkWeb.self ));				
        return json.toString();
    }

	@ApiOperation(tags = {"User Management"}, value="Update user properties", notes="Endpoint will update user properties from an array of name/value pairs")
    @POST
    @Path("/config/properties")
    public Response postUserConfig(@ApiParam(value = "A JSON array of name pairs (name/value) to set the value of a property", required = true) String json) throws ServiceException 	{
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

	@ApiOperation(tags = {"User Management"}, value="List User Properties", notes="Endpoint to retrieve a list of all config properties for the authenticated user")
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
	//	Contacts
	//
	//-------------------------------------------------------
	
	@ApiOperation(tags = {"Contact Management"}, value="Retrieve user roster", notes="Get a list of all roster entries (buddies / contact list) of a authenticated user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All roster entries", response = RosterEntities.class), @ApiResponse(code = 404, message = "No user with that username was found") })	
    @Path("/roster")
    @GET
    public RosterEntities getUserRoster() throws ServiceException {
		String username = getEndUser();		
        return userServiceController.getRosterEntities(username);
    }
	
	@ApiOperation(tags = {"Contact Management"}, value="Create roster entry", notes="Add a roster entry to the roster (buddies / contact list) of a particular user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The entry was added to the roster"), @ApiResponse(code = 400, message = "A roster entry cannot be added to a 'shared group' (try removing group names from the roster entry and try again)") , @ApiResponse(code = 404, message = "No user of with this username exists") , @ApiResponse(code = 409, message = "A roster entry already exists for the provided contact JID")})	
    @Path("/roster")
    @POST
    public Response createRoster(@ApiParam(value = "The definition of the roster entry that is to be added", required = true) RosterItemEntity rosterItemEntity) throws ServiceException {
		String username = getEndUser();	
		
        try {
            userServiceController.addRosterItem(username, rosterItemEntity);
        } catch (UserNotFoundException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_NOT_FOUND_EXCEPTION, Response.Status.NOT_FOUND, e);
        } catch (UserAlreadyExistsException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_ALREADY_EXISTS_EXCEPTION, Response.Status.CONFLICT, e);
        } catch (SharedGroupException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.SHARED_GROUP_EXCEPTION, Response.Status.BAD_REQUEST, e);
        }
        return Response.status(Response.Status.OK).build();
    }	
	
	@ApiOperation(tags = {"Contact Management"}, value="Remove roster entry", notes="Removes one of the roster entries (contacts) of the authenticated user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Roster entry removed"), @ApiResponse(code = 400, message = "A roster entry cannot be removed from a 'shared group'"), @ApiResponse(code = 404, message = "No user of with this username exists, or its roster did not contain this entry") })	
    @Path("/roster/{jid}")
    @DELETE
    public Response deleteRosterItem(@ApiParam(value = "The JID of the entry/contact to remove", required = true) @PathParam("jid") String jid) throws ServiceException {
		String username = getEndUser();		
		
        try {
            userServiceController.deleteRosterItem(username, jid);
        } catch (SharedGroupException e) {
            throw new ServiceException("Could not delete the roster item", jid, ExceptionType.SHARED_GROUP_EXCEPTION, Response.Status.BAD_REQUEST, e);
        }
        return Response.status(Response.Status.OK).build();		
    }

	@ApiOperation(tags = {"Contact Management"}, value="Update roster entry", notes="Update a roster entry to the roster (buddies / contact list) of a particular user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The entry was updated in the roster"), @ApiResponse(code = 400, message = "A roster entry cannot be updated with a 'shared group'"), @ApiResponse(code = 404, message = "No user of with this username exists")})	
    @Path("/roster/{jid}")
    @PUT
    public Response updateRosterItem(@ApiParam(value = "The JID of the entry/contact to remove", required = true) @PathParam("jid") String jid, @ApiParam(value = "The definition of the roster entry that is to be updated", required = true) RosterItemEntity rosterItemEntity) throws ServiceException {
		String username = getEndUser();	
		
        try {
            userServiceController.updateRosterItem(username, jid, rosterItemEntity);
        } catch (UserNotFoundException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_NOT_FOUND_EXCEPTION, Response.Status.NOT_FOUND, e);
        } catch (UserAlreadyExistsException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_ALREADY_EXISTS_EXCEPTION, Response.Status.CONFLICT, e);
        } catch (SharedGroupException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.SHARED_GROUP_EXCEPTION, Response.Status.BAD_REQUEST, e);
        }
        return Response.status(Response.Status.OK).build();
    }	
	
	//-------------------------------------------------------
	//
	//	Chat
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"Chat"}, value="Get chat messages", notes="Retrieves chat messages from Openfire messages archive")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The messages were retrieved", response = Conversations.class), @ApiResponse(code = 400, message = "The messages could not be retrieved")})	
    @Path("/chat/messages")
    @GET
    public Conversations getChatConversations(@ApiParam(value = "Search keywords", required = false) @QueryParam("keywords") String keywords, @ApiParam(value = "The message target", required = false) @QueryParam("to") String to, @ApiParam(value = "The start date in MM/dd/yy format", required = false) @QueryParam("start") String start, @ApiParam(value = "The end date in MM/dd/yy format", required = false) @QueryParam("end") String end ) throws ServiceException   {
		 return getConversations(keywords, to, start, end, null, null);
    }				

	@ApiOperation(tags = {"Chat"}, value="Post chat message", notes="post a chat message to an xmpp address")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The messages was posted"), @ApiResponse(code = 400, message = "The messages could not be posted")})	
    @Path("/chat/message/{to}")
    @POST
    public Response postMessage(@ApiParam(value = "The JID of the target xmpp address", required = true) @PathParam("to") String to, @ApiParam(value = "The text message to be posted", required = true) String body) throws ServiceException   {
		String username = getEndUser();	
        Log.debug("postMessage " + username + " " + to + "\n" + body);

        try {
            OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

            connection.sendChatMessage(body, to);

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }

	@ApiOperation(tags = {"Chat"}, value="Post chat state indicator", notes="Post a chat state to an xmpp address")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The chat state was posted"), @ApiResponse(code = 400, message = "The chat state could not be posted")})	
    @Path("/chat/chatstate/{state}/{to}")
    @POST
    public Response postChatState(@ApiParam(value = "The chat state to be posted. It can be 'composing', 'paused', 'active', 'inactive', 'gone'", required = true) @PathParam("state") String state, @ApiParam(value = "The JID of the target xmpp address", required = true) @PathParam("to") String to) throws ServiceException    {
		String username = getEndUser();	
        Log.debug("postChatState " + username + " " + to + "\n" + state);

        try {
            OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

            connection.setCurrentState(state, to);

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }
	
    //-------------------------------------------------------
    //
    //  Groupchat
    //
    //-------------------------------------------------------

	@ApiOperation(tags = {"Group Chat"}, value="Get groupchat messages", notes="Retrieves chat groupchat messages from Openfire messages archive")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The messages were retrieved", response = Conversations.class), @ApiResponse(code = 400, message = "The messages could not be retrieved")})	
    @Path("/groupchat/messages")
    @GET
    public Conversations getGroupChatConversations(@ApiParam(value = "Search keywords", required = false) @QueryParam("keywords") String keywords, @ApiParam(value = "The start date in MM/dd/yy format", required = false) @QueryParam("start") String start, @ApiParam(value = "The end date in MM/dd/yy format", required = false) @QueryParam("end") String end, @ApiParam(value = "The groupchat room used", required = false) @QueryParam("room") String room, @ApiParam(value = "The groupchat service name", required = false) @DefaultValue("conference") @QueryParam("service") String service) throws ServiceException   {
		 return getConversations(keywords, null, start, end, room, service);
    }
	
	@ApiOperation(tags = {"Group Chat"}, value="Get chat rooms", notes="Get a list of all multi-user chat rooms of a particular chat room service")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All chat rooms", response = MUCRoomEntities.class), @ApiResponse(code = 404, message = "MUC service does not exist or is not accessible")})	
    @Path("/groupchat/rooms")
    @GET
    public MUCRoomEntities getMUCRooms(@ApiParam(value = "The name of the MUC service for which to return all chat rooms", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName, @ApiParam(value = "Room type-based filter: 'all' or 'public'", required = false) @DefaultValue(MUCChannelType.PUBLIC) @QueryParam("type") String channelType, @ApiParam(value = "Search/Filter by room name.\nThis act like the wildcard search %String%", required = false) @QueryParam("search") String roomSearch, @ApiParam(value = "For all groups defined in owners, admins, members and outcasts, list individual members instead of the group name", required = false) @DefaultValue("false") @QueryParam("expandGroups") Boolean expand)  throws ServiceException   {
        return mucRoomController.getChatRooms(serviceName, channelType, roomSearch, expand);
    }

	@ApiOperation(tags = {"Group Chat"}, value="Get chat room", notes="Get information of a specific multi-user chat room")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The chat room", response = MUCRoomEntity.class), @ApiResponse(code = 404, message = "The chat room (or its service) can not be found or is not accessible")})	
    @Path("/groupchat/room/{roomName}")
    @GET
    public MUCRoomEntity getMUCRoomJSON2(@ApiParam(value = "The name of the MUC room", required = true) @PathParam("roomName") String roomName, @ApiParam(value = "The name of the MUC service", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName, @ApiParam(value = "For all groups defined in owners, admins, members and outcasts, list individual members instead of the group name", required = false) @DefaultValue("false") @QueryParam("expandGroups") Boolean expand) throws ServiceException    {
        return mucRoomController.getChatRoom(roomName, serviceName, expand);
    }


	@ApiOperation(tags = {"Group Chat"}, value="Get room participants", notes="Get all participants of a specific multi-user chat room")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The chat room participants"), @ApiResponse(code = 404, message = "The chat room (or its service) can not be found or is not accessible")})	
    @Path("/groupchat/room/{roomName}/participants")
    @GET
    public ParticipantEntities getMUCRoomParticipants(@ApiParam(value = "The name of the MUC room", required = true) @PathParam("roomName") String roomName, @ApiParam(value = "The name of the MUC service", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName)  throws ServiceException   {
        return mucRoomController.getRoomParticipants(roomName, serviceName);
    }

	@ApiOperation(tags = {"Group Chat"}, value="Get room occupants", notes="Get all occupants of a specific multi-user chat room")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The chat room occupants", response = OccupantEntities.class), @ApiResponse(code = 404, message = "The chat room (or its service) can not be found or is not accessible")})	
    @Path("/groupchat/room/{roomName}/occupants")
    @GET
    public OccupantEntities getMUCRoomOccupants(@ApiParam(value = "The name of the MUC room", required = true) @PathParam("roomName") String roomName, @ApiParam(value = "The name of the MUC service", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName)  throws ServiceException   {
        return mucRoomController.getRoomOccupants(roomName, serviceName);
    }

	@ApiOperation(tags = {"Group Chat"}, value="Get room history", notes="Get messages that have been exchanged in a specific multi-user chat room")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The chat room message history", response = MUCRoomMessageEntities.class), @ApiResponse(code = 404, message = "The chat room (or its service) can not be found or is not accessible")})	
    @Path("/groupchat/room/{roomName}/chathistory")
    @GET
    public MUCRoomMessageEntities getMUCRoomHistory(@ApiParam(value = "The name of the MUC room", required = true) @PathParam("roomName") String roomName, @ApiParam(value = "The name of the MUC service", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName)  throws ServiceException  {
        roomName = JID.nodeprep(roomName);		
        return mucRoomController.getRoomHistory(roomName, serviceName);
    }

	@ApiOperation(tags = {"Group Chat"}, value="Join groupchat", notes="Join a groupchat by entering a MUC room")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The user has joined groupchat"), @ApiResponse(code = 404, message = "The chat room (or its service) can not be found or is not accessible")})	
    @Path("/groupchat/room/{roomName}")
    @PUT	
    public Response joinRoom(@ApiParam(value = "The name of the MUC room to join", required = true) @PathParam("roomName") String roomName, @ApiParam(value = "The name of the MUC service", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName)  throws ServiceException  {
		String username = getEndUser();
        Log.debug("joinRoom " + username + " " + serviceName + " " + roomName);

        try {
           OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

			User user = SparkWeb.self.getUser(username);
			String nickName = user.getName() == null ? username : user.getName();
		
            if (!connection.joinRoom(roomName + "@" + serviceName + "" + XMPPServer.getInstance().getServerInfo().getXMPPDomain(), nickName))  {
                throw new ServiceException("Exception", "join room failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }

	@ApiOperation(tags = {"Group Chat"}, value="Leave groupchat", notes="Leave a groupchat by leaving a MUC room")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The user has left groupchat"), @ApiResponse(code = 404, message = "The chat room (or its service) can not be found or is not accessible")})	
    @Path("/groupchat/room/{roomName}")
    @DELETE	
    public Response leaveRoom(@ApiParam(value = "The name of the MUC room to leave", required = true) @PathParam("roomName") String roomName, @ApiParam(value = "The name of the MUC service", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName)  throws ServiceException  {
		String username = getEndUser();
        Log.debug("leaveRoom " + username + " " + serviceName + " " + roomName);

        try {
           OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

            if (!connection.leaveRoom(roomName + "@" + serviceName + "" + XMPPServer.getInstance().getServerInfo().getXMPPDomain())) {
                throw new ServiceException("Exception", "join room failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }

	@ApiOperation(tags = {"Group Chat"}, value="Post a message to a groupchat", notes="Post a message to a groupchat")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The message is posted"), @ApiResponse(code = 404, message = "The chat room (or its service) can not be found or is not accessible")})	
    @Path("/groupchat/room/{roomName}")
    @POST	
    public Response postToRoom(@ApiParam(value = "The name of the MUC room to post to", required = true) @PathParam("roomName") String roomName, @ApiParam(value = "The name of the MUC service", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName, @ApiParam(value = "The text message to be posted", required = true) String body)  throws ServiceException  {
		String username = getEndUser();
        Log.debug("postToRoom " + username + " " + serviceName + " " + roomName);
        try {
           OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

            if (!connection.sendRoomMessage(roomName + "@" + serviceName + "" + XMPPServer.getInstance().getServerInfo().getXMPPDomain(), body)) {
                throw new ServiceException("Exception", "join room failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }

	@ApiOperation(tags = {"Group Chat"}, value="Invite another user", notes="Invite another user to a groupchat")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The invitation has been sent"), @ApiResponse(code = 404, message = "The chat room (or its service) can not be found or is not accessible")})	
    @Path("/groupchat/room/{roomName}/{invitedJid}")
    @POST	
    public Response inviteToRoom(@ApiParam(value = "The name of the MUC room", required = true) @PathParam("roomName") String roomName, @ApiParam(value = "The name of the MUC service", required = false) @DefaultValue("conference") @QueryParam("serviceName") String serviceName, @ApiParam(value = "The xmpp address of the person to be invited", required = true) @PathParam("invitedJid") String invitedJid, @ApiParam(value = "The reason for the invitation", required = true) String reason)  throws ServiceException  {
		String username = getEndUser();
        Log.debug("inviteToRoom " + username + " " + serviceName + " " + roomName + " " + invitedJid + "\n" + reason);
		
        try {
           OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

            if (!connection.inviteToRoom(roomName + "@" + serviceName + "" + XMPPServer.getInstance().getServerInfo().getXMPPDomain(), invitedJid, reason)) {
                throw new ServiceException("Exception", "join room failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }


	//-------------------------------------------------------
	//
	//	Other Collaboration Services
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"Collaboration"}, value="Request meeting URL", notes="Request for online meeting URL needed to join and share with other users")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The request was accepted", response = OnlineMeetingEntity.class), @ApiResponse(code = 400, message = "The meeting url request failed")})	
    @Path("/meet/{service}/{id}")
    @GET
    public OnlineMeetingEntity onlineMeetingRequest(@ApiParam(value = "The online meeting service required Only 'jitsi' and 'galene' are supported", required = true) @PathParam("service") String service, @ApiParam(value = "The online meeting room, group or identity requested for", required = true) @PathParam("id") String id) throws ServiceException  {
		String username = getEndUser();	
        try {
            OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }
			
            JSONObject response = null;
			
			if ("jitsi".equals(service)) {
				response = connection.getJitsiMeetRequest(id);
			}
			else

			if ("galene".equals(service)) {
				response = connection.getGaleneRequest(id);
			}				

            if (response == null || response.has("error")) {
                throw new ServiceException("Exception", response.getString("error"), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

            return new OnlineMeetingEntity(response.getString("url"));

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }
	
	@ApiOperation(tags = {"Collaboration"}, value="Request file upload", notes="Request for GET and PUT URLs needed to upload and share a file with other users")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The request was accepted"), @ApiResponse(code = 400, message = "The upload request failed")})	
    @Path("/upload/{fileName}/{fileSize}")
    @GET
    public String uploadRequest(@ApiParam(value = "The file name to be upload", required = true) @PathParam("fileName") String fileName, @ApiParam(value = "The size of the file to be uploaded", required = true) @PathParam("fileSize") String fileSize) throws ServiceException  {
		String username = getEndUser();	
        try {
            OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }
			
            JSONObject response = connection.getUploadRequest(fileName, Long.parseLong(fileSize));

            if (response.has("error")) {
                throw new ServiceException("Exception", response.getString("error"), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

            return response.toString();

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }

	@ApiOperation(tags = {"Collaboration"}, value="Request URL preview", notes="Request for URL preview metadata")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The metadata was obtained"), @ApiResponse(code = 400, message = "The preview request failed")})	
    @Path("/preview/{quality}/{url}")
    @GET	
    public String previewLink(@ApiParam(value = "The quality of the preview image on a scale 1-9", required = true) @PathParam("quality") String quality, @ApiParam(value = "The url to be previewd", required = true) @PathParam("url") String url) throws ServiceException   {
        Log.debug("previewLink " + url + " " + quality);

        try {
            JSONObject jsonObject = new JSONObject();
            SourceContent sourceContent = TextCrawler.scrape(url, Integer.parseInt(quality));

            if (sourceContent == null) {
                throw new ServiceException("Exception", "bad url", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }
			
			List<String> images = sourceContent.getImages();
			
            if (images != null && images.size() > 0)      	jsonObject.put("image", sourceContent.getImages().get(0));
            if (sourceContent.getDescription() != null) 	jsonObject.put("descriptionShort", sourceContent.getDescription());
            if (sourceContent.getTitle()!=null)         	jsonObject.put("title", sourceContent.getTitle());

            return jsonObject.toString();

        } catch (Exception e) {
			Log.error("previewLink " + e, e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }
	
	//-------------------------------------------------------
	//
	//	Presence
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"Presence"}, value="Get contacts presence", notes="Retrieve a list of all roster entries (buddies / contact list) with presence of a authenticated user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All roster entries with presence", response = RosterEntities.class), @ApiResponse(code = 400, message = "No xmpp connection found for authenticated user") })	
    @Path("/presence/roster")
    @GET
    public RosterEntities getUserRosterWithPresence() throws ServiceException {
		String username = getEndUser();	
		
		try {
			OpenfireConnection connection = OpenfireConnection.getConnection(username);

			if (connection != null) {
				return connection.getRosterEntities();	
			}				
		} catch (Exception e) {
			Log.error("getUserRosterWithPresence " + e, e);
		}

		throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
    }
	
	@ApiOperation(tags = {"Presence"}, value="Probe a target user presence", notes="Request the presence of an specific user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Presence of user requested", response = PresenceEntity.class), @ApiResponse(code = 400, message = "No xmpp connection found for authenticated user or authenticated user is not premitted to probe user presence") })	
    @Path("/presence/{target}")
    @GET
    public PresenceEntity probePresence(@ApiParam(value = "The username to be probed", required = true) @PathParam("target") String target) throws ServiceException {
		String username = getEndUser();	
		
		try {		
			JID prober = new JID(username + "@" + XMPPServer.getInstance().getServerInfo().getXMPPDomain());

			if (username.equals(target) || SparkWeb.presenceManager.canProbePresence(prober, target)) {		
				User user = SparkWeb.userManager.getUser(target);
				Presence presence = SparkWeb.presenceManager.getPresence(user);
				return new PresenceEntity(target, presence.getShow() != null ? presence.getShow().toString() : "available", presence.getStatus());
			}
		} catch (Exception e) {  
			Log.error("probePresence " + e, e);		
		}
		
		return new PresenceEntity(target, "unknown", "");
	}		
	
	@ApiOperation(tags = {"Presence"}, value="Set Presence", notes="Update the presence state of the authenticated user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Presence was set"), @ApiResponse(code = 400, message = "No xmpp connection found for authenticated user")})	
    @Path("/presence")
    @POST
    public Response postPresence(@ApiParam(value = "The availability state of the authenticated user", required = false) @QueryParam("show") String show, @ApiParam(value = "A detailed description of the availability state", required = false) @QueryParam("status") String status) throws ServiceException   {
        Log.debug("postPresence " + show + " " + status);

        try {
			String username = getEndUser();				
            OpenfireConnection connection = OpenfireConnection.getConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

            connection.postPresence(show, status);

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }

	//-------------------------------------------------------
	//
	//	Openfire Bookmarks API
	//
	//-------------------------------------------------------

	@ApiOperation(tags = {"Bookmarks"}, value="Create a new bookmark", notes="This endpoint is used to create a new bookmark")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bookmark was created", response = Bookmark.class), @ApiResponse(code = 400, message = "Bookmark could not be created")})	
    @Path("/bookmark")
    @POST	
    public Bookmark createBookmark(@ApiParam(value = "The bookmark definition", required = true) Bookmark newBookmark) throws ServiceException     {
        Log.debug("createBookmark " + newBookmark);

        try {
            return new Bookmark(newBookmark.getType(), newBookmark.getName(), newBookmark.getValue(), newBookmark.getUsers(), newBookmark.getGroups());

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }
	
	@ApiOperation(tags = {"Bookmarks"}, value="Get all bookmark", notes="This endpoint is used to retrieve all bookmarks")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List of bookmarks", response = Bookmarks.class), @ApiResponse(code = 400, message = "Bookmarks could not be retrieved")})	
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
	
	@ApiOperation(tags = {"Bookmarks"}, value="Get a specific bookmark", notes="This endpoint is used to retrieve a specific bookmark")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bookmark was retrieved", response = Bookmark.class), @ApiResponse(code = 400, message = "Bookmark could not be retrieved")})	
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
	
	@ApiOperation(tags = {"Bookmarks"}, value="Delete a specific bookmark", notes="This endpoint is used to delete a specific bookmark")
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
	
	@ApiOperation(tags = {"Bookmarks"}, value="Update a specific bookmark", notes="This endpoint is used to update a specific bookmark")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bookmark was updated", response = Bookmark.class), @ApiResponse(code = 400, message = "Bookmark could not be updated")})	
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
	
	@ApiOperation(tags = {"Bookmarks"}, value="Create/Update a bookmark property", notes="This endpoint is used to create or update a bookmark property value")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bookmark was updated", response = Bookmark.class), @ApiResponse(code = 400, message = "Bookmark could not be updated")})	
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
	
	@ApiOperation(tags = {"Bookmarks"}, value="Delete a bookmark property", notes="This endpoint is used to delete a bookmark property")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bookmark was updated", response = Bookmark.class), @ApiResponse(code = 400, message = "Bookmark could not be updated")})	
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
    //  Web Push
    //
    //-------------------------------------------------------

	@ApiOperation(tags = {"Web Push"}, value="Get all web push subscribers", notes="This endpoint is used to obtain all web push subscribers")
    @Path("/webpush/subscribers")	
    @GET	
    public UserEntities getPushSubscribers()  throws ServiceException {
        List<UserEntity> users = new ArrayList<UserEntity>();
        List<User> usernames = SparkWeb.self.getUsersByProperty("webpush.subscribe.%", null);

        for (User user : usernames) {
            users.add(new UserEntity(user.getUsername(), user.getName(), user.getEmail()));			
        }

        UserEntities userEntities = new UserEntities();
        userEntities.setUsers(users);		
        return userEntities;
    }

	@ApiOperation(tags = {"Web Push"}, value="Get the webpush vapid key", notes="This endpoint is used to obtain the vapid key need by the client to sign web push messages")
    @Path("/webpush/vapidkey")
    @GET
    public PublicKey getWebPushPublicKey() throws ServiceException {
		String username = getEndUser();
        Log.debug("getWebPushPublicKey " + username);

        String publicKey = fetchWebPushPublicKey(username);

        if (publicKey == null) {
            publicKey = JiveGlobals.getProperty("vapid.public.key", null);
        }

        if (publicKey == null)
            throw new ServiceException("Exception", "public key not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION,  Response.Status.NOT_FOUND);

        return new PublicKey(publicKey);
    }	

	@ApiOperation(tags = {"Web Push"}, value="Store web push subscription for this user", notes="This endpoint is used to save a subscription created by a web client for this user")
    @Path("/webpush/subscribe/{resource}")	
    @PUT
    public Response postWebPushSubscription(@ApiParam(value = "A resource name to tag the subscription", required = true) @PathParam("resource") String resource, @ApiParam(value = "The subscription as created by the web client", required = true) String subscription) throws ServiceException {
		String username = getEndUser();
        Log.debug("postWebPushSubscription " + username + " " + resource + "\n" + subscription);

		User user = SparkWeb.self.getUser(username);
		
		if (user != null) {
			user.getProperties().put("webpush.subscribe" + resource, subscription);
			return Response.status(Response.Status.OK).build();
		}

        return Response.status(Response.Status.BAD_REQUEST).build();
    }	

	@ApiOperation(tags = {"Web Push"}, value="Send a notification to all subscriptions of another user", notes="This endpoint is used to push a notification to all subscriptions of the specified user")	
    @POST
    @Path("/webpush/notify/{target}")
    public Response postWebPushNotification(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("target") String target, @ApiParam(value = "The notification to be pushed to the user", required = true) NotificationEntity notification) throws ServiceException {
		String username = getEndUser(); 
		Log.debug("postWebPush " + username + " " + target + "\n" + notification.getBody());

		User self = SparkWeb.self.getUser(username);
        User user = SparkWeb.self.getUser(target);
        
		if (user == null || self == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		if (SparkWeb.self.postWebPush(user, notification)) {
			return Response.status(Response.Status.OK).build();	
		}	

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
	
	@ApiOperation(tags = {"Web Push"}, value="Send a text message to all subscriptions of another user", notes="This endpoint is used to push a text message to all subscriptions of the specified user")	
    @POST
    @Path("/webpush/message/{target}")
    public Response postWebPushText(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("target") String target, @ApiParam(value = "The text message to be pushed to the user", required = true) String text) throws ServiceException {
		String username = getEndUser(); 
		Log.debug("postWebPush " + username + " " + target + "\n" + text);
		
		if (sendWebPushMessage(username, target, text)) {
			return Response.status(Response.Status.OK).build();	
		}
		
        return Response.status(Response.Status.BAD_REQUEST).build();
    }	

	@ApiOperation(tags = {"Web Push"}, value="Send a notification action", notes="This endpoint is used to post the user action of a web push notification")	
    @POST
    @Path("/webpush/action")
    public Response handleNotificationAction(@ApiParam(value = "A notification action", required = true)  NotificationActionEntity notificationAction) throws ServiceException {
		String username = getEndUser(); 
		//String payload = (new Gson()).toJson(notificationAction);		
		Log.debug("handleNotificationAction " + username + " " + notificationAction.getAction() + " " + notificationAction.getValue());		

		if ("webpush.message".equals(notificationAction.getAction())) 
		{			
			if (sendWebPushMessage(username, notificationAction.getData(), notificationAction.getValue())) {
				return Response.status(Response.Status.OK).build();	
			}
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();		
    }
	
	//-------------------------------------------------------
    //
    //  Authentication
    //
    //-------------------------------------------------------

	@ApiOperation(tags = {"Authentication"}, value="Login with Username/Password", notes="This endpoint is used to login a user with a username and password")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The authentication token", response = TokenEntity.class), @ApiResponse(code = 500, message = "Authentication failed")})	
    @POST
    @Path("/login/{username}")
    public TokenEntity loginUser(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("username") String username, @ApiParam(value = "The current Openfire password for the user", required = true) String password) throws ServiceException    {
		JSONObject json = new JSONObject();				
        Log.debug("registerUser " + username + "\n" + password);
			
        try {
			AuthFactory.authenticate(username, password);
			
			User user = SparkWeb.self.getUser(username);
			return new TokenEntity(SparkWeb.self.makeAccessToken(user));				

        } catch (Exception e) {
            Log.error("loginUser", e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }

	@ApiOperation(tags = {"Authentication"}, value="Logout user", notes="This end point is used to logout the authenticated user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The user was logged out"), @ApiResponse(code = 400, message = "The user not be logged out")})	
    @Path("/logout")
    @POST
    public Response logoutUser() throws ServiceException   {
		String username = getEndUser();	
        Log.debug("logoffUser " + username);

        try {
            OpenfireConnection connection = OpenfireConnection.removeConnection(username);

            if (connection == null) {
                throw new ServiceException("Exception", "xmpp connection not found", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
            }

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }

        return Response.status(Response.Status.OK).build();
    }
	
	@ApiOperation(tags = {"Authentication"}, value="Register a new user with username/password", notes="This endpoint is used to register a new user")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The authentication token", response = TokenEntity.class), @ApiResponse(code = 500, message = "Authentication failed")})	
    @POST
    @Path("/register/{username}")
    public TokenEntity registerUser(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("username") String username, @ApiParam(value = "The password for the user", required = true) String password) throws ServiceException    {
		JSONObject json = new JSONObject();				
        Log.debug("registerUser " + username);
			
        try {
 			User user = SparkWeb.self.getUser(username);	
			
			if (user == null) {	// create a new user if in-band reg is allowed
				IQRegisterHandler regHandler = XMPPServer.getInstance().getIQRegisterHandler();
				
				if (regHandler.isInbandRegEnabled()) {
					String name = username.substring(0, 1).toUpperCase() + username.substring(1);
					String email = username + "@" + XMPPServer.getInstance().getServerInfo().getXMPPDomain();
					user = SparkWeb.userManager.createUser( username, password, name, email);
					return new TokenEntity(SparkWeb.self.makeAccessToken(user));				
				} else {
					throw new ServiceException("Exception", "user cannot be created", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);					
				}
			} else {
				throw new ServiceException("Exception", "user exists", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);					
			}			

        } catch (Exception e) {
            Log.error("registerUser", e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }
	
	@ApiOperation(tags = {"Authentication"}, value="Start process to register a user for WebAuthn", notes="This endpoint is used to start the webauthn registration proces")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "WebAuthn data to registration"), @ApiResponse(code = 500, message = "Authentication failed")})	
    @POST
    @Path("/webauthn/register/start/{username}")
    public String webauthnRegisterStart(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("username") String username, @ApiParam(value = "The current Openfire password for the user", required = true) String password) throws ServiceException     {	
		JSONObject json = new JSONObject();	
		
        try {
 			User user = SparkWeb.self.getUser(username);	
			
			if (user == null) {	// create a new user if in-band reg is allowed
				IQRegisterHandler regHandler = XMPPServer.getInstance().getIQRegisterHandler();
				
				if (regHandler.isInbandRegEnabled()) {
					String name = username.substring(0, 1).toUpperCase() + username.substring(1);
					String email = username + "@" + XMPPServer.getInstance().getServerInfo().getXMPPDomain();
					user = SparkWeb.userManager.createUser( username, password, name, email);
				} else {
					throw new ServiceException("Exception", "user cannot be created", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);					
				}
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

	@ApiOperation(tags = {"Authentication"}, value="Finish process to register a user for WebAuthn", notes="This endpoint is used to finish the webauthn registration proces")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The authentication token", response = TokenEntity.class), @ApiResponse(code = 500, message = "Authentication failed")})	
    @POST
    @Path("/webauthn/register/finish/{username}")
    public TokenEntity webauthnRegisterFinish(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("username") String username, @ApiParam(value = "The credentials generated by the web client", required = true) String credentials) throws ServiceException    {
		JSONObject json = new JSONObject();				
        Log.debug("webauthnRegisterFinish " + username + "\n" + credentials);
			
        try {
			if (finishRegisterWebAuthn(credentials, username) != null) {
				User user = SparkWeb.self.getUser(username);
				return new TokenEntity(SparkWeb.self.makeAccessToken(user));
			}			

        } catch (Exception e) {
            Log.error("webauthnRegisterFinish", e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
        
       throw new ServiceException("Exception", "WebAuthn registration failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
    }	

	@ApiOperation(tags = {"Authentication"}, value="Start process to authenticate a user with WebAuthn", notes="This endpoint is used to start the webauthn authentication proces")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "WebAuthn data to start authentication"), @ApiResponse(code = 500, message = "Authentication failed")})	
    @POST
    @Path("/webauthn/authenticate/start/{username}")
    public String webauthnAuthenticateStart(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("username") String username) throws ServiceException  {		
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

	@ApiOperation(tags = {"Authentication"}, value="Finish process to authenticate a user with WebAuthn", notes="This endpoint is used to finish the webauthn authentication proces")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The authentication token", response = TokenEntity.class), @ApiResponse(code = 500, message = "Authentication failed")})	
    @POST
    @Path("/webauthn/authenticate/finish/{username}")
    public TokenEntity webauthnAuthenticateFinish(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("username") String username, @ApiParam(value = "The assertion generated by the web client", required = true) String assertion) throws ServiceException     {
		JSONObject json = new JSONObject();		
        Log.debug("webauthnAuthenticateFinish " + username + "\n" + assertion);
			
        try {
			if (finishAuthentication(assertion, username)) {
				User user = SparkWeb.self.getUser(username);
				return new TokenEntity(SparkWeb.self.makeAccessToken(user));							
			}			

        } catch (Exception e) {
            Log.error("webauthnAuthenticateFinish", e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
        
        throw new ServiceException("Exception", "WebAuthn authentication failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
    }
	
	@ApiOperation(tags = {"Authentication"}, value="Create a TOTP registration QR code", notes="This endpoint is used to obtain a QR code for time based one-time password (TOTP) two-factor (2FA) authentication")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "QR Code for TOTP registration", response = TOTPEntity.class), @ApiResponse(code = 500, message = "Request failed")})	
    @POST
    @Path("/totp/register/{username}")
    public TOTPEntity getTotpQrCode(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("username") String username, @ApiParam(value = "The current Openfire password for the user", required = true) String password) throws ServiceException     {	
        Log.debug("getTotpQrCode " + username);

        try {
			AuthFactory.authenticate(username, password);			
			
			User user = SparkWeb.self.getUser(username);
			String base32Secret = user.getProperties().get("totp.secret");

			if (base32Secret == null) {
				base32Secret = TimeBasedOneTimePasswordUtil.generateBase32Secret();
				user.getProperties().put("totp.secret", base32Secret);
			}
			return new TOTPEntity(TimeBasedOneTimePasswordUtil.qrImageUrl(username + "@" + XMPPServer.getInstance().getServerInfo().getXMPPDomain(), base32Secret));


        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
	}

	@ApiOperation(tags = {"Authentication"}, value="Authenticate a user with with a TOTP code", notes="This endpoint is used to authenticate a user with a time based one-time password (TOTP) for two-factor (2FA) authentication")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The authentication token", response = TokenEntity.class), @ApiResponse(code = 500, message = "Authentication failed")})	
    @POST
    @Path("/totp/authenticate/{username}/{code}")
    public TokenEntity authenticateTotpCode(@ApiParam(value = "A valid Openfire username", required = true) @PathParam("username") String username, @ApiParam(value = "A TOTP code", required = true) @PathParam("code") String code) throws ServiceException     {	
        Log.debug("getTotpQrCode " + username + " " + code);

        try {
			User user = SparkWeb.self.getUser(username);
			String base32Secret = user.getProperties().get("totp.secret");

			if (base32Secret == null) {
				throw new ServiceException("Exception", "user not registered for TOTP", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
			}
            
			String serverCode = TimeBasedOneTimePasswordUtil.generateCurrentNumberString(base32Secret);			

            if (serverCode.equals(code)) {
				return new TokenEntity(SparkWeb.self.makeAccessToken(user));					
			}

        } catch (Exception e) {
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
		
		throw new ServiceException("Exception", "TOTP authentication failed", ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);		
	}
	
	//-------------------------------------------------------
	//
	//	Utilities
	//
	//-------------------------------------------------------

	private boolean sendWebPushMessage(String from, String to, String message) {
		Log.debug("sendWebPushMessage " + from + " " + to + " " + message);
		
		try {
			User self = SparkWeb.self.getUser(from);
			User targetUser = SparkWeb.self.getUser(to);
			
			if (targetUser == null || self == null) {
				return false;
			}	

			NotificationEntity notification = new NotificationEntity();
			notification.setBody(message);
			notification.setData(from);
			notification.setSubject(self.getName());		
			
			List<NotificationActionEntity> actions = new ArrayList<>();
			actions.add(new NotificationActionEntity("webpush.message", "text", "Reply"));
			notification.setActions(actions);
			
			if (SparkWeb.self.postWebPush(targetUser, notification)) {
				return true;
			}
		} catch (Exception e) {
			Log.error("sendWebPushMessage " + e, e);
		}
		return false;
	}

    public Conversations getConversations(String keywords, String to, String start, String end, String room, String service) throws ServiceException   {
		String username = getEndUser();	 
		Log.debug("getConversations " + username + " " + keywords + " " + " " + to  + " " + start + " " + end + " " + room + " " + service);

        try {
            ArchiveSearch search = new ArchiveSearch();
            JID participant1JID = makeJid(username);
            JID participant2JID = null;

            if (to != null) participant2JID = makeJid(to);

            if (participant2JID != null) {
                search.setParticipants(participant1JID, participant2JID);
            }

            if (start != null) {
                Date startDate = null;

                try {
                    if (start.contains("T")) {
                        startDate = Date.from(Instant.parse(start));
                    }
                    else {
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
                        startDate = formatter.parse(start);
                    }
                    startDate = new Date(startDate.getTime() - JiveConstants.MINUTE * 5);
                    search.setDateRangeMin(startDate);
                }
                catch (Exception e) {
                    Log.error("ConversationPDFServlet", e);
                }
            }

            if (end != null) {
                Date endDate = null;

                try {
                    if (end.contains("T"))
                    {
                        endDate = Date.from(Instant.parse(end));
                    }
                    else {
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
                        endDate = formatter.parse(end);
                    }
                    endDate = new Date(endDate.getTime() + JiveConstants.DAY - 1);
                    search.setDateRangeMax(endDate);
                }
                catch (Exception e) {
                    Log.error("ConversationPDFServlet", e);
                }
            }

            if (keywords != null) search.setQueryString(keywords);
            if (service == null || "".equals(service)) service = "conference";

            if (room != null) {
                search.setRoom(new JID(room + "@" + service + "" + XMPPServer.getInstance().getServerInfo().getXMPPDomain()));
            }

            search.setSortOrder(ArchiveSearch.SortOrder.ascending);

            Collection<Conversation> conversations = new ArchiveSearcher().search(search);
            Collection<Conversation> list = new ArrayList<Conversation>();

            for (Conversation conversation : conversations) {
                list.add(conversation);
            }

            return new Conversations(list);

        } catch (Exception e) {
            Log.error("getConversations", e);
            throw new ServiceException("Exception", e.getMessage(), ExceptionType.ILLEGAL_ARGUMENT_EXCEPTION, Response.Status.BAD_REQUEST);
        }
    }
	
    private JID makeJid(String participant1) {
        JID participant1JID = null;

        try {
            int position = participant1.lastIndexOf("@");

            if (position > -1) {
                String node = participant1.substring(0, position);
                participant1JID = new JID(JID.escapeNode(node) + participant1.substring(position));
            } else {
                participant1JID = new JID(JID.escapeNode(participant1), XMPPServer.getInstance().getServerInfo().getXMPPDomain(), null);
            }
        } catch (Exception e) {
            Log.error("makeJid", e);
        }
        return participant1JID;
    }
	
    public String fetchWebPushPublicKey(String username)  {
        Log.debug("fetchWebPushPublicKey " + username);

        User user = SparkWeb.self.getUser(username);
        if (user == null) return null;

        String ofPublicKey = user.getProperties().get("vapid.public.key");
        String ofPrivateKey = user.getProperties().get("vapid.private.key");

        if (ofPublicKey == null || ofPrivateKey == null) {
            try {
                KeyPair keyPair = generateKeyPair();

                final ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
                final ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();

				byte[] encodedPublicKey = Utils.encode(ecPublicKey);
				byte[] encodedPrivateKey = Utils.encode(ecPrivateKey);
		
				ofPublicKey = java.util.Base64.getUrlEncoder().encodeToString(encodedPublicKey);
				ofPrivateKey = java.util.Base64.getUrlEncoder().encodeToString(encodedPrivateKey);

                user.getProperties().put("vapid.public.key", ofPublicKey);
                user.getProperties().put("vapid.private.key", ofPrivateKey);

            } catch (Exception e) {
                Log.error("fetchWebPushPublicKey", e);
            }
        }

        return ofPublicKey;
    }

    private KeyPair generateKeyPair() throws InvalidAlgorithmParameterException, java.security.NoSuchProviderException, NoSuchAlgorithmException {
        ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec("prime256v1");

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "BC");
        keyPairGenerator.initialize(parameterSpec);

        return keyPairGenerator.generateKeyPair();
    }
	
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
				Log.debug("found authorization " + auth);				

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
		
		for (org.jivesoftware.openfire.plugin.spark.UserProperty userProperty : bookmark.getProperties()) {
			String key = userProperty.getKey();								
			json.put(key, userProperty.getValue());
		}
		return json;
	}
}
