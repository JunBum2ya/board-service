package com.midas.boardservice.mail.service

import com.azure.communication.email.EmailAsyncClient
import com.azure.communication.email.models.EmailAddress
import com.azure.communication.email.models.EmailMessage
import com.azure.core.util.polling.LongRunningOperationStatus
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.mail.dto.MailRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AzureMailServiceImpl(
    @Value("\${mail.azure.sender}") val sender: String,
    private val emailAsyncClient: EmailAsyncClient
) : MailService {

    private final val log = LoggerFactory.getLogger(AzureMailServiceImpl::class.java)

    override fun sendMessage(mail: MailRequest) {

        val toRecipients = mail.recipients.map { EmailAddress(it) }

        val message = EmailMessage()
            .setSenderAddress(sender)
            .setSubject(mail.title)
            .setBodyPlainText(mail.content)
            .setToRecipients(toRecipients)

        val flux = emailAsyncClient.beginSend(message)
        flux.subscribe({
            if(it.status === LongRunningOperationStatus.SUCCESSFULLY_COMPLETED) {
                log.debug("Successfully sent message : ${it.value.id}")
            } else {
                log.error("메일 전송 시 오류 발생 (${it.value.id}) : ${it.value.error}")
            }
        },{
            log.error("메일 전송 시 오류 발생 : ${it.message}")
        })
    }
}