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

package org.jboss.jca.common.metadata.jbossra;

import org.jboss.jca.common.api.metadata.jbossra.JbossRa;
import org.jboss.jca.common.api.metadata.ra.RaConfigProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * A JbossRa. Abstract class containig common memeber for jboss_ra_1_0 and jboss_ra_2_0
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public abstract class JbossRaAbstractImpl implements JbossRa
{

   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   private final ArrayList<RaConfigProperty<?>> raConfigProperties;

   /**
    *
    * Create a new JbossRa. Protected constructor for subclass convenience
    *
    * @param raConfigProperties properties list
    */
   protected JbossRaAbstractImpl(List<RaConfigProperty<?>> raConfigProperties)
   {
      if (raConfigProperties != null)
      {
         this.raConfigProperties = new ArrayList<RaConfigProperty<?>>(raConfigProperties.size());
         this.raConfigProperties.addAll(raConfigProperties);
      }
      else
      {
         this.raConfigProperties = new ArrayList<RaConfigProperty<?>>(0);
      }
   }

   /**
    * @return raConfigProperties properties list
    */
   @Override
   public List<RaConfigProperty<?>> getRaConfigProperties()
   {
      return raConfigProperties == null ? null : Collections.unmodifiableList(raConfigProperties);
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((raConfigProperties == null) ? 0 : raConfigProperties.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof JbossRaAbstractImpl))
         return false;
      JbossRaAbstractImpl other = (JbossRaAbstractImpl) obj;
      if (raConfigProperties == null)
      {
         if (other.raConfigProperties != null)
            return false;
      }
      else if (!raConfigProperties.equals(other.raConfigProperties))
         return false;
      return true;
   }

   @Override
   public String toString()
   {
      return "JbossRa [raConfigProperties=" + raConfigProperties + "]";
   }

}
