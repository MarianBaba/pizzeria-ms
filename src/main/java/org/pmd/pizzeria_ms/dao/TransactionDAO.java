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
		org.hibernate.Transaction transaction = null;
		List<Transaction> transactions = null;
		
		try {
			transaction = session.beginTransaction();
			transactions = session.createQuery("FROM Transaction").list();
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
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
		Session session = factory.openSession();
		org.hibernate.Transaction transaction = null;
		Integer id = null;
		
		try {
			transaction = session.beginTransaction();
			Transaction transactionToSave = new Transaction();
			transactionToSave.setType(Type.valueOf(params[0]));
			transactionToSave.setAmount(Double.valueOf(params[1]));
			transactionToSave.setDescription(params[2]);
			transactionToSave.setDate(LocalDate.parse(params[3]));
			id = (Integer) session.save(transactionToSave);
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
		org.hibernate.Transaction transaction = null;
	    
	    try {
	    	Transaction transactionToUpdate = get(id).get();
	    	if (params.containsKey("type")) {
	    		transactionToUpdate.setType(Type.valueOf(params.get("type")));
	    	}
	    	if (params.containsKey("amount")) {
	    		transactionToUpdate.setAmount(Double.valueOf(params.get("amount")));
	    	}
	    	if (params.containsKey("description")) {
	    		transactionToUpdate.setDescription(params.get("description"));
	    	}
	    	if (params.containsKey("date")) {
	    		transactionToUpdate.setDate(LocalDate.parse(params.get("date")));
	    	}
	    	
	    	transaction = session.beginTransaction();
	    	session.update(transactionToUpdate);
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
		org.hibernate.Transaction transaction = null;
	    try {
	         transaction = session.beginTransaction(); 
	         Transaction transactionToDelete = get(id).get();
	         session.delete(transactionToDelete); 
	         transaction.commit();
	      } catch (HibernateException e) {
	         if (transaction!=null) transaction.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }	
	}
}
