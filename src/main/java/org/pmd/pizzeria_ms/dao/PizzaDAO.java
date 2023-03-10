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
		Transaction tx = null;
		List<Pizza> pizzas = null;
		
		try {
			tx = session.beginTransaction();
			pizzas = session.createQuery("FROM Pizza").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
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
		String name = params[0];
		Double price = Double.valueOf(params[1]);
		String description = params[2];
		
		Session session = factory.openSession();
		Transaction tx = null;
		Integer id = null;
		
		try {
			tx = session.beginTransaction();
			Pizza pizza = new Pizza();
			pizza.setName(name);
			pizza.setPrice(price);
			pizza.setDescription(description);
			id = (Integer) session.save(pizza);
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
	        e.printStackTrace();
		} finally {
			session.close();
		}
		
		return id;
	}
	
	@Override
	public void update(Integer id, Map<String, String> params) {
		Session session = factory.openSession();
	    Transaction tx = null;
	    
	    try {
	    	Pizza pizza = get(id).get();
	    	if (params.containsKey("name")) {
	    		pizza.setName(params.get("name"));
	    	}
	    	if (params.containsKey("description")) {
	    		pizza.setDescription(params.get("description"));
	    	}
	    	tx = session.beginTransaction();
	    	session.update(pizza);
	    	tx.commit();
	    } catch (HibernateException e) {
	    	e.printStackTrace();
	    } finally {
	    	session.close();
	    }
	    
	}
	
	@Override
	public void delete(Integer id) {
		Session session = factory.openSession();
	    Transaction tx = null;
	    try {
	         tx = session.beginTransaction(); 
	         Pizza pizza = get(id).get();
	         session.delete(pizza); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }	
	}
}
