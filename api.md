


SparkWeb REST API adds support for a whole range of modern web service connections to Openfire/XMPP
  

## Informations

### Version

0.0.1-SNAPSHOT

## Tags

  ### <span id="tag-user-management"></span>User Management

provide user services for the authenticated user.

  ### <span id="tag-contact-management"></span>Contact Management

provide user roster services to manage contacts

  ### <span id="tag-presence"></span>Presence

Perform XMPP Prsence functions

  ### <span id="tag-chat"></span>Chat

Perform XMPP Chat functions

  ### <span id="tag-web-authentication"></span>Web Authentication

provide server-side Web Authentication services

  ### <span id="tag-web-push"></span>Web Push

provide server-side Web Push services

  ### <span id="tag-global-and-user-properties"></span>Global and User Properties

Access global and user properties

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

###  bookmarks

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/bookmark | [create bookmark](#create-bookmark) | Bookmark API - Create a new bookmark |
| DELETE | /sparkweb/api/rest/bookmark/{bookmarkID} | [delete bookmark](#delete-bookmark) | Bookmark API - Delete a specific bookmark |
| DELETE | /sparkweb/api/rest/bookmark/{bookmarkID}/{name} | [delete bookmark property](#delete-bookmark-property) | Bookmark API - Delete a bookmark property |
| GET | /sparkweb/api/rest/bookmark/{bookmarkID} | [get bookmark](#get-bookmark) | Bookmark API - Get a specific bookmark |
| GET | /sparkweb/api/rest/bookmarks | [get bookmarks](#get-bookmarks) | Bookmark API - Get all bookmark |
| PUT | /sparkweb/api/rest/bookmark/{bookmarkID} | [update bookmark](#update-bookmark) | Bookmark API - Update a specific bookmark |
| POST | /sparkweb/api/rest/bookmark/{bookmarkID}/{name} | [update bookmark property](#update-bookmark-property) | Bookmark API - Create/Update a bookmark property |
  


###  contact_management

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/roster | [create roster](#create-roster) | Create roster entry |
| DELETE | /sparkweb/api/rest/roster/{jid} | [delete roster item](#delete-roster-item) | Remove roster entry |
| GET | /sparkweb/api/rest/roster | [get user roster](#get-user-roster) | Retrieve user roster |
| PUT | /sparkweb/api/rest/roster/{jid} | [update roster item](#update-roster-item) | Update roster entry |
  


###  global_and_user_properties

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| GET | /sparkweb/api/rest/config/global | [get global config](#get-global-config) | Global and User Properties - List global properties affecting this user |
| GET | /sparkweb/api/rest/config/properties | [get user config](#get-user-config) | Global and User Properties - List User Properties |
| POST | /sparkweb/api/rest/config/properties | [post user config](#post-user-config) | Global and User Properties - Update user properties |
  


###  presence

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| GET | /sparkweb/api/rest/presence/{userid} | [get presence](#get-presence) | Presence - Query presence of a Teams user |
| POST | /sparkweb/api/rest/presence | [post presence](#post-presence) | Presence - Store the presence of a user |
  


###  user_management

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/user/groups/{groupName} | [add user to group](#add-user-to-group) | Add user to a group |
| POST | /sparkweb/api/rest/user/groups | [add user to groups](#add-user-to-groups) | Add user to groups |
| DELETE | /sparkweb/api/rest/user | [delete user](#delete-user) | Delete authenticated user |
| DELETE | /sparkweb/api/rest/user/groups/{groupName} | [delete user from group](#delete-user-from-group) | Delete user from group |
| DELETE | /sparkweb/api/rest/user/groups | [delete user from groups](#delete-user-from-groups) | Delete user from groups |
| GET | /sparkweb/api/rest/user | [get user](#get-user) | Get authenticated user |
| GET | /sparkweb/api/rest/user/groups | [get user groups](#get-user-groups) | Get user's groups |
| GET | /sparkweb/api/rest/users | [get users](#get-users) | Get users |
| PUT | /sparkweb/api/rest/user | [update user](#update-user) | Update authenticated user |
  


###  web_authentication

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/webauthn/authenticate/finish/{username} | [webauthn authenticate finish](#webauthn-authenticate-finish) | Web Authentication - End Authentication |
| POST | /sparkweb/api/rest/webauthn/authenticate/start/{username} | [webauthn authenticate start](#webauthn-authenticate-start) | Web Authentication - Start Authentication |
| POST | /sparkweb/api/rest/webauthn/register/finish/{username} | [webauthn register finish](#webauthn-register-finish) | Web Authentication - Finish Registration |
| POST | /sparkweb/api/rest/webauthn/register/start/{username} | [webauthn register start](#webauthn-register-start) | Web Authentication - Start Registration |
  


###  web_push

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| GET | /sparkweb/api/rest/webpush/subscribers | [get push subscribers](#get-push-subscribers) | Web Push - Get all web push subscribers |
| GET | /sparkweb/api/rest/webpush/vapidkey | [get web push public key](#get-web-push-public-key) | Web Push - Get the webpush vapid key |
| POST | /sparkweb/api/rest/webpush/publish/{target} | [post web push](#post-web-push) | Web Push - Send a text message to all subscriptions of another user |
| POST | /sparkweb/api/rest/webpush/subscribe/{resource} | [post web push subscription](#post-web-push-subscription) | Web Push - Store web push subscription for this user |
  


## Paths

### <span id="add-user-to-group"></span> Add user to a group (*addUserToGroup*)

```
POST /sparkweb/api/rest/user/groups/{groupName}
```

Add authenticated user to a collection of groups. When a group that is provided does not exist, it will be automatically created if possible.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| groupName | `path` | string | `string` |  | ✓ |  | The name of the group that the user is to be added to. |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#add-user-to-group-200) | OK | The user was added to all groups. |  | [schema](#add-user-to-group-200-schema) |
| [400](#add-user-to-group-400) | Bad Request | The group could not be found. |  | [schema](#add-user-to-group-400-schema) |

#### Responses


##### <span id="add-user-to-group-200"></span> 200 - The user was added to all groups.
Status: OK

###### <span id="add-user-to-group-200-schema"></span> Schema

##### <span id="add-user-to-group-400"></span> 400 - The group could not be found.
Status: Bad Request

###### <span id="add-user-to-group-400-schema"></span> Schema

### <span id="add-user-to-groups"></span> Add user to groups (*addUserToGroups*)

```
POST /sparkweb/api/rest/user/groups
```

Add authenticated user to a collection of groups. When a group that is provided does not exist, it will be automatically created if possible.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [UserGroupsEntity](#user-groups-entity) | `models.UserGroupsEntity` | | ✓ | | A collection of names for groups that the user is to be added to. |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#add-user-to-groups-200) | OK | The user was added to all groups. |  | [schema](#add-user-to-groups-200-schema) |
| [404](#add-user-to-groups-404) | Not Found | One or more groups could not be found. |  | [schema](#add-user-to-groups-404-schema) |

#### Responses


##### <span id="add-user-to-groups-200"></span> 200 - The user was added to all groups.
Status: OK

###### <span id="add-user-to-groups-200-schema"></span> Schema

##### <span id="add-user-to-groups-404"></span> 404 - One or more groups could not be found.
Status: Not Found

###### <span id="add-user-to-groups-404-schema"></span> Schema

### <span id="create-bookmark"></span> Bookmark API - Create a new bookmark (*createBookmark*)

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
| body | `body` | [Bookmark](#bookmark) | `models.Bookmark` | |  | |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#create-bookmark-200) | OK | successful operation |  | [schema](#create-bookmark-200-schema) |

#### Responses


##### <span id="create-bookmark-200"></span> 200 - successful operation
Status: OK

###### <span id="create-bookmark-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

### <span id="create-roster"></span> Create roster entry (*createRoster*)

```
POST /sparkweb/api/rest/roster
```

Add a roster entry to the roster (buddies / contact list) of a particular user.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [RosterItemEntity](#roster-item-entity) | `models.RosterItemEntity` | | ✓ | | The definition of the roster entry that is to be added. |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#create-roster-200) | OK | The entry was added to the roster. |  | [schema](#create-roster-200-schema) |
| [400](#create-roster-400) | Bad Request | A roster entry cannot be added to a 'shared group' (try removing group names from the roster entry and try again). |  | [schema](#create-roster-400-schema) |
| [404](#create-roster-404) | Not Found | No user of with this username exists. |  | [schema](#create-roster-404-schema) |
| [409](#create-roster-409) | Conflict | A roster entry already exists for the provided contact JID. |  | [schema](#create-roster-409-schema) |

#### Responses


##### <span id="create-roster-200"></span> 200 - The entry was added to the roster.
Status: OK

###### <span id="create-roster-200-schema"></span> Schema

##### <span id="create-roster-400"></span> 400 - A roster entry cannot be added to a 'shared group' (try removing group names from the roster entry and try again).
Status: Bad Request

###### <span id="create-roster-400-schema"></span> Schema

##### <span id="create-roster-404"></span> 404 - No user of with this username exists.
Status: Not Found

###### <span id="create-roster-404-schema"></span> Schema

##### <span id="create-roster-409"></span> 409 - A roster entry already exists for the provided contact JID.
Status: Conflict

###### <span id="create-roster-409-schema"></span> Schema

### <span id="delete-bookmark"></span> Bookmark API - Delete a specific bookmark (*deleteBookmark*)

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

### <span id="delete-bookmark-property"></span> Bookmark API - Delete a bookmark property (*deleteBookmarkProperty*)

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
| [200](#delete-bookmark-property-200) | OK | successful operation |  | [schema](#delete-bookmark-property-200-schema) |

#### Responses


##### <span id="delete-bookmark-property-200"></span> 200 - successful operation
Status: OK

###### <span id="delete-bookmark-property-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

### <span id="delete-roster-item"></span> Remove roster entry (*deleteRosterItem*)

```
DELETE /sparkweb/api/rest/roster/{jid}
```

Removes one of the roster entries (contacts) of the authenticated user.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| jid | `path` | string | `string` |  | ✓ |  | The JID of the entry/contact to remove. |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-roster-item-200) | OK | Roster entry removed |  | [schema](#delete-roster-item-200-schema) |
| [400](#delete-roster-item-400) | Bad Request | A roster entry cannot be removed from a 'shared group'. |  | [schema](#delete-roster-item-400-schema) |
| [404](#delete-roster-item-404) | Not Found | No user of with this username exists, or its roster did not contain this entry. |  | [schema](#delete-roster-item-404-schema) |

#### Responses


##### <span id="delete-roster-item-200"></span> 200 - Roster entry removed
Status: OK

###### <span id="delete-roster-item-200-schema"></span> Schema

##### <span id="delete-roster-item-400"></span> 400 - A roster entry cannot be removed from a 'shared group'.
Status: Bad Request

###### <span id="delete-roster-item-400-schema"></span> Schema

##### <span id="delete-roster-item-404"></span> 404 - No user of with this username exists, or its roster did not contain this entry.
Status: Not Found

###### <span id="delete-roster-item-404-schema"></span> Schema

### <span id="delete-user"></span> Delete authenticated user (*deleteUser*)

```
DELETE /sparkweb/api/rest/user
```

Delete authenticated user in Openfire.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-user-200) | OK | The Openfire user was deleted. |  | [schema](#delete-user-200-schema) |
| [404](#delete-user-404) | Not Found | No user with that username was found. |  | [schema](#delete-user-404-schema) |

#### Responses


##### <span id="delete-user-200"></span> 200 - The Openfire user was deleted.
Status: OK

###### <span id="delete-user-200-schema"></span> Schema

##### <span id="delete-user-404"></span> 404 - No user with that username was found.
Status: Not Found

###### <span id="delete-user-404-schema"></span> Schema

### <span id="delete-user-from-group"></span> Delete user from group (*deleteUserFromGroup*)

```
DELETE /sparkweb/api/rest/user/groups/{groupName}
```

Removes a user from a group.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| groupName | `path` | string | `string` |  | ✓ |  | The name of the group that the user is to be added to. |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-user-from-group-200) | OK | The user was taken out of the group. |  | [schema](#delete-user-from-group-200-schema) |
| [400](#delete-user-from-group-400) | Bad Request | The group could not be found. |  | [schema](#delete-user-from-group-400-schema) |

#### Responses


##### <span id="delete-user-from-group-200"></span> 200 - The user was taken out of the group.
Status: OK

###### <span id="delete-user-from-group-200-schema"></span> Schema

##### <span id="delete-user-from-group-400"></span> 400 - The group could not be found.
Status: Bad Request

###### <span id="delete-user-from-group-400-schema"></span> Schema

### <span id="delete-user-from-groups"></span> Delete user from groups (*deleteUserFromGroups*)

```
DELETE /sparkweb/api/rest/user/groups
```

Removes a user from a collection of groups..

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [UserGroupsEntity](#user-groups-entity) | `models.UserGroupsEntity` | | ✓ | | A collection of names for groups that the user is to be added to. |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#delete-user-from-groups-200) | OK | The user was taken out of the group. |  | [schema](#delete-user-from-groups-200-schema) |
| [404](#delete-user-from-groups-404) | Not Found | One or more groups could not be found. |  | [schema](#delete-user-from-groups-404-schema) |

#### Responses


##### <span id="delete-user-from-groups-200"></span> 200 - The user was taken out of the group.
Status: OK

###### <span id="delete-user-from-groups-200-schema"></span> Schema

##### <span id="delete-user-from-groups-404"></span> 404 - One or more groups could not be found.
Status: Not Found

###### <span id="delete-user-from-groups-404-schema"></span> Schema

### <span id="get-bookmark"></span> Bookmark API - Get a specific bookmark (*getBookmark*)

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
| [200](#get-bookmark-200) | OK | successful operation |  | [schema](#get-bookmark-200-schema) |

#### Responses


##### <span id="get-bookmark-200"></span> 200 - successful operation
Status: OK

###### <span id="get-bookmark-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

### <span id="get-bookmarks"></span> Bookmark API - Get all bookmark (*getBookmarks*)

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
| [200](#get-bookmarks-200) | OK | successful operation |  | [schema](#get-bookmarks-200-schema) |

#### Responses


##### <span id="get-bookmarks-200"></span> 200 - successful operation
Status: OK

###### <span id="get-bookmarks-200-schema"></span> Schema
   
  

[Bookmarks](#bookmarks)

### <span id="get-global-config"></span> Global and User Properties - List global properties affecting this user (*getGlobalConfig*)

```
GET /sparkweb/api/rest/config/global
```

Endpoint will retrieve all Openfire Global properties that are used by this authenticated user.

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
   
  



### <span id="get-presence"></span> Presence - Query presence of a Teams user (*getPresence*)

```
GET /sparkweb/api/rest/presence/{userid}
```

Endpoint to retrieve a the presence of a specific user.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| userid | `path` | string | `string` |  | ✓ |  |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-presence-200) | OK | successful operation |  | [schema](#get-presence-200-schema) |

#### Responses


##### <span id="get-presence-200"></span> 200 - successful operation
Status: OK

###### <span id="get-presence-200-schema"></span> Schema
   
  



### <span id="get-push-subscribers"></span> Web Push - Get all web push subscribers (*getPushSubscribers*)

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

### <span id="get-user"></span> Get authenticated user (*getUser*)

```
GET /sparkweb/api/rest/user
```

Retrieve a user that is defined in Openfire.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-user-200) | OK | The Openfire user was retrieved. |  | [schema](#get-user-200-schema) |
| [404](#get-user-404) | Not Found | No user with that username was found. |  | [schema](#get-user-404-schema) |

#### Responses


##### <span id="get-user-200"></span> 200 - The Openfire user was retrieved.
Status: OK

###### <span id="get-user-200-schema"></span> Schema

##### <span id="get-user-404"></span> 404 - No user with that username was found.
Status: Not Found

###### <span id="get-user-404-schema"></span> Schema

### <span id="get-user-config"></span> Global and User Properties - List User Properties (*getUserConfig*)

```
GET /sparkweb/api/rest/config/properties
```

Endpoint to retrieve a list of all config properties for the authenticated user.

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

Retrieve names of all groups that a particular user is in.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-user-groups-200) | OK | The names of the groups that the user is in. |  | [schema](#get-user-groups-200-schema) |

#### Responses


##### <span id="get-user-groups-200"></span> 200 - The names of the groups that the user is in.
Status: OK

###### <span id="get-user-groups-200-schema"></span> Schema

### <span id="get-user-roster"></span> Retrieve user roster (*getUserRoster*)

```
GET /sparkweb/api/rest/roster
```

Get a list of all roster entries (buddies / contact list) of a authenticated user.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-user-roster-200) | OK | All roster entries |  | [schema](#get-user-roster-200-schema) |
| [404](#get-user-roster-404) | Not Found | No user with that username was found. |  | [schema](#get-user-roster-404-schema) |

#### Responses


##### <span id="get-user-roster-200"></span> 200 - All roster entries
Status: OK

###### <span id="get-user-roster-200-schema"></span> Schema

##### <span id="get-user-roster-404"></span> 404 - No user with that username was found.
Status: Not Found

###### <span id="get-user-roster-404-schema"></span> Schema

### <span id="get-users"></span> Get users (*getUsers*)

```
GET /sparkweb/api/rest/users
```

Retrieve all users defined in Openfire (with optional filtering).

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| propertyKey | `query` | string | `string` |  |  |  | Filter by a user property name. |
| propertyValue | `query` | string | `string` |  |  |  | Filter by user property value. Note: This can only be used in combination with a property name parameter |
| search | `query` | string | `string` |  |  |  | Search/Filter by username. This act like the wildcard search %String% |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#get-users-200) | OK | A list of Openfire users. |  | [schema](#get-users-200-schema) |

#### Responses


##### <span id="get-users-200"></span> 200 - A list of Openfire users.
Status: OK

###### <span id="get-users-200-schema"></span> Schema

### <span id="get-web-push-public-key"></span> Web Push - Get the webpush vapid key (*getWebPushPublicKey*)

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

### <span id="post-presence"></span> Presence - Store the presence of a user (*postPresence*)

```
POST /sparkweb/api/rest/presence
```

This endpoint is used to store the presence of a user

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | string | `string` | |  | |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [default](#post-presence-default) | | successful operation |  | [schema](#post-presence-default-schema) |

#### Responses


##### <span id="post-presence-default"></span> Default Response
successful operation

###### <span id="post-presence-default-schema"></span> Schema
empty schema

### <span id="post-user-config"></span> Global and User Properties - Update user properties (*postUserConfig*)

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

### <span id="post-web-push"></span> Web Push - Send a text message to all subscriptions of another user (*postWebPush*)

```
POST /sparkweb/api/rest/webpush/publish/{target}
```

This endpoint is used to push a message with a payload to all subscriptions of the specified user

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
| [default](#post-web-push-default) | | successful operation |  | [schema](#post-web-push-default-schema) |

#### Responses


##### <span id="post-web-push-default"></span> Default Response
successful operation

###### <span id="post-web-push-default-schema"></span> Schema
empty schema

### <span id="post-web-push-subscription"></span> Web Push - Store web push subscription for this user (*postWebPushSubscription*)

```
POST /sparkweb/api/rest/webpush/subscribe/{resource}
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

### <span id="update-bookmark"></span> Bookmark API - Update a specific bookmark (*updateBookmark*)

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
| [200](#update-bookmark-200) | OK | successful operation |  | [schema](#update-bookmark-200-schema) |

#### Responses


##### <span id="update-bookmark-200"></span> 200 - successful operation
Status: OK

###### <span id="update-bookmark-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

### <span id="update-bookmark-property"></span> Bookmark API - Create/Update a bookmark property (*updateBookmarkProperty*)

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
| [200](#update-bookmark-property-200) | OK | successful operation |  | [schema](#update-bookmark-property-200-schema) |

#### Responses


##### <span id="update-bookmark-property-200"></span> 200 - successful operation
Status: OK

###### <span id="update-bookmark-property-200-schema"></span> Schema
   
  

[Bookmark](#bookmark)

### <span id="update-roster-item"></span> Update roster entry (*updateRosterItem*)

```
PUT /sparkweb/api/rest/roster/{jid}
```

Update a roster entry to the roster (buddies / contact list) of a particular user.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| jid | `path` | string | `string` |  | ✓ |  | The JID of the entry/contact to remove. |
| body | `body` | [RosterItemEntity](#roster-item-entity) | `models.RosterItemEntity` | | ✓ | | The definition of the roster entry that is to be updated. |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#update-roster-item-200) | OK | The entry was updated in the roster. |  | [schema](#update-roster-item-200-schema) |
| [400](#update-roster-item-400) | Bad Request | A roster entry cannot be updated with a 'shared group'. |  | [schema](#update-roster-item-400-schema) |
| [404](#update-roster-item-404) | Not Found | No user of with this username exists. |  | [schema](#update-roster-item-404-schema) |

#### Responses


##### <span id="update-roster-item-200"></span> 200 - The entry was updated in the roster.
Status: OK

###### <span id="update-roster-item-200-schema"></span> Schema

##### <span id="update-roster-item-400"></span> 400 - A roster entry cannot be updated with a 'shared group'.
Status: Bad Request

###### <span id="update-roster-item-400-schema"></span> Schema

##### <span id="update-roster-item-404"></span> 404 - No user of with this username exists.
Status: Not Found

###### <span id="update-roster-item-404-schema"></span> Schema

### <span id="update-user"></span> Update authenticated user (*updateUser*)

```
PUT /sparkweb/api/rest/user
```

Update the authenticated user in Openfire.

#### Produces
  * application/json

#### Security Requirements
  * authorization

#### Parameters

| Name | Source | Type | Go type | Separator | Required | Default | Description |
|------|--------|------|---------|-----------| :------: |---------|-------------|
| body | `body` | [UserEntity](#user-entity) | `models.UserEntity` | | ✓ | | The definition of the authenticated user to update. |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [200](#update-user-200) | OK | The Openfire user was updated. |  | [schema](#update-user-200-schema) |
| [404](#update-user-404) | Not Found | No user with that username was found. |  | [schema](#update-user-404-schema) |

#### Responses


##### <span id="update-user-200"></span> 200 - The Openfire user was updated.
Status: OK

###### <span id="update-user-200-schema"></span> Schema

##### <span id="update-user-404"></span> 404 - No user with that username was found.
Status: Not Found

###### <span id="update-user-404-schema"></span> Schema

### <span id="webauthn-authenticate-finish"></span> Web Authentication - End Authentication (*webauthnAuthenticateFinish*)

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
| [200](#webauthn-authenticate-finish-200) | OK | successful operation |  | [schema](#webauthn-authenticate-finish-200-schema) |

#### Responses


##### <span id="webauthn-authenticate-finish-200"></span> 200 - successful operation
Status: OK

###### <span id="webauthn-authenticate-finish-200-schema"></span> Schema
   
  



### <span id="webauthn-authenticate-start"></span> Web Authentication - Start Authentication (*webauthnAuthenticateStart*)

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
| [200](#webauthn-authenticate-start-200) | OK | successful operation |  | [schema](#webauthn-authenticate-start-200-schema) |

#### Responses


##### <span id="webauthn-authenticate-start-200"></span> 200 - successful operation
Status: OK

###### <span id="webauthn-authenticate-start-200-schema"></span> Schema
   
  



### <span id="webauthn-register-finish"></span> Web Authentication - Finish Registration (*webauthnRegisterFinish*)

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
| [200](#webauthn-register-finish-200) | OK | successful operation |  | [schema](#webauthn-register-finish-200-schema) |

#### Responses


##### <span id="webauthn-register-finish-200"></span> 200 - successful operation
Status: OK

###### <span id="webauthn-register-finish-200-schema"></span> Schema
   
  



### <span id="webauthn-register-start"></span> Web Authentication - Start Registration (*webauthnRegisterStart*)

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
| [200](#webauthn-register-start-200) | OK | successful operation |  | [schema](#webauthn-register-start-200-schema) |

#### Responses


##### <span id="webauthn-register-start-200"></span> 200 - successful operation
Status: OK

###### <span id="webauthn-register-start-200-schema"></span> Schema
   
  



## Models

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



### <span id="iterator"></span> Iterator


  

[interface{}](#interface)

### <span id="iterator-string"></span> IteratorString


  

[interface{}](#interface)

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
| subscriptionType | int32 (formatted integer)| `int32` |  | |  |  |



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


