package se.digiplant.resource.api

import org.specs2.mutable.Specification
import java.io.{FileInputStream, File}
import org.apache.commons.io.IOUtils
import se.digiplant.scalr.api.ScalrPlugin
import javax.imageio.ImageIO

object ScalrPluginSpec extends Specification {

  //override def is = args(sequential = true) ^ super.is

  "Scalr Plugin" should {

    "start" in new FakeApp {
      scalr must beAnInstanceOf[ScalrPlugin]
      res must beAnInstanceOf[ResourcePlugin]
    }

    "resize image" in new FakeApp {
      val fileuid = res.put(getTestFile)
      fileuid.isDefined must beEqualTo(true)
      new File("tmp/default/5564/ac5e/5564ac5e3968e77b4022f55a23d36630bdeb0274.jpg").exists() must beEqualTo(true)

      val testFile = res.get(fileuid.get).get

      val resized = scalr.resize(testFile, 50, 50)
      resized.exists() must beEqualTo(true)

      val in = new FileInputStream(resized)
      val buf = ImageIO.read(in)
      buf.getWidth must beEqualTo(50)
      buf.getHeight must beEqualTo(50)
      IOUtils.closeQuietly(in)
    }
  }

  step {
    // Delete tmp directory on end
    Files.tmp.delete()
    success
  }
}
