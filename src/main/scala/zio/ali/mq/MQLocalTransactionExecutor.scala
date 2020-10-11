package zio.ali.mq

import com.aliyun.openservices.ons.api.Message
import com.aliyun.openservices.ons.api.transaction.TransactionStatus

trait MQLocalTransactionExecutor[T] {
  def execute(message: Message, arg: T): TransactionStatus

}
