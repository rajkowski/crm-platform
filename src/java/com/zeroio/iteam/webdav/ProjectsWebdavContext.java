/*
 *  Copyright(c) 2004 Concursive Corporation (http://www.concursive.com/) All
 *  rights reserved. This material cannot be distributed without written
 *  permission from Concursive Corporation. Permission to use, copy, and modify
 *  this material for internal use is hereby granted, provided that the above
 *  copyright notice and this permission notice appear in all copies. CONCURSIVE
 *  CORPORATION MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES, EXPRESS OR
 *  IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR ANY PARTICULAR
 *  PURPOSE, AND THE WARRANTY AGAINST INFRINGEMENT OF PATENTS OR OTHER
 *  INTELLECTUAL PROPERTY RIGHTS. THE SOFTWARE IS PROVIDED "AS IS", AND IN NO
 *  EVENT SHALL CONCURSIVE CORPORATION OR ANY OF ITS AFFILIATES BE LIABLE FOR
 *  ANY DAMAGES, INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL
 *  DAMAGES RELATING TO THE SOFTWARE.
 */
package com.zeroio.iteam.webdav;

import com.zeroio.webdav.context.BaseWebdavContext;
import com.zeroio.webdav.context.ItemContext;
import com.zeroio.webdav.context.ModuleContext;
import org.aspcfs.controller.SystemStatus;
import org.aspcfs.modules.base.Constants;

import javax.naming.NamingException;
import java.io.FileNotFoundException;
import java.sql.*;

/**
 * Description of the Class
 *
 * @author ananth
 * @version $Id: ProjectsWebdavContext.java,v 1.2.6.1 2005/05/10 18:01:53
 *          ananth Exp $
 * @created November 11, 2004
 */
public class ProjectsWebdavContext
    extends BaseWebdavContext implements ModuleContext {

  private final static String PROJECTS = "projects";
  private int linkModuleId = Constants.DOCUMENTS_PROJECTS;
  private int userId = -1;
  private String fileLibraryPath = null;
  private String permission = "projects-view";


  /**
   * Sets the userId attribute of the ProjectsWebdavContext object
   *
   * @param tmp The new userId value
   */
  public void setUserId(int tmp) {
    this.userId = tmp;
  }


  /**
   * Sets the userId attribute of the ProjectsWebdavContext object
   *
   * @param tmp The new userId value
   */
  public void setUserId(String tmp) {
    this.userId = Integer.parseInt(tmp);
  }


  /**
   * Gets the userId attribute of the ProjectsWebdavContext object
   *
   * @return The userId value
   */
  public int getUserId() {
    return userId;
  }


  /**
   * Sets the linkModuleId attribute of the ProjectsWebdavContext object
   *
   * @param tmp The new linkModuleId value
   */
  public void setLinkModuleId(int tmp) {
    this.linkModuleId = tmp;
  }


  /**
   * Sets the linkModuleId attribute of the ProjectsWebdavContext object
   *
   * @param tmp The new linkModuleId value
   */
  public void setLinkModuleId(String tmp) {
    this.linkModuleId = Integer.parseInt(tmp);
  }


  /**
   * Sets the fileLibraryPath attribute of the ProjectsWebdavContext object
   *
   * @param tmp The new fileLibraryPath value
   */
  public void setFileLibraryPath(String tmp) {
    this.fileLibraryPath = tmp;
  }


  /**
   * Sets the permission attribute of the ProjectsWebdavContext object
   *
   * @param tmp The new permission value
   */
  public void setPermission(String tmp) {
    this.permission = tmp;
  }


  /**
   * Gets the linkModuleId attribute of the ProjectsWebdavContext object
   *
   * @return The linkModuleId value
   */
  public int getLinkModuleId() {
    return linkModuleId;
  }


  /**
   * Gets the fileLibraryPath attribute of the ProjectsWebdavContext object
   *
   * @return The fileLibraryPath value
   */
  public String getFileLibraryPath() {
    return fileLibraryPath;
  }


  /**
   * Gets the permission attribute of the ProjectsWebdavContext object
   *
   * @return The permission value
   */
  public String getPermission() {
    return permission;
  }


  /**
   * Constructor for the ProjectsWebdavContext object
   */
  public ProjectsWebdavContext() {
  }


  /**
   * Constructor for the ProjectsWebdavContext object
   *
   * @param name         Description of the Parameter
   * @param linkModuleId Description of the Parameter
   */
  public ProjectsWebdavContext(String name, int linkModuleId) {
    this.contextName = name;
    this.linkModuleId = linkModuleId;
  }


  /**
   * Description of the Method
   *
   * @param db              Description of the Parameter
   * @param fileLibraryPath Description of the Parameter
   * @param userId          Description of the Parameter
   * @param thisSystem      Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void buildResources(SystemStatus thisSystem, Connection db, int userId, String fileLibraryPath) throws SQLException {
    this.fileLibraryPath = fileLibraryPath;
    this.userId = userId;
    bindings.clear();
    if (hasPermission(thisSystem, userId, "projects-projects-view")) {
      populateBindings(db);
    }
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void populateBindings(Connection db) throws SQLException {
    if (linkModuleId == -1) {
      throw new SQLException("Module ID not specified");
    }
    PreparedStatement pst = db.prepareStatement(
        "SELECT project_id, title, entered, modified " +
        "FROM projects " +
        "WHERE project_id > -1 " +
        "AND trashed_date IS NULL " +
        "AND project_id IN (SELECT project_id FROM project_team WHERE user_id = ? " +
        "AND status IS NULL " +
        "AND portal = ?) ");
    pst.setInt(1, userId);
    pst.setBoolean(2, false);
    ResultSet rs = pst.executeQuery();
    while (rs.next()) {
      ItemContext item = new ItemContext();
      item.setLinkModuleId(linkModuleId);
      item.setLinkItemId(rs.getInt("project_id"));
      item.setContextName(rs.getString("title"));
      item.setPath(fileLibraryPath + PROJECTS + fs);
      item.setUserId(userId);
      item.setPermission("project-documents");
      bindings.put(item.getContextName(), item);
      Timestamp entered = rs.getTimestamp("entered");
      Timestamp modified = rs.getTimestamp("modified");
      buildProperties(
          item.getContextName(), entered, modified, new Integer(0));
    }
    rs.close();
    pst.close();
  }


  /**
   * Description of the Method
   *
   * @param thisSystem Description of the Parameter
   * @param db         Description of the Parameter
   * @param folderName Description of the Parameter
   * @return Description of the Return Value
   * @throws SQLException          Description of the Exception
   * @throws FileNotFoundException Description of the Exception
   * @throws NamingException       Description of the Exception
   */
  public boolean createSubcontext(SystemStatus thisSystem, Connection db, String folderName) throws SQLException,
      FileNotFoundException, NamingException {
    //Not allowed
    return false;
  }
}

