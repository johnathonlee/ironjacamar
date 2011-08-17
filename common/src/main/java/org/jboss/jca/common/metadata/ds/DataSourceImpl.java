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
package org.jboss.jca.common.metadata.ds;

import org.jboss.jca.common.CommonBundle;
import org.jboss.jca.common.api.metadata.common.CommonPool;
import org.jboss.jca.common.api.metadata.ds.DataSource;
import org.jboss.jca.common.api.metadata.ds.DsSecurity;
import org.jboss.jca.common.api.metadata.ds.Statement;
import org.jboss.jca.common.api.metadata.ds.TimeOut;
import org.jboss.jca.common.api.metadata.ds.TransactionIsolation;
import org.jboss.jca.common.api.metadata.ds.Validation;
import org.jboss.jca.common.api.validator.ValidateException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jboss.logging.Messages;

/**
 *
 * A DataSourceImpl.
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public class DataSourceImpl extends DataSourceAbstractImpl implements DataSource
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -5214100851560229431L;

   /** The bundle */
   private static CommonBundle bundle = Messages.getBundle(CommonBundle.class);

   private final boolean jta;

   private final String connectionUrl;

   private String driverClass;

   private String dataSourceClass;

   private final String driver;

   private final HashMap<String, String> connectionProperties;

   private final String newConnectionSql;

   private final CommonPool pool;

   /**
    * Create a new DataSourceImpl.
    *
    * @param connectionUrl connectionUrl
    * @param driverClass driverClass
    * @param dataSourceClass dataSourceClass
    * @param driver driver
    * @param transactionIsolation transactionIsolation
    * @param connectionProperties connectionProperties
    * @param timeOut timeOut
    * @param security security
    * @param statement statement
    * @param validation validation
    * @param urlDelimiter urlDelimiter
    * @param urlSelectorStrategyClassName urlSelectorStrategyClassName
    * @param newConnectionSql newConnectionSql
    * @param useJavaContext useJavaContext
    * @param poolName poolName
    * @param enabled enabled
    * @param jndiName jndiName
    * @param spy spy
    * @param useccm useccm
    * @param jta jta
    * @param pool pool
    * @throws ValidateException ValidateException
    */
   public DataSourceImpl(String connectionUrl, String driverClass, String dataSourceClass, String driver,
                         TransactionIsolation transactionIsolation, Map<String, String> connectionProperties, 
                         TimeOut timeOut, DsSecurity security, Statement statement, Validation validation, 
                         String urlDelimiter, String urlSelectorStrategyClassName, String newConnectionSql, 
                         boolean useJavaContext, String poolName, boolean enabled, String jndiName, 
                         boolean spy, boolean useccm, boolean jta, CommonPool pool)
      throws ValidateException
   {
      super(transactionIsolation, timeOut, security, statement, validation, urlDelimiter,
            urlSelectorStrategyClassName, useJavaContext, poolName, enabled, jndiName, spy, useccm);
      this.jta = jta;
      this.connectionUrl = connectionUrl;
      this.driverClass = driverClass;
      this.dataSourceClass = dataSourceClass;
      this.driver = driver;
      if (connectionProperties != null)
      {
         this.connectionProperties = new HashMap<String, String>(connectionProperties.size());
         this.connectionProperties.putAll(connectionProperties);
      }
      else
      {
         this.connectionProperties = new HashMap<String, String>(0);
      }
      this.newConnectionSql = newConnectionSql;
      this.pool = pool;
      this.validate();
   }

   /**
    * {@inheritDoc}
    */
   public boolean isJTA()
   {
      return jta;
   }

   /**
    * Get the connectionUrl.
    *
    * @return the connectionUrl.
    */
   @Override
   public final String getConnectionUrl()
   {
      return connectionUrl;
   }

   /**
    * Get the driverClass.
    *
    * @return the driverClass.
    */
   @Override
   public final String getDriverClass()
   {
      return driverClass;
   }

   /**
    * Get the dataSourceClass.
    *
    * @return the dataSourceClass.
    */
   @Override
   public final String getDataSourceClass()
   {
      return dataSourceClass;
   }

   /**
    * Get the driver.
    *
    * @return the driver.
    */
   @Override
   public final String getDriver()
   {
      return driver;
   }

   /**
    * Get the connectionProperties.
    *
    * @return the connectionProperties.
    */
   @Override
   public final Map<String, String> getConnectionProperties()
   {
      return Collections.unmodifiableMap(connectionProperties);
   }

   /**
    * Get the statement.
    *
    * @return the statement.
    */
   @Override
   public final Statement getStatement()
   {
      return statement;
   }

   /**
    * Get the urlDelimiter.
    *
    * @return the urlDelimiter.
    */
   @Override
   public final String getUrlDelimiter()
   {
      return urlDelimiter;
   }

   /**
    * Get the urlSelectorStrategyClassName.
    *
    * @return the urlSelectorStrategyClassName.
    */
   @Override
   public final String getUrlSelectorStrategyClassName()
   {
      return urlSelectorStrategyClassName;
   }

   /**
    * Get the newConnectionSql.
    *
    * @return the newConnectionSql.
    */
   @Override
   public final String getNewConnectionSql()
   {
      return newConnectionSql;
   }

   /**
    * Get the pool.
    *
    * @return the pool.
    */
   @Override
   public final CommonPool getPool()
   {
      return pool;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((connectionProperties == null) ? 0 : connectionProperties.hashCode());
      result = prime * result + ((connectionUrl == null) ? 0 : connectionUrl.hashCode());
      result = prime * result + ((driver == null) ? 0 : driver.hashCode());
      result = prime * result + ((driverClass == null) ? 0 : driverClass.hashCode());
      result = prime * result + ((dataSourceClass == null) ? 0 : dataSourceClass.hashCode());
      result = prime * result + ((newConnectionSql == null) ? 0 : newConnectionSql.hashCode());
      result = prime * result + ((pool == null) ? 0 : pool.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof DataSourceImpl))
         return false;
      DataSourceImpl other = (DataSourceImpl) obj;
      if (connectionProperties == null)
      {
         if (other.connectionProperties != null)
            return false;
      }
      else if (!connectionProperties.equals(other.connectionProperties))
         return false;
      if (connectionUrl == null)
      {
         if (other.connectionUrl != null)
            return false;
      }
      else if (!connectionUrl.equals(other.connectionUrl))
         return false;
      if (driver == null)
      {
         if (other.driver != null)
            return false;
      }
      else if (!driver.equals(other.driver))
         return false;
      if (driverClass == null)
      {
         if (other.driverClass != null)
            return false;
      }
      else if (!driverClass.equals(other.driverClass))
         return false;
      if (dataSourceClass == null)
      {
         if (other.dataSourceClass != null)
            return false;
      }
      else if (!dataSourceClass.equals(other.dataSourceClass))
         return false;
      if (newConnectionSql == null)
      {
         if (other.newConnectionSql != null)
            return false;
      }
      else if (!newConnectionSql.equals(other.newConnectionSql))
         return false;
      if (pool == null)
      {
         if (other.pool != null)
            return false;
      }
      else if (!pool.equals(other.pool))
         return false;
      return true;
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();

      sb.append("<datasource");

      if (jndiName != null)
         sb.append(" ").append(DataSource.Attribute.JNDINAME).append("=\"").append(jndiName).append("\"");

      if (poolName != null)
         sb.append(" ").append(DataSource.Attribute.POOL_NAME).append("=\"").append(poolName).append("\"");

      if (enabled != null)
         sb.append(" ").append(DataSource.Attribute.ENABLED).append("=\"").append(enabled).append("\"");

      if (useJavaContext != null)
         sb.append(" ").append(DataSource.Attribute.USEJAVACONTEXT).append("=\"").append(useJavaContext).append("\"");

      if (spy)
         sb.append(" ").append(DataSource.Attribute.SPY).append("=\"").append(spy).append("\"");

      if (useCcm)
         sb.append(" ").append(DataSource.Attribute.USE_CCM).append("=\"").append(useCcm).append("\"");

      if (jta)
         sb.append(" ").append(DataSource.Attribute.JTA).append("=\"").append(jta).append("\"");

      sb.append(">");

      if (connectionUrl != null)
      {
         sb.append("<").append(DataSource.Tag.CONNECTIONURL).append(">");
         sb.append(connectionUrl);
         sb.append("</").append(DataSource.Tag.CONNECTIONURL).append(">");
      }

      if (driverClass != null)
      {
         sb.append("<").append(DataSource.Tag.DRIVERCLASS).append(">");
         sb.append(driverClass);
         sb.append("</").append(DataSource.Tag.DRIVERCLASS).append(">");
      }

      if (dataSourceClass != null)
      {
         sb.append("<").append(DataSource.Tag.DATASOURCECLASS).append(">");
         sb.append(dataSourceClass);
         sb.append("</").append(DataSource.Tag.DATASOURCECLASS).append(">");
      }

      if (driver != null)
      {
         sb.append("<").append(DataSource.Tag.DRIVER).append(">");
         sb.append(driver);
         sb.append("</").append(DataSource.Tag.DRIVER).append(">");
      }

      if (connectionProperties != null && connectionProperties.size() > 0)
      {
         Iterator<Map.Entry<String, String>> it = connectionProperties.entrySet().iterator();
         while (it.hasNext())
         {
            Map.Entry<String, String> entry = it.next();
            sb.append("<").append(DataSource.Tag.CONNECTIONPROPERTY);
            sb.append(" name=\"").append(entry.getKey()).append("\">");
            sb.append(entry.getValue());
            sb.append("</").append(DataSource.Tag.CONNECTIONPROPERTY).append(">");
         }
      }

      if (newConnectionSql != null)
      {
         sb.append("<").append(DataSource.Tag.NEWCONNECTIONSQL).append(">");
         sb.append(newConnectionSql);
         sb.append("</").append(DataSource.Tag.NEWCONNECTIONSQL).append(">");
      }

      if (transactionIsolation != null)
      {
         sb.append("<").append(DataSource.Tag.TRANSACTIONISOLATION).append(">");
         sb.append(transactionIsolation);
         sb.append("</").append(DataSource.Tag.TRANSACTIONISOLATION).append(">");
      }

      if (urlDelimiter != null)
      {
         sb.append("<").append(DataSource.Tag.URLDELIMITER).append(">");
         sb.append(urlDelimiter);
         sb.append("</").append(DataSource.Tag.URLDELIMITER).append(">");
      }

      if (urlSelectorStrategyClassName != null)
      {
         sb.append("<").append(DataSource.Tag.URLSELECTORSTRATEGYCLASSNAME).append(">");
         sb.append(urlSelectorStrategyClassName);
         sb.append("</").append(DataSource.Tag.URLSELECTORSTRATEGYCLASSNAME).append(">");
      }

      if (pool != null)
         sb.append(pool);

      if (security != null)
         sb.append(security);

      if (validation != null)
         sb.append(validation);

      if (timeOut != null)
         sb.append(timeOut);

      if (statement != null)
         sb.append(statement);

      sb.append("</datasource>");

      return sb.toString();
   }

   @Override
   public void validate() throws ValidateException
   {
      if (this.driverClass != null && (this.connectionUrl == null || this.connectionUrl.trim().length() == 0))
         throw new ValidateException(bundle.requiredElementMissing(Tag.CONNECTIONURL.getLocalName(), 
                                                                   this.getClass().getCanonicalName()));
      
      if ((this.driverClass == null || this.driverClass.trim().length() == 0) &&
          (this.dataSourceClass == null || this.dataSourceClass.trim().length() == 0) &&
          (this.driver == null || this.driver.trim().length() == 0))
         throw new ValidateException(bundle.requiredElementMissing(Tag.DRIVERCLASS.getLocalName(), 
                                                                   this.getClass().getCanonicalName()));
   }

   /**
    * Set the driverClass.
    *
    * @param driverClass The driverClass to set.
    */
   public final void forceDriverClass(String driverClass)
   {
      this.driverClass = driverClass;
   }

   /**
    * Set the dataSourceClass.
    *
    * @param dataSourceClass The dataSourceClass to set.
    */
   public final void forceDataSourceClass(String dataSourceClass)
   {
      this.dataSourceClass = dataSourceClass;
   }
}
