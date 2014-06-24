package ifis.delivermath.zbmathparser.java;

import ifis.delivermath.zbmathparser.java.ZbMetadataItem;

import java.util.Iterator;

public class ZbMetadataCorpus implements Iterable<ZbMetadataItem> {
    private String filename;
    
    public ZbMetadataCorpus(String filename) {
        this.filename = filename;
    }
    
    @Override
    public Iterator<ZbMetadataItem> iterator() {
        return new ZbMetadataIterator(filename);
    }
}
