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
	    Transaction tx = null;
	    List<Order> orders = null;
	    
	      try {
	         tx = session.beginTransaction();
	         orders = session.createQuery("FROM Order").list(); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
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
    	String description = params[0];
    	Double totalPrice = Double.valueOf(params[1]);
    	LocalDate date = LocalDate.parse(params[2]); // format: xxxx-xx-xx
    	String supplier = params[3];
    	
    	Session session = factory.openSession();
		Transaction tx = null;
    	Integer id = null;
		
		
		try {
			tx = session.beginTransaction();
			Order order = new Order();
			order.setDescription(description);
			order.setTotalPrice(totalPrice);
			order.setDate(date);
			order.setSupplier(supplier);
			id = (Integer) session.save(order);
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
	    	tx = session.beginTransaction();
	    	session.update(order);
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
	         Order order = get(id).get();
	         session.delete(order); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }	
	}
}
