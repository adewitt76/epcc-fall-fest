package edu.epcc.epccfallfest;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

import edu.epcc.epccfallfestapp.backend.registrationApi.RegistrationApi;
import edu.epcc.epccfallfestapp.backend.registrationApi.model.RegistrationBean;

public class CodeGenerator {

    private static final int VALID = 6401;
    private static final int USED = 6402;
    private static final int INVALID = 6403;

    public static void main(String[] args) {

        boolean exit = false;

        System.out.println("Welcome to the Monster Hunt code generator");

        while(!exit) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("    1) Generate new codes");
            System.out.println("    2) Check status of a code");
            System.out.println("    3) Clear existing codes");
            System.out.println("    4) Exit");
            System.out.print("Enter 1-4 >> ");

            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();

            switch(choice) {
                case 1:
                    generateCodes();
                    break;
                case 2:
                    checkStatus();
                    break;
                case 3:
                    clearCodes();
                    break;
                case 4:
                    exit = true;
                    break;
            }

            input.nextLine();
        }
    }

    private static void generateCodes() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nThis action will generate a new code list");
        System.out.print("Continue? (Y = yes) >> ");
        String inStr = input.nextLine();
        if(inStr.equals("Y")) {
            System.out.print("Enter event date: (mm dd yyyy) >> ");
            int month = input.nextInt(); int day = input.nextInt(); int year = input.nextInt();
            input.nextLine();
            if((0 < month && month < 13) && (0 < day && day < 32) && (2014 < year && year < 2115)){
                System.out.print("How many tickets: (1 - 5000) >> ");
                int numb = input.nextInt(); input.nextLine();
                if (0 < numb && numb < 5001) {
                    LinkedList<String> tickets = generateCodeList(numb);
                    printListToFile(tickets);
                    try {
                        RegistrationApi.Builder builder =
                                new RegistrationApi.Builder(new NetHttpTransport(), new GsonFactory(), null);
                        builder.setApplicationName("CodeGenerator");
                        RegistrationApi server = builder.build();
                        for (String ticket: tickets) server.addCode(ticket,month,day,year).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Invalid number");
                }
            } else {
                System.out.println("Date invalid");
            }
        }
    }

    private static void checkStatus() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nWARNING: This action will make the ticket invalid");
        System.out.print("Continue? (Y = yes) >> ");
        String inStr = input.nextLine();
        if(inStr.equals("Y")) {
            System.out.println("Enter the ticket number to check.");
            System.out.print(">> ");
            inStr = input.nextLine();
            RegistrationBean regBean = null;
            try {
                RegistrationApi.Builder builder = new RegistrationApi.Builder(new NetHttpTransport(), new GsonFactory(), null);
                builder.setApplicationName("CodeGenerator");
                RegistrationApi server = builder.build();
                regBean = server.getStatus(inStr).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("The registration code is " + statusToString(regBean.getRegistrationCodeStatus()));
        }
    }

    private static void clearCodes() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nWARNING: This action will clear all codes from the server.");
        System.out.print("Continue? (Y = yes) >> ");
        String inStr = input.nextLine();
        if(inStr.equals("Y")) {
            try {
                RegistrationApi.Builder builder = new RegistrationApi.Builder(new NetHttpTransport(), new GsonFactory(), null);
                builder.setApplicationName("CodeGenerator");
                RegistrationApi server = builder.build();
                server.clearCodeList().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String statusToString(int status) {
        String rString = "ERROR";
        switch (status)  {
            case VALID:
                rString = "valid";
                break;
            case INVALID:
                rString = "invalid";
                break;
            case USED:
                rString = "used";
                break;
        }
        return rString;
    }

    private static LinkedList<String> generateCodeList(int numb) {
        LinkedList<String> list = new LinkedList<>();
        for (int j = 0; j < numb; j++) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int rand = (int) (Math.random() * 50);
                builder.append(charConverter(rand));
            }
            if(!list.contains(builder.toString())) {
                list.add(builder.toString());
            }
        }
        return list;
    }

    private static char charConverter(int c) {
        char rc;
        if ( 1 <= c && c <= 9){
            rc = Integer.toString(c).charAt(0);
        } else if (10 <= c && c <= 25) {
            c = c + 55;
            rc = (char) c;
        } else {
            c = c + 71;
            rc = (char) c;
        }
        return rc;
    }

    private static void printListToFile(LinkedList<String> list) {
        File file = new File("TicketList.txt");
        try {
            PrintWriter pw = new PrintWriter(file);
            for(String ticket: list) pw.print(ticket + " ");
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");;
        }
    }
}
