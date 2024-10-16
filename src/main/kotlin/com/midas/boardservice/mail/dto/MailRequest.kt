package com.midas.boardservice.mail.dto

data class MailRequest(val title: String, val content: String, val recipients: List<String>) {
}