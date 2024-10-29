package model.domain


object Game {

  //A private apply(...) Because We read/decode Game partially
  def apply(system: String,
            id: String,
            is_mobile_supported: Boolean,
            is_demo_supported: Boolean,
            is_portrait_view_supported: Boolean,

            is_visible: Boolean,
            is_available_for_anonymous_user: Boolean,
            name: String,
            thumbnail_url: String,
            background_url: String,

            game_type: String,
            categories: Array[Int],
            collections: Array[Int],
            providers: Array[Int],
            priority: Int,

            rating: Double,
            votes: Int,
            featured: Boolean,
            is_mobile_only: Boolean): Game = {
    new Game(system,
      id,
      is_mobile_supported,
      is_demo_supported,
      is_portrait_view_supported,
      is_visible,
      is_available_for_anonymous_user,
      name: String,
      background_url,
      thumbnail_url,
      game_type,
      categories,
      collections,
      providers,
      priority,
      rating,
      votes,
      featured,
      is_mobile_only
    )
  }


}

case class Game(system: String = null,
                id: String = null,
                is_mobile_supported: Boolean = true,
                is_demo_supported: Boolean = true,
                is_portrait_view_supported: Boolean = true,
                is_visible: Boolean = true,
                is_available_for_anonymous_user: Boolean = true,
                name: String = null,
                background_url: String = null,
                thumbnail_url: String = null,
                game_type: String = null,
                categories: Array[Int] = Array.empty[Int],
                collections: Array[Int] = Array.empty[Int],
                providers: Array[Int] = Array.empty[Int],
                priority: Int = 0,
                rating: Double = 0.0,
                votes: Int = 0,
                featured: Boolean = false,
                is_mobile_only: Boolean = false,
                disallowed_for_domain_groups: Array[Int] = Array.empty[Int],
                disallowed_for_product_markets: Array[Int] = Array.empty[Int],
                disallowed_for_domain_group_ids: Array[Int] = Array.empty[Int],
                disallowed_for_product_market_ids: Array[Int] = Array.empty[Int]
               ) {
  override def toString: String = s"/${this.system}/${this.id}-${this.name.replaceAll(" ", "-")}"
}



