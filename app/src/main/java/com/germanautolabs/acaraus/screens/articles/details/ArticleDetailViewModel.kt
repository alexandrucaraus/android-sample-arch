package com.germanautolabs.acaraus.screens.articles.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.germanautolabs.acaraus.lib.asStateFlow
import com.germanautolabs.acaraus.models.Article
import kotlinx.coroutines.CoroutineScope
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class ArticleDetailViewModel(
    handle: SavedStateHandle,
    @InjectedParam article: Article,
    @InjectedParam coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {

    val state = handle.asStateFlow(key = ARTICLE_KEY, defaultValue = article)

    companion object {
        const val ARTICLE_KEY = "artk"
    }
}

val dummyArticleDetails = Article(
    id = "",
    imageURL = "https://picsum.photos/200",
    title = "Belkin's 3-in-1 MagSafe Charging Stand Is as Cheap Now as It Was During Prime Day",
    source = "CNET",
    contentUrl = "https://www.cnet.com/news/belkins-3-in-1-magsafe-charging-stand-is-as-cheap-now-as-it-was-during-prime-day/",
    description = null,
    content = "\n" +
        "If you're someone who's got an iPhone, an Apple Watch and some AirPods, then having a convenient way to charge them all is a huge boon. Belkin has just the ticket with its three-in-one BoostCharge Pro MagSafe charger, now on sale for just \$100 at Amazon -- the same record-low price it hit during Prime Day. It's one of our favorite wireless chargers for those after a MagSafe-enabled solution. \n" +
        "\n" +
        "This story is part of Amazon Prime Day, CNET's guide to everything you need to know and how to find the best deals.\n" +
        "See at Amazon\n" +
        "Only the black version of this wireless charger is available at this special price, but with it, you'll get support for MagSafe wireless iPhone charging at the faster 15-watt speed. Apple Watch fast charging is also supported, meaning the Apple Watch Series 7 and later will charge 33% faster with this Belkin unit than on other chargers. There's also a space for wirelessly charging your AirPods or AirPods Pro, and because this is one unit, the included charging brick only takes up one AC outlet.\n" +
        "\n" +
        "Hey, did you know? CNET Deals texts are free, easy and save you money.\n" +
        "\n" +
        "Thanks to the strong MagSafe connection, you can turn your iPhone into landscape mode and make use of iOS 17's funky StandBy feature as well, making this an ideal nightstand or desk charger. If you're looking for some deals that are a little bit cheaper, then have a look at our picks for the best Amazon deals under \$100.  ",
)
