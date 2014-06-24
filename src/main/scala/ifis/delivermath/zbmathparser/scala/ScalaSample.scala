package ifis.delivermath.zbmathparser.scala

import ifis.delivermath.zbmathparser.scala.ZbMetadataCorpus
import ifis.delivermath.zbmathparser.scala.ZbMetadataItem

object ScalaSample {
    def main(args: Array[String]) {
        val c = new ZbMetadataCorpus("/path/to/corpus")
        
        println(c.take(10).map(_.title))
    }
}
