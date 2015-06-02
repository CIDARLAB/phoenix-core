/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

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
    
}
