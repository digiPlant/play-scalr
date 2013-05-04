package controllers

import play.api.mvc.Controller
import se.digiplant.scalr._
import models.Image

object Images extends Controller {
  // Default Source
  def at(fileuid: String, width: Int, height: Int) = ScalrResAssets.at(fileuid, width, height, source="default")
  def crop(fileuid: String, width: Int, height: Int) = ScalrResAssets.at(fileuid, width, height, mode = "crop", source="default")
  def thumb(fileuid: String) = ScalrResAssets.at(fileuid, 120, 120, mode = "crop", source="default")

  // ProfileImage Source
  def profile(fileuid: String) = ScalrResAssets.at(fileuid, 120, 120, mode = "crop", source="profileimage")
}
