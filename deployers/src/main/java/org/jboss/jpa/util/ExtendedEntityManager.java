package org.jboss.jpa.util;
/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.jboss.jpa.spi.XPCResolver;

/**
 * EntityManager a managed extended persistence context.
 *
 * Contract: setXpcResolver must be set before getPersistenceContext is called.
 *
 * @author Scott Marlow (cloned from work by Bill Burke)
 */
public class ExtendedEntityManager extends JPAEntityManagerDelegator implements EntityManager, Serializable

{
   private static final long serialVersionUID = 2L;
   private String identity;
   private static XPCResolver xpcResolver;

   public ExtendedEntityManager(String name)
   {
      this.identity = name;
   }

   public ExtendedEntityManager()
   {
   }

   public static void setXpcResolver(XPCResolver xpcResolver)
   {
      ExtendedEntityManager.xpcResolver = xpcResolver;
   }

   public void close()
   {
      throw new IllegalStateException("It is illegal to close an injected EntityManager");
   }

   @Override
   protected EntityManager getEntityManager()
   {
      return getPersistenceContext();
   }

   public Session getHibernateSession()
   {
      if (getPersistenceContext() instanceof HibernateEntityManager)
      {
         return ((HibernateEntityManager) getPersistenceContext()).getSession();
      }
      throw new RuntimeException("ILLEGAL ACTION:  Not a Hibernate persistence provider");
   }

   public EntityManager getPersistenceContext()
   {
      if (null == xpcResolver)
      {
         throw new RuntimeException("ExtendedEntityManager.xpcResolver is null which is a configuration error");
      }

      EntityManager persistenceContext = xpcResolver.getExtendedPersistenceContext(identity);
      if (persistenceContext == null)
         throw new RuntimeException("Unable to determine persistenceContext: " + identity);
      return persistenceContext;
   }
}

