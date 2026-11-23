package io.aparok.app.core_ui.uitests

import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import io.aparok.app.core_models.Url
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.tools.ClipboardInterceptor
import io.aparok.app.core_models.ThemeColor
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.test_utils.TestFragment
import io.aparok.app.test_utils.utils.checkHasTextColor
import io.aparok.app.test_utils.utils.checkIsDisplayed
import io.aparok.app.test_utils.utils.onItemView
import io.aparok.app.test_utils.utils.rVMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import io.aparok.app.test_utils.R as R_test


@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.P],
    instrumentedPackages = [
        // required to access final members on androidx.loader.content.ModernAsyncTask
        "androidx.loader.content"
    ]
)
class NumberedBlockTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    lateinit var scenario: FragmentScenario<TestFragment>

    private val clipboardInterceptor: ClipboardInterceptor = object : ClipboardInterceptor {
        override fun onClipboardAction(action: ClipboardInterceptor.Action) {}
        override fun onBookmarkPasted(url: Url) {}
    }

    @Before
    fun setUp() {
        context.setTheme(R.style.Theme_MaterialComponents)
        scenario = launchFragmentInContainer()
    }

    @Test
    fun `should change text color - when payload`() {
        scenario.onFragment {
            val recycler = givenRecycler(it)
            val adapter = givenAdapter(listOf(givenNumbered(ThemeColor.RED)))
            recycler.adapter = adapter


            R_test.id.recycler.rVMatcher().apply {
                onItemView(0, R.id.number).checkIsDisplayed()
                onItemView(0, R.id.number).checkHasTextColor(
                    context.getColor(R.color.palette_dark_red)
                )
                onItemView(0, R.id.numberedListContent).checkIsDisplayed()
                onItemView(0, R.id.numberedListContent).checkHasTextColor(
                    context.getColor(R.color.palette_dark_red)
                )
            }


            adapter.updateWithDiffUtil(listOf(givenNumbered(ThemeColor.BLUE)))

            R_test.id.recycler.rVMatcher().apply {
                onItemView(0, R.id.number).checkIsDisplayed()
                onItemView(0, R.id.number).checkHasTextColor(
                    context.getColor(R.color.palette_dark_blue)
                )
                onItemView(0, R.id.numberedListContent).checkIsDisplayed()
                onItemView(0, R.id.numberedListContent).checkHasTextColor(
                    context.getColor(R.color.palette_dark_blue)
                )
            }
        }
    }

    private fun givenRecycler(it: Fragment): RecyclerView =
        it.view!!.findViewById<RecyclerView>(R_test.id.recycler).apply {
            layoutManager = LinearLayoutManager(context)
        }

    private fun givenNumbered(color: ThemeColor) = BlockView.Text.Numbered(
        text = "text",
        id = "id",
        mode = BlockView.Mode.EDIT,
        indent = 0,
        number = 123,
        color = color
    )
}