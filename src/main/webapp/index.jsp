<%@ page import="com.nuts.framework.config.SystemProperties" %>
<% String welcomePage = SystemProperties.getLocalWelcome();%>

<jsp:forward page="<%=welcomePage%>"/>