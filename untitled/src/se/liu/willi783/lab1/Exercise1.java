package se.liu.willi783.lab1;

import javax.swing.*;

public class Exercise1 {


    public static void printOutPut(){
        String input =
                JOptionPane.showInputDialog("Please input a value: ");

        boolean fungerar = false;

        while(!fungerar){
        try {
            int tabell = Integer.parseInt(input);

            System.out.println(tabell);
            fungerar = true;

        } catch (NumberFormatException error){
            System.out.println("Lägg in ett nummber mf");
        }
    }
    }


    public static void main(String[] args){

        printOutPut();
    }
}
