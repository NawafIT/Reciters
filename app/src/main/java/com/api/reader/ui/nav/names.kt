package com.api.reader.ui.nav

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import com.api.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.api.reader.ui.api.Constant.listNumberOfSurh
import com.api.reader.ui.api.Constant.listSurhName
import com.api.reader.ui.apiclass.Reciter
import com.api.reader.ui.mvvm.ViewModelReciter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Start(id: String?, navController: NavController) {
    val mediaPlayer = MediaPlayer()
    val context = LocalContext.current
    BackHandler {
        navController.navigateUp()
        mediaPlayer.stop()
    }
    val view: ViewModelReciter = viewModel()
    val myReciter by view.myReciter.observeAsState()
    Scaffold(topBar = {
//        TopBarReader() {
//            navController.navigateUp()
//            mediaPlayer.stop()
//        }
    }) {
        if (myReciter != null) {
            val exoPlayer =  ExoPlayer.Builder(context).build()
            val list = myReciter!!.reciters.filterIndexed { index, reciter ->
                reciter.moshaf[0].surah_total == 114
            }
            val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
            val filteredArray = list.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBar(searchQuery, setSearchQuery) {
                    navController.navigateUp()
                    mediaPlayer.stop()
                }
                LazyColumn {
                    itemsIndexed(filteredArray) { index, reciter ->
                        CardInfo(reciter, index, stop = { mediaPlayer.reset() }) {
                            val s = filteredArray[it].moshaf[0].server + id
                            GlobalScope.launch(Dispatchers.Main) {

                                val dataSourceFactory = DefaultDataSourceFactory(context, "MyApp")
                                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                                    Uri.parse(s))
                                exoPlayer.setMediaSource(mediaSource)
                                exoPlayer.prepare()
                                exoPlayer.play()

                            }
                        }
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = Color.Red)
            }
        }
    }
}


@Composable
fun CardInfo(reciter: Reciter, index: Int, stop: () -> Unit, click: (index: Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    stop()
                    click(index)
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {


            Text(
                reciter.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "${index + 1}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 6.dp)

            )

        }
    }
}


@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    click: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxSize()
        ) {
            IconButton(onClick = { click() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", tint = Color.White)
            }
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                placeholder = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true,
                maxLines = 1
            )
        }

    }

}
