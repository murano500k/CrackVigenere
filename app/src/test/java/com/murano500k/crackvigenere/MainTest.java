package com.murano500k.crackvigenere;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

import static com.murano500k.crackvigenere.DeVigenere.MAX_KEY_LENGTH;
import static com.murano500k.crackvigenere.DeVigenere.alphabet;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MainTest {
    private static final String ENCRYPTED = "testencrypted.txt";
    private static final String INPUT_FILE0 = "testinput0.txt";
    private static final String INPUT_FILE1 = "testinput1.txt";
    private static final String INPUT_FILE2 = "testinput2.txt";
    private static final String INPUT_FILE3 = "testinput3.txt";
    private static final String TEST_KEY = "testpassword";
    private static final int TEST_ITERATIONS = 100;

    @Test
    public void testEncrypt()throws Exception{
        DeVigenere vigenere =new DeVigenere(TEST_KEY);
        String input=getInput(-1);
        System.out.println("input:\n"+input);
        System.out.println("###################\n");
        System.out.println("key="+vigenere.key);
        System.out.println("encrypted:\n"+vigenere.encrypt(input));
    }

    @Test
    public void testCrack()throws Exception{
        String input = Main.readFile(ENCRYPTED);
        Result result =DeVigenere.devignere(input,12,'a','z');
        System.out.println("\tResult:\n\tkey:\n"+result.key+"\n\ttext:\n"+result.text);
        Assert.assertTrue(TEST_KEY.contains(result.key));
    }

    @Test
    //Find minimum length of input text encrypted with 2-12 char key
    //  which can be successfully decrypted
    public void testMinInputLengthRandomKeySize() throws Exception {
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
            if(iterate(i,input,v)>0){
                if(minSuccessfullLength>i){
                    minSuccessfullLength=i;
                    System.out.println("\nkey = "+key+"\nCurrent min successfull length = "+minSuccessfullLength);
                }
                i=MAX_KEY_LENGTH;
                successCount++;
            }
        }

        System.out.println("###########\n\n\nMin successfull length: "+minSuccessfullLength);
        assertTrue(minSuccessfullLength>0);
    }

    @Test
    //Find minimum length of input text encrypted with 12 char key
    //  which can be successfully decrypted
    public void testMinInputLengthMaxKeySize() throws Exception {
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
            if(iterate(i,input,v)>0){
                if(minSuccessfullLength>i){
                    minSuccessfullLength=i;
                    System.out.println("key = "+key+"\nCurrent min successfull length = "+minSuccessfullLength);
                }
                i=MAX_KEY_LENGTH;
                successCount++;
            }
        }

        System.out.println("###########\n\n\nMin successfull length: "+minSuccessfullLength);
        assertTrue(minSuccessfullLength>0);
    }



    public static int iterate(int i,String input, DeVigenere v){
        String C = v.encrypt(input);
        Result result = v.crack(C);
        if(result.text.contentEquals(input) && result.key.contains(v.key)) {
            return i;
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
        String base = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}