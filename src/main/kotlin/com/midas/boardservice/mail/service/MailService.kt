package com.midas.boardservice.mail.service

import com.midas.boardservice.mail.dto.MailRequest

interface MailService {
    fun sendMessage(mail: MailRequest)
}