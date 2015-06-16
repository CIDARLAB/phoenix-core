/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;

/**
 *
 * THIS IS THE FILE FOR RANDOM UTILITY METHODS
 * 
 * @author evanappleton
 */
public class Utilities {
  
    /*
     * Get the reverse complement of a sequence
     */    
    public static String reverseComplement(String seq) {
//        String lSeq = seq.toLowerCase();
        String lSeq = seq;
        String revComplement = "";
        for (int i = 0; i < lSeq.length(); i++) {
            if (lSeq.charAt(i) == 'a') {
                revComplement = "t" + revComplement;
            } else if (lSeq.charAt(i) == 'g') {
                revComplement = "c" + revComplement;
            } else if (lSeq.charAt(i) == 'c') {
                revComplement = "g" + revComplement;
            } else if (lSeq.charAt(i) == 't') {
                revComplement = "a" + revComplement;
            } else if (lSeq.charAt(i) == 'w') {
                revComplement = "w" + revComplement;
            } else if (lSeq.charAt(i) == 's') {
                revComplement = "s" + revComplement;
            } else if (lSeq.charAt(i) == 'm') {
                revComplement = "k" + revComplement;
            } else if (lSeq.charAt(i) == 'k') {
                revComplement = "m" + revComplement;
            } else if (lSeq.charAt(i) == 'r') {
                revComplement = "y" + revComplement;
            } else if (lSeq.charAt(i) == 'y') {
                revComplement = "r" + revComplement;
            } else if (lSeq.charAt(i) == 'b') {
                revComplement = "v" + revComplement;
            } else if (lSeq.charAt(i) == 'd') {
                revComplement = "h" + revComplement;
            } else if (lSeq.charAt(i) == 'h') {
                revComplement = "d" + revComplement;
            } else if (lSeq.charAt(i) == 'v') {
                revComplement = "b" + revComplement;
            } else if (lSeq.charAt(i) == 'n') {
                revComplement = "n" + revComplement;
            } else if (lSeq.charAt(i) == 'A') {
                revComplement = "T" + revComplement;
            } else if (lSeq.charAt(i) == 'G') {
                revComplement = "C" + revComplement;
            } else if (lSeq.charAt(i) == 'C') {
                revComplement = "G" + revComplement;
            } else if (lSeq.charAt(i) == 'T') {
                revComplement = "A" + revComplement;
            } else if (lSeq.charAt(i) == 'W') {
                revComplement = "W" + revComplement;
            } else if (lSeq.charAt(i) == 'S') {
                revComplement = "S" + revComplement;
            } else if (lSeq.charAt(i) == 'M') {
                revComplement = "K" + revComplement;
            } else if (lSeq.charAt(i) == 'K') {
                revComplement = "M" + revComplement;
            } else if (lSeq.charAt(i) == 'R') {
                revComplement = "Y" + revComplement;
            } else if (lSeq.charAt(i) == 'Y') {
                revComplement = "R" + revComplement;
            } else if (lSeq.charAt(i) == 'B') {
                revComplement = "V" + revComplement;
            } else if (lSeq.charAt(i) == 'D') {
                revComplement = "H" + revComplement;
            } else if (lSeq.charAt(i) == 'H') {
                revComplement = "D" + revComplement;
            } else if (lSeq.charAt(i) == 'V') {
                revComplement = "B" + revComplement;
            } else {
                revComplement = "N" + revComplement;
            }
        }
        return revComplement;
    }
    
    /**
     * Logic for going from OH variable place holders to actual sequences for MoClo *
     */
    public static BiMap<String, String> getMoCloOHseqs() {

        BiMap<String, String> overhangVariableSequenceHash = HashBiMap.create();
//        HashMap<String, String> overhangVariableSequenceHash = new HashMap<String, String>();
        overhangVariableSequenceHash.put("0", "ggag");
        overhangVariableSequenceHash.put("1", "tact");
        overhangVariableSequenceHash.put("2", "aatg");
        overhangVariableSequenceHash.put("3", "aggt");
        overhangVariableSequenceHash.put("4", "gctt");
        overhangVariableSequenceHash.put("5", "cgct");
        overhangVariableSequenceHash.put("6", "tgcc");
        overhangVariableSequenceHash.put("7", "acta");
        overhangVariableSequenceHash.put("8", "tcta");
        overhangVariableSequenceHash.put("9", "cgac");
        overhangVariableSequenceHash.put("10", "cgtt");
        overhangVariableSequenceHash.put("11", "tgtg");
        overhangVariableSequenceHash.put("12", "atgc");
        overhangVariableSequenceHash.put("13", "gtca");
        overhangVariableSequenceHash.put("14", "gaac");
        overhangVariableSequenceHash.put("15", "ctga");
        overhangVariableSequenceHash.put("16", "acag");
        overhangVariableSequenceHash.put("17", "tagc");
        overhangVariableSequenceHash.put("18", "atcg");
        overhangVariableSequenceHash.put("19", "cagt");
        overhangVariableSequenceHash.put("20", "gcaa");
        overhangVariableSequenceHash.put("21", "cggc");
        overhangVariableSequenceHash.put("22", "aaga");
        overhangVariableSequenceHash.put("0*", "ctcc");
        overhangVariableSequenceHash.put("1*", "agta");
        overhangVariableSequenceHash.put("2*", "catt");
        overhangVariableSequenceHash.put("3*", "acct");
        overhangVariableSequenceHash.put("4*", "aagc");
        overhangVariableSequenceHash.put("5*", "agcg");
        overhangVariableSequenceHash.put("6*", "ggca");
        overhangVariableSequenceHash.put("7*", "tagt");
        overhangVariableSequenceHash.put("8*", "taga");
        overhangVariableSequenceHash.put("9*", "gtcg");
        overhangVariableSequenceHash.put("10*", "aacg");
        overhangVariableSequenceHash.put("11*", "caca");
        overhangVariableSequenceHash.put("12*", "gcat");
        overhangVariableSequenceHash.put("13*", "tgac");
        overhangVariableSequenceHash.put("14*", "gtcc");
        overhangVariableSequenceHash.put("15*", "tcag");
        overhangVariableSequenceHash.put("16*", "ctgt");
        overhangVariableSequenceHash.put("17*", "gcta");
        overhangVariableSequenceHash.put("18*", "cgat");
        overhangVariableSequenceHash.put("19*", "actg");
        overhangVariableSequenceHash.put("20*", "ttgc");
        overhangVariableSequenceHash.put("21*", "gccg");
        overhangVariableSequenceHash.put("22*", "tctt");
        return overhangVariableSequenceHash;
    }
    
}
