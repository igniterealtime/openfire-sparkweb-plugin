


SparkWeb REST API adds support for a whole range of modern web service connections to Openfire/XMPP
  

## Informations

### Version

0.0.1-SNAPSHOT

## Tags

  ### <span id="tag-authentication"></span>Authentication

provides server-side authentication services

  ### <span id="tag-chat"></span>Chat

provides chat services for the authenticated user

  ### <span id="tag-group-chat"></span>Group Chat

provides groupchat services to manage contacts

  ### <span id="tag-presence"></span>Presence

provides presence services

  ### <span id="tag-collaboration"></span>Collaboration

provides meeting and other collaboration services

  ### <span id="tag-user-management"></span>User Management

provides user services for the authenticated user

  ### <span id="tag-contact-management"></span>Contact Management

provides user roster services to manage contacts

  ### <span id="tag-web-push"></span>Web Push

provides server-side Web Push services

  ### <span id="tag-bookmarks"></span>Bookmarks

Create, update and delete Openfire bookmarks

## Content negotiation

### URI Schemes
  * http
  * https

### Consumes
  * application/json

### Produces
  * application/json

## Access control

### Security Schemes

#### authorization (header: authorization)



> **Type**: apikey

## All endpoints

###  authentication

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/totp/authenticate/{username}/{code} | [authenticate totp code](#authenticate-totp-code) | Authenticate a user with with a TOTP code |
| POST | /sparkweb/api/rest/totp/register/{username} | [get totp qr code](#get-totp-qr-code) | Create a TOTP registration QR code |
| POST | /sparkweb/api/rest/login/{username} | [login user](#login-user) | Login with Username/Password |
| POST | /sparkweb/api/rest/logout | [logout user](#logout-user) | Logout user |
| POST | /sparkweb/api/rest/register/{username} | [register user](#register-user) | Register a new user with username/password |
| GET | /sparkweb/api/sse | [sse listener](#sse-listener) | Server Sent Events (EventSource) |
| POST | /sparkweb/api/rest/webauthn/authenticate/finish/{username} | [webauthn authenticate finish](#webauthn-authenticate-finish) | Finish process to authenticate a user with WebAuthn |
| POST | /sparkweb/api/rest/webauthn/authenticate/start/{username} | [webauthn authenticate start](#webauthn-authenticate-start) | Start process to authenticate a user with WebAuthn |
| POST | /sparkweb/api/rest/webauthn/register/finish/{username} | [webauthn register finish](#webauthn-register-finish) | Finish process to register a user for WebAuthn |
| POST | /sparkweb/api/rest/webauthn/register/start/{username} | [webauthn register start](#webauthn-register-start) | Start process to register a user for WebAuthn |
  


###  bookmarks

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/bookmark | [create bookmark](#create-bookmark) | Create a new bookmark |
| DELETE | /sparkweb/api/rest/bookmark/{bookmarkID} | [delete bookmark](#delete-bookmark) | Delete a specific bookmark |
| DELETE | /sparkweb/api/rest/bookmark/{bookmarkID}/{name} | [delete bookmark property](#delete-bookmark-property) | Delete a bookmark property |
| GET | /sparkweb/api/rest/bookmark/{bookmarkID} | [get bookmark](#get-bookmark) | Get a specific bookmark |
| GET | /sparkweb/api/rest/bookmarks | [get bookmarks](#get-bookmarks) | Get all bookmark |
| PUT | /sparkweb/api/rest/bookmark/{bookmarkID} | [update bookmark](#update-bookmark) | Update a specific bookmark |
| POST | /sparkweb/api/rest/bookmark/{bookmarkID}/{name} | [update bookmark property](#update-bookmark-property) | Create/Update a bookmark property |
  


###  chat

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| GET | /sparkweb/api/rest/chat/messages | [get chat conversations](#get-chat-conversations) | Get chat messages |
| POST | /sparkweb/api/rest/chat/chatstate/{state}/{to} | [post chat state](#post-chat-state) | Post chat state indicator |
| POST | /sparkweb/api/rest/chat/message/{to} | [post message](#post-message) | Post chat message |
  


###  collaboration

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/pubsub/create/{node} | [create pub sub node](#create-pub-sub-node) | Create a pubsub node |
| GET | /sparkweb/api/rest/pep/nodes | [get pep nodes](#get-pep-nodes) | Get user pep nodes |
| GET | /sparkweb/api/rest/pubsub/nodes | [get pub sub nodes](#get-pub-sub-nodes) | Get all pubsub nodes |
| GET | /sparkweb/api/rest/pubsub/subscribers/{node} | [get pub subscribers](#get-pub-subscribers) | Get all pubsub subscribers |
| GET | /sparkweb/api/rest/meet/{service}/{id} | [online meeting request](#online-meeting-request) | Request meeting URL |
| POST | /sparkweb/api/rest/xmpp | [post Xmpp message](#post-xmpp-message) | Send XMPP Stanza |
| GET | /sparkweb/api/rest/preview/{quality}/{url} | [preview link](#preview-link) | Request URL preview |
| POST | /sparkweb/api/rest/pep/publish/{node} | [publish pep event](#publish-pep-event) | Publish to pep node |
| POST | /sparkweb/api/rest/pubsub/publish/{node} | [publish pub sub event](#publish-pub-sub-event) | Publish to pubsub node |
| POST | /sparkweb/api/rest/pubsub/subscribe/{node} | [subscribe pub sub event](#subscribe-pub-sub-event) | Subscribe to pubsub node |
| GET | /sparkweb/api/rest/upload/{fileName}/{fileSize} | [upload request](#upload-request) | Request file upload |
  


###  contact_management

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/roster | [create roster](#create-roster) | Create roster entry |
| DELETE | /sparkweb/api/rest/roster/{jid} | [delete roster item](#delete-roster-item) | Remove roster entry |
| GET | /sparkweb/api/rest/roster | [get user roster](#get-user-roster) | Retrieve user roster |
| PUT | /sparkweb/api/rest/roster/{jid} | [update roster item](#update-roster-item) | Update roster entry |
  


###  group_chat

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| GET | /sparkweb/api/rest/groupchat/messages | [get group chat conversations](#get-group-chat-conversations) | Get groupchat messages |
| GET | /sparkweb/api/rest/groupchat/room/{roomName}/chathistory | [get m u c room history](#get-m-u-c-room-history) | Get room history |
| GET | /sparkweb/api/rest/groupchat/room/{roomName} | [get m u c room JSON 2](#get-m-u-c-room-json-2) | Get chat room |
| GET | /sparkweb/api/rest/groupchat/room/{roomName}/occupants | [get m u c room occupants](#get-m-u-c-room-occupants) | Get room occupants |
| GET | /sparkweb/api/rest/groupchat/room/{roomName}/participants | [get m u c room participants](#get-m-u-c-room-participants) | Get room participants |
| GET | /sparkweb/api/rest/groupchat/rooms | [get m u c rooms](#get-m-u-c-rooms) | Get chat rooms |
| POST | /sparkweb/api/rest/groupchat/room/{roomName}/{invitedJid} | [invite to room](#invite-to-room) | Invite another user |
| PUT | /sparkweb/api/rest/groupchat/room/{roomName} | [join room](#join-room) | Join groupchat |
| DELETE | /sparkweb/api/rest/groupchat/room/{roomName} | [leave room](#leave-room) | Leave groupchat |
| POST | /sparkweb/api/rest/groupchat/room/{roomName} | [post to room](#post-to-room) | Post a message to a groupchat |
  


###  presence

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| GET | /sparkweb/api/rest/presence/roster | [get user roster with presence](#get-user-roster-with-presence) | Get contacts presence |
| POST | /sparkweb/api/rest/presence | [post presence](#post-presence) | Set Presence |
| GET | /sparkweb/api/rest/presence/{target} | [probe presence](#probe-presence) | Probe a target user presence |
  


###  user_management

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/user/groups/{groupName} | [add user to group](#add-user-to-group) | Add user to a group |
| POST | /sparkweb/api/rest/user/groups | [add user to groups](#add-user-to-groups) | Add user to groups |
| DELETE | /sparkweb/api/rest/user | [delete user](#delete-user) | Delete authenticated user |
| DELETE | /sparkweb/api/rest/user/groups/{groupName} | [delete user from group](#delete-user-from-group) | Delete user from group |
| DELETE | /sparkweb/api/rest/user/groups | [delete user from groups](#delete-user-from-groups) | Delete user from groups |
| GET | /sparkweb/api/rest/config/global | [get global config](#get-global-config) | List global properties affecting this user |
| GET | /sparkweb/api/rest/user | [get user](#get-user) | Get authenticated user |
| GET | /sparkweb/api/rest/config/properties | [get user config](#get-user-config) | List User Properties |
| GET | /sparkweb/api/rest/user/groups | [get user groups](#get-user-groups) | Get user's groups |
| GET | /sparkweb/api/rest/users | [get users](#get-users) | Get users |
| POST | /sparkweb/api/rest/config/properties | [post user config](#post-user-config) | Update user properties |
| PUT | /sparkweb/api/rest/user | [update user](#update-user) | Update authenticated user |
  


###  web_push

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| GET | /sparkweb/api/rest/webpush/subscribers | [get push subscribers](#get-push-subscribers) | Get all web push subscribers |
| GET | /sparkweb/api/rest/webpush/vapidkey | [get web push public key](#get-web-push-public-key) | Get the webpush vapid key |
| POST | /sparkweb/api/rest/webpush/action | [handle notification action](#handle-notification-action) | Send a notification action |
| POST | /sparkweb/api/rest/webpush/notify/{target} | [post web push notification](#post-web-push-notification) | Send a notification to all subscriptions of another user |
| PUT | /sparkweb/api/rest/webpush/subscribe/{resource} | [post web push subscription](#post-web-push-subscription) | Store web push subscription for this user |
| POST | /sparkweb/api/rest/webpush/message/{target} | [post web push text](#post-web-push-text) | Send a text message to all subscriptions of another user |
  


## Paths

### <span id="add-user-to-group"></span> Add user to a group (*addUserToGroup*)

```
POST /sparkweb/api/rest/user/groups/{groupName}
```

Add authenticated user to a collection of groups. When a group that is provided does not exist, it will be automatically created if possible

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| groupName | `path` | string | `string` |  | ✓ |  | The name of the group that the user is to be added to |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#add-user-to-group-200) | OK | The user was added to all groups |  | [schema](#add-user-to-group-200-schema) |
| [400](#add-user-to-group-400) | Bad Request | The group could not be found |  | [schema](#add-user-to-group-400-schema) |

#### Responses


##### <span id="add-user-to-group-200"></span> 200 - The user was added to all groups
Status: OK

###### <span id="add-user-to-group-200-schema"></span> Schema

##### <span id="add-user-to-group-400"></span> 400 - The group could not be found
Status: Bad Request

###### <span id="add-user-to-group-400-schema"></span> Schema

### <span id="add-user-to-groups"></span> Add user to groups (*addUserToGroups*)

```
POST /sparkweb/api/rest/user/groups
```

Add authenticated user to a collection of groups. When a group that is provided does not exist, it will be automatically created if possible

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [UserGroupsEntity](#user-groups-entity) | `models.UserGroupsEntity` | | ✓ | | A collection of names for groups that the user is to be added to |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#add-user-to-groups-200) | OK | The user was added to all groups |  | [schema](#add-user-to-groups-200-schema) |
| [404](#add-user-to-groups-404) | Not Found | One or more groups could not be found |  | [schema](#add-user-to-groups-404-schema) |

#### Responses


##### <span id="add-user-to-groups-200"></span> 200 - The user was added to all groups
Status: OK

###### <span id="add-user-to-groups-200-schema"></span> Schema

##### <span id="add-user-to-groups-404"></span> 404 - One or more groups could not be found
Status: Not Found

###### <span id="add-user-to-groups-404-schema"></span> Schema

### <span id="authenticate-totp-code"></span> Authenticate a user with with a TOTP code (*authenticateTotpCode*)

```
POST /sparkweb/api/rest/totp/authenticate/{username}/{code}
```

This endpoint is used to authenticate a user with a time based one-time password (TOTP) for two-factor (2FA) authentication

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| code | `path` | string | `string` |  | ✓ |  | A TOTP code |
| username | `path` | string | `string` |  | ✓ |  | A valid Openfire username |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#authenticate-totp-code-200) | OK | The authentication token |  | [schema](#authenticate-totp-code-200-schema) |
| [500](#authenticate-totp-code-500) | Internal Server Error | Authentication failed |  | [schema](#authenticate-totp-code-500-schema) |

#### Responses


##### <span id="authenticate-totp-code-200"></span> 200 - The authentication token
Status: OK

###### <span id="authenticate-totp-code-200-schema"></span> Schema
   
  

[TokenEntity](#token-entity)

##### <span id="authenticate-totp-code-500"></span> 500 - Authentication failed
Status: Internal Server Error

###### <span id="authenticate-totp-code-500-schema"></span> Schema

### <span id="create-bookmark"></span> Create a new bookmark (*createBookmark*)

```
POST /sparkweb/api/rest/bookmark
```

This endpoint is used to create a new bookmark

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [Bookmark](#bookmark) | `models.Bookmark` | | ✓ | | The bookmark definition |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#create-bookmark-200) | OK | Bookmark was created |  | [schema](#create-bookmark-200-schema) |
| [400](#create-bookmark-400) | Bad Request | Bookmark could not be created |  | [schema](#create-bookmark-400-schema) |

#### Responses


##### <span id="create-bookmark-200"></span> 200 - Bookmark was created
Status: OK

###### <span id="create-bookmark-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

##### <span id="create-bookmark-400"></span> 400 - Bookmark could not be created
Status: Bad Request

###### <span id="create-bookmark-400-schema"></span> Schema

### <span id="create-pub-sub-node"></span> Create a pubsub node (*createPubSubNode*)

```
POST /sparkweb/api/rest/pubsub/create/{node}
```

This endpoint is used to create a pubsub node

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| node | `path` | string | `string` |  | ✓ |  | The pubsub node to be created |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#create-pub-sub-node-200) | OK | The node was created |  | [schema](#create-pub-sub-node-200-schema) |
| [400](#create-pub-sub-node-400) | Bad Request | The node could not be created |  | [schema](#create-pub-sub-node-400-schema) |

#### Responses


##### <span id="create-pub-sub-node-200"></span> 200 - The node was created
Status: OK

###### <span id="create-pub-sub-node-200-schema"></span> Schema

##### <span id="create-pub-sub-node-400"></span> 400 - The node could not be created
Status: Bad Request

###### <span id="create-pub-sub-node-400-schema"></span> Schema

### <span id="create-roster"></span> Create roster entry (*createRoster*)

```
POST /sparkweb/api/rest/roster
```

Add a roster entry to the roster (buddies / contact list) of a particular user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [RosterItemEntity](#roster-item-entity) | `models.RosterItemEntity` | | ✓ | | The definition of the roster entry that is to be added |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#create-roster-200) | OK | The entry was added to the roster |  | [schema](#create-roster-200-schema) |
| [400](#create-roster-400) | Bad Request | A roster entry cannot be added to a 'shared group' (try removing group names from the roster entry and try again) |  | [schema](#create-roster-400-schema) |
| [404](#create-roster-404) | Not Found | No user of with this username exists |  | [schema](#create-roster-404-schema) |
| [409](#create-roster-409) | Conflict | A roster entry already exists for the provided contact JID |  | [schema](#create-roster-409-schema) |

#### Responses


##### <span id="create-roster-200"></span> 200 - The entry was added to the roster
Status: OK

###### <span id="create-roster-200-schema"></span> Schema

##### <span id="create-roster-400"></span> 400 - A roster entry cannot be added to a 'shared group' (try removing group names from the roster entry and try again)
Status: Bad Request

###### <span id="create-roster-400-schema"></span> Schema

##### <span id="create-roster-404"></span> 404 - No user of with this username exists
Status: Not Found

###### <span id="create-roster-404-schema"></span> Schema

##### <span id="create-roster-409"></span> 409 - A roster entry already exists for the provided contact JID
Status: Conflict

###### <span id="create-roster-409-schema"></span> Schema

### <span id="delete-bookmark"></span> Delete a specific bookmark (*deleteBookmark*)

```
DELETE /sparkweb/api/rest/bookmark/{bookmarkID}
```

This endpoint is used to delete a specific bookmark

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| bookmarkID | `path` | string | `string` |  | ✓ |  |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [default](#delete-bookmark-default) | | successful operation |  | [schema](#delete-bookmark-default-schema) |

#### Responses


##### <span id="delete-bookmark-default"></span> Default Response
successful operation

###### <span id="delete-bookmark-default-schema"></span> Schema
empty schema

### <span id="delete-bookmark-property"></span> Delete a bookmark property (*deleteBookmarkProperty*)

```
DELETE /sparkweb/api/rest/bookmark/{bookmarkID}/{name}
```

This endpoint is used to delete a bookmark property

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| bookmarkID | `path` | string | `string` |  | ✓ |  |  |
| name | `path` | string | `string` |  | ✓ |  |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-bookmark-property-200) | OK | Bookmark was updated |  | [schema](#delete-bookmark-property-200-schema) |
| [400](#delete-bookmark-property-400) | Bad Request | Bookmark could not be updated |  | [schema](#delete-bookmark-property-400-schema) |

#### Responses


##### <span id="delete-bookmark-property-200"></span> 200 - Bookmark was updated
Status: OK

###### <span id="delete-bookmark-property-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

##### <span id="delete-bookmark-property-400"></span> 400 - Bookmark could not be updated
Status: Bad Request

###### <span id="delete-bookmark-property-400-schema"></span> Schema

### <span id="delete-roster-item"></span> Remove roster entry (*deleteRosterItem*)

```
DELETE /sparkweb/api/rest/roster/{jid}
```

Removes one of the roster entries (contacts) of the authenticated user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| jid | `path` | string | `string` |  | ✓ |  | The JID of the entry/contact to remove |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-roster-item-200) | OK | Roster entry removed |  | [schema](#delete-roster-item-200-schema) |
| [400](#delete-roster-item-400) | Bad Request | A roster entry cannot be removed from a 'shared group' |  | [schema](#delete-roster-item-400-schema) |
| [404](#delete-roster-item-404) | Not Found | No user of with this username exists, or its roster did not contain this entry |  | [schema](#delete-roster-item-404-schema) |

#### Responses


##### <span id="delete-roster-item-200"></span> 200 - Roster entry removed
Status: OK

###### <span id="delete-roster-item-200-schema"></span> Schema

##### <span id="delete-roster-item-400"></span> 400 - A roster entry cannot be removed from a 'shared group'
Status: Bad Request

###### <span id="delete-roster-item-400-schema"></span> Schema

##### <span id="delete-roster-item-404"></span> 404 - No user of with this username exists, or its roster did not contain this entry
Status: Not Found

###### <span id="delete-roster-item-404-schema"></span> Schema

### <span id="delete-user"></span> Delete authenticated user (*deleteUser*)

```
DELETE /sparkweb/api/rest/user
```

Delete authenticated user in Openfire

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-user-200) | OK | The Openfire user was deleted |  | [schema](#delete-user-200-schema) |
| [404](#delete-user-404) | Not Found | No user with that username was found |  | [schema](#delete-user-404-schema) |

#### Responses


##### <span id="delete-user-200"></span> 200 - The Openfire user was deleted
Status: OK

###### <span id="delete-user-200-schema"></span> Schema

##### <span id="delete-user-404"></span> 404 - No user with that username was found
Status: Not Found

###### <span id="delete-user-404-schema"></span> Schema

### <span id="delete-user-from-group"></span> Delete user from group (*deleteUserFromGroup*)

```
DELETE /sparkweb/api/rest/user/groups/{groupName}
```

Removes a user from a group

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| groupName | `path` | string | `string` |  | ✓ |  | The name of the group that the user is to be added to |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-user-from-group-200) | OK | The user was taken out of the group |  | [schema](#delete-user-from-group-200-schema) |
| [400](#delete-user-from-group-400) | Bad Request | The group could not be found |  | [schema](#delete-user-from-group-400-schema) |

#### Responses


##### <span id="delete-user-from-group-200"></span> 200 - The user was taken out of the group
Status: OK

###### <span id="delete-user-from-group-200-schema"></span> Schema

##### <span id="delete-user-from-group-400"></span> 400 - The group could not be found
Status: Bad Request

###### <span id="delete-user-from-group-400-schema"></span> Schema

### <span id="delete-user-from-groups"></span> Delete user from groups (*deleteUserFromGroups*)

```
DELETE /sparkweb/api/rest/user/groups
```

Removes a user from a collection of groups.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [UserGroupsEntity](#user-groups-entity) | `models.UserGroupsEntity` | | ✓ | | A collection of names for groups that the user is to be added to |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-user-from-groups-200) | OK | The user was taken out of the group |  | [schema](#delete-user-from-groups-200-schema) |
| [404](#delete-user-from-groups-404) | Not Found | One or more groups could not be found |  | [schema](#delete-user-from-groups-404-schema) |

#### Responses


##### <span id="delete-user-from-groups-200"></span> 200 - The user was taken out of the group
Status: OK

###### <span id="delete-user-from-groups-200-schema"></span> Schema

##### <span id="delete-user-from-groups-404"></span> 404 - One or more groups could not be found
Status: Not Found

###### <span id="delete-user-from-groups-404-schema"></span> Schema

### <span id="get-bookmark"></span> Get a specific bookmark (*getBookmark*)

```
GET /sparkweb/api/rest/bookmark/{bookmarkID}
```

This endpoint is used to retrieve a specific bookmark

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| bookmarkID | `path` | string | `string` |  | ✓ |  |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-bookmark-200) | OK | Bookmark was retrieved |  | [schema](#get-bookmark-200-schema) |
| [400](#get-bookmark-400) | Bad Request | Bookmark could not be retrieved |  | [schema](#get-bookmark-400-schema) |

#### Responses


##### <span id="get-bookmark-200"></span> 200 - Bookmark was retrieved
Status: OK

###### <span id="get-bookmark-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

##### <span id="get-bookmark-400"></span> 400 - Bookmark could not be retrieved
Status: Bad Request

###### <span id="get-bookmark-400-schema"></span> Schema

### <span id="get-bookmarks"></span> Get all bookmark (*getBookmarks*)

```
GET /sparkweb/api/rest/bookmarks
```

This endpoint is used to retrieve all bookmarks

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-bookmarks-200) | OK | List of bookmarks |  | [schema](#get-bookmarks-200-schema) |
| [400](#get-bookmarks-400) | Bad Request | Bookmarks could not be retrieved |  | [schema](#get-bookmarks-400-schema) |

#### Responses


##### <span id="get-bookmarks-200"></span> 200 - List of bookmarks
Status: OK

###### <span id="get-bookmarks-200-schema"></span> Schema
   
  

[Bookmarks](#bookmarks)

##### <span id="get-bookmarks-400"></span> 400 - Bookmarks could not be retrieved
Status: Bad Request

###### <span id="get-bookmarks-400-schema"></span> Schema

### <span id="get-chat-conversations"></span> Get chat messages (*getChatConversations*)

```
GET /sparkweb/api/rest/chat/messages
```

Retrieves chat messages from Openfire messages archive

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| end | `query` | string | `string` |  |  |  | The end date in MM/dd/yy format |
| keywords | `query` | string | `string` |  |  |  | Search keywords |
| start | `query` | string | `string` |  |  |  | The start date in MM/dd/yy format |
| to | `query` | string | `string` |  |  |  | The message target |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-chat-conversations-200) | OK | The messages were retrieved |  | [schema](#get-chat-conversations-200-schema) |
| [400](#get-chat-conversations-400) | Bad Request | The messages could not be retrieved |  | [schema](#get-chat-conversations-400-schema) |

#### Responses


##### <span id="get-chat-conversations-200"></span> 200 - The messages were retrieved
Status: OK

###### <span id="get-chat-conversations-200-schema"></span> Schema
   
  

[Conversations](#conversations)

##### <span id="get-chat-conversations-400"></span> 400 - The messages could not be retrieved
Status: Bad Request

###### <span id="get-chat-conversations-400-schema"></span> Schema

### <span id="get-global-config"></span> List global properties affecting this user (*getGlobalConfig*)

```
GET /sparkweb/api/rest/config/global
```

Endpoint will retrieve all Openfire Global properties that are used by this authenticated user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-global-config-200) | OK | successful operation |  | [schema](#get-global-config-200-schema) |

#### Responses


##### <span id="get-global-config-200"></span> 200 - successful operation
Status: OK

###### <span id="get-global-config-200-schema"></span> Schema
   
  



### <span id="get-group-chat-conversations"></span> Get groupchat messages (*getGroupChatConversations*)

```
GET /sparkweb/api/rest/groupchat/messages
```

Retrieves chat groupchat messages from Openfire messages archive

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| end | `query` | string | `string` |  |  |  | The end date in MM/dd/yy format |
| keywords | `query` | string | `string` |  |  |  | Search keywords |
| room | `query` | string | `string` |  |  |  | The groupchat room used |
| service | `query` | string | `string` |  |  | `"conference"` | The groupchat service name |
| start | `query` | string | `string` |  |  |  | The start date in MM/dd/yy format |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-group-chat-conversations-200) | OK | The messages were retrieved |  | [schema](#get-group-chat-conversations-200-schema) |
| [400](#get-group-chat-conversations-400) | Bad Request | The messages could not be retrieved |  | [schema](#get-group-chat-conversations-400-schema) |

#### Responses


##### <span id="get-group-chat-conversations-200"></span> 200 - The messages were retrieved
Status: OK

###### <span id="get-group-chat-conversations-200-schema"></span> Schema
   
  

[Conversations](#conversations)

##### <span id="get-group-chat-conversations-400"></span> 400 - The messages could not be retrieved
Status: Bad Request

###### <span id="get-group-chat-conversations-400-schema"></span> Schema

### <span id="get-m-u-c-room-history"></span> Get room history (*getMUCRoomHistory*)

```
GET /sparkweb/api/rest/groupchat/room/{roomName}/chathistory
```

Get messages that have been exchanged in a specific multi-user chat room

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| roomName | `path` | string | `string` |  | ✓ |  | The name of the MUC room |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-m-u-c-room-history-200) | OK | The chat room message history |  | [schema](#get-m-u-c-room-history-200-schema) |
| [404](#get-m-u-c-room-history-404) | Not Found | The chat room (or its service) can not be found or is not accessible |  | [schema](#get-m-u-c-room-history-404-schema) |

#### Responses


##### <span id="get-m-u-c-room-history-200"></span> 200 - The chat room message history
Status: OK

###### <span id="get-m-u-c-room-history-200-schema"></span> Schema
   
  

[MUCRoomMessageEntities](#m-u-c-room-message-entities)

##### <span id="get-m-u-c-room-history-404"></span> 404 - The chat room (or its service) can not be found or is not accessible
Status: Not Found

###### <span id="get-m-u-c-room-history-404-schema"></span> Schema

### <span id="get-m-u-c-room-json-2"></span> Get chat room (*getMUCRoomJSON2*)

```
GET /sparkweb/api/rest/groupchat/room/{roomName}
```

Get information of a specific multi-user chat room

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| roomName | `path` | string | `string` |  | ✓ |  | The name of the MUC room |
| expandGroups | `query` | boolean | `bool` |  |  |  | For all groups defined in owners, admins, members and outcasts, list individual members instead of the group name |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-m-u-c-room-json-2-200) | OK | The chat room |  | [schema](#get-m-u-c-room-json-2-200-schema) |
| [404](#get-m-u-c-room-json-2-404) | Not Found | The chat room (or its service) can not be found or is not accessible |  | [schema](#get-m-u-c-room-json-2-404-schema) |

#### Responses


##### <span id="get-m-u-c-room-json-2-200"></span> 200 - The chat room
Status: OK

###### <span id="get-m-u-c-room-json-2-200-schema"></span> Schema
   
  

[MUCRoomEntity](#m-u-c-room-entity)

##### <span id="get-m-u-c-room-json-2-404"></span> 404 - The chat room (or its service) can not be found or is not accessible
Status: Not Found

###### <span id="get-m-u-c-room-json-2-404-schema"></span> Schema

### <span id="get-m-u-c-room-occupants"></span> Get room occupants (*getMUCRoomOccupants*)

```
GET /sparkweb/api/rest/groupchat/room/{roomName}/occupants
```

Get all occupants of a specific multi-user chat room

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| roomName | `path` | string | `string` |  | ✓ |  | The name of the MUC room |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-m-u-c-room-occupants-200) | OK | The chat room occupants |  | [schema](#get-m-u-c-room-occupants-200-schema) |
| [404](#get-m-u-c-room-occupants-404) | Not Found | The chat room (or its service) can not be found or is not accessible |  | [schema](#get-m-u-c-room-occupants-404-schema) |

#### Responses


##### <span id="get-m-u-c-room-occupants-200"></span> 200 - The chat room occupants
Status: OK

###### <span id="get-m-u-c-room-occupants-200-schema"></span> Schema
   
  

[OccupantEntities](#occupant-entities)

##### <span id="get-m-u-c-room-occupants-404"></span> 404 - The chat room (or its service) can not be found or is not accessible
Status: Not Found

###### <span id="get-m-u-c-room-occupants-404-schema"></span> Schema

### <span id="get-m-u-c-room-participants"></span> Get room participants (*getMUCRoomParticipants*)

```
GET /sparkweb/api/rest/groupchat/room/{roomName}/participants
```

Get all participants of a specific multi-user chat room

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| roomName | `path` | string | `string` |  | ✓ |  | The name of the MUC room |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-m-u-c-room-participants-200) | OK | The chat room participants |  | [schema](#get-m-u-c-room-participants-200-schema) |
| [404](#get-m-u-c-room-participants-404) | Not Found | The chat room (or its service) can not be found or is not accessible |  | [schema](#get-m-u-c-room-participants-404-schema) |

#### Responses


##### <span id="get-m-u-c-room-participants-200"></span> 200 - The chat room participants
Status: OK

###### <span id="get-m-u-c-room-participants-200-schema"></span> Schema

##### <span id="get-m-u-c-room-participants-404"></span> 404 - The chat room (or its service) can not be found or is not accessible
Status: Not Found

###### <span id="get-m-u-c-room-participants-404-schema"></span> Schema

### <span id="get-m-u-c-rooms"></span> Get chat rooms (*getMUCRooms*)

```
GET /sparkweb/api/rest/groupchat/rooms
```

Get a list of all multi-user chat rooms of a particular chat room service

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| expandGroups | `query` | boolean | `bool` |  |  |  | For all groups defined in owners, admins, members and outcasts, list individual members instead of the group name |
| search | `query` | string | `string` |  |  |  | Search/Filter by room name.
This act like the wildcard search %String% |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service for which to return all chat rooms |
| type | `query` | string | `string` |  |  | `"public"` | Room type-based filter: 'all' or 'public' |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-m-u-c-rooms-200) | OK | All chat rooms |  | [schema](#get-m-u-c-rooms-200-schema) |
| [404](#get-m-u-c-rooms-404) | Not Found | MUC service does not exist or is not accessible |  | [schema](#get-m-u-c-rooms-404-schema) |

#### Responses


##### <span id="get-m-u-c-rooms-200"></span> 200 - All chat rooms
Status: OK

###### <span id="get-m-u-c-rooms-200-schema"></span> Schema
   
  

[MUCRoomEntities](#m-u-c-room-entities)

##### <span id="get-m-u-c-rooms-404"></span> 404 - MUC service does not exist or is not accessible
Status: Not Found

###### <span id="get-m-u-c-rooms-404-schema"></span> Schema

### <span id="get-pep-nodes"></span> Get user pep nodes (*getPepNodes*)

```
GET /sparkweb/api/rest/pep/nodes
```

This endpoint is used to obtain user pep nodes

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-pep-nodes-200) | OK | The request was accepted |  | [schema](#get-pep-nodes-200-schema) |
| [400](#get-pep-nodes-400) | Bad Request | The request was rejected |  | [schema](#get-pep-nodes-400-schema) |

#### Responses


##### <span id="get-pep-nodes-200"></span> 200 - The request was accepted
Status: OK

###### <span id="get-pep-nodes-200-schema"></span> Schema
   
  

[NodeEntities](#node-entities)

##### <span id="get-pep-nodes-400"></span> 400 - The request was rejected
Status: Bad Request

###### <span id="get-pep-nodes-400-schema"></span> Schema

### <span id="get-pub-sub-nodes"></span> Get all pubsub nodes (*getPubSubNodes*)

```
GET /sparkweb/api/rest/pubsub/nodes
```

This endpoint is used to obtain pubsub nodes

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-pub-sub-nodes-200) | OK | The request was accepted |  | [schema](#get-pub-sub-nodes-200-schema) |
| [400](#get-pub-sub-nodes-400) | Bad Request | The request was rejected |  | [schema](#get-pub-sub-nodes-400-schema) |

#### Responses


##### <span id="get-pub-sub-nodes-200"></span> 200 - The request was accepted
Status: OK

###### <span id="get-pub-sub-nodes-200-schema"></span> Schema
   
  

[NodeEntities](#node-entities)

##### <span id="get-pub-sub-nodes-400"></span> 400 - The request was rejected
Status: Bad Request

###### <span id="get-pub-sub-nodes-400-schema"></span> Schema

### <span id="get-pub-subscribers"></span> Get all pubsub subscribers (*getPubSubscribers*)

```
GET /sparkweb/api/rest/pubsub/subscribers/{node}
```

This endpoint is used to obtain pubsub subscribers for a specific node

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| node | `path` | string | `string` |  | ✓ |  | The pubsub node of interest |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-pub-subscribers-200) | OK | The request was accepted |  | [schema](#get-pub-subscribers-200-schema) |
| [400](#get-pub-subscribers-400) | Bad Request | The request was rejected |  | [schema](#get-pub-subscribers-400-schema) |

#### Responses


##### <span id="get-pub-subscribers-200"></span> 200 - The request was accepted
Status: OK

###### <span id="get-pub-subscribers-200-schema"></span> Schema
   
  

[UserEntities](#user-entities)

##### <span id="get-pub-subscribers-400"></span> 400 - The request was rejected
Status: Bad Request

###### <span id="get-pub-subscribers-400-schema"></span> Schema

### <span id="get-push-subscribers"></span> Get all web push subscribers (*getPushSubscribers*)

```
GET /sparkweb/api/rest/webpush/subscribers
```

This endpoint is used to obtain all web push subscribers

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-push-subscribers-200) | OK | successful operation |  | [schema](#get-push-subscribers-200-schema) |

#### Responses


##### <span id="get-push-subscribers-200"></span> 200 - successful operation
Status: OK

###### <span id="get-push-subscribers-200-schema"></span> Schema
   
  

[UserEntities](#user-entities)

### <span id="get-totp-qr-code"></span> Create a TOTP registration QR code (*getTotpQrCode*)

```
POST /sparkweb/api/rest/totp/register/{username}
```

This endpoint is used to obtain a QR code for time based one-time password (TOTP) two-factor (2FA) authentication

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| username | `path` | string | `string` |  | ✓ |  | A valid Openfire username |
| body | `body` | string | `string` | | ✓ | | The current Openfire password for the user |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-totp-qr-code-200) | OK | QR Code for TOTP registration |  | [schema](#get-totp-qr-code-200-schema) |
| [500](#get-totp-qr-code-500) | Internal Server Error | Request failed |  | [schema](#get-totp-qr-code-500-schema) |

#### Responses


##### <span id="get-totp-qr-code-200"></span> 200 - QR Code for TOTP registration
Status: OK

###### <span id="get-totp-qr-code-200-schema"></span> Schema
   
  

[TOTPEntity](#t-o-t-p-entity)

##### <span id="get-totp-qr-code-500"></span> 500 - Request failed
Status: Internal Server Error

###### <span id="get-totp-qr-code-500-schema"></span> Schema

### <span id="get-user"></span> Get authenticated user (*getUser*)

```
GET /sparkweb/api/rest/user
```

Retrieve a user that is defined in Openfire

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-user-200) | OK | The Openfire user was retrieved |  | [schema](#get-user-200-schema) |
| [404](#get-user-404) | Not Found | No user with that username was found |  | [schema](#get-user-404-schema) |

#### Responses


##### <span id="get-user-200"></span> 200 - The Openfire user was retrieved
Status: OK

###### <span id="get-user-200-schema"></span> Schema
   
  

[UserEntity](#user-entity)

##### <span id="get-user-404"></span> 404 - No user with that username was found
Status: Not Found

###### <span id="get-user-404-schema"></span> Schema

### <span id="get-user-config"></span> List User Properties (*getUserConfig*)

```
GET /sparkweb/api/rest/config/properties
```

Endpoint to retrieve a list of all config properties for the authenticated user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-user-config-200) | OK | successful operation |  | [schema](#get-user-config-200-schema) |

#### Responses


##### <span id="get-user-config-200"></span> 200 - successful operation
Status: OK

###### <span id="get-user-config-200-schema"></span> Schema
   
  



### <span id="get-user-groups"></span> Get user's groups (*getUserGroups*)

```
GET /sparkweb/api/rest/user/groups
```

Retrieve names of all groups that a particular user is in

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-user-groups-200) | OK | The names of the groups that the user is in |  | [schema](#get-user-groups-200-schema) |

#### Responses


##### <span id="get-user-groups-200"></span> 200 - The names of the groups that the user is in
Status: OK

###### <span id="get-user-groups-200-schema"></span> Schema
   
  

[UserGroupsEntity](#user-groups-entity)

### <span id="get-user-roster"></span> Retrieve user roster (*getUserRoster*)

```
GET /sparkweb/api/rest/roster
```

Get a list of all roster entries (buddies / contact list) of a authenticated user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-user-roster-200) | OK | All roster entries |  | [schema](#get-user-roster-200-schema) |
| [404](#get-user-roster-404) | Not Found | No user with that username was found |  | [schema](#get-user-roster-404-schema) |

#### Responses


##### <span id="get-user-roster-200"></span> 200 - All roster entries
Status: OK

###### <span id="get-user-roster-200-schema"></span> Schema
   
  

[RosterEntities](#roster-entities)

##### <span id="get-user-roster-404"></span> 404 - No user with that username was found
Status: Not Found

###### <span id="get-user-roster-404-schema"></span> Schema

### <span id="get-user-roster-with-presence"></span> Get contacts presence (*getUserRosterWithPresence*)

```
GET /sparkweb/api/rest/presence/roster
```

Retrieve a list of all roster entries (buddies / contact list) with presence of a authenticated user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-user-roster-with-presence-200) | OK | All roster entries with presence |  | [schema](#get-user-roster-with-presence-200-schema) |
| [400](#get-user-roster-with-presence-400) | Bad Request | No xmpp connection found for authenticated user |  | [schema](#get-user-roster-with-presence-400-schema) |

#### Responses


##### <span id="get-user-roster-with-presence-200"></span> 200 - All roster entries with presence
Status: OK

###### <span id="get-user-roster-with-presence-200-schema"></span> Schema
   
  

[RosterEntities](#roster-entities)

##### <span id="get-user-roster-with-presence-400"></span> 400 - No xmpp connection found for authenticated user
Status: Bad Request

###### <span id="get-user-roster-with-presence-400-schema"></span> Schema

### <span id="get-users"></span> Get users (*getUsers*)

```
GET /sparkweb/api/rest/users
```

Retrieve all users defined in Openfire (with optional filtering)

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| propertyKey | `query` | string | `string` |  |  |  | Filter by a user property name |
| propertyValue | `query` | string | `string` |  |  |  | Filter by user property value. Note: This can only be used in combination with a property name parameter |
| search | `query` | string | `string` |  |  |  | Search/Filter by username. This act like the wildcard search %String% |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-users-200) | OK | A list of Openfire users |  | [schema](#get-users-200-schema) |

#### Responses


##### <span id="get-users-200"></span> 200 - A list of Openfire users
Status: OK

###### <span id="get-users-200-schema"></span> Schema
   
  

[UserEntities](#user-entities)

### <span id="get-web-push-public-key"></span> Get the webpush vapid key (*getWebPushPublicKey*)

```
GET /sparkweb/api/rest/webpush/vapidkey
```

This endpoint is used to obtain the vapid key need by the client to sign web push messages

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-web-push-public-key-200) | OK | successful operation |  | [schema](#get-web-push-public-key-200-schema) |

#### Responses


##### <span id="get-web-push-public-key-200"></span> 200 - successful operation
Status: OK

###### <span id="get-web-push-public-key-200-schema"></span> Schema
   
  

[PublicKey](#public-key)

### <span id="handle-notification-action"></span> Send a notification action (*handleNotificationAction*)

```
POST /sparkweb/api/rest/webpush/action
```

This endpoint is used to post the user action of a web push notification

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [NotificationActionEntity](#notification-action-entity) | `models.NotificationActionEntity` | | ✓ | | A notification action |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [default](#handle-notification-action-default) | | successful operation |  | [schema](#handle-notification-action-default-schema) |

#### Responses


##### <span id="handle-notification-action-default"></span> Default Response
successful operation

###### <span id="handle-notification-action-default-schema"></span> Schema
empty schema

### <span id="invite-to-room"></span> Invite another user (*inviteToRoom*)

```
POST /sparkweb/api/rest/groupchat/room/{roomName}/{invitedJid}
```

Invite another user to a groupchat

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| invitedJid | `path` | string | `string` |  | ✓ |  | The xmpp address of the person to be invited |
| roomName | `path` | string | `string` |  | ✓ |  | The name of the MUC room |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service |
| body | `body` | string | `string` | | ✓ | | The reason for the invitation |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#invite-to-room-200) | OK | The invitation has been sent |  | [schema](#invite-to-room-200-schema) |
| [404](#invite-to-room-404) | Not Found | The chat room (or its service) can not be found or is not accessible |  | [schema](#invite-to-room-404-schema) |

#### Responses


##### <span id="invite-to-room-200"></span> 200 - The invitation has been sent
Status: OK

###### <span id="invite-to-room-200-schema"></span> Schema

##### <span id="invite-to-room-404"></span> 404 - The chat room (or its service) can not be found or is not accessible
Status: Not Found

###### <span id="invite-to-room-404-schema"></span> Schema

### <span id="join-room"></span> Join groupchat (*joinRoom*)

```
PUT /sparkweb/api/rest/groupchat/room/{roomName}
```

Join a groupchat by entering a MUC room

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| roomName | `path` | string | `string` |  | ✓ |  | The name of the MUC room to join |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#join-room-200) | OK | The user has joined groupchat |  | [schema](#join-room-200-schema) |
| [404](#join-room-404) | Not Found | The chat room (or its service) can not be found or is not accessible |  | [schema](#join-room-404-schema) |

#### Responses


##### <span id="join-room-200"></span> 200 - The user has joined groupchat
Status: OK

###### <span id="join-room-200-schema"></span> Schema

##### <span id="join-room-404"></span> 404 - The chat room (or its service) can not be found or is not accessible
Status: Not Found

###### <span id="join-room-404-schema"></span> Schema

### <span id="leave-room"></span> Leave groupchat (*leaveRoom*)

```
DELETE /sparkweb/api/rest/groupchat/room/{roomName}
```

Leave a groupchat by leaving a MUC room

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| roomName | `path` | string | `string` |  | ✓ |  | The name of the MUC room to leave |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#leave-room-200) | OK | The user has left groupchat |  | [schema](#leave-room-200-schema) |
| [404](#leave-room-404) | Not Found | The chat room (or its service) can not be found or is not accessible |  | [schema](#leave-room-404-schema) |

#### Responses


##### <span id="leave-room-200"></span> 200 - The user has left groupchat
Status: OK

###### <span id="leave-room-200-schema"></span> Schema

##### <span id="leave-room-404"></span> 404 - The chat room (or its service) can not be found or is not accessible
Status: Not Found

###### <span id="leave-room-404-schema"></span> Schema

### <span id="login-user"></span> Login with Username/Password (*loginUser*)

```
POST /sparkweb/api/rest/login/{username}
```

This endpoint is used to login a user with a username and password

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| username | `path` | string | `string` |  | ✓ |  | A valid Openfire username |
| body | `body` | string | `string` | | ✓ | | The current Openfire password for the user |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#login-user-200) | OK | The authentication token |  | [schema](#login-user-200-schema) |
| [500](#login-user-500) | Internal Server Error | Authentication failed |  | [schema](#login-user-500-schema) |

#### Responses


##### <span id="login-user-200"></span> 200 - The authentication token
Status: OK

###### <span id="login-user-200-schema"></span> Schema
   
  

[TokenEntity](#token-entity)

##### <span id="login-user-500"></span> 500 - Authentication failed
Status: Internal Server Error

###### <span id="login-user-500-schema"></span> Schema

### <span id="logout-user"></span> Logout user (*logoutUser*)

```
POST /sparkweb/api/rest/logout
```

This end point is used to logout the authenticated user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#logout-user-200) | OK | The user was logged out |  | [schema](#logout-user-200-schema) |
| [400](#logout-user-400) | Bad Request | The user not be logged out |  | [schema](#logout-user-400-schema) |

#### Responses


##### <span id="logout-user-200"></span> 200 - The user was logged out
Status: OK

###### <span id="logout-user-200-schema"></span> Schema

##### <span id="logout-user-400"></span> 400 - The user not be logged out
Status: Bad Request

###### <span id="logout-user-400-schema"></span> Schema

### <span id="online-meeting-request"></span> Request meeting URL (*onlineMeetingRequest*)

```
GET /sparkweb/api/rest/meet/{service}/{id}
```

Request for online meeting URL needed to join and share with other users

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| id | `path` | string | `string` |  | ✓ |  | The online meeting room, group or identity requested for |
| service | `path` | string | `string` |  | ✓ |  | The online meeting service required Only 'jitsi' and 'galene' are supported |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#online-meeting-request-200) | OK | The request was accepted |  | [schema](#online-meeting-request-200-schema) |
| [400](#online-meeting-request-400) | Bad Request | The meeting url request failed |  | [schema](#online-meeting-request-400-schema) |

#### Responses


##### <span id="online-meeting-request-200"></span> 200 - The request was accepted
Status: OK

###### <span id="online-meeting-request-200-schema"></span> Schema
   
  

[OnlineMeetingEntity](#online-meeting-entity)

##### <span id="online-meeting-request-400"></span> 400 - The meeting url request failed
Status: Bad Request

###### <span id="online-meeting-request-400-schema"></span> Schema

### <span id="post-chat-state"></span> Post chat state indicator (*postChatState*)

```
POST /sparkweb/api/rest/chat/chatstate/{state}/{to}
```

Post a chat state to an xmpp address

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| state | `path` | string | `string` |  | ✓ |  | The chat state to be posted. It can be 'composing', 'paused', 'active', 'inactive', 'gone' |
| to | `path` | string | `string` |  | ✓ |  | The JID of the target xmpp address |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#post-chat-state-200) | OK | The chat state was posted |  | [schema](#post-chat-state-200-schema) |
| [400](#post-chat-state-400) | Bad Request | The chat state could not be posted |  | [schema](#post-chat-state-400-schema) |

#### Responses


##### <span id="post-chat-state-200"></span> 200 - The chat state was posted
Status: OK

###### <span id="post-chat-state-200-schema"></span> Schema

##### <span id="post-chat-state-400"></span> 400 - The chat state could not be posted
Status: Bad Request

###### <span id="post-chat-state-400-schema"></span> Schema

### <span id="post-message"></span> Post chat message (*postMessage*)

```
POST /sparkweb/api/rest/chat/message/{to}
```

post a chat message to an xmpp address

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| to | `path` | string | `string` |  | ✓ |  | The JID of the target xmpp address |
| body | `body` | string | `string` | | ✓ | | The text message to be posted |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#post-message-200) | OK | The messages was posted |  | [schema](#post-message-200-schema) |
| [400](#post-message-400) | Bad Request | The messages could not be posted |  | [schema](#post-message-400-schema) |

#### Responses


##### <span id="post-message-200"></span> 200 - The messages was posted
Status: OK

###### <span id="post-message-200-schema"></span> Schema

##### <span id="post-message-400"></span> 400 - The messages could not be posted
Status: Bad Request

###### <span id="post-message-400-schema"></span> Schema

### <span id="post-presence"></span> Set Presence (*postPresence*)

```
POST /sparkweb/api/rest/presence
```

Update the presence state of the authenticated user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| show | `query` | string | `string` |  |  |  | The availability state of the authenticated user |
| status | `query` | string | `string` |  |  |  | A detailed description of the availability state |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#post-presence-200) | OK | Presence was set |  | [schema](#post-presence-200-schema) |
| [400](#post-presence-400) | Bad Request | No xmpp connection found for authenticated user |  | [schema](#post-presence-400-schema) |

#### Responses


##### <span id="post-presence-200"></span> 200 - Presence was set
Status: OK

###### <span id="post-presence-200-schema"></span> Schema

##### <span id="post-presence-400"></span> 400 - No xmpp connection found for authenticated user
Status: Bad Request

###### <span id="post-presence-400-schema"></span> Schema

### <span id="post-to-room"></span> Post a message to a groupchat (*postToRoom*)

```
POST /sparkweb/api/rest/groupchat/room/{roomName}
```

Post a message to a groupchat

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| roomName | `path` | string | `string` |  | ✓ |  | The name of the MUC room to post to |
| serviceName | `query` | string | `string` |  |  | `"conference"` | The name of the MUC service |
| body | `body` | string | `string` | | ✓ | | The text message to be posted |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#post-to-room-200) | OK | The message is posted |  | [schema](#post-to-room-200-schema) |
| [404](#post-to-room-404) | Not Found | The chat room (or its service) can not be found or is not accessible |  | [schema](#post-to-room-404-schema) |

#### Responses


##### <span id="post-to-room-200"></span> 200 - The message is posted
Status: OK

###### <span id="post-to-room-200-schema"></span> Schema

##### <span id="post-to-room-404"></span> 404 - The chat room (or its service) can not be found or is not accessible
Status: Not Found

###### <span id="post-to-room-404-schema"></span> Schema

### <span id="post-user-config"></span> Update user properties (*postUserConfig*)

```
POST /sparkweb/api/rest/config/properties
```

Endpoint will update user properties from an array of name/value pairs

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | string | `string` | | ✓ | | A JSON array of name pairs (name/value) to set the value of a property |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [default](#post-user-config-default) | | successful operation |  | [schema](#post-user-config-default-schema) |

#### Responses


##### <span id="post-user-config-default"></span> Default Response
successful operation

###### <span id="post-user-config-default-schema"></span> Schema
empty schema

### <span id="post-web-push-notification"></span> Send a notification to all subscriptions of another user (*postWebPushNotification*)

```
POST /sparkweb/api/rest/webpush/notify/{target}
```

This endpoint is used to push a notification to all subscriptions of the specified user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| target | `path` | string | `string` |  | ✓ |  | A valid Openfire username |
| body | `body` | [NotificationEntity](#notification-entity) | `models.NotificationEntity` | | ✓ | | The notification to be pushed to the user |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [default](#post-web-push-notification-default) | | successful operation |  | [schema](#post-web-push-notification-default-schema) |

#### Responses


##### <span id="post-web-push-notification-default"></span> Default Response
successful operation

###### <span id="post-web-push-notification-default-schema"></span> Schema
empty schema

### <span id="post-web-push-subscription"></span> Store web push subscription for this user (*postWebPushSubscription*)

```
PUT /sparkweb/api/rest/webpush/subscribe/{resource}
```

This endpoint is used to save a subscription created by a web client for this user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| resource | `path` | string | `string` |  | ✓ |  | A resource name to tag the subscription |
| body | `body` | string | `string` | | ✓ | | The subscription as created by the web client |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [default](#post-web-push-subscription-default) | | successful operation |  | [schema](#post-web-push-subscription-default-schema) |

#### Responses


##### <span id="post-web-push-subscription-default"></span> Default Response
successful operation

###### <span id="post-web-push-subscription-default-schema"></span> Schema
empty schema

### <span id="post-web-push-text"></span> Send a text message to all subscriptions of another user (*postWebPushText*)

```
POST /sparkweb/api/rest/webpush/message/{target}
```

This endpoint is used to push a text message to all subscriptions of the specified user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| target | `path` | string | `string` |  | ✓ |  | A valid Openfire username |
| body | `body` | string | `string` | | ✓ | | The text message to be pushed to the user |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [default](#post-web-push-text-default) | | successful operation |  | [schema](#post-web-push-text-default-schema) |

#### Responses


##### <span id="post-web-push-text-default"></span> Default Response
successful operation

###### <span id="post-web-push-text-default-schema"></span> Schema
empty schema

### <span id="post-xmpp-message"></span> Send XMPP Stanza (*postXmppMessage*)

```
POST /sparkweb/api/rest/xmpp
```

send an XMPP stanza from aunthenticated user to server

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | string | `string` | | ✓ | | XMPP stanza to be sent to server |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#post-xmpp-message-200) | OK | XMPP stanza was set |  | [schema](#post-xmpp-message-200-schema) |
| [400](#post-xmpp-message-400) | Bad Request | No xmpp connection found for authenticated user |  | [schema](#post-xmpp-message-400-schema) |

#### Responses


##### <span id="post-xmpp-message-200"></span> 200 - XMPP stanza was set
Status: OK

###### <span id="post-xmpp-message-200-schema"></span> Schema

##### <span id="post-xmpp-message-400"></span> 400 - No xmpp connection found for authenticated user
Status: Bad Request

###### <span id="post-xmpp-message-400-schema"></span> Schema

### <span id="preview-link"></span> Request URL preview (*previewLink*)

```
GET /sparkweb/api/rest/preview/{quality}/{url}
```

Request for URL preview metadata

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| quality | `path` | string | `string` |  | ✓ |  | The quality of the preview image on a scale 1-9 |
| url | `path` | string | `string` |  | ✓ |  | The url to be previewd |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#preview-link-200) | OK | The metadata was obtained |  | [schema](#preview-link-200-schema) |
| [400](#preview-link-400) | Bad Request | The preview request failed |  | [schema](#preview-link-400-schema) |

#### Responses


##### <span id="preview-link-200"></span> 200 - The metadata was obtained
Status: OK

###### <span id="preview-link-200-schema"></span> Schema

##### <span id="preview-link-400"></span> 400 - The preview request failed
Status: Bad Request

###### <span id="preview-link-400-schema"></span> Schema

### <span id="probe-presence"></span> Probe a target user presence (*probePresence*)

```
GET /sparkweb/api/rest/presence/{target}
```

Request the presence of an specific user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| target | `path` | string | `string` |  | ✓ |  | The username to be probed |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#probe-presence-200) | OK | Presence of user requested |  | [schema](#probe-presence-200-schema) |
| [400](#probe-presence-400) | Bad Request | No xmpp connection found for authenticated user or authenticated user is not premitted to probe user presence |  | [schema](#probe-presence-400-schema) |

#### Responses


##### <span id="probe-presence-200"></span> 200 - Presence of user requested
Status: OK

###### <span id="probe-presence-200-schema"></span> Schema
   
  

[PresenceEntity](#presence-entity)

##### <span id="probe-presence-400"></span> 400 - No xmpp connection found for authenticated user or authenticated user is not premitted to probe user presence
Status: Bad Request

###### <span id="probe-presence-400-schema"></span> Schema

### <span id="publish-pep-event"></span> Publish to pep node (*publishPepEvent*)

```
POST /sparkweb/api/rest/pep/publish/{node}
```

This endpoint is used to publish a JSON payload to personal eventing protocol (PEP) node

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| node | `path` | string | `string` |  | ✓ |  | The pep node to be published |
| body | `body` | string | `string` | | ✓ | | The JSON payload to be published |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#publish-pep-event-200) | OK | The payload was published |  | [schema](#publish-pep-event-200-schema) |
| [400](#publish-pep-event-400) | Bad Request | The payload could not be published |  | [schema](#publish-pep-event-400-schema) |

#### Responses


##### <span id="publish-pep-event-200"></span> 200 - The payload was published
Status: OK

###### <span id="publish-pep-event-200-schema"></span> Schema

##### <span id="publish-pep-event-400"></span> 400 - The payload could not be published
Status: Bad Request

###### <span id="publish-pep-event-400-schema"></span> Schema

### <span id="publish-pub-sub-event"></span> Publish to pubsub node (*publishPubSubEvent*)

```
POST /sparkweb/api/rest/pubsub/publish/{node}
```

This endpoint is used to publish a JSON payload to pubsub node

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| node | `path` | string | `string` |  | ✓ |  | The pubsub node to be published to |
| body | `body` | string | `string` | | ✓ | | The JSON payload to be published |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#publish-pub-sub-event-200) | OK | The payload was published |  | [schema](#publish-pub-sub-event-200-schema) |
| [400](#publish-pub-sub-event-400) | Bad Request | The payload could not be published |  | [schema](#publish-pub-sub-event-400-schema) |

#### Responses


##### <span id="publish-pub-sub-event-200"></span> 200 - The payload was published
Status: OK

###### <span id="publish-pub-sub-event-200-schema"></span> Schema

##### <span id="publish-pub-sub-event-400"></span> 400 - The payload could not be published
Status: Bad Request

###### <span id="publish-pub-sub-event-400-schema"></span> Schema

### <span id="register-user"></span> Register a new user with username/password (*registerUser*)

```
POST /sparkweb/api/rest/register/{username}
```

This endpoint is used to register a new user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| username | `path` | string | `string` |  | ✓ |  | A valid Openfire username |
| body | `body` | string | `string` | | ✓ | | The password for the user |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#register-user-200) | OK | The authentication token |  | [schema](#register-user-200-schema) |
| [500](#register-user-500) | Internal Server Error | Authentication failed |  | [schema](#register-user-500-schema) |

#### Responses


##### <span id="register-user-200"></span> 200 - The authentication token
Status: OK

###### <span id="register-user-200-schema"></span> Schema
   
  

[TokenEntity](#token-entity)

##### <span id="register-user-500"></span> 500 - Authentication failed
Status: Internal Server Error

###### <span id="register-user-500-schema"></span> Schema

### <span id="sse-listener"></span> Server Sent Events (EventSource) (*sseListener*)

```
GET /sparkweb/api/sse
```

# Server Sent Events (EventSource)
 These are the different types of events that can be sent by the server:

 ## chatapi.xmpp - raw xmpp messages
 The XML is encoded as a base64 string

 ```JSON
 {
     "xmpp": "PG1lc3NhZ2UgeG1sbnM9Imp.....mxkPC9ib2R5PjwvbWVzc2FnZT4="
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
 This is for documentation only. This SSE endpoint should be used in JavaScript as follows :

 ```js 
 const source = new EventSource('./sse?token=' + token);

 source.onerror = async event => { 

	 };

 source.addEventListener('chatapi.chat', async event => {

 });	
 ``` 
                       

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| token | `query` | string | `string` |  |  |  | JWT Token issued by WebAuthn |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#sse-listener-200) | OK | SSE connected |  | [schema](#sse-listener-200-schema) |
| [500](#sse-listener-500) | Internal Server Error | SSE failed to connect |  | [schema](#sse-listener-500-schema) |

#### Responses


##### <span id="sse-listener-200"></span> 200 - SSE connected
Status: OK

###### <span id="sse-listener-200-schema"></span> Schema

##### <span id="sse-listener-500"></span> 500 - SSE failed to connect
Status: Internal Server Error

###### <span id="sse-listener-500-schema"></span> Schema

### <span id="subscribe-pub-sub-event"></span> Subscribe to pubsub node (*subscribePubSubEvent*)

```
POST /sparkweb/api/rest/pubsub/subscribe/{node}
```

This endpoint is used to subscribe to a pubsub node

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| node | `path` | string | `string` |  | ✓ |  | The pubsub node to be published |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#subscribe-pub-sub-event-200) | OK | The subscription was made |  | [schema](#subscribe-pub-sub-event-200-schema) |
| [400](#subscribe-pub-sub-event-400) | Bad Request | The subscription fails |  | [schema](#subscribe-pub-sub-event-400-schema) |

#### Responses


##### <span id="subscribe-pub-sub-event-200"></span> 200 - The subscription was made
Status: OK

###### <span id="subscribe-pub-sub-event-200-schema"></span> Schema

##### <span id="subscribe-pub-sub-event-400"></span> 400 - The subscription fails
Status: Bad Request

###### <span id="subscribe-pub-sub-event-400-schema"></span> Schema

### <span id="update-bookmark"></span> Update a specific bookmark (*updateBookmark*)

```
PUT /sparkweb/api/rest/bookmark/{bookmarkID}
```

This endpoint is used to update a specific bookmark

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| bookmarkID | `path` | string | `string` |  | ✓ |  |  |
| body | `body` | [Bookmark](#bookmark) | `models.Bookmark` | |  | |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#update-bookmark-200) | OK | Bookmark was updated |  | [schema](#update-bookmark-200-schema) |
| [400](#update-bookmark-400) | Bad Request | Bookmark could not be updated |  | [schema](#update-bookmark-400-schema) |

#### Responses


##### <span id="update-bookmark-200"></span> 200 - Bookmark was updated
Status: OK

###### <span id="update-bookmark-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

##### <span id="update-bookmark-400"></span> 400 - Bookmark could not be updated
Status: Bad Request

###### <span id="update-bookmark-400-schema"></span> Schema

### <span id="update-bookmark-property"></span> Create/Update a bookmark property (*updateBookmarkProperty*)

```
POST /sparkweb/api/rest/bookmark/{bookmarkID}/{name}
```

This endpoint is used to create or update a bookmark property value

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| bookmarkID | `path` | string | `string` |  | ✓ |  |  |
| name | `path` | string | `string` |  | ✓ |  |  |
| body | `body` | string | `string` | |  | |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#update-bookmark-property-200) | OK | Bookmark was updated |  | [schema](#update-bookmark-property-200-schema) |
| [400](#update-bookmark-property-400) | Bad Request | Bookmark could not be updated |  | [schema](#update-bookmark-property-400-schema) |

#### Responses


##### <span id="update-bookmark-property-200"></span> 200 - Bookmark was updated
Status: OK

###### <span id="update-bookmark-property-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

##### <span id="update-bookmark-property-400"></span> 400 - Bookmark could not be updated
Status: Bad Request

###### <span id="update-bookmark-property-400-schema"></span> Schema

### <span id="update-roster-item"></span> Update roster entry (*updateRosterItem*)

```
PUT /sparkweb/api/rest/roster/{jid}
```

Update a roster entry to the roster (buddies / contact list) of a particular user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| jid | `path` | string | `string` |  | ✓ |  | The JID of the entry/contact to remove |
| body | `body` | [RosterItemEntity](#roster-item-entity) | `models.RosterItemEntity` | | ✓ | | The definition of the roster entry that is to be updated |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#update-roster-item-200) | OK | The entry was updated in the roster |  | [schema](#update-roster-item-200-schema) |
| [400](#update-roster-item-400) | Bad Request | A roster entry cannot be updated with a 'shared group' |  | [schema](#update-roster-item-400-schema) |
| [404](#update-roster-item-404) | Not Found | No user of with this username exists |  | [schema](#update-roster-item-404-schema) |

#### Responses


##### <span id="update-roster-item-200"></span> 200 - The entry was updated in the roster
Status: OK

###### <span id="update-roster-item-200-schema"></span> Schema

##### <span id="update-roster-item-400"></span> 400 - A roster entry cannot be updated with a 'shared group'
Status: Bad Request

###### <span id="update-roster-item-400-schema"></span> Schema

##### <span id="update-roster-item-404"></span> 404 - No user of with this username exists
Status: Not Found

###### <span id="update-roster-item-404-schema"></span> Schema

### <span id="update-user"></span> Update authenticated user (*updateUser*)

```
PUT /sparkweb/api/rest/user
```

Update the authenticated user in Openfire

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [UserEntity](#user-entity) | `models.UserEntity` | | ✓ | | The definition of the authenticated user to update |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#update-user-200) | OK | The Openfire user was updated |  | [schema](#update-user-200-schema) |
| [404](#update-user-404) | Not Found | No user with that username was found |  | [schema](#update-user-404-schema) |

#### Responses


##### <span id="update-user-200"></span> 200 - The Openfire user was updated
Status: OK

###### <span id="update-user-200-schema"></span> Schema

##### <span id="update-user-404"></span> 404 - No user with that username was found
Status: Not Found

###### <span id="update-user-404-schema"></span> Schema

### <span id="upload-request"></span> Request file upload (*uploadRequest*)

```
GET /sparkweb/api/rest/upload/{fileName}/{fileSize}
```

Request for GET and PUT URLs needed to upload and share a file with other users

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| fileName | `path` | string | `string` |  | ✓ |  | The file name to be upload |
| fileSize | `path` | string | `string` |  | ✓ |  | The size of the file to be uploaded |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#upload-request-200) | OK | The request was accepted |  | [schema](#upload-request-200-schema) |
| [400](#upload-request-400) | Bad Request | The upload request failed |  | [schema](#upload-request-400-schema) |

#### Responses


##### <span id="upload-request-200"></span> 200 - The request was accepted
Status: OK

###### <span id="upload-request-200-schema"></span> Schema

##### <span id="upload-request-400"></span> 400 - The upload request failed
Status: Bad Request

###### <span id="upload-request-400-schema"></span> Schema

### <span id="webauthn-authenticate-finish"></span> Finish process to authenticate a user with WebAuthn (*webauthnAuthenticateFinish*)

```
POST /sparkweb/api/rest/webauthn/authenticate/finish/{username}
```

This endpoint is used to finish the webauthn authentication proces

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| username | `path` | string | `string` |  | ✓ |  | A valid Openfire username |
| body | `body` | string | `string` | | ✓ | | The assertion generated by the web client |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#webauthn-authenticate-finish-200) | OK | The authentication token |  | [schema](#webauthn-authenticate-finish-200-schema) |
| [500](#webauthn-authenticate-finish-500) | Internal Server Error | Authentication failed |  | [schema](#webauthn-authenticate-finish-500-schema) |

#### Responses


##### <span id="webauthn-authenticate-finish-200"></span> 200 - The authentication token
Status: OK

###### <span id="webauthn-authenticate-finish-200-schema"></span> Schema
   
  

[TokenEntity](#token-entity)

##### <span id="webauthn-authenticate-finish-500"></span> 500 - Authentication failed
Status: Internal Server Error

###### <span id="webauthn-authenticate-finish-500-schema"></span> Schema

### <span id="webauthn-authenticate-start"></span> Start process to authenticate a user with WebAuthn (*webauthnAuthenticateStart*)

```
POST /sparkweb/api/rest/webauthn/authenticate/start/{username}
```

This endpoint is used to start the webauthn authentication proces

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| username | `path` | string | `string` |  | ✓ |  | A valid Openfire username |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#webauthn-authenticate-start-200) | OK | WebAuthn data to start authentication |  | [schema](#webauthn-authenticate-start-200-schema) |
| [500](#webauthn-authenticate-start-500) | Internal Server Error | Authentication failed |  | [schema](#webauthn-authenticate-start-500-schema) |

#### Responses


##### <span id="webauthn-authenticate-start-200"></span> 200 - WebAuthn data to start authentication
Status: OK

###### <span id="webauthn-authenticate-start-200-schema"></span> Schema

##### <span id="webauthn-authenticate-start-500"></span> 500 - Authentication failed
Status: Internal Server Error

###### <span id="webauthn-authenticate-start-500-schema"></span> Schema

### <span id="webauthn-register-finish"></span> Finish process to register a user for WebAuthn (*webauthnRegisterFinish*)

```
POST /sparkweb/api/rest/webauthn/register/finish/{username}
```

This endpoint is used to finish the webauthn registration proces

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| username | `path` | string | `string` |  | ✓ |  | A valid Openfire username |
| body | `body` | string | `string` | | ✓ | | The credentials generated by the web client |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#webauthn-register-finish-200) | OK | The authentication token |  | [schema](#webauthn-register-finish-200-schema) |
| [500](#webauthn-register-finish-500) | Internal Server Error | Authentication failed |  | [schema](#webauthn-register-finish-500-schema) |

#### Responses


##### <span id="webauthn-register-finish-200"></span> 200 - The authentication token
Status: OK

###### <span id="webauthn-register-finish-200-schema"></span> Schema
   
  

[TokenEntity](#token-entity)

##### <span id="webauthn-register-finish-500"></span> 500 - Authentication failed
Status: Internal Server Error

###### <span id="webauthn-register-finish-500-schema"></span> Schema

### <span id="webauthn-register-start"></span> Start process to register a user for WebAuthn (*webauthnRegisterStart*)

```
POST /sparkweb/api/rest/webauthn/register/start/{username}
```

This endpoint is used to start the webauthn registration proces

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| username | `path` | string | `string` |  | ✓ |  | A valid Openfire username |
| body | `body` | string | `string` | | ✓ | | The current Openfire password for the user |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#webauthn-register-start-200) | OK | WebAuthn data to registration |  | [schema](#webauthn-register-start-200-schema) |
| [500](#webauthn-register-start-500) | Internal Server Error | Authentication failed |  | [schema](#webauthn-register-start-500-schema) |

#### Responses


##### <span id="webauthn-register-start-200"></span> 200 - WebAuthn data to registration
Status: OK

###### <span id="webauthn-register-start-200-schema"></span> Schema

##### <span id="webauthn-register-start-500"></span> 500 - Authentication failed
Status: Internal Server Error

###### <span id="webauthn-register-start-500-schema"></span> Schema

## Models

### <span id="archived-message"></span> ArchivedMessage


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| body | string| `string` |  | |  |  |
| conversationID | int64 (formatted integer)| `int64` |  | |  |  |
| from | string| `string` |  | |  |  |
| fromJID | [JID](#j-id)| `JID` |  | |  |  |
| roomEvent | boolean| `bool` |  | |  |  |
| sentDate | date-time (formatted string)| `strfmt.DateTime` |  | |  |  |
| stanza | string| `string` |  | |  |  |
| to | string| `string` |  | |  |  |
| toJID | [JID](#j-id)| `JID` |  | |  |  |



### <span id="bookmark"></span> Bookmark


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| bookmarkID | int64 (formatted integer)| `int64` |  | |  |  |
| globalBookmark | boolean| `bool` |  | |  |  |
| groups | []string| `[]string` |  | |  |  |
| name | string| `string` |  | |  |  |
| properties | [][UserProperty](#user-property)| `[]*UserProperty` |  | |  |  |
| propertyNames | [IteratorString](#iterator-string)| `IteratorString` |  | |  |  |
| type | string| `string` |  | |  |  |
| users | []string| `[]string` |  | |  |  |
| value | string| `string` |  | |  |  |



### <span id="bookmarks"></span> Bookmarks


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| bookmarks | [][Bookmark](#bookmark)| `[]*Bookmark` |  | |  |  |



### <span id="conversation"></span> Conversation


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| chatRoom | string| `string` |  | |  |  |
| conversationID | int64 (formatted integer)| `int64` |  | |  |  |
| conversationParticipants | []string| `[]string` |  | |  |  |
| external | boolean| `bool` |  | |  |  |
| lastActivity | date-time (formatted string)| `strfmt.DateTime` |  | |  |  |
| messageCount | int32 (formatted integer)| `int32` |  | |  |  |
| messages | [][ArchivedMessage](#archived-message)| `[]*ArchivedMessage` |  | |  |  |
| participantList | []string| `[]string` |  | |  |  |
| participants | [][JID](#j-id)| `[]*JID` |  | |  |  |
| room | [JID](#j-id)| `JID` |  | |  |  |
| startDate | date-time (formatted string)| `strfmt.DateTime` |  | |  |  |



### <span id="conversations"></span> Conversations


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| conversations | [][Conversation](#conversation)| `[]*Conversation` |  | |  |  |



### <span id="iterator"></span> Iterator


  

[interface{}](#interface)

### <span id="iterator-string"></span> IteratorString


  

[interface{}](#interface)

### <span id="j-id"></span> JID


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| domain | string| `string` |  | |  |  |
| node | string| `string` |  | |  |  |
| resource | string| `string` |  | |  |  |



### <span id="m-u-c-room-entities"></span> MUCRoomEntities


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| chatRooms | [][MUCRoomEntity](#m-u-c-room-entity)| `[]*MUCRoomEntity` |  | |  |  |



### <span id="m-u-c-room-entity"></span> MUCRoomEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| adminGroups | []string| `[]string` |  | |  |  |
| admins | []string| `[]string` |  | |  |  |
| allowPM | string| `string` |  | |  |  |
| broadcastPresenceRoles | []string| `[]string` |  | |  |  |
| canAnyoneDiscoverJID | boolean| `bool` |  | |  |  |
| canChangeNickname | boolean| `bool` |  | |  |  |
| canOccupantsChangeSubject | boolean| `bool` |  | |  |  |
| canOccupantsInvite | boolean| `bool` |  | |  |  |
| creationDate | date-time (formatted string)| `strfmt.DateTime` |  | |  |  |
| description | string| `string` |  | |  |  |
| logEnabled | boolean| `bool` |  | |  |  |
| loginRestrictedToNickname | boolean| `bool` |  | |  |  |
| maxUsers | int32 (formatted integer)| `int32` |  | |  |  |
| memberGroups | []string| `[]string` |  | |  |  |
| members | []string| `[]string` |  | |  |  |
| membersOnly | boolean| `bool` |  | |  |  |
| moderated | boolean| `bool` |  | |  |  |
| modificationDate | date-time (formatted string)| `strfmt.DateTime` |  | |  |  |
| naturalName | string| `string` |  | |  |  |
| outcastGroups | []string| `[]string` |  | |  |  |
| outcasts | []string| `[]string` |  | |  |  |
| ownerGroups | []string| `[]string` |  | |  |  |
| owners | []string| `[]string` |  | |  |  |
| password | string| `string` |  | |  |  |
| persistent | boolean| `bool` |  | |  |  |
| publicRoom | boolean| `bool` |  | |  |  |
| registrationEnabled | boolean| `bool` |  | |  |  |
| roomName | string| `string` |  | |  |  |
| subject | string| `string` |  | |  |  |



### <span id="m-u-c-room-message-entities"></span> MUCRoomMessageEntities


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| messages | [][MUCRoomMessageEntity](#m-u-c-room-message-entity)| `[]*MUCRoomMessageEntity` |  | |  |  |



### <span id="m-u-c-room-message-entity"></span> MUCRoomMessageEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| body | string| `string` |  | |  |  |
| delayFrom | string| `string` |  | |  |  |
| delayStamp | string| `string` |  | |  |  |
| from | string| `string` |  | |  |  |
| to | string| `string` |  | |  |  |
| type | string| `string` |  | |  |  |



### <span id="node-entities"></span> NodeEntities


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| nodes | [][NodeEntity](#node-entity)| `[]*NodeEntity` |  | |  |  |



### <span id="node-entity"></span> NodeEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| description | string| `string` |  | |  |  |
| id | string| `string` |  | |  |  |
| name | string| `string` |  | |  |  |
| owners | []string| `[]string` |  | |  |  |



### <span id="notification-action-entity"></span> NotificationActionEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| action | string| `string` |  | |  |  |
| data | string| `string` |  | |  |  |
| icon | string| `string` |  | |  |  |
| placeholder | string| `string` |  | |  |  |
| title | string| `string` |  | |  |  |
| type | string| `string` |  | |  |  |
| value | string| `string` |  | |  |  |



### <span id="notification-entity"></span> NotificationEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| actions | [][NotificationActionEntity](#notification-action-entity)| `[]*NotificationActionEntity` |  | |  |  |
| body | string| `string` |  | |  |  |
| data | string| `string` |  | |  |  |
| icon | string| `string` |  | |  |  |
| persistent | boolean| `bool` |  | |  |  |
| requireInteraction | boolean| `bool` |  | |  |  |
| sticky | boolean| `bool` |  | |  |  |
| subject | string| `string` |  | |  |  |
| token | string| `string` |  | |  |  |



### <span id="occupant-entities"></span> OccupantEntities


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| occupants | [][OccupantEntity](#occupant-entity)| `[]*OccupantEntity` |  | |  |  |



### <span id="occupant-entity"></span> OccupantEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| affiliation | string| `string` |  | |  |  |
| jid | string| `string` |  | |  |  |
| role | string| `string` |  | |  |  |
| userAddress | string| `string` |  | |  |  |



### <span id="online-meeting-entity"></span> OnlineMeetingEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| url | string| `string` |  | |  |  |



### <span id="participant-entities"></span> ParticipantEntities


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| participants | [][ParticipantEntity](#participant-entity)| `[]*ParticipantEntity` |  | |  |  |



### <span id="participant-entity"></span> ParticipantEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| affiliation | string| `string` |  | |  |  |
| jid | string| `string` |  | |  |  |
| role | string| `string` |  | |  |  |



### <span id="presence-entity"></span> PresenceEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| show | string| `string` |  | |  |  |
| status | string| `string` |  | |  |  |
| username | string| `string` |  | |  |  |



### <span id="public-key"></span> PublicKey


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| publicKey | string| `string` |  | |  |  |



### <span id="roster-entities"></span> RosterEntities


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| roster | [][RosterItemEntity](#roster-item-entity)| `[]*RosterItemEntity` |  | |  |  |



### <span id="roster-item-entity"></span> RosterItemEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| groups | []string| `[]string` |  | |  |  |
| jid | string| `string` |  | |  |  |
| nickname | string| `string` |  | |  |  |
| show | string| `string` |  | |  |  |
| status | string| `string` |  | |  |  |
| subscriptionType | int32 (formatted integer)| `int32` |  | |  |  |



### <span id="t-o-t-p-entity"></span> TOTPEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| url | string| `string` |  | |  |  |



### <span id="token-entity"></span> TokenEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| token | string| `string` |  | |  |  |



### <span id="user-entities"></span> UserEntities


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| users | [][UserEntity](#user-entity)| `[]*UserEntity` |  | |  |  |



### <span id="user-entity"></span> UserEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| email | string| `string` |  | |  |  |
| name | string| `string` |  | |  |  |
| password | string| `string` |  | |  |  |
| properties | [][UserProperty](#user-property)| `[]*UserProperty` |  | |  |  |
| username | string| `string` |  | |  |  |
| vcard | string| `string` |  | |  |  |



### <span id="user-groups-entity"></span> UserGroupsEntity


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| groupnames | []string| `[]string` |  | |  |  |



### <span id="user-property"></span> UserProperty


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| key | string| `string` |  | |  |  |
| value | string| `string` |  | |  |  |


