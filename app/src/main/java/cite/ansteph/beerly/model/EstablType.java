package cite.ansteph.beerly.model;

/**
 * Created by loicstephan on 2017/10/13.
 */

public class EstablType {

    int id;
    String typeName, typeDesc;


    public EstablType() {
    }

    public EstablType(int id, String typeName, String typeDesc) {
        this.id = id;
        this.typeName = typeName;
        this.typeDesc = typeDesc;
    }

    public EstablType(String typeName, String typeDesc) {
        this.typeName = typeName;
        this.typeDesc = typeDesc;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
