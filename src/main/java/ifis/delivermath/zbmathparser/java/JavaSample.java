package ifis.delivermath.zbmathparser.java;

import ifis.delivermath.zbmathparser.java.ZbMetadataItem;
import ifis.delivermath.zbmathparser.java.ZbMetadataCorpus;

public class JavaSample {
    public static void main(String[] args) {
        ZbMetadataCorpus c = new ZbMetadataCorpus("/path/to/corpus");
        
        for(ZbMetadataItem mi : c) {
            System.out.println(mi.title());
        }
    }
}
