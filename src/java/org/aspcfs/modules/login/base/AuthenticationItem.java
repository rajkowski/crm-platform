package com.darkhorseventures.utils;

import java.sql.*;
import org.theseus.actions.*;

/**
 *  Description of the Class
 *
 *@author     matt rajkowski
 *@created    November 11, 2002
 *@version    $Id$
 */
public class AuthenticationItem {

  private String id = null;
  private String code = null;
  private int systemId = -1;
  private int clientId = -1;
  private java.sql.Timestamp lastAnchor = null;
  private java.sql.Timestamp nextAnchor = null;
  private String authCode = "unset";


  /**
   *  Constructor for the AuthenticationItem object
   */
  public AuthenticationItem() { }


  /**
   *  Sets the id attribute of the AuthenticationItem object
   *
   *@param  tmp  The new id value
   */
  public void setId(String tmp) {
    id = tmp;
  }


  /**
   *  Sets the code attribute of the AuthenticationItem object
   *
   *@param  tmp  The new code value
   */
  public void setCode(String tmp) {
    code = tmp;
  }


  /**
   *  Sets the clientId attribute of the AuthenticationItem object
   *
   *@param  tmp  The new clientId value
   */
  public void setClientId(int tmp) {
    clientId = tmp;
  }


  /**
   *  Sets the clientId attribute of the AuthenticationItem object
   *
   *@param  tmp  The new clientId value
   */
  public void setClientId(String tmp) {
    clientId = Integer.parseInt(tmp);
  }


  /**
   *  Sets the systemId attribute of the AuthenticationItem object
   *
   *@param  tmp  The new systemId value
   */
  public void setSystemId(int tmp) {
    this.systemId = tmp;
  }


  /**
   *  Sets the systemId attribute of the AuthenticationItem object
   *
   *@param  tmp  The new systemId value
   */
  public void setSystemId(String tmp) {
    this.systemId = Integer.parseInt(tmp);
  }


  /**
   *  Sets the lastAnchor attribute of the AuthenticationItem object
   *
   *@param  tmp  The new lastAnchor value
   */
  public void setLastAnchor(java.sql.Timestamp tmp) {
    this.lastAnchor = tmp;
  }


  /**
   *  Sets the lastAnchor attribute of the AuthenticationItem object
   *
   *@param  tmp  The new lastAnchor value
   */
  public void setLastAnchor(String tmp) {
    this.lastAnchor = java.sql.Timestamp.valueOf(tmp);
  }


  /**
   *  Sets the nextAnchor attribute of the AuthenticationItem object
   *
   *@param  tmp  The new nextAnchor value
   */
  public void setNextAnchor(java.sql.Timestamp tmp) {
    this.nextAnchor = tmp;
  }


  /**
   *  Sets the nextAnchor attribute of the AuthenticationItem object
   *
   *@param  tmp  The new nextAnchor value
   */
  public void setNextAnchor(String tmp) {
    this.nextAnchor = java.sql.Timestamp.valueOf(tmp);
  }


  /**
   *  Gets the id attribute of the AuthenticationItem object
   *
   *@return    The id value
   */
  public String getId() {
    return id;
  }


  /**
   *  Gets the code attribute of the AuthenticationItem object
   *
   *@return    The code value
   */
  public String getCode() {
    return code;
  }


  /**
   *  Gets the clientId attribute of the AuthenticationItem object
   *
   *@return    The clientId value
   */
  public int getClientId() {
    return clientId;
  }


  /**
   *  Gets the systemId attribute of the AuthenticationItem object
   *
   *@return    The systemId value
   */
  public int getSystemId() {
    return systemId;
  }


  /**
   *  Gets the lastAnchor attribute of the AuthenticationItem object
   *
   *@return    The lastAnchor value
   */
  public java.sql.Timestamp getLastAnchor() {
    return lastAnchor;
  }


  /**
   *  Gets the nextAnchor attribute of the AuthenticationItem object
   *
   *@return    The nextAnchor value
   */
  public java.sql.Timestamp getNextAnchor() {
    return nextAnchor;
  }


