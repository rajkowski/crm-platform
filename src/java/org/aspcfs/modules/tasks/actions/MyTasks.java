package com.darkhorseventures.cfsmodule;

import javax.servlet.*;
import javax.servlet.http.*;
import org.theseus.actions.*;
import com.darkhorseventures.cfsbase.*;
import com.darkhorseventures.utils.*;
import com.darkhorseventures.webutils.*;
import java.util.*;
import java.sql.*;
import org.theseus.actions.*;
import com.zeroio.webutils.*;
import com.isavvix.tools.*;
import java.io.*;

/**
 *  Description of the Class
 *
 *@author     akhi_m
 *@created    August 15, 2002
 *@version    $Id$
 */
public final class MyTasks extends CFSModule {

  /**
   *  Description of the Method
   *
   *@param  context  Description of the Parameter
   *@return          Description of the Return Value
   */
  public String executeCommandDefault(ActionContext context) {
    if (!(hasPermission(context, "myhomepage-inbox-view"))) {
      return ("DefaultError");
    }
    return (this.executeCommandListTasks(context));
  }


  /**
   *  Description of the Method
   *
   *@param  context  Description of the Parameter
   *@return          Description of the Return Value
   */
  public String executeCommandListTasks(ActionContext context) {

    Exception errorMessage = null;
    PagedListInfo taskInfo = this.getPagedListInfo(context, "TaskListInfo");
    taskInfo.setLink("/MyTasks.do?command=ListTasks");

    Connection db = null;
    TaskList taskList = new TaskList();

    try {
      db = this.getConnection(context);
      taskList.setPagedListInfo(taskInfo);
      taskList.setEnteredBy(getUserId(context));
      taskList.buildList(db);
    } catch (Exception e) {
      errorMessage = e;
    } finally {
      this.freeConnection(context, db);
    }

    if (errorMessage == null) {
      context.getRequest().setAttribute("TaskList", taskList);
      addModuleBean(context, "My Tasks", "Task Home");
      return ("TaskListOK");
    } else {
      context.getRequest().setAttribute("Error", errorMessage);
      return ("SystemError");
    }
  }


  /**
   *  Description of the Method
   *
   *@param  context  Description of the Parameter
   *@return          Description of the Return Value
   */
  public String executeCommandNew(ActionContext context) {

    Exception errorMessage = null;
    Connection db = null;
    Task thisTask = null;
    int id = -1;
    if (!(hasPermission(context, "myhomepage-inbox-view"))) {
      return ("DefaultError");
    }
    context.getSession().removeAttribute("contactListInfo");
    if (context.getRequest().getParameter("id") != null) {
      id = Integer.parseInt(context.getRequest().getParameter("id"));
    }
    try {
      db = this.getConnection(context);
      thisTask = new Task(db, id);
    } catch (Exception e) {
      errorMessage = e;
    } finally {
      this.freeConnection(context, db);
    }

    if (errorMessage == null) {
      context.getRequest().setAttribute("Task", thisTask);
      addModuleBean(context, "My Tasks", "Task Home");
      return ("NewTaskOK");
    } else {
      context.getRequest().setAttribute("Error", errorMessage);
      return ("SystemError");
    }
  }


  /**
   *  Description of the Method
   *
   *@param  context  Description of the Parameter
   *@return          Description of the Return Value
   */
  public String executeCommandInsert(ActionContext context) {
    System.out.println("Comin in to uinsert");
    Exception errorMessage = null;
    Connection db = null;
    int id = -1;
    int contactId = -1;
    boolean done = false;
    int sharing = 1;
    if (!(hasPermission(context, "myhomepage-inbox-view"))) {
      return ("DefaultError");
    }

    if (context.getRequest().getParameter("id") != null) {
      System.out.println("The id is"+id);
      id = Integer.parseInt(context.getRequest().getParameter("id"));
    }

    try {
      db = this.getConnection(context);
      Task newTask = (Task) context.getFormBean();
      newTask.setEnteredBy(getUserId(context));
      if (id != -1) {
        done = newTask.update(db, id);
      } else {
        System.out.println("trying to insert");
        done = newTask.insert(db);
      }
    } catch (Exception e) {
      errorMessage = e;
    } finally {
      this.freeConnection(context, db);
    }

    if (errorMessage == null) {
      return ("InsertTaskOK");
    } else {
      context.getRequest().setAttribute("Error", errorMessage);
      return ("SystemError");
    }
  }

  

