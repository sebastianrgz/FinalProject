/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package guimaker;
import java.awt.event.*;
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JPanel;
import java.util.HashMap;
import java.util.ArrayList;
/**
 *
 * @author Sebastian Gonzalez
 */
class CustomFrame implements ActionListener {
    //storeFrame GUI elements
    private JFrame storeFrame = new JFrame();
    private JPanel storePanel = new JPanel(); 
    private JLabel storeTitle;
    private JButton storeCheckOut = new JButton("Checkout");
    private JButton reportButton = new JButton("Generate report");
    private HashMap<Item,Integer> order = new HashMap<>(); 
    //Contains all items selected and how many of them for the current order
    private HashMap<JButton,Item> buttons = new HashMap<>();
    //Contains all the buttons that correspond to each Item
    private ArrayList<HashMap<Item,Integer>> orders = new ArrayList<>();
    //contains all orders 
    private int height = 300;
    //checkOutFrame GUI elements
    private JFrame checkOutFrame = new JFrame();
    private JPanel checkOutPanel = new JPanel();
    private JLabel orderNumberLabel = new JLabel();
    private JLabel changeToGiveLabel = new JLabel();
    private JButton calculateChangeButton = new JButton("Calculate change");
    private JButton finalizeButton = new JButton("Finalize");
    private javax.swing.JList<String> checkOutList = new javax.swing.JList<>();
    private javax.swing.DefaultListModel<String> checkOutListModel = new javax.swing.DefaultListModel<>();
    private ArrayList<Bill> register = new ArrayList<>();
    //contains all the bills and their corresponding quantities in the register
    private double orderTotal;
    private double total;
    
   
    CustomFrame(HashMap<JButton,Item> buttons,String storeName,ArrayList<Bill> change){
        this.storeTitle = new JLabel(storeName);
        this.buttons.putAll(buttons);
        this.register.addAll(change);
        buttonProperties();
        prepareStoreGUI();
    }
    private void prepareStoreGUI(){ //generates storeFrame GUI
        storePanel.setLayout(null);
        storePanel.add(storeCheckOut);
        storeCheckOut.setBounds(200,height-100,100,30);
        storeCheckOut.addActionListener(this);
        storePanel.add(reportButton);
        reportButton.setBounds(360,height-100,100,30);
        reportButton.addActionListener(this);
        storeFrame.setTitle("My Window");
        storePanel.add(storeTitle);
        storeTitle.setBounds(200,20,200,30);
        storeFrame.add(storePanel);
        storeFrame.setSize(500,height);  
        storeFrame.setLocationRelativeTo(null);  
        storeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        storeFrame.setVisible(true);
    }
    private void prepareCheckOutGUI(){//generates checkOutFrame GUI
        orderTotal=0;
        int quantity;
        checkOutPanel.setLayout(null);
        orderNumberLabel.setText("Order#"+orders.size());
        checkOutPanel.add(orderNumberLabel);
        orderNumberLabel.setBounds(200,20,200,30);
        for(Item i: orders.get(orders.size()-1).keySet()){ //adds selected items to JList to show to user
            quantity = orders.get(orders.size()-1).get(i);
            checkOutListModel.addElement(quantity+"x "+i.name+"...........total: $"+(quantity*i.price));
            orderTotal+=quantity*i.price;
        }
        checkOutListModel.addElement("\n");
        checkOutListModel.addElement(String.format("Subtotal:    $%.2f",orderTotal));
        checkOutListModel.addElement(String.format("Total:    $%.2f",orderTotal*1.07));
        checkOutList.setModel(checkOutListModel);
        checkOutPanel.add(checkOutList);
        checkOutList.setBounds(50, 60, 300, height-200);
        int temp=0,x,y=0,z=0;
        for(Bill i:register){ // sets up each Item label and spinner in the store frame
            if(i.getValue()<=1){temp=z; x=250;z+=26;}else {temp=y; x=0;y+=26;}
            checkOutPanel.add(i.getLabel());
            i.getLabel().setBounds(50+x,height-100+temp,50,20);
            checkOutPanel.add(i.getSpinner());
            i.getSpinner().setBounds(105+x,height-100+temp,50,20);
        }
        checkOutPanel.add(calculateChangeButton);
        calculateChangeButton.setBounds(50,height-60+temp,150,30);
        calculateChangeButton.addActionListener(this);
        checkOutPanel.add(finalizeButton);
        finalizeButton.setBounds(300,height+20+temp,150,50);
        finalizeButton.addActionListener(this);
        checkOutFrame.add(checkOutPanel);
        checkOutFrame.setSize(500,height+300);  
        checkOutFrame.setLocationRelativeTo(null);  
        checkOutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        checkOutFrame.setVisible(true);
    }
    private void calculateChange(double total){ 
        /*Calculates how many of each bill 
        should be given back to customer*/
        double cashGiven = 0.0, change=0;
        int billsToGive[] = new int[10];
        String cashToGive = "Change:";
        java.util.Arrays.fill(billsToGive, 0);
        for(Bill i : register){
            cashGiven+=i.getValue()*i.getSpinnerValue();
            i.setQuantity(i.getQuantity()+i.getSpinnerValue());
        }
        change += cashGiven-total*1.07;
        if(change<0){ //make sure that the cash entered is more than or equal to the total
            changeToGiveLabel.setText("Insufficient funds");
            checkOutPanel.add(changeToGiveLabel);
            changeToGiveLabel.setBounds(50,calculateChangeButton.getY()+36,400,20);
            changeToGiveLabel.setVisible(false);
            changeToGiveLabel.setVisible(true);
            return;
        }
        changeLoop :while(change>=0.01){//calcultes how many of each bill to be given back as change
            
            for(Bill i :register){
                if(change>=i.getValue()&&i.getQuantity()>0){
                   billsToGive[register.indexOf(i)]++;
                   change-=i.getValue();
                   i.setQuantity(i.getQuantity()-1);
                   break;
                }else if(change>=i.getValue()&&i.getQuantity()==0&&i.getValue()==0.01){
                    break changeLoop;
                }
            }
        }
        if(change<0.01 && change>=0.009){//takes into account weird double math
            billsToGive[9]+=1;
        }
        for(int i=0;i<10;i++){//
            cashToGive+=billsToGive[i]>0 ? " "+billsToGive[i]+"x "+register.get(i):"";
        }
        changeToGiveLabel.setText(cashToGive);
        checkOutPanel.add(changeToGiveLabel);
        changeToGiveLabel.setBounds(50,calculateChangeButton.getY()+36,400,20);
        changeToGiveLabel.setVisible(false);
        changeToGiveLabel.setVisible(true);
    }
    
