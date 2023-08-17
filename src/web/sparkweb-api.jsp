<!--
  - Copyright (C) 2017 Ignite Realtime Foundation. All rights reserved.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  - http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page errorPage="error.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="org.jsoup.Jsoup" %>
<%@ page import="org.jsoup.safety.Safelist" %>
<%@ page import="org.ifsoft.openfire.SparkWeb" %>
<%@ page import="org.ifsoft.openfire.Language" %>
<%@ page import="org.jivesoftware.openfire.user.User" %>
<%@ page import="org.jivesoftware.openfire.XMPPServer" %>
<%@ page import="org.jivesoftware.openfire.http.HttpBindManager" %>
<%@ page import="org.jivesoftware.util.CookieUtils" %>
<%@ page import="org.jivesoftware.util.JiveGlobals" %>
<%@ page import="org.jivesoftware.util.ParamUtils" %>
<%@ page import="org.jivesoftware.util.StringUtils" %>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="webManager" class="org.jivesoftware.util.WebManager"  />
<% webManager.init(request, response, session, application, out ); %>
<%
	String username = ParamUtils.getParameter(request, "username");
    HttpBindManager httpBindManager = HttpBindManager.getInstance();
	String url = "https://" + XMPPServer.getInstance().getServerInfo().getHostname() + ":" + JiveGlobals.getProperty("httpbind.port.secure", "7443") + "/sparkweb/swagger?username=" + username;	
%>
<html>
<head>
    <title><fmt:message key="admin.sidebar.webclients.item.api.name"/></title>
    <meta name="subPageID" content="sparkweb-api"/>			
	<meta name="extraParams" content="username=<%= username %>" />
	<style type='text/css'>
		#jive-main table, #jive-main-content {
			height: 92%;
		}
	</style>	
</head>
<body>

<% if ( !httpBindManager.isHttpBindEnabled() ) { %>

<div class="jive-warning">
    <table cellpadding="0" cellspacing="0" border="0">
        <tbody>
        <tr><td class="jive-icon"><img src="images/warning-16x16.gif" width="16" height="16" border="0" alt=""></td>
            <td class="jive-icon-label">
                <fmt:message key="warning.httpbinding.disabled">
                    <fmt:param value="<a href=\"../../http-bind.jsp\">"/>
                    <fmt:param value="</a>"/>
                </fmt:message>
            </td></tr>
        </tbody>
    </table>
</div><br>

<%  } %>
<script>location.href = '<%= url %>';</script>
</body>
</html>
