package ifis.delivermath.zbmathparser.scala

import java.util.Date
import scala.io.Source
import java.io.File
import scala.collection.mutable
import scala.collection.mutable.HashMap
import scala.io.BufferedSource
import java.io.BufferedInputStream
import java.io.FileInputStream
import scala.io.Codec
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction

case class ZbMetadataItem(val paperId: Int, val title: String, val abstractText: Option[String], val mscCats: List[String], 
        val sc: Option[String], val publicationDate: Date, val authors: List[String], val authorIdentifiers: List[String], 
        val languages: List[String], val unorderedTerms: List[String], val issns: List[String], val doi: Option[String], 
        val euclidId: Option[String], val arxivId: Option[String], val emisId: Option[String], val numdamId: Option[String], 
        val vixraId: Option[String], val crelleId: Option[String], val httpLink: Option[String], val ftpLink: Option[String])

object ZbMetadataIterator {
    def normalizeMSC(msc: String) = msc.length() match {
        case 5 => if(msc.charAt(3) == 'X') msc else msc.substring(0, 3) + "xx"
        case 3 => msc + "xx"
        case 2 => msc + "-xx"
        case _ => throw new IllegalAccessException(msc + " cannot be recognized as MSC class")
    }
    
    def rawDoc2Doc(doc: scala.collection.Map[String, String]) = {
        val paperId = doc(":id:").trim().toInt
        
        val title = {
            val tmpTitle = doc(":ti:").trim()
            
            if(tmpTitle.contains("\n")) tmpTitle.split("\n").map(_.trim()).mkString(" ")
            else tmpTitle
        }
        
        val publicationTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(doc(":tm:").trim())
        val publicationYear = doc(":py:").trim()
        
        val authors = doc(":au:")
            .trim()
            .split("; ")
            .map(_.trim())
            .filter(_ != "")
            .toList
        
        val authorIdentifiers = doc(":ai:")
            .trim()
            .split("; ")
            .map(_.trim())
            .filter(_ != "")
            .toList
        
        val issns = if(doc contains ":se.sn:") {
            doc(":se.sn:")
                .trim()
                .split("; ")
                .map(_.trim())
                .filter(_ != "")
                .toList
        } else {
            List()
        }
        
        val literatureIds = doc(":li:")
            .trim()
            .split("\n")
            .map(_.trim())
            .filter(_ != "")
            .toList
        
        var doi: Option[String] = None
        var url: Option[String] = None
        var euclidId: Option[String] = None
        var arxivId: Option[String] = None
        var emisId: Option[String] = None
        var numdamId: Option[String] = None
        var vixraId: Option[String] = None
        var crelleId: Option[String] = None
        var ftpUrl: Option[String] = None

        for(lid <- literatureIds) {
            if(lid.substring(0, 3) == "doi") {
                doi = Some(lid.substring(4, lid.length()))
            } else if(lid.substring(0, 4) == "http") {
                url = Some(lid.substring(5, lid.length()))
            } else if (lid.substring(0, 6) == "euclid") {
                euclidId = Some(lid.substring(7, lid.length()))
            } else if (lid.substring(0, 5) == "arxiv") {
                var versionNumberLength = 1
                while(lid.charAt(lid.length()-(versionNumberLength+1)) != 'v' && versionNumberLength < 4) {
                    versionNumberLength += 1
                }
                
                arxivId = Some({
                    var tmp = if(versionNumberLength == 4) lid.substring(6, lid.length()) 
                    else lid.substring(6, lid.length()-(versionNumberLength+1))
                    
                    if(tmp.charAt(tmp.length()-1) == '.') tmp.substring(0, tmp.length()-1)
                    else tmp
                })
            } else if (lid.substring(0, 4) == "emis") {
                emisId = Some(lid.substring(5, lid.length()))
            } else if (lid.substring(0, 6) == "numdam") {
                numdamId = Some(lid.substring(7, lid.length()))
            } else if (lid.substring(0, 5) == "vixra") {
                vixraId = Some(lid.substring(6, lid.length()))
            } else if (lid.substring(0, 6) == "crelle") {
                crelleId = Some(lid.substring(7, lid.length()))
            } else if (lid.substring(0, 3) == "ftp") {
                ftpUrl = Some(lid.substring(4, lid.length()))
            }
        }
        
        /* val validLanguages = Set(
            "ZH", "EN", "RU", "DE", "FR", "PT", "UK", "IT", "KO", "CS", "NL", "JP", 
            "ES", "EB", "JA", "SK", "FI", "CA", "DA", "SV", "LA", "EL", "PL", "GA", 
            "RO", "BG", "SL", "SR", "NO", "SA", "MS", "HI", "AR", "ET", "HU", "HR", 
            "TR", "BN", "MK", "IN", "EM", "FA", "MA", "LT", "LV", "TH", "KA", "UZ", 
            "GL", "BE", "SH", "SB", "AL", "EO", "AZ", "MO", "HY", "AF", "VI", "CY", 
            "KS", "MN", "IW", "FL", "TK", "AM"
        ) */
        
        val languages = doc(":la:").trim().split(" ").toList
        
        val unorderedTerms = {
            val lines = doc(":ut:").trim().split("\n")
            lines.flatMap(line => line.split(";")
                .map(_.trim())
                .filter(_ != "")
            ).toList
        }
        
        val sc = doc.get(":sc:").map(_.trim())
        
        val abstractText = 
            if(doc contains ":ab/en:") Some(doc(":ab/en:").trim()) 
            else None
        
        val mscCats = doc(":cc:")
            .trim()
            .split(" ")
            .filter(_ != "")
            .map(normalizeMSC)
            .toList
        
        ZbMetadataItem(paperId, title, abstractText, mscCats, sc, publicationTime, authors, authorIdentifiers, 
            languages, unorderedTerms, issns, doi, euclidId, arxivId, emisId, numdamId, vixraId, 
            crelleId, url, ftpUrl)
    }
    
