<%@ taglib uri="/WEB-INF/dhv-taglib.tld" prefix="dhv" %>
<%@ page import="java.util.*,org.aspcfs.modules.contacts.base.*,org.aspcfs.modules.pipeline.base.OpportunityComponent,com.zeroio.iteam.base.*" %>
<jsp:useBean id="ContactDetails" class="org.aspcfs.modules.contacts.base.Contact" scope="request"/>
<jsp:useBean id="opportunityHeader" class="org.aspcfs.modules.pipeline.base.OpportunityHeader" scope="request"/>
<jsp:useBean id="componentList" class="org.aspcfs.modules.pipeline.base.OpportunityComponentList" scope="request"/>
<jsp:useBean id="ComponentListInfo" class="org.aspcfs.utils.web.PagedListInfo" scope="session"/>
<%@ include file="../initPage.jsp" %>
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript" SRC="javascript/popURL.js"></SCRIPT>
<dhv:evaluate exp="<%= !isPopup(request) %>">
<a href="ExternalContacts.do">General Contacts</a> > 
<a href="ExternalContacts.do?command=ListContacts">View Contacts</a> >
<a href="ExternalContacts.do?command=ContactDetails&id=<%= ContactDetails.getId() %>">Contact Details</a> >
<a href="ExternalContactsOpps.do?command=ViewOpps&contactId=<%= ContactDetails.getId() %>">Opportunities</a> >
Opportunity Details<br>
<hr color="#BFBFBB" noshade>
</dhv:evaluate>
<%-- Begin container --%>
<%@ include file="contact_details_header_include.jsp" %>
<% String param1 = "id=" + ContactDetails.getId(); 
   String param2 = addLinkParams(request, "popup|popupType|actionId"); %>
<dhv:container name="contacts" selected="opportunities" param="<%= param1 %>" appendToUrl="<%= param2 %>" style="tabs"/>
<table cellpadding="4" cellspacing="0" border="0" width="100%">
  <tr>
    <td class="containerBack">
      <%-- Begin container content --%>
      <img src="images/icons/stock_form-currency-field-16.gif" border="0" align="absmiddle">
      <strong><%= toHtml(opportunityHeader.getDescription()) %></strong>
      <% FileItem thisFile = new FileItem(); %>
      <dhv:evaluate if="<%= opportunityHeader.hasFiles() %>">
        <%= thisFile.getImageTag() %>
      </dhv:evaluate>
      <br>
    <% if(ContactDetails.getOrgId() > 0){ %>
      <dhv:permission name="contacts-external_contacts-opportunities-add,accounts-accounts-contacts-opportunities-add"  all="true">
      <br>
      <a href="ExternalContactsOppComponents.do?command=Prepare&headerId=<%= opportunityHeader.getId() %>&contactId=<%= opportunityHeader.getContactLink() %><%= addLinkParams(request, "popup|popupType|actionId") %>">Add a Component</a><br>
    </dhv:permission>
    <% }else{ %>
      <dhv:permission name="contacts-external_contacts-opportunities-add">
      <br>
      <a href="ExternalContactsOppComponents.do?command=Prepare&headerId=<%= opportunityHeader.getId() %>&contactId=<%= opportunityHeader.getContactLink() %><%= addLinkParams(request, "popup|popupType|actionId") %>">Add a Component</a><br>
    </dhv:permission>
    <%}%>
    
