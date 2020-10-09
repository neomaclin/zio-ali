package zio.ali

import java.io.{File, InputStream}
import java.time.Instant

import com.aliyun.oss.event.ProgressListener
import com.aliyun.oss.model._
import zio.Chunk

import scala.jdk.CollectionConverters._

package object models {

  object OSS {

    final case class OSSCreateBucketRequest(bucketName: String,
                                            locationConstraint: String = "",
                                            acl: Option[CannedAccessControlList] = None,
                                            dataRedundancyType: Option[DataRedundancyType] = None,
                                            storageClass: Option[StorageClass] = None) {
      def toJava: CreateBucketRequest = {
        val request = new CreateBucketRequest(bucketName)
        request.setCannedACL(acl.orNull)
        request.setDataRedundancyType(dataRedundancyType.orNull)
        request.setStorageClass(storageClass.orNull)
        if (locationConstraint.nonEmpty) request.setLocationConstraint(locationConstraint)
        request
      }
    }

    final case class OSSGenericRequest(bucketName: String = "", key: String = "", versionId: String = "") {
      def toJava: GenericRequest = {
        val request = new GenericRequest()
        if (bucketName.nonEmpty) request.setBucketName(bucketName)
        if (key.nonEmpty) request.setKey(key)
        if (versionId.nonEmpty) request.setBucketName(bucketName)
        request
      }

    }

    final case class OSSListBucketsRequest(prefix: String = "",
                                           marker: String = "",
                                           maxKeys: Int = 100,
                                           bid: String = "",
                                           tagKey: String = "",
                                           tagValue: String = "") {
      def toJava: ListBucketsRequest = {
        val request = new ListBucketsRequest()
        if (marker.nonEmpty) request.setMarker(marker)
        if (prefix.nonEmpty) request.setPrefix(prefix)
        if (maxKeys >= 0) request.setMaxKeys(maxKeys)
        if (bid.nonEmpty) request.setBid(bid)
        if (tagKey.nonEmpty && tagValue.nonEmpty) request.setTag(tagKey, tagValue)
        request
      }

    }


    final case class OSSSetBucketTaggingRequest(bucketName: String,
                                                tags: Map[String, String] = Map.empty) {
      def toJava: SetBucketTaggingRequest = {
        val request = new SetBucketTaggingRequest(bucketName)
        if (tags.nonEmpty) request.setTagSet(new TagSet(tags.asJava))
        request
      }
    }

    final case class OSSSetBucketInventoryConfigurationRequest(bucketName: String, inventoryConfiguration: InventoryConfiguration) {
      def toJava: SetBucketInventoryConfigurationRequest =
        new SetBucketInventoryConfigurationRequest(bucketName, inventoryConfiguration)
    }

    final case class OSSGetBucketInventoryConfigurationRequest(bucketName: String, inventoryId: String) {
      def toJava: GetBucketInventoryConfigurationRequest = {
        new GetBucketInventoryConfigurationRequest(bucketName, inventoryId)
      }
    }

    final case class OSSListBucketInventoryConfigurationsRequest(bucketName: String, continuationToken: String = "") {
      def toJava: ListBucketInventoryConfigurationsRequest = {
        val request = new ListBucketInventoryConfigurationsRequest(bucketName)
        if (continuationToken.nonEmpty) request.setContinuationToken(continuationToken)
        request
      }
    }

    final case class OSSDeleteBucketInventoryConfigurationRequest(bucketName: String, inventoryId: String) {
      def toJava: DeleteBucketInventoryConfigurationRequest = {
        val request = new DeleteBucketInventoryConfigurationRequest(bucketName, inventoryId)
        request
      }
    }

    final case class OSSSetBucketLifecycleRequest(bucketName: String, lifecycleRules: List[LifecycleRule] = Nil) {
      def toJava: SetBucketLifecycleRequest = {
        val request = new SetBucketLifecycleRequest(bucketName)
        if (lifecycleRules.nonEmpty) request.setLifecycleRules(lifecycleRules.asJava)
        request
      }
    }

    final case class OSSInitiateBucketWormRequest(bucketName: String, retentionPeriodInDays: Int = 0) {
      def toJava: InitiateBucketWormRequest = {
        val request = new InitiateBucketWormRequest(bucketName, retentionPeriodInDays)
        request
      }
    }


    final case class OSSCompleteBucketWormRequest(bucketName: String, wormId: String) {
      def toJava: CompleteBucketWormRequest = {
        val request = new CompleteBucketWormRequest(bucketName, wormId)
        request
      }
    }


    final case class OSSExtendBucketWormRequest(bucketName: String, wormId: String = "", retentionPeriodInDays: Int = 0) {
      def toJava: ExtendBucketWormRequest = {
        val request = new ExtendBucketWormRequest(bucketName)
        if (wormId.nonEmpty) request.setWormId(wormId)
        if (retentionPeriodInDays > 0) request.setRetentionPeriodInDays(retentionPeriodInDays)
        request
      }
    }


    final case class OSSSetBucketRequestPaymentRequest(bucketName: String, payer: Option[Payer]) {
      def toJava: SetBucketRequestPaymentRequest = {
        val request = new SetBucketRequestPaymentRequest(bucketName)
        if (payer.nonEmpty) request.setPayer(payer.get)
        request
      }
    }


    final case class OSSPutObjectRequest(bucketName: String,
                                         key: String,
                                         input: Either[File, InputStream],
                                         metaData: Option[ObjectMetadata] = None,
                                         callback: Option[Callback] = None,
                                         process: String = "",
                                         trafficLimit: Int = 0) {
      def toJava: PutObjectRequest = {
        val request = input match {
          case Right(inputStream) => new PutObjectRequest(bucketName, key, inputStream)
          case Left(file) => new PutObjectRequest(bucketName, key, file)
        }
        if (metaData.nonEmpty) request.setMetadata(metaData.get)
        if (callback.nonEmpty) request.setCallback(callback.get)
        if (process.nonEmpty) request.setProcess(process)
        if (trafficLimit > 0) request.setTrafficLimit(trafficLimit)
        request
      }
    }


    final case class OSSGetObjectRequest(bucketName: String, key: String, versionId: String = "") {
      def toJava: GetObjectRequest = {
        val request = new GetObjectRequest(bucketName, key)
        if (versionId.nonEmpty) request.setVersionId(versionId)
        request
      }
    }

    import java.net.URL

    final case class OSSGetObjectURLRequest(absoluteUrl: URL, requestHeaders: Map[String, String]) {
      def toJava: GetObjectRequest = {
        val request = new GetObjectRequest(absoluteUrl, requestHeaders.asJava)
        request
      }
    }

    final case class OSSExtendBucketWormRequest(bucketName: String, wormId: String = "", retentionPeriodInDays: Int = 0) {
      def toJava: ExtendBucketWormRequest = {
        val request = new ExtendBucketWormRequest(bucketName)
        if (wormId.nonEmpty) request.setWormId(wormId)
        if (retentionPeriodInDays > 0) request.setRetentionPeriodInDays(retentionPeriodInDays)
        request
      }
    }

    final case class OSSListVersionsRequest(bucketName: String = "",
                                            prefix: String = "",
                                            keyMarker: String = "",
                                            versionIdMarker: String = "",
                                            delimiter: String = "",
                                            maxResults: Int = 0) {
      def toJava: ListVersionsRequest = {
        val request = new ListVersionsRequest()

        if (bucketName.nonEmpty) request.setBucketName(bucketName)
        if (prefix.nonEmpty) request.setPrefix(prefix)
        if (keyMarker.nonEmpty) request.setKeyMarker(keyMarker)
        if (versionIdMarker.nonEmpty) request.setVersionIdMarker(versionIdMarker)
        if (delimiter.nonEmpty) request.setDelimiter(delimiter)
        if (maxResults > 0) request.setMaxResults(maxResults)
        request
      }
    }


    final case class OSSDeleteVersionRequest(bucketName: String, key: String, versionId: String) {
      def toJava: DeleteVersionRequest = {
        val request = new DeleteVersionRequest(bucketName, key, versionId)
        request
      }
    }


    final case class OSSDeleteVersionsRequest(bucketName: String, keys: List[DeleteVersionsRequest.KeyVersion] = Nil) {
      def toJava: DeleteVersionsRequest = {
        val request = new DeleteVersionsRequest(bucketName)
        if (keys.nonEmpty) request.setKeys(keys.asJava)
        request
      }
    }


    final case class OSSDeleteObjectsRequest(bucketName: String,
                                             keys: List[String] = Nil,
                                             isQuiet: Boolean = false,
                                             encodingType: String = "") {
      def toJava: DeleteObjectsRequest = {
        val request = new DeleteObjectsRequest(bucketName).withQuiet(isQuiet)
        if (keys.nonEmpty) request.setKeys(keys.asJava)
        if (encodingType.nonEmpty) request.setEncodingType(encodingType)
        request
      }
    }


    final case class OSSListPartsRequest(bucketName: String, key: String, uploadId: String, encodingType: String = "") {
      def toJava: ListPartsRequest = {
        val request = new ListPartsRequest(bucketName, key, uploadId)
        if (encodingType.nonEmpty) request.setEncodingType(encodingType)
        request
      }
    }

    final case class OSSRange(start: Long, end: Long)

    final case class OSSSelectObjectRequest(bucketName: String,
                                            key: String,
                                            lineRange: Option[OSSRange] = None,
                                            splitRange: Option[OSSRange] = None,
                                            expression: String = "",
                                            skipPartialDataRecord: Boolean = false,
                                            maxSkippedRecordsAllowed: Long = 0L,
                                            selectProgressListener: Option[ProgressListener]) {
      def toJava: SelectObjectRequest = {
        val request = new SelectObjectRequest(bucketName, key)
        lineRange.foreach(range => request.setLineRange(range.start, range.`end`))
        splitRange.foreach(range => request.setSplitRange(range.start, range.end))
        if (expression.nonEmpty) request.setExpression(expression)
        selectProgressListener.foreach(request.setSelectProgressListener)
        request
      }
    }


    final case class OSSSetBucketVersioningRequest(bucketName: String, configuration: BucketVersioningConfiguration) {
      def toJava: SetBucketVersioningRequest = {
        val request = new SetBucketVersioningRequest(bucketName, configuration)
        request
      }
    }


    final case class OSSSetBucketRefererRequest(bucketName: String, referer: Option[BucketReferer]) {
      def toJava: SetBucketRefererRequest = {
        val request = new SetBucketRefererRequest(bucketName)
        referer.foreach(request.setReferer)
        request
      }
    }


    final case class OSSSetBucketLoggingRequest(bucketName: String, targetBucket: String = "", targetPrefix: String  = "" ) {
      def toJava: SetBucketLoggingRequest = {
        val request = new SetBucketLoggingRequest(bucketName)
        if (targetBucket.nonEmpty) request.setTargetBucket(targetBucket)
        if (targetPrefix.nonEmpty) request.setTargetPrefix(targetPrefix)
        request
      }
    }


    final case class OSSSetBucketWebsiteRequest(bucketName: String,
                                                indexDocument: String = "",
                                                errorDocument: String  = "",
                                                routingRules: List[RoutingRule] = Nil) {
      def toJava: SetBucketWebsiteRequest = {
        val request = new SetBucketWebsiteRequest(bucketName)
        if (indexDocument.nonEmpty) request.setIndexDocument(indexDocument)
        if (errorDocument.nonEmpty) request.setErrorDocument(errorDocument)
        if (routingRules.nonEmpty) request.setRoutingRules(routingRules.asJava)
        request
      }
    }


    final case class OSSAddBucketReplicationRequest(bucketName: String
                                                   ) {
      def toJava: AddBucketReplicationRequest =  {
        val request = new AddBucketReplicationRequest(bucketName)
        if (indexDocument.nonEmpty) request.setIndexDocument(indexDocument)
        if (errorDocument.nonEmpty) request.setErrorDocument(errorDocument)
        if (routingRules.nonEmpty) request.setRoutingRules(routingRules.asJava)
        request
      }
    }


    final case class OSSGetBucketReplicationProgressRequest() {
      def toJava: GetBucketReplicationProgressRequest = ???
    }


    final case class OSSDeleteBucketReplicationRequest() {
      def toJava: DeleteBucketReplicationRequest = ???
    }


    final case class OSSSetBucketAclRequest() {
      def toJava: SetBucketAclRequest = ???
    }


    final case class OSSSetBucketCORSRequest() {
      def toJava: SetBucketCORSRequest = ???
    }


    final case class OSSAppendObjectRequest() {
      def toJava: AppendObjectRequest = ???
    }


    final case class OSSUploadFileRequest() {
      def toJava: UploadFileRequest = ???
    }


    final case class OSSInitiateMultipartUploadRequest() {
      def toJava: InitiateMultipartUploadRequest = ???
    }


    final case class OSSUploadPartRequest() {
      def toJava: UploadPartRequest = ???
    }


    final case class OSSCompleteMultipartUploadRequest() {
      def toJava: CompleteMultipartUploadRequest = ???
    }


    final case class OSSAbortMultipartUploadRequest() {
      def toJava: AbortMultipartUploadRequest = ???
    }


    final case class OSSSetBucketPolicyRequest() {
      def toJava: SetBucketPolicyRequest = ???
    }


    final case class OSSListMultipartUploadsRequest() {
      def toJava: ListMultipartUploadsRequest = ???
    }


    final case class OSSDownloadFileRequest() {
      def toJava: DownloadFileRequest = ???
    }


    final case class OSSSetObjectAclRequest() {
      def toJava: SetObjectAclRequest = ???
    }


    final case class OSSCopyObjectRequest() {
      def toJava: CopyObjectRequest = ???
    }


    final case class OSSRestoreObjectRequest() {
      def toJava: RestoreObjectRequest = ???
    }


    final case class OSSListObjectsRequest() {
      def toJava: ListObjectsRequest = ???
    }


    final case class OSSUploadPartCopyRequest() {
      def toJava: UploadPartCopyRequest = ???
    }


    final case class OSSCreateSymlinkRequest() {
      def toJava: CreateSymlinkRequest = ???
    }


    final case class OSSSetObjectTaggingRequest() {
      def toJava: SetObjectTaggingRequest = ???
    }


    final case class OSSCreateSelectObjectMetadataRequest() {
      def toJava: CreateSelectObjectMetadataRequest = ???
    }


    final case class OSSGeneratePresignedUrlRequest() {
      def toJava: GeneratePresignedUrlRequest = {

      }
    }


    final case class OSSSetBucketEncryptionRequest() {
      def toJava: SetBucketEncryptionRequest = ???
    }


    final case class OSSProcessObjectRequest() {
      def toJava: ProcessObjectRequest = ???
    }


    final case class OSSBucket(name: String, creationDate: Instant)

    type OSSBucketListing = Chunk[OSSBucket]

    def fromBucket(bucket: Bucket): OSSBucket =
      OSSBucket(bucket.getName, bucket.getCreationDate.toInstant)

    def fromBuckets(l: List[Bucket]): OSSBucketListing =
      Chunk.fromIterable(l.map(fromBucket))

  }

  object SMS {
    final val action: String = "SendSms"

    final case class Request(phoneNumber: String,
                             signName: String,
                             templateCode: String)

    final case class Response(BizId: Option[String],
                              Code: String,
                              Message: String,
                              RequestId: String)

  }

}
