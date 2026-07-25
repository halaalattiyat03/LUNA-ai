package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MockData
import com.example.model.JournalEntry
import com.example.ui.components.GlassCard
import com.example.ui.components.PersistentSearchBar
import com.example.ui.theme.*
import com.example.viewmodel.LunaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    viewModel: LunaViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var isNewEntryModalOpen by remember { mutableStateOf(false) }

    // Filter States
    var selectedDateFilter by remember { mutableStateOf<String?>(null) } // null = All Dates
    var selectedEmotionFilter by remember { mutableStateOf<String?>(null) } // null = All Emotions
    var selectedTagFilter by remember { mutableStateOf("All") } // "All" or tag name
    var searchQuery by remember { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // New Entry Form State
    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }
    var selectedMoodEmoji by remember { mutableStateOf("🌸") }
    var selectedMoodLabel by remember { mutableStateOf("Radiant") }

    val allTags = listOf("All", "Mindfulness", "MorningRoutine", "Career", "Pilates", "SelfCare", "Gratitude", "Skincare", "Beauty", "Glow")

    val quickDates = listOf("All Dates", "Today", "Yesterday", "July 23, 2026")

    // Filter Logic
    val filteredEntries = remember(searchQuery, selectedDateFilter, selectedEmotionFilter, selectedTagFilter, state.journalEntries) {
        state.journalEntries.filter { entry ->
            val matchesTag = selectedTagFilter == "All" || entry.tags.any { it.equals(selectedTagFilter, ignoreCase = true) }

            val matchesQuery = searchQuery.isBlank() ||
                    entry.title.contains(searchQuery, ignoreCase = true) ||
                    entry.content.contains(searchQuery, ignoreCase = true)

            val matchesEmotion = selectedEmotionFilter == null ||
                    entry.moodLabel.equals(selectedEmotionFilter, ignoreCase = true) ||
                    entry.moodEmoji == selectedEmotionFilter

            val matchesDate = selectedDateFilter == null || when (selectedDateFilter) {
                "Today" -> entry.date.contains("Today", ignoreCase = true)
                "Yesterday" -> entry.date.contains("Yesterday", ignoreCase = true)
                else -> entry.date.contains(selectedDateFilter!!, ignoreCase = true)
            }

            matchesTag && matchesQuery && matchesEmotion && matchesDate
        }
    }

    val isAnyFilterActive = selectedDateFilter != null || selectedEmotionFilter != null || selectedTagFilter != "All" || searchQuery.isNotBlank()

    // DatePicker Dialog
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.US)
                            selectedDateFilter = formatter.format(Date(millis))
                        }
                    }
                ) {
                    Text("Apply Date", fontWeight = FontWeight.Bold, color = LunaPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // New Entry Bottom Sheet
    if (isNewEntryModalOpen) {
        ModalBottomSheet(
            onDismissRequest = { isNewEntryModalOpen = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New Journal Entry",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = { isNewEntryModalOpen = false }) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mood Selector Row
                Text(text = "How are you feeling?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(MockData.moodOptions) { (emoji, label) ->
                        val isSelected = selectedMoodEmoji == emoji
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) LunaPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                selectedMoodEmoji = emoji
                                selectedMoodLabel = label
                            }
                        ) {
                            Text(
                                text = "$emoji $label",
                                fontSize = 12.sp,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    placeholder = { Text("Title (e.g. Evening Reflection)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LunaPrimary,
                        unfocusedBorderColor = LunaBorderLight
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = newContent,
                    onValueChange = { newContent = it },
                    placeholder = { Text("Write your thoughts, gratitude, or goals...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LunaPrimary,
                        unfocusedBorderColor = LunaBorderLight
                    ),
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (newTitle.isBlank() || newContent.isBlank()) {
                            Toast.makeText(context, "Please fill in title and content", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addJournalEntry(
                                title = newTitle,
                                content = newContent,
                                moodEmoji = selectedMoodEmoji,
                                moodLabel = selectedMoodLabel,
                                tags = listOf("Mindfulness", "Journal")
                            )
                            isNewEntryModalOpen = false
                            newTitle = ""
                            newContent = ""
                            Toast.makeText(context, "Journal entry saved ✨", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LunaPrimary)
                ) {
                    Text(text = "Save Entry", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Journal Top Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mindful Timeline",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Filter reflections by date, emotion & tags",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FloatingActionButton(
                    onClick = { isNewEntryModalOpen = true },
                    shape = CircleShape,
                    containerColor = LunaPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp).testTag("journal_new_entry_button")
                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "New Entry")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Persistent Search Bar
            PersistentSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search journal reflections, gratitude & tags...",
                resultsCount = filteredEntries.size,
                onClear = { searchQuery = "" }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 1. DATE PICKER & DATE FILTER ROW
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Calendar Date Picker Dialog Trigger Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (selectedDateFilter != null && !quickDates.contains(selectedDateFilter)) LunaPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clickable { showDatePickerDialog = true }
                        .testTag("journal_date_picker_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Pick Date",
                            tint = if (selectedDateFilter != null && !quickDates.contains(selectedDateFilter)) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (selectedDateFilter != null && !quickDates.contains(selectedDateFilter)) selectedDateFilter!! else "Pick Date",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedDateFilter != null && !quickDates.contains(selectedDateFilter)) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Quick Date Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(quickDates) { dateLabel ->
                        val isSelected = (dateLabel == "All Dates" && selectedDateFilter == null) || (selectedDateFilter == dateLabel)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) LunaPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                selectedDateFilter = if (dateLabel == "All Dates") null else dateLabel
                            }
                        ) {
                            Text(
                                text = dateLabel,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. EMOTION / MOOD FILTER ROW
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Mood:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        val isAllSelected = selectedEmotionFilter == null
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isAllSelected) LunaSecondary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedEmotionFilter = null }
                        ) {
                            Text(
                                text = "All Moods",
                                fontSize = 11.sp,
                                fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isAllSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    items(MockData.moodOptions) { (emoji, label) ->
                        val isSelected = selectedEmotionFilter == label
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) LunaSecondary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                selectedEmotionFilter = if (isSelected) null else label
                            }
                        ) {
                            Text(
                                text = "$emoji $label",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3. TAG CHIPS ROW
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Tags:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(allTags) { tag ->
                        val isSelected = selectedTagFilter == tag
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) LunaAccent else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedTagFilter = tag }
                        ) {
                            Text(
                                text = "#$tag",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // 4. ACTIVE FILTERS SUMMARY & COUNTER BAR
            if (isAnyFilterActive) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Found ${filteredEntries.size} matching entry${if (filteredEntries.size != 1) "s" else ""}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LunaPrimary
                    )

                    TextButton(
                        onClick = {
                            selectedDateFilter = null
                            selectedEmotionFilter = null
                            selectedTagFilter = "All"
                            searchQuery = ""
                        },
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                    ) {
                        Icon(imageVector = Icons.Rounded.RestartAlt, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Reset Filters", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LunaPrimary)
                    }
                }
            }
        }

        // 5. TIMELINE ENTRIES LIST
        if (filteredEntries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔍", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No journal entries found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try adjusting your date, mood, or tag filter criteria.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                itemsIndexed(filteredEntries, key = { _, entry -> entry.id }) { index, entry ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(200)) + slideInVertically(initialOffsetY = { 20 }, animationSpec = tween(200)),
                        exit = fadeOut(tween(150))
                    ) {
                        TimelineEntryRow(
                            entry = entry,
                            isLast = index == filteredEntries.lastIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineEntryRow(
    entry: JournalEntry,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Vertical Timeline Column (Node + Line)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            // Node Emoji Orb
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(LunaPrimary, LunaSecondary))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = entry.moodEmoji, fontSize = 16.sp)
            }

            // Connecting Vertical Line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(LunaPrimary.copy(alpha = 0.25f))
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Journal Entry Content Card
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = entry.moodLabel,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = LunaPrimary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = entry.date,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = entry.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = entry.content,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )

                    if (entry.photoRes != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Image(
                            painter = painterResource(id = entry.photoRes),
                            contentDescription = "Journal Attachment",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        entry.tags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "#$tag",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
