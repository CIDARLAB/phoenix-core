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

    //@Getter
    //private ObjectId id;
    //@Getter
    private String description, accession;
    
    //@Getter
    private boolean isLinear, isSingleStranded;
    
    //@Getter
    private Date submissionDate;
    
    //@Getter
    private List<Highlight> highlights;
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
    private Part vector;
}
