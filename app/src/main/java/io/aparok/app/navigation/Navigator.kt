package io.aparok.app.navigation

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.navOptions
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.presentation.navigation.AppNavigation
import io.aparok.app.presentation.widgets.collection.Subscription
import io.aparok.app.ui.allcontent.AllContentFragment
import io.aparok.app.ui.auth.account.DeletedAccountFragment
import io.aparok.app.ui.chats.ChatFragment
import io.aparok.app.ui.discussions.DiscussionFragment
import io.aparok.app.ui.date.DateObjectFragment
import io.aparok.app.ui.editor.EditorFragment
import io.aparok.app.ui.editor.EditorModalFragment
import io.aparok.app.ui.home.WidgetsScreenFragment
import io.aparok.app.ui.primitives.CreateTypeFragment
import io.aparok.app.ui.primitives.ObjectTypeFieldsFragment
import io.aparok.app.ui.primitives.ObjectTypeFragment
import io.aparok.app.ui.profile.ParticipantFragment
import io.aparok.app.ui.search.v2.SearchV2Fragment
import io.aparok.app.ui.sets.ObjectSetFragment
import io.aparok.app.ui.templates.EditorTemplateFragment.Companion.TYPE_TEMPLATE_EDIT
import io.aparok.app.ui.templates.EditorTemplateFragment.Companion.TYPE_TEMPLATE_SELECT
import io.aparok.app.ui.templates.TemplateSelectFragment
import io.aparok.app.ui.widgets.collection.CollectionFragment
import timber.log.Timber

class Navigator : AppNavigation {

    private var navController: NavController? = null

    private fun NavController.safePopBackStack(@IdRes destinationId: Int, inclusive: Boolean): Boolean {
        return try {
            popBackStack(destinationId, inclusive)
        } catch (e: IllegalArgumentException) {
            Timber.w(e, "popBackStack failed - destination not in back stack: id=$destinationId")
            false
        }
    }

    override fun openSpaceSettings() {
        try {
            navController?.navigate(R.id.action_open_space_settings)
        } catch (e: Exception) {
            Timber.e(e, "Error while opening settings")
        }
    }

    override fun openChat(target: Id, space: Id, popUpToVault: Boolean) {
        navController?.navigate(
            R.id.chatScreen,
            ChatFragment.args(
                ctx = target,
                space = space,
                popUpToVault = popUpToVault
            )
        )
    }

    override fun openDiscussion(target: Id, space: Id) {
        navController?.navigate(
            R.id.discussionScreen,
            DiscussionFragment.args(
                ctx = target,
                space = space
            )
        )
    }

    override fun openDocument(target: Id, space: Id) {
        navController?.navigate(
            R.id.objectNavigation,
            EditorFragment.args(
                ctx = target,
                space = space
            )
        )
    }

    override fun openModalTemplateSelect(
        template: Id,
        templateTypeId: Id,
        templateTypeKey: Key,
        space: Id,
    ) {
        navController?.navigate(
            R.id.nav_editor_modal,
            bundleOf(
                EditorModalFragment.ARG_TEMPLATE_ID to template,
                EditorModalFragment.ARG_TEMPLATE_TYPE_ID to templateTypeId,
                EditorModalFragment.ARG_TEMPLATE_TYPE_KEY to templateTypeKey,
                EditorModalFragment.ARG_SCREEN_TYPE to TYPE_TEMPLATE_SELECT,
                EditorModalFragment.ARG_SPACE_ID to space
            )
        )
    }

    override fun openModalTemplateEdit(
        template: Id,
        templateTypeId: Id,
        templateTypeKey: Key,
        space: Id,
    ) {
        navController?.navigate(
            R.id.nav_editor_modal,
            bundleOf(
                EditorModalFragment.ARG_TEMPLATE_ID to template,
                EditorModalFragment.ARG_TEMPLATE_TYPE_ID to templateTypeId,
                EditorModalFragment.ARG_TEMPLATE_TYPE_KEY to templateTypeKey,
                EditorModalFragment.ARG_SCREEN_TYPE to TYPE_TEMPLATE_EDIT,
                EditorModalFragment.ARG_SPACE_ID to space
            )
        )
    }

    override fun launchDocument(target: String, space: Id) {
        navController?.navigate(
            R.id.objectNavigation,
            EditorFragment.args(
                ctx = target,
                space = space
            ),
            navOptions {
                launchSingleTop = true
                popUpTo(R.id.globalSearchScreen) {
                    inclusive = true
                }
            }
        )
    }

    override fun launchCollections(subscription: Subscription, space: Id) {
        navController?.navigate(
            R.id.homeScreenWidgets,
            CollectionFragment.args(
                subscription = subscription.id,
                space = space
            )
        )
    }

    override fun launchObjectSet(target: Id, space: Id) {
        navController?.navigate(
            R.id.dataViewNavigation,
            ObjectSetFragment.args(
                ctx = target,
                space = space
            ),
            navOptions {
                launchSingleTop = true
                popUpTo(R.id.globalSearchScreen) {
                    inclusive = true
                }
            }
        )
    }

