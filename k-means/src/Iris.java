import java.util.List;

public class Iris {
    List<Double> attributes;
    String name;
    Iris(List<Double>attributes,String name){
        this.attributes=attributes;
        this.name=name;
    }

    @Override
    public String toString() {
        String attrsString="";
        for(Double attr:attributes)
            attrsString+=" "+attr;
        return name+attrsString;
    }
}
