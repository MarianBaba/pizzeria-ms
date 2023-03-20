package org.pmd.pizzeria_ms.jdbc;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.pmd.pizzeria_ms.model.Order;
import org.pmd.pizzeria_ms.model.Pizza;
import org.pmd.pizzeria_ms.model.Product;
import org.pmd.pizzeria_ms.model.Transaction;

public class SessionFactoryManager {
	
	// singleton pattern implemented
	
	private static SessionFactory factory;
	
	@SuppressWarnings("exports")
	public static SessionFactory getSessionFactory() {
		if (factory == null) {
			factory = new Configuration().configure()
					.addAnnotatedClass(Product.class)
					.addAnnotatedClass(Transaction.class)
					.addAnnotatedClass(Order.class)
					.addAnnotatedClass(Pizza.class)
					.buildSessionFactory();
		}
		return factory;
	}
	
	private SessionFactoryManager() {}
}