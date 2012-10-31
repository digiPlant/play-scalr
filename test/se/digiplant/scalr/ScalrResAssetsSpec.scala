package se.digiplant.scalr

import org.specs2.mutable.Specification
import play.api.test._
import play.api.test.Helpers._
import se.digiplant.res.api.Res

object ScalrResAssetsSpec extends Specification {

  val ctx = new ScalrContext

  "Scalr Res Assets Controller" should {
    "return resized image" in {
      running(ctx.app) {
        Res.put(ctx.testFile)
        val result = ScalrResAssets.at("5564ac5e3968e77b4022f55a23d36630bdeb0274.jpg", 50, 50)(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome("image/jpeg")
      }
    }
  }
}
