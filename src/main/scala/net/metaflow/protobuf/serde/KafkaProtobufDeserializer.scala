package net.metaflow.protobuf.serde

import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}

import scala.util.Try

class KafkaProtobufDeserializer[T <: GeneratedMessage with Message[T]](parser: GeneratedMessageCompanion[T]) extends Deserializer[T] {

  override def deserialize(topic: String, data: Array[Byte]): T = {
    Try(parser.parseFrom(data)) getOrElse(throw new SerializationException("can't deserialize"))
  }
}
