package org.jboss.jpa.deployers.switchboard;

import org.jboss.jpa.util.ExtendedEntityManager;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 * Objectfactory for extended persistence context entity manager.
 *
 * @author Scott Marlow
 */
public class ExtendedEntityManagerObjectFactory implements ObjectFactory
{
   public Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception
   {
      if ( obj != null && obj instanceof Reference)
      {
         Reference ref = (Reference)obj;
         RefAddr puSupplierRef = ref.get("puName");
         String  puSupplier = (String)puSupplierRef.getContent();
         return new ExtendedEntityManager(puSupplier);
      }
      return null;
   }
}
