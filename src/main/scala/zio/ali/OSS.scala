package zio.ali

import java.io.{File, InputStream}
import java.net.URL

import com.aliyun.oss.model._
import zio.{Managed, Task, ZIO}
import com.aliyun.oss.{ClientBuilderConfiguration, ClientException, OSSClientBuilder, OSS => DefaultOSS}
import zio.ali.models.OSS._
import zio.blocking.{Blocking, blocking}

import scala.collection.JavaConverters._


final class OSS(unsafeClient: DefaultOSS) extends AliYun.OSSService {

  override def execute[T](f: DefaultOSS => Task[T]): ZIO[Blocking, ClientException, T] =
    blocking(f(unsafeClient)).mapError(e => new ClientException(e))


  override def createBucket(createBucketRequest: OSSCreateBucketRequest): ZIO[Blocking, ClientException, Bucket] =
    execute(client => Task.effect(client.createBucket(createBucketRequest.toJava)))

  override def listBuckets(): ZIO[Blocking, ClientException, Seq[Bucket]] = execute(client => Task.effect {
    client.listBuckets().asScala
  })

  override def listBuckets(listBucketsRequest: OSSListBucketsRequest): ZIO[Blocking, ClientException, BucketList] =
    execute(client => Task.effect(client.listBuckets(listBucketsRequest.toJava)))


  override def doesBucketExist(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Boolean] =
    execute(c => Task.effect(c.doesBucketExist(genericRequest.toJava)))

  override def getBucketLocation(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, String] =
    execute(c => Task.effect(c.getBucketLocation(genericRequest.toJava)))


  override def getBucketInfo(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, BucketInfo] =
    execute(c => Task.effect(c.getBucketInfo(genericRequest.toJava)))


