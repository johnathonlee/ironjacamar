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
package org.jboss.jca.common.metadata.common;

import org.jboss.jca.common.api.metadata.common.AdminObject;
import org.jboss.jca.common.api.metadata.common.AdminObject.Attribute;
import org.jboss.jca.common.api.metadata.common.CommonPool;
import org.jboss.jca.common.api.metadata.common.CommonSecurity;
import org.jboss.jca.common.api.metadata.common.CommonTimeOut;
import org.jboss.jca.common.api.metadata.common.CommonValidation;
import org.jboss.jca.common.api.metadata.common.ConnectionDefinition;
import org.jboss.jca.common.api.metadata.resourceadapter.ResourceAdapter;
import org.jboss.jca.common.metadata.AbstractParser;
import org.jboss.jca.common.metadata.ParserException;

import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 *
 * A CommonIronJacamarParser.
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public abstract class CommonIronJacamarParser extends AbstractParser
{

   /**
    *
    * parse a single connection-definition tag
    *
    * @param reader the reader
    * @return the parse {@link ConnectionDefinition} object
    * @throws XMLStreamException XMLStreamException
    * @throws ParserException ParserException
    */
   protected ConnectionDefinition parseConnectionDefinitions(XMLStreamReader reader) throws XMLStreamException,
      ParserException
   {
      HashMap<String, String> configProperties = new HashMap<String, String>();
      CommonSecurity security = null;
      CommonTimeOut timeOut = null;
      CommonValidation validation = null;
      boolean noTxSeparatePools = false;
      CommonPool pool = null;

      //attributes reading
      boolean useJavaContext = false;
      String className = null;
      boolean enabled = true;
      String jndiName = null;
      String poolName = null;

      for (ConnectionDefinition.Attribute attribute : ConnectionDefinition.Attribute.values())
      {
         switch (attribute)
         {
            case ENABLED : {
               enabled = attributeAsBoolean(reader, attribute.getLocalName(), true);
               break;
            }
            case JNDINAME : {
               jndiName = attributeAsString(reader, attribute.getLocalName());
               break;
            }
            case CLASS_NAME : {
               className = attributeAsString(reader, attribute.getLocalName());
               break;
            }
            case POOL_NAME : {
               poolName = attributeAsString(reader, attribute.getLocalName());
               break;
            }
            case USEJAVACONTEXT : {
               useJavaContext = attributeAsBoolean(reader, attribute.getLocalName(), false);
               break;
            }
            default :
               throw new ParserException("Unexpected attribute:" + attribute.getLocalName() + "at " +
                                         reader.getLocalName());
         }
      }

      while (reader.hasNext())
      {
         switch (reader.nextTag())
         {
            case END_ELEMENT : {
               if (ResourceAdapter.Tag.forName(reader.getLocalName()) == ResourceAdapter.Tag.CONNECTION_DEFINITION)
               {

                  return new ConnectionDefinitionImpl(configProperties, className, jndiName, poolName, enabled,
                                                      useJavaContext, pool, timeOut, validation, security,
                                                      noTxSeparatePools);
               }
               else
               {
                  if (ConnectionDefinition.Tag.forName(reader.getLocalName()) == ConnectionDefinition.Tag.UNKNOWN)
                  {
                     throw new ParserException("unexpected end tag" + reader.getLocalName());
                  }
               }
               break;
            }
            case START_ELEMENT : {
               switch (ConnectionDefinition.Tag.forName(reader.getLocalName()))
               {
                  case CONFIG_PROPERTY : {
                     configProperties.put(attributeAsString(reader, "name"), elementAsString(reader));
                     break;
                  }
                  case SECURITY : {
                     security = parseSecuritySettings(reader);
                     break;
                  }
                  case TIMEOUT : {
                     timeOut = parseTimeOut(reader);
                     break;
                  }
                  case VALIDATION : {
                     validation = parseValidation(reader);
                     break;
                  }
                  case NO_TX_SEPARATE_POOL : {
                     noTxSeparatePools = elementAsBoolean(reader);
                     break;
                  }
                  case POOL : {
                     pool = parsePool(reader);
                     break;
                  }
                  default :
                     throw new ParserException("Unexpected element:" + reader.getLocalName());
               }
               break;
            }
         }
      }
      throw new ParserException("Reached end of xml document unexpectedly");
   }

   private CommonValidation parseValidation(XMLStreamReader reader) throws XMLStreamException, ParserException
   {
      boolean useFastFail = false;
      boolean backgroundValidation = false;
      Long backgroundValidationMinutes = null;

      while (reader.hasNext())
      {
         switch (reader.nextTag())
         {
            case END_ELEMENT : {
               if (ConnectionDefinition.Tag.forName(reader.getLocalName()) == ConnectionDefinition.Tag.VALIDATION)
               {

                  return new CommonValidationImpl(backgroundValidation, backgroundValidationMinutes, useFastFail);
               }
               else
               {
                  if (CommonValidation.Tag.forName(reader.getLocalName()) == CommonValidation.Tag.UNKNOWN)
                  {
                     throw new ParserException("unexpected end tag" + reader.getLocalName());
                  }
               }
               break;
            }
            case START_ELEMENT : {
               switch (CommonValidation.Tag.forName(reader.getLocalName()))
               {
                  case BACKGROUNDVALIDATIONMINUTES : {
                     backgroundValidationMinutes = elementAsLong(reader);
                     break;
                  }
                  case BACKGROUNDVALIDATION : {
                     backgroundValidation = elementAsBoolean(reader);
                     break;
                  }
                  case USEFASTFAIL : {
                     useFastFail = elementAsBoolean(reader);
                     break;
                  }
                  default :
                     throw new ParserException("Unexpected element:" + reader.getLocalName());
               }
               break;
            }
         }
      }
      throw new ParserException("Reached end of xml document unexpectedly");
   }

   private CommonTimeOut parseTimeOut(XMLStreamReader reader) throws XMLStreamException, ParserException
   {
      Long blockingTimeoutMillis = null;
      Long allocationRetryWaitMillis = null;
      Long idleTimeoutMinutes = null;
      Long allocationRetry = null;
      Long xaResourceTimeout = null;

      while (reader.hasNext())
      {
         switch (reader.nextTag())
         {
            case END_ELEMENT : {
               if (ConnectionDefinition.Tag.forName(reader.getLocalName()) == ConnectionDefinition.Tag.TIMEOUT)
               {

                  return new CommonTimeOutImpl(blockingTimeoutMillis, idleTimeoutMinutes, allocationRetry,
                                               allocationRetryWaitMillis, xaResourceTimeout);
               }
               else
               {
                  if (CommonTimeOut.Tag.forName(reader.getLocalName()) == CommonTimeOut.Tag.UNKNOWN)
                  {
                     throw new ParserException("unexpected end tag" + reader.getLocalName());
                  }
               }
               break;
            }
            case START_ELEMENT : {
               switch (CommonTimeOut.Tag.forName(reader.getLocalName()))
               {
                  case ALLOCATIONRETRYWAITMILLIS : {
                     allocationRetryWaitMillis = elementAsLong(reader);
                     break;
                  }
                  case ALLOCATIONRETRY : {
                     allocationRetry = elementAsLong(reader);
                     break;
                  }
                  case BLOCKINGTIMEOUTMILLIS : {
                     blockingTimeoutMillis = elementAsLong(reader);
                     break;
                  }
                  case IDLETIMEOUTMINUTES : {
                     idleTimeoutMinutes = elementAsLong(reader);
                     break;
                  }
                  case XARESOURCETIMEOUT : {
                     xaResourceTimeout = elementAsLong(reader);
                     break;
                  }
                  default :
                     throw new ParserException("Unexpected element:" + reader.getLocalName());
               }
               break;
            }
         }
      }
      throw new ParserException("Reached end of xml document unexpectedly");
   }

   /**
    *
    * parse a single admin-oject tag
    *
    * @param reader the reader
    * @return the parsed {@link AdminObject}
    * @throws XMLStreamException XMLStreamException
    * @throws ParserException ParserException
    */
   protected AdminObject parseAdminObjects(XMLStreamReader reader) throws XMLStreamException, ParserException
   {
      HashMap<String, String> configProperties = new HashMap<String, String>();

      //attributes reading
      boolean useJavaContext = false;
      String className = null;
      boolean enabled = true;
      String jndiName = null;
      String poolName = null;

      for (Attribute attribute : AdminObject.Attribute.values())
      {
         switch (attribute)
         {
            case ENABLED : {
               enabled = attributeAsBoolean(reader, attribute.getLocalName(), true);
               break;
            }
            case JNDINAME : {
               jndiName = attributeAsString(reader, attribute.getLocalName());
               break;
            }
            case CLASS_NAME : {
               className = attributeAsString(reader, attribute.getLocalName());
               break;
            }
            case USEJAVACONTEXT : {
               useJavaContext = attributeAsBoolean(reader, attribute.getLocalName(), false);
               break;
            }
            case POOL_NAME : {
               poolName = attributeAsString(reader, attribute.getLocalName());
               break;
            }
            default :
               throw new ParserException("Unexpected attribute:" + attribute.getLocalName() + "at " +
                                         reader.getLocalName());
         }
      }

      while (reader.hasNext())
      {
         switch (reader.nextTag())
         {
            case END_ELEMENT : {
               if (ResourceAdapter.Tag.forName(reader.getLocalName()) == ResourceAdapter.Tag.ADMIN_OBJECT)
               {

                  return new AdminObjectImpl(configProperties, className, jndiName, poolName, enabled,
                                             useJavaContext);
               }
               else
               {
                  if (AdminObject.Tag.forName(reader.getLocalName()) == AdminObject.Tag.UNKNOWN)
                  {
                     throw new ParserException("unexpected end tag" + reader.getLocalName());
                  }
               }
               break;
            }
            case START_ELEMENT : {
               switch (AdminObject.Tag.forName(reader.getLocalName()))
               {
                  case CONFIG_PROPERTY : {
                     configProperties.put(attributeAsString(reader, "name"), elementAsString(reader));
                     break;
                  }
                  default :
                     throw new ParserException("Unexpected element:" + reader.getLocalName());
               }
               break;
            }
         }
      }
      throw new ParserException("Reached end of xml document unexpectedly");
   }

}
