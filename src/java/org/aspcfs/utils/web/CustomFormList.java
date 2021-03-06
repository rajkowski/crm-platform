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
package org.aspcfs.utils.web;

import org.aspcfs.modules.base.CustomField;
import org.aspcfs.utils.XMLUtils;
import org.w3c.dom.Element;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.*;

/**
 * Description of the Class
 *
 * @author chris price
 * @version $Id: CustomFormList.java,v 1.2 2002/08/27 19:28:31 mrajkowski Exp
 *          $
 * @created June 12, 2002
 */
public class CustomFormList extends HashMap {

  /**
   * Constructor for the CustomFormList object
   */
  public CustomFormList() {
  }


  /**
   * Constructor for the CustomFormList object
   *
   * @param context Description of the Parameter
   * @param fn      Description of the Parameter
   */
  public CustomFormList(ServletContext context, String fn) {
    loadXML(context, fn);
  }


  /**
   * Loads the given XML file & builds the CustomForm object from the XML
   *
   * @param context Description of the Parameter
   * @param file    Description of the Parameter
   * @return Description of the Return Value
   */
  private LinkedHashMap loadXML(ServletContext context, String file) {
    LinkedHashMap form = new LinkedHashMap();
    try {
      XMLUtils xml = new XMLUtils(context, "/WEB-INF/" + file);
      LinkedList formList = new LinkedList();
      XMLUtils.getAllChildren(xml.getDocumentElement(), "form", formList);
      Iterator list = formList.iterator();
      while (list.hasNext()) {
        Element f = (Element) list.next();
        if (System.getProperty("DEBUG") != null) {
          System.out.println(
              "CustomFormList-> Form Added: " + f.getAttribute("name"));
        }
        CustomForm newForm = this.buildForm(f);
        this.put(newForm.getName(), newForm);
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
    return form;
  }


  /**
   * Builds the CustomForm object from the XML<br>
   * CustomForm follows a strict heirarchy : tabs --> groups --> rows -->
   * columns --> fields
   *
   * @param container XML element containing the representation of the form
   * @return The populated CustomForm
   */
  private CustomForm buildForm(Element container) {
    CustomForm thisForm = new CustomForm();
    thisForm.setName(container.getAttribute("name"));
    thisForm.setAction(container.getAttribute("action"));
    thisForm.addJScripts(container.getAttribute("scripts"));
    //Buttons
    LinkedList buttonElements = new LinkedList();
    XMLUtils.getAllChildren(container, "button", buttonElements);
    Iterator buttons = buttonElements.iterator();
    while (buttons.hasNext()) {
      Element button = (Element) buttons.next();
      thisForm.getButtonList().put(
          button.getAttribute("text"), button.getAttribute("link"));
    }
    //Tabs
    LinkedList tabList = new LinkedList();
    XMLUtils.getAllChildren(container, "tab", tabList);
    Iterator list1 = tabList.iterator();
    while (list1.hasNext()) {
      Element tab = (Element) list1.next();
      CustomFormTab thisTab = new CustomFormTab();
      thisTab.setName(tab.getAttribute("name"));
      thisTab.setId(tab.getAttribute("id"));
      thisTab.setDefaultField(tab.getAttribute("defaultField"));
      thisTab.setOnLoadEvent(tab.getAttribute("onLoad"));
      thisTab.setReturnLinkText(tab.getAttribute("returnLinkText"));
      //header & footer buttons
      LinkedList buttonList = new LinkedList();
      XMLUtils.getAllChildren(tab, "buttonGroup", buttonList);
      Iterator tmpList = buttonList.iterator();
      while (tmpList.hasNext()) {
        Element buttonGroup = (Element) tmpList.next();
        LinkedList TabButtons = new LinkedList();
        XMLUtils.getAllChildren(buttonGroup, "button", TabButtons);
        Iterator tmpList1 = TabButtons.iterator();
        while (tmpList1.hasNext()) {
          Element button = (Element) tmpList1.next();
          thisTab.addButton(new HtmlButton(button).toString());
        }
      }
      //groups
      LinkedList groupList = new LinkedList();
      XMLUtils.getAllChildren(tab, "group", groupList);
      Iterator list2 = groupList.iterator();
      while (list2.hasNext()) {
        Element group = (Element) list2.next();
        CustomFormGroup thisGroup = new CustomFormGroup();
        thisGroup.setName(group.getAttribute("name"));
        //rows
        LinkedList rowList = new LinkedList();
        XMLUtils.getAllChildren(group, "row", rowList);
        Iterator list3 = rowList.iterator();
        while (list3.hasNext()) {
          Element row = (Element) list3.next();
          CustomRow thisRow = new CustomRow(row);
          //columns
          LinkedList columnList = new LinkedList();
          XMLUtils.getAllChildren(row, "column", columnList);
          Iterator list4 = columnList.iterator();
          while (list4.hasNext()) {
            Element column = (Element) list4.next();
            CustomColumn thisColumn = new CustomColumn(column);
            //fields
            LinkedList fieldList = new LinkedList();
            XMLUtils.getAllChildren(column, "field", fieldList);
            Iterator list5 = fieldList.iterator();
            while (list5.hasNext()) {
              Element field = (Element) list5.next();
              CustomField thisField = new CustomField();
              processField(field, thisField);
              thisColumn.add(thisField);
            }
            thisRow.add(thisColumn);
          }
          thisGroup.add(thisRow);
        }
        thisTab.add(thisGroup);
      }
      thisForm.add(thisTab);
    }
    return thisForm;
  }


  /**
   * Sets the Attributes of a CustomField from XML element
   *
   * @param field     XML element representing the field
   * @param thisField CustomField
   */
  private void processField(Element field, CustomField thisField) {
    thisField.setName(field.getAttribute("name"));
    thisField.setDisplay(field.getAttribute("display"));
    thisField.setType(field.getAttribute("type"));
    thisField.setLengthVar(field.getAttribute("lengthVar"));
    thisField.setOnChange(field.getAttribute("onChange"));
    thisField.setJsEvent(field.getAttribute("jsEvent"));

    //check if the field is to be populated using a list object
    thisField.setListName(field.getAttribute("listName"));
    thisField.setListItemName(field.getAttribute("listItemName"));

    thisField.setDelimiter("^");

    thisField.setTextAsCode(field.getAttribute("textAsCode"));
    thisField.setLookupList(field.getAttribute("lookupList"));

    StringTokenizer st = new StringTokenizer(
        field.getAttribute("parameters"), "^");

    if (st.hasMoreTokens()) {
      while (st.hasMoreTokens()) {
        StringTokenizer b = new StringTokenizer(st.nextToken(), "=");
        if (b.hasMoreTokens()) {
          while (b.hasMoreTokens()) {
            thisField.setParameter(b.nextToken(), b.nextToken());
          }
        }
      }
    }

    thisField.setRequired(field.getAttribute("required"));
  }
}

