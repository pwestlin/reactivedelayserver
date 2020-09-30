package nu.westlin.reactivedelayserver

import kotlinx.coroutines.delay
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

    @GetMapping("/{delayTime}")
    suspend fun slowCall(@PathVariable delayTime: Long): Long {
        return measureTimeMillis { delay(delayTime) }
    }
}