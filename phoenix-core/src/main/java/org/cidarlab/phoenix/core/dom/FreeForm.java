/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.List;
import org.cidarlab.phoenix.core.dom.Format;
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Part;

/**
 *
 * @author spaige
 */
public class FreeForm implements Format {

	@Override
    public boolean checkPart(Part p) {
        return true;
    }

	@Override
    public boolean checkComposite(List<Part> composition) {
        return true;
    }

	@Override
    public NucSeq generateCompositeSequence(List<Part> composition) {
        //XXX: dummy implementation
        StringBuilder builder = new StringBuilder();
        for (Part part : composition){
            builder.append(part.getSequence().getSeq());
        }
        return new NucSeq(builder.toString());
    }
    
}
