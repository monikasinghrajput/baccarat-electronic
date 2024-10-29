package model.domain

import play.api.libs.json._


case class Pictures(bg: String)
case class Colors(firstColor: String,
                  gradientTo: String,
                  accentColor: String,
                  secondColor: String,
                  gradientFrom: String,
                  gradientPoint: String)
case class Extra(colors: Colors,
                 pictures: Pictures,
                 locations: Array[String])
case class Collection(id: Int,
                      title: Title,
                      icon_url: String,
                      extra: Extra,
                      disallowed_for_domain_groups: Array[Int],
                      disallowed_for_product_markets: Array[String],
                      disallowed_for_domain_group_ids: Array[Int],
                      disallowed_for_product_market_ids: Array[Int],
                     )

trait CollectionCodec {

  implicit val titleDecoder: Reads[Title] = Json.reads[Title]
  implicit val titleEncoder: Writes[Title] = Json.writes[Title]

  implicit val picturesEncoder: Writes[Pictures] = Json.writes[Pictures]
  implicit val picturesDecoder: Reads[Pictures] = Json.reads[Pictures]

  implicit val colorsEncoder: Writes[Colors] = Json.writes[Colors]
  implicit val colorsDecoder: Reads[Colors] = Json.reads[Colors]

  implicit val extraEncoder: Writes[Extra] = Json.writes[Extra]
  implicit val extraDecoder: Reads[Extra] = Json.reads[Extra]

  implicit val collectionEncoder: Writes[Collection] = Json.writes[Collection]
  implicit val collectionDecoder: Reads[Collection] = Json.reads[Collection]
}