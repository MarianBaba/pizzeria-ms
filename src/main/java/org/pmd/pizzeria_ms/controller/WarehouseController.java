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
	private TableColumn<Product, Integer> id;
	
	@FXML
	private TableColumn<Product, String> name;

	@FXML
	private TableColumn<Product, Double> price;

	@FXML
	private TableColumn<Product, Integer> quantity;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		TableColumn<Product, Void> deleteButtonColumn = new TableColumn<>("azioni");
		Callback<TableColumn<Product, Void>, TableCell<Product, Void>> deleteButtonCellFactory = 
				new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
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
        productsTable.getColumns().add(deleteButtonColumn);
		
        List<Product> products = productDAO.getAll();
        ObservableList<Product> productsList = FXCollections.observableArrayList(products);
        
        productsTable.setItems(productsList);
        productsTable.setMinWidth(Region.USE_PREF_SIZE);
        productsTable.setMinHeight(Region.USE_PREF_SIZE);
	}
	
	// insert fields + save function
	
	@FXML
	private TextField insertName;
	
	@FXML
	private TextField insertPrice;
	
	@FXML
	private TextField insertQuantity;
	
	@FXML
	public void save() throws IOException {
		productDAO.save(insertName.getText(), insertPrice.getText(), insertQuantity.getText());
		Pizzeria.setRoot("warehouse");
	}
	
	// update fields + update function
	
	@FXML
	private TextField updateId;
	
	@FXML
	private TextField updateName;
	
	@FXML
	private TextField updatePrice;
	
	@FXML
	private TextField updateQuantity;
	
	@FXML
	public void update() throws IOException {
		Map<String, String> updatedProduct = new HashMap<>();
		if (updateName.getText() != null && !updateName.getText().equals("")) {
			updatedProduct.put("name", updateName.getText());
		}
		
		if (updatePrice.getText() != null && !updatePrice.getText().equals("")) {
			updatedProduct.put("price", updatePrice.getText());
		}
		
		if (updateQuantity.getText() != null && !updateQuantity.getText().equals("")) {
			updatedProduct.put("quantity", updateQuantity.getText());
		}
		
		productDAO.update(Integer.valueOf(updateId.getText()), updatedProduct);
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
