/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.jpa.hibernate36;

import org.jboss.jpa.spi.JtaManager;
import org.jboss.jpa.spi.PersistenceProviderAdaptor;
import org.jboss.jpa.spi.PersistenceUnitMetadata;

import java.util.Map;

/**
 * Implements the PersistenceProviderAdaptor for Hibernate
 *
 * @author Scott Marlow
 */
public class HibernatePersistenceProviderAdaptor implements PersistenceProviderAdaptor {

    @Override
    public void setJtaManager(JtaManager jtaManager) {
        HibernateTransactionManagerLookup.setTransactionManager(jtaManager.locateTransactionManager());
    }

    @Override
    public void addProviderProperties(Map properties, PersistenceUnitMetadata pu) {
        properties.put("hibernate.transaction.manager_lookup_class", "org.jboss.as.jpa.hibernate.HibernateTransactionManagerLookup");
        properties.put("hibernate.id.new_generator_mappings", "true");
        properties.put("hibernate.ejb.resource_scanner", "org.jboss.as.jpa.hibernate.HibernateAnnotationScanner");
    }


    @Override
    public Iterable getProviderDependencies(PersistenceUnitMetadata pu) {
        return null;
    }

    @Override
    public void beforeCreateContainerEntityManagerFactory(PersistenceUnitMetadata pu) {
        // set backdoor annotation scanner access to pu
        //HibernateAnnotationScanner.setThreadLocalPersistenceUnitMetadata(pu);
    }

    @Override
    public void afterCreateContainerEntityManagerFactory(PersistenceUnitMetadata pu) {
        // clear backdoor annotation scanner access to pu
        //HibernateAnnotationScanner.clearThreadLocalPersistenceUnitMetadata();
    }
}
