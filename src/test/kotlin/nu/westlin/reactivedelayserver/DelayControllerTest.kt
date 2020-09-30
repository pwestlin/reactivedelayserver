package nu.westlin.reactivedelayserver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult


@SpringBootTest
@AutoConfigureWebTestClient
internal class DelayControllerTesttest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun `delay a while`() {
        val delayTime: Long = 10

        val result = webClient.get()
            .uri("/$delayTime")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .returnResult<Long>().responseBody.blockFirst()!!

        assertThat(result).isGreaterThanOrEqualTo(delayTime)
    }

    @Test
    fun `delay a while with applicationName`() {
        val applicationName = "Foo"
        val delayTime: Long = 10

        val result = webClient.get()
            .uri("/$applicationName/$delayTime")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .returnResult<ApplicationDelay>().responseBody.blockFirst()!!

        assertThat(result.applicationName).isEqualTo(applicationName)
        assertThat(result.actualDelayTime).isGreaterThanOrEqualTo(delayTime)
    }
}