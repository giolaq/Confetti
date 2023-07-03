package dev.johnoreilly.confetti.speakers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.rememberTvLazyGridState
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ShapeDefaults
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import dev.johnoreilly.confetti.R
import dev.johnoreilly.confetti.decompose.SpeakersComponent
import dev.johnoreilly.confetti.decompose.SpeakersUiState
import dev.johnoreilly.confetti.fragment.SpeakerDetails
import dev.johnoreilly.confetti.ui.ErrorView
import dev.johnoreilly.confetti.ui.HomeScaffold
import dev.johnoreilly.confetti.ui.LoadingView
import dev.johnoreilly.confetti.ui.isExpanded


@Composable
fun SpeakersRoute(
    component: SpeakersComponent,
    windowSizeClass: WindowSizeClass,
    topBarActions: @Composable RowScope.() -> Unit,
) {
    val uiState by component.uiState.subscribeAsState()

    HomeScaffold(
        title = stringResource(R.string.speakers),
        windowSizeClass = windowSizeClass,
        topBarActions = topBarActions,
    ) {
        when (val uiState1 = uiState) {
            is SpeakersUiState.Success -> {
                if (windowSizeClass.isExpanded) {
                    SpeakerGridView(uiState1.speakers, component::onSpeakerClicked)
                }
            }

            is SpeakersUiState.Loading -> LoadingView()
            is SpeakersUiState.Error -> ErrorView {

            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SpeakerGridView(
    speakers: List<SpeakerDetails>, navigateToSpeaker: (id: String) -> Unit
) {
    val speakerGridState = rememberTvLazyGridState()
    TvLazyVerticalGrid(
        state = speakerGridState,
        modifier = Modifier.fillMaxSize(),
        columns = TvGridCells.Fixed(6),
        content = {
            speakers.forEachIndexed { index, speakerDetails ->
                item {
                    key(speakerDetails.id) {
                        StandardCardLayout(
                            modifier = Modifier
                                .aspectRatio(1 / 1.5f)
                                .padding(vertical = 8.dp)
                                .padding(end = if ((index + 1) % 6 == 0) 0.dp else 16.dp), imageCard = {
                                CardLayoutDefaults.ImageCard(
                                    onClick = { navigateToSpeaker(speakerDetails.id) },
                                    shape = CardDefaults.shape(shape = ShapeDefaults.ExtraSmall),
                                    scale = CardDefaults.scale(focusedScale = 1f),
                                    border = CardDefaults.border(
                                        focusedBorder = Border(
                                            border = BorderStroke(
                                                width = 2.dp, color = MaterialTheme.colorScheme.onTertiary
                                            ), shape = ShapeDefaults.ExtraSmall
                                        )
                                    ),
                                    interactionSource = it
                                ) {
                                    if (speakerDetails.photoUrl?.isNotEmpty() == true) {
                                        Box(contentAlignment = Alignment.CenterStart) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(speakerDetails.photoUrl)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = speakerDetails.name,
                                                contentScale = ContentScale.Crop,
                                                placeholder = painterResource(dev.johnoreilly.confetti.shared.R.drawable.ic_person_black_24dp),
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            Text(
                                                modifier = Modifier.padding(16.dp).align(Alignment.BottomStart),
                                                text = speakerDetails.name,
                                                style = MaterialTheme.typography.bodySmall
                                                    .copy(
                                                        shadow = Shadow(
                                                            offset = Offset(0.5f, 0.5f),
                                                            blurRadius = 5f
                                                        ),
                                                        color = Color.White
                                                    ),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.size(150.dp))
                                    }
                                }
                            }, title = { })
                    }
                }
            }
        })
}

@Composable
fun SpeakerItemView(
    speaker: SpeakerDetails, navigateToSpeaker: (id: String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { navigateToSpeaker(speaker.id) }),
        headlineContent = {
            Text(text = speaker.name)
        },
        supportingContent = speaker.tagline?.let { company ->
            {
                Text(company)
            }
        },
        leadingContent = speaker.photoUrl?.let { photoUrl ->
            {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = speaker.name,
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(dev.johnoreilly.confetti.shared.R.drawable.ic_person_black_24dp),
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }
        }
    )
}
