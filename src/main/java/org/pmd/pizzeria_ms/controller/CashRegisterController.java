package org.pmd.pizzeria_ms.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.pmd.pizzeria_ms.dao.TransactionDAO;
import org.pmd.pizzeria_ms.model.Transaction;
import org.pmd.pizzeria_ms.utility.CSVManager;
import org.pmd.pizzeria_ms.enumeration.Type;
import org.pmd.pizzeria_ms.view.Pizzeria;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.util.Callback;

public class CashRegisterController implements Initializable {
	
	private static TransactionDAO transactionDAO = new TransactionDAO();
	
	@FXML
	private TableView<Transaction> transactionsTable;
	
	@FXML
	private TableColumn<Transaction, Integer> id;
	
	@FXML
	private TableColumn<Transaction, Type> type;
	
	@FXML
	private TableColumn<Transaction, Double> amount;
	
	@FXML
	private TableColumn<Transaction, String> description;
	
	@FXML
	private TableColumn<Transaction, LocalDate> date;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		type.setCellValueFactory(new PropertyValueFactory<>("type"));
		amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		description.setCellValueFactory(new PropertyValueFactory<>("description"));
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		TableColumn<Transaction, Void> deleteButtonColumn = new TableColumn<>("azioni");
		Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>> deleteButtonCellFactory = 
				new Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>>() {
            		@Override
            		public TableCell<Transaction, Void> call(final TableColumn<Transaction, Void> param) {
            			final TableCell<Transaction, Void> cell = new TableCell<Transaction, Void>() {
            				private final Button btn = new Button("Elimina");
            				
            				{
            					btn.setOnAction((ActionEvent event) -> {
            						Integer transactionToDelete = getTableView().getItems().get(getIndex()).getId();
            						transactionDAO.delete(transactionToDelete);
            						try {
            							Pizzeria.setRoot("cashRegister");
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
        
        final Callback<TableColumn<Transaction,String>, TableCell<Transaction, String>> WRAPPING_CELL_FACTORY = 
            new Callback<TableColumn<Transaction,String>, TableCell<Transaction, String>>() {
                    
            	@Override public TableCell<Transaction,String> call(TableColumn<Transaction,String> param) {
            		TableCell<Transaction, String> tableCell = new TableCell<Transaction,String>() {
            			
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
        List<Transaction> transactions = transactionDAO.getAll();
        ObservableList<Transaction> transactionsList = FXCollections.observableArrayList(transactions);
        transactionsTable.getColumns().add(deleteButtonColumn);
        transactionsTable.setItems(transactionsList);
        transactionsTable.setMinWidth(Region.USE_PREF_SIZE);
        transactionsTable.setMinHeight(Region.USE_PREF_SIZE);
	}
	
	// save fields + save function
	
	@FXML
	private TextField insertType;
	
	@FXML
	private TextField insertAmount;
	
	@FXML
	private TextField insertDescription;
	
	@FXML
	private TextField insertDate;
	
	@FXML
	private void save() throws IOException {
		transactionDAO.save(insertType.getText(), insertAmount.getText(), insertDescription.getText(), insertDate.getText());
		Pizzeria.setRoot("cashRegister");
	}
	
	// update fields + update function
	
	@FXML
	private TextField updateId;
	
	@FXML
	private TextField updateType;
	
	@FXML
	private TextField updateAmount;
	
	@FXML
	private TextField updateDescription;
	
	@FXML
	private TextField updateDate;
	
	
	@FXML
	public void update() throws IOException {
		Map<String, String> updatedTransaction = new HashMap<>();
		if (updateType.getText() != null && !updateType.getText().equals("")) {
			updatedTransaction.put("type", updateType.getText());
		}
		
		if (updateAmount.getText() != null && !updateAmount.getText().equals("")) {
			updatedTransaction.put("amount", updateAmount.getText());
		}
		
		if (updateDescription.getText() != null && !updateDescription.getText().equals("")) {
			updatedTransaction.put("description", updateDescription.getText());
		}
		
		if (updateDate.getText() != null && !updateDate.getText().equals("")) {
			updatedTransaction.put("date", updateDate.getText());
		}
		
		transactionDAO.update(Integer.valueOf(updateId.getText()), updatedTransaction);
		Pizzeria.setRoot("cashRegister");
	}
	
	@FXML
	public void exportCSV() throws IOException {
		CSVManager.exportCSV("transactions");
	}
	
	@FXML
	public void switchToPizzeria() throws IOException {
		Pizzeria.setRoot("pizzeria");
	}
}
