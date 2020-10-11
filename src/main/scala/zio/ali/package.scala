package zio


import java.io.{File, InputStream}
import java.net.URL
import java.util.Properties

import com.aliyun.openservices.ons.api.exception.ONSClientException
import com.aliyun.openservices.ons.api._
import com.aliyun.openservices.ons.api.order.MessageOrderListener
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker
import com.aliyun.oss.model.SetBucketCORSRequest.CORSRule
import com.aliyun.oss.model._
import com.aliyun.oss.{ClientException => OSSClientException, OSS => OSSClient}
import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.exceptions.ClientException
import zio.ali.models.OSS._
import zio.ali.models.SMS
import zio.ali.mq.{MQLocalTransactionExecutor, RocketMQConsumer, RocketMQOrderConsumer, RocketMQOrderProducer, RocketMQProducer, RocketMQPullConsumer, RocketMQTransactionProducer}
import zio.blocking.Blocking
import zio.duration.Duration
import zio.stream.ZStream

package object ali {
  type AliYun    = Has[AliYun.Service]
  type AliYunOSS = Has[AliYun.OSSService]
  type AliYunRocketMQProducer = Has[AliYun.RocketMQService.ProducerService]
  type AliYunRocketMQConsumer = Has[AliYun.RocketMQService.ConsumerService]
  type AliYunRocketMQPullConsumer = Has[AliYun.RocketMQService.PullConsumerService]
  type AliYunRocketMQOrderProducer = Has[AliYun.RocketMQService.OrderProducerService]
  type AliYunRocketMQOrderConsumer = Has[AliYun.RocketMQService.OrderConsumerService]
  type AliYunRocketTractionProducer = Has[AliYun.RocketMQService.TransactionProducerService]

  object AliYun {

    trait Service {
      def sendSMS(request: SMS.Request, templateParamValue: String): ZIO[Blocking, ClientException, SMS.Response]
      def execute[T](f: DefaultAcsClient => Task[T]): ZIO[Blocking, ClientException, T]
    }

    object RocketMQService{
      trait ProducerService{
        def send(message: Message): ZIO[Blocking,ONSClientException, SendResult]
        def sendAsync(message: Message): IO[OnExceptionContext, SendResult]
        def sendOneway(message: Message): Task[Unit]
      }
      trait ConsumerService{
        def subscribe(topic: String, selector: MessageSelector)(listener: MessageListener): Task[Unit]
      }

      trait OrderProducerService{
        def send(message: Message,shardingKey: String): ZIO[Blocking, ONSClientException, SendResult]
      }

      trait OrderConsumerService{
        def subscribe(topic: String, selector: MessageSelector)(listener: MessageOrderListener): Task[Unit]
      }

      trait TransactionProducerService{
        def send[T](message: Message, executor: MQLocalTransactionExecutor[T], arg: T): ZIO[Blocking, ONSClientException, SendResult]
      }

      // TODO: do not use me
      trait PullConsumerService{
        def poll(topic: String,duration: Duration): ZStream[Any, Throwable, Message]
      }
    }


    trait OSSService{
      def execute[T](f: OSSClient => Task[T]): ZIO[Blocking, OSSClientException, T]
      def createBucket(createBucketRequest: OSSCreateBucketRequest): ZIO[Blocking,OSSClientException,Bucket]
      def listBuckets(): ZIO[Blocking,OSSClientException,Seq[Bucket]]
      def listBuckets(listBucketsRequest: OSSListBucketsRequest): ZIO[Blocking, OSSClientException, BucketList]
      def doesBucketExist(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Boolean]
      def getBucketLocation(genericRequest: OSSGenericRequest): ZIO[Blocking,OSSClientException, String]
      def getBucketInfo(genericRequest: OSSGenericRequest): ZIO[Blocking,OSSClientException,BucketInfo]
      def setBucketAcl(setBucketAclRequest: OSSSetBucketAclRequest): ZIO[Blocking,OSSClientException,Unit]
      def getBucketAcl(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, AccessControlList]
      def deleteBucket(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketTagging(setBucketTaggingRequest: OSSSetBucketTaggingRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketTagging(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, TagSet]
      def deleteBucketTagging(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketPolicy(setBucketPolicyRequest: OSSSetBucketPolicyRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketPolicy(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, GetBucketPolicyResult]
      def deleteBucketPolicy(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest: OSSSetBucketInventoryConfigurationRequest): ZIO[Blocking,OSSClientException, Unit]
      def getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest: OSSGetBucketInventoryConfigurationRequest): ZIO[Blocking, OSSClientException, GetBucketInventoryConfigurationResult]
      def listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest: OSSListBucketInventoryConfigurationsRequest): ZIO[Blocking, OSSClientException, ListBucketInventoryConfigurationsResult]
      def deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest: OSSDeleteBucketInventoryConfigurationRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketLifecycle(setBucketLifecycleRequest: OSSSetBucketLifecycleRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketLifecycle(genericRequest: OSSGenericRequest):ZIO[Blocking, OSSClientException, Seq[LifecycleRule]]
      def deleteBucketLifecycle(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def initiateBucketWorm(initiateBucketWormRequest: OSSInitiateBucketWormRequest): ZIO[Blocking, OSSClientException,InitiateBucketWormResult]
      def abortBucketWorm(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def completeBucketWorm(completeBucketWormRequest: OSSCompleteBucketWormRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketWorm(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, GetBucketWormResult]
      def extendBucketWorm(extendBucketWormRequest: OSSExtendBucketWormRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketRequestPayment(setBucketRequestPaymentRequest: OSSSetBucketRequestPaymentRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketRequestPayment(genericRequest: OSSGenericRequest): ZIO[Blocking,OSSClientException,GetBucketRequestPaymentResult]
      def putObject(putObjectRequest: OSSPutObjectRequest): ZIO[Blocking, OSSClientException, PutObjectResult]
      def putObject(signedUrl: URL, filePath: String,requestHeaders:Map[String,String], useChunkEncoding: Boolean): ZIO[Blocking,OSSClientException,PutObjectResult]
      def putObject(signedUrl: URL, requestContent: InputStream, contentLength: Long, requestHeaders: Map[String, String], useChunkEncoding: Boolean):ZIO[Blocking,OSSClientException,PutObjectResult]
      def getObject(getObjectRequest: OSSGetObjectRequest, file: File): ZIO[Blocking, OSSClientException, ObjectMetadata]
      def getObject(getObjectRequest: OSSGetObjectRequest): ZIO[Blocking, OSSClientException, OSSObject]
      def getObject(getObjectRequest: OSSGetObjectURLRequest): ZIO[Blocking,OSSClientException,OSSObject]
      def selectObject(selectObjectRequest: OSSSelectObjectRequest): ZIO[Blocking, OSSClientException, OSSObject]
      def deleteObject(genericRequest: OSSGenericRequest): ZIO[Blocking,OSSClientException,Unit]
      def setBucketReferer(setBucketRefererRequest: OSSSetBucketRefererRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketReferer(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, BucketReferer]
      def setBucketLogging(request: OSSSetBucketLoggingRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketLogging(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, BucketLoggingResult]
      def setBucketWebsite(setBucketWebSiteRequest: OSSSetBucketWebsiteRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketWebsite(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, BucketWebsiteResult]
      def deleteBucketWebsite(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def addBucketReplication(addBucketReplicationRequest: OSSAddBucketReplicationRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketReplication(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Seq[ReplicationRule]]
      def getBucketReplicationProgress(getBucketReplicationProgressRequest: OSSGetBucketReplicationProgressRequest): ZIO[Blocking, OSSClientException, BucketReplicationProgress]
      def getBucketReplicationLocation(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Seq[String]]
      def deleteBucketReplication(deleteBucketReplicationRequest: OSSDeleteBucketReplicationRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketCORS(request: OSSSetBucketCORSRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketCORSRules(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException,Seq[CORSRule]]
      def deleteBucketCORSRules(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def appendObject(appendObjectRequest: OSSAppendObjectRequest): ZIO[Blocking, OSSClientException, AppendObjectResult]
      def uploadFile(uploadFileRequest: OSSUploadFileRequest): ZIO[Blocking, OSSClientException, UploadFileResult]
      def initiateMultipartUpload(request: OSSInitiateMultipartUploadRequest): ZIO[Blocking, OSSClientException, InitiateMultipartUploadResult]
      def uploadPart(request: OSSUploadPartRequest): ZIO[Blocking, OSSClientException, UploadPartResult]
      def completeMultipartUpload(request: OSSCompleteMultipartUploadRequest): ZIO[Blocking, OSSClientException, CompleteMultipartUploadResult]
      def abortMultipartUpload(request: OSSAbortMultipartUploadRequest): ZIO[Blocking, OSSClientException, Unit]
      def listParts(request: OSSListPartsRequest): ZIO[Blocking, OSSClientException, PartListing]
      def listMultipartUploads(request: OSSListMultipartUploadsRequest): ZIO[Blocking, OSSClientException, MultipartUploadListing]
      def downloadFile(downloadFileRequest: OSSDownloadFileRequest): ZIO[Blocking, OSSClientException, DownloadFileResult]
      def doesObjectExist(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Boolean]
      def setObjectAcl(setObjectAclRequest: OSSSetObjectAclRequest): ZIO[Blocking, OSSClientException, Unit]
      def getObjectAcl(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, ObjectAcl]
      def getObjectMetadata(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, ObjectMetadata]
      def copyObject(copyObjectRequest: OSSCopyObjectRequest): ZIO[Blocking, OSSClientException, CopyObjectResult]
      def getSimplifiedObjectMeta(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, SimplifiedObjectMeta]
      def restoreObject(restoreObjectRequest: OSSRestoreObjectRequest): ZIO[Blocking ,OSSClientException, RestoreObjectResult]
      def listObjects(listObjectsRequest: OSSListObjectsRequest): ZIO[Blocking, OSSClientException, ObjectListing]
      def createSelectObjectMetadata(createSelectObjectMetadataRequest: OSSCreateSelectObjectMetadataRequest): ZIO[Blocking, OSSClientException,SelectObjectMetadata]
      def deleteObjects(deleteObjectsRequest: OSSDeleteObjectsRequest): ZIO[Blocking, OSSClientException, DeleteObjectsResult]
      def uploadPartCopy(request: OSSUploadPartCopyRequest): ZIO[Blocking, OSSClientException, UploadPartCopyResult]
      def createSymlink(createSymlinkRequest: OSSCreateSymlinkRequest): ZIO[Blocking, OSSClientException, Unit]
      def getSymlink(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, OSSSymlink]
      def setBucketVersioning(setBucketVersioningRequest: OSSSetBucketVersioningRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketVersioning(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, BucketVersioningConfiguration]
      def listVersions(listVersionsRequest: OSSListVersionsRequest): ZIO[Blocking, OSSClientException, VersionListing]
      def deleteVersion(deleteVersionRequest: OSSDeleteVersionRequest): ZIO[Blocking, OSSClientException, Unit]
      def deleteVersions(deleteVersionsRequest: OSSDeleteVersionsRequest): ZIO[Blocking, OSSClientException, DeleteVersionsResult]
      def getObjectTagging(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, TagSet]
      def setObjectTagging(setObjectTaggingRequest: OSSSetObjectTaggingRequest): ZIO[Blocking, OSSClientException, Unit]
      def deleteObjectTagging(genericRequest: OSSGenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def generatePresignedUrl(request: OSSGeneratePresignedUrlRequest): ZIO[Blocking, OSSClientException, URL]
      def setBucketEncryption(setBucketEncryptionRequest: OSSSetBucketEncryptionRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketEncryption(genericRequest: OSSGenericRequest): ZIO[Blocking,OSSClientException, ServerSideEncryptionConfiguration]
      def deleteBucketEncryption(genericRequest: OSSGenericRequest): ZIO[Blocking,OSSClientException, Unit]
      def processObject(processObjectRequest: OSSProcessObjectRequest): ZIO[Blocking, OSSClientException, GenericResult]
    }
  }

  def live(region: String, credentials: AliYunCredentials): Layer[ConnectionError, AliYun] =
    ZLayer.fromManaged(Core.connect(region, credentials))

  val live: ZLayer[AliYunSettings, ConnectionError, AliYun] = ZLayer.fromFunctionManaged(Core.connect)

  def execute[T](f: DefaultAcsClient => Task[T]): ZIO[Blocking with AliYun, ClientException, T] =
    ZIO.accessM(_.get[AliYun.Service].execute(f))

  def sendSMS(request: SMS.Request, templateParamValue: String): ZIO[Blocking with AliYun,ClientException,SMS.Response] =
    ZIO.accessM(_.get[AliYun.Service].sendSMS(request, templateParamValue))

  def oss(endpoint: String, credentials: AliYunCredentials): Layer[ConnectionError, AliYunOSS] =
    ZLayer.fromManaged(OSS.connect(endpoint, credentials))

//  import OSS._

  def bucketInfo(bucketName: String): ZIO[Blocking with AliYunOSS,OSSClientException,BucketInfo] =
    ZIO.accessM(_.get[AliYun.OSSService].getBucketInfo(OSSGenericRequest(bucketName)))

  def rocketMQProducer(properties: Properties): Layer[ConnectionError, AliYunRocketMQProducer] =
    ZLayer.fromManaged(RocketMQProducer.connect(properties))

  def rocketMQConsumer(properties: Properties): Layer[ConnectionError, AliYunRocketMQConsumer] =
    ZLayer.fromManaged(RocketMQConsumer.connect(properties))

  def rocketMQOrderProducer(properties: Properties): Layer[ConnectionError, AliYunRocketMQOrderProducer] =
    ZLayer.fromManaged(RocketMQOrderProducer.connect(properties))

  def rocketMQOrderConsumer(properties: Properties): Layer[ConnectionError, AliYunRocketMQOrderConsumer] =
    ZLayer.fromManaged(RocketMQOrderConsumer.connect(properties))

  def rocketMQTransactionProducer(properties: Properties,checker: LocalTransactionChecker): Layer[ConnectionError, AliYunRocketTractionProducer] =
    ZLayer.fromManaged(RocketMQTransactionProducer.connect(properties,checker))

  // TODO: do not use me
  def rocketMQPullConsumer(properties: Properties): Layer[ConnectionError, AliYunRocketMQPullConsumer] =
    ZLayer.fromManaged(RocketMQPullConsumer.connect(properties))
}
