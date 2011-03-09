/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jca.common.metadata.resourceadapter;

import org.jboss.jca.common.api.metadata.common.CommonAdminObject;
import org.jboss.jca.common.api.metadata.common.CommonConnDef;
import org.jboss.jca.common.api.metadata.common.CommonXaPool;
import org.jboss.jca.common.api.metadata.common.Credential;
import org.jboss.jca.common.api.metadata.common.Extension;
import org.jboss.jca.common.api.metadata.common.TransactionSupportEnum;
import org.jboss.jca.common.api.metadata.ds.Recovery;
import org.jboss.jca.common.api.metadata.resourceadapter.ResourceAdapter;
import org.jboss.jca.common.api.metadata.resourceadapter.ResourceAdapters;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.jboss.util.file.FileSuffixFilter;
import org.jboss.util.file.FilenamePrefixFilter;

import org.hamcrest.core.IsNull;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNull;

/**
 *
 * A ResourceAdapterParserTestCase.
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public class ResourceAdapterParserTestCase
{
   /**
    * shouldParseAnyExample
    * @throws Exception in case of error
    */
   @Test
   public void shouldParseAnyExample() throws Exception
   {
      FileInputStream is = null;

      //given
      File directory = new File(Thread.currentThread().getContextClassLoader().getResource("resource-adapter")
         .toURI());
      for (File xmlFile : directory.listFiles(new FileSuffixFilter("-ra.xml")))
      {
         System.out.println(xmlFile.getName());
         try
         {
            is = new FileInputStream(xmlFile);
            ResourceAdapterParser parser = new ResourceAdapterParser();
            //when
            ResourceAdapters ra = parser.parse(is);
            //then
            assertThat(ra.getResourceAdapters().size() >= 1, is(true));

         }
         finally
         {
            if (is != null)
               is.close();
         }
      }
   }

   /**
   *
   * shouldParseEmptyFileAndHaveNullMDContents
   * @throws Exception in case of error
   */
   @Test
   public void shouldParseEmptyFileAndHaveNullMDContents() throws Exception
   {
      FileInputStream is = null;

      //given
      File directory = new File(Thread.currentThread().getContextClassLoader().getResource("resource-adapter")
         .toURI());
      File xmlFile = directory.listFiles(new FilenamePrefixFilter("empty-ra.xml"))[0];
      try
      {
         is = new FileInputStream(xmlFile);
         ResourceAdapterParser parser = new ResourceAdapterParser();
         //when
         ResourceAdapters ra = parser.parse(is);
         //then
         assertThat(ra.getResourceAdapters().size() == 1, is(true));
         ResourceAdapter res = ra.getResourceAdapters().get(0);
         assertThat(res.getAdminObjects(), new IsNull<List<CommonAdminObject>>());
         assertThat(res.getConfigProperties(), new IsNull<Map<String, String>>());
         assertThat(res.getBeanValidationGroups(), new IsNull<List<String>>());
         assertThat(res.getConnectionDefinitions(), new IsNull<List<CommonConnDef>>());
         assertThat(res.getBootstrapContext(), new IsNull<String>());
         assertThat(res.getTransactionSupport(), new IsNull<TransactionSupportEnum>());
         assertThat(res.getArchive(), is("token"));

      }
      finally
      {
         if (is != null)
            is.close();
      }

   }

   /**
   *
   * shouldParseRaWithRecovery
   * @throws Exception in case of error
   */
   @Test
   public void shouldParseRaWithRecovery() throws Exception
   {
      FileInputStream is = null;

      //given
      File directory = new File(Thread.currentThread().getContextClassLoader().getResource("resource-adapter")
         .toURI());
      File xmlFile = directory.listFiles(new FilenamePrefixFilter("recovery-ra.xml"))[0];
      try
      {
         is = new FileInputStream(xmlFile);
         ResourceAdapterParser parser = new ResourceAdapterParser();
         //when
         ResourceAdapters ra = parser.parse(is);
         //then
         assertThat(ra.getResourceAdapters().size() == 1, is(true));
         ResourceAdapter res = ra.getResourceAdapters().get(0);
         CommonXaPool xaPool = (CommonXaPool) res.getConnectionDefinitions().get(0).getPool();
         Recovery recovery = xaPool.getRecovery();
         assertThat(recovery, not(isNull()));
         assertThat(recovery.getNoRecovery(), is(false));
         Credential security = recovery.getSecurity();
         Extension plugin = recovery.getPlugin();
         assertThat(security.getUserName(), is("myUserName"));
         assertThat(security.getPassword(), is("myPassword"));
         assertThat(security.getSecurityDomain(), is("mySecurityDomain"));
         assertThat(plugin.getClassName(), is("myClassName"));
         assertThat(plugin.getConfigPropertiesMap().size(), is(1));
         assertThat(plugin.getConfigPropertiesMap().get("MyProperty"), is("MyPropertyValue"));

      }
      finally
      {
         if (is != null)
            is.close();
      }

   }
}
