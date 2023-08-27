window.addEventListener('online', function() {
	console.debug("window.online");
	
	function restartApp() {
		if (!isOnline()) {
			setTimeout(restartApp, 1000);		
			return;
		}
		
		window.location.reload();	
	}	
	restartApp();
})

window.addEventListener('offline', function() {
	console.debug("window.offline");
})

window.onload = function() {
	console.debug("window.onload");

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
	window.ui = SwaggerUIBundle({
		url: location.protocol + "//" + location.host + "/sparkweb/api/swagger.json",
		dom_id: '#swagger-ui',
		deepLinking: true,
		presets: [
		  SwaggerUIBundle.presets.apis,
		  SwaggerUIStandalonePreset
		],
		plugins: [
		  SwaggerUIBundle.plugins.DownloadUrl
		],
		onComplete: async function() {
			const urlify = (text) => {
				const urlRegex = /(https?:\/\/[^\s]+)/g;
				return text.replace(urlRegex, '<a target="_blank" href="$1">$1</a>');
			}

			const getUrlParam = (name, label)	=> {
				let value = null;
				let results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
				
				if (results) {
					value = unescape(results[1] || undefined);
					console.debug("urlParam get", name, value);	
				}
	
				if (!value) {
					value = sessionStorage.getItem("sparkweb.config." + name);
				}	

				if (!value) {
					value = prompt("Enter " + label);
				}			
					
				if (value) {
					sessionStorage.setItem("sparkweb.config." + name, value);
				}

				console.debug("getUrlParam", value);				
				return value;
			}		
			const url = location.protocol + "//" + location.host;

			let username = sessionStorage.getItem("sparkweb.config.username");
			let password = sessionStorage.getItem("sparkweb.config.password");
			
			if (!username) {
				let credentials = await navigator.credentials.get({password: true});
				console.debug("web credentials get", credentials);	
				
				if (credentials?.id) {
					username = credentials.id.split("@")[0];
				} else {
					username = getUrlParam("username", "Username");
				}
			}
			
			if (username) {
				try {
					const token = await webAuthenticate(username);	
					setupListeners(username, url, token);
				} catch (e) {
					console.error("web authenticate fails", e);	

					if (confirm("Create and register new credentials for " + username + "?")) {
						
						if (credentials?.password) {
							password = credentials.password;
						} else {
							password = getUrlParam("password", "Password");
							credentials = await navigator.credentials.create({password: {id: username, password: password}});
							await navigator.credentials.store(credentials);							
						}
							
						const token = await webRegister(username, password);
						setupListeners(username, url, token);	
					}						
				}
			}			
		},	
		layout: "StandaloneLayout"
	});
  
	function setupListeners(username, url, token) {
		window.ui.preauthorizeApiKey("authorization", token);
		let serviceWorkerRegistration;

		const initialiseError = (error) => {
			console.error("setupServiceWorker - initialiseError", error);
		}	

		const initialiseState = (registration) => {
			if (!('showNotification' in ServiceWorkerRegistration.prototype)) {
				console.warn('Notifications aren\'t supported.');
				return;
			}

			if (Notification.permission === 'denied') {
				console.warn('The user has blocked notifications.');
				return;
			}

			if (!('PushManager' in window)) {
				console.warn('Push messaging isn\'t supported.');		
				return;
			}

			console.debug("setupServiceWorker - initialiseState", registration);
			setupPushNotifications(url, token, registration);				
		}
	
		const source = new EventSource(url + "/sparkweb/sse?token=" + token);
		
		source.onerror = async event => {
			const online = await isOnline();
			if (online) location.reload();
			console.debug("EventSource - onError", online, event);				
		};

		source.addEventListener('onConnect', async event => {
			const msg = JSON.parse(event.data);				
			document.getElementById("status").innerHTML = "User " + msg.username + " (" + msg.name + ") Signed In";				
			console.debug("EventSource - onConnect", msg);

			navigator.serviceWorker.register('./sparkweb-sw.js', {scope: '.'}).then(initialiseState, initialiseError);	
			
		});	
		
		source.addEventListener('chatapi.chat', async event => {
			const msg = JSON.parse(event.data);	
			
			if (msg.type == "headline") {
				document.getElementById("status").innerHTML = "System Message - " + msg.body;				
			} else {
				document.getElementById("status").innerHTML = msg.type + " " + msg.from + " - " + msg.body;								
			}
			console.debug("EventSource - chatapi.chat", msg);	
		});	

		source.addEventListener('chatapi.muc', async event => {
			const msg = JSON.parse(event.data);	
			document.getElementById("status").innerHTML = msg.type + " " + msg.from + " - " + msg.body;				
			console.debug("EventSource - chatapi.muc", msg);	
		});	

		source.addEventListener('chatapi.xmpp', async event => {
			const msg = JSON.parse(event.data);	
			console.debug("EventSource - chatapi.xmpp", msg, atob(msg.xmpp));	
		});	
		
		source.addEventListener('pubsub.item', async event => {
			const msg = JSON.parse(event.data);	
			console.debug("EventSource - pubsub.item", msg);	
		});		

		const actionChannel = new BroadcastChannel('sparkweb.notification.action');
		
		actionChannel.addEventListener('message', event => {
			console.debug("setupListeners - notication action", event.data);

		});	

		window.addEventListener('beforeunload', function() {
			console.debug("setupListeners - before unload", url, token);
			fetch(url + "/sparkweb/api/rest/logout", {method: "POST", headers: {"Authorization": token}});
		});	
	}
	
	function setupPushNotifications(baseUrl, token, registration) {		
		const url = baseUrl + "/sparkweb/api/rest/webpush/vapidkey";	
		const options = {method: "GET", headers: {"Authorization": token, "Accept":"application/json", "Content-Type":"application/json"}};

		console.debug("setupPushNotifications - vapidGetPublicKey", url, options);
			
		fetch(url, options).then(function(response) {
			if (response.status < 400) {
				return response.json()
			}	
		}).then(function(vapid) {
			if (vapid?.publicKey) {
				subscribeForPushNotifications(baseUrl, vapid.publicKey, token, registration);
			}
		}).catch(function (err) {
			console.error('vapidGetPublicKey error!', err);
		});	
	}

	function subscribeForPushNotifications(baseUrl, publicKey, token, registration) {
		console.debug("subscribeForPushNotifications", baseUrl, publicKey, token);	

		registration.pushManager.getSubscription().then(function (subscription) {
			if (subscription) {
			  subscription.unsubscribe().then((successful) => {
				registerSubscription(baseUrl, publicKey, token, registration);
			  })
			  .catch((e) => {
				console.error('Unable to unsubscribe to push.', e);
				registerSubscription(baseUrl, publicKey, token, registration);				
			  });
			} else {
				registerSubscription(baseUrl, publicKey, token, registration);				
			}
		});  			
	}
	
	function registerSubscription(baseUrl, publicKey, token, registration) {
		console.debug("registerSubscription", baseUrl, publicKey, token);	
		
		registration.pushManager.subscribe({
			userVisibleOnly: true,
			applicationServerKey: base64UrlToUint8Array(publicKey)
		})
		.then(function (subscription) {
			return sendSubscriptionToServer(baseUrl, subscription, token);
		})
		.catch(function (e) {
			if (Notification.permission === 'denied') {
				console.warn('Permission for Notifications was denied');
			} else {
				console.error('Unable to subscribe to push.', e);
			}
		});		
	}

	function base64UrlToUint8Array(base64UrlData) {
		const padding = '='.repeat((4 - base64UrlData.length % 4) % 4);
		const base64 = (base64UrlData + padding).replace(/\-/g, '+').replace(/_/g, '/');
		const rawData = atob(base64);
		const buffer = new Uint8Array(rawData.length);

		for (let i = 0; i < rawData.length; ++i) {
			buffer[i] = rawData.charCodeAt(i);
		}

		return buffer;
	}	
	
	function sendSubscriptionToServer(baseUrl, subscription, token) {
		console.debug("sendSubscriptionToServer", subscription);

		const key = subscription.getKey ? subscription.getKey('p256dh') : '';
		const auth = subscription.getKey ? subscription.getKey('auth') : '';

		const subscriptionString = JSON.stringify(subscription);  // TODO

		console.debug("web push subscription", {
			endpoint: subscription.endpoint,
			key: key ? btoa(String.fromCharCode.apply(null, new Uint8Array(key))) : '',
			auth: auth ? btoa(String.fromCharCode.apply(null, new Uint8Array(auth))) : ''
		}, subscription);

		const resource = "sparkweb-swagger";
		const url = baseUrl + "/sparkweb/api/rest/webpush/subscribe/" + resource;
		const options = {method: "PUT", body: JSON.stringify(subscription), headers: {"Authorization": token, "Accept":"application/json", "Content-Type":"application/json"}};

		return fetch(url, options).then(function(response) {
			console.debug("sendSubscriptionToServer - subscribe response", response);

		}).catch(function (err) {
			console.error('subscribe error!', err);
		});
	}	
  
	async function webAuthenticate(username) {
		authorization = sessionStorage.getItem("sparkweb.config.token");
		console.debug("webAuthn step 0", username, authorization);		
		const url = location.protocol + '//' + location.host + "/sparkweb/api/rest/webauthn";	
		const response = await fetch(url + "/authenticate/start/" + username, {method: "POST", headers: {authorization}});
		const json =  await response.json();
		
		if (json.token) {
			sessionStorage.setItem("sparkweb.config.token", json.token);			
			return json.token;
			
		} else {		
			const params =  json.assertion.publicKey;

			console.debug("webAuthn step 1", params);
			
			if (params.allowCredentials.length > 0) 
			{							
				params.allowCredentials.forEach(listItem =>  {
					listItem.id = bufferDecode(listItem.id)
				});
			}
			
			console.debug("webAuthn step 2", params);	
			
			params.challenge = bufferDecode(params.challenge);						
			const assertion = await navigator.credentials.get({publicKey: params});	
			console.debug("webAuthn step 3", assertion, assertion.id, assertion.type);	
			
			const credential = {};
			credential.id =     assertion.id;
			credential.type =   assertion.type;
			credential.rawId =  bufferEncode(assertion.rawId);

			if (assertion.response) {
				const clientDataJSON = bufferEncode(assertion.response.clientDataJSON);
				const authenticatorData = bufferEncode(assertion.response.authenticatorData);
				const signature = bufferEncode(assertion.response.signature);
				const userHandle = bufferEncode(assertion.response.userHandle);
				credential.response = {clientDataJSON, authenticatorData,	signature, userHandle};
				if (!credential.clientExtensionResults) credential.clientExtensionResults = {};						  
			}
			console.debug("webAuthn step 4", credential);
			const response2 = await fetch(url + "/authenticate/finish/" + username, {method: "POST", headers: {authorization}, body: JSON.stringify(credential)});		
			const json2 =  await response2.json();
			sessionStorage.setItem("sparkweb.config.token", json2.token);			
			return json2.token;
		}
	}

	async function webRegister(username, password) {
		console.debug("webRegister step 0", username, password);
		const url = location.protocol + '//' + location.host + "/sparkweb/api/rest/webauthn";	
		const response = await fetch(url + "/register/start/" + username, {method: "POST", body: password});	
		const json =  await response.json();
		const credentialCreationOptions =  json.credentials.publicKey;

		console.debug("webRegister step 1", credentialCreationOptions);
		
		if (credentialCreationOptions.excludeCredentials) 
		{
			credentialCreationOptions.excludeCredentials.forEach(listItem => 
			{
				listItem.id = bufferDecode(listItem.id)
			});
		}
		
		credentialCreationOptions.challenge = bufferDecode(credentialCreationOptions.challenge);
		credentialCreationOptions.user.id = bufferDecode(credentialCreationOptions.user.id);
		const cred = await navigator.credentials.create({publicKey: credentialCreationOptions});	
		console.debug("webRegister step 2", cred);
		
		const credential = {};
		credential.id =     cred.id;
		credential.rawId =  bufferEncode(cred.rawId);
		credential.type =   cred.type;

		if (cred.response) {
		  const clientDataJSON = bufferEncode(cred.response.clientDataJSON);
		  const attestationObject = bufferEncode(cred.response.attestationObject);
		  credential.response = {clientDataJSON, attestationObject};
		  if (!credential.clientExtensionResults) credential.clientExtensionResults = {};
		}

		console.debug("webRegister step 3", credential);		
		const response2 = await fetch(url + "/register/finish/" + username, {method: "POST", body: JSON.stringify(credential)});	
		const json2 =  await response2.json();	
		console.debug("webRegister step 4", json2);
		sessionStorage.setItem("sparkweb.config.token", json2.token);			
		return json2.token;	
	}

	function bufferDecode(e) {
		const t = "==".slice(0, (4 - e.length % 4) % 4),
			n = e.replace(/-/g, "+").replace(/_/g, "/") + t,
			r = atob(n),
			o = new ArrayBuffer(r.length),
			c = new Uint8Array(o);
		for (let e = 0; e < r.length; e++) c[e] = r.charCodeAt(e);
		return o;
	}

	function bufferEncode(e) {
		const t = new Uint8Array(e);
		let n = "";
		for (const e of t) n += String.fromCharCode(e);
		return btoa(n).replace(/\+/g, "-").replace(/\//g, "_").replace(/=/g, "")
	}  
};


async function isOnline() {
	try {
		if (!self.navigator.onLine) //false is always reliable that no network. true might lie
			return false;

		const url = location.protocol + "//" + location.host;					
		const request = new URL(url);
		request.searchParams.set('rand', Date.now().toString()); // random value to prevent cached responses
		const response = await fetch(request.toString(), { method: 'HEAD' });
		return response.ok;
	} catch {
		return false;
	}
}
