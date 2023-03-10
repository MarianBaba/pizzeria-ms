package org.pmd.pizzeria_ms.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
	private TableColumn<Order, Integer> idColumn;
	
	@FXML
	private TableColumn<Order, String> descriptionColumn;
	
	@FXML
	private TableColumn<Pizza, Double> priceColumn;

	@FXML
	private TableColumn<Pizza, LocalDate> dateColumn;

	@FXML
	private TableColumn<Pizza, String> supplierColumn;
	
	// inizializzazine della tabella
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
		
		TableColumn<Order, Void> deleteButtonColumn = new TableColumn<>("");
		Callback<TableColumn<Order, Void>, TableCell<Order, Void>> deleteButtonCellFactory = new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
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
        
        
        // TUTTO QUESTO SERVE PER FARE IN MODO CHE IL TESTO VADA A CAPO IN UNA CELLA DELLA TABELLA INVECE CHE DIVENTARE INVISIBILE
        final Callback<TableColumn<Order,String>, TableCell<Order,String>> WRAPPING_CELL_FACTORY = 
                new Callback<TableColumn<Order,String>, TableCell<Order,String>>() {
                    
            @Override public TableCell<Order,String> call(TableColumn<Order,String> param) {
                TableCell<Order,String> tableCell = new TableCell<Order,String>() {
                    @Override protected void updateItem(String item, boolean empty) {
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
        descriptionColumn.setCellFactory(WRAPPING_CELL_FACTORY);
        
        List<Order> orders = orderDAO.getAll();
        ObservableList<Order> ordersList = FXCollections.observableArrayList(orders);
        
        ordersTable.getColumns().add(deleteButtonColumn);
        ordersTable.setItems(ordersList);
        ordersTable.setMinWidth(Region.USE_PREF_SIZE);
        ordersTable.setMinHeight(Region.USE_PREF_SIZE);
	}
	
	// inserimento di un ordine
	@FXML
	private TextField insertDescriptionTextField;
	
	@FXML
	private TextField insertTotalPriceTextField;
	
	@FXML
	private TextField insertDateTextField;
	
	@FXML
	private TextField insertSupplierTextField;
	
	@FXML
	public void save() throws IOException {
		String description = insertDescriptionTextField.getText();
		String totalPrice = insertTotalPriceTextField.getText();
		String date = insertDateTextField.getText();
		String supplier = insertSupplierTextField.getText();
		Integer id = orderDAO.save(description, totalPrice, date, supplier);
		
		String type = "EXPENSE";
		String amount = totalPrice;
		String transactionDescription = "Nuovo ordine: " + id;
		String transactionDate = LocalDate.now().toString();
		transactionDAO.save(type, amount, transactionDescription, transactionDate);
		
		Pizzeria.setRoot("logistics");
	}
	
	// update di un ordine
	@FXML
	private TextField updateIdTextField;
	
	@FXML
	private TextField updateDescriptionTextField;
	
	@FXML
	private TextField updateTotalPriceTextField;
	
	@FXML
	private TextField updateDateTextField;
	
	@FXML
	private TextField updateSupplierTextField;
	
	@FXML
	private void update() throws IOException {
		Map<String, String> updatedOrder = new HashMap<>();
		if (updateDescriptionTextField.getText() != null && !updateDescriptionTextField.getText().equals("")) {
			updatedOrder.put("description", updateDescriptionTextField.getText());
		}
		if (updateTotalPriceTextField.getText() != null && !updateTotalPriceTextField.getText().equals("")) {
			updatedOrder.put("totalPrice", updateTotalPriceTextField.getText());
		}
		if (updateDateTextField.getText() != null && !updateDateTextField.getText().equals("")) {
			updatedOrder.put("date", updateDateTextField.getText());
		}
		if (updateSupplierTextField.getText() != null && !updateSupplierTextField.getText().equals("")) {
			updatedOrder.put("supplier", updateSupplierTextField.getText());
		}
		orderDAO.update(Integer.valueOf(updateIdTextField.getText()), updatedOrder);
		Pizzeria.setRoot("logistics");
	}
	
	@FXML
	public void exportCSV() throws IOException {
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
	}
	
	@FXML
	public void switchToPizzeria() throws IOException {
		Pizzeria.setRoot("pizzeria");
	}
}
