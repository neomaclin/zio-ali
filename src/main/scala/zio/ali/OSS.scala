package zio.ali

import java.io.{File, InputStream}
import java.net.URL
import java.util.Date

import com.aliyun.oss.model._
import zio.{Managed, Task, ZIO}
import com.aliyun.oss.{ClientBuilderConfiguration, ClientException, HttpMethod, OSSClientBuilder, OSS => DefaultOSS}
import zio.blocking.{Blocking, blocking}

import scala.collection.JavaConverters._


final class OSS(unsafeClient: DefaultOSS) extends AliYun.OSSService {

  override def execute[T](f: DefaultOSS => Task[T]): ZIO[Blocking, ClientException, T] =
    blocking(f(unsafeClient)).mapError(e => new ClientException(e))


  override def createBucket(createBucketRequest: CreateBucketRequest): ZIO[Blocking, ClientException, Bucket] =
    execute(client => Task.effect(client.createBucket(createBucketRequest)))

  override def createBucket(bucketName: String): ZIO[Blocking, ClientException, Bucket] = {
    execute(client => Task.effect(client.createBucket(bucketName)))
  }

  override def listBuckets(): ZIO[Blocking, ClientException, Seq[Bucket]] = execute(client => Task.effect {
    val bs = client.listBuckets()
    bs.asScala
  })

  override def listBuckets(prefix: String, marker: String, maxKeys: Int): ZIO[Blocking, ClientException, BucketList] =
    execute(client => Task.effect(client.listBuckets(prefix, marker, maxKeys)))

  override def listBuckets(listBucketsRequest: ListBucketsRequest): ZIO[Blocking, ClientException, BucketList] =
    execute(client => Task.effect(client.listBuckets(listBucketsRequest)))

  override def doesBucketExist(bucketName: String): ZIO[Blocking, ClientException, Boolean] =
    execute(c => Task.effect(c.doesBucketExist(bucketName)))

  override def doesBucketExist(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Boolean] =
    execute(c => Task.effect(c.doesBucketExist(genericRequest)))

  override def getBucketLocation(bucketName: String): ZIO[Blocking, ClientException, String] =
    execute(c => Task.effect(c.getBucketLocation(bucketName)))

  override def getBucketLocation(genericRequest: GenericRequest): ZIO[Blocking, ClientException, String] =
    execute(c => Task.effect(c.getBucketLocation(genericRequest)))

  override def getBucketInfo(bucketName: String): ZIO[Blocking, ClientException, BucketInfo] =
    execute(client => Task.effect(client.getBucketInfo(bucketName)))

  override def getBucketInfo(genericRequest: GenericRequest): ZIO[Blocking, ClientException, BucketInfo] =
    execute(c => Task.effect(c.getBucketInfo(genericRequest)))

