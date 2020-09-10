package net.metaflow.protobuf.serde

import org.apache.kafka.common.serialization.Serializer
import scalapb.{GeneratedMessage, Message}

class KafkaProtobufSerializer[T <: GeneratedMessage with Message[T]] extends Serializer[T] {
  override def serialize(topic: String, data: T): Array[Byte] = data.toByteArray
}
