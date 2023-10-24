package org.example.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.function.Function


@Component
class JwtService {
    fun generateToken(userName: String): String {
        val claims: Map<String, Any?> = HashMap()
        return createToken(claims, userName)
    }

    private fun createToken(claims: Map<String, Any?>, userName: String): String {
        return Jwts.builder().claims(claims).subject(userName).issuedAt(Date(System.currentTimeMillis())).expiration(
            Date(
                System.currentTimeMillis() + 1000 * 60 * 30
            )
        ).signWith(signKey, SignatureAlgorithm.HS256).compact()
    }

    private val signKey: Key
        get() {
            val keyBytes = Decoders.BASE64.decode(SECRET)
            return Keys.hmacShaKeyFor(keyBytes)
        }

    fun extractUsername(token: String?): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    fun extractExpiration(token: String?): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser()
            .setSigningKey(signKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    companion object {
        const val SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437"
    }
}