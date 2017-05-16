package com.murano500k.crackvigenere;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if(args==null || args.length==0){
            System.err.println("No args");
            return;
        }
        String input = readFile(args[0]);
        Result result =DeVigenere.devignere(input,12,'a','z');
        System.out.println("\tResult:\n\tkey:\n"+result.key+"\n\ttext:\n"+result.text);
    }

    public static String readFile(String fileName){
        File file = new File(fileName);
        if(!file.exists())  {
            System.err.println("Error opening file "+file);
            return null;
        }
        StringBuilder sb=new StringBuilder();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready())sb.append(br.readLine());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

}
