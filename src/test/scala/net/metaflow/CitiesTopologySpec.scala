package net.metaflow

import java.lang
import java.time.Duration
import java.util.Properties

import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer}
import net.metaflow.CitiesTopology.{INPUT_TOPIC, OUTPUT_TOPIC}
import net.metaflow.models.address.City
import net.metaflow.protobuf.serde.{KafkaProtobufDeserializer, KafkaProtobufSerializer}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, Producer}
import org.apache.kafka.common.serialization.{LongSerializer, StringDeserializer}
import org.awaitility.Awaitility.await
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.SECONDS
import scala.jdk.CollectionConverters._

class CitiesTopologySpec extends AnyFlatSpec with Matchers with ForAllTestContainer with BeforeAndAfterAll {
  override val container: KafkaContainer = new KafkaContainer()
  container.start()
  private val kafkaProps: Properties = new Application().config(container.bootstrapServers)
  kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  val consumer = new KafkaConsumer(kafkaProps,
    new StringDeserializer(), new KafkaProtobufDeserializer(City.messageCompanion))

  val producer: Producer[lang.Long, City] = new KafkaProducer(kafkaProps, new LongSerializer(), new KafkaProtobufSerializer[City]())

  "S S join" should "save" in {
    val topology = new CitiesTopology()
    topology.topologyInit(container.bootstrapServers)
    val recordsCount = new DataGenerator().genAddressData(producer, INPUT_TOPIC)
    producer.flush()

    consumer.subscribe(List(OUTPUT_TOPIC).asJava)

    var consumerRecords = ListBuffer[ConsumerRecord[String, City]]()


    Future {
      while (!Thread.currentThread().isInterrupted) {
        val received = consumer.poll(Duration.ofMillis(100))
        received.forEach(x => consumerRecords += x)
      }
    }
    await().atMost(2, SECONDS).until(() => {
      val records = consumerRecords.map(_.value().name)
      records.size == recordsCount && records.last == "Ennadai"
    })
  }

  override def afterAll(): Unit = {
    container.stop()
  }
}
