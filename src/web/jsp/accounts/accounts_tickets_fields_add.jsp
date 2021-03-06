<%-- 
  - Copyright(c) 2004 Concursive Corporation (http://www.concursive.com/) All
  - rights reserved. This material cannot be distributed without written
  - permission from Concursive Corporation. Permission to use, copy, and modify
  - this material for internal use is hereby granted, provided that the above
  - copyright notice and this permission notice appear in all copies. CONCURSIVE
  - CORPORATION MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES, EXPRESS OR
  - IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT NOT LIMITED TO, THE
  - IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR ANY PARTICULAR
  - PURPOSE, AND THE WARRANTY AGAINST INFRINGEMENT OF PATENTS OR OTHER
  - INTELLECTUAL PROPERTY RIGHTS. THE SOFTWARE IS PROVIDED "AS IS", AND IN NO
  - EVENT SHALL CONCURSIVE CORPORATION OR ANY OF ITS AFFILIATES BE LIABLE FOR
  - ANY DAMAGES, INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL
  - DAMAGES RELATING TO THE SOFTWARE.
  - 
  - Version: $Id$
  - Description:
  --%>
<%@ taglib uri="/WEB-INF/dhv-taglib.tld" prefix="dhv" %>
<%@ page import="java.util.*,org.aspcfs.modules.troubletickets.base.*,org.aspcfs.modules.base.*" %>
<%@ page import="java.text.DateFormat" %>
<jsp:useBean id="OrgDetails" class="org.aspcfs.modules.accounts.base.Organization" scope="request"/>
<jsp:useBean id="TicketDetails" class="org.aspcfs.modules.troubletickets.base.Ticket" scope="request"/>
<jsp:useBean id="Category" class="org.aspcfs.modules.base.CustomFieldCategory" scope="request"/>
<jsp:useBean id="User" class="org.aspcfs.modules.login.beans.UserBean" scope="session"/>
<jsp:useBean id="systemStatus" class="org.aspcfs.controller.SystemStatus" scope="request"/>
<%@ include file="../initPage.jsp" %>
<script language="JavaScript" TYPE="text/javascript" SRC="javascript/checkDate.js"></script>
<script language="JavaScript" TYPE="text/javascript" SRC="javascript/popCalendar.js"></script>
<form name="details" action="AccountTicketFolders.do?command=Fields&ticketId=<%= TicketDetails.getId() %>&catId=<%= Category.getId() %><%= addLinkParams(request, "popup|popupType|actionId") %>" method="post">
<dhv:evaluate if="<%= !isPopup(request) %>">
<%-- Trails --%>
<table class="trails" cellspacing="0">
<tr>
<td>
<a href="Accounts.do"><dhv:label name="accounts.accounts">Accounts</dhv:label></a> > 
<a href="Accounts.do?command=Search"><dhv:label name="accounts.SearchResults">Search Results</dhv:label></a> >
<a href="Accounts.do?command=Details&orgId=<%= TicketDetails.getOrgId() %>"><dhv:label name="accounts.details">Account Details</dhv:label></a> >
<a href="Accounts.do?command=ViewTickets&orgId=<%= TicketDetails.getOrgId() %>"><dhv:label name="accounts.tickets.tickets">Tickets</dhv:label></a> >
<a href="AccountTickets.do?command=TicketDetails&id=<%= TicketDetails.getId() %>"><dhv:label name="accounts.tickets.details">Ticket Details</dhv:label></a> >
<a href="AccountTicketFolders.do?command=FolderList&ticketId=<%= TicketDetails.getId() %>"><dhv:label name="accounts.Folders">Folders</dhv:label></a> >
<dhv:evaluate if="<%= (Category.getAllowMultipleRecords()) %>">
  <a href="AccountTicketFolders.do?command=Fields&ticketId=<%= TicketDetails.getId() %>&catId=<%= Category.getId() %>"><dhv:label name="accounts.accounts_fields.ListOfFolderRecords">List of Folder Records</dhv:label></a> >
</dhv:evaluate>
<dhv:evaluate if="<%= (!Category.getAllowMultipleRecords()) %>">
  <a href="AccountTicketFolders.do?command=Fields&ticketId=<%= TicketDetails.getId() %>&catId=<%= Category.getId() %>"><dhv:label name="accounts.accounts_fields.FolderRecordDetails">Folder Record Details</dhv:label></a> >
</dhv:evaluate>
<dhv:label name="accounts.accounts_fields_add.AddFolderRecord">Add Folder Record</dhv:label>
</td>
</tr>
</table>
<%-- End Trails --%>
</dhv:evaluate>
<dhv:container name="accounts" selected="tickets" object="OrgDetails" param='<%= "orgId=" + OrgDetails.getOrgId() %>' appendToUrl='<%= addLinkParams(request, "popup|popupType|actionId") %>'>
  <dhv:container name="accountstickets" selected="folders" object="TicketDetails" param='<%= "id=" + TicketDetails.getId() %>' appendToUrl='<%= addLinkParams(request, "popup|popupType|actionId") %>'>
    <%@ include file="accounts_ticket_header_include.jsp" %>
    &nbsp;<br />
    <table cellspacing="0" cellpadding="0" border="0" width="100%">
    <tr>
      <td>
        <dhv:label name="accounts.accounts_documents_folders_add.Folder.colon">Folder:</dhv:label>
        <strong><%= toHtml(Category.getName()) %></strong>
      </td>
      <% if (!Category.getAllowMultipleRecords()) { %>
        <td align="right" nowrap>
          <img src="images/icons/16_edit_comment.gif" align="absMiddle" border="0" />
          <dhv:label name="accounts.accounts_fields.FolderOneRecord">This folder can have only one record</dhv:label>
        </td>
      <% } else { %>
        <td align="right" nowrap>
          <img src="images/icons/16_edit_comment.gif" align="absMiddle" border="0" />
          <dhv:label name="accounts.accounts_fields.FolderHaveMultipleRecords">This folder can have multiple records</dhv:label>
        </td>
      <% } %>
    </tr>
  </table>
    <dhv:evaluate if="<%= !Category.isEmpty() %>">
      <br />
      <input type="submit" value="<dhv:label name="global.button.save">Save</dhv:label>" onClick="javascript:this.form.action='AccountTicketFolders.do?command=InsertFields&ticketId=<%= TicketDetails.getId() %>&catId=<%= Category.getId() %><%= addLinkParams(request, "popup|popupType|actionId") %>'">
      <input type="submit" value="<dhv:label name="global.button.cancel">Cancel</dhv:label>" onClick="javascript:this.form.action='AccountTicketFolders.do?command=Fields&ticketId=<%= TicketDetails.getId() %>&catId=<%= Category.getId() %><%= addLinkParams(request, "popup|popupType|actionId") %>'"><br />
      <br /><dhv:formMessage showSpace="false" />
    </dhv:evaluate>
<%
  Iterator groups = Category.iterator();
  if (groups.hasNext()) {
    while (groups.hasNext()) {
    CustomFieldGroup thisGroup = (CustomFieldGroup)groups.next();
%>
    <table cellpadding="4" cellspacing="0" border="0" width="100%" class="details">
      <tr>
        <th colspan="2">
          <strong><%= StringUtils.toHtml(thisGroup.getName()) %></strong>
        </th>
      </tr>
<%  
  Iterator fields = thisGroup.iterator();
  if (fields.hasNext()) {
    while (fields.hasNext()) {
      CustomField thisField = (CustomField)fields.next();
%>    
      <tr class="containerBody">
        <%-- Do not use toHtml() here, it's done by CustomField --%>
        <td valign="top" nowrap class="formLabel">
          <%= thisField.getNameHtml() %>
        </td>
        <td valign="top">
          <%= thisField.getHtmlElement(systemStatus) %> <font color="red"><%= (thisField.getRequired()?"*":"") %></font>
          <font color="#006699"><%= toHtml(thisField.getError()) %></font>
          <%= toHtml(thisField.getAdditionalText()) %>
        </td>
      </tr>
<%    
    }
  } else {
%>
      <tr class="containerBody">
        <td colspan="2">
          <font color="#9E9E9E"><dhv:label name="accounts.accounts_fields.NoFieldsAvailable">No fields available.</dhv:label></font>
        </td>
      </tr>
<%}%>
    </table>
    <br />
<%}%>
    <dhv:evaluate if="<%= !Category.isEmpty() %>">
      <input type="submit" value="<dhv:label name="global.button.save">Save</dhv:label>" onClick="javascript:this.form.action='AccountTicketFolders.do?command=InsertFields&ticketId=<%= TicketDetails.getId() %>&catId=<%= Category.getId() %><%= addLinkParams(request, "popup|popupType|actionId") %>'" />
      <input type="submit" value="<dhv:label name="global.button.cancel">Cancel</dhv:label>" onClick="javascript:this.form.action='AccountTicketFolders.do?command=Fields&ticketId=<%= TicketDetails.getId() %>&catId=<%= Category.getId() %><%= addLinkParams(request, "popup|popupType|actionId") %>'" />
    </dhv:evaluate>
<%}else{%>
    <table cellpadding="4" cellspacing="0" border="0" width="100%" class="details">
      <tr class="containerBody">
        <td colspan="2">
          <font color="#9E9E9E"><dhv:label name="accounts.accounts_fields_add.NoGroupsAvailable">No groups available.</dhv:label></font>
        </td>
      </tr>
    </table>
<%}%>
  </dhv:container>
</dhv:container>
</form>
