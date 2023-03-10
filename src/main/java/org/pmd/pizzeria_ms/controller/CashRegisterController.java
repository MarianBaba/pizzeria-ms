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

import org.pmd.pizzeria_ms.dao.TransactionDAO;
import org.pmd.pizzeria_ms.model.Transaction;
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
	private TableColumn<Transaction, Integer> idColumn;
	
	@FXML
	private TableColumn<Transaction, Type> typeColumn;
	
	@FXML
	private TableColumn<Transaction, Double> amountColumn;
	
	@FXML
	private TableColumn<Transaction, String> descriptionColumn;
	
	@FXML
	private TableColumn<Transaction, LocalDate> dateColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		TableColumn<Transaction, Void> deleteButtonColumn = new TableColumn<>("");
		Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>> deleteButtonCellFactory = new Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>>() {
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
        
        
        List<Transaction> transactions = transactionDAO.getAll();
        ObservableList<Transaction> transactionsList = FXCollections.observableArrayList(transactions);
        transactionsTable.getColumns().add(deleteButtonColumn);
        transactionsTable.setItems(transactionsList);
        transactionsTable.setMinWidth(Region.USE_PREF_SIZE);
        transactionsTable.setMinHeight(Region.USE_PREF_SIZE);
	}
	
	@FXML
	private TextField insertTypeTextField;
	
	@FXML
	private TextField insertAmountTextField;
	
	@FXML
	private TextField insertDescriptionTextField;
	
	@FXML
	private TextField insertDateTextField;
	
	@FXML
	private void save() throws IOException {
		String type = insertTypeTextField.getText();
		String amount = insertAmountTextField.getText();
		String description = insertDescriptionTextField.getText();
		String date = insertDateTextField.getText();
		transactionDAO.save(type, amount, description, date);
		Pizzeria.setRoot("cashRegister");
	}
	
	@FXML
	private TextField updateIdTextField;
	
	@FXML
	private TextField updateTypeTextField;
	
	@FXML
	private TextField updateAmountTextField;
	
	@FXML
	private TextField updateDescriptionTextField;
	
	@FXML
	private TextField updateDateTextField;
	
	@FXML
	public void update() throws IOException {
		Map<String, String> updatedTransaction = new HashMap<>();
		
		if (updateTypeTextField.getText() != null && !updateTypeTextField.getText().equals("")) {
			updatedTransaction.put("type", updateTypeTextField.getText());
		}
		
		if (updateAmountTextField.getText() != null && !updateAmountTextField.getText().equals("")) {
			updatedTransaction.put("amount", updateAmountTextField.getText());
		}
		
		if (updateDescriptionTextField.getText() != null && !updateDescriptionTextField.getText().equals("")) {
			updatedTransaction.put("description", updateDescriptionTextField.getText());
		}
		
		if (updateDateTextField.getText() != null && !updateDateTextField.getText().equals("")) {
			updatedTransaction.put("date", updateDateTextField.getText());
		}
		
		transactionDAO.update(Integer.valueOf(updateIdTextField.getText()), updatedTransaction);
		Pizzeria.setRoot("cashRegister");
	}
	
	@FXML
	public void exportCSV() throws IOException {
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
	}
	
	
	@FXML
	public void switchToPizzeria() throws IOException {
		Pizzeria.setRoot("pizzeria");
	}
}
