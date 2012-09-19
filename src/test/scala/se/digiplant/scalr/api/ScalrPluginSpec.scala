package se.digiplant.scalr.api

import org.specs2.mutable.Specification
import java.io.{FileInputStream, File}
import org.apache.commons.io.IOUtils
import javax.imageio.ImageIO
import se.digiplant.resource.api.ResourcePlugin

object ScalrPluginSpec extends Specification {

  implicit val ctx = new ScalrContext

  "Scalr Plugin" should {

    "start" in {
      ctx.scalr must beAnInstanceOf[ScalrPlugin]
      ctx.res must beAnInstanceOf[ResourcePlugin]
    }

    "resize image" in {
      val fileuid = ctx.res.put(ctx.getTestFile)
      fileuid.isDefined must beEqualTo(true)
      new File("tmp/default/5564/ac5e/5564ac5e3968e77b4022f55a23d36630bdeb0274.jpg").exists() must beEqualTo(true)

      val testFile = ctx.res.get(fileuid.get).get

      val resized = ctx.scalr.resize(testFile, 50, 50)
      resized.exists() must beEqualTo(true)

      val in = new FileInputStream(resized)
      val buf = ImageIO.read(in)
      IOUtils.closeQuietly(in)
      buf.getWidth must beEqualTo(50)
      buf.getHeight must beEqualTo(50)
    }

  }
}
