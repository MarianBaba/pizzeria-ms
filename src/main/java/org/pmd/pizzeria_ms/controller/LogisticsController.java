package org.pmd.pizzeria_ms.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.pmd.pizzeria_ms.dao.OrderDAO;
import org.pmd.pizzeria_ms.dao.TransactionDAO;
import org.pmd.pizzeria_ms.model.Order;
import org.pmd.pizzeria_ms.model.Pizza;
import org.pmd.pizzeria_ms.utility.CSVManager;
import org.pmd.pizzeria_ms.view.Pizzeria;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.application.*;

public class LogisticsController implements Initializable {
	
	private static OrderDAO orderDAO = new OrderDAO();
	private static TransactionDAO transactionDAO = new TransactionDAO();
	
	@FXML
	private TableView<Order> ordersTable;
	
	@FXML
	private TableColumn<Order, Integer> id;
	
	@FXML
	private TableColumn<Order, String> description;
	
	@FXML
	private TableColumn<Pizza, Double> price;

	@FXML
	private TableColumn<Pizza, LocalDate> date;

	@FXML
	private TableColumn<Pizza, String> supplier;
	
	// inizializzazine della tabella
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		description.setCellValueFactory(new PropertyValueFactory<>("description"));
		price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		supplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
		
		TableColumn<Order, Void> deleteButtonColumn = new TableColumn<>("azioni");
		Callback<TableColumn<Order, Void>, TableCell<Order, Void>> deleteButtonCellFactory = 
				new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
            		@Override
            		public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
            			final TableCell<Order, Void> cell = new TableCell<Order, Void>() {
            				private final Button btn = new Button("Elimina");
            				
            				{
            					btn.setOnAction((ActionEvent event) -> {
            						Integer orderToDelete = getTableView().getItems().get(getIndex()).getId();
            						orderDAO.delete(orderToDelete);
            						try {
            							Pizzeria.setRoot("logistics");
            						} catch (IOException e) {
            							e.printStackTrace();
            						}
            					});
            				}
            				@Override
            				public void updateItem(Void item, boolean empty) {
            					super.updateItem(item, empty);
            					if (empty) {
            						setGraphic(null);
            					} else {
            						setGraphic(btn);
            					}
            				}
            			};
            		return cell;
            		}
        		};
        
        deleteButtonColumn.setCellFactory(deleteButtonCellFactory);
        
        final Callback<TableColumn<Order,String>, TableCell<Order,String>> WRAPPING_CELL_FACTORY = 
                new Callback<TableColumn<Order,String>, TableCell<Order,String>>() {
                    
            		@Override public TableCell<Order,String> call(TableColumn<Order,String> param) {
            		TableCell<Order,String> tableCell = new TableCell<Order,String>() {
            			@Override 
            			protected void updateItem(String item, boolean empty) {
            				if (item == getItem()) return;
            				super.updateItem(item, empty);

            				if (item == null) {
            					super.setText(null);
            					super.setGraphic(null);
            				} else {
            					super.setText(null);
            					Label l = new Label(item);
            					l.setWrapText(true);
	                            VBox box = new VBox(l);
	                            l.heightProperty().addListener((observable,oldValue,newValue)-> {
	                            	box.setPrefHeight(newValue.doubleValue()+1);
	                            	Platform.runLater(()->this.getTableRow().requestLayout());
	                            });
	                            super.setGraphic(box);
            				}
            			}
            		};
            		return tableCell;
            	}
        	};
        	
        description.setCellFactory(WRAPPING_CELL_FACTORY);
        List<Order> orders = orderDAO.getAll();
        ObservableList<Order> ordersList = FXCollections.observableArrayList(orders);
        
        ordersTable.getColumns().add(deleteButtonColumn);
        ordersTable.setItems(ordersList);
        ordersTable.setMinWidth(Region.USE_PREF_SIZE);
        ordersTable.setMinHeight(Region.USE_PREF_SIZE);
	}
	
	// insert fields + save function
	
	@FXML
	private TextField insertDescription;
	
	@FXML
	private TextField insertTotalPrice;
	
	@FXML
	private TextField insertDate;
	
	@FXML
	private TextField insertSupplier;
	
	@FXML
	public void save() throws IOException {
		Integer newOrderId = orderDAO.save(insertDescription.getText(), insertTotalPrice.getText(), insertDate.getText(), insertSupplier.getText());
		transactionDAO.save("EXPENSE", insertTotalPrice.getText(), "Nuovo ordine: " + newOrderId, LocalDate.now().toString());
		Pizzeria.setRoot("logistics");
	}
	
	// update fields + update function
	
	@FXML
	private TextField updateId;
	
	@FXML
	private TextField updateDescription;
	
	@FXML
	private TextField updateTotalPrice;
	
	@FXML
	private TextField updateDate;
	
	@FXML
	private TextField updateSupplier;
	
	@FXML
	private void update() throws IOException {
		Map<String, String> updatedOrder = new HashMap<>();
		if (updateDescription.getText() != null && !updateDescription.getText().equals("")) {
			updatedOrder.put("description", updateDescription.getText());
		}
		if (updateTotalPrice.getText() != null && !updateTotalPrice.getText().equals("")) {
			updatedOrder.put("totalPrice", updateTotalPrice.getText());
		}
		if (updateDate.getText() != null && !updateDate.getText().equals("")) {
			updatedOrder.put("date", updateDate.getText());
		}
		if (updateSupplier.getText() != null && !updateSupplier.getText().equals("")) {
			updatedOrder.put("supplier", updateSupplier.getText());
		}
		orderDAO.update(Integer.valueOf(updateId.getText()), updatedOrder);
		Pizzeria.setRoot("logistics");
	}
	
	@FXML
	public void exportCSV() throws IOException {
		CSVManager.exportCSV("orders");
	}
	
	@FXML
	public void switchToPizzeria() throws IOException {
		Pizzeria.setRoot("pizzeria");
	}
}
