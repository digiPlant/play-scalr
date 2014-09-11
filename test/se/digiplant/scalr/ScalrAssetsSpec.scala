package se.digiplant.scalr

import org.specs2.mutable.Specification
import play.api.test._
import play.api.test.Helpers._

object ScalrAssetsSpec extends Specification {

  "Scalr Controller" should {

    "return resized image" in new ScalrContext {
      val result = ScalrAssets.at("/test/resources", "digiPlant.jpg", 50, 50)(FakeRequest())
      status(result) must equalTo(OK)
      contentType(result) must beSome("image/jpeg")
    }

    "return resized image in subfolder" in new ScalrContext {
      val result = ScalrAssets.at("/test/resources", "subdir/test.jpg", 50, 50)(FakeRequest())
      status(result) must equalTo(OK)
      contentType(result) must beSome("image/jpeg")
    }

  }
}
