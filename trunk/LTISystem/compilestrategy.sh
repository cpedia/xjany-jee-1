#!/bin/sh

cd /usr/apache-tomcat-6.0.16/webapps/LTISystem

/usr/java/default/bin/java -Xms128m -Xmx1024m -classpath ./WEB-INF/classes:./ExecutorEngine:./WEB-INF/lib/jetty-server-7.0.0.RC3.jar:./WEB-INF/lib/ltisystem_base.jar:./WEB-INF/lib/castor_ofx.jar:./WEB-INF/lib/jetty-servlet-7.0.0.RC3.jar:./WEB-INF/lib/jetty-servlets-7.0.0.RC3.jar:./WEB-INF/lib/servlet-api-2.5.jar:./WEB-INF/lib/jetty-webapp-7.0.0.RC3.jar:./WEB-INF/lib/jetty-http-7.0.0.RC3.jar:./WEB-INF/lib/jetty-util-7.0.0.RC3.jar:./WEB-INF/lib/jetty-io-7.0.0.RC3.jar:./WEB-INF/lib/jetty-continuation-7.0.0.RC3.jar:./WEB-INF/lib/acegi-security-1.0.5.jar:./WEB-INF/lib/antlr-2.7.2.jar:./WEB-INF/lib/c3p0-0.9.1.2.jar:./WEB-INF/lib/castor-1.3-core.jar:./WEB-INF/lib/castor-1.3-xml.jar:./WEB-INF/lib/castor-1.3-xml-schema.jar:./WEB-INF/lib/cglib-nodep-2.1_3.jar:./WEB-INF/lib/commons-codec-1.3.jar:./WEB-INF/lib/commons-collections-3.1.jar:./WEB-INF/lib/commons-configuration-1.5.jar:./WEB-INF/lib/commons-dbcp.jar:./WEB-INF/lib/commons-digester.jar:./WEB-INF/lib/commons-httpclient.jar:./WEB-INF/lib/commons-lang-2.4.jar:./WEB-INF/lib/commons-logging-1.0.4.jar:./WEB-INF/lib/commons-math-1.2.jar:./WEB-INF/lib/commons-pool.jar:./WEB-INF/lib/dom4j-1.6.1.jar:./WEB-INF/lib/freemarker.jar:./WEB-INF/lib/hibernate3.jar:./WEB-INF/lib/jaxen-1.1.1.jar:./WEB-INF/lib/jdom.jar:./WEB-INF/lib/JRclient-RF503.jar:./WEB-INF/lib/JRI1.jar:./WEB-INF/lib/jta.jar:./WEB-INF/lib/junit.jar:./WEB-INF/lib/log4j-1.2.9.jar:./WEB-INF/lib/ltisystem_investmenttools.jar:./WEB-INF/lib/mail.jar:./WEB-INF/lib/mysql-connector-java-5.0.4-bin.jar:./WEB-INF/lib/ojalogNewest.jar:./WEB-INF/lib/oro-2.0.8.jar:./WEB-INF/lib/poi-3.2-FINAL-20081019.jar:./WEB-INF/lib/poi-contrib-3.2-FINAL-20081019.jar:./WEB-INF/lib/poi-scratchpad-3.2-FINAL-20081019.jar:./WEB-INF/lib/spring.jar:./WEB-INF/lib/standard-1.0.2.jar:./WEB-INF/lib/SuperCSV-with_src-1.52.jar:./WEB-INF/lib/ta-lib-0.4.0.jar:./WEB-INF/lib/tools.jar:./WEB-INF/lib/xercesImpl.jar:./WEB-INF/lib/xml-apis.jar com.lti.start.CompiledAllStrategies

