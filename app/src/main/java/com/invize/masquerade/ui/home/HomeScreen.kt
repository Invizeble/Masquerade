package com.invize.masquerade.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.invize.masquerade.ui.theme.Background
import com.invize.masquerade.ui.theme.Error
import com.invize.masquerade.ui.theme.OnBackground
import com.invize.masquerade.ui.theme.OnSurface
import com.invize.masquerade.ui.theme.Primary
import com.invize.masquerade.ui.theme.Subtle
import com.invize.masquerade.ui.theme.Surface

data class Chat(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val avatarColor: Color = Primary
)

val testChats = listOf(
    Chat(
        id = "1",
        name = "test",
        lastMessage = "lastMassage",
        time = "time",
        unreadCount = 1,
        avatarColor = Primary
    ),
)

@Composable
fun HomeScreen(
    onChatClick: (chatId: String, chatName: String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Masquerade",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = OnBackground,
                modifier = Modifier.weight(1f)
            )
            // DEV: кнопка выхода
            TextButton(onClick = onLogout) {
                Text(
                    text = "Выйти",
                    color = Error,
                    fontSize = 13.sp
                )
            }
        }

        HorizontalDivider(color = Subtle.copy(alpha = 0.3f), thickness = 0.5.dp)

        // Chat list
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(testChats) { chat ->
                ChatItem(
                    chat = chat,
                    onClick = { onChatClick(chat.id, chat.name) }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 80.dp),
                    color = Subtle.copy(alpha = 0.2f),
                    thickness = 0.5.dp
                )
            }
        }
    }
}

@Composable
private fun ChatItem(
    chat: Chat,
    onClick: () -> Unit
) {
    // Chat Tab Size
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(chat.avatarColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chat.name.first().toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = chat.avatarColor
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Name + last message
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = chat.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = chat.lastMessage,
                fontSize = 14.sp,
                color = OnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Time + unread badge
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = chat.time,
                fontSize = 12.sp,
                color = if (chat.unreadCount > 0) Primary else OnSurface
            )
            if (chat.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chat.unreadCount.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}