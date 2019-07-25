package com.finartz.security.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.test.LdapTestUtils;
import org.springframework.ldap.support.LdapUtils;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;


public class BaseTest {

//    protected final Logger LOGGER;
//
//    protected static GSUserDAO guestDAO;
//    protected static UserGroupDAO guestGroupDAO;
//
//    protected static ClassPathXmlApplicationContext ctx = null;
//
//    @Rule
//    public TestName name = new TestName();
//
//    public BaseTest()
//    {
//        LOGGER = LogManager.getLogger(getClass());
//
//        synchronized (BaseTest.class) {
//            if (ctx == null) {
//                String[] paths = {
//                        "classpath*:applicationContext.xml"
//                };
//                ctx = new ClassPathXmlApplicationContext(paths);
//
//                userDAO = (GSUserDAO) ctx.getBean("gsUserDAO_LDAP");
//                userGroupDAO = (UserGroupDAO) ctx.getBean("userGroupDAO_LDAP");
//            }
//
//        }
//    }
//
//
//
//    @AfterClass
//    public static void tearDownClass() throws Exception
//    {
//        LdapTestUtils.shutdownEmbeddedServer();
//    }
//
//    @Before
//    public void setUp() throws Exception
//    {
//        LOGGER.info("################ Setting up -- " + getClass().getSimpleName() + ":: " + name.getMethodName() );
////        loadData();
//        LOGGER.info("##### Ending setup for " + getClass().getSimpleName() + " ###----------------------");
//    }
//
//    @Test
//    protected static void loadData() throws Exception
//    {
//        // Bind to the directory
//        LdapContextSource contextSource = new LdapContextSource();
//        contextSource.setUrl("ldap://127.0.0.1:10389");
//        contextSource.setUserDn("uid=admin,ou=system");
//        contextSource.setPassword("secret");
//        contextSource.setPooled(false);
//        //contextSource.setDirObjectFactory(null);
//        contextSource.afterPropertiesSet();
//
//        // Create the Sprint LDAP template
//        LdapTemplate template = new LdapTemplate(contextSource);
//
//        // Clear out any old data - and load the test data
//        LdapTestUtils.clearSubContexts(contextSource, LdapUtils.newLdapName("dc=example,dc=com"));
//        LdapTestUtils.loadLdif(contextSource, new ClassPathResource("london.ldif"));
//    }
}
