package model.domain

object GameView {

}
case class GameView(id: String = null,
                is_demo_supported: Boolean = false,
                is_available_for_anonymous_user: Boolean = false,
                is_mobile_supported: Boolean = true,
                is_mobile_only: Boolean = false,
                is_portrait_view_supported: Boolean = true,
                name: String = null,
                providers: Array[Int] = Array.empty[Int],
                system: String = null,
                game_type: String = null,
                thumbnail_url: String = null,
               ) {
  override def toString: String = s"/${this.system}/${this.id}-${this.name.replaceAll(" ", "-")}"
}

