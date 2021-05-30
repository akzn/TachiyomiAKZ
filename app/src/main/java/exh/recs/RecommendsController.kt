package exh.recs

// import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.os.bundleOf
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import eu.kanade.tachiyomi.source.CatalogueSource
import eu.kanade.tachiyomi.ui.base.controller.withFadeTransaction
import eu.kanade.tachiyomi.ui.browse.source.SourceAdapter
import eu.kanade.tachiyomi.ui.browse.source.SourceController
import eu.kanade.tachiyomi.ui.browse.source.browse.BrowseSourceController
import eu.kanade.tachiyomi.ui.browse.source.browse.SourceItem
import exh.ui.smartsearch.SmartSearchController
import timber.log.Timber
import uy.kohesive.injekt.injectLazy

/**
 * Controller that shows the latest manga from the catalogue. Inherit [BrowseSourceController].
 */
class RecommendsController(
    bundle: Bundle
) : BrowseSourceController(bundle) {

    constructor(manga: Manga, source: CatalogueSource) : this(
        bundleOf(
            MANGA_ID to manga.id!!,
            SOURCE_ID_KEY to source.id
        )
    )

    /**
     * Adapter containing sources.
     */
    private var adapterSource: SourceAdapter? = null

    private val preferences: PreferencesHelper by injectLazy()

    override fun getTitle(): String? {
        return (presenter as? RecommendsPresenter)?.manga?.title
    }

    override fun createPresenter(): RecommendsPresenter {
        return RecommendsPresenter(args.getLong(MANGA_ID), args.getLong(SOURCE_ID_KEY))
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_search).isVisible = false
        menu.findItem(R.id.action_open_in_web_view).isVisible = false
        menu.findItem(R.id.action_settings).isVisible = false
    }

    override fun initFilterSheet() {
        // No-op: we don't allow filtering in recs
    }

    override fun onItemClick(view: View, position: Int): Boolean {
        val item = adapter?.getItem(position) as? SourceItem ?: return false

        // --> akz
        if (preferences.isLastSourceRecsEnabled().get()) {
            Timber.d("latestAsRecsSourcePreference : ${preferences.isLastSourceRecsEnabled().get()}")
            Timber.d("lastest source : ${preferences.lastUsedSource().get()}")

            openWithLastSource(item.manga.originalTitle)
        } else {
            openSmartSearch(item.manga.originalTitle)
        }
        // <-- akz

        return true
    }

    // --> akz

    private fun openWithLastSource(title: String) {
        val smartSearchConfig = SourceController.SmartSearchConfig(title)
        router.pushController(
            SmartSearchController(
                bundleOf(
                    SmartSearchController.ARG_SOURCE_ID to preferences.lastUsedSource().get(),
                    SmartSearchController.ARG_SMART_SEARCH_CONFIG to smartSearchConfig
                )
            ).withFadeTransaction()
        )
    }

    // <-- akz

    private fun openSmartSearch(title: String) {
        val smartSearchConfig = SourceController.SmartSearchConfig(title)
        router.pushController(
            SourceController(
                bundleOf(
                    SourceController.SMART_SEARCH_CONFIG to smartSearchConfig
                )
            ).withFadeTransaction()
        )
    }

    override fun onItemLongClick(position: Int) {
        return
    }

    companion object {
        const val MANGA_ID = "manga_id"
    }
}
