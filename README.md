zbMathRawParser
===============
This parser can be used to translate the metadata information from the Zentralblatt MATH corpus from its internal hand made format into objects for the JVM.

The parser comes with 2 versions -- one for scala and one for java. The appropiate parsers can be found in the packages `ifis.delivermath.zbmathparser.scala` and `ifis.delivermath.zbmathparser.java` respectively.

##Java sample
The following java sample initializes a corpus iterator for the given file and iterates over the titles of all documents. The iteratior processes the corpus lazily, which means that in order to process the first item, only the first item will be read from the corpus, etc.

```java
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
```

###ZbMetadataItem
The items to be iterated over are structured as follows:

```scala
class ZbMetadataItem(
    int paperId, // continously increasing integer number
    String title, // the title...
    String abstractText, // the abstract...
    List<String> mscCats, /* List of MSC categories - 5 characters each
                           * If a non-leaf node is addressed the format is e.g. 05Bxx or 05-xx
                           * There are also second teer nodes like 05-03 - documents containing
                           * such categories contain meta-information about the respective top 
                           * level category (e.g. conference info, examples, applications, etc.) */
    String sc, // Sometimes very long text accuring additionally to the abstract. No clue what it means...
    Date publicationDate, // publication date...
    List<String> authors, // list of authors...
    List<String> authorIdentifiers, // list of corpus internal unique author identifiers
    List<String> languages, /* list of used languages (mostly its List(EN), but sometimes 
                             * multiple languages are given) */
    List<String> unorderedTerms, // List of author terms assigned to the paper
    List<String> issns, /* List of issns. Sometimes two issns are given - most likely when a print 
                         * version and an e-version exists. */
    String doi, // doi (quire frequently set)
    String arxivId, // arxivId (quite rare)
    String euclidId, // euclidId (very rare)
    String emisId, // emisId (very rare)
    String numdamId, // numdamId (very rare)
    String vixraId, // vixraId (very rare)
    String crelleId, // crelleId (very rare)
    String httpLink, // httpLink (extremely rare) 
    String ftpLink // ftpLink (extremely rare)
)
```

##Scala sample
The following scala sample initializes a corpus iterator for the given file and iterates prints the titles of the first 10 documents. The iteratior processes the corpus lazily, which means that in order to process the first item, only the first item will be read from the corpus, etc.

```scala
import ifis.delivermath.zbmathparser.scala.ZbMetadataCorpus
import ifis.delivermath.zbmathparser.scala.ZbMetadataItem

object ScalaSample {
    def main(args: Array[String]) {
        val c = new ZbMetadataCorpus("/path/to/corpus")
        
        println(c.take(10).map(_.title))
    }
}
```

###ZbMetadataItem
The items to be iterated over are structured as follows:

```scala
class ZbMetadataItem(
    val paperId: Int, // continously increasing integer number
    val title: String, // the title...
    val abstractText: Option[String], // the abstract...
    val mscCats: List[String], /* List of MSC categories - 5 characters each
                                * If a non-leaf node is addressed the format is e.g. 05Bxx or 05-xx
                                * There are also second teer nodes like 05-03 - documents containing
                                * such categories contain meta-information about the respective top 
                                * level category (e.g. conference info, examples, applications, etc.) */
    val sc: Option[String], // Sometimes very long text accuring additionally to the abstract. No clue what it means...
    val publicationDate: Date, // publication date...
    val authors: List[String], // list of authors...
    val authorIdentifiers: List[String], // list of corpus internal unique author identifiers
    val languages: List[String], /* list of used languages (mostly its List(EN), but sometimes 
                                  * multiple languages are given) */
    val unorderedTerms: List[String], // List of author terms assigned to the paper
    val issns: List[String], /* List of issns. Sometimes two issns are given - most likely when a print 
                              * version and an e-version exists. */
    val doi: Option[String], // doi (quire frequently set)
    val arxivId: Option[String], // arxivId (quite rare)
    val euclidId: Option[String], // euclidId (very rare)
    val emisId: Option[String], // emisId (very rare)
    val numdamId: Option[String], // numdamId (very rare)
    val vixraId: Option[String], // vixraId (very rare)
    val crelleId: Option[String], // crelleId (very rare)
    val httpLink: Option[String], // httpLink (extremely rare) 
    val ftpLink: Option[String] // ftpLink (extremely rare)
)
```