  override def setBucketAcl(bucketName: String, acl: CannedAccessControlList): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketAcl(bucketName, acl)))

  override def setBucketAcl(setBucketAclRequest: SetBucketAclRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketAcl(setBucketAclRequest)))

  override def getBucketAcl(bucketName: String): ZIO[Blocking, ClientException, AccessControlList] =
    execute(c => Task.effect(c.getBucketAcl(bucketName)))

  override def getBucketAcl(genericRequest: GenericRequest): ZIO[Blocking, ClientException, AccessControlList] =
    execute(c => Task.effect(c.getBucketAcl(genericRequest)))

  override def deleteBucket(bucketName: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucket(bucketName)))

  override def deleteBucket(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucket(genericRequest)))

  override def setBucketTagging(setBucketTaggingRequest: SetBucketTaggingRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketTagging(setBucketTaggingRequest)))

  override def setBucketTagging(bucketName: String, tags: Map[String, String]): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketTagging(bucketName, tags.asJava)))

  override def setBucketTagging(bucketName: String, tagSet: TagSet): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketTagging(bucketName, tagSet)))

  override def getBucketTagging(genericRequest: GenericRequest): ZIO[Blocking, ClientException, TagSet] =
    execute(c => Task.effect(c.getBucketTagging(genericRequest)))

  override def getBucketTagging(bucketName: String): ZIO[Blocking, ClientException, TagSet] =
    execute(c => Task.effect(c.getBucketTagging(bucketName)))

  override def deleteBucketTagging(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketTagging(genericRequest)))

  override def deleteBucketTagging(bucketName: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketTagging(bucketName)))

  override def setBucketPolicy(bucketName: String, policyText: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketPolicy(bucketName, policyText)))

  override def setBucketPolicy(setBucketPolicyRequest: SetBucketPolicyRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketPolicy(setBucketPolicyRequest)))

  override def getBucketPolicy(bucketName: String): ZIO[Blocking, ClientException, GetBucketPolicyResult] =
    execute(c => Task.effect(c.getBucketPolicy(bucketName)))

  override def getBucketPolicy(genericRequest: GenericRequest): ZIO[Blocking, ClientException, GetBucketPolicyResult] =
    execute(c => Task.effect(c.getBucketPolicy(genericRequest)))

  override def deleteBucketPolicy(bucketName: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketPolicy(bucketName)))

  override def deleteBucketPolicy(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketPolicy(genericRequest)))

  override def setBucketInventoryConfiguration(bucketName: String, inventoryConfiguration: InventoryConfiguration): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketInventoryConfiguration(bucketName, inventoryConfiguration)))

  override def setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest: SetBucketInventoryConfigurationRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest)))

  override def getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest: GetBucketInventoryConfigurationRequest): ZIO[Blocking, ClientException, GetBucketInventoryConfigurationResult] =
    execute(c => Task.effect(c.getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest)))

  override def getBucketInventoryConfiguration(bucketName: String, id: String): ZIO[Blocking, ClientException, GetBucketInventoryConfigurationResult] =
    execute(c => Task.effect(c.getBucketInventoryConfiguration(bucketName, id)))

  override def listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest: ListBucketInventoryConfigurationsRequest): ZIO[Blocking, ClientException, ListBucketInventoryConfigurationsResult] =
    execute(c => Task.effect(c.listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest)))

  override def listBucketInventoryConfigurations(bucketName: String): ZIO[Blocking, ClientException, ListBucketInventoryConfigurationsResult] =
    execute(c => Task.effect(c.listBucketInventoryConfigurations(bucketName)))

  override def listBucketInventoryConfigurations(bucketName: String, continuationToken: String): ZIO[Blocking, ClientException, ListBucketInventoryConfigurationsResult] =
    execute(c => Task.effect(c.listBucketInventoryConfigurations(bucketName, continuationToken)))

  override def deleteBucketInventoryConfiguration(bucketName: String, id: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketInventoryConfiguration(bucketName, id)))

  override def deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest: DeleteBucketInventoryConfigurationRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest)))

  override def setBucketLifecycle(setBucketLifecycleRequest: SetBucketLifecycleRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketLifecycle(setBucketLifecycleRequest)))

  override def getBucketLifecycle(bucketName: String): ZIO[Blocking, ClientException, Seq[LifecycleRule]] =
    execute(c => Task.effect(c.getBucketLifecycle(bucketName).asScala))

  override def getBucketLifecycle(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Seq[LifecycleRule]] =
    execute(c => Task.effect(c.getBucketLifecycle(genericRequest).asScala))

  override def deleteBucketLifecycle(bucketName: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketLifecycle(bucketName)))

  override def deleteBucketLifecycle(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketLifecycle(genericRequest)))

  override def initiateBucketWorm(initiateBucketWormRequest: InitiateBucketWormRequest): ZIO[Blocking, ClientException, InitiateBucketWormResult] =
    execute(c => Task.effect(c.initiateBucketWorm(initiateBucketWormRequest)))

  override def initiateBucketWorm(bucketName: String, retentionPeriodInDays: Int): ZIO[Blocking, ClientException, InitiateBucketWormResult] =
    execute(c => Task.effect(c.initiateBucketWorm(bucketName, retentionPeriodInDays)))

  override def abortBucketWorm(bucketName: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.abortBucketWorm(bucketName)))

  override def abortBucketWorm(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.abortBucketWorm(genericRequest)))

  override def completeBucketWorm(bucketName: String, wormId: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.completeBucketWorm(bucketName, wormId)))

  override def completeBucketWorm(completeBucketWormRequest: CompleteBucketWormRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.completeBucketWorm(completeBucketWormRequest)))

  override def getBucketWorm(bucketName: String): ZIO[Blocking, ClientException, GetBucketWormResult] =
    execute(c => Task.effect(c.getBucketWorm(bucketName)))

  override def getBucketWorm(genericRequest: GenericRequest): ZIO[Blocking, ClientException, GetBucketWormResult] =
    execute(c => Task.effect(c.getBucketWorm(genericRequest)))

  override def extendBucketWorm(bucketName: String, wormId: String, retentionPeriodInDays: Int): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.extendBucketWorm(bucketName, wormId, retentionPeriodInDays)))

  override def extendBucketWorm(extendBucketWormRequest: ExtendBucketWormRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.extendBucketWorm(extendBucketWormRequest)))

  override def setBucketRequestPayment(bucketName: String, payer: Payer): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketRequestPayment(bucketName, payer)))

  override def setBucketRequestPayment(setBucketRequestPaymentRequest: SetBucketRequestPaymentRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketRequestPayment(setBucketRequestPaymentRequest)))

  override def getBucketRequestPayment(bucketName: String): ZIO[Blocking, ClientException, GetBucketRequestPaymentResult] =
    execute(c => Task.effect(c.getBucketRequestPayment(bucketName)))

  override def getBucketRequestPayment(genericRequest: GenericRequest): ZIO[Blocking, ClientException, GetBucketRequestPaymentResult] =
    execute(c => Task.effect(c.getBucketRequestPayment(genericRequest)))

  override def putObject(bucketName: String, key: String, input: InputStream): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(bucketName, key, input)))

  override def putObject(bucketName: String, key: String, input: InputStream, metadata: ObjectMetadata): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(bucketName, key, input, metadata)))

  override def putObject(bucketName: String, key: String, file: File, metadata: ObjectMetadata): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(bucketName, key, file, metadata)))

  override def putObject(bucketName: String, key: String, file: File): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(bucketName, key, file)))

  override def putObject(putObjectRequest: PutObjectRequest): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(putObjectRequest)))

  override def putObject(signedUrl: URL, filePath: String, requestHeaders: Map[String, String]): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(signedUrl, filePath, requestHeaders.asJava)))

  override def putObject(signedUrl: URL, filePath: String, requestHeaders: Map[String, String], useChunkEncoding: Boolean): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(signedUrl, filePath, requestHeaders.asJava, useChunkEncoding)))

  override def putObject(signedUrl: URL, requestContent: InputStream, contentLength: Long, requestHeaders: Map[String, String]): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(signedUrl, requestContent, contentLength, requestHeaders.asJava)))

  override def putObject(signedUrl: URL, requestContent: InputStream, contentLength: Long, requestHeaders: Map[String, String], useChunkEncoding: Boolean): ZIO[Blocking, ClientException, PutObjectResult] =
    execute(c => Task.effect(c.putObject(signedUrl, requestContent, contentLength, requestHeaders.asJava, useChunkEncoding)))

  override def getObject(bucketName: String, key: String): ZIO[Blocking, ClientException, OSSObject] =
    execute(c => Task.effect(c.getObject(bucketName, key)))

  override def getObject(getObjectRequest: GetObjectRequest, file: File): ZIO[Blocking, ClientException, ObjectMetadata] =
    execute(c => Task.effect(c.getObject(getObjectRequest, file)))

  override def getObject(getObjectRequest: GetObjectRequest): ZIO[Blocking, ClientException, OSSObject] =
    execute(c => Task.effect(c.getObject(getObjectRequest)))

  override def selectObject(selectObjectRequest: SelectObjectRequest): ZIO[Blocking, ClientException, OSSObject] =
    execute(c => Task.effect(c.selectObject(selectObjectRequest)))

  override def getObject(signedUrl: URL, requestHeaders: Map[String, String]): ZIO[Blocking, ClientException, OSSObject] =
    execute(c => Task.effect(c.getObject(signedUrl, requestHeaders.asJava)))

  override def deleteObject(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteObject(genericRequest)))

  override def deleteObject(bucketName: String, key: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteObject(bucketName, key)))

  override def setBucketReferer(bucketName: String, referer: BucketReferer): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketReferer(bucketName, referer)))

  override def setBucketReferer(setBucketRefererRequest: SetBucketRefererRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketReferer(setBucketRefererRequest)))

  override def getBucketReferer(genericRequest: GenericRequest): ZIO[Blocking, ClientException, BucketReferer] =
    execute(c => Task.effect(c.getBucketReferer(genericRequest)))

  override def getBucketReferer(bucketName: String): ZIO[Blocking, ClientException, BucketReferer] =
    execute(c => Task.effect(c.getBucketReferer(bucketName)))

  override def setBucketLogging(request: SetBucketLoggingRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketLogging(request)))

  override def getBucketLogging(bucketName: String): ZIO[Blocking, ClientException, BucketLoggingResult] =
    execute(c => Task.effect(c.getBucketLogging(bucketName)))

  override def getBucketLogging(genericRequest: GenericRequest): ZIO[Blocking, ClientException, BucketLoggingResult] =
    execute(c => Task.effect(c.getBucketLogging(genericRequest)))

  override def setBucketWebsite(setBucketWebSiteRequest: SetBucketWebsiteRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketWebsite(setBucketWebSiteRequest)))

  override def getBucketWebsite(bucketName: String): ZIO[Blocking, ClientException, BucketWebsiteResult] =
    execute(c => Task.effect(c.getBucketWebsite(bucketName)))

  override def getBucketWebsite(genericRequest: GenericRequest): ZIO[Blocking, ClientException, BucketWebsiteResult] =
    execute(c => Task.effect(c.getBucketWebsite(genericRequest)))

  override def deleteBucketWebsite(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketWebsite(genericRequest)))

  override def deleteBucketWebsite(bucketName: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketWebsite(bucketName)))

  override def addBucketReplication(addBucketReplicationRequest: AddBucketReplicationRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.addBucketReplication(addBucketReplicationRequest)))

  override def getBucketReplication(bucketName: String): ZIO[Blocking, ClientException, Seq[ReplicationRule]] =
    execute(c => Task.effect(c.getBucketReplication(bucketName).asScala))

  override def getBucketReplication(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Seq[ReplicationRule]] =
    execute(c => Task.effect(c.getBucketReplication(genericRequest).asScala))

  override def getBucketReplicationProgress(getBucketReplicationProgressRequest: GetBucketReplicationProgressRequest): ZIO[Blocking, ClientException, BucketReplicationProgress] =
    execute(c => Task.effect(c.getBucketReplicationProgress(getBucketReplicationProgressRequest)))

  override def getBucketReplicationProgress(bucketName: String, replicationRuleID: String): ZIO[Blocking, ClientException, BucketReplicationProgress] =
    execute(c => Task.effect(c.getBucketReplicationProgress(bucketName, replicationRuleID)))

  override def getBucketReplicationLocation(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Seq[String]] =
    execute(c => Task.effect(c.getBucketReplicationLocation(genericRequest).asScala))

  override def getBucketReplicationLocation(bucketName: String): ZIO[Blocking, ClientException, Seq[String]] =
    execute(c => Task.effect(c.getBucketReplicationLocation(bucketName).asScala))

  override def deleteBucketReplication(deleteBucketReplicationRequest: DeleteBucketReplicationRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketReplication(deleteBucketReplicationRequest)))

  override def deleteBucketReplication(bucketName: String, replicationRuleID: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketReplication(bucketName, replicationRuleID)))

  override def setBucketCORS(request: SetBucketCORSRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketCORS(request)))

  override def getBucketCORSRules(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Seq[SetBucketCORSRequest.CORSRule]] =
    execute(c => Task.effect(c.getBucketCORSRules(genericRequest).asScala))

  override def getBucketCORSRules(bucketName: String): ZIO[Blocking, ClientException, Seq[SetBucketCORSRequest.CORSRule]] =
    execute(c => Task.effect(c.getBucketCORSRules(bucketName).asScala))

  override def deleteBucketCORSRules(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketCORSRules(genericRequest)))

  override def deleteBucketCORSRules(bucketName: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketCORSRules(bucketName)))

  override def appendObject(appendObjectRequest: AppendObjectRequest): ZIO[Blocking, ClientException, AppendObjectResult] =
    execute(c => Task.effect(c.appendObject(appendObjectRequest)))

  override def uploadFile(uploadFileRequest: UploadFileRequest): ZIO[Blocking, ClientException, UploadFileResult] =
    execute(c => Task.effect(c.uploadFile(uploadFileRequest)))

  override def initiateMultipartUpload(request: InitiateMultipartUploadRequest): ZIO[Blocking, ClientException, InitiateMultipartUploadResult] =
    execute(c => Task.effect(c.initiateMultipartUpload(request)))

  override def uploadPart(request: UploadPartRequest): ZIO[Blocking, ClientException, UploadPartResult] =
    execute(c => Task.effect(c.uploadPart(request)))

  override def completeMultipartUpload(request: CompleteMultipartUploadRequest): ZIO[Blocking, ClientException, CompleteMultipartUploadResult] =
    execute(c => Task.effect(c.completeMultipartUpload(request)))

  override def abortMultipartUpload(request: AbortMultipartUploadRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.abortMultipartUpload(request)))

  override def listParts(request: ListPartsRequest): ZIO[Blocking, ClientException, PartListing] =
    execute(c => Task.effect(c.listParts(request)))

  override def listMultipartUploads(request: ListMultipartUploadsRequest): ZIO[Blocking, ClientException, MultipartUploadListing] =
    execute(c => Task.effect(c.listMultipartUploads(request)))

  override def downloadFile(downloadFileRequest: DownloadFileRequest): ZIO[Blocking, ClientException, DownloadFileResult] =
    execute(c => Task.effect(c.downloadFile(downloadFileRequest)))

  override def doesObjectExist(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Boolean] =
    execute(c => Task.effect(c.doesObjectExist(genericRequest)))

  override def doesObjectExist(bucketName: String, key: String): ZIO[Blocking, ClientException, Boolean] =
    execute(c => Task.effect(c.doesObjectExist(bucketName, key)))

  override def setObjectAcl(bucketName: String, key: String, cannedAcl: CannedAccessControlList): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setObjectAcl(bucketName, key, cannedAcl)))

  override def setObjectAcl(setObjectAclRequest: SetObjectAclRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setObjectAcl(setObjectAclRequest)))

  override def getObjectAcl(genericRequest: GenericRequest): ZIO[Blocking, ClientException, ObjectAcl] =
    execute(c => Task.effect(c.getObjectAcl(genericRequest)))

  override def getObjectAcl(bucketName: String, key: String): ZIO[Blocking, ClientException, ObjectAcl] =
    execute(c => Task.effect(c.getObjectAcl(bucketName, key)))

  override def getObjectMetadata(genericRequest: GenericRequest): ZIO[Blocking, ClientException, ObjectMetadata] =
    execute(c => Task.effect(c.getObjectMetadata(genericRequest)))

  override def getObjectMetadata(bucketName: String, key: String): ZIO[Blocking, ClientException, ObjectMetadata] =
    execute(c => Task.effect(c.getObjectMetadata(bucketName, key)))

  override def copyObject(copyObjectRequest: CopyObjectRequest): ZIO[Blocking, ClientException, CopyObjectResult] =
    execute(c => Task.effect(c.copyObject(copyObjectRequest)))

  override def copyObject(sourceBucketName: String, sourceKey: String, destinationBucketName: String, destinationKey: String): ZIO[Blocking, ClientException, CopyObjectResult] =
    execute(c => Task.effect(c.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey)))

  override def getSimplifiedObjectMeta(genericRequest: GenericRequest): ZIO[Blocking, ClientException, SimplifiedObjectMeta] =
    execute(c => Task.effect(c.getSimplifiedObjectMeta(genericRequest)))

  override def restoreObject(bucketName: String, key: String): ZIO[Blocking, ClientException, RestoreObjectResult] =
    execute(c => Task.effect(c.restoreObject(bucketName, key)))

  override def restoreObject(genericRequest: GenericRequest): ZIO[Blocking, ClientException, RestoreObjectResult] =
    execute(c => Task.effect(c.restoreObject(genericRequest)))

  override def restoreObject(bucketName: String, key: String, restoreConfiguration: RestoreConfiguration): ZIO[Blocking, ClientException, RestoreObjectResult] =
    execute(c => Task.effect(c.restoreObject(bucketName, key, restoreConfiguration)))

  override def restoreObject(restoreObjectRequest: RestoreObjectRequest): ZIO[Blocking, ClientException, RestoreObjectResult] =
    execute(c => Task.effect(c.restoreObject(restoreObjectRequest)))

  override def listObjects(bucketName: String): ZIO[Blocking, ClientException, ObjectListing] =
    execute(c => Task.effect(c.listObjects(bucketName)))

  override def listObjects(bucketName: String, prefix: String): ZIO[Blocking, ClientException, ObjectListing] =
    execute(c => Task.effect(c.listObjects(bucketName, prefix)))

  override def listObjects(listObjectsRequest: ListObjectsRequest): ZIO[Blocking, ClientException, ObjectListing] =
    execute(c => Task.effect(c.listObjects(listObjectsRequest)))

  override def createSelectObjectMetadata(createSelectObjectMetadataRequest: CreateSelectObjectMetadataRequest): ZIO[Blocking, ClientException, SelectObjectMetadata] =
    execute(c => Task.effect(c.createSelectObjectMetadata(createSelectObjectMetadataRequest)))

  override def deleteObjects(deleteObjectsRequest: DeleteObjectsRequest): ZIO[Blocking, ClientException, DeleteObjectsResult] =
    execute(c => Task.effect(c.deleteObjects(deleteObjectsRequest)))

  override def uploadPartCopy(request: UploadPartCopyRequest): ZIO[Blocking, ClientException, UploadPartCopyResult] =
    execute(c => Task.effect(c.uploadPartCopy(request)))

  override def createSymlink(bucketName: String, symlink: String, target: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.createSymlink(bucketName, symlink, target)))

  override def createSymlink(createSymlinkRequest: CreateSymlinkRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.createSymlink(createSymlinkRequest)))

  override def getSymlink(bucketName: String, symlink: String): ZIO[Blocking, ClientException, OSSSymlink] =
    execute(c => Task.effect(c.getSymlink(bucketName, symlink)))

  override def getSymlink(genericRequest: GenericRequest): ZIO[Blocking, ClientException, OSSSymlink] =
    execute(c => Task.effect(c.getSymlink(genericRequest)))

  override def setBucketVersioning(setBucketVersioningRequest: SetBucketVersioningRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketVersioning(setBucketVersioningRequest)))

  override def getBucketVersioning(bucketName: String): ZIO[Blocking, ClientException, BucketVersioningConfiguration] =
    execute(c => Task.effect(c.getBucketVersioning(bucketName)))

  override def getBucketVersioning(genericRequest: GenericRequest): ZIO[Blocking, ClientException, BucketVersioningConfiguration] =
    execute(c => Task.effect(c.getBucketVersioning(genericRequest)))

  override def listVersions(bucketName: String, prefix: String): ZIO[Blocking, ClientException, VersionListing] =
    execute(c => Task.effect(c.listVersions(bucketName, prefix)))

  override def listVersions(bucketName: String, prefix: String, keyMarker: String, versionIdMarker: String, delimiter: String, maxResults: Integer): ZIO[Blocking, ClientException, VersionListing] =
    execute(c => Task.effect(c.listVersions(bucketName, prefix, keyMarker, versionIdMarker, delimiter, maxResults)))

  override def listVersions(listVersionsRequest: ListVersionsRequest): ZIO[Blocking, ClientException, VersionListing] =
    execute(c => Task.effect(c.listVersions(listVersionsRequest)))

  override def deleteVersion(bucketName: String, key: String, versionId: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteVersion(bucketName, key, versionId)))

  override def deleteVersion(deleteVersionRequest: DeleteVersionRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteVersion(deleteVersionRequest)))

  override def deleteVersions(deleteVersionsRequest: DeleteVersionsRequest): ZIO[Blocking, ClientException, DeleteVersionsResult] =
    execute(c => Task.effect(c.deleteVersions(deleteVersionsRequest)))

  override def getObjectTagging(bucketName: String, key: String): ZIO[Blocking, ClientException, TagSet] =
    execute(c => Task.effect(c.getObjectTagging(bucketName, key)))

  override def getObjectTagging(genericRequest: GenericRequest): ZIO[Blocking, ClientException, TagSet] =
    execute(c => Task.effect(c.getObjectTagging(genericRequest)))

  override def setObjectTagging(bucketName: String, key: String, tags: Map[String, String]): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setObjectTagging(bucketName, key, tags.asJava)))

  override def setObjectTagging(bucketName: String, key: String, tagSet: TagSet): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setObjectTagging(bucketName, key, tagSet)))

  override def setObjectTagging(setObjectTaggingRequest: SetObjectTaggingRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setObjectTagging(setObjectTaggingRequest)))

  override def deleteObjectTagging(bucketName: String, key: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteObjectTagging(bucketName, key)))

  override def deleteObjectTagging(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteObjectTagging(genericRequest)))

  override def generatePresignedUrl(bucketName: String, key: String, expiration: Date): ZIO[Blocking, ClientException, URL] =
    execute(c => Task.effect(c.generatePresignedUrl(bucketName, key, expiration)))

  override def generatePresignedUrl(bucketName: String, key: String, expiration: Date, method: HttpMethod): ZIO[Blocking, ClientException, URL] =
    execute(c => Task.effect(c.generatePresignedUrl(bucketName, key, expiration, method)))

  override def generatePresignedUrl(request: GeneratePresignedUrlRequest): ZIO[Blocking, ClientException, URL] =
    execute(c => Task.effect(c.generatePresignedUrl(request)))

  override def setBucketEncryption(setBucketEncryptionRequest: SetBucketEncryptionRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.setBucketEncryption(setBucketEncryptionRequest)))

  override def getBucketEncryption(bucketName: String): ZIO[Blocking, ClientException, ServerSideEncryptionConfiguration] =
    execute(c => Task.effect(c.getBucketEncryption(bucketName)))

  override def getBucketEncryption(genericRequest: GenericRequest): ZIO[Blocking, ClientException, ServerSideEncryptionConfiguration] =
    execute(c => Task.effect(c.getBucketEncryption(genericRequest)))

  override def deleteBucketEncryption(bucketName: String): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketEncryption(bucketName)))

  override def deleteBucketEncryption(genericRequest: GenericRequest): ZIO[Blocking, ClientException, Unit] =
    execute(c => Task.effect(c.deleteBucketEncryption(genericRequest)))

  override def processObject(processObjectRequest: ProcessObjectRequest): ZIO[Blocking, ClientException, GenericResult] =
    execute(c => Task.effect(c.processObject(processObjectRequest)))
}

object OSS {
  def connect(endpoint: String, credentials: AliYunCredentials, securityToken: String = null, conf: ClientBuilderConfiguration = new ClientBuilderConfiguration): Managed[ConnectionError, AliYun.OSSService] = {
    Managed.makeEffect {
      new OSSClientBuilder().build(endpoint, credentials.accessKeyId, credentials.secret, securityToken, conf)
    }(_.shutdown()).map(new OSS(_)).mapError(e => ConnectionError(e.getMessage, e.getCause))
  }

  //  def connect(endpoint: String, credentials: AliYunCredentials,conf: ClientBuilderConfiguration)
}
