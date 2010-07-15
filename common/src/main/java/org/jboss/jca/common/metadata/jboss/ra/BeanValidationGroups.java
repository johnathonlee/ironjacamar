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
package org.jboss.jca.common.metadata.jboss.ra;

import org.jboss.jca.common.metadata.JCAMetadata;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:stefano.maestri@jboss.org">Stefano Maestri</a>
 *
 */
public class BeanValidationGroups implements JCAMetadata
{

   /**
    */
   private static final long serialVersionUID = 6856138720550993874L;

   private final List<String> beanValidationGroup;

   /**
    * @param beanValidationGroup List of bean validation group
    */
   public BeanValidationGroups(List<String> beanValidationGroup)
   {
      super();
      this.beanValidationGroup = beanValidationGroup;
   }

   /**
    * @return beanValidationGroup the list of bena validation group
    */
   public List<String> getBeanValidationGroup()
   {
      return Collections.unmodifiableList(beanValidationGroup);
   }

   /**
    * {@inheritDoc}
    *
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((beanValidationGroup == null) ? 0 : beanValidationGroup.hashCode());
      return result;
   }

   /**
    * {@inheritDoc}
    *
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof BeanValidationGroups))
         return false;
      BeanValidationGroups other = (BeanValidationGroups) obj;
      if (beanValidationGroup == null)
      {
         if (other.beanValidationGroup != null)
            return false;
      }
      else if (!beanValidationGroup.equals(other.beanValidationGroup))
         return false;
      return true;
   }

   /**
    * {@inheritDoc}
    *
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return "BeanValidationGroups [beanValidationGroup=" + beanValidationGroup + ", getBeanValidationGroup()="
            + getBeanValidationGroup() + ", hashCode()=" + hashCode() + "]";
   }

}
