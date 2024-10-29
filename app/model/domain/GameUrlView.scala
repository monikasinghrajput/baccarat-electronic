package model.domain

case class GameUrlView(name: String = "",
                       id: String = "",
                       game_name: String = "",
                       game_type: String = "",
                       provider_name: String = "",
                       is_visible: Boolean = false,
                       is_demo_supported: Boolean = false,
                       is_mobile_only: Boolean = false,
                       is_mobile_supported: Boolean = false,
                       is_portrait_view_supported: Boolean = false,
                       is_available_for_anonymous_user: Boolean = false,
                       game_url: String = "",
                       thumbnail_url: String = "",
               ) {
  override def toString: String = s"{this.name}"
}

