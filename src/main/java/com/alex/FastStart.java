package com.alex;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import jline.console.ConsoleReader;

/**
 * Created by alexey.simakov on 21.06.2015.
 */
public class FastStart{

    private static boolean STOP_AFTER_RUNING = false;

    public static void main(String[] args) throws IOException{

        String path1 = System.getProperty("java.class.path");
        String FileSeparator = (String) System.getProperty("file.separator");
        String pathProgram = path1.substring(0, path1.lastIndexOf(FileSeparator) + 1);
        if (pathProgram.equals("")) pathProgram = System.getProperty("user.dir");
        System.out.println("\nProgram path: " + pathProgram);


        Scanner scanner = new Scanner(System.in);
        String fileName = "";

        if (args.length == 0) {
            do
            {
                System.out.println("\nNo arguments. Input filename or 'exit' for exit:");
                fileName = scanner.nextLine();
                if (fileName.equals("exit")) return;
                File fileSpisok = new File(fileName);
                if (!fileSpisok.exists()) {
                    System.out.println("\nFile is not found. Try again.\nYour file: " + fileSpisok.getAbsolutePath());
                } else break;
            }while (true);
        } else {
            fileName = args[0];
        }

        try
        {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNext()){
                String path = sc.nextLine();
//                path = path.replaceAll("\\\\", "/");
                //для версии наследования от Thread И start в конструкторе:
                //new OpenFileThread(path);
                //для версии с runnable и без start внутри
                try {
                    new Thread(new OpenFileThread(new File(path))).run();
                }catch (Exception e){
                    System.out.println("\nFile:\n" + path + "\n" + e.getMessage() + e);
                    STOP_AFTER_RUNING = true;
                }
            }
        }catch (Exception e){
            System.out.println("\nError program:\n" + e.getMessage() + e);
            STOP_AFTER_RUNING = true;
        }

        if (STOP_AFTER_RUNING) waitAnyKey();

    }

    public static void waitAnyKey() throws IOException{
        ConsoleReader con = new ConsoleReader();
        con.setPrompt("-->");
        System.out.println("Press any key to continue...");
        con.readCharacter();
        con.clearScreen();

    }

    public static class OpenFileThread extends Thread{
        private File file;

        OpenFileThread(File file){
            this.file = file;
            //this.start();
        }

        @Override
        public void run()
        {
            try
            {
                Desktop.getDesktop().open(file);
            }catch (FileNotFoundException e){
                System.out.println("\nFile not found:\n" + file + "\n" + e.getMessage() + e);
                STOP_AFTER_RUNING = true;
            }catch (IOException e){
                System.out.println("\nIOException:\n" + file + "\n" + e.getMessage() + e);
                STOP_AFTER_RUNING = true;
            }
        }
    }

    /*
    public static class OpenFileThread implements Runnable{
    private String fileName;

    OpenFileThread(String fileName){
        this.fileName = fileName;
    }
    @Override
    public void run()
    {
        try
        {
            File file=new File(fileName);
            Desktop.getDesktop().open(file);
        }
        catch (IOException e){
            System.out.println("Ошибка открытия файла");
        }
    }
    }
    */
}
