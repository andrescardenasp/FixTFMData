package example

//import scala.io.Source
//import scala.collection.mutable
//import scala.collection.immutable._

import org.apache.commons.lang3.StringUtils
import java.io._




object Main extends App {

  var MapLineNB = scala.collection.mutable.SortedMap[Tuple2[Int,String], String]()

  val ficheroOrig = "C:\\TFM-Data\\Orig\\telephoneEvents-18_06_2017.csv"
  val ficheroDest = "C:\\TFM-Data\\Fixed\\telephoneEvents-18_06_2017.csv"


  val bufferedSource = io.Source.fromFile(ficheroOrig)

  var id = 0
  var antenaActual = ""
  var antenaAnterior = ""
  var suma = false
  var horaActual = 0
  var horaAnterior = 0
  var line = ""

  for (linea <- bufferedSource.getLines) {
    var keyValue = (id, "")
line = linea.trim
    if (id == 0) {
      keyValue = (id, line)
    } else if (id == 1) {
      keyValue = (id, line)
      if (line.contains("-/")) {
        horaAnterior = StringUtils.substring(line, 21,23).toInt
        antenaAnterior = StringUtils.substring(line, line.length - 3, line.length)
      } else {
        horaAnterior = StringUtils.substring(line, 20,22).toInt
        antenaAnterior = StringUtils.substring(line, line.length - 3, line.length)
      }


    } else {

      if (line.contains("-/")) {
        horaActual = StringUtils.substring(line, 21,23).toInt
        antenaActual = StringUtils.substring(line, line.length - 3, line.length)

      } else {
        horaActual = StringUtils.substring(line, 20,22).toInt
        antenaActual = StringUtils.substring(line, line.length - 3, line.length)
      }

      if (!StringUtils.equals(antenaActual, antenaAnterior)) {
        suma = false
      }

      if (horaActual < horaAnterior && suma == false && StringUtils.equals(antenaActual, antenaAnterior)) {
        suma = true
      }

      if (suma == true){

        var fixedTime = horaActual + 12
        var fixedEvent = new StringBuilder(line)

        if (line.contains("-/")) {

          fixedEvent.setCharAt(21,fixedTime.toString().charAt(0))
          fixedEvent.setCharAt(22,fixedTime.toString().charAt(1))
          keyValue = (id, fixedEvent.toString())
          horaAnterior = horaActual
          antenaAnterior = antenaActual

        } else {

          fixedEvent.setCharAt(20,fixedTime.toString().charAt(0))
          fixedEvent.setCharAt(21,fixedTime.toString().charAt(1))
          keyValue = (id, fixedEvent.toString())
          horaAnterior = horaActual
          antenaAnterior = antenaActual
        }

      } else {
        horaAnterior = horaActual
        antenaAnterior = antenaActual
        keyValue = (id, line)
      }
    }


    MapLineNB += (keyValue -> keyValue._2)

    id = id + 1
    //println(keyValue.toString())
    //println("lalala")
  }
  bufferedSource.close


  val pw = new PrintWriter(new File(ficheroDest))

  MapLineNB foreach ( (t2) => pw.println(t2._2)  )
  pw.close
  MapLineNB.clear()

}