  override def setBucketAcl(setBucketAclRequest: OSSSetBucketAclRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketAcl(setBucketAclRequest.toJava)))


  override def getBucketAcl(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, AccessControlList] =
    execute(c => Task.effect(c.getBucketAcl(genericRequest.toJava)))


  override def deleteBucket(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucket(genericRequest.toJava)))

  override def setBucketTagging(setBucketTaggingRequest: OSSSetBucketTaggingRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketTagging(setBucketTaggingRequest.toJava)))


  override def getBucketTagging(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, TagSet] =
    execute(c => Task.effect(c.getBucketTagging(genericRequest.toJava)))


  override def deleteBucketTagging(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketTagging(genericRequest.toJava)))


  override def setBucketPolicy(setBucketPolicyRequest: OSSSetBucketPolicyRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketPolicy(setBucketPolicyRequest.toJava)))

  override def getBucketPolicy(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, GetBucketPolicyResult] =
    execute(c => Task.effect(c.getBucketPolicy(genericRequest.toJava)))


  override def deleteBucketPolicy(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketPolicy(genericRequest.toJava)))


  override def setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest: OSSSetBucketInventoryConfigurationRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest.toJava)))

  override def getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest: OSSGetBucketInventoryConfigurationRequest): ZIO[Blocking, ClientException, GetBucketInventoryConfigurationResult] =
    execute(c => Task.effect(c.getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest.toJava)))

  override def listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest: OSSListBucketInventoryConfigurationsRequest): ZIO[Blocking, ClientException, ListBucketInventoryConfigurationsResult] =
    execute(c => Task.effect(c.listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest.toJava)))


  override def deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest: OSSDeleteBucketInventoryConfigurationRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest.toJava)))

  override def setBucketLifecycle(setBucketLifecycleRequest: OSSSetBucketLifecycleRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketLifecycle(setBucketLifecycleRequest.toJava)))


  override def getBucketLifecycle(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Seq[LifecycleRule]] =
    execute(c => Task.effect(c.getBucketLifecycle(genericRequest.toJava).asScala))

  override def deleteBucketLifecycle(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketLifecycle(genericRequest.toJava)))

  override def initiateBucketWorm(initiateBucketWormRequest: OSSInitiateBucketWormRequest): ZIO[Blocking, ClientException, InitiateBucketWormResult] =
    execute(c => Task.effect(c.initiateBucketWorm(initiateBucketWormRequest.toJava)))

  override def abortBucketWorm(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.abortBucketWorm(genericRequest.toJava)))

  override def completeBucketWorm(completeBucketWormRequest: OSSCompleteBucketWormRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.completeBucketWorm(completeBucketWormRequest.toJava)))

  override def getBucketWorm(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, GetBucketWormResult] =
    execute(c => Task.effect(c.getBucketWorm(genericRequest.toJava)))

  override def extendBucketWorm(extendBucketWormRequest: OSSExtendBucketWormRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.extendBucketWorm(extendBucketWormRequest.toJava)))


  override def setBucketRequestPayment(setBucketRequestPaymentRequest: OSSSetBucketRequestPaymentRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketRequestPayment(setBucketRequestPaymentRequest.toJava)))


  override def getBucketRequestPayment(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, GetBucketRequestPaymentResult] =
    execute(c => Task.effect(c.getBucketRequestPayment(genericRequest.toJava)))

  override def putObject(putObjectRequest: OSSPutObjectRequest): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(putObjectRequest.toJava)))

  override def putObject(signedUrl: URL, filePath: String, requestHeaders: Map[String, String], useChunkEncoding: Boolean): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(signedUrl, filePath, requestHeaders.asJava, useChunkEncoding)))


  override def putObject(signedUrl: URL, requestContent: InputStream, contentLength: Long, requestHeaders: Map[String, String], useChunkEncoding: Boolean): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(signedUrl, requestContent, contentLength, requestHeaders.asJava, useChunkEncoding)))


  override def getObject(getObjectRequest: OSSGetObjectRequest, file: File): ZIO[Blocking, ClientException, ObjectMetadata] =
    execute(c => Task.effect(c.getObject(getObjectRequest.toJava, file)))

  override def getObject(getObjectRequest: OSSGetObjectRequest): ZIO[Blocking, ClientException, OSSObject] =
    execute(c => Task.effect(c.getObject(getObjectRequest.toJava)))


  override def getObject(getObjectRequest: OSSGetObjectURLRequest): ZIO[Blocking, ClientException, OSSObject] =
    execute(c => Task.effect(c.getObject(getObjectRequest.toJava)))

  override def selectObject(selectObjectRequest: OSSSelectObjectRequest): ZIO[Blocking, ClientException, OSSObject] =
    execute(c => Task.effect(c.selectObject(selectObjectRequest.toJava)))

  override def deleteObject(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteObject(genericRequest.toJava)))

  override def setBucketReferer(setBucketRefererRequest: OSSSetBucketRefererRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketReferer(setBucketRefererRequest.toJava)))

  override def getBucketReferer(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, BucketReferer] =
    execute(c => Task.effect(c.getBucketReferer(genericRequest.toJava)))

  override def setBucketLogging(request: OSSSetBucketLoggingRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketLogging(request.toJava)))


  override def getBucketLogging(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, BucketLoggingResult] =
    execute(c => Task.effect(c.getBucketLogging(genericRequest.toJava)))

  override def setBucketWebsite(setBucketWebSiteRequest: OSSSetBucketWebsiteRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketWebsite(setBucketWebSiteRequest.toJava)))


  override def getBucketWebsite(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, BucketWebsiteResult] =
    execute(c => Task.effect(c.getBucketWebsite(genericRequest.toJava)))

  override def deleteBucketWebsite(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketWebsite(genericRequest.toJava)))

  override def addBucketReplication(addBucketReplicationRequest: OSSAddBucketReplicationRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.addBucketReplication(addBucketReplicationRequest.toJava)))


  override def getBucketReplication(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Seq[ReplicationRule]] =
    execute(c => Task.effect(c.getBucketReplication(genericRequest.toJava).asScala))

  override def getBucketReplicationProgress(getBucketReplicationProgressRequest: OSSGetBucketReplicationProgressRequest): ZIO[Blocking, ClientException, BucketReplicationProgress] =
    execute(c => Task.effect(c.getBucketReplicationProgress(getBucketReplicationProgressRequest.toJava)))


  override def getBucketReplicationLocation(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Seq[String]] =
    execute(c => Task.effect(c.getBucketReplicationLocation(genericRequest.toJava).asScala))

  override def deleteBucketReplication(deleteBucketReplicationRequest: OSSDeleteBucketReplicationRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketReplication(deleteBucketReplicationRequest.toJava)))


  override def setBucketCORS(request: OSSSetBucketCORSRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketCORS(request.toJava)))

  override def getBucketCORSRules(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Seq[SetBucketCORSRequest.CORSRule]] =
    execute(c => Task.effect(c.getBucketCORSRules(genericRequest.toJava).asScala))


  override def deleteBucketCORSRules(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketCORSRules(genericRequest.toJava)))


  override def appendObject(appendObjectRequest: OSSAppendObjectRequest): ZIO[Blocking, ClientException, AppendObjectResult] =
    execute(c => Task.effect(c.appendObject(appendObjectRequest.toJava)))

  override def uploadFile(uploadFileRequest: OSSUploadFileRequest): ZIO[Blocking, ClientException, UploadFileResult] =
    execute(c => Task.effect(c.uploadFile(uploadFileRequest.toJava)))

  override def initiateMultipartUpload(request: OSSInitiateMultipartUploadRequest): ZIO[Blocking, ClientException, InitiateMultipartUploadResult] =
    execute(c => Task.effect(c.initiateMultipartUpload(request.toJava)))

  override def uploadPart(request: OSSUploadPartRequest): ZIO[Blocking, ClientException, UploadPartResult] =
    execute(c => Task.effect(c.uploadPart(request.toJava)))

  override def completeMultipartUpload(request: OSSCompleteMultipartUploadRequest): ZIO[Blocking, ClientException, CompleteMultipartUploadResult] =
    execute(c => Task.effect(c.completeMultipartUpload(request.toJava)))

  override def abortMultipartUpload(request: OSSAbortMultipartUploadRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.abortMultipartUpload(request.toJava)))

  override def listParts(request: OSSListPartsRequest): ZIO[Blocking, ClientException, PartListing] =
    execute(c => Task.effect(c.listParts(request.toJava)))

  override def listMultipartUploads(request: OSSListMultipartUploadsRequest): ZIO[Blocking, ClientException, MultipartUploadListing] =
    execute(c => Task.effect(c.listMultipartUploads(request.toJava)))

  override def downloadFile(downloadFileRequest: OSSDownloadFileRequest): ZIO[Blocking, ClientException, DownloadFileResult] =
    execute(c => Task.effect(c.downloadFile(downloadFileRequest.toJava)))

  override def doesObjectExist(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Boolean] =
    execute(c => Task.effect(c.doesObjectExist(genericRequest.toJava)))


  override def setObjectAcl(setObjectAclRequest: OSSSetObjectAclRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setObjectAcl(setObjectAclRequest.toJava)))

  override def getObjectAcl(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, ObjectAcl] =
    execute(c => Task.effect(c.getObjectAcl(genericRequest.toJava)))


  override def getObjectMetadata(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, ObjectMetadata] =
    execute(c => Task.effect(c.getObjectMetadata(genericRequest.toJava)))


  override def copyObject(copyObjectRequest: OSSCopyObjectRequest): ZIO[Blocking, ClientException, CopyObjectResult] =
    execute(c => Task.effect(c.copyObject(copyObjectRequest.toJava)))


  override def getSimplifiedObjectMeta(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, SimplifiedObjectMeta] =
    execute(c => Task.effect(c.getSimplifiedObjectMeta(genericRequest.toJava)))

  override def restoreObject(restoreObjectRequest: OSSRestoreObjectRequest): ZIO[Blocking, ClientException, RestoreObjectResult] =
    execute(c => Task.effect(c.restoreObject(restoreObjectRequest.toJava)))

  override def listObjects(listObjectsRequest: OSSListObjectsRequest): ZIO[Blocking, ClientException, ObjectListing] =
    execute(c => Task.effect(c.listObjects(listObjectsRequest.toJava)))

  override def createSelectObjectMetadata(createSelectObjectMetadataRequest: OSSCreateSelectObjectMetadataRequest): ZIO[Blocking, ClientException, SelectObjectMetadata] =
    execute(c => Task.effect(c.createSelectObjectMetadata(createSelectObjectMetadataRequest.toJava)))

  override def deleteObjects(deleteObjectsRequest: OSSDeleteObjectsRequest): ZIO[Blocking, ClientException, DeleteObjectsResult] =
    execute(c => Task.effect(c.deleteObjects(deleteObjectsRequest.toJava)))

  override def uploadPartCopy(request: OSSUploadPartCopyRequest): ZIO[Blocking, ClientException, UploadPartCopyResult] =
    execute(c => Task.effect(c.uploadPartCopy(request.toJava)))


  override def createSymlink(createSymlinkRequest: OSSCreateSymlinkRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.createSymlink(createSymlinkRequest.toJava)))


  override def getSymlink(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, OSSSymlink] =
    execute(c => Task.effect(c.getSymlink(genericRequest.toJava)))

  override def setBucketVersioning(setBucketVersioningRequest: OSSSetBucketVersioningRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketVersioning(setBucketVersioningRequest.toJava)))

  override def getBucketVersioning(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, BucketVersioningConfiguration] =
    execute(c => Task.effect(c.getBucketVersioning(genericRequest.toJava)))

  override def listVersions(listVersionsRequest: OSSListVersionsRequest): ZIO[Blocking, ClientException, VersionListing] =
    execute(c => Task.effect(c.listVersions(listVersionsRequest.toJava)))

  override def deleteVersion(deleteVersionRequest: OSSDeleteVersionRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteVersion(deleteVersionRequest.toJava)))

  override def deleteVersions(deleteVersionsRequest: OSSDeleteVersionsRequest): ZIO[Blocking, ClientException, DeleteVersionsResult] =
    execute(c => Task.effect(c.deleteVersions(deleteVersionsRequest.toJava)))

  override def getObjectTagging(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, TagSet] =
    execute(c => Task.effect(c.getObjectTagging(genericRequest.toJava)))


  override def setObjectTagging(setObjectTaggingRequest: OSSSetObjectTaggingRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setObjectTagging(setObjectTaggingRequest.toJava)))


  override def deleteObjectTagging(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteObjectTagging(genericRequest.toJava)))


  override def generatePresignedUrl(request: OSSGeneratePresignedUrlRequest): ZIO[Blocking, ClientException, URL] =
    execute(c => Task.effect(c.generatePresignedUrl(request.toJava)))

  override def setBucketEncryption(setBucketEncryptionRequest: OSSSetBucketEncryptionRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketEncryption(setBucketEncryptionRequest.toJava)))


  override def getBucketEncryption(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, ServerSideEncryptionConfiguration] =
    execute(c => Task.effect(c.getBucketEncryption(genericRequest.toJava)))

  override def deleteBucketEncryption(genericRequest: OSSGenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketEncryption(genericRequest.toJava)))

  override def processObject(processObjectRequest: OSSProcessObjectRequest): ZIO[Blocking, ClientException, GenericResult] =
    execute(c => Task.effect(c.processObject(processObjectRequest.toJava)))
}

object OSS {
  def connect(endpoint: String,
              credentials: AliYunCredentials,
              securityToken: String = null,
              conf: ClientBuilderConfiguration = new ClientBuilderConfiguration): Managed[ConnectionError, AliYun.OSSService] = {
    Managed.makeEffect {
      new OSSClientBuilder().build(endpoint, credentials.accessKeyId, credentials.secret, securityToken, conf)
    }(_.shutdown())
      .map(new OSS(_))
      .mapError(e => ConnectionError(e.getMessage, e.getCause))
  }

}
