class SparkWeb 
{
	constructor(url, name) {
		console.debug("Sparkweb constructor", url, name);
		this.url = url;
		this.name = name;
	}
	
	async login() {
		console.debug("Sparkweb login");
		
		let credentials;
			
		if (!this.username || !this.password) {
			credentials = await navigator.credentials.get({password: true});

			if (credentials?.id && credentials?.password) {
				this.username = credentials.id.split("@")[0];
				this.password = credentials.password;				
			} else {
				this.username = prompt("Username:");
				this.password = prompt("Password:");				
			}				
		}
		
		if (this.username && this.password) {			
			const response = await fetch(this.url + "/sparkweb/api/rest/login/" + this.username, {method: "POST", body: this.password});
			const json =  await response.json();		
			this.token = json.token;
			
			if (this.token && !credentials) {			
				credentials = await navigator.credentials.create({password: {id: this.username, password: this.password}});
				await navigator.credentials.store(credentials);				
			}
		}
	};	
	
	logout() {
		console.debug("Sparkweb logout");
		
		if (this.token) {
			fetch(this.url + "/sparkweb/api/rest/logout", {method: "POST", headers: {"Authorization": this.token}});
		}
	};		
	
	async webAuthn() {
		let credentials = await navigator.credentials.get({password: true});
		console.debug("Sparkweb webAuthn", credentials);	
		
		if (credentials?.id) {
			this.username = credentials.id.split("@")[0];
		}
		
		if (this.username) {
			try {
				this.token = await _webAuthenticate();	
				this._setupServiceWorker();
				
			} catch (e) {
				console.error("web authenticate fails", e);	
					
				if (credentials?.password) {
					this.password = credentials.password;
				}
					
				if (this.password) {
					this.token = await _webRegister();
					this._setupServiceWorker();							
				}						
			}
		
		} else login();				
	} 
	
	_setupServiceWorker() {
		console.debug("Sparkweb _setupServiceWorker");			
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
			
			this.registration = registration;
			console.debug("setupServiceWorker - initialiseState", this.registration);
			this._setupPushNotifications();				
		}

