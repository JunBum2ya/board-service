package com.fastcampus.projectboard.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.TestComponent
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriComponentsBuilder

@TestComponent
class FormDataEncoder(private val mapper: ObjectMapper) {
    fun encode(obj: Any?): String? {
        val fieldMap: Map<String, String> =
            mapper.convertValue(obj, object : TypeReference<Map<String, String>>() {})
        val valueMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        valueMap.setAll(fieldMap)
        return UriComponentsBuilder.newInstance()
            .queryParams(valueMap)
            .encode()
            .build()
            .query
    }
}