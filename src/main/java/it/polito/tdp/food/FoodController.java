/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	try { int porzioni= Integer.parseInt(txtPorzioni.getText());
			if (porzioni==0)
			{
				txtResult.setText("Hai inserito 0");
			}
			else {
				boxFood.getItems().clear();
				model.creaGrafo(porzioni);
				txtResult.setText(String.format("Grafo creato con %d vertici e %d archi", model.getNumVertici(), model.getNumArchi()));
				List<Food> listaCibi= new LinkedList<Food>(model.getMappaCibi().values());
				System.out.println(listaCibi.size());
				listaCibi.sort(new Comparator<Food>() {

					@Override
					public int compare(Food o1, Food o2) {
						// TODO Auto-generated method stub
						return o1.getDisplay_name().compareTo(o2.getDisplay_name());
					}
				});
				boxFood.getItems().addAll(listaCibi);
				boxFood.setValue(listaCibi.get(0));
				boxFood.setDisable(false);
				btnCalorie.setDisable(false);
				btnSimula.setDisable(false);
			}
		}
    	catch (NumberFormatException e) {
			txtResult.setText("Non ha inserito un numero intero");
		}
    	catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	Food cibo = boxFood.getValue();
    	txtResult.appendText(String.format("Per il cibo %s i 5 ingredienti piu'calorici sono: \n", cibo.getDisplay_name()));
    	if (model.best(cibo)==null)
    	{
    		txtResult.appendText("Non hai almeno 5 ingredienti per questo cibo");
    		return;
    	}
    	for (Food f: model.best(cibo))
    	{
    		txtResult.appendText(f.getDisplay_name()+"\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	try { 
    		int k= Integer.parseInt(txtK.getText());
    		txtResult.setText(model.simula(k, boxFood.getValue()));
			
		} catch (NumberFormatException e) {
			txtResult.setText("Non hai inserito un numero");
		}
    	catch (Exception e) {
			txtResult.setText("ERRORE!!!");
		}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
        boxFood.setDisable(true);
        btnCalorie.setDisable(true);
        btnSimula.setDisable(true);
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
