@file:Suppress("NonAsciiCharacters", "PropertyName")

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


/*
Målet är att kunna skapa en person med minst en adress mha följande DSL:

person {
    förnamn = "Peter"
    efternamn = "Plutt"

    adress {
        gatuadress = "Räservägen 1"
        postnummer = "77611"
        postadress = "Räsermora"
    }

    adress {
        etikett = "Arbete"
        gatuadress = "Lantmäterigatan 1"
        postnummer = "12341"
        postadress = "Gäflä"
    }
}
 */

data class Adress(
    val etikett: String? = null,
    val gatuadress: String,
    val postnummer: String,
    val postadress: String
)

data class Person(
    val förnamn: String,
    val efternamn: String,
    val adresser: List<Adress>
)

class AdressBuilder {
    var etikett: String? = null
    lateinit var gatuadress: String
    lateinit var postnummer: String
    lateinit var postadress: String

    fun build(): Adress {
        return Adress(
            etikett = etikett,
            gatuadress = gatuadress,
            postnummer = postnummer,
            postadress = postadress
        )
    }
}

class PersonBuilder {
    lateinit var förnamn: String
    lateinit var efternamn: String
    val adresser = ArrayList<Adress>()

    fun adress(block: AdressBuilder.() -> Unit) {
        adresser.add(AdressBuilder().apply(block).build())
    }

    fun build(): Person {
        check(adresser.isNotEmpty()) { "En person måste ha minst en adress" }

        return Person(
            förnamn = förnamn,
            efternamn = efternamn,
            adresser = adresser,
        )
    }
}

fun person(block: PersonBuilder.() -> Unit): Person {
    return PersonBuilder().apply(block).build()
}

class PersonDSLTest {

    @Test
    fun `DSL för Person`() {
        val person = person {
            förnamn = "Peter"
            efternamn = "Plutt"

            adress {
                gatuadress = "Räservägen 1"
                postnummer = "77611"
                postadress = "Räsermora"
            }

            adress {
                etikett = "Arbete"
                gatuadress = "Lantmäterigatan 1"
                postnummer = "12341"
                postadress = "Gäflä"
            }
        }

        assertThat(person).isEqualTo(
            Person(
                förnamn = "Peter",
                efternamn = "Plutt",
                adresser = listOf(
                    Adress(
                        gatuadress = "Räservägen 1",
                        postnummer = "77611",
                        postadress = "Räsermora"
                    ),
                    Adress(
                        etikett = "Arbete",
                        gatuadress = "Lantmäterigatan 1",
                        postnummer = "12341",
                        postadress = "Gäflä"
                    )
                )
            )
        )
    }
}
