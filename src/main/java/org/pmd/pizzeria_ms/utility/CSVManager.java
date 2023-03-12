package org.pmd.pizzeria_ms.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.pmd.pizzeria_ms.model.Transaction;
import org.pmd.pizzeria_ms.model.Product;
import org.pmd.pizzeria_ms.model.Pizza;
import org.pmd.pizzeria_ms.model.Order;
import org.pmd.pizzeria_ms.dao.TransactionDAO;
import org.pmd.pizzeria_ms.dao.OrderDAO;
import org.pmd.pizzeria_ms.dao.PizzaDAO;
import org.pmd.pizzeria_ms.dao.ProductDAO;



public class CSVManager {
	
	private static TransactionDAO transactionDAO = new TransactionDAO();
	private static OrderDAO orderDAO = new OrderDAO();
	private static PizzaDAO pizzaDAO = new PizzaDAO();
	private static ProductDAO productDAO = new ProductDAO();
	
	public static void exportCSV(String dataToExport) throws IOException {
		switch(dataToExport) {
			case "orders": {
				Writer writer = null;
				try {
					File file = new File("C:\\Users\\maria\\Desktop\\logistics.csv");
					writer = new BufferedWriter(new FileWriter(file));
					
					List<Order> orders = orderDAO.getAll();
					for (Order o : orders) {
						String row = o.getId() + "," + o.getDescription() + "," + o.getTotalPrice() + "," + o.getDate() + "," + o.getSupplier() + "\n";
						writer.write(row);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					writer.flush();
					writer.close();
				}
				break;
			}
			case "pizzas": {
				Writer writer = null;
				try {
					File file = new File("C:\\Users\\maria\\Desktop\\menu.csv");
					writer = new BufferedWriter(new FileWriter(file));
					
					List<Pizza> pizzas = pizzaDAO.getAll();
					for (Pizza p : pizzas) {
						String row = p.getId() + "," + p.getName() + "," + p.getPrice() + "," + p.getDescription() + "\n";
						writer.write(row);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					writer.flush();
					writer.close();
				}
				break;
			}
			case "products": {
				Writer writer = null;
				try {
					File file = new File("C:\\Users\\maria\\Desktop\\warehouse.csv");
					writer = new BufferedWriter(new FileWriter(file));
					
					List<Product> products = productDAO.getAll();
					for (Product p : products) {
						String row = p.getId() + "," + p.getName() + "," + p.getPrice() + "," + p.getQuantity() + "\n";
						writer.write(row);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					writer.flush();
					writer.close();
				}
				break;
			}
			case "transactions": {
				Writer writer = null;
				try {
					File file = new File("C:\\Users\\maria\\Desktop\\cash-register.csv");
					writer = new BufferedWriter(new FileWriter(file));
					
					List<Transaction> transactions = transactionDAO.getAll();
					for (Transaction t : transactions) {
						String row = t.getId() + "," + t.getType() + "," + t.getAmount() + "," + t.getDescription() + "," + t.getDate() + "\n";
						writer.write(row);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					writer.flush();
					writer.close();
				}
				break;
			}
			default: {
				return;
			}
			
		}
	}
}
