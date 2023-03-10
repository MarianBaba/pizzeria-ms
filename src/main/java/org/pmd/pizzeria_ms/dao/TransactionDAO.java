package org.pmd.pizzeria_ms.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.pmd.pizzeria_ms.model.Transaction;
import org.pmd.pizzeria_ms.enumeration.Type;

public class TransactionDAO implements DAO<Transaction> {
	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> getAll() {
		Session session = factory.openSession();
		org.hibernate.Transaction tx = null;
		List<Transaction> transactions = null;
		
		try {
			tx = session.beginTransaction();
			transactions = session.createQuery("FROM Transaction").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return transactions;
	}
	
	@Override
	public Optional<Transaction> get(Integer id) {
		Session session = factory.openSession();
		return Optional.ofNullable(session.get(Transaction.class, id));
	}
	
	@Override
	public Integer save(String... params) {
		Type type = Type.valueOf(params[0]);
		Double amount = Double.valueOf(params[1]);
		String description = params[2];
		LocalDate date = LocalDate.parse(params[3]);
		
		Session session = factory.openSession();
		org.hibernate.Transaction tx = null;
		Integer id = null;
		
		try {
			tx = session.beginTransaction();
			Transaction transaction = new Transaction();
			transaction.setType(type);
			transaction.setAmount(amount);
			transaction.setDescription(description);
			transaction.setDate(date);
			id = (Integer) session.save(transaction);
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
		org.hibernate.Transaction tx = null;
	    
	    try {
	    	Transaction transaction = get(id).get();
	    	if (params.containsKey("type")) {
	    		transaction.setType(Type.valueOf(params.get("type")));
	    	}
	    	if (params.containsKey("amount")) {
	    		transaction.setAmount(Double.valueOf(params.get("amount")));
	    	}
	    	if (params.containsKey("description")) {
	    		transaction.setDescription(params.get("description"));
	    	}
	    	if (params.containsKey("date")) {
	    		transaction.setDate(LocalDate.parse(params.get("date")));
	    	}
	    	
	    	tx = session.beginTransaction();
	    	session.update(transaction);
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
		org.hibernate.Transaction tx = null;
	    try {
	         tx = session.beginTransaction(); 
	         Transaction transaction = get(id).get();
	         session.delete(transaction); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }	
	}
}