		navigator.serviceWorker.register('./sparkweb-sw.js', {scope: '.'}).then(initialiseState, initialiseError);	
	}
	
	_setupPushNotifications() {		
		const url = this.url + "/sparkweb/api/rest/webpush/vapidkey";	
		const options = {method: "GET", headers: {"Authorization": this.token, "Accept":"application/json", "Content-Type":"application/json"}};

		console.debug("_setupPushNotifications - vapidGetPublicKey", url, options);
			
		fetch(url, options).then(function(response) {
			if (response.status < 400) {
				return response.json()
			}	
		}).then(function(vapid) {
			if (vapid?.publicKey) {
				this._subscribeForPushNotifications(vapid.publicKey);
			}
		}).catch(function (err) {
			console.error('vapidGetPublicKey error!', err);
		});	
	}

	_subscribeForPushNotifications(publicKey) {
		console.debug("subscribeForPushNotifications", publicKey);	

		registration.pushManager.getSubscription().then(function (subscription) {
			if (subscription) {
			  subscription.unsubscribe().then((successful) => {
				this._registerSubscription(publicKey);
			  })
			  .catch((e) => {
				console.error('Unable to unsubscribe to push.', e);
				this._registerSubscription(publicKey);				
			  });
			} else {
				this._registerSubscription(publicKey);				
			}
		});  			
	}
	
	_registerSubscription(publicKey) {
		console.debug("registerSubscription", publicKey, this.token);	
		
		registration.pushManager.subscribe({
			userVisibleOnly: true,
			applicationServerKey: this._base64UrlToUint8Array(publicKey)
		})
		.then(function (subscription) {
			return this._sendSubscriptionToServer(subscription);
		})
		.catch(function (e) {
			if (Notification.permission === 'denied') {
				console.warn('Permission for Notifications was denied');
			} else {
				console.error('Unable to subscribe to push.', e);
			}
		});		
	}

	_base64UrlToUint8Array(base64UrlData) {
		const padding = '='.repeat((4 - base64UrlData.length % 4) % 4);
		const base64 = (base64UrlData + padding).replace(/\-/g, '+').replace(/_/g, '/');
		const rawData = atob(base64);
		const buffer = new Uint8Array(rawData.length);

		for (let i = 0; i < rawData.length; ++i) {
			buffer[i] = rawData.charCodeAt(i);
		}

		return buffer;
	}	
	
	_sendSubscriptionToServer(subscription) {
		console.debug("sendSubscriptionToServer", subscription);

		const key = subscription.getKey ? subscription.getKey('p256dh') : '';
		const auth = subscription.getKey ? subscription.getKey('auth') : '';

		const subscriptionString = JSON.stringify(subscription);  // TODO

		console.debug("web push subscription", {
			endpoint: subscription.endpoint,
			key: key ? btoa(String.fromCharCode.apply(null, new Uint8Array(key))) : '',
			auth: auth ? btoa(String.fromCharCode.apply(null, new Uint8Array(auth))) : ''
		}, subscription);

		const url = this.url + "/sparkweb/api/rest/webpush/subscribe/" + this.name;
		const options = {method: "PUT", body: JSON.stringify(subscription), headers: {"Authorization": this.token, "Accept":"application/json", "Content-Type":"application/json"}};

		return fetch(url, options).then(function(response) {
			console.debug("sendSubscriptionToServer - subscribe response", response);

		}).catch(function (err) {
			console.error('subscribe error!', err);
		});
	}		
	

	async _webAuthenticate() {
		authorization = sessionStorage.getItem("sparkweb.config.token");
		console.debug("webAuthn step 0", this.username, authorization);	
		
		const url = this.url + "/sparkweb/api/rest/webauthn";	
		const response = await fetch(url + "/authenticate/start/" + this.username, {method: "POST", headers: {authorization}});
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
					listItem.id = this._bufferDecode(listItem.id)
				});
			}
			
			console.debug("webAuthn step 2", params);	
			
			params.challenge = this._bufferDecode(params.challenge);						
			const assertion = await navigator.credentials.get({publicKey: params});	
			console.debug("webAuthn step 3", assertion, assertion.id, assertion.type);	
			
			const credential = {};
			credential.id =     assertion.id;
			credential.type =   assertion.type;
			credential.rawId =  this._bufferEncode(assertion.rawId);

			if (assertion.response) {
				const clientDataJSON = this._bufferEncode(assertion.response.clientDataJSON);
				const authenticatorData = this._bufferEncode(assertion.response.authenticatorData);
				const signature = this._bufferEncode(assertion.response.signature);
				const userHandle = this._bufferEncode(assertion.response.userHandle);
				credential.response = {clientDataJSON, authenticatorData,	signature, userHandle};
				if (!credential.clientExtensionResults) credential.clientExtensionResults = {};						  
			}
			console.debug("webAuthn step 4", credential);
			const response2 = await fetch(url + "/authenticate/finish/" + this.username, {method: "POST", headers: {authorization}, body: JSON.stringify(credential)});		
			const json2 =  await response2.json();
			sessionStorage.setItem("sparkweb.config.token", json2.token);			
			return json2.token;
		}
	}

	async _webRegister() {
		console.debug("webRegister step 0", this.username, this.password);
		const url = this.url + "/sparkweb/api/rest/webauthn";	
		const response = await fetch(url + "/register/start/" + this.username, {method: "POST", body: this.password});	
		const json =  await response.json();
		const credentialCreationOptions =  json.credentials.publicKey;

		console.debug("webRegister step 1", credentialCreationOptions);
		
		if (credentialCreationOptions.excludeCredentials) 
		{
			credentialCreationOptions.excludeCredentials.forEach(listItem => 
			{
				listItem.id = this._bufferDecode(listItem.id)
			});
		}
		
		credentialCreationOptions.challenge = this._bufferDecode(credentialCreationOptions.challenge);
		credentialCreationOptions.user.id = this._bufferDecode(credentialCreationOptions.user.id);
		const cred = await navigator.credentials.create({publicKey: credentialCreationOptions});	
		console.debug("webRegister step 2", cred);
		
		const credential = {};
		credential.id =     cred.id;
		credential.rawId =  this._bufferEncode(cred.rawId);
		credential.type =   cred.type;

		if (cred.response) {
		  const clientDataJSON = this._bufferEncode(cred.response.clientDataJSON);
		  const attestationObject = this._bufferEncode(cred.response.attestationObject);
		  credential.response = {clientDataJSON, attestationObject};
		  if (!credential.clientExtensionResults) credential.clientExtensionResults = {};
		}

		console.debug("webRegister step 3", credential);		
		const response2 = await fetch(url + "/register/finish/" + this.username, {method: "POST", body: JSON.stringify(credential)});	
		const json2 =  await response2.json();	
		console.debug("webRegister step 4", json2);
		sessionStorage.setItem("sparkweb.config.token", json2.token);			
		return json2.token;	
	}

	_bufferDecode(e) {
		const t = "==".slice(0, (4 - e.length % 4) % 4),
			n = e.replace(/-/g, "+").replace(/_/g, "/") + t,
			r = atob(n),
			o = new ArrayBuffer(r.length),
			c = new Uint8Array(o);
		for (let e = 0; e < r.length; e++) c[e] = r.charCodeAt(e);
		return o;
	}

	_bufferEncode(e) {
		const t = new Uint8Array(e);
		let n = "";
		for (const e of t) n += String.fromCharCode(e);
		return btoa(n).replace(/\+/g, "-").replace(/\//g, "_").replace(/=/g, "");
	}	
	
	async isOnline() {
		try {
			if (!self.navigator.onLine) //false is always reliable that no network. true might lie
				return false;
				
			const request = new URL(this.url);
			request.searchParams.set('rand', Date.now().toString()); // random value to prevent cached responses
			const response = await fetch(request.toString(), { method: 'HEAD' });
			return response.ok;
		} catch {
			return false;
		}
	}	
}