package com.example.shopease.data.model

data class Review(
    val username : String,
    val rating : Int,
    val date : String,
    val review: String
)

val fakeReviews = listOf(
    Review("Alice", 5, "2025-10-10", "Excellent quality! I am extremely impressed with the craftsmanship. Highly recommend for anyone looking for reliability."),

    Review("Bob", 4, "2025-10-09", "Works as expected and I'm very satisfied. Minor improvements could be made, but overall a solid purchase."),

    Review("Charlie", 3, "2025-10-08", "Not bad, but could be better. It works okay, though some features feel incomplete. Average product for the price."),

    Review("Diana", 5, "2025-10-07", "Amazing product! It has made my daily tasks easier and I love the quality. Customer service was very helpful."),

    Review("Ethan", 4, "2025-10-06", "Good value for the price. Performs reliably, though a few small tweaks would make it perfect. Solid overall."),

    Review("Fiona", 5, "2025-10-05", "Fast shipping and excellent support. The product works exactly as described. A smooth buying experience."),

    Review("George", 4, "2025-10-04", "Very functional and easy to use. I like the design and overall performance. Would recommend with minor reservations."),

    Review("Hannah", 5, "2025-10-03", "Exceeded my expectations! The build quality is fantastic and it works flawlessly. Definitely worth the price."),

    Review("Ian", 3, "2025-10-02", "It's okay, not terrible but not great either. Some features are lacking and the overall experience is average."),

    Review("Julia", 5, "2025-10-01", "Absolutely love it! The product is durable and reliable, and it arrived sooner than expected. Very happy with this purchase."),

    Review("Kevin", 4, "2025-09-30", "Good overall and works as intended. A few minor issues, but nothing that impacts daily use. Would buy again."),

    Review("Laura", 5, "2025-09-29", "Fantastic product! Everything about it feels premium and high-quality. I’ve already recommended it to friends."),

    Review("Mike", 3, "2025-09-28", "Average product. Works fine, but lacks some features I expected. Decent for the price, but nothing outstanding."),

    Review("Nina", 5, "2025-09-27", "Love it! The performance is top-notch and it’s very easy to use. Customer support was friendly and helpful."),

    Review("Oscar", 4, "2025-09-26", "Solid product with good features. A couple of minor flaws, but overall I’m satisfied and would consider buying again.")

)