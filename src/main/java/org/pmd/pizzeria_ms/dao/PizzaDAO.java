package org.pmd.pizzeria_ms.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmd.pizzeria_ms.model.Pizza;

public class PizzaDAO implements DAO<Pizza> {
	@SuppressWarnings("unchecked")
	@Override
	public List<Pizza> getAll() {
		Session session = factory.openSession();
		Transaction transaction = null;
		List<Pizza> pizzas = null;
		
		try {
			transaction = session.beginTransaction();
			pizzas = session.createQuery("FROM Pizza").list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pizzas;
	}
	
	@Override
	public Optional<Pizza> get(Integer id) {
		Session session = factory.openSession();
		return Optional.ofNullable(session.get(Pizza.class, id));
	}
	
	@Override
	public Integer save(String... params) {
		Session session = factory.openSession();
		Transaction transaction = null;
		Integer id = null;
		
		try {
			transaction = session.beginTransaction();
			Pizza pizza = new Pizza();
			pizza.setName(params[0]);
			pizza.setPrice(Double.valueOf(params[1]));
			pizza.setDescription(params[2]);
			id = (Integer) session.save(pizza);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction!=null) transaction.rollback();
	        e.printStackTrace();
		} finally {
			session.close();
		}
		
		return id;
	}
	
	@Override
	public void update(Integer id, Map<String, String> params) {
		Session session = factory.openSession();
	    Transaction transaction = null;
	    
	    try {
	    	Pizza pizza = get(id).get();
	    	if (params.containsKey("name")) {
	    		pizza.setName(params.get("name"));
	    	}
	    	if (params.containsKey("description")) {
	    		pizza.setDescription(params.get("description"));
	    	}
	    	transaction = session.beginTransaction();
	    	session.update(pizza);
	    	transaction.commit();
	    } catch (HibernateException e) {
	    	e.printStackTrace();
	    } finally {
	    	session.close();
	    }
	}
	
	@Override
	public void delete(Integer id) {
		Session session = factory.openSession();
	    Transaction transaction = null;
	    try {
	         transaction = session.beginTransaction(); 
	         Pizza pizza = get(id).get();
	         session.delete(pizza); 
	         transaction.commit();
	      } catch (HibernateException e) {
	         if (transaction!=null) transaction.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }	
	}
}
