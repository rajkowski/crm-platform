<%@ taglib uri="/WEB-INF/dhv-taglib.tld" prefix="dhv" %>
<%@ page import="java.util.*,java.text.*,org.aspcfs.modules.admin.base.*" %>
<jsp:useBean id="CategoryList" class="org.aspcfs.modules.base.CustomFieldCategoryList" scope="request"/>
<jsp:useBean id="Category" class="org.aspcfs.modules.base.CustomFieldCategory" scope="request"/>
<jsp:useBean id="Group" class="org.aspcfs.modules.base.CustomFieldGroup" scope="request"/>
<jsp:useBean id="PermissionCategory" class="org.aspcfs.modules.admin.base.PermissionCategory" scope="request"/>
<jsp:useBean id="ModId" class="java.lang.String" scope="request"/>
<jsp:useBean id="ConstantId" class="java.lang.String" scope="request"/>
<%@ include file="../initPage.jsp" %>
<body onLoad="document.forms[0].name.focus();">
<form name="details" action="AdminFieldsGroup.do?command=InsertGroup&modId=<%= ModId %>&catId=<%= Category.getId() %>&auto-populate=true" method="post">
<a href="Admin.do">Setup</a> >
<a href="Admin.do?command=Config">Configure Modules</a> >
<a href="Admin.do?command=ConfigDetails&moduleId=<%=ModId%>"><%= PermissionCategory.getCategory() %></a> >
<a href="AdminFieldsFolder.do?command=ListFolders&modId=<%= ModId %>">Custom Folders</a> > 
<a href="AdminFieldsGroup.do?command=ListGroups&modId=<%= ModId %>&catId=<%= Category.getId() %>">Folder</a> >
New Group<br>
<hr color="#BFBFBB" noshade>
<% if (request.getAttribute("actionError") != null) { %>
<%= showError(request, "actionError") %>
<%}%>
<table cellpadding="4" cellspacing="0" border="1" width="100%" bordercolorlight="#000000" bordercolor="#FFFFFF">
  <tr class="containerHeader">
    <td>
      <strong>Module: <%=PermissionCategory.getCategory()%></strong>
    </td>
  </tr>
  <tr class="containerMenu">
    <td>
      <strong>Folder: <%= toHtml(Category.getName()) %></strong>
    </td>
  </tr>
  <tr>
    <td class="containerBack">
      <table cellpadding="4" cellspacing="0" border="1" width="100%" bordercolorlight="#000000" bordercolor="#FFFFFF">
        <tr class="title">
          <td colspan="2">
            Add a Group
          </td>
        </tr>
        <tr class="containerBody">
          <td class="formLabel">
            Group Name
          </td>
          <td>
            <input type="text" name="name" maxlength="200" value="<%= toHtmlValue(Group.getName()) %>"><font color="red">*</font>
            <%= showAttribute(request, "nameError") %>
          </td>
        </tr>
      </table>
      &nbsp;<br>
      <input type="hidden" name="categoryId" value="<%= Category.getId() %>">
      <input type="hidden" name="moduleId" value="<%= ConstantId %>">
      <input type="submit" value="Save">
      <input type="submit" value="Cancel" onClick="javascript:this.form.action='AdminFieldsGroup.do?command=ListGroups&modId=<%= ModId %>&catId=<%= Category.getId() %>'">
    </td>
  </tr>
</table>
</form>
</body>
