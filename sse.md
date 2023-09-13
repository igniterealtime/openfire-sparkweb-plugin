# Server Sent Events (EventSource)
These are the different types of events that can be sent by the server:
## chatapi.xmpp - raw xmpp messages
The XML is encoded as a base64 string
```JSON
{
    \"xmpp\": \"PG1lc3NhZ2UgeG1sbnM9Imp.....mxkPC9ib2R5PjwvbWVzc2FnZT4=\"
}
``` 
## chatapi.presence - presence broadcasts
```JSON
{
	 'type':'presence',
	 'to':'user@domain',
	 'from':'user@domain',
	 'status':'I am busy right now',
	 'show':'dnd'
}
``` 
## chatapi.chat - one to one chat messages
```JSON
{
	 'type':'chat',
	 'body':'hello',
	 'to':'user@domain',
	 'from':'user@domain'
}
``` 
## chatapi.muc - multi-user groupchat messages
```JSON
{
	 'type':'groupchat',
	 'to':'user@domain',
	 'from':'room@conference.domain',
	 'body':'hello'
}
``` 
```JSON
{
	 'type':'invitationReceived',
	 'password':'',
	 'room':'room',
	 'inviter':'deleo',
	 'to':'user@domain',
	 'from':'user@domain',
	 'reason':'Please join me'
}
``` 
## chatapi.openlink - openlink callstatus messages
```JSON
{
	 'type':'callstatus',
	 'to':'user@domain',
	 'from':'room@conference.domain',
	 'json':'openlink json payload'
}
``` 
## chatapi.fastpath - offer received by agent
```JSON
{
	 'type':'offerReceived',
	 'workgroup':'string',
	 'from':'group@workgroup.domain',
	 'metadata':'string,string...',
	 'id':'string'
}
``` 
## chatapi.fastpath - offer revoked by agent
```JSON
{
	 'type':'offerRevoked',
	 'workgroup':'string',
	 'from':'group@workgroup.domain',
	 'reason':'string',
	 'id':'string'
}
``` 
## chatapi.fastpath - groupchat messages
```JSON
{
	 'type':'offer',
	 'to':'user@domain',
	 'from':'workgroup@workgroup.domain',
	 'mucRoom':'room@conference.domain',
	 'url':'https://server/ofmeet/r/room'
}
``` 
```JSON
{
	 'type':'groupchat',
	 'to':'user@domain',
	 'from':'room@conference.domain',
	 'body':'hello'
}
``` 
This REST endpoint is for documentation only. This SSE endpoint should be used in JavaScript as follows :
```js 
const source = new EventSource('./sse?token=' + token);
source.onerror = async event => { 	
};
source.addEventListener('chatapi.chat', async event => {
});	
```                        
