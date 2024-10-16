package new_structure.presentation.conversation

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun LoadingTimer(
    modifier: Modifier = Modifier,
    textColor: Color = LocalContentColor.current,
    isLoading: Boolean = true,
    incrementTimerDuration: Long = 70,
) {
    var job: Job? = null
    val formatter = remember { DecimalFormat("0.00") }

    var timer by remember { mutableIntStateOf(0) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            job = launch {
                while (true) {
                    timer += (incrementTimerDuration / 10).toInt()
                    delay(incrementTimerDuration)
                }
            }
        } else {
            job?.cancel()
        }
    }

    if (isLoading) {
        val formattedTimer by remember {
            derivedStateOf {
                formatter.format(timer / 100f)
            }
        }

        Text(
            modifier = modifier,
            text = formattedTimer,
            style = MaterialTheme.typography.caption,
            color = textColor.copy(0.6f)
        )
    }
}
