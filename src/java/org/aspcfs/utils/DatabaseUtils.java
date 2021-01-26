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
package org.aspcfs.utils;

import com.darkhorseventures.database.ConnectionPool;
import com.darkhorseventures.framework.actions.ActionContext;
import org.apache.log4j.Logger;
import org.aspcfs.utils.web.PagedListInfo;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ArrayList;

import org.aspcfs.modules.base.Constants;

/**
 * Useful methods for working with multiple databases and database fields
 *
 * @author matt rajkowski
 * @version $Id: DatabaseUtils.java,v 1.13 2002/11/04 13:21:16 mrajkowski Exp
 * $
 * @created March 18, 2002
 */
public class DatabaseUtils {

  private static Logger log = Logger.getLogger(org.aspcfs.utils.DatabaseUtils.class);
  public final static String CRLF = System.getProperty("line.separator");

  //Possible time in milies for SQL query
  static final long POSSIBLE_QUERY_TIME = 3000;

  // Quote symbols
  public final static String qsDefault = "\"";
  public final static String qsMySQL = "`";

  // Database types
  public final static int POSTGRESQL = 1;
  public final static int MSSQL = 2;
  public final static int ORACLE = 3;
  public final static int FIREBIRD = 4;
  public final static int DAFFODILDB = 5;
  public final static int DB2 = 6;
  public final static int MYSQL = 7;
  public final static int DERBY = 8;
  public final static int INTERBASE = 9;
  public final static String sqlReservedWords = ",language,password,level,type,position,second," +
      "minute,hour,month,dayofweek,year,length,message," +
      "active,role,number,module,section,value,size," +
      "version,display,parameter,action,global,access,lock,comment," +
      "offset,";
  //intervals
  public final static int DAY = 1;
  public final static int WEEK = 2;
  public final static int MONTH = 3;
  public final static int YEAR = 4;
  public final static int HOUR = 5;
  public final static int MINUTE = 6;
  public final static int SECOND = 7;


