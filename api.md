


SparkWeb REST API adds support for a whole range of modern web service connections to Openfire/XMPP
  

## Informations

### Version

0.0.1-SNAPSHOT

## Tags

  ### <span id="tag-web-authentication"></span>Web Authentication

provide server-side Web Authentication services

  ### <span id="tag-web-push"></span>Web Push

provide server-side Web Push services

  ### <span id="tag-global-and-user-properties"></span>Global and User Properties

Access global and user properties

  ### <span id="tag-presence"></span>Presence

Perform XMPP Prsence functions

  ### <span id="tag-chat"></span>Chat

Perform XMPP Chat functions

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
  


###  chat

| Method  | URI     | Name   | Summary |
|---------|---------|--------|---------|
| POST | /sparkweb/api/rest/chat | [post chat](#post-chat) | Chat - Send ACS chat message to XMPP destination |
  


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
  


## Paths

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
   
  



### <span id="post-chat"></span> Chat - Send ACS chat message to XMPP destination (*postChat*)

```
POST /sparkweb/api/rest/chat
```

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
| [default](#post-chat-default) | | successful operation |  | [schema](#post-chat-default-schema) |

#### Responses


##### <span id="post-chat-default"></span> Default Response
successful operation

###### <span id="post-chat-default-schema"></span> Schema
empty schema

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
| body | `body` | string | `string` | |  | |  |

#### All responses
| Code | Status | Description | Has headers | Schema |
|------|--------|-------------|:-----------:|--------|
| [default](#post-user-config-default) | | successful operation |  | [schema](#post-user-config-default-schema) |

#### Responses


##### <span id="post-user-config-default"></span> Default Response
successful operation

###### <span id="post-user-config-default-schema"></span> Schema
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
| username | `path` | string | `string` |  | ✓ |  |  |
| body | `body` | string | `string` | |  | |  |

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
| username | `path` | string | `string` |  | ✓ |  |  |

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
| username | `path` | string | `string` |  | ✓ |  |  |
| body | `body` | string | `string` | |  | |  |

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
| username | `path` | string | `string` |  | ✓ |  |  |
| body | `body` | string | `string` | |  | |  |

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



### <span id="user-property"></span> UserProperty


  



**Properties**

| Name | Type | Go type | Required | Default | Description | Example |
|------|------|---------|:--------:| ------- |-------------|---------|
| key | string| `string` |  | |  |  |
| value | string| `string` |  | |  |  |


