package com.fastcampus.projectboard.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@DisplayName("테스트 도구 - Form 데이터 인코더")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = [FormDataEncoder::class, ObjectMapper::class]
)
internal class FormDataEncoderTest(@param:Autowired private val formDataEncoder: FormDataEncoder) {
    @DisplayName("객체를 넣으면, url encoding 된 form body data 형식의 문자열을 돌려준다.")
    @Test
    fun givenObject_whenEncoding_thenReturnsFormEncodedString() {
        // Given
        val obj = TestObject(
            str = "This 'is' \"test\" string.",
            listStr1 = listOf("hello", "my", "friend").toString().replace(" ", ""),
            listStr2 = listOf("hello","my","friend").joinToString(separator = ","),
            nullStr = null,
            number = 1234,
            floatingNumber = 3.14,
            bool = false,
            bigDecimal = BigDecimal.TEN,
            testEnum = TestEnum.THREE
        )

        // When
        val result = formDataEncoder.encode(obj)

        // Then
        assertThat(result).isEqualTo(
            "str=This%20'is'%20%22test%22%20string." +
                    "&listStr1=%5Bhello,my,friend%5D" +
                    "&listStr2=hello,my,friend" +
                    "&nullStr" +
                    "&number=1234" +
                    "&floatingNumber=3.14" +
                    "&bool=false" +
                    "&bigDecimal=10" +
                    "&testEnum=THREE"
        )
    }

    @JvmRecord
    internal data class TestObject(
        val str: String,
        val listStr1: String,
        val listStr2: String,
        val nullStr: String?,
        val number: Int,
        val floatingNumber: Double,
        val bool: Boolean,
        val bigDecimal: BigDecimal,
        val testEnum: TestEnum
    )

    internal enum class TestEnum {
        ONE,
        TWO,
        THREE
    }
}