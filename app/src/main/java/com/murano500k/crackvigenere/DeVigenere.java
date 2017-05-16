package com.murano500k.crackvigenere;

public class DeVigenere {

    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    public static final int MAX_KEY_LENGTH = 12;
    private static double[] frequencies = { 8.167, 1.492, 2.782, 4.253, 12.702, 2.228,
            2.015, 6.094, 6.966, 0.153, 0.772, 4.025, 2.406, 6.749, 7.507,
            1.929, 0.095, 5.987, 6.327, 9.056, 2.758, 0.978, 2.360, 0.150,
            1.974, 0.074 }; //frequencies of a-z in english

    public String key;

    public DeVigenere(String key) {
        this.key=key;
    }

    private static double score(String data) { // compute sum squares of deviations from expected frequency
        int[] letters = new int[26];   //compute histogram of a-z frequency:
        for(char c : data.toLowerCase().replaceAll("[^a-z]", "").toCharArray()) letters[c - 'a']++;

        double sumDSquared = 0.0;
        for(int j = 0; j < frequencies.length; j++) sumDSquared += Math.pow((100.0 * letters[j] / data.length() - frequencies[j]), 2);
        return sumDSquared;
    }

    public static String min(String a, String b) { //Return which of 2 strings has lower score
        return a == null || (b != null && score(a) > score(b)) ? b : a;
    }

    public static int minInt(String a, String b) {
        return a == null || (b != null && score(a) > score(b)) ? -1 : 1;
    }

    static String decaesar(String msg, char minChar, char maxChar) { //Returns caesar decryption with lowest score
        String best = null;
        for(int i = minChar; i <= maxChar; i++) best = min(best, vignere(msg, "" + (char) (i), minChar, maxChar));
        return best;
    }

    static String vignere(String data, String password, char minChar, char maxChar) { //vigenere cipher
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            int pass = password.charAt(i % password.length()) - minChar, range = maxChar - minChar + 1;
            out.append((c >= minChar && c <= maxChar) ? (char) (minChar + ((c - minChar + pass) % range + range) % range) : c);
        }
        return out.toString();
    }

    static StringBuilder[] splitInterleaves(String input, int length) { //converts "123456",3 to array "14","25","36"
        StringBuilder[] inter = new StringBuilder[length];
        for(int i = 0; i < inter.length; i++) inter[i] = new StringBuilder();
        for(int j = 0; j < input.length(); j++) inter[j % length].append(input.charAt(j));
        return inter;
    }

    static Result devignere(String input, int maxLen, char minChar, char maxChar) {// Guessing passwords from 1 to maxLen.
        String best = null;
        int bestPwdLen = 0;
        for(int pwlen = 1; pwlen <= maxLen; pwlen++) {
            StringBuilder[] inter = splitInterleaves(input, pwlen);
            String[] decryptedInterleaves = new String[pwlen];
            // Decrypt each
            for(int j = 0; j < pwlen; j++) decryptedInterleaves[j] = decaesar(inter[j].toString(), minChar, maxChar);

            StringBuilder combined = new StringBuilder();
            for(int x = 0; x < input.length(); x++)
                combined.append(decryptedInterleaves[x % pwlen].charAt(x / pwlen));// Combine interleaves
            if(minInt(combined.toString(), best)>0){
                best=combined.toString();
                bestPwdLen=pwlen;
            }
        }
        return new Result(best, String.valueOf(resolveKey(input, best,bestPwdLen))/**/);
    }

    private static String resolveKey(String enc, String input, int bestPwdLen) {
        char[] key=new char[bestPwdLen];
        for (int i = 0; i < bestPwdLen; i++) {
            int inp = (int) input.charAt(i);
            int out = (int) enc.charAt(i);
            int res;
            if (out < inp) out += 26;
            res = ((int) 'a') + (out - inp);
            char resC = (char) res;
            key[i] = resC;
        }
        return String.valueOf(key);
    }

    public String encrypt(String s) {
        return vignere(s, key, 'a','z');
    }


    public Result crack(String s) {
        return devignere(s,12, 'a','z');
    }


}
