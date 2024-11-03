package com.pluto.plugins.network.intercept

import com.pluto.plugins.network.internal.Status
import com.pluto.plugins.network.internal.interceptor.logic.mapCode2Message
import io.ktor.http.ContentType
import org.json.JSONObject

class NetworkData {

    data class Request(
        val url: String,
        val method: String,
        val body: Body?,
        val headers: Map<String, String?>,
        val sentTimestamp: Long,
    ) {
        data class GraphqlData(
            val queryType: String,
            val queryName: String,
        )

        val graphqlData: GraphqlData? = parseGraphqlData()

        private fun parseGraphqlData(): GraphqlData? {
            if (method != "POST" ||
                body == null ||
                !body.isLikelyJson
            ) return null
            val json = runCatching { JSONObject(body!!.body.toString()) }.getOrNull() ?: return null
            val query = json.optString("query") ?: return null
            val match = graqphlQueryRegex.find(query)?.groupValues ?: return null
            return GraphqlData(
                queryType = match[1],
                queryName = match[2],
            )
        }

        internal val isGzipped: Boolean
            get() = headers["Content-Encoding"].equals("gzip", ignoreCase = true)
    }

    data class Response(
        val request: Request,
        private val statusCode: Int,
        val body: Body?,
        val headers: Map<String, String?>,
        val sentTimestamp: Long,
        val receiveTimestamp: Long,
        val protocol: String = "",
        val fromDiskCache: Boolean = false,
    ) {
        val hasGraphqlErrors = parseHasGraphqlErrors()

        internal val status: Status
            get() = Status(statusCode, getStatusMessage())
        val isSuccessful: Boolean
            get() = statusCode in 200..299 && !hasGraphqlErrors
        internal val isGzipped: Boolean
            get() = headers["Content-Encoding"].equals("gzip", ignoreCase = true)

        private fun getStatusMessage() = mapCode2Message(statusCode) +
            if (hasGraphqlErrors) ", Response with errors" else ""

        private fun parseHasGraphqlErrors(): Boolean {
            if (request.graphqlData == null ||
                body == null ||
                !body.isLikelyJson
            ) return false
            val json = runCatching { JSONObject(body!!.body.toString()) }.getOrNull() ?: return false
            return json.has("errors")
        }
    }

    data class Body(
        val body: CharSequence,
        val contentType: String,
    ) {
        private val contentTypeInternal: ContentType = ContentType.parse(contentType)
        private val mediaType: String = contentTypeInternal.contentType
        internal val mediaSubtype: String = contentTypeInternal.contentSubtype
        internal val isBinary: Boolean = BINARY_MEDIA_TYPES.contains(mediaType)
        val sizeInBytes: Long = body.length.toLong()
        internal val mediaTypeFull: String = "$mediaType/$mediaSubtype"
        val isLikelyJson get() = !isBinary && body.startsWith('{')
    }

    companion object {
        internal val BINARY_MEDIA_TYPES = listOf("audio", "video", "image", "font")
        private val graqphlQueryRegex = Regex("""\b(query|mutation)\s+(\w+)""")
    }
}
