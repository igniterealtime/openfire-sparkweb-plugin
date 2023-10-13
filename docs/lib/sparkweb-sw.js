// -------------------------------------------------------
//
//  General services worker listeners
//
// -------------------------------------------------------

self.addEventListener('install', function(event) {
    console.debug('install', event);

	var indexPage = new Request('index.html');
	event.waitUntil(
		fetch(indexPage).then(function(response) {
			return caches.open('offline').then(function(cache) {
				return cache.put(indexPage, response);
			});
		})	
	);	
});
self.addEventListener('activate', function (event) {
    //console.debug('activate', event);
});

self.addEventListener('fetch', function(event) {
	console.debug('activate', event);
	
	if (event.request.method == "GET") event.respondWith(
		fetch(event.request).then(function(response) {
		  return caches.open('offline').then(function(cache) {
			  try {
				cache.put(event.request, response.clone());
			} catch (e) {};
			return response;
		  });
		}).catch(function (error) {
		  caches.match(event.request).then(function(resp) {
			return resp;
		  });
		})
	);
});

self.addEventListener('message', function (event) {
	console.debug('message', event.data);
})

self.addEventListener('push', function (event) {
   const data = event.data.json();
   console.debug('push', data);
   
   const options = {
        body: data.body,
        icon: data.icon ? data.icon : './icon.png',
        requireInteraction: data.requireInteraction,
        persistent: data.persistent,
        sticky: data.sticky,
        vibrate: [100, 50, 100],
        data: data,
        actions: data.actions
    };
	
    event.waitUntil(
        self.registration.showNotification(data.subject, options)
    );
});

self.addEventListener("pushsubscriptionchange", function(e) {
    console.debug('pushsubscriptionchange', e);
});

self.addEventListener('notificationclose', function(event) {
    console.debug('notificationclose', event.notification);
});

self.addEventListener('notificationclick', async function(event) {
	const data = event.notification.data;	
    console.debug('notificationclick', data, event.action, event.reply);
	
	const reply = {data: data.data, action: event.action, value: event.reply}
	const url = "/sparkweb/api/rest/webpush/action";	
	const options = {method: "POST", body: JSON.stringify(reply), headers: {"Authorization": data.token, "Accept":"application/json", "Content-Type":"application/json"}};	
	const response = await fetch(url, options);	
		
    console.debug('notificationclick - response', response);	
	event.notification.close();	
});