  /**
   *  Description of the Method
   *
   *@param  context  Description of the Parameter
   *@return          Description of the Return Value
   */
  public String executeCommandConfirmDelete(ActionContext context) {

    Exception errorMessage = null;
    Connection db = null;
    Task thisTask = null;
    HtmlDialog htmlDialog = new HtmlDialog();

    int id = -1;
    if (!(hasPermission(context, "myhomepage-inbox-view"))) {
      return ("DefaultError");
    }

    if (context.getRequest().getParameter("id") != null) {
      id = Integer.parseInt(context.getRequest().getParameter("id"));
    }
    try {
      db = this.getConnection(context);
      thisTask = new Task(db, id);
      htmlDialog.setRelationships(thisTask.processDependencies(db));
      if (htmlDialog.getRelationships().size() == 0) {
        htmlDialog.setTitle("Confirm");
        htmlDialog.setShowAndConfirm(false);
        htmlDialog.setDeleteUrl("javascript:window.location.href='MyTasks.do?command=Delete&id=" + id + "'");
      } else {
        htmlDialog.setTitle("Confirm");
        htmlDialog.setHeader("Are you sure you want to delete this item:");
        htmlDialog.addButton("Delete All", "javascript:window.location.href='/MyTasks.do?command=Delete&id=" + id + "'");
        htmlDialog.addButton("No", "javascript:window.close()");
      }
    } catch (Exception e) {
      errorMessage = e;
    } finally {
      this.freeConnection(context, db);
    }
    if (errorMessage == null) {
      context.getRequest().setAttribute("Dialog", htmlDialog);
      return ("ConfirmDeleteOK");
    } else {
      context.getRequest().setAttribute("Error", errorMessage);
      return ("SystemError");
    }
  }



  /**
   *  Description of the Method
   *
   *@param  context  Description of the Parameter
   *@return          Description of the Return Value
   */
  public String executeCommandDelete(ActionContext context) {

    Exception errorMessage = null;
    Connection db = null;
    Task thisTask = null;
    int id = -1;
    int action = -1;
    if (!(hasPermission(context, "myhomepage-inbox-view"))) {
      return ("DefaultError");
    }

    if (context.getRequest().getParameter("id") != null) {
      id = Integer.parseInt(context.getRequest().getParameter("id"));
    }
    if (context.getRequest().getParameter("action") != null) {
      action = Integer.parseInt(context.getRequest().getParameter("action"));
    }
    try {
      db = this.getConnection(context);
      thisTask = new Task(db, id);
      thisTask.delete(db);
    } catch (Exception e) {
      errorMessage = e;
    } finally {
      this.freeConnection(context, db);
    }
    if (errorMessage == null) {
      context.getRequest().setAttribute("Task", thisTask);
      return ("DeleteOK");
    } else {
      context.getRequest().setAttribute("Error", errorMessage);
      return ("SystemError");
    }
  }


  /**
   *  Description of the Method
   *
   *@param  context  Description of the Parameter
   *@return          Description of the Return Value
   */
  public String executeCommandProcessImage(ActionContext context) {
    Exception errorMessage = null;
    Connection db = null;
    boolean queryDone = false;

    String id = (String) context.getRequest().getParameter("id");

    //Start the download
    try {
      StringTokenizer st = new StringTokenizer(id, "|");
      String fileName = st.nextToken();
      String imageType = st.nextToken();
      int taskId = Integer.parseInt(st.nextToken());
      int status = Integer.parseInt(st.nextToken());
      db = this.getConnection(context);
      Task thisTask = new Task(db, taskId);

      if (status == Task.DONE) {
        thisTask.setComplete(true);
      } else {
        thisTask.setComplete(false);
      }
      queryDone = thisTask.update(db, taskId);
      this.freeConnection(context, db);
      if (queryDone) {
        String filePath = context.getServletContext().getRealPath("/") + "images" + fs + fileName;
        FileDownload fileDownload = new FileDownload();
        fileDownload.setFullPath(filePath);
        fileDownload.setDisplayName(fileName);
        if (fileDownload.fileExists()) {
          fileDownload.sendFile(context, "image/" + imageType);
        } else {
          System.err.println("Image-> Trying to send a file that does not exist");
        }
      }
    } catch (java.net.SocketException se) {
      //User either cancelled the download or lost connection
    } catch (Exception e) {
      errorMessage = e;
      System.out.println(e.toString());
    } finally {
      this.freeConnection(context, db);
    }
    return ("-none-");
  }
}

