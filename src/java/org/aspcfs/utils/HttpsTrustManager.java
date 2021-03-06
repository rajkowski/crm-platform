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

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * This is a replacement trust manager that accepts ANYTHING. This was created
 * to allow self-signed X509 certificates to work.<p>
 * <p/>
 * This class is a work in progress, which later on should at least detect some
 * credentials.
 *
 * @author matt rajkowski
 * @version $Id: HttpsTrustManager.java,v 1.1 2003/03/26 21:12:56 mrajkowski
 *          Exp $
 * @created March 25, 2003
 */
public class HttpsTrustManager implements X509TrustManager {
  /**
   * Constructor for the HttpsTrustManager object
   */
  HttpsTrustManager() {
  }


  /**
   * Description of the Method
   *
   * @param chain    Description of the Parameter
   * @param authType Description of the Parameter
   * @throws CertificateException Description of the Exception
   */
  public void checkClientTrusted(X509Certificate chain[], String authType) throws CertificateException {
  }


  /**
   * Description of the Method
   *
   * @param chain    Description of the Parameter
   * @param authType Description of the Parameter
   * @throws CertificateException Description of the Exception
   */
  public void checkServerTrusted(X509Certificate chain[], String authType) throws CertificateException {
  }


  /**
   * Gets the acceptedIssuers attribute of the HttpsTrustManager object
   *
   * @return The acceptedIssuers value
   */
  public X509Certificate[] getAcceptedIssuers() {
    return null;
  }
}

