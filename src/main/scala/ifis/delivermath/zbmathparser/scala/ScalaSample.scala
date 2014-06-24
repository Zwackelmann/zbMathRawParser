package ifis.delivermath.zbmathparser.scala

import ifis.delivermath.zbmathparser.scala.ZbMetadataCorpus
import ifis.delivermath.zbmathparser.scala.ZbMetadataItem

object ScalaSample {
    def main(args: Array[String]) {
        val c = new ZbMetadataCorpus("/home/simon/Dokumente/Arbeit/Research/Delivermath/ZBL Full Corpus/zbl_full_dmo.txt")
        
        println(c.take(10).map(_.title))
    }
}