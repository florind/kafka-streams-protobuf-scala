package net.metaflow

import net.metaflow.CitiesTopology.{INPUT_TOPIC, OUTPUT_TOPIC}
import net.metaflow.models.address.City
import net.metaflow.protobuf.serde.KafkaProtobufSerde
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.scala.kstream.Consumed
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}

class CitiesTopology {
  def topologyInit(bootstrapServers: String): Unit = {

    val builder = new StreamsBuilder()
    builder.stream(INPUT_TOPIC)(Consumed.`with`(Serdes.String, new KafkaProtobufSerde(City.messageCompanion)))
    .filter ( (_, _) => true )
      .to(OUTPUT_TOPIC)(Produced.`with`(Serdes.String, new KafkaProtobufSerde(City.messageCompanion)))

    val topology = builder.build()
    val kafkaStreams = new KafkaStreams(topology, new Application().config(bootstrapServers))
    print(topology.describe)
    kafkaStreams.start()
  }
}

object CitiesTopology {
  val INPUT_TOPIC = "input-topic"
  val OUTPUT_TOPIC = "output-topic"
}
