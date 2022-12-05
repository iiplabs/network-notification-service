package com.iiplabs.nns.ksms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KsmsServiceApplication

fun main(args: Array<String>) {
	runApplication<KsmsServiceApplication>(*args)
}
