package se.digiplant.scalr

import org.specs2.mutable.Specification
import play.api.test._
import play.api.test.Helpers._

object ScalrResAssetsSpec extends Specification {

  "Scalr Res Assets Controller" should {

    "return resized image" in {
      val ctx = new ScalrContext
      running(ctx.app) {
        ctx.res.put(ctx.getTestFile)

        val result = ScalrResAssets.at("5564ac5e3968e77b4022f55a23d36630bdeb0274.jpg", 50, 50)(FakeRequest())

        status(result) must equalTo(OK)
        contentType(result) must beSome("image/jpeg")
      }
    }

  }
}
