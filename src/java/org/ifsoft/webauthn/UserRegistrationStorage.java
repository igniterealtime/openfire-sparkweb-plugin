package org.ifsoft.webauthn;

import org.ifsoft.openfire.SparkWebAPI;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.util.JiveGlobals;

import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.sf.json.*;
import org.jivesoftware.openfire.plugin.rest.dao.PropertyDAO;

public class UserRegistrationStorage implements CredentialRepository {

  private Logger Log = LogManager.getLogger(UserRegistrationStorage.class);
  private UserManager userManager = XMPPServer.getInstance().getUserManager(); 

  public void storeSignatureCount(String username, long count) {
	Map<String, String> properties = getUserProperties(username);
	properties.put("webauthn-signature-count", String.valueOf(count));	
  }
  
  
  public void removeCredential(String username) {
	Map<String, String> properties = getUserProperties(username);

	if (properties != null) {	  
		properties.remove("webauthn-userid");

		for (String key : properties.keySet())
		{
			if (key.startsWith("webauthn-key-")) {		
				properties.remove(key);			
			}
		}
	}
  }
  
  public void addCredential(String username, byte[] id, byte[] credentialId, byte[] publicKeyCose, long signatureCount) {
	Log.debug("addCredential " + username);	  
	Map<String, String> properties = getUserProperties(username);

	if (properties != null) {
		JSONObject json = new JSONObject();
		String keyId = (new ByteArray(credentialId)).getBase64Url();
		String userId = BytesUtil.bytesToString(id);
		json.put("userId", userId);
		json.put("credentialId", keyId);		
		json.put("publicKeyCose", (new ByteArray(publicKeyCose)).getBase64Url());
		
		properties.put("webauthn-key-" + keyId, json.toString());	
		properties.put("webauthn-userid", userId);	
		properties.put("webauthn-signature-count", String.valueOf(signatureCount));			
	}
  }  

  @Override
  public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
	Log.debug("getCredentialIdsForUsername " + username);	  	  
    Set<PublicKeyCredentialDescriptor> result = new HashSet<>();	
	Map<String, String> properties = getUserProperties(username);

	if (properties != null)
	{
		for (String key : properties.keySet())
		{
			if (key.startsWith("webauthn-key-")) {
				try {
					JSONObject json = new JSONObject(properties.get(key));
					Log.debug("getCredentialIdsForUsername - credential " + key + "\n" + json);					
					ByteArray credentialId = ByteArray.fromBase64Url(json.getString("credentialId"));
					PublicKeyCredentialDescriptor descriptor = PublicKeyCredentialDescriptor.builder().id(credentialId).build();
					result.add(descriptor);	
				}
				catch (Exception e) {
					Log.warn( "getCredentialIdsForUsername", e );
				}				
			}
		}
	}	
    return result;	
  }

  @Override
  public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
    String userId = BytesUtil.bytesToString(userHandle.getBytes());	  
	Log.debug("getUsernameForUserHandle " + userId);	

	try {	
		List<String> usernames = PropertyDAO.getUsernameByProperty("webauthn-userid", userId);	
		
		if (usernames.size() > 0)
		{
			for (String username : usernames) {
				return Optional.of(username);
			}
		}	
	}
	catch (Exception e) {
		Log.warn( "getUsernameForUserHandle", e );
	}		
    return Optional.empty();
  }

  @Override
  public Optional<ByteArray> getUserHandleForUsername(String username) {
	Log.debug("getUserHandleForUsername " + username);	  
	Map<String, String> properties = getUserProperties(username);

	if (properties != null)
	{
		for (String key : properties.keySet())
		{
			if (key.startsWith("webauthn-key-"))
			{
				try {
					JSONObject json = new JSONObject(properties.get(key));
					String id = json.getString("userId");
					return Optional.of(new ByteArray(BytesUtil.stringToBytes(id)));	
				}
				catch (Exception e) {
					Log.warn( "getUserHandleForUsername", e );
				}				
			}
		}
	}			  
    return Optional.empty();
  }


  @Override
  public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
    String userId = BytesUtil.bytesToString(userHandle.getBytes());	 
	Log.debug("lookup " + credentialId + " " + userHandle);	  
	
	String key = "webauthn-key-" + credentialId.getBase64Url();	
	Log.debug("lookup " + userId + " " + key);
	
	try {	
		List<String> usernames = PropertyDAO.getUsernameByProperty(key, null);	
		
		if (usernames.size() > 0)
		{
			for (String username : usernames) {
				Log.debug("lookup user " + username);				
				Map<String, String> properties = getUserProperties(username);

				if (properties != null)
				{
					JSONObject json = new JSONObject(properties.get(key));	
					String userId2 = json.getString("userId");
					Log.debug("lookup user webauthn\n" + json);						
					
					if (userId2.equals(userId)) {						
						long signatureCount = Long.parseLong(properties.get("webauthn-signature-count"));
						return Optional.of(RegisteredCredential.builder()
						  .credentialId(credentialId)
						  .userHandle(userHandle)
						  .publicKeyCose(ByteArray.fromBase64Url(json.getString("publicKeyCose")))
						  .signatureCount(signatureCount).build());
					}
				}
			}
		}	
	}
	catch (Exception e) {
		Log.warn( "getUsernameForUserHandle", e );
	}		
    return Optional.empty();	    
  }

  @Override
  public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
	Log.debug("lookupAll " + credentialId);		  
    Set<RegisteredCredential> result = new HashSet<>();
	String key = "webauthn-key-" + credentialId.getBase64Url();	
	
	try {	
		List<String> usernames = PropertyDAO.getUsernameByProperty(key, null);	
		
		if (usernames.size() > 0)
		{
			for (String username : usernames) {
				Map<String, String> properties = getUserProperties(username);

				if (properties != null) {
					long signatureCount = Long.parseLong(properties.get("webauthn-signature-count"));
					JSONObject json = new JSONObject(properties.get(key));	
					String userId = json.getString("userId");
					ByteArray id = new ByteArray(BytesUtil.stringToBytes(userId));
					ByteArray keyId = ByteArray.fromBase64Url(json.getString("credentialId"));
					ByteArray publicKeyCose = ByteArray.fromBase64Url(json.getString("publicKeyCose"));					
					
					result.add(RegisteredCredential.builder()
					  .credentialId(keyId)
					  .userHandle(id)
					  .publicKeyCose(publicKeyCose)
					  .signatureCount(signatureCount).build());
				}
			}
		}	
	}
	catch (Exception e) {
		Log.warn( "getUsernameForUserHandle", e );
	}
	return result;
  }
  
  private Map<String, String> getUserProperties(String username)  {	  
	try {
		User user = userManager.getUser(username);
		return user.getProperties();
	}
	catch (Exception e) {
		Log.warn( "user not found " + username, e );
		return null;
	}	  
  }
}
