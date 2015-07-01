/*
 Copyright (c) 2009 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS..
 */
package org.cidarlab.phoenix.core.dom;

import org.cidarlab.phoenix.core.dom.NucSeq;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.clothocad.core.datums.ObjectId;
import org.clothocad.core.datums.SharableObjBase;

/**
 *
 * @author J. Christopher Anderson
 */
//unique name requirement
//sequence should be unique
//start/stop codons get clipped
//sequence cannot be degenerate
//cds-like sequences might be cds
//cds features must have valid cds sequence
//took out notes for demo
public class Feature extends SharableObjBase {

    @Setter
    @Getter
    private Sequence sequence;

    @Setter
    private Color forwardColor, reverseColor;

    @Setter
    @Getter
    private String genbankId, swissProtId, PDBId;

    @Setter
    @Getter
    private Person author;

    @Getter
    private short riskGroup;

    // Field currently isn't used and should be replaced with role
    @Getter
    @Deprecated
    private boolean isCDS;

    @Setter
    @Getter
    private FeatureRole role;

    @Setter
    @Getter
    private boolean isFP;

    
    @Setter
    @Getter
    private List<Arc> arcs;

    //Clotho ID
    @Setter
    @Getter
    private String clothoID;

    /**
     * No Args constructor
     */
    public Feature() {
        this.arcs = new ArrayList<>();
        this.forwardColor = new Color(0);
        this.reverseColor = new Color(0);
        this.isFP = false;
    }

    /**
     * Constructor of a new Feature
     *
     * @param name
     * @param seq
     * @param author
     * @param role
     */
    public Feature(String name, Sequence seq, Person author, FeatureRole role) {
        super(name, author);
        sequence = seq;
        this.role = role;
        this.isFP = false;
        this.forwardColor = new Color(0);
        this.reverseColor = new Color(0);
        isCDS = (role == FeatureRole.CDS);
        this.arcs = new ArrayList<>();
    }

    /**
     * Relayed constructor of a new Feature
     *
     * @param name
     * @param seq
     * @param author
     */
    private Feature(String name, Sequence seq, Person author, boolean iscds) {
        super(name, author);
        sequence = seq;
        isCDS = iscds;
        this.isFP = false;
        this.forwardColor = new Color(0);
        this.reverseColor = new Color(0);
        this.arcs = new ArrayList<>();
    }

    /**
     * Method for generating a Feature. Features are either CDS's or not. For
     * non-CDS features, the Feature will have the sequence as explicitly
     * submitted to the method. For CDS features, you can provide a sequence
     * with start and stop codons on the ends, but they'll get chopped off. You
     * can also provide them without starts or stops as long as they are in
     * frame from start to end. Starts and stops are inferred later during
     * autoannotate, so there is no real information loss. The reason things are
     * this way is to avoid calling multiple subtly-different CDS's different
     * names when they are simply regulatory variants of the same
     * polypeptide-coding sequence. Different codon usage, however, is regarded
     * as being a separate feature as it may behave differently during
     * translation (folding rates, translation rates, etc.)
     *
     * Regardless of strand, a Feature is considered to be equivalent. The CDS
     * Features must be passed as if they are encoded on the sense strand
     * (ATG...TAA). All Features will read 5' to 3', though they can be
     * annotated onto NucSeqs in either orientation.
     *
     * For CDS parts with intentional internal stops (often used during amber
     * suppression, but also might be natural for some organisms), this method
     * will warn the user that they are present, but it is acceptable to have
     * them.
     *
     * If a sequence passed to this method has degeneracy codes (N's R's etc.),
     * it currently will be rejected. Future version of Clotho will support
     * libraries, but for now, Features are exact sequences.
     *
     * After creating a Feature, you can set the Forward and Reverse colors
     * programmatically. If none are supplied, random colors will be generated
     * when those colors are later requested.
     *
     * You can also add Notes to the Feature after creating the Feature.
     *
     * Setting the Family(s) for a Feature is currently not implemented, but
     * will be implemented in future versions of Clotho.
     *
     * @param name the Feature's name (should be unique in the database)
     * @param seq the sequence of the Feature (case is ignored)
     * @param author the Person object to be author of the new Feature
     * @param type
     * @return the new Feature object, a preexisting Feature of the same
     * sequence, or null if the new Feature was rejected for some reason.
     */
    public static Feature generateFeature(String name, String seq, Person author, boolean iscds) {
        // To find a feature who's sequence matches the above (chop off start and stop for CDS)
        String uppSeq = seq.toUpperCase();
        String testseq = uppSeq;
        if (uppSeq.startsWith("ATG") || uppSeq.startsWith("GTG")) {
            testseq = uppSeq.substring(3);
        }
        if (uppSeq.endsWith("TAA") || uppSeq.endsWith("TAG") || uppSeq.endsWith("TGA")) {
            String dudly = testseq.substring(0, testseq.length() - 3);
            testseq = dudly;
        }

        //If it's cleared checks for preexisting features, start creating a new one
        NucSeq nseq = new NucSeq(uppSeq);

        final Feature afeature = new Feature(name, nseq, author, iscds);
        nseq.setLocked(true);

        return afeature;
    }