    private void writeReport(){
        //writes report of all items sold
        try{
                java.io.FileWriter fileWriter = new java.io.FileWriter("report.txt");
                int totalItems=0,itemsSold;
                fileWriter.write(storeTitle.getText()+"\n");
                fileWriter.write(String.format("Orders: %d\n",orders.size() ));
                for(Item i:buttons.values()){
                    itemsSold=0;
                    for(HashMap<Item,Integer> j: orders){
                        itemsSold+=j.get(i)!=null ? j.get(i):0;
                    }
                    totalItems+=itemsSold;
                    fileWriter.write(itemsSold+"x "+i.name+"\n");
                }
                fileWriter.write(String.format("Total items sold: %d\n", totalItems));
                fileWriter.write(String.format("Total: $%.2f\n", total ));
                fileWriter.write(String.format("Taxes: $%.2f\n", total * 0.07));

                fileWriter.close();

            }catch(java.io.IOException d){
                    d.printStackTrace();
            }
            
        
    }
    private void buttonProperties(){
        //sets up buttons for store frame
        for( JButton x:buttons.keySet()){
            height+=26;
            storePanel.add(buttons.get(x).label);
            buttons.get(x).label.setBounds(50,height-250,200,20);
            storePanel.add(x);
            x.setBounds(322,height-250,60,20);
            x.addActionListener(this);
            storePanel.add(buttons.get(x).spinner);
            buttons.get(x).spinner.setBounds(256,height-250,50,20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // determines the functionality of each button
        if(e.getActionCommand().equals("Checkout")){
            this.storeFrame.setVisible(false);
            orders.add(new HashMap<Item,Integer>(order));
            order.clear();
            prepareCheckOutGUI();
            return;
        }else if(e.getActionCommand().equals("Calculate change")){
            calculateChange( orderTotal);
            return;
        }else if((e.getActionCommand().equals("Finalize"))){
            try{
                java.io.FileWriter fileWriter = new java.io.FileWriter("order#"+orders.size()+".txt");
                int numItems=0;
                fileWriter.write(storeTitle.getText()+"\n");
                fileWriter.write(String.format("Order# %d\n",orders.size() ));
                for (Item i : orders.get(orders.size()-1).keySet()) {
                  fileWriter.write(orders.get(orders.size()-1).get(i)
                          +"x "+i.name+"...........total: $"+
                          (orders.get(orders.size()-1).get(i)*i.price)
                          +"\n");
                  numItems+=orders.get(orders.size()-1).get(i);
                }
                fileWriter.write(String.format("Number of items: %d\n", numItems));
                fileWriter.write(String.format("Subtotal: %.2f\n", orderTotal));
                fileWriter.write(String.format("Total: $%.2f\n", orderTotal * 1.07));

                fileWriter.close();

            }catch(java.io.IOException d){
                    d.printStackTrace();
            }
            checkOutListModel.clear();
            checkOutPanel.removeAll();
            checkOutFrame.setVisible(false);
            storeFrame.setVisible(true);
            total+=orderTotal;
            return;
        }else if(e.getActionCommand().equals("Generate report")){
            writeReport();
            System.exit(0);
            return;
            
        }
        Object b = e.getSource();
        Item i = buttons.get(b);
        int s = (Integer) i.spinner.getValue();
        if(!order.containsKey(i)){
        order.put(i,s);
        }else{
            order.replace(i, order.get(i)+s);
        }
    }
}

public class GUIMaker {  
    public static void main(String s[]) {  
        UserFrame userFrame = new UserFrame();
        userFrame.setVisible(true);
        long i=0;
        while(userFrame.isVisible()){
            System.out.print("");
        }
        CustomFrame customFrame = new CustomFrame(userFrame.buttons,userFrame.storeName,userFrame.change);
    }  
} 