  /**
   * Gets the true attribute of the DatabaseUtils class
   *
   * @param db Description of Parameter
   * @return The true value
   */
  public static String getTrue(Connection db) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "true";
      case DatabaseUtils.MSSQL:
        return "1";
      case DatabaseUtils.DAFFODILDB:
        return "true";
      case DatabaseUtils.ORACLE:
        return "1";
      case DatabaseUtils.DB2:
        return "'1'";
      case DatabaseUtils.FIREBIRD:
        return "'Y'";
      case DatabaseUtils.MYSQL:
        return "1";
      case DatabaseUtils.DERBY:
        return "'1'";
      case DatabaseUtils.INTERBASE:
        return "true";
      default:
        return "true";
    }
  }


  /**
   * Gets the false attribute of the DatabaseUtils class
   *
   * @param db Description of Parameter
   * @return The false value
   */
  public static String getFalse(Connection db) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "false";
      case DatabaseUtils.MSSQL:
        return "0";
      case DatabaseUtils.DAFFODILDB:
        return "false";
      case DatabaseUtils.ORACLE:
        return "0";
      case DatabaseUtils.DB2:
        return "'0'";
      case DatabaseUtils.FIREBIRD:
        return "'N'";
      case DatabaseUtils.MYSQL:
        return "0";
      case DatabaseUtils.DERBY:
        return "'0'";
      case DatabaseUtils.INTERBASE:
        return "false";
      default:
        return "false";
    }
  }


  /**
   * Gets the currentTimestamp attribute of the DatabaseUtils class
   *
   * @param db Description of Parameter
   * @return The currentTimestamp value
   */
  public static String getCurrentTimestamp(Connection db) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "CURRENT_TIMESTAMP";
      case DatabaseUtils.MSSQL:
        return "CURRENT_TIMESTAMP";
      case DatabaseUtils.DAFFODILDB:
        return "CURRENT_TIMESTAMP";
      case DatabaseUtils.ORACLE:
        return "CURRENT_TIMESTAMP";
      case DatabaseUtils.DB2:
        return "CURRENT_TIMESTAMP";
      case DatabaseUtils.FIREBIRD:
        return "CURRENT_TIMESTAMP";
      case DatabaseUtils.MYSQL:
        return "CURRENT_TIMESTAMP";
      case DatabaseUtils.DERBY:
        return "CURRENT_TIMESTAMP";
      case DatabaseUtils.INTERBASE:
        return "CURRENT_TIMESTAMP";
      default:
        return "CURRENT_TIMESTAMP";
    }
  }


  /**
   * Gets the type attribute of the DatabaseUtils class
   *
   * @param db Description of Parameter
   * @return The type value
   */
  public static int getType(Connection db) {
    return POSTGRESQL;
  }


  /**
   * Gets the typeName attribute of the DatabaseUtils class
   *
   * @param db Description of the Parameter
   * @return The typeName value
   */
  public static String getTypeName(Connection db) {
    return "postgresql";
  }


  /**
   * Description of the Method
   *
   * @param db   Description of the Parameter
   * @param date Description of the Parameter
   * @return Description of the Return Value
   */
  public static String castDateTimeToDate(Connection db, String date) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return (date + "::date");
      case DatabaseUtils.MSSQL:
        return ("CONVERT(char(10), " + date + ", 101)");
      case DatabaseUtils.FIREBIRD:
        return ("EXTRACT(DATE FROM " + date + ")");
      case DatabaseUtils.DAFFODILDB:
        return ("DATE(" + date + ")");
      case DatabaseUtils.ORACLE:
        return ("TO_DATE(" + date + ",'dd/mm/yyyy')");
      //case DatabaseUtils.ORACLE:
      //  return ("CAST(" + date + " AS DATE)");
      case DatabaseUtils.DB2:
        return ("CAST(" + date + " AS DATE)");
      //case DatabaseUtils.ORACLE:
      //  return ("CAST(" + date + " AS DATE)");
      case DatabaseUtils.MYSQL:
        return ("DATE(" + date + ")");
      case DatabaseUtils.DERBY:
        return ("DATE(" + date + ")");
      case DatabaseUtils.INTERBASE:
        return ("CAST(" + date + " AS DATE)");
      default:
        return "";
    }
  }


  /**
   * Database specific method of adding an interval in one field to a date
   * field of the same record, using the specified units
   *
   * @param db                  The feature to be added to the
   *                            TimestampInterval attribute
   * @param units               The feature to be added to the
   *                            TimestampInterval attribute
   * @param termsColumnName     The feature to be added to the
   *                            TimestampInterval attribute
   * @param timestampColumnName The feature to be added to the
   *                            TimestampInterval attribute
   * @return Description of the Return Value
   */
  public static String addTimestampInterval(Connection db, int units, String termsColumnName, String timestampColumnName) {
    // TODO: report why +1 is being used
    String addTimestampIntervalString = "";
    String customUnits = "";
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        if (units == DAY) {
          customUnits = "days";
        } else if (units == WEEK) {
          customUnits = "weeks";
        } else if (units == MONTH) {
          customUnits = "months";
        } else if (units == YEAR) {
          customUnits = "years";
        }
        addTimestampIntervalString = timestampColumnName + " + ( (" + termsColumnName + " + 1 )::text || ' " + customUnits + "')::interval ";
        break;
      case DatabaseUtils.MSSQL:
        if (units == DAY) {
        } else if (units == WEEK) {
          customUnits = "WEEK";
        } else if (units == MONTH) {
          customUnits = "MONTH";
        } else if (units == YEAR) {
          customUnits = "YEAR";
        }
        addTimestampIntervalString = " DATEADD(" + customUnits + ",(" + termsColumnName + " + 1)," + timestampColumnName + ")";
        break;
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        if (units == DAY) {
          addTimestampIntervalString = " (" + timestampColumnName + " + " + termsColumnName + ") ";
        } else if (units == WEEK) {
          addTimestampIntervalString = " (" + timestampColumnName + " + (" + termsColumnName + " * 7)) ";
        } else if (units == MONTH) {
          // NOTE: approximate function to add a month
          addTimestampIntervalString = " (" + timestampColumnName + " + (" + termsColumnName + " * 30)) ";
        } else if (units == YEAR) {
          // NOTE: approximate function to add a year
          addTimestampIntervalString = " (" + timestampColumnName + " + (" + termsColumnName + " * 365)) ";
        }
        break;
      case DatabaseUtils.DAFFODILDB:
        if (units == DAY) {
        } else if (units == WEEK) {
          customUnits = "SQL_TSI_WEEK";
        } else if (units == MONTH) {
          customUnits = "SQL_TSI_MONTH";
        } else if (units == YEAR) {
          customUnits = "SQL_TSI_YEAR";
        }
        addTimestampIntervalString = " TIMESTAMPADD(" + customUnits + ",(" + termsColumnName + " + 1)," + timestampColumnName + ")";
        break;
      case DatabaseUtils.DB2:
        if (units == DAY) {
          addTimestampIntervalString = timestampColumnName + " + (" + termsColumnName + "+1) day ";
        } else if (units == WEEK) {
          addTimestampIntervalString = timestampColumnName + " + ((" + termsColumnName + "+1)*7) day ";
        } else if (units == MONTH) {
          addTimestampIntervalString = timestampColumnName + " + (" + termsColumnName + "+1) month ";
        } else if (units == YEAR) {
          addTimestampIntervalString = timestampColumnName + " + (" + termsColumnName + "+1) year ";
        }
        break;
      case DatabaseUtils.ORACLE:
        if (units == DAY) {
          addTimestampIntervalString = " (" + timestampColumnName + " + NUMTODSINTERVAL(" + termsColumnName + " ,'day')) ";
        } else if (units == WEEK) {
          addTimestampIntervalString = " (" + timestampColumnName + " + NUMTODSINTERVAL(" + termsColumnName + " * 7 ,'day'))";
        } else if (units == MONTH) {
          addTimestampIntervalString = " (" + timestampColumnName + " + NUMTOYMINTERVAL(" + termsColumnName + ", 'month')) ";
        } else if (units == YEAR) {
          addTimestampIntervalString = " (" + timestampColumnName + " + NUMTOYMINTERVAL(" + termsColumnName + ", 'year')) ";
        }
        break;
      case DatabaseUtils.MYSQL:
        if (units == DAY) {
        } else if (units == WEEK) {
          customUnits = "WEEK";
        } else if (units == MONTH) {
          customUnits = "MONTH";
        } else if (units == YEAR) {
          customUnits = "YEAR";
        }
        addTimestampIntervalString = " ADDDATE(" + timestampColumnName + ", INTERVAL " + termsColumnName + "+1 " + customUnits + ")";
        break;
      case DatabaseUtils.DERBY:
        if (units == DAY) {
          customUnits = "SQL_TSI_DAY";
        } else if (units == WEEK) {
          customUnits = "SQL_TSI_WEEK";
        } else if (units == MONTH) {
          customUnits = "SQL_TSI_MONTH";
        } else if (units == YEAR) {
          customUnits = "SQL_TSI_YEAR";
        }
        addTimestampIntervalString = " {fn TIMESTAMPADD(" + customUnits + ", CAST(" + termsColumnName + "+1 AS INTEGER), " + timestampColumnName + ")}";
        break;
    }
    return addTimestampIntervalString;
  }


  /**
   * Database specific method of adding an interval in one field to a date
   * field of the same record, using the specified units
   *
   * @param db                  The feature to be added to the
   *                            TimestampInterval attribute
   * @param units               The feature to be added to the
   *                            TimestampInterval attribute
   * @param termsColumnName     The feature to be added to the
   *                            TimestampInterval attribute
   * @param timestampColumnName The feature to be added to the
   *                            TimestampInterval attribute
   * @param defaultUnits        The feature to be added to the
   *                            TimestampInterval attribute
   * @param defaultTerms        The feature to be added to the
   *                            TimestampInterval attribute
   * @return Description of the Return Value
   */
  public static String addTimestampInterval(Connection db, int units, String termsColumnName, String timestampColumnName, String defaultUnits, long defaultTerms) {
    String addTimestampIntervalString = "";
    String customUnits = "";
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        if (units == WEEK) {
          customUnits = "weeks";
        }
        addTimestampIntervalString = timestampColumnName + " + ( (" + termsColumnName + " + " + (defaultTerms + 1) + " )::text || ' " + customUnits + "')::interval ";
        break;
      case DatabaseUtils.MSSQL:
        if (units == WEEK) {
          customUnits = "WEEK";
        }
        addTimestampIntervalString = " DATEADD(" + customUnits + ",(" + termsColumnName + " + " + (defaultTerms + 1) + ")," + timestampColumnName + ")";
        break;
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        addTimestampIntervalString = " (" + timestampColumnName + " + ((" + termsColumnName + " + " + defaultTerms + 1 + ") * 7)) ";
        break;
      case DatabaseUtils.DAFFODILDB:
        if (units == WEEK) {
          customUnits = "SQL_TSI_WEEK";
        }
        addTimestampIntervalString = " TIMESTAMPADD(" + customUnits + ",(" + termsColumnName + " + " + (defaultTerms + 1) + ")," + timestampColumnName + ")";
        break;
      case DatabaseUtils.DB2:
        if (units == WEEK) {
          addTimestampIntervalString = timestampColumnName + " + ((" + termsColumnName + "+" + defaultTerms + "+1)*7) day ";
        }
        break;
      case DatabaseUtils.ORACLE:
        addTimestampIntervalString = " (" + timestampColumnName + " + ((" + termsColumnName + " + " + defaultTerms + 1 + ") * 7)) ";
        break;
      case DatabaseUtils.MYSQL:
        if (units == WEEK) {
          customUnits = "WEEK";
        }
        addTimestampIntervalString = " ADDDATE(" + timestampColumnName + ", INTERVAL (" + termsColumnName + " + " + (defaultTerms + 1) + ") " + customUnits + ")";
        break;
      case DatabaseUtils.DERBY:
        if (units == WEEK) {
          customUnits = "SQL_TSI_WEEK";
        }
        addTimestampIntervalString = "{fn TIMESTAMPADD(" + customUnits + ", CAST(" + termsColumnName + " + " + (defaultTerms + 1) + " AS INTEGER)," + timestampColumnName + ")}";
        break;
    }
    return addTimestampIntervalString;
  }


  /**
   * Gets the nextSeq attribute of the DatabaseUtils class, used before an
   * insert statement has been executed
   *
   * @param db               Description of the Parameter
   * @param origSequenceName Description of the Parameter
   * @return The nextSeq value
   * @throws SQLException Description of the Exception
   */
  public static int getNextSeq(Connection db, String origSequenceName) throws SQLException {
    int typeId = DatabaseUtils.getType(db);
    if (typeId == POSTGRESQL || typeId == MSSQL || typeId == MYSQL || typeId == DERBY) {
      return -1;
    }
    int id = -1;
    Statement st = db.createStatement();
    ResultSet rs = null;
    String sequenceName = getSequenceName(db, origSequenceName);
    switch (typeId) {
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        rs = st.executeQuery(
            "SELECT GEN_ID (" + sequenceName + ",1) FROM RDB$DATABASE");
        break;
      case DatabaseUtils.DAFFODILDB:
        rs = st.executeQuery("SELECT " + sequenceName + ".nextval from dual");
        break;
      case DatabaseUtils.ORACLE:
        rs = st.executeQuery("SELECT " + sequenceName + ".nextval from dual");
        break;
      case DatabaseUtils.DB2:
        rs = st.executeQuery("VALUES NEXTVAL FOR " + sequenceName);
        break;
      default:
        break;
    }
    if (rs.next()) {
      id = rs.getInt(1);
    }
    rs.close();
    st.close();
    return id;
  }

  public static String getSequenceName(Connection db, String sequenceName) {
    int typeId = DatabaseUtils.getType(db);
    switch (typeId) {
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        // interbase actually allows 64 character names, but since we are using the same db scripts...
        if (sequenceName.length() > 31) {
          String seqPart1 = sequenceName.substring(0, 13);
          String seqPart2 = sequenceName.substring(14);
          sequenceName = seqPart1 + "_" + seqPart2.substring(seqPart2.length() - 17);
        }
        break;
      case DatabaseUtils.DAFFODILDB:
        break;
      case DatabaseUtils.ORACLE:
        if (sequenceName.length() > 30) {
          String seqPart1 = sequenceName.substring(0, 13);
          String seqPart2 = sequenceName.substring(14);
          sequenceName = seqPart1 + "_" + seqPart2.substring(seqPart2.length() - 16);
        }
        break;
      case DatabaseUtils.DB2:
        if (sequenceName.length() > 30) {
          String seqPart1 = sequenceName.substring(0, 13);
          String seqPart2 = sequenceName.substring(14);
          sequenceName = seqPart1 + "_" + seqPart2.substring(seqPart2.length() - 16);
        }
        break;
      default:
        break;
    }
    return sequenceName;
  }


  /**
   * Gets the currVal attribute of the DatabaseUtils class, used after an
   * insert statement has been executed
   *
   * @param db           Description of the Parameter
   * @param sequenceName Description of the Parameter
   * @param defaultValue Description of the Parameter
   * @return The currVal value
   * @throws SQLException Description of the Exception
   */
  public static int getCurrVal(Connection db, String sequenceName, int defaultValue) throws SQLException {
    int typeId = DatabaseUtils.getType(db);
    if (typeId != POSTGRESQL && typeId != MSSQL && typeId != MYSQL && typeId != DERBY) {
      return defaultValue;
    }
    int id = -1;
    Statement st = db.createStatement();
    ResultSet rs = null;
    switch (typeId) {
      case DatabaseUtils.POSTGRESQL:
        rs = st.executeQuery("SELECT currval('" + sequenceName + "')");
        break;
      case DatabaseUtils.MSSQL:
        rs = st.executeQuery("SELECT @@IDENTITY");
        break;
      case DatabaseUtils.MYSQL:
        rs = st.executeQuery("SELECT LAST_INSERT_ID()");
        break;
      case DatabaseUtils.DERBY:
        rs = st.executeQuery("VALUES IDENTITY_VAL_LOCAL()");
        break;
      default:
        break;
    }
    if (rs.next()) {
      id = rs.getInt(1);
    }
    rs.close();
    st.close();
    return id;
  }


  /**
   * Useful when generating a SQL order by clause to sort by year for the given
   * timestamp field
   *
   * @param db        Description of the Parameter
   * @param fieldname Description of the Parameter
   * @return The yearPart value
   */
  public static String getYearPart(Connection db, String fieldname) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "date_part('year', " + fieldname + ")";
      case DatabaseUtils.MSSQL:
        return "DATEPART(YY, " + fieldname + ")";
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        return "EXTRACT(YEAR FROM " + fieldname + ")";
      case DatabaseUtils.DAFFODILDB:
        return "YEAR(" + fieldname + ")";
      case DatabaseUtils.ORACLE:
        return "EXTRACT(YEAR FROM " + fieldname + ")";
      case DatabaseUtils.DB2:
        return "YEAR(" + fieldname + ")";
      case DatabaseUtils.MYSQL:
        return "YEAR(" + fieldname + ")";
      case DatabaseUtils.DERBY:
        return "YEAR(" + fieldname + ")";
      default:
        return "";
    }
  }


  /**
   * Useful when a temporary table name needs to be determined based on the
   * database type
   *
   * @param db        Description of the Parameter
   * @param tableName Description of the Parameter
   * @return The yearPart value
   */
  public static String getTempTableName(Connection db, String tableName) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return tableName;
      case DatabaseUtils.MSSQL:
        return "#" + tableName;
      case DatabaseUtils.FIREBIRD:
        return "";
      case DatabaseUtils.DAFFODILDB:
        return "";
      case DatabaseUtils.ORACLE:
        return "";
      case DatabaseUtils.DB2:
        return "SESSION." + tableName;
      case DatabaseUtils.MYSQL:
        return "";
      case DatabaseUtils.DERBY:
        return "";
      case DatabaseUtils.INTERBASE:
        // ib does support temporary tables?
        return "";
      default:
        return "";
    }
  }

  /**
   * Useful when generating a SQL order by clause to sort by month for the
   * given timestamp field
   *
   * @param db        Description of the Parameter
   * @param fieldname Description of the Parameter
   * @return The orderByMonth value
   */
  public static String getMonthPart(Connection db, String fieldname) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "date_part('month', " + fieldname + ")";
      case DatabaseUtils.MSSQL:
        return "DATEPART(MM, " + fieldname + ")";
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        return "EXTRACT(MONTH FROM " + fieldname + ")";
      case DatabaseUtils.DAFFODILDB:
        return "MONTH(" + fieldname + ")";
      case DatabaseUtils.ORACLE:
        return "EXTRACT(MONTH FROM " + fieldname + ")";
      case DatabaseUtils.DB2:
        return "MONTH(" + fieldname + ")";
      case DatabaseUtils.MYSQL:
        return "MONTH(" + fieldname + ")";
      case DatabaseUtils.DERBY:
        return "MONTH(" + fieldname + ")";
      default:
        return "";
    }
  }


  /**
   * Useful when generating a SQL order by clause to sort by day for the given
   * timestamp field
   *
   * @param db        Description of the Parameter
   * @param fieldname Description of the Parameter
   * @return The orderByDay value
   */
  public static String getDayPart(Connection db, String fieldname) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "date_part('day', " + fieldname + ")";
      case DatabaseUtils.MSSQL:
        return "DATEPART(DD, " + fieldname + ")";
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        return "EXTRACT(DAY FROM " + fieldname + ")";
      case DatabaseUtils.DAFFODILDB:
        return "DAYOFWEEK(" + fieldname + ")";
      case DatabaseUtils.ORACLE:
        return "EXTRACT(DAY FROM " + fieldname + ")";
      case DatabaseUtils.DB2:
        return "DAY(" + fieldname + ")";
      case DatabaseUtils.MYSQL:
        return "DAY(" + fieldname + ")";
      case DatabaseUtils.DERBY:
        return "DAY(" + fieldname + ")";
      default:
        return "";
    }
  }


  /**
   * Useful when generating a SQL order by clause to sort by hour of day for
   * the given timestamp field
   *
   * @param db        Description of the Parameter
   * @param fieldname Description of the Parameter
   * @return The dayPart value
   */
  public static String getHourPart(Connection db, String fieldname) {
    //TODO: Verify if all databases work
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "date_part('hour', " + fieldname + ")";
      case DatabaseUtils.MSSQL:
        return "DATEPART(HH, " + fieldname + ")";
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        return "EXTRACT(HOUR FROM " + fieldname + ")";
      case DatabaseUtils.DAFFODILDB:
        //return "DAYOFWEEK(" + fieldname + ")";
      case DatabaseUtils.ORACLE:
        return "EXTRACT(HOUR FROM " + fieldname + ")";
      case DatabaseUtils.DB2:
        return "HOUR(" + fieldname + ")";
      case DatabaseUtils.MYSQL:
        return "HOUR(" + fieldname + ")";
      case DatabaseUtils.DERBY:
        return "HOUR(" + fieldname + ")";
      default:
        return "";
    }
  }


  /**
   * Useful when generating a SQL order by clause to sort by minutes for the
   * given timestamp field
   *
   * @param db        Description of the Parameter
   * @param fieldname Description of the Parameter
   * @return The dayPart value
   */
  public static String getMinutePart(Connection db, String fieldname) {
    //TODO: Verify if all databases work
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "date_part('minute', " + fieldname + ")";
      case DatabaseUtils.MSSQL:
        return "DATEPART(M, " + fieldname + ")";
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        return "EXTRACT(MINUTE FROM " + fieldname + ")";
      case DatabaseUtils.DAFFODILDB:
        return "DAYOFWEEK(" + fieldname + ")";
      case DatabaseUtils.ORACLE:
        return "EXTRACT(MINUTE FROM " + fieldname + ")";
      case DatabaseUtils.DB2:
        return "MINUTE(" + fieldname + ")";
      case DatabaseUtils.MYSQL:
        return "MINUTE(" + fieldname + ")";
      case DatabaseUtils.DERBY:
        return "MINUTE(" + fieldname + ")";
      default:
        return "";
    }
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @return Description of the Return Value
   */
  public static String toLowerCase(Connection db) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "lower";
      case DatabaseUtils.MSSQL:
        return "lower";
      case DatabaseUtils.ORACLE:
        return "lower";
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        return "lower";
      case DatabaseUtils.DAFFODILDB:
        return "lcase";
      case DatabaseUtils.DB2:
        return "lower";
      case DatabaseUtils.MYSQL:
        return "lower";
      case DatabaseUtils.DERBY:
        return "lcase";
      default:
        return "lower";
    }
  }


  /**
   * Description of the Method
   *
   * @param db    Description of the Parameter
   * @param field Description of the Parameter
   * @return Description of the Return Value
   */
  public static String toLowerCase(Connection db, String field) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "lower(" + field + ")";
      case DatabaseUtils.MSSQL:
        return "lower(" + field + ")";
      case DatabaseUtils.ORACLE:
        return "lower(" + field + ")";
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        return "lower(" + field + ")";
      case DatabaseUtils.DAFFODILDB:
        return "lcase(" + field + ")";
      case DatabaseUtils.DB2:
        return "lower(" + field + ")";
      case DatabaseUtils.MYSQL:
        return "lower(" + field + ")";
      case DatabaseUtils.DERBY:
        return "LCASE(" + field + ")";
      default:
        return "lower(" + field + ")";
    }
  }


  /**
   * Gets the substring function for the given database
   *
   * @param db    Description of the Parameter
   * @param field Description of the Parameter
   * @param first Description of the Parameter
   * @param size  Description of the Parameter
   * @return The subString value
   */
  public static String getSubString(Connection db, String field, int first, int size) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return "substr(" + field + "," + first + (size < 0 ? "" : "," + size) + ") ";
      case DatabaseUtils.MSSQL:
        return "substring(" + field + "," + first + (size < 0 ? "" : "," + size) + ") ";
      case DatabaseUtils.ORACLE:
        return "substr(" + field + "," + first + (size < 0 ? "" : "," + size) + ") ";
      case DatabaseUtils.FIREBIRD:
        return "substr(" + field + " FROM " + first + (size < 0 ? "" : " FOR " + size) + " ) ";
      case DatabaseUtils.DAFFODILDB:
        return "substring(" + field + "," + first + (size < 0 ? "" : "," + size) + ") ";
      case DatabaseUtils.DB2:
        return "substr(" + field + "," + (first + 1) + (size < 0 ? "" : "," + size) + ") ";
      case DatabaseUtils.MYSQL:
        return "substr(" + field + "," + first + (size < 0 ? "" : "," + size) + ") ";
      case DatabaseUtils.DERBY:
        return "substr(" + field + "," + (first + 1) + (size < 0 ? "" : "," + size) + ") ";
      case DatabaseUtils.INTERBASE:
        return "substr(" + field + "," + (first + 1) + (size < 0 ? ", 32767" : "," + size) + " )";
      default:
        return "substr(" + field + "," + first + (size < 0 ? "" : "," + size) + ") ";
    }
  }


  /**
   * If a text column needs to be sorted, then in some databases it must be
   * converted to something sortable
   *
   * @param db    Description of the Parameter
   * @param field Description of the Parameter
   * @return Description of the Return Value
   */
  public static String convertToVarChar(Connection db, String field) {
    switch (DatabaseUtils.getType(db)) {
      case DatabaseUtils.POSTGRESQL:
        return field;
      case DatabaseUtils.MSSQL:
        return "CONVERT(VARCHAR(2000), " + field + ")";
      case DatabaseUtils.ORACLE:
        return "TO_CHAR(" + field + ")";
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        return field;
      case DatabaseUtils.DB2:
        return "CAST(" + field + " AS VARCHAR(32000))";
      case DatabaseUtils.DAFFODILDB:
        // TODO: This doesn't work for DaffodilDB, so use a VARCHAR(4192) instead of CLOB
        return field;
      case DatabaseUtils.MYSQL:
        // NOTE: If MYSQL has a problem then get a substring
        //return "SUBSTR(" + field + ", 2000, 1)";
        return field;
      case DatabaseUtils.DERBY:
        return "CAST(" + field + " AS VARCHAR(32000))";
      default:
        return field;
    }

  }


  /**
   * Description of the Method
   *
   * @param tmp          Description of the Parameter
   * @param defaultValue Description of the Parameter
   * @return Description of the Return Value
   */
  public static int parseInt(String tmp, int defaultValue) {
    try {
      return Integer.parseInt(tmp);
    } catch (Exception e) {
      return defaultValue;
    }
  }


  /**
   * Description of the Method
   *
   * @param tmp Description of the Parameter
   * @return Description of the Return Value
   */
  public static boolean parseBoolean(String tmp) {
    return ("ON".equalsIgnoreCase(tmp) ||
        "TRUE".equalsIgnoreCase(tmp) ||
        "1".equals(tmp) ||
        "Y".equalsIgnoreCase(tmp) ||
        "YES".equalsIgnoreCase(tmp));
  }


  /**
   * Description of the Method
   *
   * @param tmp Description of the Parameter
   * @return Description of the Return Value
   */
  public static java.sql.Date parseDate(String tmp) {
    java.sql.Date dateValue = null;
    try {
      java.util.Date tmpDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(
          tmp);
      dateValue = new java.sql.Date(new java.util.Date().getTime());
      dateValue.setTime(tmpDate.getTime());
      return dateValue;
    } catch (Exception e) {
      try {
        return java.sql.Date.valueOf(tmp);
      } catch (Exception e2) {
      }
    }
    return null;
  }


  /**
   * Description of the Method
   *
   * @param tmp Description of the Parameter
   * @return Description of the Return Value
   */
  public static java.sql.Timestamp parseTimestamp(String tmp) {
    return parseTimestamp(tmp, Locale.getDefault());
  }


  /**
   * Description of the Method
   *
   * @param tmp    Description of the Parameter
   * @param locale Description of the Parameter
   * @return Description of the Return Value
   */
  public static java.sql.Timestamp parseTimestamp(String tmp, Locale locale) {
    java.sql.Timestamp timestampValue = null;
    try {
      java.util.Date tmpDate = DateFormat.getDateTimeInstance(
          DateFormat.SHORT, DateFormat.LONG, locale).parse(tmp);
      timestampValue = new java.sql.Timestamp(new java.util.Date().getTime());
      timestampValue.setTime(tmpDate.getTime());
      return timestampValue;
    } catch (Exception e) {
      try {
        return java.sql.Timestamp.valueOf(tmp);
      } catch (Exception e2) {
      }
    }
    return null;
  }


  /**
   * Description of the Method
   *
   * @param tmp       Description of the Parameter
   * @param locale    Description of the Parameter
   * @param beLenient Description of the Parameter
   * @return Description of the Return Value
   */
  public static java.sql.Timestamp parseTimestamp(String tmp, Locale locale, boolean beLenient) {
    java.sql.Timestamp timestampValue = null;
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", locale);
      simpleDateFormat.setLenient(beLenient);
      java.util.Date tmpDate = simpleDateFormat.parse(tmp);
      timestampValue = new java.sql.Timestamp(new java.util.Date().getTime());
      timestampValue.setTime(tmpDate.getTime());
      return timestampValue;
    } catch (Exception e) {
      try {
        return java.sql.Timestamp.valueOf(tmp);
      } catch (Exception e2) {
      }
    }
    return null;
  }


  /**
   * Description of the Method
   *
   * @param tmp Description of the Parameter
   * @return Description of the Return Value
   */
  public static java.sql.Timestamp parseDateToTimestamp(String tmp) {
    return parseDateToTimestamp(tmp, Locale.getDefault());
  }


  /**
   * Description of the Method
   *
   * @param tmp    Description of the Parameter
   * @param locale Description of the Parameter
   * @return Description of the Return Value
   */
  public static java.sql.Timestamp parseDateToTimestamp(String tmp, Locale locale) {
    java.sql.Timestamp timestampValue = DatabaseUtils.parseTimestamp(
        tmp, locale);
    if (timestampValue == null) {
      try {
        DateFormat tmpDateFormat = DateFormat.getDateInstance(
            DateFormat.SHORT, locale);
        tmpDateFormat.setLenient(false);
        java.util.Date tmpDate = tmpDateFormat.parse(tmp);
        timestampValue = new java.sql.Timestamp(System.currentTimeMillis());
        timestampValue.setTime(tmpDate.getTime());
        timestampValue.setNanos(0);
        return timestampValue;
      } catch (Exception e) {
      }
    }
    return timestampValue;
  }


  /**
   * Gets the int attribute of the DatabaseUtils class
   *
   * @param rs           Description of the Parameter
   * @param column       Description of the Parameter
   * @param defaultValue Description of the Parameter
   * @return The int value
   * @throws SQLException Description of the Exception
   */
  public static int getInt(ResultSet rs, String column, int defaultValue) throws SQLException {
    int fieldValue = rs.getInt(column);
    if (rs.wasNull()) {
      fieldValue = defaultValue;
    }
    return fieldValue;
  }


  /**
   * Gets the double attribute of the DatabaseUtils class
   *
   * @param rs           Description of the Parameter
   * @param column       Description of the Parameter
   * @param defaultValue Description of the Parameter
   * @return The double value
   * @throws SQLException Description of the Exception
   */
  public static double getDouble(ResultSet rs, String column, double defaultValue) throws SQLException {
    double fieldValue = rs.getDouble(column);
    if (rs.wasNull()) {
      fieldValue = defaultValue;
    }
    return fieldValue;
  }


  /**
   * Gets the int attribute of the DatabaseUtils class
   *
   * @param rs     Description of the Parameter
   * @param column Description of the Parameter
   * @return The int value
   * @throws SQLException Description of the Exception
   */
  public static int getInt(ResultSet rs, String column) throws SQLException {
    return DatabaseUtils.getInt(rs, column, -1);
  }


  /**
   * Gets the double attribute of the DatabaseUtils class
   *
   * @param rs     Description of the Parameter
   * @param column Description of the Parameter
   * @return The double value
   * @throws SQLException Description of the Exception
   */
  public static double getDouble(ResultSet rs, String column) throws SQLException {
    return DatabaseUtils.getDouble(rs, column, -1.0);
  }


  /**
   * Gets the long attribute of the DatabaseUtils class
   *
   * @param rs     Description of the Parameter
   * @param column Description of the Parameter
   * @return The long value
   * @throws SQLException Description of the Exception
   */
  public static long getLong(ResultSet rs, String column) throws SQLException {
    return DatabaseUtils.getLong(rs, column, -1);
  }


  /**
   * Gets the long attribute of the DatabaseUtils class
   *
   * @param rs           Description of the Parameter
   * @param column       Description of the Parameter
   * @param defaultValue Description of the Parameter
   * @return The long value
   * @throws SQLException Description of the Exception
   */
  public static long getLong(ResultSet rs, String column, long defaultValue) throws SQLException {
    long fieldValue = rs.getLong(column);
    if (rs.wasNull()) {
      fieldValue = defaultValue;
    }
    return fieldValue;
  }


  /**
   * Sets the int attribute of the DatabaseUtils class
   *
   * @param pst        The new int value
   * @param paramCount The new int value
   * @param value      The new int value
   * @throws SQLException Description of the Exception
   */
  public static void setInt(PreparedStatement pst, int paramCount, int value) throws SQLException {
    if (value == -1) {
      pst.setNull(paramCount, java.sql.Types.INTEGER);
    } else {
      pst.setInt(paramCount, value);
    }
  }


  /**
   * Sets the double attribute of the DatabaseUtils class
   *
   * @param pst        The new double value
   * @param paramCount The new double value
   * @param value      The new double value
   * @throws SQLException Description of the Exception
   */
  public static void setDouble(PreparedStatement pst, int paramCount, double value) throws SQLException {
    if (value == -1.0) {
      pst.setNull(paramCount, java.sql.Types.DOUBLE);
    } else {
      pst.setDouble(paramCount, value);
    }
  }


  /**
   * Sets the long attribute of the DatabaseUtils class
   *
   * @param pst        The new long value
   * @param paramCount The new long value
   * @param value      The new long value
   * @throws SQLException Description of the Exception
   */
  public static void setLong(PreparedStatement pst, int paramCount, long value) throws SQLException {
    if (value == -1) {
      pst.setNull(paramCount, java.sql.Types.INTEGER);
    } else {
      pst.setLong(paramCount, value);
    }
  }


  /**
   * Sets the timestamp attribute of the DatabaseUtils class
   *
   * @param pst        The new timestamp value
   * @param paramCount The new timestamp value
   * @param value      The new timestamp value
   * @throws SQLException Description of the Exception
   */
  public static void setTimestamp(PreparedStatement pst, int paramCount, java.sql.Timestamp value) throws SQLException {
    if (value == null) {
      pst.setNull(paramCount, java.sql.Types.DATE);
    } else {
      pst.setTimestamp(paramCount, value);
    }
  }


  /**
   * Sets the date attribute of the DatabaseUtils class
   *
   * @param pst        The new date value
   * @param paramCount The new date value
   * @param value      The new date value
   * @throws SQLException Description of the Exception
   */
  public static void setDate(PreparedStatement pst, int paramCount, java.sql.Date value) throws SQLException {
    if (value == null) {
      pst.setNull(paramCount, java.sql.Types.DATE);
    } else {
      pst.setDate(paramCount, value);
    }
  }


  /**
   * Reads in a text file of SQL statements from the filesystem, and executes
   * them
   *
   * @param db       Description of the Parameter
   * @param filename Description of the Parameter
   * @throws SQLException Description of the Exception
   * @throws IOException  Description of the Exception
   */
  public static void executeSQL(Connection db, String filename) throws SQLException, IOException {
    System.out.println("DatabaseUtils-> executeSQL: " + filename);
    BufferedReader in = new BufferedReader(new FileReader(filename));
    executeSQL(db, in);
    in.close();
  }


  /**
   * Reads in a text file of SQL statements from the servlet context, and
   * executes them
   *
   * @param db       Description of the Parameter
   * @param context  Description of the Parameter
   * @param filename Description of the Parameter
   * @throws SQLException Description of the Exception
   * @throws IOException  Description of the Exception
   */
  public static void executeSQL(Connection db, ServletContext context, String filename) throws SQLException, IOException {
    if (context == null) {
      DatabaseUtils.executeSQL(db, filename);
    } else {
      InputStream source = context.getResourceAsStream(filename);
      BufferedReader in = new BufferedReader(new InputStreamReader(source));
      executeSQL(db, in);
      in.close();
    }
  }

  public static void executeSQL(Connection db, URL url) throws SQLException, IOException {
    InputStream source = url.openStream();
    BufferedReader in = new BufferedReader(new InputStreamReader(source));
    executeSQL(db, in);
    in.close();
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @param in Description of the Parameter
   * @throws SQLException Description of the Exception
   * @throws IOException  Description of the Exception
   */
  public static void executeSQL(Connection db, BufferedReader in) throws SQLException, IOException {
    // Read the file and execute each statement
    StringBuffer sql = new StringBuffer();
    String line = null;
    Statement st = db.createStatement();
    int tCount = 0;
    int lineCount = 0;
    while ((line = in.readLine()) != null) {
      ++lineCount;
      // SQL Comment
      if (line.startsWith("//")) {
        continue;
      }
      // SQL Comment
      if (line.startsWith("--")) {
        continue;
      }
      sql.append(line);
      // check for delimiter
      if (line.trim().endsWith(";")) {
        // Got a transaction, so execute it
        ++tCount;
        try {
          st.execute(sql.substring(0, sql.lastIndexOf(";")));
        } catch (SQLException e) {
          System.out.println(
              "DatabaseUtils-> ERROR(1), line: " + lineCount + " message: " + e.getMessage());
          throw new SQLException(e.getMessage());
        }
        sql.setLength(0);
      } else if (line.trim().equals("GO")) {
        // Got a transaction, so execute it
        ++tCount;
        try {
          st.execute(sql.substring(0, sql.lastIndexOf("GO")));
        } catch (SQLException e) {
          System.out.println(
              "DatabaseUtils-> ERROR(2), line: " + lineCount + " message: " + e.getMessage());
          throw new SQLException(e.getMessage());
        }
        sql.setLength(0);
      } else {
        // Continue with another line
        sql.append(CRLF);
      }
    }
    // Statement didn't end with a delimiter
    if (sql.toString().trim().length() > 0 && !CRLF.equals(
        sql.toString().trim())) {
      ++tCount;
      try {
        st.execute(sql.toString());
      } catch (SQLException e) {
        System.out.println(
            "DatabaseUtils-> ERROR(3), line: " + lineCount + " message: " + e.getMessage());
        throw new SQLException(e.getMessage());
      }
    }
    st.close();
    System.out.println("Executed " + tCount + " total statements");
  }


  /**
   * Description of the Method
   *
   * @param context Description of the Parameter
   * @param db      Description of the Parameter
   */
  public static void renewConnection(ActionContext context, Connection db) {
    //Connections are usually checked out and expire, this will renew the expiration
    //time
    if (db != null) {
      ConnectionPool sqlDriver = (ConnectionPool) context.getServletContext().getAttribute(
          "ConnectionPool");
      if (sqlDriver != null) {
        sqlDriver.renew(db);
      }
    }
  }


  /**
   * Gets the tableName attribute of the DatabaseUtils class
   *
   * @param db        Description of the Parameter
   * @param tableName Description of the Parameter
   * @return The tableName value
   */
  public static String getTableName(Connection db, String tableName) {
    if (DatabaseUtils.getType(db) != FIREBIRD &&
        DatabaseUtils.getType(db) != ORACLE &&
        DatabaseUtils.getType(db) != DB2 &&
        DatabaseUtils.getType(db) != INTERBASE) {
      return tableName;
    }
    if (tableName.length() < 32) {
      return tableName;
    }
    if ("business_process_component_library".equals(tableName)) {
      return "business_process_comp_library";
    }
    if ("business_process_component_parameter".equals(tableName)) {
      return "business_pro_comp_parameter";
    }
    if ("business_process_component_result_lookup".equals(tableName)) {
      if (DatabaseUtils.getType(db) == FIREBIRD || DatabaseUtils.getType(db) == INTERBASE) {
        return "business_pro_comp_result_lookup";
      } else {
        return "business_pro_com_result_lookup";
      }
    }
    if ("business_process_parameter_library".equals(tableName)) {
      return "business_process_param_library";
    }
    if ("document_store_department_member".equals(tableName)) {
      return "doc_store_depart_member";
    }
    if ("lookup_document_store_permission".equals(tableName)) {
      return "lookup_doc_store_perm";
    }
    if ("lookup_document_store_permission_category".equals(tableName)) {
      return "lookup_doc_store_perm_cat";
    }
    if ("lookup_project_permission_category".equals(tableName)) {
      return "lookup_project_perm_category";
    }
    if ("lookup_opportunity_event_compelling".equals(tableName)) {
      return "lookup_opt_event_compelling";
    }
    if ("ticket_category_draft_assignment".equals(tableName)) {
      if (DatabaseUtils.getType(db) == FIREBIRD || DatabaseUtils.getType(db) == INTERBASE) {
        return "ticket_category_draf_assignment";
      } else {
        return "ticket_category_dra_assignment";
      }
    }
    System.out.println("DatabaseUtils-> Invalid table name: " + tableName);
    return tableName;
  }


  /**
   * Description of the Method
   *
   * @param db           Description of the Parameter
   * @param reservedWord Description of the Parameter
   * @return Description of the Return Value
   */
  public static String parseReservedWord(Connection db, String reservedWord) {
    if (DatabaseUtils.getType(db) == FIREBIRD ||
        DatabaseUtils.getType(db) == INTERBASE ||
        DatabaseUtils.getType(db) == ORACLE ||
        DatabaseUtils.getType(db) == DB2 ||
        DatabaseUtils.getType(db) == MYSQL ||
        DatabaseUtils.getType(db) == DERBY) {
      if (reservedWord.indexOf(".") > -1) {
        // Table is being specified with field
        String part1 = reservedWord.substring(0, reservedWord.indexOf("."));
        String part2 = reservedWord.substring(reservedWord.indexOf(".") + 1);
        return (part1 + "." + parseReservedWord(db, part2));
      }
      if (sqlReservedWords.indexOf("," + reservedWord + ",") != -1) {
        return addQuotes(db, reservedWord);
      }
    }
    return reservedWord;
  }


  /**
   * Gets the quote attribute of the DatabaseUtils class
   *
   * @param db Description of the Parameter
   * @return The quote value
   */
  private static String getQuote(Connection db) {
    String quoteSymbol = "";
    switch (DatabaseUtils.getType(db)) {
      case MYSQL:
        quoteSymbol = qsMySQL;
        break;
      default:
        quoteSymbol = qsDefault;
    }

    return quoteSymbol;
  }


  /**
   * Adds a feature to the Quotes attribute of the DatabaseUtils class
   *
   * @param db            The feature to be added to the Quotes attribute
   * @param stringToQuote The feature to be added to the Quotes attribute
   * @return Description of the Return Value
   */
  public static String addQuotes(Connection db, String stringToQuote) {
    String quoteSymbol = getQuote(db);
    return quoteSymbol + stringToQuote + quoteSymbol;
  }


  /**
   * Gets the connection attribute of the DatabaseUtils class
   *
   * @param dbUrl  Description of the Parameter
   * @param dbUser Description of the Parameter
   * @param dbPwd  Description of the Parameter
   * @return The connection value
   * @throws SQLException Description of the Exception
   */
  public static Connection getConnection(String dbUrl, String dbUser, String dbPwd) throws SQLException {
    Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
    /*
    System.out.println("DatabaseUtils-> Holdability: " + connection.getHoldability());
    */
    if (DatabaseUtils.getType(connection) == DatabaseUtils.MYSQL) {
      PreparedStatement pst = connection.prepareStatement("SELECT @@session.sql_mode;");
      String currentMode = "";
      ResultSet rs = pst.executeQuery();
      if (rs.next()) {
        currentMode = rs.getString(1);
      }
      rs.close();
      pst.close();

      pst = connection.prepareStatement(
          "SET sql_mode = '" + currentMode + ((currentMode.length() > 0) ? "," : "") + "ANSI_QUOTES,NO_AUTO_VALUE_ON_ZERO';");
      pst.execute();
      pst.close();
    } else if (DatabaseUtils.getType(connection) == DatabaseUtils.DB2) {
      /*
      connection.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
      */
    }
    return connection;
  }


  /**
   * Description of the Method
   *
   * @param db            DB Connection
   * @param rs            Description of Parameter
   * @param skipRowsCount Rows to skip
   * @throws SQLException Description of Exception
   */
  public static void skipRowsManual(Connection db, ResultSet rs, int skipRowsCount) throws SQLException {
    if (DatabaseUtils.getType(db) == DatabaseUtils.MSSQL ||
        DatabaseUtils.getType(db) == DatabaseUtils.DAFFODILDB ||
        DatabaseUtils.getType(db) == DatabaseUtils.DERBY ||
        DatabaseUtils.getType(db) == DatabaseUtils.ORACLE) {
      for (int skipCount = 0; skipCount < skipRowsCount; skipCount++) {
        rs.next();
      }
    }
  }

  /**
   * Description of the Method
   *
   * @param db           DB Connection
   * @param pst          Description of Parameter
   * @param maxRowsCount Count of rows to read
   * @throws SQLException Description of Exception
   */
  public static void doManualLimit(Connection db, PreparedStatement pst, int maxRowsCount) throws SQLException {
    if (DatabaseUtils.getType(db) == DatabaseUtils.DERBY) {
      pst.setMaxRows(maxRowsCount);
    }
  }


  public static String getTruncDateDialect(String dateColumn, int truncTo, int dbType) {
    String truncSQL = "";
    String dateFormat = "";
    switch (dbType) {
      case DatabaseUtils.POSTGRESQL:
        switch (truncTo) {
          case DatabaseUtils.DAY:
            dateFormat = "day";
            break;
          case DatabaseUtils.MONTH:
            dateFormat = "month";
            break;
          case DatabaseUtils.MINUTE:
            dateFormat = "minute";
            break;
          case DatabaseUtils.HOUR:
            dateFormat = "hour";
            break;
          case DatabaseUtils.YEAR:
            dateFormat = "year";
            break;
          default:
            return null;
        }
        truncSQL = "date_trunc('" + dateFormat + "'," + dateColumn + ")";
        break;
      case DatabaseUtils.MSSQL:
        switch (truncTo) {
          case DatabaseUtils.DAY:
            truncSQL = "CAST(CONVERT(varchar, " + dateColumn + ",101) AS DATETIME) ";
            break;
          case DatabaseUtils.MONTH:
            // TODO: fix this
            truncSQL = "CAST(CONVERT(varchar, " + dateColumn + ",101) AS DATETIME) ";
            break;
          case DatabaseUtils.MINUTE:
            truncSQL = "CAST(CONVERT(varchar, " + dateColumn + ",100) AS DATETIME) ";
            break;
          case DatabaseUtils.HOUR:
            // TODO: fix this
            truncSQL = "CAST(CONVERT(varchar, " + dateColumn + ",101) AS DATETIME) ";
            break;
          case DatabaseUtils.YEAR:
            // TODO: fix this
            truncSQL = "CAST(CONVERT(varchar, " + dateColumn + ",101) AS DATETIME) ";
            break;
          default:
            return null;
        }
        break;
      case DatabaseUtils.DAFFODILDB:
        switch (truncTo) {
          case DatabaseUtils.DAY:
            dateFormat = "DD";
            break;
          case DatabaseUtils.MONTH:
            dateFormat = "MM";
            break;
          case DatabaseUtils.MINUTE:
            dateFormat = "MI";
            break;
          case DatabaseUtils.HOUR:
            dateFormat = "HH";
            break;
          case DatabaseUtils.YEAR:
            dateFormat = "YYYY";
            break;
          default:
            return null;
        }
        truncSQL = "trunc(" + dateColumn + ",'" + dateFormat + "')";
        break;
      case DatabaseUtils.ORACLE:
        switch (truncTo) {
          case DatabaseUtils.DAY:
            dateFormat = "DD";
            break;
          case DatabaseUtils.MONTH:
            dateFormat = "MM";
            break;
          case DatabaseUtils.MINUTE:
            dateFormat = "MI";
            break;
          case DatabaseUtils.HOUR:
            dateFormat = "HH";
            break;
          case DatabaseUtils.YEAR:
            dateFormat = "YYYY";
            break;
          default:
            return null;
        }
        truncSQL = "trunc(" + dateColumn + ",'" + dateFormat + "')";
        break;
      case DatabaseUtils.DB2:
        switch (truncTo) {
          case DatabaseUtils.DAY:
            truncSQL = "DATE(" + dateColumn + ") ";
            break;
          default:
            return null;
        }
        break;
      case DatabaseUtils.FIREBIRD:
      case DatabaseUtils.INTERBASE:
        switch (truncTo) {
          case DatabaseUtils.DAY:
            truncSQL = "CAST(" + dateColumn + " AS date) ";
            break;
          default:
            return null;
        }

        break;
      case DatabaseUtils.MYSQL:
        switch (truncTo) {
          case DatabaseUtils.DAY:
            truncSQL = "CAST(" + dateColumn + " AS date) ";
            break;
          default:
            return null;
        }
        break;
      case DatabaseUtils.DERBY:
        switch (truncTo) {
          case DatabaseUtils.DAY:
            truncSQL = "CAST(" + dateColumn + " AS date) ";
            break;
          default:
            return null;
        }
        break;
    }

    return truncSQL;
  }

  /**
   * Gets the maxId attribute of the DatabaseUtils class
   *
   * @param db               Description of the Parameter
   * @param origTableName    Description of the Parameter
   * @param origSequenceName Description of the Parameter
   * @param uniqueField      Description of the Parameter
   * @return The maxId value
   * @throws SQLException Description of the Exception
   */
  public static int getMaxId(Connection db, String origTableName, String origSequenceName, String uniqueField) throws SQLException {
    //TODO: Verify the working of this method for all supported databases
    int typeId = DatabaseUtils.getType(db);

    int id = -1;
    Statement st = db.createStatement();
    ResultSet rs = null;
    String sequenceName = getSequenceName(db, origSequenceName);
    String tableName = getTableName(db, origTableName);

    switch (typeId) {
      case DatabaseUtils.POSTGRESQL:
        rs = st.executeQuery(
            "SELECT nextval('" + sequenceName + "')");
        break;
      case DatabaseUtils.MSSQL:
      case DatabaseUtils.DERBY:
        rs = st.executeQuery(
            "SELECT MAX(" + uniqueField + ") FROM " + parseReservedWord(db, tableName));
        break;
      case DatabaseUtils.FIREBIRD:
        rs = st.executeQuery(
            "SELECT GEN_ID (" + sequenceName + ",1) FROM RDB$DATABASE");
        break;
      case DatabaseUtils.DAFFODILDB:
        rs = st.executeQuery("SELECT " + sequenceName + ".nextval from dual");
        break;
      case DatabaseUtils.ORACLE:
        rs = st.executeQuery("SELECT " + sequenceName + ".nextval from dual");
        break;
      case DatabaseUtils.DB2:
        rs = st.executeQuery("VALUES NEXTVAL FOR " + sequenceName);
        break;
      default:
        break;
    }
    if (rs.next()) {
      id = rs.getInt(1);
    }
    rs.close();
    st.close();
    return id;
  }

  /**
   * Description of the Method
   *
   * @param db
   * @param pst
   * @return
   * @throws SQLException Description of the Returned Value
   */
  public static ResultSet executeQuery(Connection db, PreparedStatement pst) throws SQLException {
    return executeQuery(db, pst, null);
  }

  /**
   * Description of the Method
   *
   * @param db
   * @param pst
   * @param pagedListInfo
   * @return
   * @throws SQLException Description of the Returned Value
   */
  public static ResultSet executeQuery(Connection db, PreparedStatement pst, PagedListInfo pagedListInfo) throws SQLException {
    ResultSet rs = null;
    long milies = System.currentTimeMillis();
    if (pagedListInfo != null) {
      pagedListInfo.doManualOffset(db, pst);
    }
    rs = pst.executeQuery();
    milies = System.currentTimeMillis() - milies;
    log.debug(pst);
    log.debug(milies + " ms.");
    if (milies > POSSIBLE_QUERY_TIME) {
      log.warn("To improve the speed of your application please send the following query to Concourse Suite Community Edition support:");
      log.warn("------------------------");
      log.warn(pst);
      log.warn("[QUERY TIME: " + milies + " ms.]");
      log.warn("------------------------");
    }
    if (pagedListInfo != null) {
      pagedListInfo.doManualOffset(db, rs);
    }
    return rs;
  }


  /**
   * This function is used to create "?" for CreateFilter()
   * This function inerts AND or OR for columns with string types
   *
   * @param list ArrayList,for which to create the number of params
   * @return String A series of ? to be used as parameters for CreateFilter Prepared Statement
   */
  public static String createParams(ArrayList list, String column, Connection db, String option, String dataType) throws SQLException {
    String qStr = null;
    StringBuffer str = null;
    if (Constants.STRING.equals(dataType.toLowerCase())) {
      qStr = DatabaseUtils.toLowerCase(db) + column + " LIKE " + DatabaseUtils.toLowerCase(db) + " (?) ";
    } else if (Constants.INT.equals(dataType.toLowerCase())) {
      qStr = column + " = ? ";
    } else {
      throw new SQLException("DatabaseUtils.java -> In createParams() for datatType parameter only Constants.INT or Constants.STRING can be used");
    }
    str = new StringBuffer(" ( " + qStr);
    for (int count = 1; count < list.size(); count++) {
      if (option.equals(Constants.ALL)) {
        str.append(" AND ");
      } else {
        str.append(" OR ");
      }
      str.append(qStr);
    }
    str.append(" ) ");
    return str.toString();
  }

  /**
   * This function is used to create "?" for CreateFilter()
   * This function does intersection between the queries
   *
   * @param list ArrayList,for which to create the number of params
   * @return String A series of ? to be used as parameters for CreateFilter Prepared Statement
   */
  public static String createParams(ArrayList list, String column) {
    String qStr = column;
    StringBuffer str = new StringBuffer(" ( " + qStr);
    for (int count = 1; count < list.size(); count++) {
      str.append(" INTERSECT ");
      str.append(qStr);
    }
    str.append(" ) ");
    return str.toString();
  }

}
