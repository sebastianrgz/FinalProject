/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guimaker;

/**
 *
 * @author Sebastian Gonzalez
 */
public class Item {
    double price;
    String name;
    javax.swing.JLabel label;
    javax.swing.JButton button;
    javax.swing.JSpinner spinner;
    
    public Item(String x, double y ){
            this.name = x;
            this.price = y;
            this.label = new javax.swing.JLabel(String.format(this.toString()));
            this.button = new javax.swing.JButton("Add");
            this.spinner = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(1, 1, 99, 1));

    }
    
    @Override
    public String toString(){
     return String.format("%s ...$%.2f",this.name,this.price);
    }
        
}
