package org.pmd.pizzeria_ms.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmd.pizzeria_ms.model.Product;

public class ProductDAO implements DAO<Product> {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAll() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Product> products = null;
		
		try {
			tx = session.beginTransaction();
			products = session.createQuery("FROM Product").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return products;
	}
	
	@Override
	public Optional<Product> get(Integer id) {
		Session session = factory.openSession();
		return Optional.ofNullable(session.get(Product.class, id));
	}
	
	@Override
	public Integer save(String... params) {
		String name = params[0];
		Double price = Double.valueOf(params[1]);
		Integer quantity = Integer.valueOf(params[2]);
		
		Session session = factory.openSession();
		Transaction tx = null;
		Integer id = null;
		
		try {
			tx = session.beginTransaction();
			Product product = new Product();
			product.setName(name);
			product.setPrice(price);
			product.setQuantity(quantity);
			id = (Integer) session.save(product);
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
	    	Product prod = get(id).get();
	    	if (params.containsKey("name")) {
	    		prod.setName(params.get("name"));
	    	}
	    	if (params.containsKey("price")) {
	    		prod.setPrice(Double.valueOf(params.get("price")));
	    	}
	    	if (params.containsKey("quantity")) {
	    		prod.setQuantity(Integer.valueOf(params.get("quantity")));
	    	}
	    	tx = session.beginTransaction();
	    	session.update(prod);
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
	         Product product = get(id).get();
	         session.delete(product); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }	
	}
}
