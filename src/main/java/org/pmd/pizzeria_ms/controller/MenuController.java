package org.pmd.pizzeria_ms.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.pmd.pizzeria_ms.dao.PizzaDAO;
import org.pmd.pizzeria_ms.dao.ProductDAO;
import org.pmd.pizzeria_ms.model.Pizza;
import org.pmd.pizzeria_ms.model.Product;
import org.pmd.pizzeria_ms.utility.CSVManager;
import org.pmd.pizzeria_ms.view.Pizzeria;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.util.Callback;

public class MenuController implements Initializable {
	
	private static PizzaDAO pizzaDAO = new PizzaDAO();
	
	@FXML
	private TableView<Pizza> pizzasTable;
	
	@FXML
	private TableColumn<Pizza, Integer> idColumn;
	
	@FXML
	private TableColumn<Pizza, String> nameColumn;
	
	@FXML
	private TableColumn<Pizza, Double> priceColumn;

	@FXML
	private TableColumn<Pizza, String> descriptionColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		TableColumn<Pizza, Void> deleteButtonColumn = new TableColumn<>("");
		Callback<TableColumn<Pizza, Void>, TableCell<Pizza, Void>> deleteButtonCellFactory = new Callback<TableColumn<Pizza, Void>, TableCell<Pizza, Void>>() {
            @Override
            public TableCell<Pizza, Void> call(final TableColumn<Pizza, Void> param) {
                final TableCell<Pizza, Void> cell = new TableCell<Pizza, Void>() {

                    private final Button btn = new Button("Elimina");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                        	
                        	Integer pizzaToDelete = getTableView().getItems().get(getIndex()).getId();
                        	pizzaDAO.delete(pizzaToDelete);
                        	
                        	try {
								Pizzeria.setRoot("menu");
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
        final Callback<TableColumn<Pizza,String>, TableCell<Pizza, String>> WRAPPING_CELL_FACTORY = 
                new Callback<TableColumn<Pizza,String>, TableCell<Pizza, String>>() {
                    
            @Override public TableCell<Pizza,String> call(TableColumn<Pizza,String> param) {
                TableCell<Pizza, String> tableCell = new TableCell<Pizza,String>() {
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
        List<Pizza> pizzas = pizzaDAO.getAll();
        ObservableList<Pizza> pizzasList = FXCollections.observableArrayList(pizzas);
        pizzasTable.getColumns().add(deleteButtonColumn);
        pizzasTable.setItems(pizzasList);
        pizzasTable.setMinWidth(Region.USE_PREF_SIZE);
        pizzasTable.setMinHeight(Region.USE_PREF_SIZE);
	}
	
	// aggiungi pizza
	@FXML
	private TextField insertNameTextField;
	
	@FXML
	private TextField insertIngredientsTextField;
	
	@FXML
	public void save() throws IOException {
		String name = insertNameTextField.getText();
		List<Integer> ingredientsId = new ArrayList<>();
		String[] ingredients = insertIngredientsTextField.getText().strip().split(",");
		for (String id : ingredients) {
			ingredientsId.add(Integer.valueOf(id));
		}
		StringBuilder description = new StringBuilder();
		List<Double> prices = new ArrayList<>();
		description.append("[");
		
		ProductDAO productDAO = new ProductDAO();
		
		for (Integer id : ingredientsId) {
			Product prod = productDAO.get(id).get();
			description.append(prod.getName() + ",");
			prices.add(prod.getPrice());
		}
		
		description.deleteCharAt(description.length() - 1);
		description.append("]");
		System.out.println(description);
		
		String price = Pizza.getPriceByProducts(prices).toString();
		
		pizzaDAO.save(name, price, description.toString());
		Pizzeria.setRoot("menu");
	}
	
	@FXML
	private TextField updateIdTextField;
	
	@FXML
	private TextField updateNameTextField;
	
	@FXML
	private TextField updateIngredientsTextField;
	
	@FXML
	private void update() throws IOException {
		Map<String, String> updatedPizza= new HashMap<>();
		if (updateNameTextField.getText() != null && !updateNameTextField.getText().equals("")) {
			updatedPizza.put("name", updateNameTextField.getText());
		}
		if (updateIngredientsTextField.getText() != null && !updateIngredientsTextField.getText().equals("")) {
			List<Integer> ingredientsId = new ArrayList<>();
			String[] ingredients = updateIngredientsTextField.getText().strip().split(",");
			for (String id : ingredients) {
				ingredientsId.add(Integer.valueOf(id));
			}
			StringBuilder description = new StringBuilder();
			List<Double> prices = new ArrayList<>();
			description.append("[");
			ProductDAO productDAO = new ProductDAO();
			for (Integer id : ingredientsId) {
				Product prod = productDAO.get(id).get();
				description.append(prod.getName() + ",");
				prices.add(prod.getPrice());
			}
			
			description.deleteCharAt(description.length() - 1);
			description.append("]");
			String price = Pizza.getPriceByProducts(prices).toString();
			
			updatedPizza.put("description", description.toString());
			updatedPizza.put("price", price);
		}
		pizzaDAO.update(Integer.valueOf(updateIdTextField.getText()), updatedPizza);
		Pizzeria.setRoot("menu");
	}
	
	@FXML
	public void exportCSV() throws IOException {
		CSVManager.exportCSV("pizzas");
	}
	
	@FXML
	public void switchToPizzeria() throws IOException {
		Pizzeria.setRoot("pizzeria");
	}
}
