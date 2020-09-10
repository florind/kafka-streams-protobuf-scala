package net.metaflow

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.StreamsConfig

class Application {

  def config(bootstrapServers: String): Properties = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "APP_ID_1")
    props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 4)
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1")
    props
  }
}
