/*
 * IronJacamar, a Java EE Connector Architecture implementation
 * Copyright 2012, Red Hat Inc, and individual contributors
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

package org.jboss.jca.sjc.ant;

import org.jboss.jca.sjc.ProcessController;

import org.apache.tools.ant.BuildException;

/**
 * Stop an IronJacamar/Standalone instance
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
public class Stop extends AbstractHomeTask
{
   /**
    * Constructor
    */
   public Stop()
   {
   }

   /**
    * Execute
    * @exception BuildException If the build fails
    */
   @Override
   public void execute() throws BuildException 
   {
      if (getHome() == null)
         throw new BuildException("Home isn't set");

      try 
      {
         ProcessController pc = ProcessController.getInstance();
         int exitCode = pc.stop(getHome());

         if (exitCode != 0)
            throw new BuildException("IronJacamar instance exit code: " + exitCode);

         log("Stopped IronJacamar instance from: " + getHome());
      }
      catch (BuildException be) 
      {
         throw be;
      }
      catch (Throwable t) 
      {
         throw new BuildException(t.getMessage(), t);
      }
   }
}
