package model.domain


case class Provider(id: Int, title: Title, icon_url: String, thumbnail_url_normal: String, thumbnail_url_big: String) {
  override def toString: String = s"Provider -> $id-$title"
}

case class Title(en: String) {
  override def toString: String = s"$en"
}


