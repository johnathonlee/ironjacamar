/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jca.adapters.jdbc.extensions.oracle;

import org.jboss.jca.adapters.jdbc.spi.ValidConnectionChecker;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.jboss.logging.Logger;

/**
 * Implements check valid connection sql
 *
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 105425 $
 */
public class OracleValidConnectionChecker implements ValidConnectionChecker, Serializable
{
   private static final long serialVersionUID = 5379340663276548636L;

   private static Logger log = Logger.getLogger(OracleValidConnectionChecker.class);

   // The timeout in seconds (apparently the timeout is ignored?)
   private Integer pingTimeOut;

   /**
    * Constructor
    */
   public OracleValidConnectionChecker()
   {
      pingTimeOut = Integer.valueOf(5);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public SQLException isValidConnection(Connection c)
   {
      Object[] params = new Object[] {pingTimeOut};

      try
      {
         Method ping = c.getClass().getMethod("pingDatabase", new Class<?>[] {Integer.TYPE});
         ping.setAccessible(true);

         Integer status = (Integer) ping.invoke(c, params);

         // Error
         if (status == null || status.intValue() < 0)
            return new SQLException("pingDatabase failed status=" + status);
      }
      catch (Exception e)
      {
         // What do we do here? Assume it is a misconfiguration
         log.warn("Unexpected error in pingDatabase", e);
      }

      // OK
      return null;
   }

   /**
    * Get the pingTimeOut.
    *
    * @return the pingTimeOut.
    */
   public Integer getPingTimeOut()
   {
      return pingTimeOut;
   }

   /**
    * Set the pingTimeOut.
    *
    * @param pingTimeOut The pingTimeOut to set.
    */
   public void setPingTimeOut(Integer pingTimeOut)
   {
      this.pingTimeOut = pingTimeOut;
   }
}
