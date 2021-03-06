Concourse Suite Community Edition; build: @BUILD.NUMBER@; @BUILD.DATE@
$Id$

Concourse Suite Community Edition

----------------------------------------------------------------------------
| LEGAL                                                                    |
----------------------------------------------------------------------------

This software is licensed under the Centric Public License (CPL).
You should have received a copy of the CPL with this source code package in
the LICENSE file. If you did not receive a copy of the CPL, you may download
it from http://www.concursive.com. Compiling or using this software
signifies your acceptance of the Centric Public License.

Copyright(c) 2000-2007 Concursive Corporation (http://www.concursive.com/) All
rights reserved. This material cannot be distributed without written
permission from Concursive Corporation. Permission to use, copy, and modify
this material for internal use is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies. CONCURSIVE
CORPORATION MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES, EXPRESS OR
IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR ANY PARTICULAR
PURPOSE, AND THE WARRANTY AGAINST INFRINGEMENT OF PATENTS OR OTHER
INTELLECTUAL PROPERTY RIGHTS. THE SOFTWARE IS PROVIDED "AS IS", AND IN NO
EVENT SHALL CONCURSIVE CORPORATION OR ANY OF ITS AFFILIATES BE LIABLE FOR
ANY DAMAGES, INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL
DAMAGES RELATING TO THE SOFTWARE.


----------------------------------------------------------------------------
| INTRODUCTION                                                             |
----------------------------------------------------------------------------

Welcome to Concourse Suite Community Edition!

The installation and configuration of Concourse Suite Community Edition is intended to be as
simple as possible.

PLEASE READ ALL INSTRUCTIONS TO AVOID ANY PROBLEMS.

If you are going to be developing Concourse Suite Community Edition, please make sure to read the
developer information at http://www.concursive.com/Portal.do?key=community,
you can also find information about installing, configuring, and using
Concourse Suite Community Edition there.


----------------------------------------------------------------------------
| INSTALLATION SUMMARY                                                     |
----------------------------------------------------------------------------

1. Install Sun or IBM Java
2. Install a Web Application Server
3. Install a Database Server
4. Install Apache Ant
5. Setup the environment variables
6. Compile Concourse Suite Community Edition using "ant deploy" (update build.properties at this time)
7. Create a .war using "ant war"
8. Install the database using "ant installdb"
9. Deploy the .war to the Web Application Server


----------------------------------------------------------------------------
| REQUIREMENTS                                                             |
----------------------------------------------------------------------------

The Concourse Suite Community Edition Source Code can be obtained from the Concourse Suite Community Edition Subversion
server.  You will need a Subversion client and a user account at
http://www.concursive.com to download the source.

Latest Stable Release:

  https://svn.centricsuite.com/webapp/branches/branch-50

Latest Unstable Developer Code (release + fixes + untested features):

  https://svn.centricsuite.com/webapp/branches/branch-51


The following software needs to be installed in order to compile Concourse Suite Community Edition:

  Sun Java or IBM Java...

    Sun J2SE JDK 5.0 or Sun J2SE 1.4 (Depending on Web Application Server)
    http://java.sun.com

    IBM Java 1.4 or 1.5 (Depending on Web Application Server; usually included)
    http://www.ibm.com

  Apache Ant 1.6
  http://ant.apache.org


You will also need a supported Java Web Application Server installed.
The following are supported:

  Apache Tomcat 5.5, use with Sun J2SE 5.0 or
  Apache Tomcat 5.0, use with Sun J2SE 1.4
  http://tomcat.apache.org

  IBM Web Application Server Community Edition (WAS-CE) with Java 1.4 or 1.5
  http://www.ibm.com/software/webservers/appserv/community


You will also need a database server installed.
The following are supported in the source version:

  Postgresql 8.1, 8.0, 7.4
  http://www.postgresql.org

  IBM DB2 9.0 Universal or Express-C
  http://www.ibm.com/db2

  Microsoft SQL Server 2005, 2000/MSDE
  http://www.microsoft.com/sql

  Firebird SQL 1.5 +
  http://www.firebirdsql.org

  MySQL 5
  http://www.mysql.org

  Oracle 10g Express
  http://www.oracle.com/technology/software/products/database/xe/index.html

  DaffodilDB / One$DB Embedded Server 4.0 (included with Concourse Suite Community Edition)
  http://www.daffodildb.com

  Apache Derby (included with Concourse Suite Community Edition)
  http://db.apache.org/derby/


Concourse Suite Community Edition includes and licenses many 3rd party libraries.  These are
distributed with Concourse Suite Community Edition.  See below, in the "About this Software"
section, for specific license information.


----------------------------------------------------------------------------
| SYSTEM SETTINGS                                                          |
----------------------------------------------------------------------------

-> Linux, MacOSX, and Unix use "export VARIABLE=text"
-> Windows use the "My Computer-> Properties -> Environment Variables GUI"

* You must have the JAVA_HOME environment variable properly set to the
location of your Java SDK installation directory. On MacOSX, this path is:
/System/Library/Frameworks/JavaVM.framework/Home

  $ export JAVA_HOME=/usr/local/java/j2sdk1.4

* You must have Ant installed and ANT_HOME defined in your environment as
well as ANT_HOME/bin in your PATH.  You must also increase the JVM memory for
Ant by defining ANT_OPTS.

  $ export ANT_HOME=/usr/local/ant
  $ export ANT_OPTS="-Xmx192m -Xms192m"

* The properties: CATALINA_HOME, CENTRIC_HOME, and CENTRIC_FILELIBRARY must
  exist as environment variables or as properties in a file called
  "home.properties" (an example is provided as "home.properties.example")

* You must have Apache Tomcat or IBM WAS-CE installed.

If using Apache Tomcat, then CATALINA_HOME must be defined in your properties
which points to the tomcat directory.  Also specify the environment variable
CATALINA_OPTS=-Djava.awt.headless=true on Linux/Unix.  You should run Tomcat
with additional memory by setting this in Tomcat's startup script.

  $ export CATALINA_HOME=/path/to/apache-tomcat
  $ export CATALINA_OPTS=-Djava.awt.headless=true
  $ export JAVA_OPTS="-Xms256m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m"

  % Using System in Windows Control Panel set:
    CATALINA_HOME=c:\Program Files\Apache Software Foundation\Tomcat 5.5
    # In Tomcat's configuration editor, increase memory to 256MB


If using IBM WAS-CE, then GERONIMO_HOME must be defined in your properties
which points to the WAS-CE directory.  Also specify the environment variable
CATALINA_OPTS on Linux/Unix.  You should run WAS-CE with additional memory
by setting this in WAS-CE's startup script.

  $ export GERONIMO_HOME=/path/to/ibm-wasce
  $ export GERONIMO_OPTS=-Djava.awt.headless=true
  $ export JAVA_OPTS="-Xms256m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m"

  % Using System in Windows Control Panel set:
    GERONIMO_HOME=C:\Program Files\IBM\WebSphere\AppServerCommunityEdition 
    JAVA_OPTS=-Xms256m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m


* You must have CENTRIC_HOME defined in your properties which points to a
directory in which you want the exploded Concourse Suite Community Edition web application to be
deployed.  The ant script will allow you to install and upgrade this exploded
directory.  If using Tomcat, then this can be set to a directory in webapps
so that a .war is not needed; if using WAS-CE, then point this to a working
directory outside of WAS-CE.


Using Tomcat:

  $ export CENTRIC_HOME=/path/to/apache-tomcat/webapps/centric

  % Using System, in Windows Control Panel, set:
    CENTRIC_HOME=
      c:\Program Files\Apache Software Foundation\Tomcat 5.5\webapps\centric

Using WAS-CE:

  $ export CENTRIC_HOME=/path/to/user/deploy

  % Using System, in Windows Control Panel, set:
    CENTRIC_HOME=c:\CentricCRM\deploy


* You must have CENTRIC_FILELIBRARY defined in your properties which points to a
Concourse Suite Community Edition file library (initially empty) to be used by Ant and Concourse Suite Community Edition.

  $ export CENTRIC_FILELIBRARY=/var/lib/centric_crm/fileLibrary
  $ export CENTRIC_FILELIBRARY=/Library/Application\ Support/CentricCRM/fileLibrary

  % Using System in Windows Control Panel set:
    CENTRIC_FILELIBRARY=c:\CentricCRM\fileLibrary


----------------------------------------------------------------------------
| BUILD PROCESS                                                            |
----------------------------------------------------------------------------

Begin by executing "ant" from the command line.  You will be presented with
the available ant commands.

To verify that you have all settings in place, and to attempt to deploy
from the source code, execute:

  $ ant deploy

A new file will be created in the CENTRIC_FILELIBRARY directory, which must be
edited with your custom settings.  Ant will notify you that you must make
changes to:

  $CENTRIC_FILELIBRARY/build.properties

Once changes have been made, you can try ant again:

  $ ant deploy

This time you should end up with with a compiled version of Concourse Suite Community Edition.

Before using Concourse Suite Community Edition, you must install the database using ant.  All of
the source SQL scripts have been included.  If you are using Daffodil DB then
you do not need an existing database.  If you are using a database server,
then you must create a centric_crm database before installing the database.

To create the database schema and install the default data, edit the
build.properties file by uncommenting the database of choice, then execute ant
with:

  $ ant installdb

Ant will ask for the database name, and use the URL and driver from the
build.properties file.  The database will be fully created.

Daffodil DB requires additional steps:

  $ ant installdb2
  $ ant installdb3


Now you can start Tomcat and begin the web-based configuration steps of
Concourse Suite Community Edition.


----------------------------------------------------------------------------
| UPGRADE PROCESS                                                          |
----------------------------------------------------------------------------

To keep up-to-date with Concourse Suite Community Edition, you will want to routinely update to
the latest stable or unstable source in the Subversion repository.

As developers commit code to the repository they often include SQL and BSH
upgrade scripts.  These scripts need to be executed diligently to maintain
a stable environment.  The unstable branches may not be complete in regards
to upgrade scripts for various databases.  The stable branches will
contain a list of upgrade scripts that need to be run in order to upgrade
from one Concourse Suite Community Edition version to the next.

Using a staging server will ensure minimal downtime.

To upgrade from one stable release to the next stable release:

1. Upgrade to the latest source code stable release
2. Stop Tomcat
3. Backup your fileLibrary, webapp and database before running any scripts
4. Run "ant deploy" to compile and deploy any needed classes and files used
   by the upgrade process
5. Run the stable upgrade script, for example in bin/ there is an
   upgrade_41-50.sh and upgrade_41-50.bat file which will execute a
   corresponding upgrade_v41-50.txt which contains a list of SQL and BSH
   scripts required to run from one stable version to the next

Please report back your results.  It's possible that a script file might
be missing or incomplete.

To upgrade using the daily developer unstable code, you will need to run
scripts as they are committed to the repository.  Keep in mind that many
of these scripts have not been reviewed and may not be approved.  Approved
scripts get appended to an "upgrade_v*-*.txt" file for the stable releases.

Same directions as above, except instead of running the .bat or .sh script,
you can execute the scripts directly (in order by date and number):

  $ ant upgradedb

You will be prompted for the script to run.


----------------------------------------------------------------------------
| Web Services                                                             |
----------------------------------------------------------------------------

Centric provides a set of classes that can be exposed as Web Services, and
can be consumed by external applications using SOAP. To enable Web Services
the following software needs to be installed as a separate webapp under your
tomcat installation.

  Apache Axis 1.2
  http://ws.apache.org/axis/

You must have AXIS_HOME property defined as an environment variable or as a
property in a file called "home.properties"

  $ export AXIS_HOME=/path/to/apache-tomcat/webapps/axis

  % Using System, in Windows Control Panel, set:
    AXIS_HOME=c:\Program Files\Apache Software Foundation\Tomcat 5.5\webapps\axis

Make sure to edit 'Axis' related properties in the following properties file

  $CENTRIC_HOME/WEB-INF/build.properties

To make the web service classes available to external applications, register
Centric's Web Services with Axis. Run the following ant command:

  $ ant ws

If the command is successful, then you should be able to point your browser at
the following url and you will see the Centric's Web Services that were deployed

  http://{yourhostname}/{axis.webapp}/servlet/AxisServlet/


----------------------------------------------------------------------------
| ABOUT THIS SOFTWARE                                                      |
----------------------------------------------------------------------------

Concourse Suite Community Edition licenses libraries and code from the following projects, some
are proprietary in which Concursive Corporation has been granted a license
to redistribute, some are Open Source and are used according to the project
license:

Project Name                      License
--------------------------------  -----------------------------------------
Asterisk-Java                     Apache Software License
Batik                             Apache Software License
Bean Shell                        Sun Public License
Bouncy Castle Crypto API          Bouncy Castle Open Source License
Castor                            Apache Software License 2.0
DaffodilDB (One$DB Embedded)      LGPL
gnu.regexp                        LGPL
HTMLArea                          BSD style
HTTPMultiPartParser               iSavvix Public License
IBM DB2 JDBC Driver               IBM DB2 Developers Redistribution
Ingres JDBC Driver                Ingres License
InterBase JDBC Driver             InterBase Public License
iText                             LGPL
JFreeChart                        LGPL
Jakarta Commons                   Apache Software License
Jakarta Taglibs JSTL              Apache Software License
Jasper Reports                    LGPL
Java Activitation Framework       Sun License
Java Mail                         Sun License
Jaybird FirebirdSQL JDBC Driver   LGPL
Jcrontab                          LGPL
jTDS Microsoft SQL Server Driver  LGPL
JUnit                             Common Public License 1.0
Kafenio                           LGPL
Log4J                             Apache Software License
Lucene                            Apache Software License
MySQL Enterprise Connector/J      MySQL Enterprise Driver Software License
NekoHTML                          Apache style
Oracle JDBC Driver                Oracle Technology Network Development and
                                  Distribution License
PDFBox                            BSD
POI                               Apache Software License
PostgreSQL JDBC Driver            BSD
Quartz                            OpenSymphony Software License
Smack and Smackx                  Apache Software License
Sun Java Streaming XML Parser     Sun Binary Code License
Team Elements Project Management  Centric Public License
TinyMCE                           LGPL
TMExtractors                      Apache style
WebFX Cross Browser Tree Widget   Apache Software License 2.0
Xerces                            Apache Software License
Ximian Icons                      LGPL
ypSlideOutMenu                    Creative Commons Attribution 2.0 license
