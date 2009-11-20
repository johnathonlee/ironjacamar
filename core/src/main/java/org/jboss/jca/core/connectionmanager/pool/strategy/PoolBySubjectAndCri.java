/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008-2009, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jca.core.connectionmanager.pool.strategy;

import org.jboss.jca.core.connectionmanager.pool.AbstractPool;
import org.jboss.jca.core.connectionmanager.pool.PoolParams;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;

/**
 * Pool implementation that uses subject and connection
 * request info for its pool partition.
 * 
 * @author <a href="mailto:gurkanerdogdu@yahoo.com">Gurkan Erdogdu</a>
 * @version $Rev: $
 */
public class PoolBySubjectAndCri extends AbstractPool
{
   
   /**
    * Creates a new instance.
    * @param mcf managed connection factory
    * @param poolParams pool parameter
    * @param noTxSeparatePools notx seperate pool
    */
   protected PoolBySubjectAndCri(final ManagedConnectionFactory mcf, final PoolParams poolParams,
         final boolean noTxSeparatePools)
   {
      super(mcf, poolParams, noTxSeparatePools);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Object getKey(Subject subject, ConnectionRequestInfo cri, boolean separateNoTx) throws ResourceException
   {
      return new SubjectCriKey(subject, cri, separateNoTx);
   }

   /**
    * Prefill.
    * @param subject subject instance
    * @param cri connection request info
    */
   public void prefill(Subject subject, ConnectionRequestInfo cri)
   {
      prefill(subject, cri, false);

   }
   
   /**
    * {@inheritDoc}
    */
   public void prefill(Subject subject, ConnectionRequestInfo cri, boolean noTxSeperatePool)
   {
      if (getPreFill())
      {
         log.warn("Prefill pool option was selected for pool with JNDI name " + getPoolName()
               + " that does not support this feature.");
         
         log.warn("Please verify your *-ds.xml file that corresponds with this resource and " + 
               "either remove " + "the <prefill>true|false</prefill element or explicitly set this value to false.");

      }
   }
   
}
