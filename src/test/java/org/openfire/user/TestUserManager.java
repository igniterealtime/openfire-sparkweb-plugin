package org.jivesoftware.openfire.user;

import org.jivesoftware.openfire.XMPPServer;

public class TestUserManager {

	public static UserManager getUserManager(XMPPServer xmppServer) {
		return new UserManager(xmppServer);
	}
}