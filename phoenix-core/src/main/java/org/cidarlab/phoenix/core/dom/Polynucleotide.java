package org.cidarlab.phoenix.core.dom;

import java.io.Serializable;
import java.util.List;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.clothocad.core.datums.ObjBase;

@Data()
@NoArgsConstructor
public class Polynucleotide extends ObjBase implements Serializable {

    public Polynucleotide (String _name) {
        clothoID = _name;
    }
    
    private String description, accession;
    private boolean isLinear, isSingleStranded;
    private Date submissionDate;
        
    @NotNull
    private NucSeq sequence;
    
    //Clotho ID
    @Setter
    @Getter
    private String clothoID;
    
    //Part
    @Setter
    @Getter
    private Part part;
    
    //Vector
    @Setter
    @Getter
    private Vector vector;
    
    //Destination vector notation
    @Setter
    @Getter
    private boolean isDV;
    
    //MoClo level
    @Setter
    @Getter
    private int level;
    
    //Parts of which this polynucleotide is made
    @Setter
    @Getter
    private List<Part> parts;
}
