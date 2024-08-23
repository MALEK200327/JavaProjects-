package uniProjects;


import uniProjects.sheffield.EasyReader;
import uniProjects.sheffield.EasyWriter;

public class Assignment1  {

    public static void main(String [] args) {

        final int PENCE_IN_A_SHILLING = 12;
        final int NEW_PENCE_TO_OLD = 20*12;

        //Task 1
        //Read in the amount from the keyboard
        EasyReader keyboard = new EasyReader();
        double amountInNewMoney =
              keyboard.readDouble("What amount do you want to convert? ");

        //Calculate the pounds and old pence
        int pounds = (int)amountInNewMoney;
        int oldPence = (int)Math.round(
            (amountInNewMoney-pounds)*NEW_PENCE_TO_OLD);

        //Display the result
        EasyWriter screen = new EasyWriter();
        screen.println("That is L"+pounds+"."+
              oldPence/PENCE_IN_A_SHILLING+"s."+
              oldPence%PENCE_IN_A_SHILLING+"d");

        //Task 2
        //Display the result again as a table
        final int NEW_MONEY_WIDTH=6, L_WIDTH=7, S_WIDTH=5, D_WIDTH=5,
           DECIMAL_PLACES=2;
        screen.println();
        screen.println("   New      L    s    d");
        screen.print(amountInNewMoney, DECIMAL_PLACES, NEW_MONEY_WIDTH);
        screen.print(pounds, L_WIDTH);
        screen.print(oldPence/PENCE_IN_A_SHILLING, S_WIDTH);
        screen.println(oldPence%PENCE_IN_A_SHILLING, D_WIDTH);

        //Task3
        //Read a new amount from the file money.txt
        EasyReader file = new EasyReader("money.txt");
        amountInNewMoney = file.readDouble();

        //Calculate the pounds and pence
        pounds = (int)amountInNewMoney;
        oldPence = (int)Math.round(
            (amountInNewMoney-pounds)*NEW_PENCE_TO_OLD);

        //Add the new data to the table
        screen.print(amountInNewMoney, DECIMAL_PLACES, NEW_MONEY_WIDTH);
        screen.print(pounds, L_WIDTH);
        screen.print(oldPence/PENCE_IN_A_SHILLING, S_WIDTH);
        screen.println(oldPence%PENCE_IN_A_SHILLING, D_WIDTH);

        //Task4
        //Read the second line from the file
        String pleaseConvert = file.readString().substring(15);
        amountInNewMoney = Double.valueOf(
            pleaseConvert.substring(0, pleaseConvert.length()-13));

        //Calculate the pounds and pence
        pounds = (int)amountInNewMoney;
        oldPence = (int)Math.round(
            (amountInNewMoney-pounds)*NEW_PENCE_TO_OLD);

        //Add the new data to the table
        screen.print(amountInNewMoney, DECIMAL_PLACES, NEW_MONEY_WIDTH);
        screen.print(pounds, L_WIDTH);
        screen.print(oldPence/PENCE_IN_A_SHILLING, S_WIDTH);
        screen.println(oldPence%PENCE_IN_A_SHILLING, D_WIDTH);

    }
}