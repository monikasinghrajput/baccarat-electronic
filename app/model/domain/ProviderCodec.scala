package model.domain

import play.api.libs.json._

trait ProviderCodec {


  implicit val titleAutoDecoder: Reads[Title] = Json.reads[Title]
  implicit val titleAutoEncoder: Writes[Title] = Json.writes[Title]


  implicit val providerAutoDecoder: Reads[Provider] = Json.reads[Provider]
  implicit val providerAutoWriter: Writes[Provider] = Json.writes[Provider]

}
