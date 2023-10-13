let sparkWeb;

window.addEventListener('load', () => {
	console.debug("window.load");
	sparkWeb = new SparkWeb("http://localhost:7070", "simple-app");
	
	document.getElementById("login").addEventListener('click', async () => {
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
		}
	})
})

window.addEventListener('beforeunload', () => {
	console.debug("window.beforeunload");
	if (sparkWeb) sparkWeb.logout();
})