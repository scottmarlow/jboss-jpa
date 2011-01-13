package org.jboss.jpa.deployers.switchboard;

import org.jboss.jpa.deployment.ManagedEntityManagerFactory;
import org.jboss.jpa.deployment.PersistenceUnitDeployment;
import org.jboss.jpa.spi.PersistenceUnit;
import org.jboss.jpa.spi.PersistenceUnitRegistry;
import org.jboss.jpa.tx.TransactionScopedEntityManager;
import org.jboss.jpa.util.ExtendedEntityManager;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 * Objectfactory for transaction scoped persistence context entity manager.
 *
 * @author Scott Marlow
 */
public class EntityManagerObjectFactory implements ObjectFactory
{
   public Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception
   {
      if ( obj != null && obj instanceof Reference)
      {
         Reference ref = (Reference)obj;
         RefAddr puSupplierRef = ref.get("puName");
         String  puSupplier = (String)puSupplierRef.getContent();

         PersistenceUnit pu = PersistenceUnitRegistry.getPersistenceUnit(puSupplier);
         ManagedEntityManagerFactory factory = ((PersistenceUnitDeployment)pu).getManagedFactory();
         return new TransactionScopedEntityManager(factory);
      }
      return null;
   }
}
