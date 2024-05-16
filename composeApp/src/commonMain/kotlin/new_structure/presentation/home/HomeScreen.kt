package new_structure.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import new_structure.presentation.home.model.HomeItem

@Composable
fun HomeScreen(items: List<HomeItem>) {
    LazyColumn {
        items(items) {
            Box(
                modifier = Modifier.clickable { it.onClick() }
                    .padding(16.dp),
            ) {
                Text(it.name)
            }
        }
    }
}
