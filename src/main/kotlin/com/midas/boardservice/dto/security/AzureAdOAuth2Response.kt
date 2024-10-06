package com.midas.boardservice.dto.security

import java.net.URL
import java.time.Instant
import java.util.Date

@SuppressWarnings("unchecked")
class AzureAdOAuth2Response(
    val sub: String,
    var ver: Float,
    iss: URL,
    oid: String,
    preferredUsername: String,
    uti: String,
    givenName: String,
    nonce: String,
    picture: String,
    tid: String,
    email: String,
    aud: List<Any>,
    nbf: Date,
    idp: String,
    rh: String,
    name: String,
    exp: Instant,
    familyName: String,
    iat: Instant
)