    /**
     * Get the preferred forward color for this Feature. If no forward color was
     * set, a default color will be returned.
     *
     * @return an AWT Color object. It won't be null;
     */
    public Color getForwardColor() {
        if (forwardColor == null) {
            return new Color(125, 225, 235);
        }
        return forwardColor;
    }

    /**
     * Get the preferred reverse color for this Feature. If no reverse color was
     * set, a default color will be returned.
     *
     * @return an AWT Color object. It won't be null;
     */
    public Color getReverseColor() {
        if (reverseColor == null) {
            return new Color(125, 225, 235);
        }
        return reverseColor;
    }

    /**
     * Set the forward and reverse preferred colors for this feature to some
     * random medium-bright color.
     */
    public void setRandomColors() {
        int[][] intVal = new int[2][3];
        for (int j = 0; j < 3; j++) {
            double doubVal = Math.floor(Math.random() * 155 + 100);
            intVal[0][j] = (int) doubVal;
            intVal[1][j] = 255 - intVal[0][j];
        }
        forwardColor = new Color(intVal[0][0], intVal[0][1], intVal[0][2]);
        reverseColor = new Color(intVal[1][0], intVal[1][1], intVal[1][2]);
    }

    public final void setRiskGroup(short newrg) {
        if (newrg > riskGroup && newrg <= 5) {
            //addUndo("_riskGroup", _featDatum._riskGroup, newrg);
            riskGroup = newrg;
            // setChanged(org.clothocore.api.dnd.RefreshEvent.Condition.RISK_GROUP_CHANGED);
        }
        //todo: throw appropriate invalid operation exception
    }

    /**
     * Get the Author of this Feature as a UUID link.
     *
     * @return a UUID String
     */
    public ObjectId getAuthorUUID() {
        return author.getId();
    }

    @Override
    public Feature clone() {
        Feature clone = new Feature();
        clone.PDBId = this.PDBId;
        clone.clothoID = this.clothoID;
        clone.forwardColor = this.forwardColor;
        clone.reverseColor = this.reverseColor;
        clone.genbankId = this.genbankId;
        clone.isCDS = this.isCDS;
        clone.riskGroup = this.riskGroup;
        clone.role = this.role;
        clone.sequence = this.sequence;
        clone.swissProtId = this.swissProtId;
        clone.arcs.addAll(this.arcs); //Copy?
        return clone;
    }

    // Feel free to add more of these
    public static enum FeatureRole {

        PROMOTER,
        PROMOTER_REPRESSIBLE,
        PROMOTER_INDUCIBLE,
        PROMOTER_CONSTITUTIVE,
        RBS,
        CDS,
        CDS_REPRESSOR,
        CDS_ACTIVATOR,
        CDS_REPRESSIBLE_REPRESSOR,
        CDS_ACTIVATIBLE_ACTIVATOR,
        CDS_LINKER,
        CDS_TAG,
        CDS_RESISTANCE,
        CDS_FLUORESCENT,
        CDS_FLUORESCENT_FUSION,
        TERMINATOR,
        ORIGIN,
        VECTOR,
        TESTING,
        MARKER,
        WILDCARD;
    }
}
