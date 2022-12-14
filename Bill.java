/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guimaker;

/**
 *
 * @author Sebastian Gonzalez
 */
public class Bill {
    private double value;
    private int quantity;
    private javax.swing.JLabel label;
    private javax.swing.JSpinner spinner;
    public Bill(double x){
        this.value=x;
        this.label = new javax.swing.JLabel(String.format("$%.2f", value));
        this.spinner = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(0, 0, 2047, 1));
    }
    
    public double getValue(){
        return this.value;
    }
    public int getQuantity(){
        return this.quantity;
    }
    
    public void setQuantity(int x){
        this.quantity=x;
    }
    public javax.swing.JLabel getLabel(){
        return this.label;
    }
    
    public javax.swing.JSpinner getSpinner(){
        return this.spinner;
    }
    
    public int getSpinnerValue(){
        return (Integer) this.spinner.getValue();
    }
    
    @Override
    public String toString(){
        return this.getValue()>1 ? 
        String.format("$%.0f",this.getValue()): 
        String.format("$%.2f",this.getValue());
    }
}
