package se.digiplant.scalr.api

import org.specs2.mutable.Specification
import java.io.{FileInputStream, File}
import org.apache.commons.io.IOUtils
import javax.imageio.ImageIO
import se.digiplant.res.api.Res
import se.digiplant.scalr.ScalrContext

object ScalrSpec extends Specification {

  implicit val ctx = new ScalrContext

  "Scalr Plugin" should {

    "resize image" in {
      val fileuid = Res.put(ctx.testFile)
      fileuid.isEmpty must beFalse
      val file = new File("tmp/default/5564/ac5e/5564ac5e3968e77b4022f55a23d36630bdeb0274.jpg")
      file.exists() must beTrue

      val testFile = Res.get(fileuid).get

      val resized = Scalr.resize(testFile, 50, 50)
      resized.exists() must beTrue

      val in = new FileInputStream(resized)
      val buf = ImageIO.read(in)
      IOUtils.closeQuietly(in)
      buf.getWidth must beEqualTo(50)
      buf.getHeight must beEqualTo(50)
    }

  }
}
