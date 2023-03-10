package org.pmd.pizzeria_ms.controller;

import java.io.IOException;

import org.pmd.pizzeria_ms.view.Pizzeria;

import javafx.fxml.FXML;

public class PizzeriaController {

	@FXML
	public void switchToWarehouse() throws IOException {
		Pizzeria.setRoot("warehouse");
	}
	
	@FXML
	public void switchToCashRegister() throws IOException {
		Pizzeria.setRoot("cashRegister");
	}
	
	@FXML
	public void switchToLogistics() throws IOException {
		Pizzeria.setRoot("logistics");
	}
	
	@FXML
	public void switchToMenu() throws IOException {
		Pizzeria.setRoot("menu");
	}
	
	
}
