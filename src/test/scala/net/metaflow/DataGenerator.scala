package net.metaflow

import net.metaflow.models.address.{City, Coordinates}
import org.apache.kafka.clients.producer.{Producer, ProducerRecord}

import scala.io.Source
import scala.util.{Failure, Success, Try}
import java.lang

class DataGenerator {
  def genAddressData(producer : Producer[lang.Long, City], topic: String): Int = {
    var count = 0
    readCitiesDb().map(v => new ProducerRecord[lang.Long, City](topic, v.id, v))
      .foreach(p => {
        producer.send(p)
        count += 1
      })
    count
  }

  def readCitiesDb(): Iterator[City] = {
    Try(Source.fromResource("worldcities.csv")) match {
      case Success(v) => v.getLines.drop(1).map(line => {
        val addressParts: Array[String] = line.split("\",").map(_.replace("\"", ""))

        City(name = addressParts(0), id = addressParts(10).toLong,
          coordinates = Option(Coordinates(addressParts(2).toDouble, addressParts(3).toDouble)),
          countryIso2 = addressParts(3),
          capital = addressParts(8),
          population = Try(addressParts(9).toDouble.toInt).getOrElse(-1),
          adminName = addressParts(7))
      })
      case Failure(exception) => throw exception
    }
  }
}
