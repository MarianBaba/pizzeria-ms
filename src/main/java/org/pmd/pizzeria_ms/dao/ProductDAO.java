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
		Transaction transaction = null;
		List<Product> products = null;
		
		try {
			transaction = session.beginTransaction();
			products = session.createQuery("FROM Product").list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
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
		Session session = factory.openSession();
		Transaction transaction = null;
		Integer id = null;
		
		try {
			transaction = session.beginTransaction();
			Product product = new Product();
			product.setName(params[0]);
			product.setPrice(Double.valueOf(params[1]));
			product.setQuantity(Integer.valueOf(params[2]));
			id = (Integer) session.save(product);
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
	    	transaction = session.beginTransaction();
	    	session.update(prod);
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
	         Product product = get(id).get();
	         session.delete(product); 
	         transaction.commit();
	      } catch (HibernateException e) {
	         if (transaction!=null) transaction.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }	
	}
}
