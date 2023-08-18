
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
				var urlRegex = /(https?:\/\/[^\s]+)/g;
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
			const username = getUrlParam("username", "Username");
			
			if (username) {
				try {
					const token = await webAuthenticate(username);	
					startSse(url, token);
				} catch (e) {
					console.error("web authenticate fails", e);	

					if (confirm("Create and register new credentials for " + username + "?")) {
						const password = getUrlParam("password", "Password");	
						const token = await webRegister(username, password);
						startSse(url, token);	
					}						
				}
			}			
		},	
		layout: "StandaloneLayout"
	});

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
  
	function startSse(url, token) {
		window.ui.preauthorizeApiKey("authorization", token);		
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
		});	
		
		source.addEventListener('chatapi.chat', async event => {
			const msg = JSON.parse(event.data);	
			if (msg.type == "headline") document.getElementById("status").innerHTML = "System Message - " + msg.body;				
			console.debug("EventSource - chatapi.chat", msg);	
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