<%= addHiddenParams(request, "popup|popupType|actionId") %>
<input type="hidden" name="actionSource" value="ExternalContactsOppComponents">
<dhv:pagedListStatus title="<%= showError(request, "actionError") %>" object="ComponentListInfo"/>
<table cellpadding="4" cellspacing="0" border="0" width="100%" class="details">
  <tr>
    <dhv:permission name="contacts-external_contacts-opportunities-edit,contacts-external_contacts-opportunities-delete">
    <th nowrap>
      <strong>Action</strong>
    </th>
    </dhv:permission>
    <th nowrap>
      <strong><a href="ExternalContactsOpps.do?command=DetailsOpp&headerId=<%= opportunityHeader.getId() %>&contactId=<%= ContactDetails.getId() %>&column=oc.description<%= addLinkParams(request, "popup|popupType|actionId") %>">Component</a></strong>
      <%= ComponentListInfo.getSortIcon("oc.description") %>
    </th>
    <th nowrap>
      <strong><a href="ExternalContactsOpps.do?command=DetailsOpp&headerId=<%= opportunityHeader.getId() %>&contactId=<%= ContactDetails.getId() %>&column=oc.closed<%= addLinkParams(request, "popup|popupType|actionId") %>">Status</a></strong>
      <%= ComponentListInfo.getSortIcon("oc.closed") %>
    </th>
    <th nowrap>
      <strong><a href="ExternalContactsOpps.do?command=DetailsOpp&headerId=<%= opportunityHeader.getId() %>&contactId=<%= ContactDetails.getId() %>&column=oc.guessvalue<%= addLinkParams(request, "popup|popupType|actionId") %>">Guess Amount</a></strong>
      <%= ComponentListInfo.getSortIcon("oc.guessvalue") %>
    </th>
    <th nowrap>
      <strong><a href="ExternalContactsOpps.do?command=DetailsOpp&headerId=<%= opportunityHeader.getId() %>&contactId=<%= ContactDetails.getId() %>&column=oc.closedate<%= addLinkParams(request, "popup|popupType|actionId") %>">Close Date</a></strong>
      <%= ComponentListInfo.getSortIcon("oc.closedate") %>
    </th>
    <th nowrap>
      <strong><a href="ExternalContactsOpps.do?command=DetailsOpp&headerId=<%= opportunityHeader.getId() %>&contactId=<%= ContactDetails.getId() %>&column=stagename<%= addLinkParams(request, "popup|popupType|actionId") %>">Current Stage</a></strong>
      <%= ComponentListInfo.getSortIcon("stagename") %>
    </th>
  </tr>
