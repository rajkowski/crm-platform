//Copyright 2002 Dark Horse Ventures
package org.aspcfs.modules.service.base;

import java.sql.*;
import java.util.*;

/**
 *  A cache for keeping track of SyncClient data. This collection contains
 *  client tables and the related client-server mappings.  Currently
 *  all of the records are cached for the given client, so all must be added
 *  or removed at the same time.
 *
 *@author     matt rajkowski
 *@created    June 10, 2002
 *@version    $Id: SyncClientManager.java,v 1.3 2003/01/13 21:54:40 mrajkowski
 *      Exp $
 */
public class SyncClientManager extends Hashtable {

  /**
   *  Adds a feature to the Client attribute of the SyncClientManager object
   *
   *@param  db                The feature to be added to the Client attribute
   *@param  clientId          The feature to be added to the Client attribute
   *@return                   Description of the Return Value
   *@exception  SQLException  Description of the Exception
   */
  public Hashtable addClient(Connection db, int clientId) throws SQLException {
    if (this.containsKey(new Integer(clientId))) {
      return (Hashtable) this.get(new Integer(clientId));
    } else {
      Hashtable client = new Hashtable();
      this.put(new Integer(clientId), client);
      String sql =
          "SELECT table_id, record_id, cuid " +
          "FROM sync_map " +
          "WHERE client_id = ? " +
          "ORDER BY table_id ";
      PreparedStatement pst = db.prepareStatement(sql);
      pst.setInt(1, clientId);
      ResultSet rs = pst.executeQuery();
      int lastTableId = -1;
      Hashtable lastTable = null;
      while (rs.next()) {
        int tableId = rs.getInt("table_id");
        int recordId = rs.getInt("record_id");
        int cuid = rs.getInt("cuid");
        if (lastTableId != tableId) {
          lastTableId = tableId;
          lastTable = new Hashtable();
          client.put(new Integer(lastTableId), lastTable);
        }
        lastTable.put(new Integer(recordId), new Integer(cuid));
      }
      rs.close();
      pst.close();
      return client;
    }
  }


  /**
   *  Removes a client from the in-memory cache
   *
   *@param  clientId  Description of the Parameter
   *@return           Description of the Return Value
   */
  public void removeClient(int clientId) {
    if (this.containsKey(new Integer(clientId))) {
      this.remove(new Integer(clientId));
    }
  }


  /**
   *  Inserts a client-server record mapping into the cache
   *
   *@param  clientId  Description of the Parameter
   *@param  tableId   Description of the Parameter
   *@param  recordId  Description of the Parameter
   *@param  cuid      Description of the Parameter
   */
  public void insert(int clientId, int tableId, Integer recordId, Integer cuid) {
    Hashtable clientLookup = (Hashtable) this.get(new Integer(clientId));
    Hashtable tableLookup = (Hashtable) clientLookup.get(new Integer(tableId));
    if (tableLookup == null) {
      tableLookup = new Hashtable();
      clientLookup.put(new Integer(tableId), tableLookup);
    }
    tableLookup.put(recordId, cuid);
  }


  /**
   *  Removes a client-server record mapping from the cache
   *
   *@param  clientId  Description of the Parameter
   *@param  tableId   Description of the Parameter
   *@param  recordId  Description of the Parameter
   */
  public void remove(int clientId, int tableId, Integer recordId) {
    Hashtable clientLookup = (Hashtable) this.get(new Integer(clientId));
    Hashtable tableLookup = (Hashtable) clientLookup.get(new Integer(tableId));
    if (tableLookup != null) {
      tableLookup.remove(recordId);
    }
  }
}

