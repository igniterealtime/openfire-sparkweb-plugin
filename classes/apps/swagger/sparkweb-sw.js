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
    console.debug('activate', event);
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
        icon: './icon.png',
        requireInteraction: true,
        persistent: true,
        sticky: true,
        vibrate: [100, 50, 100],
        data: data,
        actions: [
          {action: 'reply', type: 'text', title: 'Reply', icon: './check-solid.png', placeholder: 'Type a reply here..'},
          {action: 'ignore', type: 'button', title: 'Ignore', icon: './times-solid.png'},
        ]
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

self.addEventListener('notificationclick', function(event) {
    console.debug('notificationclick', event);
	
	const actionChannel = new BroadcastChannel("sparkweb.notification.action");	
	actionChannel.postMessage({data: event.notification.data, action: event.action, reply: event.reply});		
	
	event.notification.close();	
});