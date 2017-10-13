package me.archdev

import me.archdev.restapi.Boot
import me.archdev.utils.InMemoryPostgresStorage
import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend

class BootIT extends BaseServiceTest {

  InMemoryPostgresStorage
  implicit val sttpBackend = AkkaHttpBackend()

  "Service" should {

    "bind on port successfully and answer on health checks" in {
      awaitForResult(for {
        serverBinding <- Boot.startApplication()
        healthCheckResponse <- sttp.get(uri"http://localhost:9000/healthcheck").send()
        _ <- serverBinding.unbind()
      } yield {
        healthCheckResponse.code shouldBe 200
        healthCheckResponse.body shouldBe Right("OK")
      })
    }

  }

}
