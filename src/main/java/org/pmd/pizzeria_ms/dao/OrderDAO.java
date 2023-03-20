package org.pmd.pizzeria_ms.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmd.pizzeria_ms.model.Order;

public class OrderDAO implements DAO<Order> {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getAll() {
		Session session = factory.openSession();
	    Transaction transaction = null;
	    List<Order> orders = null;
	    
	      try {
	         transaction = session.beginTransaction();
	         orders = session.createQuery("FROM Order").list(); 
	         transaction.commit();
	      } catch (HibernateException e) {
	         if (transaction!=null) transaction.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	      
	      return orders;
	}
    
    @Override
    public Optional<Order> get(Integer id) {
    	Session session = factory.openSession();
		return Optional.ofNullable(session.get(Order.class, id));
    }
    
    @Override
    public Integer save(String... params) {
    	Session session = factory.openSession();
		Transaction transaction = null;
    	Integer id = null;
		
		try {
			transaction = session.beginTransaction();
			Order order = new Order();
			order.setDescription(params[0]);
			order.setTotalPrice(Double.valueOf(params[1]));
			order.setDate(LocalDate.parse(params[2]));
			order.setSupplier( params[3]);
			id = (Integer) session.save(order);
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
	    	Order order = get(id).get();
	    	if (params.containsKey("description")) {
	    		order.setDescription(params.get("description"));
	    	}
	    	if (params.containsKey("totalPrice")) {
	    		order.setTotalPrice(Double.valueOf(params.get("totalPrice")));
	    	}
	    	if (params.containsKey("date")) {
	    		order.setDate(LocalDate.parse(params.get("date")));
	    	}
	    	if (params.containsKey("supplier")) {
	    		order.setSupplier(params.get("supplier"));
	    	}
	    	transaction = session.beginTransaction();
	    	session.update(order);
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
	         Order order = get(id).get();
	         session.delete(order); 
	         transaction.commit();
	      } catch (HibernateException e) {
	         if (transaction!=null) transaction.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }	
	}
}