<%
	Iterator j = componentList.iterator();
	if ( j.hasNext() ) {
		int rowid = 0;
	    while (j.hasNext()) {
        rowid = (rowid != 1?1:2);
        OpportunityComponent oppComponent = (OpportunityComponent)j.next();
%>      
  <tr class="containerBody">
    <dhv:permission name="contacts-external_contacts-opportunities-edit,contacts-external_contacts-opportunities-delete">
    <td width="8" valign="top" nowrap class="row<%= rowid %>">
      <% if(ContactDetails.getOrgId() > 0){ %>
       <dhv:permission name="contacts-external_contacts-opportunities-edit,accounts-accounts-contacts-opportunities-edit" all="true"><a href="ExternalContactsOppComponents.do?command=ModifyComponent&headerId=<%= oppComponent.getHeaderId() %>&id=<%= oppComponent.getId() %>&contactId=<%= ContactDetails.getId() %>&return=list&actionSource=ExternalContactsOppComponents<%= addLinkParams(request, "popup|popupType|actionId") %>">Edit</a></dhv:permission><dhv:permission name="contacts-external_contacts-opportunities-edit,contacts-external_contacts-opportunities-delete,accounts-accounts-contacts-opportunities-edit,accounts-accounts-contacts-opportunities-delete" all="true">|</dhv:permission><dhv:permission name="contacts-external_contacts-opportunities-delete,accounts-accounts-contacts-opportunities-delete" 
       all="true"><a href="javascript:popURLReturn('ExternalContactsOppComponents.do?command=ConfirmComponentDelete&contactId=<%= ContactDetails.getId() %>&id=<%= oppComponent.getId() %>&popup=true<%= addLinkParams(request, "popupType|actionId") %>','ExternalContactsOpps.do?command=ViewOpps&contactId=<%= ContactDetails.getId() %>', 'Delete_opp','320','200','yes','no');">Del</a></dhv:permission>
       <dhv:permission name="contacts-external_contacts-opportunities-edit,contacts-external_contacts-opportunities-delete,accounts-accounts-contacts-opportunities-edit,accounts-accounts-contacts-opportunities-delete" all="true" none="true">&nbsp;</dhv:permission>
      <% }else{ %>
      <dhv:permission name="contacts-external_contacts-opportunities-edit"><a href="ExternalContactsOppComponents.do?command=ModifyComponent&headerId=<%= oppComponent.getHeaderId() %>&id=<%= oppComponent.getId() %>&contactId=<%= ContactDetails.getId() %>&return=list&actionSource=ExternalContactsOppComponents<%= addLinkParams(request, "popup|popupType|actionId") %>">Edit</a></dhv:permission><dhv:permission name="contacts-external_contacts-opportunities-edit,contacts-external_contacts-opportunities-delete" all="true">|</dhv:permission><dhv:permission name="contacts-external_contacts-opportunities-delete"><a href="javascript:popURLReturn('ExternalContactsOppComponents.do?command=ConfirmComponentDelete&contactId=<%= ContactDetails.getId() %>&id=<%= oppComponent.getId() %>&popup=true<%= addLinkParams(request, "popupType|actionId") %>','ExternalContactsOpps.do?command=ViewOpps&contactId=<%= ContactDetails.getId() %>', 'Delete_opp','320','200','yes','no');">Del</a></dhv:permission>
      <dhv:permission name="contacts-external_contacts-opportunities-edit,contacts-external_contacts-opportunities-delete" all="true" none="true">&nbsp;</dhv:permission>
      <% } %>
    </td>
    </dhv:permission>
    <td width="100%" valign="top" class="row<%= rowid %>">
      <a href="ExternalContactsOppComponents.do?command=DetailsComponent&contactId=<%= ContactDetails.getId() %>&id=<%= oppComponent.getId() %><%= addLinkParams(request, "popup|popupType|actionId") %>">
      <%= toHtml(oppComponent.getDescription()) %></a>
    </td>
    <td valign="top" align="center" nowrap class="row<%= rowid %>">
      <%= oppComponent.getClosed() != null ? "<font color=\"red\">closed</font>" : "<font color=\"green\">open</font>" %>
    </td>
    <td valign="top" align="right" nowrap class="row<%= rowid %>">
      $<%= oppComponent.getGuessCurrency() %>
    </td>
    <td valign="top" align="center" nowrap class="row<%= rowid %>">
      <%= toHtml(oppComponent.getCloseDateString()) %>
    </td>
    <td valign="top" align="center" nowrap class="row<%= rowid %>">
      <%= toHtml(oppComponent.getStageName()) %>
    </td>		
  </tr>
<%}%>
<%} else {%>
  <tr class="containerBody">
    <td colspan="6">
      No opportunity components found.
    </td>
  </tr>
<%}%>
</table>
<br>
<dhv:pagedListControl object="ComponentListInfo"/>
&nbsp;<br>
<dhv:sharing primaryBean="opportunityHeader" secondaryBeans="ContactDetails" action="edit"><input type="button" value="Rename Opportunity" onClick="javascript:window.location.href='ExternalContactsOpps.do?command=ModifyOpp&headerId=<%= opportunityHeader.getId() %>&contactId=<%= opportunityHeader.getContactLink() %><%= addLinkParams(request, "popup|popupType|actionId") %>';"></dhv:sharing>
<dhv:sharing primaryBean="opportunityHeader" secondaryBeans="ContactDetails" action="delete"><input type="button" value="Delete Opportunity" onClick="javascript:popURLReturn('ExternalContactsOpps.do?command=ConfirmDelete&contactId=<%= ContactDetails.getId() %>&headerId=<%= opportunityHeader.getId() %>&popup=true<%= addLinkParams(request, "popupType|actionId") %>','ExternalContactsOpps.do?command=ViewOpps&contactId=<%= ContactDetails.getId() %>', 'Delete_opp','320','200','yes','no')"></dhv:sharing>
</td>
</tr>
</table>
