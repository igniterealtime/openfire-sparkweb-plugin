let sparkWeb;

window.addEventListener('load', () => {
	console.debug("window.load");
	
	document.getElementById("login").addEventListener('click', async () => {
		sparkWeb = new SparkWeb(document.getElementById("host").value, "simple-app");		
		await sparkWeb.login();
		
		if (sparkWeb.token) {
			const source = new EventSource(sparkWeb.url + "/sparkweb/sse?token=" + sparkWeb.token);
			
			source.onerror = async event => {
				const online = await sparkWeb.isOnline();
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
				
				if (msg.type == "headline") {
					document.getElementById("status").innerHTML = "System Message - " + msg.body;				
				} else {
					document.getElementById("status").innerHTML = msg.type + " " + msg.from + " - " + msg.body;								
				}
				console.debug("EventSource - chatapi.chat", msg);	
			});				
		}
	})
})

window.addEventListener('beforeunload', () => {
	console.debug("window.beforeunload");
	if (sparkWeb) sparkWeb.logout();
})