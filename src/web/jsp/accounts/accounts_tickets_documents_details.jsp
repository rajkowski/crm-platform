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
<%@ taglib uri="/WEB-INF/zeroio-taglib.tld" prefix="zeroio" %>
<%@ page import="com.zeroio.iteam.base.FileItemVersion,java.util.Iterator" %>
<jsp:useBean id="OrgDetails"
             class="org.aspcfs.modules.accounts.base.Organization"
             scope="request"/>
<jsp:useBean id="TicketDetails"
             class="org.aspcfs.modules.troubletickets.base.Ticket"
             scope="request"/>
<jsp:useBean id="FileItem" class="com.zeroio.iteam.base.FileItem"
             scope="request"/>
<jsp:useBean id="User" class="org.aspcfs.modules.login.beans.UserBean"
             scope="session"/>
<%@ include file="../initPage.jsp" %>
<%-- Trails --%>
<table class="trails" cellspacing="0">
  <tr>
    <td>
      <a href="Accounts.do"><dhv:label
          name="accounts.accounts">Accounts</dhv:label></a> >
      <a href="Accounts.do?command=Search"><dhv:label
          name="accounts.SearchResults">Search Results</dhv:label></a> >
      <a href="Accounts.do?command=Details&orgId=<%=TicketDetails.getOrgId()%>"><dhv:label
          name="accounts.details">Account Details</dhv:label></a> >
      <a href="Accounts.do?command=ViewTickets&orgId=<%=TicketDetails.getOrgId()%>"><dhv:label
          name="accounts.tickets.tickets">Tickets</dhv:label></a> >
      <a href="AccountTickets.do?command=TicketDetails&id=<%=TicketDetails.getId()%>"><dhv:label
          name="accounts.tickets.details">Ticket Details</dhv:label></a> >
      <a href="AccountTicketsDocuments.do?command=View&tId=<%=TicketDetails.getId()%>"><dhv:label
          name="accounts.accounts_documents_details.Documents">Documents</dhv:label></a>
      >
      <dhv:label name="accounts.details.long_html">Details</dhv:label>
    </td>
  </tr>
</table>
<%-- End Trails --%>
<dhv:container name="accounts" selected="tickets" object="OrgDetails"
               param='<%= "orgId=" + OrgDetails.getOrgId() %>'
               appendToUrl='<%= addLinkParams(request, "popup|popupType|actionId") %>'>
  <dhv:container name="accountstickets" selected="documents"
                 object="TicketDetails"
                 param='<%= "id=" + TicketDetails.getId() %>'
                 appendToUrl='<%= addLinkParams(request, "popup|popupType|actionId") %>'>
    <%@ include file="accounts_ticket_header_include.jsp" %>
    <table border="0" cellpadding="4" cellspacing="0" width="100%">
      <tr class="subtab">
        <td>
          <% String documentLink = "AccountTicketsDocuments.do?command=View&tId=" + TicketDetails.getId(); %>
          <zeroio:folderHierarchy module="AccountsTickets"
                                  link="<%= documentLink %>"
                                  showLastLink="true"/> >
          <%= FileItem.getSubject() %>
        </td>
      </tr>
    </table>
    <br/>
    <table cellpadding="4" cellspacing="0" border="0" width="100%"
           class="details">
      <tr>
        <th colspan="7">
          <strong><dhv:label
              name="accounts.accounts_documents_details.AllVersionsDocument">All
            Versions of this Document</dhv:label></strong>
        </th>
      </tr>
      <tr class="title2">
        <td width="8">
          <dhv:permission name="accounts-accounts-tickets-documents-edit">
            &nbsp;
          </dhv:permission>
        </td>
        <td><dhv:label
            name="accounts.accounts_documents_details.Item">Item</dhv:label></td>
        <td><dhv:label
            name="accounts.accounts_documents_details.Size">Size</dhv:label></td>
        <td><dhv:label name="accounts.accounts_documents_details.Version">Version</dhv:label></td>
        <td><dhv:label name="accounts.accounts_documents_details.Submitted">Submitted</dhv:label></td>
        <td><dhv:label name="accounts.accounts_documents_details.SentBy">Sent
          By</dhv:label></td>
        <td><dhv:label
            name="accounts.accounts_documents_details.DL">D/L</dhv:label></td>
      </tr>
      <%
        Iterator versionList = FileItem.getVersionList().iterator();
        int rowid = 0;
        while (versionList.hasNext()) {
          rowid = (rowid != 1 ? 1 : 2);
          FileItemVersion thisVersion = (FileItemVersion) versionList.next();
      %>
      <tr class="row<%= rowid %>">
        <td width="10" align="center" rowspan="2" nowrap>
          <dhv:permission name="accounts-accounts-tickets-documents-edit">
            <a href="TroubleTicketsDocuments.do?command=Download&tId=<%= TicketDetails.getId() %>&fid=<%= FileItem.getId() %>&ver=<%= thisVersion.getVersion() %>"><dhv:label
                name="accounts.accounts_documents_details.Download">Download</dhv:label></a>
          </dhv:permission>
        </td>
        <td width="100%">
          <%= FileItem.getImageTag("-23") %><%= thisVersion.getClientFilename() %>
        </td>
        <td align="right" nowrap>
          <%= thisVersion.getRelativeSize() %> <dhv:label
            name="admin.oneThousand.abbreviation">k</dhv:label>&nbsp;
        </td>
        <td align="right" nowrap>
          <%= thisVersion.getVersion() %>&nbsp;
        </td>
        <td nowrap>
          <zeroio:tz timestamp="<%= thisVersion.getEntered() %>"
                     timeZone="<%= User.getTimeZone() %>" showTimeZone="true"/>
        </td>
        <td>
          <dhv:username id="<%= thisVersion.getEnteredBy() %>"/>
        </td>
        <td align="right">
          <%= thisVersion.getDownloads() %>
        </td>
      </tr>
      <tr class="row<%= rowid %>">
        <td colspan="6">
          <i><%= thisVersion.getSubject() %>
          </i>
        </td>
      </tr>
      <%}%>
    </table>
  </dhv:container>
</dhv:container>