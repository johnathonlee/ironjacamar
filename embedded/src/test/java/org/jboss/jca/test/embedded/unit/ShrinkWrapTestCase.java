/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jca.test.embedded.unit;

import org.jboss.jca.embedded.EmbeddedJCA;
import org.jboss.jca.test.embedded.rars.simple.MessageListener;
import org.jboss.jca.test.embedded.rars.simple.TestActivationSpec;
import org.jboss.jca.test.embedded.rars.simple.TestConnection;
import org.jboss.jca.test.embedded.rars.simple.TestConnectionInterface;
import org.jboss.jca.test.embedded.rars.simple.TestManagedConnection;
import org.jboss.jca.test.embedded.rars.simple.TestManagedConnectionFactory;
import org.jboss.jca.test.embedded.rars.simple.TestResourceAdapter;

import java.util.UUID;

import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for deploying resource adapter archives (.RAR)
 * using ShrinkWrap
 * 
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 * @version $Revision: $
 */
public class ShrinkWrapTestCase
{

   // --------------------------------------------------------------------------------||
   // Class Members ------------------------------------------------------------------||
   // --------------------------------------------------------------------------------||

   private static Logger log = Logger.getLogger(ShrinkWrapTestCase.class);

   /*
    * Embedded
    */
   private static EmbeddedJCA embedded;

   // --------------------------------------------------------------------------------||
   // Tests --------------------------------------------------------------------------||
   // --------------------------------------------------------------------------------||

   /**
    * Null ShrinkWrap ResourceAdapterArchive test case
    * @exception Throwable Thrown if case of an error
    */
   @Test
   public void testNull() throws Throwable
   {
      ResourceAdapterArchive raa = null; 

      try
      {
         embedded.deploy(raa);
         fail("Null deployment successful");
      }
      catch (Throwable t)
      {
         // Ok
      }
      finally
      {
         try
         {
            embedded.undeploy(raa);
            fail("Null undeployment successful");
         }
         catch (Throwable t)
         {
            // Ok
         }
      }
   }

   /**
    * Basic ShrinkWrap ResourceAdapterArchive test case
    * @exception Throwable Thrown if case of an error
    */
   @Test
   public void testBasic() throws Throwable
   {
      ResourceAdapterArchive raa =
         Archives.create(UUID.randomUUID().toString() + ".rar", ResourceAdapterArchive.class);

      JavaArchive ja = Archives.create(UUID.randomUUID().toString() + ".jar", JavaArchive.class);
      ja.addClasses(MessageListener.class, TestActivationSpec.class, TestConnection.class,
                    TestConnectionInterface.class, TestManagedConnection.class, 
                    TestManagedConnectionFactory.class, TestResourceAdapter.class);

      raa.addLibrary(ja);
      raa.addManifestResource("simple.rar/META-INF/ra.xml", "ra.xml");

      try
      {
         embedded.deploy(raa);
      }
      catch (Throwable t)
      {
         log.error(t.getMessage(), t);
         fail(t.getMessage());
      }
      finally
      {
         embedded.undeploy(raa);
      }
   }

   // --------------------------------------------------------------------------------||
   // Lifecycle Methods --------------------------------------------------------------||
   // --------------------------------------------------------------------------------||

   /**
    * Lifecycle start, before the suite is executed
    * @throws Throwable throwable exception 
    */
   @BeforeClass
   public static void beforeClass() throws Throwable
   {
      // Create and set an embedded JCA instance
      embedded = new EmbeddedJCA();

      // Startup
      embedded.startup();
   }

   /**
    * Lifecycle stop, after the suite is executed
    * @throws Throwable throwable exception 
    */
   @AfterClass
   public static void afterClass() throws Throwable
   {
      // Shutdown embedded
      embedded.shutdown();

      // Set embedded to null
      embedded = null;
   }
}
