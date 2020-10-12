package zio.ali

import java.io.{File, InputStream}
import java.time.Instant
import java.util.Date

import com.aliyun.openservices.log.common.LogItem
import com.aliyun.oss.HttpMethod
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
        acl.foreach(request.setCannedACL)
        dataRedundancyType.foreach(request.setDataRedundancyType)
        storageClass.foreach(request.setStorageClass)
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
                                            lineOSSRange: Option[OSSRange] = None,
                                            splitOSSRange: Option[OSSRange] = None,
                                            expression: String = "",
                                            skipPartialDataRecord: Boolean = false,
                                            maxSkippedRecordsAllowed: Long = 0L,
                                            selectProgressListener: Option[ProgressListener]) {
      def toJava: SelectObjectRequest = {
        val request = new SelectObjectRequest(bucketName, key)
        lineOSSRange.foreach(range => request.setLineRange(range.start, range.end))
        splitOSSRange.foreach(range => request.setSplitRange(range.start, range.end))
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


    final case class OSSSetBucketLoggingRequest(bucketName: String, targetBucket: String = "", targetPrefix: String = "") {
      def toJava: SetBucketLoggingRequest = {
        val request = new SetBucketLoggingRequest(bucketName)
        if (targetBucket.nonEmpty) request.setTargetBucket(targetBucket)
        if (targetPrefix.nonEmpty) request.setTargetPrefix(targetPrefix)
        request
      }
    }


    final case class OSSSetBucketWebsiteRequest(bucketName: String,
                                                indexDocument: String = "",
                                                errorDocument: String = "",
                                                routingRules: List[RoutingRule] = Nil) {
      def toJava: SetBucketWebsiteRequest = {
        val request = new SetBucketWebsiteRequest(bucketName)
        if (indexDocument.nonEmpty) request.setIndexDocument(indexDocument)
        if (errorDocument.nonEmpty) request.setErrorDocument(errorDocument)
        if (routingRules.nonEmpty) request.setRoutingRules(routingRules.asJava)
        request
      }
    }


    final case class OSSAddBucketReplicationRequest(bucketName: String,
                                                    replicationRuleID: String = "",
                                                    replicationKmsKeyId: String = "",
                                                    targetBucketName: String = "",
                                                    targetBucketLocation: String = "",
                                                    targetCloud: String = "",
                                                    targetCloudLocation: String = "",
                                                    syncRule: String = "",
                                                    sseKmsEncryptedObjectsStatus: String = "",
                                                    enableHistoricalObjectReplication: Boolean = false,
                                                    objectPrefixList: List[String] = Nil,
                                                    replicationActionList: List[AddBucketReplicationRequest.ReplicationAction] = Nil
                                                   ) {
      def toJava: AddBucketReplicationRequest = {
        val request = new AddBucketReplicationRequest(bucketName)

        if (replicationRuleID.nonEmpty) request.setReplicationRuleID(replicationRuleID)
        if (replicationKmsKeyId.nonEmpty) request.setReplicaKmsKeyID(replicationKmsKeyId)
        if (targetBucketName.nonEmpty) request.setTargetBucketName(targetBucketName)
        if (targetBucketLocation.nonEmpty) request.setTargetBucketLocation(targetBucketLocation)
        if (targetCloud.nonEmpty) request.setTargetCloud(targetCloud)
        if (targetCloudLocation.nonEmpty) request.setTargetCloudLocation(targetCloudLocation)
        if (syncRule.nonEmpty) request.setSyncRole(syncRule)
        if (sseKmsEncryptedObjectsStatus.nonEmpty) request.setSseKmsEncryptedObjectsStatus(sseKmsEncryptedObjectsStatus)
        if (enableHistoricalObjectReplication) request.setEnableHistoricalObjectReplication(enableHistoricalObjectReplication)
        if (objectPrefixList.nonEmpty) request.setObjectPrefixList(objectPrefixList.asJava)
        if (replicationActionList.nonEmpty) request.setReplicationActionList(replicationActionList.asJava)
        request
      }
    }


    final case class OSSGetBucketReplicationProgressRequest(bucketName: String, replicationRuleID: String = "") {
      def toJava: GetBucketReplicationProgressRequest = {
        val request = new GetBucketReplicationProgressRequest(bucketName)
        if (replicationRuleID.nonEmpty) request.setReplicationRuleID(replicationRuleID)
        request
      }
    }


    final case class OSSDeleteBucketReplicationRequest(bucketName: String, replicationRuleID: String = "") {
      def toJava: DeleteBucketReplicationRequest = {
        val request = new DeleteBucketReplicationRequest(bucketName)
        if (replicationRuleID.nonEmpty) request.setReplicationRuleID(replicationRuleID)
        request
      }
    }


    final case class OSSSetBucketAclRequest(bucketName: String,
                                            acl: Option[CannedAccessControlList] = None) {
      def toJava: SetBucketAclRequest = {
        val request = new SetBucketAclRequest(bucketName)
        acl.foreach(request.setCannedACL)
        request
      }
    }


    final case class OSSSetBucketCORSRequest(bucketName: String,
                                             corsRules: List[SetBucketCORSRequest.CORSRule] = Nil,
                                             responseVary: Boolean = false) {
      def toJava: SetBucketCORSRequest = {
        val request = new SetBucketCORSRequest(bucketName)
        if (corsRules.nonEmpty) request.setCorsRules(corsRules.asJava)
        if (responseVary) request.setResponseVary(responseVary)
        request
      }
    }


    final case class OSSAppendObjectRequest(bucketName: String,
                                            key: String,
                                            input: Either[File, InputStream],
                                            position: Long = 0,
                                            iniCRC: Long = 0,
                                            trafficLimit: Int = 0) {
      def toJava: AppendObjectRequest = {
        val request = input match {
          case Right(inputStream) => new AppendObjectRequest(bucketName, key, inputStream)
          case Left(file) => new AppendObjectRequest(bucketName, key, file)
        }
        if (trafficLimit > 0) request.setTrafficLimit(trafficLimit)
        if (position > 0) request.setPosition(position)
        if (iniCRC > 0) request.setInitCRC(iniCRC)
        request
      }
    }


    final case class OSSUploadFileRequest(bucketName: String,
                                          key: String,
                                          uploadFile: String = "",
                                          partSize: Long = 0,
                                          taskNum: Int = 0,
                                          enableCheckpoint: Boolean = false,
                                          sequentialMode: Boolean = false,
                                          checkpointFile: String = "",
                                          metaData: Option[ObjectMetadata] = None,
                                          callback: Option[Callback] = None,
                                          trafficLimit: Int = 0) {
      def toJava: UploadFileRequest = {
        val request = new UploadFileRequest(bucketName, key)
        if (uploadFile.nonEmpty) request.setUploadFile(uploadFile)
        if (partSize > 0) request.setPartSize(partSize)
        if (taskNum > 0) request.setTaskNum(taskNum)
        if (trafficLimit > 0) request.setTrafficLimit(trafficLimit)
        if (enableCheckpoint) request.setEnableCheckpoint(enableCheckpoint)
        if (sequentialMode) request.setSequentialMode(sequentialMode)
        if (checkpointFile.nonEmpty) request.setCheckpointFile(checkpointFile)
        callback.foreach(request.setCallback)
        metaData.foreach(request.setObjectMetadata)
        request
      }
    }


    final case class OSSInitiateMultipartUploadRequest(bucketName: String,
                                                       key: String,
                                                       sequentialMode: Boolean = false,
                                                       metaData: Option[ObjectMetadata] = None) {
      def toJava: InitiateMultipartUploadRequest = {
        val request = new InitiateMultipartUploadRequest(bucketName, key)
        if (sequentialMode) request.setSequentialMode(sequentialMode)
        metaData.foreach(request.setObjectMetadata)
        request
      }
    }


    final case class OSSUploadPartRequest(bucketName: String,
                                          key: String,
                                          uploadId: String,
                                          partNumber: Int,
                                          inputStream: InputStream,
                                          partSize: Long,
                                          md5Digest: String = "",
                                          useChunkEncoding: Boolean = false,
                                          trafficLimit: Int = 0) {
      def toJava: UploadPartRequest = {
        val request = new UploadPartRequest(bucketName, key, uploadId, partNumber, inputStream, partSize)
        if (md5Digest.nonEmpty) request.setMd5Digest(md5Digest)
        if (useChunkEncoding) request.setUseChunkEncoding(useChunkEncoding)
        if (trafficLimit > 0) request.setTrafficLimit(trafficLimit)
        request
      }
    }


    final case class OSSCompleteMultipartUploadRequest(bucketName: String,
                                                       key: String,
                                                       uploadId: String,
                                                       partETags: List[PartETag],
                                                       callback: Option[Callback] = None,
                                                       process: String = "",
                                                       acl: Option[CannedAccessControlList] = None
                                                      ) {
      def toJava: CompleteMultipartUploadRequest = {
        val request = new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags.asJava)
        callback.foreach(request.setCallback)
        if (process.nonEmpty) request.setProcess(process)
        acl.foreach(request.setObjectACL)
        request
      }
    }


    final case class OSSAbortMultipartUploadRequest(bucketName: String, key: String, uploadId: String) {
      def toJava: AbortMultipartUploadRequest = {
        val request = new AbortMultipartUploadRequest(bucketName, key, uploadId)
        request
      }
    }


    final case class OSSSetBucketPolicyRequest(bucketName: String, policyText: String) {
      def toJava: SetBucketPolicyRequest = {
        val request = new SetBucketPolicyRequest(bucketName, policyText)
        request
      }
    }


    final case class OSSListMultipartUploadsRequest(bucketName: String,
                                                    delimiter: String = "",
                                                    prefix: String = "",
                                                    maxUploads: Integer = 0,
                                                    keyMarker: String = "",
                                                    uploadIdMarker: String = "",
                                                    encodingType: String = "") {
      def toJava: ListMultipartUploadsRequest = {
        val request = new ListMultipartUploadsRequest(bucketName)
        if (delimiter.nonEmpty) request.setDelimiter(delimiter)
        if (prefix.nonEmpty) request.setPrefix(prefix)
        if (keyMarker.nonEmpty) request.setKeyMarker(keyMarker)
        if (uploadIdMarker.nonEmpty) request.setUploadIdMarker(uploadIdMarker)
        if (encodingType.nonEmpty) request.setEncodingType(encodingType)
        if (maxUploads > 0) request.setMaxUploads(maxUploads)
        request
      }
    }


    final case class OSSDownloadFileRequest(bucketName: String,
                                            key: String,
                                            downloadFile: String = "",
                                            partSize: Long = 1024 * 100,
                                            taskNum: Int = 1,
                                            enableCheckpoint: Boolean = false,
                                            checkpointFile: String = "",
                                            matchingETagConstraints: List[String] = Nil,
                                            nonmatchingEtagConstraints: List[String] = Nil,
                                            unmodifiedSinceConstraint: Option[Date] = None,
                                            modifiedSinceConstraint: Option[Date] = None,
                                            trafficLimit: Int = 0,
                                            range: Option[OSSRange] = None
                                           ) {
      def toJava: DownloadFileRequest = {
        val request = new DownloadFileRequest(bucketName, key)
        if (downloadFile.nonEmpty) request.setDownloadFile(downloadFile)
        if (partSize != 1024 * 100) request.setPartSize(partSize)
        if (taskNum > 1) request.setTaskNum(taskNum)
        if (enableCheckpoint) request.setEnableCheckpoint(enableCheckpoint)
        if (checkpointFile.nonEmpty) request.setCheckpointFile(checkpointFile)
        if (matchingETagConstraints.nonEmpty) request.setMatchingETagConstraints(matchingETagConstraints.asJava)
        if (nonmatchingEtagConstraints.nonEmpty) request.setNonmatchingETagConstraints(nonmatchingEtagConstraints.asJava)
        if (unmodifiedSinceConstraint.nonEmpty) request.setUnmodifiedSinceConstraint(unmodifiedSinceConstraint.get)
        if (modifiedSinceConstraint.nonEmpty) request.setModifiedSinceConstraint(modifiedSinceConstraint.get)
        if (trafficLimit > 0) request.setTrafficLimit(trafficLimit)
        range.foreach(r => request.setRange(r.start, r.end))
        request
      }
    }


    final case class OSSSetObjectAclRequest(bucketName: String, key: String, versionId: String,
                                            cannedACL: Option[CannedAccessControlList] = None) {
      def toJava: SetObjectAclRequest = {
        val request = new SetObjectAclRequest(bucketName, key)
        cannedACL.foreach(request.setCannedACL)
        request
      }
    }


    final case class OSSCopyObjectRequest(sourceBucketName: String,
                                          sourceKey: String,
                                          destinationBucketName: String,
                                          destinationKey: String,
                                          sourceVersionId: String = "",
                                          serverSideEncryption: String = "",
                                          serverSideEncryptionKeyID: String = "",
                                          newObjectMetadata: Option[ObjectMetadata] = None,
                                          matchingETagConstraints: List[String] = Nil,
                                          nonmatchingEtagConstraints: List[String] = Nil,
                                          unmodifiedSinceConstraint: Option[Date] = None,
                                          modifiedSinceConstraint: Option[Date] = None,
                                          payer: Option[Payer] = None) {
      def toJava: CopyObjectRequest = {
        val request = new CopyObjectRequest(sourceBucketName, sourceKey, destinationBucketName, destinationKey)
        if (sourceVersionId.nonEmpty) request.setSourceVersionId(sourceVersionId)
        if (serverSideEncryption.nonEmpty) request.setServerSideEncryption(serverSideEncryption)
        if (serverSideEncryptionKeyID.nonEmpty) request.setServerSideEncryptionKeyId(serverSideEncryptionKeyID)
        newObjectMetadata.foreach(request.setNewObjectMetadata)
        if (matchingETagConstraints.nonEmpty) request.setMatchingETagConstraints(matchingETagConstraints.asJava)
        if (nonmatchingEtagConstraints.nonEmpty) request.setNonmatchingETagConstraints(nonmatchingEtagConstraints.asJava)
        if (unmodifiedSinceConstraint.nonEmpty) request.setUnmodifiedSinceConstraint(unmodifiedSinceConstraint.get)
        if (modifiedSinceConstraint.nonEmpty) request.setModifiedSinceConstraint(modifiedSinceConstraint.get)
        payer.foreach(request.setRequestPayer)
        request
      }
    }


    final case class OSSRestoreObjectRequest(bucketName: String,
                                             key: String,
                                             restoreConfiguration: RestoreConfiguration) {
      def toJava: RestoreObjectRequest = {
        val request = new RestoreObjectRequest(bucketName, key, restoreConfiguration)
        request
      }
    }


    final case class OSSListObjectsRequest(bucketName: String,
                                           delimiter: String = "",
                                           prefix: String = "",
                                           maxKeys: Integer = 0,
                                           marker: String = "",
                                           encodingType: String = "") {
      def toJava: ListObjectsRequest = {
        val request = new ListObjectsRequest(bucketName)
        if (delimiter.nonEmpty) request.setDelimiter(delimiter)
        if (prefix.nonEmpty) request.setPrefix(prefix)
        if (marker.nonEmpty) request.setMarker(marker)
        if (encodingType.nonEmpty) request.setEncodingType(encodingType)
        if (maxKeys > 0) request.setMaxKeys(maxKeys)
        request
      }
    }


    final case class OSSUploadPartCopyRequest(sourceBucketName: String,
                                              sourceKey: String,
                                              targetBucketName: String,
                                              targetKey: String,
                                              uploadId: String = "",
                                              partNumber: Int = 0,
                                              beginIndex: Long = 0,
                                              partSize: Long = 0,
                                              sourceVersionId: String = "",
                                              md5Digest: String = "",
                                              matchingETagConstraints: List[String] = Nil,
                                              nonmatchingEtagConstraints: List[String] = Nil,
                                              unmodifiedSinceConstraint: Option[Date] = None,
                                              modifiedSinceConstraint: Option[Date] = None,
                                              payer: Option[Payer] = None) {
      def toJava: UploadPartCopyRequest = {
        val request = new UploadPartCopyRequest(sourceBucketName, sourceKey, targetBucketName, targetKey)
        if (sourceVersionId.nonEmpty) request.setSourceVersionId(sourceVersionId)
        if (uploadId.nonEmpty) request.setUploadId(uploadId)
        if (md5Digest.nonEmpty) request.setMd5Digest(md5Digest)
        if (partNumber > 0) request.setPartNumber(partNumber)
        if (beginIndex > 0) request.setBeginIndex(beginIndex)
        if (partSize > 0) request.setPartSize(partSize)
        if (matchingETagConstraints.nonEmpty) request.setMatchingETagConstraints(matchingETagConstraints.asJava)
        if (nonmatchingEtagConstraints.nonEmpty) request.setNonmatchingETagConstraints(nonmatchingEtagConstraints.asJava)
        if (unmodifiedSinceConstraint.nonEmpty) request.setUnmodifiedSinceConstraint(unmodifiedSinceConstraint.get)
        if (modifiedSinceConstraint.nonEmpty) request.setModifiedSinceConstraint(modifiedSinceConstraint.get)
        payer.foreach(request.setRequestPayer)
        request
      }
    }


    final case class OSSCreateSymlinkRequest(bucketName: String, symlink: String, target: String,
                                             metaData: Option[ObjectMetadata] = None) {
      def toJava: CreateSymlinkRequest = {
        val request = new CreateSymlinkRequest(bucketName, symlink, target)
        metaData.foreach(request.setMetadata)
        request
      }
    }


    final case class OSSSetObjectTaggingRequest(bucketName: String,
                                                key: String,
                                                tags: Map[String, String] = Map.empty) {
      def toJava: SetObjectTaggingRequest = {
        val request = new SetObjectTaggingRequest(bucketName, key)
        if (tags.nonEmpty) request.setTagSet(new TagSet(tags.asJava))
        request
      }
    }


    final case class OSSCreateSelectObjectMetadataRequest(bucketName: String,
                                                          key: String,
                                                          process: String = "",
                                                          inputSerialization: Option[InputSerialization] = None,
                                                          overwrite: Boolean = false,
                                                          selectProgressListener: Option[ProgressListener] = None) {
      def toJava: CreateSelectObjectMetadataRequest = {
        val request = new CreateSelectObjectMetadataRequest(bucketName, key)
        if (process.nonEmpty) request.setProcess(process)
        inputSerialization.foreach(request.setInputSerialization)
        if (overwrite) request.setOverwrite(overwrite)
        selectProgressListener.foreach(request.setSelectProgressListener)
        request
      }
    }


    final case class OSSGeneratePresignedUrlRequest(bucketName: String,
                                                    key: String,
                                                    method: Option[HttpMethod] = None,
                                                    contentType: String = "",
                                                    contentMD5: String = "",
                                                    process: String = "",
                                                    expiration: Option[Date] = None,
                                                    responseHeaders: Option[ResponseHeaderOverrides] = None,
                                                    userMetadata: Map[String, String] = Map.empty,
                                                    queryParam: Map[String, String] = Map.empty,
                                                    headers: Map[String, String] = Map.empty,
                                                    additionalHeaderNames: Set[String] = Set.empty,
                                                    trafficLimit: Int = 0
                                                   ) {
      def toJava: GeneratePresignedUrlRequest = {
        val request = new GeneratePresignedUrlRequest(bucketName, key)
        if (contentType.nonEmpty) request.setContentType(contentType)
        if (contentMD5.nonEmpty) request.setContentMD5(contentMD5)
        if (process.nonEmpty) request.setProcess(process)
        expiration.foreach(request.setExpiration)
        responseHeaders.foreach(request.setResponseHeaders)
        if (userMetadata.nonEmpty) request.setUserMetadata(userMetadata.asJava)
        if (queryParam.nonEmpty) request.setQueryParameter(queryParam.asJava)
        if (headers.nonEmpty) request.setHeaders(headers.asJava)
        if (additionalHeaderNames.nonEmpty) request.setAdditionalHeaderNames(additionalHeaderNames.asJava)
        if (trafficLimit > 0) request.setTrafficLimit(trafficLimit)
        request
      }
    }


    final case class OSSSetBucketEncryptionRequest(bucketName: String,
                                                   serverSideEncryptionConfiguration: Option[ServerSideEncryptionConfiguration] = None) {
      def toJava: SetBucketEncryptionRequest = {
        val request = new SetBucketEncryptionRequest(bucketName)
        serverSideEncryptionConfiguration.foreach(request.setServerSideEncryptionConfiguration)
        request
      }
    }


    final case class OSSProcessObjectRequest(bucketName: String, key: String, process: String) {
      def toJava: ProcessObjectRequest = {
        val request = new ProcessObjectRequest(bucketName, key, process)

        request
      }
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

  object Log{
    final case class LogServiceRequest(project: String,logStore: String,topic: String = "",source:String = "",
                                       shardHash:Option[String] = None,logItems: Seq[LogItem])
  }

}
