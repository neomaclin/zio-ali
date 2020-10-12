package zio.ali.mq

import com.aliyun.openservices.ons.api.Message
import com.aliyun.openservices.ons.api.transaction.{LocalTransactionExecuter, TransactionStatus}

trait MQLocalTransactionExecutor[T] extends LocalTransactionExecuter {
  override def execute(msg: Message, arg: Any): TransactionStatus = this.execute(msg, arg.asInstanceOf[T])
  def execute(message: Message, arg: T): TransactionStatus

}
