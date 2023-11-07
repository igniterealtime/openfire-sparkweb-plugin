## SparkWeb 
Modern web client services (REST, SSE JWT and WebAuthn) to Openfire/XMPP.
 
## CI Build Status

[![Build Status](https://github.com/igniterealtime/openfire-sparkweb-plugin/workflows/Java%20CI/badge.svg)](https://github.com/igniterealtime/openfire-sparkweb-plugin/actions)

## Introduction
This plugin adds support for a whole range of modern web service connections to Openfire/XMPP via the embedded Jetty web server in a different way to the traditional persistent client XMPP session over TCP/5222 or Bosh/7443 or Websockets/7443 used by native binary clients.

It uses :

- [Web Authentication, Web Credentials and Time based one time passwords (TOTP) with JWTs (JSON Web Tokens)](https://github.com/igniterealtime/openfire-sparkweb-plugin/blob/main/authenticate.md) in addition to traditional username/passwords for multiple factor authentication on user requests and apply the considered user permissions and entitlements for the request.
- [REST APIs](https://github.com/igniterealtime/openfire-sparkweb-plugin/blob/main/api.md) to make requests and receive immediate responses.
- [Server Sent Events](https://github.com/igniterealtime/openfire-sparkweb-plugin/blob/main/sse.md) (SSE) and
- [Web-Push](https://github.com/igniterealtime/openfire-sparkweb-plugin/blob/main/webpush.md) to push events to users when they are online or offline.

A user has a singleton xmpp session in Openfire that is created on demand and removed when it expires. This single user session can have many active REST and SSE connections depending on how many browsers tabs, browser windows or browser instances are connected to Openfire from applications in web pages opened on behalf of the user.

The xmpp session has the full feature set of an XMPP client that is based on Smack/Spark. It also has User Interface (UI) consisting of web-components based on Converse that can bind directly to Spark features. For example, a contacts roster widget and a chat conversation widget. that work independent of each other and can be hosted in different web pages or different browsers but end up pointing at the same xmpp session.

Here is a [simple example](classes/apps/examples/login.html) to create an xmpp session token with standard username and password. The token is used to create an EventSource which can receive a chat message.

````
<html lang="en">
<head>	
  <script>
	let token;
	
	window.addEventListener('load', async () => {
		console.debug("window.load"); 
		
		document.getElementById("login").addEventListener('click', async () => {
			const url = document.getElementById("url").value;
			const username = document.getElementById("username").value;
			const body = document.getElementById("password").value;
			
			const response = await fetch(url + "/sparkweb/api/rest/login/" + username, {method: "POST", body});
			const json =  await response.json();		
			token = json.token;	

			if (token) {
				const status = document.getElementById("status");
				const source = new EventSource(url + "/sparkweb/sse?token=" + token);
				
				source.onerror = async event => {
					console.debug("EventSource - onError", event);				
				};

				source.addEventListener('onConnect', async event => {
					const msg = JSON.parse(event.data);				
					status.innerHTML = "User " + msg.username + " (" + msg.name + ") Signed In";				
					console.debug("EventSource - onConnect", msg);				
				});	

				source.addEventListener('chatapi.chat', async event => {
					const msg = JSON.parse(event.data);	
					
					if (msg.type == "headline") {
						status.innerHTML = "System Message - " + msg.body;				
					} else {
						status.innerHTML = msg.type + " " + msg.from + " - " + msg.body;								
					}
					console.debug("EventSource - chatapi.chat", msg);	
				});				
			}			
		});		
		
	});
	
	window.addEventListener('beforeunload', async () => {
		console.debug("window.beforeunload");
		if (token) fetch(document.getElementById("url").value + "/sparkweb/api/rest/logout", {method: "POST", headers: {"Authorization": token}});
	})			
  </script>
</head>

<body>
	<div style="margin: 10px;font-family: monospace;font-size: 16px;" id="status">Inactive</div> 
	<input id="url" type="text" value="https://localhost:7443" /><br/>
	<input id="username" type="text" value="admin" /><br/>
	<input id="password" type="text" value="admin" /><br/>	
	<button id="login">Login</button>
</body>
</html>	
````
