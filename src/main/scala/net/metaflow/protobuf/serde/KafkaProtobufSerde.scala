package net.metaflow.protobuf.serde

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}
import scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}

class KafkaProtobufSerde[T <: GeneratedMessage with Message[T]](generatedMessageCompanion: GeneratedMessageCompanion[T]) extends Serde[T] {
  override def serializer(): Serializer[T] = new KafkaProtobufSerializer[T]()

  override def deserializer(): Deserializer[T] = new KafkaProtobufDeserializer[T](generatedMessageCompanion)
}
