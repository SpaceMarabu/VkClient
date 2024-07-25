package com.example.vkclient.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.vkclient.presentation.home.bottomNavigationSupport.NavigationItem
import com.example.vkclient.presentation.home.bottomNavigationSupport.NavigationKey
import com.example.vkclient.presentation.home.newsFlow.NewsFlowContent

@Composable
fun HomeFlowContent(component: HomeFlowComponent) {

    Children(
        stack = component.stack
    ) {

        var selectedItemKey by remember {
            mutableStateOf(NavigationItem.Home.screen.navigationKey)
        }

        Scaffold(
            modifier = Modifier
                .padding(top = 8.dp),
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                ) {

                    val items = listOf(
                        NavigationItem.Home,
                        NavigationItem.Favourite,
                        NavigationItem.Profile
                    )
                    items.forEach { item ->

                        val selected = item.screen.navigationKey == selectedItemKey

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    when (item.screen.navigationKey) {
                                        NavigationKey.HOME -> {
                                            component.onHomeClicked()
                                        }

                                        NavigationKey.FAVOURITE -> {
                                            component.onFavouriteClicked()
                                        }

                                        NavigationKey.PROFILE -> {
                                            component.onProfileClicked()
                                        }
                                    }
                                }
                            },
                            icon = {
                                Icon(item.icon, contentDescription = null)
                            },
                            label = {
                                Text(text = stringResource(id = item.titleResId))
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->

            when (val instance = it.instance) {
                is HomeFlowComponent.Child.NewsFlow -> {
                    selectedItemKey = instance.navKey
                    NewsFlowContent(
                        component = instance.component,
                        paddingValues = paddingValues
                    )
                }

                //костыль
                is HomeFlowComponent.Child.FavouriteFlow -> {
                    selectedItemKey = instance.navKey
                    TextCounter(name = "Favourite")
                }

                //костыль
                is HomeFlowComponent.Child.ProfileFlow -> {
                    selectedItemKey = instance.navKey
                    TextCounter(name = "Profile")
                }
            }
        }
    }

}

@Composable
private fun TextCounter(name: String) {
    var count by rememberSaveable {
        mutableIntStateOf(0)
    }

    Text(
        modifier = Modifier.clickable { count++ },
        text = "$name Count: $count",
        color = Color.Black
    )
}