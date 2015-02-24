/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import javax.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author spaige
 */
@NoArgsConstructor
public class BasicPart extends Part {

//    @Valid
//    @Getter
//    private NucSeq sequence;

    public BasicPart(String name, String shortdescription, NucSeq seq, Format form, Person author) {
        super(name, shortdescription, seq, form, author);
//        this. = new NucSeq(seq);
    }

//    @Override
//    public boolean checkFormat() {
//        return getFormat().checkPart(this);
//    }

    /**
     * This is a convenience method, the real change to the sequence happens in
     * the linked NucSeq
     *
     * @param newseq
     */
//    public void setSequence(final String newseq) {
//        if (newseq.equals("") || newseq == null) {
//            return;
//        }
//
//        final String oldseq = sequence.toString();
//
//        sequence.APIchangeSeq(newseq);
//
//        boolean isok = getFormat().checkPart(this);
//        if (!isok) {
//            sequence.APIchangeSeq(oldseq);
//            return;
//        }

        //Change the risk group
        //riskGroup = sequence.performBiosafetyCheck();
//    }
}
