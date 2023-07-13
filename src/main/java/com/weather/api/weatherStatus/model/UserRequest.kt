package com.weather.api.weatherStatus.model

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "UserRequest")
data class UserRequest @JvmOverloads constructor


(
        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        val requestId: String?="",
        val requestCity:String?,
        val requestCountry:String?,
        val requestTime:LocalDateTime?





)




