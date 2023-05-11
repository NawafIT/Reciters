package com.api.reader

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.api.reader.ui.api.Constant
import com.api.reader.ui.api.Constant.listNumberOfSurh
import com.api.reader.ui.apiclass.Reciter
import com.api.reader.ui.mvvm.ViewModelReciter
import com.api.reader.ui.nav.Nav
import com.api.reader.ui.theme.ReaderTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReaderTheme {
                // A surface container using the 'background' color from the theme

                Nav()

            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Greeting(navController: NavController) {
    val titles = listOf("استماع", "قراءة")
    val pagerState = rememberPagerState(pageCount = titles.size)
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { TopAppBar(title = { Text("تجربة") }) }
    ) {
        Column {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                    )
                }
            }
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> {
                        // Content for Tab 1
                        MyApp(navController)
                    }
                    1 -> {
                        // Content for Tab 2
                        MyApp(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(navController: NavController) {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    var isNetworkAvailable by remember {
        mutableStateOf(connectivityManager?.activeNetworkInfo?.isConnected ?: false)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn {
            itemsIndexed(Constant.listSurhName) { index, obj ->
                Display(obj, index) {
                    isNetworkAvailable =
                        connectivityManager?.activeNetworkInfo?.isConnected ?: false
                    if (isNetworkAvailable) {
                        navController.navigate(route = "link?id=${Constant.listNumberOfSurh[it]}")
                    } else {
                        Toast.makeText(context, "يجب الاتصال بالانترنت", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
    }


}

@Composable
fun Display(
    name:
    String,
    index: Int,
    click: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    click(index)
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {


            Text(
                name,
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