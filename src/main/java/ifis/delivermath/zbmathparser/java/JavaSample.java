package ifis.delivermath.zbmathparser.java;

import ifis.delivermath.zbmathparser.java.ZbMetadataItem;
import ifis.delivermath.zbmathparser.java.ZbMetadataCorpus;

public class JavaSample {
    public static void main(String[] args) {
        ZbMetadataCorpus c = new ZbMetadataCorpus("/home/simon/Dokumente/Arbeit/Research/Delivermath/ZBL Full Corpus/zbl_full_dmo.txt");
        
        for(ZbMetadataItem mi : c) {
            System.out.println(mi.title());
        }
    }
}