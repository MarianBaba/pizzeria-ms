package org.pmd.pizzeria_ms.jdbc;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.pmd.pizzeria_ms.model.Order;
import org.pmd.pizzeria_ms.model.Pizza;
import org.pmd.pizzeria_ms.model.Product;
import org.pmd.pizzeria_ms.model.Transaction;

public class SessionFactoryManager {
	
	private static SessionFactory factory = new Configuration().configure()
					.addAnnotatedClass(Product.class)
					.addAnnotatedClass(Transaction.class)
					.addAnnotatedClass(Order.class)
					.addAnnotatedClass(Pizza.class)
					.buildSessionFactory();
	
	@SuppressWarnings("exports")
	public static SessionFactory getSessionFactory() {
		return factory;
	}
	
	private SessionFactoryManager() {
		
	}

}