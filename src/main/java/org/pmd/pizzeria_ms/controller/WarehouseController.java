package org.pmd.pizzeria_ms.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.pmd.pizzeria_ms.dao.ProductDAO;
import org.pmd.pizzeria_ms.model.Product;
import org.pmd.pizzeria_ms.utility.CSVManager;
import org.pmd.pizzeria_ms.view.Pizzeria;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.fxml.Initializable;

public class WarehouseController implements Initializable {
	
	private static ProductDAO productDAO = new ProductDAO();
	
	@FXML
	private TableView<Product> productsTable;
	
	@FXML
	private TableColumn<Product, Integer> idColumn;
	
	@FXML
	private TableColumn<Product, String> nameColumn;

	@FXML
	private TableColumn<Product, Double> priceColumn;

	@FXML
	private TableColumn<Product, Integer> quantityColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		TableColumn<Product, Void> deleteButtonColumn = new TableColumn<>("");
		Callback<TableColumn<Product, Void>, TableCell<Product, Void>> deleteButtonCellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<Product, Void>() {

                    private final Button btn = new Button("Elimina");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                        	
                        	Integer productToDelete = getTableView().getItems().get(getIndex()).getId();
                        	productDAO.delete(productToDelete);
                        	
                        	try {
								Pizzeria.setRoot("warehouse");
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
		
        List<Product> products = productDAO.getAll();
        ObservableList<Product> productsList = FXCollections.observableArrayList(products);
        
        productsTable.getColumns().add(deleteButtonColumn);
        productsTable.setItems(productsList);
        productsTable.setMinWidth(Region.USE_PREF_SIZE);
        productsTable.setMinHeight(Region.USE_PREF_SIZE);
	}
	
	// inserisci prodotto
	
	@FXML
	private TextField insertNameTextField;
	
	@FXML
	private TextField insertPriceTextField;
	
	@FXML
	private TextField insertQuantityTextField;
	
	@FXML
	public void save() throws IOException {
		String name = insertNameTextField.getText();
		String price = insertPriceTextField.getText();
		String quantity = insertQuantityTextField.getText();
		
		productDAO.save(name, price, quantity);
		Pizzeria.setRoot("warehouse");
	}
	
	// update prodotto
	@FXML
	private TextField updateIdTextField;
	
	@FXML
	private TextField updateNameTextField;
	
	@FXML
	private TextField updatePriceTextField;
	
	@FXML
	private TextField updateQuantityTextField;
	
	@FXML
	public void update() throws IOException {
		Map<String, String> updatedProduct = new HashMap<>();
		
		if (updateNameTextField.getText() != null && !updateNameTextField.getText().equals("")) {
			updatedProduct.put("name", updateNameTextField.getText());
		}
		
		if (updatePriceTextField.getText() != null && !updatePriceTextField.getText().equals("")) {
			updatedProduct.put("price", updatePriceTextField.getText());
		}
		
		if (updateQuantityTextField.getText() != null && !updateQuantityTextField.getText().equals("")) {
			updatedProduct.put("quantity", updateQuantityTextField.getText());
		}
		
		productDAO.update(Integer.valueOf(updateIdTextField.getText()), updatedProduct);
		Pizzeria.setRoot("warehouse");
	}
	
	@FXML
	public void exportCSV() throws IOException {
		CSVManager.exportCSV("products");
	}
	
	@FXML
	public void switchToPizzeria() throws IOException {
		Pizzeria.setRoot("pizzeria");
	}
}
