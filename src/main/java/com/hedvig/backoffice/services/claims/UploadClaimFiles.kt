package com.hedvig.backoffice.services.claims

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.ObjectTagging
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.Tag
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.util.*

data class UploadResult (
  val bucket: String,
  val key: String
)

@Service
data class UploadClaimFiles(
  val claimsBucket: String = "com-hedvig-claims-files",
  val s3Client: AmazonS3Client
) {

  fun uploadClaimFilesToS3Bucket(
    contentType: String,
    data: ByteArray,
    claimId: String,
    fileName: String,
    memberId: String): UploadResult {
    val metadata = ObjectMetadata()
    metadata.contentType = contentType
    metadata.contentLength = data.size.toLong()

    val uploadKey = "claim${UUID.randomUUID()}$fileName"

    val uploadRequest = PutObjectRequest(
      claimsBucket, uploadKey, ByteArrayInputStream(data), metadata)

    val tags = listOf(Tag("claimId", claimId), Tag("memberId", memberId))

    uploadRequest.tagging = ObjectTagging(tags)

    s3Client.putObject(uploadRequest)

    return UploadResult(claimsBucket, uploadKey)
  }
}
