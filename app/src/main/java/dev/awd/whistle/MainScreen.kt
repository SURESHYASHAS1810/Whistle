package dev.awd.whistle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.awd.whistle.managers.LapItem
import dev.awd.whistle.managers.StopwatchState
import dev.awd.whistle.managers.TimeItem
import dev.awd.whistle.ui.theme.WhistleTheme
import dev.awd.whistle.utils.formatTime
import dev.awd.whistle.utils.pad
import kotlin.time.Duration

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    lapList: List<LapItem>,
    time: TimeItem,
    stopwatchState: StopwatchState,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResumeClick: () -> Unit,
    onResetClick: () -> Unit,
    onLapClick: () -> Unit,

    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name).uppercase(),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.weight(0.5f))
        TimeCircle(
            time = time,
            stopwatchState = stopwatchState,
            onResetClick = onResetClick
        )
        Spacer(modifier = Modifier.weight(1f))
        if (lapList.isNotEmpty()) {
            LapsListView(laps = lapList)
        }
        BottomButtons(
            stopwatchState = stopwatchState,
            onStartClick = onStartClick,
            onStopClick = onStopClick,
            onResumeClick = onResumeClick,
            onLapClick = onLapClick
        )
    }
}

@Composable
fun TimeCircle(
    modifier: Modifier = Modifier,
    time: TimeItem,
    stopwatchState: StopwatchState,
    onResetClick: () -> Unit,
) {

    Box(
        modifier
            .wrapContentSize()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 24.dp,
                    shape = CircleShape,
                )
                .background(Color.White)
                .padding(8.dp)
                .size(300.dp)
        ) {
            val text = formatTime(
                time.minutes,
                time.seconds,
                time.millis,
            )
            Text(
                text = text,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (
            stopwatchState == StopwatchState.Stopped
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Reset",
                tint = Color.White,
                modifier = modifier
                    .clickable(onClick = onResetClick)
                    .size(74.dp)
                    .align(
                        Alignment.BottomCenter
                    )
                    .offset(y = 30.dp)
                    .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary)
                    .clip(CircleShape)
                    .padding(8.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = CircleShape,
                    )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LapsListView(
    modifier: Modifier = Modifier,
    laps: List<LapItem>
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(key1 = laps) {
        if (laps.isNotEmpty()) {
            scrollState.animateScrollToItem(laps.lastIndex)
        }
    }
    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(8.dp),
        modifier = modifier.sizeIn(maxHeight = 200.dp),
        reverseLayout = true,
        userScrollEnabled = true
    ) {

        itemsIndexed(laps) { index, item ->
            LapItem(
                index = index + 1,
                lapTime = item.time.toComponents { _, minutesComponent, secondsComponent, millisComponent ->
                    formatTime(
                        minutesComponent.pad(),
                        secondsComponent.pad(),
                        millisComponent.pad().take(2)
                    )

                },
                overAllTime = item.overallTime.toComponents { _, minutesComponent, secondsComponent, millisComponent ->
                    formatTime(
                        minutesComponent.pad(),
                        secondsComponent.pad(),
                        millisComponent.pad().take(2)
                    )

                }
            )

        }
        stickyHeader {
            Divider()
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = "Index")
                Text(text = "Lap Time")
                Text(text = "Overall")
            }

        }
    }
}

@Composable
private fun LapItem(
    modifier: Modifier = Modifier,
    index: Int,
    lapTime: String,
    overAllTime: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = index.pad())
        Text(text = lapTime)
        Text(text = overAllTime)
    }
}


@Composable
fun BottomButtons(
    modifier: Modifier = Modifier,
    stopwatchState: StopwatchState,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResumeClick: () -> Unit,
    onLapClick: () -> Unit,
) {
    AnimatedContent(
        targetState = stopwatchState,
        transitionSpec = {
            slideInVertically { width -> width } + fadeIn() togetherWith
                    slideOutVertically { width -> width } + fadeOut()
        },
        label = ""
    ) { state ->
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (state == StopwatchState.Started) {
                Button(
                    modifier = Modifier
                        .weight(0.5f)
                        .shadow(elevation = 50.dp),
                    onClick = onLapClick
                ) {
                    Text(text = "Lap")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Button(
                modifier = Modifier
                    .weight(0.5f)
                    .shadow(elevation = 50.dp),
                onClick =
                when (state) {
                    StopwatchState.Idle -> onStartClick
                    StopwatchState.Started -> onStopClick
                    StopwatchState.Stopped -> onResumeClick

                }
            ) {
                Text(
                    text = when (state) {
                        StopwatchState.Idle -> "Start"
                        StopwatchState.Started -> "Stop"
                        StopwatchState.Stopped -> "Resume"
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    WhistleTheme {
        MainScreen(
            lapList = listOf(LapItem(Duration.ZERO, Duration.ZERO)),
            time = TimeItem(
                minutes = "45",
                seconds = "02",
                millis = "88",
            ),
            stopwatchState = StopwatchState.Idle,
            onStartClick = {},
            onStopClick = {},
            onResumeClick = {},
            onResetClick = {},
            onLapClick = {},
        )
    }
}