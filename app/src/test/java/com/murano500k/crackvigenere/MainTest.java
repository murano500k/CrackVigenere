package com.murano500k.crackvigenere;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

import static com.murano500k.crackvigenere.DeVigenere.MAX_KEY_LENGTH;
import static com.murano500k.crackvigenere.DeVigenere.alphabet;

public class MainTest {
    private static final String ENCRYPTED = "testencrypted.txt";
    private static final String INPUT_FILE0 = "testinput0.txt";
    private static final String INPUT_FILE1 = "testinput1.txt";
    private static final String INPUT_FILE2 = "testinput2.txt";
    private static final String INPUT_FILE3 = "testinput3.txt";
    private static final String TEST_KEY = "testpassword";
    private static final int TEST_ITERATIONS = 10;

    @Test
    public void testEncrypt()throws Exception{

        System.out.println("\n\n###################");
        System.out.println("test encrypt input");
        DeVigenere vigenere =new DeVigenere(TEST_KEY);
        String input=getInput(-1);
        System.out.println("input: "+input);
        System.out.println("output: ");
        System.out.println("key = "+vigenere.key);
        System.out.println("encrypted: "+vigenere.encrypt(input));
    }

    @Test
    public void testCrack()throws Exception{

        System.out.println("\n\n###################");
        System.out.println("test crack encrypted text");
        String input = Main.readFile(ENCRYPTED);

        Result result =DeVigenere.devignere(input,12,'a','z');
        System.out.println("Result:\n\tkey: "+result.key+"\n\ttext: "+result.text);
        Assert.assertTrue(TEST_KEY.contains(result.key));
    }

    @Test
    //Find minimum length of input text encrypted with 2-12 char key
    //  which can be successfully decrypted
    public void testMinInputLengthRandomKeySize() throws Exception {

        System.out.println("\n\n###################");
        System.out.println("test shortest successfully cracked text");
        System.out.println("key length - random string 2 - 12 chars length");
        System.out.println("best result of 100 successful iterations will be shown");
        String inputUntrimmed;
        try {
            inputUntrimmed = cleanInput(getInput(-1));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        int minSuccessfullLength=Integer.MAX_VALUE;
        int successCount = 0;
        for(int i=MAX_KEY_LENGTH ; successCount<TEST_ITERATIONS ; i++){
            String input = inputUntrimmed.substring(0,i);
            //int keyLength = 12;
            int keyLength = new Random().nextInt(MAX_KEY_LENGTH);
            if(keyLength<3) keyLength=3;
            String key = getRandomKey(keyLength);
            DeVigenere v = new DeVigenere(key);
            if(iterate(input,v)>0){
                if(minSuccessfullLength>i){
                    minSuccessfullLength=i;
                    System.out.println("key = "+key+"\nCurrent min successfull length = "+minSuccessfullLength);
                }
                i=MAX_KEY_LENGTH;
                successCount++;
            }
        }

        System.out.println("Result\nRandom key size, shortest decrypted text length: "+minSuccessfullLength);
    }

    @Test
    //Find minimum length of input text encrypted with 12 char key
    //  which can be successfully decrypted
    public void testMinInputLengthMaxKeySize() throws Exception {

        System.out.println("\n\n###################");
        System.out.println("test shortest successfully cracked text");
        System.out.println("key length - random string 12 chars length");
        System.out.println("best result of 100 successful iterations will be shown");
        String inputUntrimmed;
        try {
            inputUntrimmed = cleanInput(getInput(-1));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        int minSuccessfullLength=Integer.MAX_VALUE;
        int successCount = 0;
        for(int i=MAX_KEY_LENGTH ; successCount<TEST_ITERATIONS ; i++){
            String input = inputUntrimmed.substring(0,i);
            String key = getRandomKey(MAX_KEY_LENGTH);
            DeVigenere v = new DeVigenere(key);
            if(iterate(input,v)>0){
                if(minSuccessfullLength>i){
                    minSuccessfullLength=i;

                }
                i=MAX_KEY_LENGTH;
                successCount++;
            }
        }

        System.out.println("Result\nMax key size, shortest decrypted text length: "+minSuccessfullLength);
    }


    public static int iterate(String input, DeVigenere v){
        String C = v.encrypt(input);
        Result result = v.crack(C);
        if(result.text.contentEquals(input) && result.key.contains(v.key)) {
            return input.length();
        }
        else return -1;
    }

    private static String getInput(int len) throws Exception {
        File file=null;
        switch (new Random().nextInt(3)){
            case 0:
                file=new File(INPUT_FILE0);
                break;
            case 1:
                file=new File(INPUT_FILE1);
                break;
            case 2:
                file=new File(INPUT_FILE2);
                break;
            case 3:
                file=new File(INPUT_FILE3);
                break;
        }
        FileReader fileReader=new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        while(br.ready())sb.append(br.readLine());
        String res = cleanInput(sb.toString());
        if(len>0 && res.length()>len)res=res.substring(0, len);
        return res;
    }

    public static String cleanInput(String s){
        s=s.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for(int i =0; i<s.length();i++){
            if(alphabet.indexOf(s.charAt(i))>=0) sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    public static String getRandomKey(int length) {

        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(number));
        }
        return sb.toString();
    }

}