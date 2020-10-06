package zio


import java.io.{File, InputStream}
import java.net.URL
import java.util.Date

import com.aliyun.oss.model.SetBucketCORSRequest.CORSRule
import com.aliyun.oss.model._
import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.exceptions.ClientException
import com.aliyun.oss.{HttpMethod, ClientException => OSSClientException, OSS => OSSClient}
import zio.blocking.Blocking

package object ali {
  type AliYun          = Has[AliYun.Service]
  type AliYunOSS = Has[AliYun.OSSService]

  object AliYun {

    trait Service {
      def sendSMS(request: SMS.Request, templateParamValue: String): ZIO[Blocking, ClientException, SMS.Response]
      def execute[T](f: DefaultAcsClient => Task[T]): ZIO[Blocking, ClientException, T]
    }

    trait OSSService{
      def execute[T](f: OSSClient => Task[T]): ZIO[Blocking, OSSClientException, T]
      def createBucket(createBucketRequest: CreateBucketRequest): ZIO[Blocking,OSSClientException,Bucket]
      def createBucket(bucketName: String): ZIO[Blocking,OSSClientException,Bucket]
      def listBuckets(): ZIO[Blocking,OSSClientException,Seq[Bucket]]
      def listBuckets(prefix: String,marker: String,maxKeys: Int): ZIO[Blocking, OSSClientException, BucketList]
      def listBuckets(listBucketsRequest: ListBucketsRequest): ZIO[Blocking, OSSClientException, BucketList]
      def doesBucketExist(bucketName: String): ZIO[Blocking, OSSClientException, Boolean]
      def doesBucketExist(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Boolean]
      def getBucketLocation(bucketName: String): ZIO[Blocking, OSSClientException, String]
      def getBucketLocation(genericRequest: GenericRequest): ZIO[Blocking,OSSClientException, String]
      def getBucketInfo(bucketName: String): ZIO[Blocking,OSSClientException,BucketInfo]
      def getBucketInfo(genericRequest: GenericRequest): ZIO[Blocking,OSSClientException,BucketInfo]
      def setBucketAcl(bucketName: String, acl: CannedAccessControlList): ZIO[Blocking,OSSClientException,Unit]
      def setBucketAcl(setBucketAclRequest: SetBucketAclRequest): ZIO[Blocking,OSSClientException,Unit]
      def getBucketAcl(bucketName: String): ZIO[Blocking, OSSClientException, AccessControlList]
      def getBucketAcl(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, AccessControlList]
      def deleteBucket(bucketName: String): ZIO[Blocking, OSSClientException, Unit]
      def deleteBucket(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketTagging(setBucketTaggingRequest: SetBucketTaggingRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketTagging(bucketName: String, tags: Map[String, String]): ZIO[Blocking, OSSClientException, Unit]
      def setBucketTagging(bucketName: String, tagSet: TagSet): ZIO[Blocking, OSSClientException, Unit]
      def getBucketTagging(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, TagSet]
      def getBucketTagging(bucketName: String): ZIO[Blocking, OSSClientException, TagSet]
      def deleteBucketTagging(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def deleteBucketTagging(bucketName: String): ZIO[Blocking, OSSClientException, Unit]
      def setBucketPolicy(bucketName: String, policyText: String): ZIO[Blocking, OSSClientException, Unit]
      def setBucketPolicy(setBucketPolicyRequest: SetBucketPolicyRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketPolicy(bucketName: String): ZIO[Blocking, OSSClientException, GetBucketPolicyResult]
      def getBucketPolicy(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, GetBucketPolicyResult]
      def deleteBucketPolicy(bucketName: String): ZIO[Blocking, OSSClientException, Unit]
      def deleteBucketPolicy(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketInventoryConfiguration(bucketName: String, inventoryConfiguration: InventoryConfiguration): ZIO[Blocking,OSSClientException, Unit]
      def setBucketInventoryConfiguration(setBucketInventoryConfigurationRequest: SetBucketInventoryConfigurationRequest): ZIO[Blocking,OSSClientException, Unit]
      def getBucketInventoryConfiguration(getBucketInventoryConfigurationRequest: GetBucketInventoryConfigurationRequest): ZIO[Blocking, OSSClientException, GetBucketInventoryConfigurationResult]
      def getBucketInventoryConfiguration(bucketName: String, id: String):ZIO[Blocking, OSSClientException, GetBucketInventoryConfigurationResult]
      def listBucketInventoryConfigurations(listBucketInventoryConfigurationsRequest: ListBucketInventoryConfigurationsRequest): ZIO[Blocking, OSSClientException, ListBucketInventoryConfigurationsResult]
      def listBucketInventoryConfigurations(bucketName: String): ZIO[Blocking, OSSClientException, ListBucketInventoryConfigurationsResult]
      def listBucketInventoryConfigurations(bucketName: String, continuationToken: String): ZIO[Blocking, OSSClientException, ListBucketInventoryConfigurationsResult]
      def deleteBucketInventoryConfiguration(bucketName: String, id: String): ZIO[Blocking, OSSClientException, Unit]
      def deleteBucketInventoryConfiguration(deleteBucketInventoryConfigurationRequest: DeleteBucketInventoryConfigurationRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketLifecycle(setBucketLifecycleRequest: SetBucketLifecycleRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketLifecycle(bucketName: String): ZIO[Blocking, OSSClientException, Seq[LifecycleRule]]
      def getBucketLifecycle(genericRequest: GenericRequest):ZIO[Blocking, OSSClientException, Seq[LifecycleRule]]
      def deleteBucketLifecycle(bucketName: String): ZIO[Blocking, OSSClientException, Unit]
      def deleteBucketLifecycle(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def initiateBucketWorm(initiateBucketWormRequest: InitiateBucketWormRequest): ZIO[Blocking, OSSClientException,InitiateBucketWormResult]
      def initiateBucketWorm(bucketName: String, retentionPeriodInDays: Int): ZIO[Blocking, OSSClientException,InitiateBucketWormResult]
      def abortBucketWorm(bucketName: String): ZIO[Blocking, OSSClientException, Unit]
      def abortBucketWorm(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def completeBucketWorm(bucketName: String, wormId: String): ZIO[Blocking, OSSClientException, Unit]
      def completeBucketWorm(completeBucketWormRequest: CompleteBucketWormRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketWorm(bucketName: String): ZIO[Blocking, OSSClientException, GetBucketWormResult]
      def getBucketWorm(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, GetBucketWormResult]
      def extendBucketWorm(bucketName: String, wormId: String, retentionPeriodInDays: Int): ZIO[Blocking, OSSClientException, Unit]
      def extendBucketWorm(extendBucketWormRequest: ExtendBucketWormRequest): ZIO[Blocking, OSSClientException, Unit]
      def setBucketRequestPayment(bucketName: String, payer: Payer): ZIO[Blocking, OSSClientException, Unit]
      def setBucketRequestPayment(setBucketRequestPaymentRequest: SetBucketRequestPaymentRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketRequestPayment(bucketName: String): ZIO[Blocking,OSSClientException,GetBucketRequestPaymentResult]
      def getBucketRequestPayment(genericRequest: GenericRequest): ZIO[Blocking,OSSClientException,GetBucketRequestPaymentResult]
      def putObject(bucketName: String, key: String, input: InputStream): ZIO[Blocking, OSSClientException, PutObjectResult]
      def putObject(bucketName: String, key: String, input: InputStream, metadata: ObjectMetadata): ZIO[Blocking, OSSClientException, PutObjectResult]
      def putObject(bucketName: String, key: String, file: File, metadata: ObjectMetadata): ZIO[Blocking, OSSClientException,PutObjectResult]
      def putObject(bucketName: String, key: String, file: File): ZIO[Blocking, OSSClientException,PutObjectResult]
      def putObject(putObjectRequest: PutObjectRequest): ZIO[Blocking, OSSClientException, PutObjectResult]
      def putObject(signedUrl: URL, filePath: String,requestHeaders:Map[String,String]): ZIO[Blocking,OSSClientException,PutObjectResult]
      def putObject(signedUrl: URL, filePath: String,requestHeaders:Map[String,String], useChunkEncoding: Boolean): ZIO[Blocking,OSSClientException,PutObjectResult]
      def putObject(signedUrl: URL, requestContent: InputStream, contentLength: Long, requestHeaders: Map[String, String]): ZIO[Blocking,OSSClientException,PutObjectResult]
      def putObject(signedUrl: URL, requestContent: InputStream, contentLength: Long, requestHeaders: Map[String, String], useChunkEncoding: Boolean):ZIO[Blocking,OSSClientException,PutObjectResult]
      def getObject(bucketName: String, key: String): ZIO[Blocking, OSSClientException, OSSObject]
      def getObject(getObjectRequest: GetObjectRequest, file: File): ZIO[Blocking, OSSClientException, ObjectMetadata]
      def getObject(getObjectRequest: GetObjectRequest): ZIO[Blocking, OSSClientException, OSSObject]
      def selectObject(selectObjectRequest: SelectObjectRequest): ZIO[Blocking, OSSClientException, OSSObject]
      def getObject(signedUrl: URL, requestHeaders: Map[String, String]): ZIO[Blocking,OSSClientException,OSSObject]
      def deleteObject(genericRequest: GenericRequest): ZIO[Blocking,OSSClientException,Unit]
      def deleteObject(bucketName: String, key: String): ZIO[Blocking,OSSClientException,Unit]
      def setBucketReferer(bucketName: String, referer: BucketReferer): ZIO[Blocking, OSSClientException, Unit]
      def setBucketReferer(setBucketRefererRequest: SetBucketRefererRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketReferer(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, BucketReferer]
      def getBucketReferer(bucketName: String): ZIO[Blocking, OSSClientException, BucketReferer]
      def setBucketLogging(request: SetBucketLoggingRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketLogging(bucketName: String): ZIO[Blocking, OSSClientException, BucketLoggingResult]
      def getBucketLogging(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, BucketLoggingResult]
      def setBucketWebsite(setBucketWebSiteRequest: SetBucketWebsiteRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketWebsite(bucketName: String): ZIO[Blocking, OSSClientException, BucketWebsiteResult]
      def getBucketWebsite(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, BucketWebsiteResult]
      def deleteBucketWebsite(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def deleteBucketWebsite(bucketName: String): ZIO[Blocking, OSSClientException, Unit]
      def addBucketReplication(addBucketReplicationRequest: AddBucketReplicationRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketReplication(bucketName: String): ZIO[Blocking, OSSClientException, Seq[ReplicationRule]]
      def getBucketReplication(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Seq[ReplicationRule]]
      def getBucketReplicationProgress(getBucketReplicationProgressRequest: GetBucketReplicationProgressRequest): ZIO[Blocking, OSSClientException, BucketReplicationProgress]
      def getBucketReplicationProgress(bucketName: String, replicationRuleID: String): ZIO[Blocking, OSSClientException, BucketReplicationProgress]
      def getBucketReplicationLocation(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Seq[String]]
      def getBucketReplicationLocation(bucketName: String): ZIO[Blocking, OSSClientException, Seq[String]]
      def deleteBucketReplication(deleteBucketReplicationRequest: DeleteBucketReplicationRequest): ZIO[Blocking, OSSClientException, Unit]
      def deleteBucketReplication(bucketName: String, replicationRuleID: String): ZIO[Blocking, OSSClientException, Unit]
      def setBucketCORS(request: SetBucketCORSRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketCORSRules(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException,Seq[CORSRule]]
      def getBucketCORSRules(bucketName: String): ZIO[Blocking, OSSClientException,Seq[CORSRule]]
      def deleteBucketCORSRules(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def deleteBucketCORSRules(bucketName: String): ZIO[Blocking, OSSClientException, Unit]
      def appendObject(appendObjectRequest: AppendObjectRequest): ZIO[Blocking, OSSClientException, AppendObjectResult]
      def uploadFile(uploadFileRequest: UploadFileRequest): ZIO[Blocking, OSSClientException, UploadFileResult]
      def initiateMultipartUpload(request: InitiateMultipartUploadRequest): ZIO[Blocking, OSSClientException, InitiateMultipartUploadResult]
      def uploadPart(request: UploadPartRequest): ZIO[Blocking, OSSClientException, UploadPartResult]
      def completeMultipartUpload(request: CompleteMultipartUploadRequest): ZIO[Blocking, OSSClientException, CompleteMultipartUploadResult]
      def abortMultipartUpload(request: AbortMultipartUploadRequest): ZIO[Blocking, OSSClientException, Unit]
      def listParts(request: ListPartsRequest): ZIO[Blocking, OSSClientException, PartListing]
      def listMultipartUploads(request: ListMultipartUploadsRequest): ZIO[Blocking, OSSClientException, MultipartUploadListing]
      def downloadFile(downloadFileRequest: DownloadFileRequest): ZIO[Blocking, OSSClientException, DownloadFileResult]
      def doesObjectExist(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Boolean]
      def doesObjectExist(bucketName: String, key: String): ZIO[Blocking, OSSClientException, Boolean]
      def setObjectAcl(bucketName: String, key: String, cannedAcl: CannedAccessControlList): ZIO[Blocking, OSSClientException, Unit]
      def setObjectAcl(setObjectAclRequest: SetObjectAclRequest): ZIO[Blocking, OSSClientException, Unit]
      def getObjectAcl(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, ObjectAcl]
      def getObjectAcl(bucketName: String, key: String): ZIO[Blocking, OSSClientException, ObjectAcl]
      def getObjectMetadata(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, ObjectMetadata]
      def getObjectMetadata(bucketName: String, key: String): ZIO[Blocking, OSSClientException, ObjectMetadata]
      def copyObject(copyObjectRequest: CopyObjectRequest): ZIO[Blocking, OSSClientException, CopyObjectResult]
      def copyObject(sourceBucketName: String, sourceKey: String, destinationBucketName: String,
                     destinationKey: String): ZIO[Blocking, OSSClientException, CopyObjectResult]
      def getSimplifiedObjectMeta(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, SimplifiedObjectMeta]
      def restoreObject(bucketName: String, key: String): ZIO[Blocking, OSSClientException, RestoreObjectResult]
      def restoreObject(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, RestoreObjectResult]
      def restoreObject(bucketName: String, key: String, restoreConfiguration: RestoreConfiguration): ZIO[Blocking, OSSClientException, RestoreObjectResult]
      def restoreObject(restoreObjectRequest: RestoreObjectRequest): ZIO[Blocking ,OSSClientException, RestoreObjectResult]
      def listObjects(bucketName: String): ZIO[Blocking, OSSClientException, ObjectListing]
      def listObjects(bucketName: String, prefix: String): ZIO[Blocking, OSSClientException, ObjectListing]
      def listObjects(listObjectsRequest: ListObjectsRequest): ZIO[Blocking, OSSClientException, ObjectListing]
      def createSelectObjectMetadata(createSelectObjectMetadataRequest: CreateSelectObjectMetadataRequest): ZIO[Blocking, OSSClientException,SelectObjectMetadata]
      def deleteObjects(deleteObjectsRequest: DeleteObjectsRequest): ZIO[Blocking, OSSClientException, DeleteObjectsResult]
      def uploadPartCopy(request: UploadPartCopyRequest): ZIO[Blocking, OSSClientException, UploadPartCopyResult]
      def createSymlink(bucketName: String, symlink: String, target: String): ZIO[Blocking, OSSClientException, Unit]
      def createSymlink(createSymlinkRequest: CreateSymlinkRequest): ZIO[Blocking, OSSClientException, Unit]
      def getSymlink(bucketName: String, symlink: String): ZIO[Blocking, OSSClientException, OSSSymlink]
      def getSymlink(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, OSSSymlink]
      def setBucketVersioning(setBucketVersioningRequest: SetBucketVersioningRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketVersioning(bucketName: String): ZIO[Blocking, OSSClientException, BucketVersioningConfiguration]
      def getBucketVersioning(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, BucketVersioningConfiguration]
      def listVersions(bucketName: String, prefix: String): ZIO[Blocking, OSSClientException, VersionListing]
      def listVersions(bucketName: String, prefix: String,
                       keyMarker: String, versionIdMarker: String, delimiter: String, maxResults: Integer): ZIO[Blocking, OSSClientException, VersionListing]
      def listVersions(listVersionsRequest: ListVersionsRequest): ZIO[Blocking, OSSClientException, VersionListing]
      def deleteVersion(bucketName: String, key: String, versionId: String): ZIO[Blocking, OSSClientException, Unit]
      def deleteVersion(deleteVersionRequest: DeleteVersionRequest): ZIO[Blocking, OSSClientException, Unit]
      def deleteVersions(deleteVersionsRequest: DeleteVersionsRequest): ZIO[Blocking, OSSClientException, DeleteVersionsResult]
      def getObjectTagging(bucketName: String, key: String): ZIO[Blocking, OSSClientException, TagSet]
      def getObjectTagging(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, TagSet]
      def setObjectTagging(bucketName: String, key: String, tags: Map[String, String]): ZIO[Blocking, OSSClientException, Unit]
      def setObjectTagging(bucketName: String, key: String, tagSet: TagSet): ZIO[Blocking, OSSClientException, Unit]
      def setObjectTagging(setObjectTaggingRequest: SetObjectTaggingRequest): ZIO[Blocking, OSSClientException, Unit]
      def deleteObjectTagging(bucketName: String, key: String): ZIO[Blocking, OSSClientException, Unit]
      def deleteObjectTagging(genericRequest: GenericRequest): ZIO[Blocking, OSSClientException, Unit]
      def generatePresignedUrl(bucketName: String, key: String, expiration: Date): ZIO[Blocking, OSSClientException, URL]
      def generatePresignedUrl(bucketName: String, key: String, expiration: Date, method: HttpMethod): ZIO[Blocking, OSSClientException, URL]
      def generatePresignedUrl(request: GeneratePresignedUrlRequest): ZIO[Blocking, OSSClientException, URL]
      def setBucketEncryption(setBucketEncryptionRequest: SetBucketEncryptionRequest): ZIO[Blocking, OSSClientException, Unit]
      def getBucketEncryption(bucketName: String): ZIO[Blocking,OSSClientException, ServerSideEncryptionConfiguration]
      def getBucketEncryption(genericRequest: GenericRequest): ZIO[Blocking,OSSClientException, ServerSideEncryptionConfiguration]
      def deleteBucketEncryption(bucketName: String): ZIO[Blocking,OSSClientException, Unit]
      def deleteBucketEncryption(genericRequest: GenericRequest): ZIO[Blocking,OSSClientException, Unit]
      def processObject(processObjectRequest: ProcessObjectRequest): ZIO[Blocking, OSSClientException, GenericResult]
    }
  }

  def live(region: String, credentials: AliYunCredentials): Layer[ConnectionError, AliYun] =
    ZLayer.fromManaged(Live.connect(region, credentials))

  val live: ZLayer[AliYunSettings, ConnectionError, AliYun] = ZLayer.fromFunctionManaged(Live.connect)

  def execute[T](f: DefaultAcsClient => Task[T]): ZIO[Blocking with AliYun, ClientException, T] =
    ZIO.accessM(_.get[AliYun.Service].execute(f))

  def sendSMS(request: SMS.Request, templateParamValue: String): ZIO[Blocking with AliYun,ClientException,SMS.Response] =
    ZIO.accessM(_.get[AliYun.Service].sendSMS(request, templateParamValue))

  def oss(endpoint: String, credentials: AliYunCredentials): Layer[ConnectionError, AliYunOSS] =
    ZLayer.fromManaged(OSS.connect(endpoint, credentials))

  def ossBucketInfo(bucketName: String): ZIO[Blocking with AliYunOSS,OSSClientException,BucketInfo] =
    ZIO.accessM(_.get[AliYun.OSSService].getBucketInfo(bucketName))
}
