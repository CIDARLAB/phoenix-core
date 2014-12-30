package org.clothocad.core.datums;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author spaige
 */
@EqualsAndHashCode(exclude = {"dateCreated", "lastModified", "lastAccessed", "isDeleted"})
@Data()
@NoArgsConstructor
@Slf4j
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "schema", include = JsonTypeInfo.As.PROPERTY)
public abstract class ObjBase {

    //add schema
    //remove schema
    //can only manipulate schema set if you have write privs
    public ObjBase(String name) {
        this.name = name;
    }
    
  
    //@JsonProperty("_id")
    private ObjectId id;
    private String name;
  
    private boolean isDeleted;
    @Setter(AccessLevel.NONE)
    private Date dateCreated;
  
    private Date lastModified, lastAccessed;
    
  

}