    val ignoreSet = Set(
        ":dt:", ":rf:", ":ci:", ":so:", ":an:", ":db:", ":RV:",
        ":se.pt:", ":se.is:", ":se.ti:", ":se.tp:", ":se.vo:", ":se.in:", ":se.st:", ":se.se:", 
        ":mo.bn:", ":mo.ti:", ":mo.ed:",  
        ":rv/en:", ":ab/de:", ":rv/de:", ":ab/fr:", ":rv/fr:", ":ab/es:", ":rv/es:", ":ab/it:", ":rv/it:"
    )
}

class ZbMetadataCorpus(val filePath: String) extends Iterable[ZbMetadataItem] {
    def iterator() = new ZbMetadataIterator(filePath)
}

class ZbMetadataIterator(filePath: String) extends Iterator[ZbMetadataItem] {
    import ZbMetadataIterator._
    
    val lines = {
        val codec = new Codec(Charset.defaultCharset())
        codec.onMalformedInput(CodingErrorAction.IGNORE)
        
        val bis = new BufferedInputStream(new FileInputStream(new File(filePath)))
        
        new BufferedSource(bis)(codec).getLines
    }
    var lastKey: String = null
    var buffer: ZbMetadataItem = null
    
    def hasNext = !(buffer == null && bufferNext() == null)
    
    def next() = if(hasNext) {
        val b = buffer
        buffer = null
        b
    } else {
        null
    }
    
    def bufferNext() = {
        if(!lines.hasNext) {
            buffer = null
        } else {
            val rawDoc = mutable.HashMap[String, String]()
            
            var line = lines.next()
            var key: Option[String] = None
            var lastKey: Option[String] = None
            
            while(line.trim() != "::::" && line != null) {
                if(line.length() > 4 && line.charAt(0) == ':' && line.charAt(3) == ':') { 
                    key = Some(line.substring(0, 4))
                } else if(line.length() > 7 && line.charAt(0) == ':' && line.charAt(6) == ':') {
                    key = Some(line.substring(0, 7))
                } else {
                    key = None
                }
                
                if(key.map(k => !(ignoreSet contains k)).getOrElse(true)) {
                    if(key.isDefined) {
                        rawDoc += (key.get -> line.substring(key.get.length()+1, line.length()))
                    } else if(lastKey.map(k => !(ignoreSet contains k)).getOrElse(false)) {
                        rawDoc(lastKey.get) = rawDoc(lastKey.get) + "\n" + line
                    }
                }
    
                if(key.isDefined) lastKey = key
    
                if(lines.hasNext) {
                    line = lines.next()
                } else {
                    line = null
                }
            }
            
            if(line != null || (line == null && (rawDoc contains ":id:"))) {
                buffer = rawDoc2Doc(rawDoc)
            } else {
                buffer = null
            }
        }
        
        buffer
    }
}