  /**
   *  Gets the connection attribute of the AuthenticationItem object
   *
   *@param  context           Description of the Parameter
   *@return                   The connection value
   *@exception  SQLException  Description of the Exception
   */
  public Connection getConnection(ActionContext context) throws SQLException {
    return getConnection(context, true);
  }


  /**
   *  Based on the sitecode and servername (vhost) supplied in the
   *  authentication node, a connection element is returned, without performing
   *  any verification of the password
   *
   *@param  context           Description of the Parameter
   *@return                   The connectionElement value
   *@exception  SQLException  Description of the Exception
   */
  public ConnectionElement getConnectionElement(ActionContext context) throws SQLException {
    String gkHost = (String) context.getServletContext().getAttribute("GKHOST");
    String gkUser = (String) context.getServletContext().getAttribute("GKUSER");
    String gkUserPw = (String) context.getServletContext().getAttribute("GKUSERPW");
    String siteCode = (String) context.getServletContext().getAttribute("SiteCode");
    String gkDriver = (String) context.getServletContext().getAttribute("GKDRIVER");
    String serverName = context.getRequest().getServerName();
    if (System.getProperty("DEBUG") != null) {
      System.out.println("AuthenticationItem-> GateKeeper: " + gkHost);
      System.out.println("AuthenticationItem-> ServerName: " + serverName);
      System.out.println("AuthenticationItem-> SiteCode: " + siteCode);
    }
    ConnectionPool sqlDriver = (ConnectionPool) context.getServletContext().getAttribute("ConnectionPool");
    ConnectionElement gk = new ConnectionElement(gkHost, gkUser, gkUserPw);
    gk.setDriver(gkDriver);
    ConnectionElement ce = null;

    String sql =
        "SELECT * FROM sites " +
        "WHERE sitecode = ? " +
        "AND vhost = ? ";
    Connection db = sqlDriver.getConnection(gk);
    PreparedStatement pst = db.prepareStatement(sql);
    pst.setString(1, siteCode);
    pst.setString(2, serverName);
    ResultSet rs = pst.executeQuery();
    if (rs.next()) {
      String siteDbHost = rs.getString("dbhost");
      String siteDbName = rs.getString("dbname");
      String siteDbUser = rs.getString("dbuser");
      String siteDbPw = rs.getString("dbpw");
      String siteDriver = rs.getString("driver");
      authCode = rs.getString("code");
      ce = new ConnectionElement(siteDbHost, siteDbUser, siteDbPw);
      ce.setDbName(siteDbName);
      ce.setDriver(siteDriver);
    }
    rs.close();
    pst.close();
    sqlDriver.free(db);
    return ce;
  }


  /**
   *  Gets the connection attribute of the AuthenticationItem object
   *
   *@param  context           Description of the Parameter
   *@param  checkCode         Description of the Parameter
   *@return                   The connection value
   *@exception  SQLException  Description of the Exception
   */
  public Connection getConnection(ActionContext context, boolean checkCode) throws SQLException {
    if (this.isAuthenticated(context, checkCode)) {
      ConnectionElement ce = this.getConnectionElement(context);
      if (ce != null) {
        ConnectionPool sqlDriver = (ConnectionPool) context.getServletContext().getAttribute("ConnectionPool");
        return sqlDriver.getConnection(ce);
      }
    }
    return null;
  }


  /**
   *  Gets the authenticated attribute of the AuthenticationItem object
   *
   *@param  context  Description of the Parameter
   *@return          The authenticated value
   */
  public boolean isAuthenticated(ActionContext context) {
    return this.isAuthenticated(context, true);
  }


  /**
   *  Gets the authenticated attribute of the AuthenticationItem object
   *
   *@param  context    Description of the Parameter
   *@param  checkCode  Description of the Parameter
   *@return            The authenticated value
   */
  public boolean isAuthenticated(ActionContext context, boolean checkCode) {
    String serverName = context.getRequest().getServerName();
    if ((id != null && id.equals(serverName)) || !checkCode) {
      if (!checkCode || (code != null && authCode != null && code.equals(authCode))) {
        return true;
      }
    }
    return false;
  }
}