    override fun exit(space: Id) {
        val popped = navController?.popBackStack()
        if (popped == false) {
            navController?.navigate(
                R.id.homeScreen,
                WidgetsScreenFragment.args(space = space)
            )
        }
    }

    override fun exitToDesktop(space: Id) {
        navController?.let { controller ->
            val popped = controller.safePopBackStack(R.id.homeScreen, inclusive = false)
            if (!popped) {
                controller.navigate(
                    R.id.homeScreen,
                    WidgetsScreenFragment.args(space = space)
                )
            }
        }
    }

    override fun exitToVault() {
        navController?.let { controller ->
            val popped = controller.safePopBackStack(R.id.vaultScreen, inclusive = true)
            if (!popped) {
                controller.navigate(R.id.vaultScreen)
            }
        }
    }

    override fun exitToSpaceHome() {
        runCatching {
            navController?.let { controller ->
                val isPopped = controller.popBackStack(R.id.chatScreen, inclusive = false)
                if (!isPopped) {
                    controller.popBackStack(R.id.homeScreen, inclusive = false)
                }
            }
        }.onFailure {
            Timber.e(it, "Error while exiting to space home")
        }
    }

    @SuppressLint("RestrictedApi") // NavController.currentBackStack is RestrictTo(LIBRARY_GROUP); lint is non-fatal
    override fun popToBackStackEntry(entryId: String) {
        val controller = navController ?: return
        runCatching {
            val stack = controller.currentBackStack.value
            if (stack.none { it.id == entryId }) {
                Timber.w("popToBackStackEntry: entry $entryId no longer in back stack")
                return
            }
            var guard = stack.size + 4
            while (controller.currentBackStackEntry?.id != entryId && guard-- > 0) {
                if (!controller.popBackStack()) break
            }
        }.onFailure {
            Timber.e(it, "Error while popping to back stack entry")
        }
    }

    override fun openGlobalSearch(space: Id) {
        // Unified search (search v2): in-space entry seeds the space scope token.
        navController?.navigate(
            resId = R.id.searchV2Screen,
            args = SearchV2Fragment.args(space = space)
        )
    }

    override fun openShareScreen(space: SpaceId) {
        // Space sharing removed in offline sovereign mode
    }

    override fun openObjectSet(
        target: Id,
        space: Id,
        view: Id?,
        isPopUpToDashboard: Boolean
    ) {
        if (isPopUpToDashboard) {
            navController?.navigate(
                R.id.dataViewNavigation,
                ObjectSetFragment.args(
                    ctx = target,
                    space = space,
                    view = view
                ),
                navOptions {
                    popUpTo(R.id.homeScreen) {
                        inclusive = false
                    }
                }
            )
        } else {
            navController?.navigate(
                R.id.dataViewNavigation,
                ObjectSetFragment.args(
                    ctx = target,
                    space = space,
                    view = view
                )
            )
        }
    }

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        navController = null
    }

    override fun openUpdateAppScreen() {
        // In-app update check removed in Aparok sovereign offline build
    }

    override fun deletedAccountScreen(deadline: Long) {
        navController?.navigate(
            R.id.deletedAccountNavigation,
            bundleOf(DeletedAccountFragment.DEADLINE_KEY to deadline),
            navOptions {
                popUpTo = R.id.main_navigation
            }
        )
    }

    override fun logout() {
        navController?.navigate(R.id.actionLogout)
    }

    override fun openRemoteFilesManageScreen(subscription: Id, space: Id) {
        // Remote file management removed in offline sovereign mode
    }

    override fun openTemplatesModal(typeId: Id) {
        navController?.navigate(
            R.id.nav_templates_modal,
            bundleOf(
                TemplateSelectFragment.ARG_TYPE_ID to typeId
            )
        )
    }

    override fun openAllContent(space: Id) {
        navController?.navigate(
            resId = R.id.allContentScreen,
            args = AllContentFragment.args(space)
        )
    }

    override fun openCreateObjectTypeScreen(spaceId: Id) {
        val args = CreateTypeFragment.args(
            spaceId = spaceId
        )
        navController?.navigate(
            resId = R.id.createObjectTypeScreen,
            args = args
        )
    }

    override fun openDateObject(
        objectId: Id,
        space: Id
    ) {
        navController?.navigate(
            resId = R.id.dateObjectScreen,
            args = DateObjectFragment.args(
                objectId = objectId,
                space = space)
        )
    }

    override fun openParticipantObject(
        objectId: Id,
        space: Id
    ) {
        navController?.navigate(
            resId = R.id.participantScreen,
            args = ParticipantFragment.args(
                objectId = objectId,
                space = space
            )
        )
    }

    override fun openObjectType(
        objectId: Id,
        space: Id,
        view: Id?
    ) {
        navController?.navigate(
            resId = R.id.objectTypeNavigation,
            args = ObjectTypeFragment.args(
                objectId = objectId,
                space = space,
                view = view
            )
        )
    }

    override fun openCurrentObjectTypeFields(
        objectId: Id,
        space: Id
    ) {
        navController?.navigate(
            resId = R.id.objectTypeFieldsScreen,
            args = ObjectTypeFieldsFragment.args(
                objectId = objectId,
                space = space
            )
        )
    }
}
