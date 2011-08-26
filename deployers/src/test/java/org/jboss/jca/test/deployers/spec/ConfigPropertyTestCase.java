/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jca.test.deployers.spec;

import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyAdminObjectImpl;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyAdminObjectInterface;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyConnection;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyConnectionFactory;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyConnectionFactoryImpl;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyConnectionImpl;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyManagedConnection;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyManagedConnectionFactory;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyManagedConnectionMetaData;
import org.jboss.jca.test.deployers.spec.rars.configproperty.ConfigPropertyResourceAdapter;

import java.util.UUID;

import javax.annotation.Resource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * ConnectorTestCase
 *
 * @version $Revision: $
 */
@RunWith(Arquillian.class)
public class ConfigPropertyTestCase
{
   private static Logger log = Logger.getLogger(ConfigPropertyTestCase.class);

   /**
    * Define the deployment
    *
    * @return The deployment archive
    */
   @Deployment
   public static ResourceAdapterArchive createDeployment()
   {
      String deploymentName = "config-property.rar";

      ResourceAdapterArchive raa =
         ShrinkWrap.create(ResourceAdapterArchive.class, deploymentName);
      JavaArchive ja = ShrinkWrap.create(JavaArchive.class, UUID.randomUUID().toString() + ".jar");
      ja.addClasses(ConfigPropertyResourceAdapter.class, ConfigPropertyManagedConnectionFactory.class,
                    ConfigPropertyManagedConnection.class, ConfigPropertyConnectionFactory.class,
                    ConfigPropertyManagedConnectionMetaData.class,
                    ConfigPropertyConnectionFactoryImpl.class, ConfigPropertyConnection.class,
                    ConfigPropertyConnectionImpl.class);
      raa.addAsLibrary(ja);

      raa.addAsManifestResource(deploymentName + "/META-INF/ra.xml", "ra.xml");
      raa.addAsManifestResource(deploymentName + "/META-INF/ironjacamar.xml", "ironjacamar.xml");

      return raa;
   }

   /** CF */
   @Resource(mappedName = "java:/eis/ConfigPropertyConnectionFactory")
   private ConfigPropertyConnectionFactory connectionFactory;

   /** AO */
   @Resource(mappedName = "java:/eis/ao/ConfigPropertyAdminObjectInterface")
   private ConfigPropertyAdminObjectInterface adminObject;

   /**
    * Test config properties
    *
    * @exception Throwable Thrown if case of an error
    */
   @Test
   public void testConfigProperties() throws Throwable
   {
      assertNotNull(connectionFactory);
      assertNotNull(adminObject);

      ConfigPropertyConnection connection = connectionFactory.getConnection();
      assertNotNull(connection);

      assertEquals("A", connection.getResourceAdapterProperty());
      assertEquals("B", connection.getManagedConnectionFactoryProperty());

      assertEquals("C", adminObject.getProperty());

      connection.close();
   }
}
