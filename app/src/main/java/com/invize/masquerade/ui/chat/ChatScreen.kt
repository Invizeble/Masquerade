package com.invize.masquerade.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.invize.masquerade.ui.theme.Background
import com.invize.masquerade.ui.theme.OnBackground
import com.invize.masquerade.ui.theme.OnSurface
import com.invize.masquerade.ui.theme.Primary
import com.invize.masquerade.ui.theme.Subtle
import com.invize.masquerade.ui.theme.Surface
import com.invize.masquerade.ui.theme.SurfaceVariant

data class Message(
    val id: String,
    val text: String,
    val isOwn: Boolean,
    val time: String
)

@Composable
fun ChatScreen(
    chatId: String,
    chatName: String,
    onBack: () -> Unit
) {
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    val messages = remember {
        mutableStateListOf(
            Message("1", "Привет! Это тестовый чат.", false, "12:30"),
            Message("2", "Здесь можно общаться анонимно.", false, "12:31"),
            Message("3", "Отлично, спасибо!", true, "12:33"),
        )
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .imePadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = OnBackground
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chatName.first().toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = chatName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground
                )
                Text(
                    text = "в сети",
                    fontSize = 12.sp,
                    color = Primary
                )
            }
        }

        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(modifier = Modifier.size(8.dp)) }
            items(messages) { message ->
                MessageBubble(message = message)
            }
            item { Spacer(modifier = Modifier.size(8.dp)) }
        }

        // Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Сообщение...", color = Subtle) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                singleLine = false,
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (inputText.isNotBlank()) {
                            messages.add(
                                Message(
                                    id = System.currentTimeMillis().toString(),
                                    text = inputText.trim(),
                                    isOwn = true,
                                    time = getCurrentTime()
                                )
                            )
                            inputText = ""
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = OnBackground,
                    unfocusedTextColor = OnBackground,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Subtle,
                    cursorColor = Primary,
                    focusedContainerColor = SurfaceVariant,
                    unfocusedContainerColor = SurfaceVariant
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Send button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (inputText.isNotBlank()) Primary else Subtle.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            messages.add(
                                Message(
                                    id = System.currentTimeMillis().toString(),
                                    text = inputText.trim(),
                                    isOwn = true,
                                    time = getCurrentTime()
                                )
                            )
                            inputText = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Отправить",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isOwn) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (message.isOwn) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (message.isOwn) 18.dp else 4.dp,
                            bottomEnd = if (message.isOwn) 4.dp else 18.dp
                        )
                    )
                    .background(if (message.isOwn) Primary else SurfaceVariant)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 15.sp,
                    color = if (message.isOwn) Color.White else OnBackground,
                    lineHeight = 20.sp
                )
            }
            Text(
                text = message.time,
                fontSize = 11.sp,
                color = OnSurface,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

private fun getCurrentTime(): String {
    val calendar = java.util.Calendar.getInstance()
    val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
    val minute = calendar.get(java.util.Calendar.MINUTE).toString().padStart(2, '0')
    return "$hour:$minute"
}