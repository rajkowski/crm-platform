/*
 *  Copyright(c) 2004 Dark Horse Ventures LLC (http://www.centriccrm.com/) All
 *  rights reserved. This material cannot be distributed without written
 *  permission from Dark Horse Ventures LLC. Permission to use, copy, and modify
 *  this material for internal use is hereby granted, provided that the above
 *  copyright notice and this permission notice appear in all copies. DARK HORSE
 *  VENTURES LLC MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES, EXPRESS OR
 *  IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR ANY PARTICULAR
 *  PURPOSE, AND THE WARRANTY AGAINST INFRINGEMENT OF PATENTS OR OTHER
 *  INTELLECTUAL PROPERTY RIGHTS. THE SOFTWARE IS PROVIDED "AS IS", AND IN NO
 *  EVENT SHALL DARK HORSE VENTURES LLC OR ANY OF ITS AFFILIATES BE LIABLE FOR
 *  ANY DAMAGES, INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL
 *  DAMAGES RELATING TO THE SOFTWARE.
 */
package org.aspcfs.taglib;

import org.aspcfs.utils.DatabaseUtils;
import org.aspcfs.utils.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Displays a form message when an error or warning message is generated by the
 * user.
 *
 * @author kbhoopal
 * @version $Id: FormMessageHandler.java,v 1.1.2.1 2004/09/02 15:25:12
 *          kbhoopal Exp $
 * @created September 1, 2004
 */
public class FormMessageHandler extends TagSupport implements TryCatchFinally {

  private boolean showSpace = true;

  public void doCatch(Throwable throwable) throws Throwable {
    // Required but not needed
  }

  public void doFinally() {
    // Reset each property or else the value gets reused
    showSpace = true;
  }


  /**
   * Sets the showSpace attribute of the FormMessageHandler object
   *
   * @param tmp The new showSpace value
   */
  public void setShowSpace(boolean tmp) {
    this.showSpace = tmp;
  }


  /**
   * Sets the showSpace attribute of the FormMessageHandler object
   *
   * @param tmp The new showSpace value
   */
  public void setShowSpace(String tmp) {
    showSpace = DatabaseUtils.parseBoolean(tmp);
  }


  /**
   * Description of the Method
   *
   * @return Description of the Return Value
   * @throws JspException Description of the Exception
   */
  public final int doStartTag() throws JspException {
    try {
      // Check to see if there is a form error
      String error = (String) pageContext.getRequest().getAttribute(
          "actionError");
      if (error == null) {
        error = (String) pageContext.getRequest().getParameter("actionError");
      }
      // Check to see if there is a form warning
      String warning = (String) pageContext.getRequest().getAttribute(
          "actionWarning");
      if (warning == null) {
        warning = (String) pageContext.getRequest().getParameter(
            "actionWarning");
      }
      // Display the error, if none then display the warning, else nothing
      String messageToShow = "";
      if (error != null) {
        messageToShow =
            "<img src=\"images/error.gif\" border=\"0\" align=\"absmiddle\" /> " +
            "<font color=\"red\">" + StringUtils.toHtml(error) + "</font>";
      } else if (warning != null) {
        messageToShow =
            "<img src=\"images/box-hold.gif\" border=\"0\" align=\"absmiddle\" /> " +
            "<font color=\"#FF9933\">" + StringUtils.toHtml(warning) + "</font>";
      }
      if (!"".equals(messageToShow)) {
        this.pageContext.getOut().write(
            (showSpace ? "&nbsp;<br>" : "") + messageToShow + "<br />&nbsp;<br />");
      } else {
        this.pageContext.getOut().write(((showSpace) ? "&nbsp;" : ""));
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
    return SKIP_BODY;
  }


  /**
   * Description of the Method
   *
   * @return Description of the Return Value
   */
  public int doEndTag() {
    return EVAL_PAGE;
  }
}

