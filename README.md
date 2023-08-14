# openfire-sparkweb-plugin
This plugin adds support for a whole range of modern web service connections to Openfire/XMPP via the embedded Jetty web server in a different way to the traditional persistent client XMPP session over TCP/5222 or Bosh/7443 or Websockets/7443 used by native binary clients.

It uses :

- JWTs (JSON Web Tokens) instead of traditional username/passwords combined with Web Authentication and Web Credentials to authenticate user requests and manage the user permissions and entitlements that apply for the request or entitlement.
- REST APIs to make requests and receive immediate responses.
- Server Sent Events (SSE) and
- Web-Push to push events to users when they are online or offline.

A user has a singleton xmpp session in Openfire that is created on demand and removed when it expires. This single user session can have many active REST and SSE connections depending on how many browsers tabs, browser windows or browser instances are connected to Openfire from applications in web pages opened on behalf of the user.

The xmpp session has the full feature set of an XMPP client that is based on Smack/Spark. It also has User Interface (UI) consisting of web-components that can bind directly to Spark features. For example, a contacts roster widget and a chat conversation widget. that work independent of each other and can be hosted in different web pages or different browsers but end up pointing at the same xmpp session.

A fully working XMPP client can be constructed in a web page with minimal HTML and JavaScript.
