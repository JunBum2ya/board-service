package com.midas.boardservice.util

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.property.arbitrary.TypeReference
import org.springframework.boot.test.context.TestComponent
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriComponentsBuilder


@TestComponent
class FormDataEncoder(private val mapper: ObjectMapper) {

    fun encode(obj: Any): String? {
        val fieldMap: Map<String,String> = mapper.convertValue(obj, Map::class.java) as Map<String,String>
        val valueMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        valueMap.setAll(fieldMap)

        return UriComponentsBuilder.newInstance()
            .queryParams(valueMap)
            .encode()
            .build()
            .query
    }

}