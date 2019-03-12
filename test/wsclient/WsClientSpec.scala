package wsclient

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.ws.ahc.AhcWSClient

class WsClientSpec extends WordSpec with Matchers with ScalaFutures with GuiceOneServerPerSuite {

  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  override implicit val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(10, Seconds), interval = Span(50, Millis))

  "AhcWsClient" should {
    "be stateless if we share an instance" in {
      val wsClient: AhcWSClient = AhcWSClient()

      val result_1 = wsClient.url("http://localhost:" + portNumber.value + "/echo-session").get().futureValue

      result_1.body shouldBe "{}"

      val result_2 = wsClient.url("http://localhost:" + portNumber.value + "/echo-session").get().futureValue

      // Play 2.6 = Pass
      // Play 2.7 = Fail [Actual   :"{["session-exists":"true"]}"] - For some reason the second call uses the cookie which is returned from the 1st call so restores the session.
      result_2.body shouldBe "{}"

      wsClient.close()
    }
  }

  "AhcWsClient" should {
    "be stateless if we create a fresh instance each time" in {
      val wsClient_1: AhcWSClient = AhcWSClient()
      val wsClient_2: AhcWSClient = AhcWSClient()

      val result_1 = wsClient_1.url("http://localhost:" + portNumber.value + "/echo-session").get().futureValue

      result_1.body shouldBe "{}"

      val result_2 = wsClient_2.url("http://localhost:" + portNumber.value + "/echo-session").get().futureValue

      // Play 2.6 = Pass
      // Play 2.7 = Pass
      result_2.body shouldBe "{}"

      wsClient_1.close()
      wsClient_2.close()
    }
  }
}
