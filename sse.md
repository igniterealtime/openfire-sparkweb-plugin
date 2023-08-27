# Server Sent Events (EventSource)\n
These are the different types of events that can be sent by the server:\n\n
## chatapi.xmpp - raw xmpp messages\n
The XML is encoded as a base64 string\n\n
```JSON\n
{\n
    \"xmpp\": \"PG1lc3NhZ2UgeG1sbnM9Imp.....mxkPC9ib2R5PjwvbWVzc2FnZT4=\"\n
}\n
``` \n
## chatapi.presence - presence broadcasts\n
```JSON\n
{\n
	 'type':'presence',\n
	 'to':'user@domain',\n
	 'from':'user@domain',\n
	 'status':'I am busy right now',\n
	 'show':'dnd'\n
}\n
``` \n
## chatapi.chat - one to one chat messages\n\n
```JSON\n
{\n
	 'type':'chat',\n
	 'body':'hello',\n
	 'to':'user@domain',\n
	 'from':'user@domain'\n
}\n
``` \n
## chatapi.muc - multi-user groupchat messages\n\n
```JSON\n
{\n
	 'type':'groupchat',\n
	 'to':'user@domain',\n
	 'from':'room@conference.domain',\n
	 'body':'hello'\n
}\n
``` \n\n
```JSON\n
{\n
	 'type':'invitationReceived',\n
	 'password':'',\n
	 'room':'room',\n
	 'inviter':'deleo',\n
	 'to':'user@domain',\n
	 'from':'user@domain',\n
	 'reason':'Please join me'\n
}\n
``` \n
## chatapi.openlink - openlink callstatus messages\n\n
```JSON\n
{\n
	 'type':'callstatus',\n
	 'to':'user@domain',\n
	 'from':'room@conference.domain',\n
	 'json':'openlink json payload'\n
}\n
``` \n\n
## chatapi.fastpath - offer received by agent\n\n
```JSON\n
{\n
	 'type':'offerReceived',\n
	 'workgroup':'string',\n
	 'from':'group@workgroup.domain',\n
	 'metadata':'string,string...',\n
	 'id':'string'\n
}\n
``` \n\n
## chatapi.fastpath - offer revoked by agent\n\n
```JSON\n
{\n
	 'type':'offerRevoked',\n
	 'workgroup':'string',\n
	 'from':'group@workgroup.domain',\n
	 'reason':'string',\n
	 'id':'string'\n
}\n
``` \n\n
## chatapi.fastpath - groupchat messages\n\n
```JSON\n
{\n
	 'type':'offer',\n
	 'to':'user@domain',\n
	 'from':'workgroup@workgroup.domain',\n
	 'mucRoom':'room@conference.domain',\n
	 'url':'https://server/ofmeet/r/room'\n
}\n
``` \n\n
```JSON\n
{\n
	 'type':'groupchat',\n
	 'to':'user@domain',\n
	 'from':'room@conference.domain',\n
	 'body':'hello'\n
}\n
``` \n
This REST endpoint is for documentation only. This SSE endpoint should be used in JavaScript as follows :\n\n
```js \n
const source = new EventSource('./sse?token=' + token);\n\n
source.onerror = async event => { \n\n	
};\n\n
source.addEventListener('chatapi.chat', async event => {\n\n
});	\n
``` \n                       
