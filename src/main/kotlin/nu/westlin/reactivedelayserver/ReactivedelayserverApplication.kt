package nu.westlin.reactivedelayserver

import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@SpringBootApplication
class ReactivedelayserverApplication

fun main(args: Array<String>) {
    runApplication<ReactivedelayserverApplication>(*args)
}

@RestController
class DelayController {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/{delayTime}")
    suspend fun slowCall(@PathVariable delayTime: Long): Long {
        return measureTimeMillis { delay(delayTime) }
    }

    @GetMapping("/{applicationName}/{delayTime}")
    suspend fun slowCall(@PathVariable applicationName: String, @PathVariable delayTime: Long): Long {
        logger.info("$applicationName - delaying $delayTime ms ")
        return measureTimeMillis { delay(delayTime) }
    }
}