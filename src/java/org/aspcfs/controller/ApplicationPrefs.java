package org.aspcfs.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import java.util.Properties;
import org.jcrontab.*;
import org.aspcfs.utils.StringUtils;
import com.darkhorseventures.framework.actions.*;
import com.darkhorseventures.database.*;
import java.util.prefs.*;

/**
 *  Loads and saves the build.properties file for application settings; each
 *  application instance can have its own properties on the same server
 *
 *@author     matt rajkowski
 *@created    August 27, 2003
 *@version    $Id: ApplicationPrefs.java,v 1.1.2.2 2003/08/28 21:03:45
 *      mrajkowski Exp $
 */
public class ApplicationPrefs {

  public final static String fs = System.getProperty("file.separator");
  public final static String ls = System.getProperty("line.separator");
  public final static String GENERATED_MESSAGE =
      "### GENERATED BY org.aspcfs.controller.ApplicationPrefs";
  private Map prefs = new LinkedHashMap();
  private String filename = null;


  /**
   *  Constructor for the ApplicationPrefs object
   */
  public ApplicationPrefs() { }


  /**
   *  Constructor for the ApplicationPrefs object
   *
   *@param  context  Description of the Parameter
   */
  public ApplicationPrefs(ServletContext context) {
    String dir = context.getRealPath("/");
    try {
      Preferences prefs = Preferences.userNodeForPackage(org.aspcfs.modules.setup.utils.Prefs.class);
      // Check "dir" prefs first, based on the installed directory of this webapp
      String fileLibrary = prefs.get(dir, null);
      if (fileLibrary == null) {
        // Check in the current dir of the webapp
        fileLibrary = context.getRealPath("/") + "WEB-INF" + fs + "fileLibrary" + fs;
        File propertyFile = new File(fileLibrary + "build.properties");
        if (propertyFile.exists()) {
          savePref(dir, fileLibrary);
        } else {
          // check in "root", for backwards compatibility
          fileLibrary = prefs.get("cfs.fileLibrary", null);
          if (fileLibrary != null) {
            // Save these prefs under the "dir" prefs for next time
            savePref(dir, fileLibrary);
            // erase "root" since a "dir" pref has been configured and saved
            savePref("cfs.fileLibrary", null);
          }
        }
      }
      context.setAttribute("SiteCode", prefs.get("cfs.gatekeeper.sitecode", "cfs"));
      if (System.getProperty("DEBUG") != null) {
        System.out.println("ApplicationPrefs-> Using file library at: " + fileLibrary);
      }
      // Now load the default properties
      if (fileLibrary != null) {
        this.load(fileLibrary + "build.properties");
        this.add("FILELIBRARY", fileLibrary);
      }
      this.populateContext(context);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }


  /**
   *  Constructor for the ApplicationPrefs object
   *
   *@param  filename  Description of the Parameter
   */
  public ApplicationPrefs(String filename) {
    load(filename);
  }


  /**
   *  Sets the filename attribute of the ApplicationPrefs object
   *
   *@param  tmp  The new filename value
   */
  public void setFilename(String tmp) {
    this.filename = tmp;
  }


  /**
   *  Gets the filename attribute of the ApplicationPrefs object
   *
   *@return    The filename value
   */
  public String getFilename() {
    return filename;
  }


  /**
   *  Gets the prefs attribute of the ApplicationPrefs object
   *
   *@return    The prefs value
   */
  public Map getPrefs() {
    return prefs;
  }


  /**
   *  Description of the Method
   *
   *@param  filename  Description of the Parameter
   */
  public void load(String filename) {
    if (System.getProperty("DEBUG") != null) {
      System.out.println("ApplicationPrefs-> Loading prefs: " + filename);
    }
    this.filename = filename;
    try {
      prefs.clear();
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String line = null;
      int count = 0;
      while ((line = in.readLine()) != null) {
        ++count;
        if (!line.startsWith("#") && line.indexOf("=") > 0) {
          String param = line.substring(0, line.indexOf("="));
          String value = "";
          if (line.indexOf("=") + 1 < line.length()) {
            value = line.substring(line.indexOf("=") + 1);
          }
          this.add(param, value);
        } else if (!line.startsWith(GENERATED_MESSAGE)) {
          this.add("#" + count, line);
        }
      }
      in.close();
    } catch (Exception e) {
      System.out.println("ApplicationPrefs-> EXCEPTION: " + e.getMessage());
    }
    if (System.getProperty("DEBUG") != null) {
      System.out.println("ApplicationPrefs-> Loaded items: " + prefs.size());
    }
  }


  /**
   *  Description of the Method
   *
   *@param  param  Description of the Parameter
   *@return        Description of the Return Value
   */
  public String get(String param) {
    return (String) prefs.get(param);
  }


  /**
   *  Description of the Method
   *
   *@param  param  Description of the Parameter
   *@return        Description of the Return Value
   */
  public boolean has(String param) {
    return (prefs.containsKey(param));
  }


  /**
   *  Description of the Method
   */
  public void clear() {
    prefs.clear();
  }


  /**
   *  Description of the Method
   *
   *@param  param  Description of the Parameter
   *@param  value  Description of the Parameter
   */
  public void add(String param, String value) {
    if (param != null) {
      if (value != null) {
        prefs.put(param, value);
      } else {
        prefs.remove(param);
      }
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public boolean save() {
    if (filename != null) {
      return save(filename);
    }
    return false;
  }


  /**
   *  Saves the preferences to a file to be reloaded
   *
   *@param  filename  Description of the Parameter
   *@return           Description of the Return Value
   */
  public boolean save(String filename) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(filename));
      out.write(GENERATED_MESSAGE + " on " + new java.util.Date() + " ###" + ls);
      Iterator i = prefs.keySet().iterator();
      while (i.hasNext()) {
        String param = (String) i.next();
        String value = (String) prefs.get(param);
        if (param.startsWith("#")) {
          out.write(value + ls);
        } else {
          out.write(param + "=" + value + ls);
        }
      }
      out.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }


  /**
   *  When preferences are loaded, some values are not easily accessible
   *  so they are stored in the System context.
   *
   *@param  context  Description of the Parameter
   */
  public void populateContext(ServletContext context) {
    //Configure debug mode
    if (this.has("DEBUGLEVEL")) {
      System.setProperty("DEBUG", this.get("DEBUGLEVEL"));
    }
    //Turn off Setup if setup is complete
    if (this.has("CONTROL")) {
      context.setAttribute("cfs.setup", "true");
    }
    //Verify the WEB-INF if set
    if (this.has("WEB-INF")) {
      if (!this.get("WEB-INF").equals(context.getRealPath("/") + "WEB-INF" + fs)) {
        save();
      }
    }
    //Define the ConnectionPool, else defaults from the ContextListener will be used
    ConnectionPool cp = (ConnectionPool) context.getAttribute("ConnectionPool");
    if (cp != null) {
      if (this.has("CONNECTION_POOL.DEBUG")) {
        cp.setDebug(this.get("CONNECTION_POOL.DEBUG"));
      }
      if (this.has("CONNECTION_POOL.TEST_CONNECTIONS")) {
        cp.setTestConnections(this.get("CONNECTION_POOL.TEST_CONNECTIONS"));
      }
      if (this.has("CONNECTION_POOL.ALLOW_SHRINKING")) {
        cp.setAllowShrinking(this.get("CONNECTION_POOL.ALLOW_SHRINKING"));
      }
      if (this.has("CONNECTION_POOL.MAX_CONNECTIONS")) {
        cp.setMaxConnections(this.get("CONNECTION_POOL.MAX_CONNECTIONS"));
      }
      if (this.has("CONNECTION_POOL.MAX_IDLE_TIME.SECONDS")) {
        cp.setMaxIdleTimeSeconds(this.get("CONNECTION_POOL.MAX_IDLE_TIME.SECONDS"));
      }
      if (this.has("CONNECTION_POOL.MAX_DEAD_TIME.SECONDS")) {
        cp.setMaxDeadTimeSeconds(this.get("CONNECTION_POOL.MAX_DEAD_TIME.SECONDS"));
      }
    }
    // Default LAYOUT prefs
    if (!this.has("LAYOUT.TEMPLATE")) {
      this.add("LAYOUT.TEMPLATE", "template0");
    }
    if (!this.has("LAYOUT.JSP.WELCOME")) {
      this.add("LAYOUT.JSP.WELCOME", "welcome.jsp");
    }
    if (!this.has("LAYOUT.JSP.LOGIN")) {
      this.add("LAYOUT.JSP.LOGIN", "login.jsp");
    }
    //Define whether the app requires SSL for browser clients
    addParameter(context, "ForceSSL", this.get("FORCESSL"), "false");
    //Define the developer's debug code
    addParameter(context, "GlobalPWInfo", this.get("WEBSERVER.PASSWORD"), "#notspecified");
    //Define the web server operation mode
    addParameter(context, "WEBSERVER.ASPMODE", this.get("WEBSERVER.ASPMODE"));
    //Define the mail server to be used within CFS
    addParameter(context, "MailServer", this.get("MAILSERVER"));
    addParameter(context, "FaxServer", this.get("FAXSERVER"));
    addParameter(context, "FaxEnabled", this.get("FAXENABLED"));
    addParameter(context, "SYSTEM.TIMEZONE", this.get("SYSTEM.TIMEZONE"));
    if (this.has("MAILSERVER")) {
      System.setProperty("MailServer", this.get("MAILSERVER"));
    }
    //Verify the license
    if (this.has("FILELIBRARY")) {
      String edition = null;
      String crc = null;
      try {
        File zlib = new File(this.get("FILELIBRARY") + "init" + fs + "zlib.jar");
        File input = new File(this.get("FILELIBRARY") + "init" + fs + "input.txt");
        if (zlib.exists() && input.exists()) {
          //If key and license exists, read in and parse
          java.security.Key key = org.aspcfs.utils.PrivateString.loadKey(this.get("FILELIBRARY") + "init" + fs + "zlib.jar");
          org.aspcfs.utils.XMLUtils xml = new org.aspcfs.utils.XMLUtils(org.aspcfs.utils.PrivateString.decrypt(key, StringUtils.loadText(this.get("FILELIBRARY") + "init" + fs + "input.txt")));
          //The edition will be shown
          edition = org.aspcfs.utils.XMLUtils.getNodeText(xml.getFirstChild("edition"));
          crc = org.aspcfs.utils.XMLUtils.getNodeText(xml.getFirstChild("text2"));
          if (edition != null) {
            context.setAttribute("APP_TEXT", edition);
            if ("-1".equals(crc.substring(7))) {
              context.removeAttribute("APP_SIZE");
            } else {
              context.setAttribute("APP_SIZE", crc.substring(7));
            }
          }
          //The licensed organization will be shown
          String organization = org.aspcfs.utils.XMLUtils.getNodeText(xml.getFirstChild("company"));
          if (organization != null) {
            context.setAttribute("APP_ORGANIZATION", organization);
          }
          //String gdot = org.aspcfs.utils.XMLUtils.getNodeText(xml.getFirstChild("text2"));
        }
      } catch (Exception e) {
        //Could not read in key and process license
      }
      //Set the edition text
      if (edition == null) {
        if ("true".equals(this.get("WEBSERVER.ASPMODE"))) {
          context.setAttribute("APP_TEXT", "Enterprise Edition");
        } else {
          context.removeAttribute("cfs.setup");
        }
      }
    }
    //Start the cron last
    if ("true".equals(this.get("CRON.ENABLED"))) {
      try {
        System.out.println("InitHook-> Starting CRON");
        Crontab crontab = Crontab.getInstance();
        Properties jcronProperties = new Properties();
        jcronProperties.setProperty("org.jcrontab.Crontab.refreshFrequency", "3");
        //Specify the cron items are in the gatekeeper database
        jcronProperties.setProperty("org.jcrontab.data.datasource", "org.aspcfs.jcrontab.datasource.CFSDatasource");
        jcronProperties.setProperty("org.jcrontab.data.GenericSQLSource.driver", this.get("GATEKEEPER.DRIVER"));
        jcronProperties.setProperty("org.jcrontab.data.GenericSQLSource.url", this.get("GATEKEEPER.URL"));
        jcronProperties.setProperty("org.jcrontab.data.GenericSQLSource.username", this.get("GATEKEEPER.USER"));
        jcronProperties.setProperty("org.jcrontab.data.GenericSQLSource.password", this.get("GATEKEEPER.PASSWORD"));
        jcronProperties.setProperty("org.jcrontab.path.DefaultFilePath", StringUtils.toString(this.get("FILELIBRARY")));
        if (this.has("WEBSERVER.URL")) {
          jcronProperties.setProperty("org.jcrontab.path.WebServerUrl", this.get("WEBSERVER.URL"));
        } else {
          jcronProperties.setProperty("org.jcrontab.path.WebServerUrl", "127.0.0.1");
        }
        //jcron logger -- TODO: implement a database logger
        jcronProperties.setProperty("org.jcrontab.log.Logger", "org.jcrontab.log.DebugLogger");
        crontab.setConnectionPool(cp);
        crontab.setServletContext(context);
        crontab.init(jcronProperties);
        context.setAttribute("Crontab", crontab);
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
  }


  /**
   *  Adds a feature to the Parameter attribute of the InitHook object
   *
   *@param  context  The feature to be added to the Parameter attribute
   *@param  param    The feature to be added to the Parameter attribute
   *@param  value    The feature to be added to the Parameter attribute
   */
  private void addParameter(ServletContext context, String param, String value) {
    addParameter(context, param, value, null);
  }


  /**
   *  Adds a feature to the Parameter attribute of the InitHook object
   *
   *@param  context       The feature to be added to the Parameter attribute
   *@param  param         The feature to be added to the Parameter attribute
   *@param  value         The feature to be added to the Parameter attribute
   *@param  defaultValue  The feature to be added to the Parameter attribute
   */
  private void addParameter(ServletContext context, String param, String value, String defaultValue) {
    if (value != null) {
      context.setAttribute(param, value);
    } else {
      if (defaultValue != null) {
        context.setAttribute(param, defaultValue);
      } else {
        context.removeAttribute(param);
      }
    }
  }


  /**
   *  Gets the pref attribute of the ApplicationPrefs class
   *
   *@param  context  Description of the Parameter
   *@param  param    Description of the Parameter
   *@return          The pref value
   */
  public static String getPref(ServletContext context, String param) {
    ApplicationPrefs prefs = (ApplicationPrefs) context.getAttribute("applicationPrefs");
    if (prefs != null) {
      return prefs.get(param);
    } else {
      return null;
    }
  }


  /**
   *  Save a name/value pair to the Java Preferences store
   *
   *@param  name   Description of the Parameter
   *@param  value  Description of the Parameter
   *@return        Description of the Return Value
   */
  private static boolean savePref(String name, String value) {
    try {
      Preferences prefs = Preferences.userNodeForPackage(org.aspcfs.modules.setup.utils.Prefs.class);
      prefs.put(name, value);
      prefs.flush();
      return true;
    } catch (Exception e) {
      return false;
    }
  }


  /**
   *  Shows all of the preferences that have been cached
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();
    Iterator i = prefs.keySet().iterator();
    while (i.hasNext()) {
      String name = (String) i.next();
      String value = (String) prefs.get(name);
      sb.append(name + "=" + value + ls);
    }
    return sb.toString();
  }
}

