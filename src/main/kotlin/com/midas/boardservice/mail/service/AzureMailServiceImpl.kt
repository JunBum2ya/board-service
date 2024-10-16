package com.midas.boardservice.mail.service

import com.azure.communication.email.EmailAsyncClient
import com.azure.communication.email.models.EmailAddress
import com.azure.communication.email.models.EmailMessage
import com.azure.core.util.polling.LongRunningOperationStatus
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.mail.dto.MailRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AzureMailServiceImpl(
    @Value("\${mail.azure.sender}") val sender: String,
    private val emailAsyncClient: EmailAsyncClient
) : MailService {
    override fun sendMessage(mail: MailRequest) {

        val toRecipients = mail.recipients.map { EmailAddress(it) }

        val message = EmailMessage()
            .setSenderAddress(sender)
            .setSubject(mail.title)
            .setBodyPlainText(mail.content)
            .setToRecipients(toRecipients)

        val flux = emailAsyncClient.beginSend(message)
        val response = flux.blockLast() ?: throw CustomException(ResultStatus.UNKNOWN_EXCEPTION)

        if(response.status != LongRunningOperationStatus.SUCCESSFULLY_COMPLETED) {
            throw CustomException(ResultStatus.UNKNOWN_EXCEPTION)
        }

    }
}