package com.example.vkclient.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vkclient.R
import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.ui.theme.DarkBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedContent(component: NewsFeedComponent) {

    val state by component.model.collectAsState()

    when (val screenState = state.state) {
        is NewsFeedStore.State.NewsFeedState.Content -> {
            Content(
                posts = screenState.feedPosts,
                nextDataIsLoading = screenState.nextDataIsLoading,
                onCommentClickListener = { component.onCommentsClicked(it) },
                onPostDismissed = { component.deletePost(it) },
                onLikeClicked = { component.onLikeClicked(it) },
                onContentHasScrolled = { component.onVisibleContentHasScrolled() }
            )
        }

        NewsFeedStore.State.NewsFeedState.Loading -> {
            Loading()
        }

        else -> {}
    }

}


@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = DarkBlue)
    }
}

//<editor-fold desc="FeedPosts">
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun Content(
//    paddingValues: PaddingValues,
    posts: List<FeedPost>,
    nextDataIsLoading: Boolean,
    onCommentClickListener: (FeedPost) -> Unit,
    onPostDismissed: (FeedPost) -> Unit,
    onLikeClicked: (FeedPost) -> Unit,
    onContentHasScrolled: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 8.dp),
//        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val scope = CoroutineScope(Dispatchers.Main.immediate)

        items(items = posts, key = { it.id }) { feedPost ->
            val dismissState = rememberSwipeToDismissBoxState()
            LaunchedEffect(key1 = dismissState.dismissDirection) {
                scope.launch {
                    if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                        onPostDismissed(feedPost)
                    }
                }
            }
            SwipeToDismissBox(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                enableDismissFromEndToStart = true,
                enableDismissFromStartToEnd = false,
                backgroundContent = {}
            ) {
                PostCard(
                    feedPost = feedPost,
                    onCommentClickListener = {
                        onCommentClickListener(feedPost)
                    },
                    onLikeClickListener = { _ ->
                        onLikeClicked(feedPost)
                    },
                )
            }
        }
        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = DarkBlue)
                }
            } else {
                onContentHasScrolled()
            }
        }
    }
}
//</editor-fold>

