package com.darkhorseventures.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import org.theseus.servlets.ControllerInitHook;
import com.darkhorseventures.utils.*;
import java.sql.*;
import java.util.Hashtable;

/**
 *  Code that is initialized when the ServletController starts.
 *
 *@author     mrajkowski
 *@created    July 10, 2001
 *@version    $Id$
 */
public class InitHook implements ControllerInitHook {

  /**
   *  When the ServletController is initialized, this code creates a
   *  ConnectionPool and maps it to the Application scope.
   *
   *@param  config  Description of Parameter
   *@return         Description of the Returned Value
   *@since          1.1
   */
  public String executeControllerInit(ServletConfig config) {

    try {
      ConnectionPool cp = new ConnectionPool();
      cp.setDebug(true);
      config.getServletContext().setAttribute("ConnectionPool", cp);
    } catch (SQLException e) {
      System.err.println(e.toString());
    }

    if (config.getInitParameter("SiteCode") != null) {
      config.getServletContext().setAttribute("SiteCode", config.getInitParameter("SiteCode"));
    }
    
    if (config.getInitParameter("GKDRIVER") != null) {
      config.getServletContext().setAttribute("GKDRIVER", config.getInitParameter("GKDRIVER"));
    }

    if (config.getInitParameter("GKHOST") != null) {
      config.getServletContext().setAttribute("GKHOST", config.getInitParameter("GKHOST"));
    }
    if (config.getInitParameter("GKUSER") != null) {
      config.getServletContext().setAttribute("GKUSER", config.getInitParameter("GKUSER"));
    }
    if (config.getInitParameter("GKUSERPW") != null) {
      config.getServletContext().setAttribute("GKUSERPW", config.getInitParameter("GKUSERPW"));
    }
    
    if (config.getInitParameter("ForceSSL") != null) {
      if ("true".equals(config.getInitParameter("ForceSSL"))) {
        config.getServletContext().setAttribute("ForceSSL", "true");
      } else {
        config.getServletContext().setAttribute("ForceSSL", "false");
      }
    }
    
    if (config.getInitParameter("GlobalPWInfo") != null) {
      config.getServletContext().setAttribute("GlobalPWInfo", 
        config.getInitParameter("GlobalPWInfo"));
    } else {
      if (System.getProperty("DEBUG") != null) System.out.println("InitHook-> No GlobalPWInfo");
      config.getServletContext().setAttribute("GlobalPWInfo", "#notspecified");
    }
    
    if (config.getInitParameter("ClientSSLKeystore") != null) {
      config.getServletContext().setAttribute("ClientSSLKeystore", 
        config.getInitParameter("ClientSSLKeystore"));
    }
    
    if (config.getInitParameter("ClientSSLKeystorePassword") != null) {
      config.getServletContext().setAttribute("ClientSSLKeystorePassword", 
        config.getInitParameter("ClientSSLKeystorePassword"));
    }

    config.getServletContext().setAttribute("SystemStatus", new Hashtable());

    return null;
  }
  

}

