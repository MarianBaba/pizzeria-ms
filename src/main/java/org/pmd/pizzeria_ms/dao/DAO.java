package org.pmd.pizzeria_ms.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.pmd.pizzeria_ms.jdbc.SessionFactoryManager;

public interface DAO<T> {
	
	@SuppressWarnings("exports")
	SessionFactory factory = SessionFactoryManager.getSessionFactory();
	
	public List<T> getAll();
	public Optional<T> get(Integer id);
	public Integer save(String... params);
	public void update(Integer id, Map<String, String> params);
	public void delete(Integer id);

}
