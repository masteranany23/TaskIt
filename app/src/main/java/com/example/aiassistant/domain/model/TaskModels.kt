package com.example.aiassistant.domain.model

import androidx.compose.ui.graphics.Color
import com.example.aiassistant.ui.theme.*

/**
 * TaskCategory - Represents different categories of AI tasks
 * Each category has a unique visual theme and set of tasks
 */
data class TaskCategory(
    val id: String,
    val title: String,
    val description: String,
    val icon: String, // We'll use emoji for simplicity, can be replaced with vector icons
    val color: Color,
    val gradientColors: List<Color>,
    val tasks: List<Task>,
    val isPopular: Boolean = false
)

/**
 * Task - Individual AI task that users can execute
 * Each task triggers a specific n8n workflow
 */
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val estimatedTime: String,
    val difficulty: TaskDifficulty,
    val tags: List<String>,
    val workflowId: String? = null, // n8n workflow identifier
    val usageCount: Int = 0
)

/**
 * TaskDifficulty - Indicates how complex a task is for users
 */
enum class TaskDifficulty(val displayName: String, val color: Color) {
    EASY("Easy", AccentGreen),
    MEDIUM("Medium", AccentYellow),
    HARD("Advanced", AccentRed)
}

/**
 * Predefined task categories with futuristic themes
 */
object TaskCategories {
    
    /**
     * Get all available task categories
     */
    fun getAllCategories(): List<TaskCategory> = listOf(
        // Everyday Tasks - Most common user actions
        TaskCategory(
            id = "everyday",
            title = "Everyday Tasks",
            description = "Quick daily actions made simple",
            icon = "⚡",
            color = ElectricBlue,
            gradientColors = listOf(ElectricBlue, ElectricBlueVariant),
            isPopular = true,
            tasks = listOf(
                Task(
                    id = "send_email",
                    title = "Send Email",
                    description = "Compose and send professional emails",
                    icon = "📧",
                    estimatedTime = "2 min",
                    difficulty = TaskDifficulty.EASY,
                    tags = listOf("communication", "productivity"),
                    workflowId = "email-task"
                ),
                Task(
                    id = "set_reminder",
                    title = "Set Reminder",
                    description = "Create smart reminders with context",
                    icon = "⏰",
                    estimatedTime = "1 min",
                    difficulty = TaskDifficulty.EASY,
                    tags = listOf("scheduling", "memory"),
                    workflowId = "set-reminder-task"
                ),
                Task(
                    id = "summarize_day",
                    title = "Summarize My Day",
                    description = "Get AI-generated daily summary",
                    icon = "📊",
                    estimatedTime = "3 min",
                    difficulty = TaskDifficulty.EASY,
                    tags = listOf("analysis", "reflection"),
                    workflowId = "summarize-day-task"
                )
            )
        ),
        
        // Office Work - Professional productivity tools
        TaskCategory(
            id = "office",
            title = "Office Work",
            description = "Professional productivity tools",
            icon = "💼",
            color = NeonGreen,
            gradientColors = listOf(NeonGreen, AccentGreen),
            tasks = listOf(
                Task(
                    id = "create_report",
                    title = "Create Report",
                    description = "Generate professional reports from data",
                    icon = "📈",
                    estimatedTime = "10 min",
                    difficulty = TaskDifficulty.MEDIUM,
                    tags = listOf("analysis", "documentation"),
                    workflowId = "report-task"
                ),
                Task(
                    id = "schedule_meeting",
                    title = "Schedule Meeting",
                    description = "Find optimal meeting times and send invites",
                    icon = "🗓️",
                    estimatedTime = "5 min",
                    difficulty = TaskDifficulty.EASY,
                    tags = listOf("scheduling", "communication"),
                    workflowId = "schedule-meeting-task"
                ),
                Task(
                    id = "create_presentation",
                    title = "Build Presentation",
                    description = "Generate slides from your content",
                    icon = "🎯",
                    estimatedTime = "15 min",
                    difficulty = TaskDifficulty.MEDIUM,
                    tags = listOf("design", "presentation"),
                    workflowId = "create-presentation-task"
                )
            )
        ),
        
        // Research & Study - Academic and learning tools
        TaskCategory(
            id = "research",
            title = "Research & Study",
            description = "Learning and research assistance",
            icon = "🔬",
            color = NeonPurple,
            gradientColors = listOf(NeonPurple, AccentPurple),
            tasks = listOf(
                Task(
                    id = "summarize_video",
                    title = "Summarize YouTube Video",
                    description = "Extract key points from any video",
                    icon = "🎥",
                    estimatedTime = "5 min",
                    difficulty = TaskDifficulty.EASY,
                    tags = listOf("learning", "summarization"),
                    workflowId = "summarize-video-task"
                ),
                Task(
                    id = "research_topic",
                    title = "Research Topic",
                    description = "Comprehensive research with sources",
                    icon = "📚",
                    estimatedTime = "20 min",
                    difficulty = TaskDifficulty.HARD,
                    tags = listOf("research", "analysis"),
                    workflowId = "research-topic-task"
                ),
                Task(
                    id = "create_notes",
                    title = "Generate Study Notes",
                    description = "Transform content into study materials",
                    icon = "📝",
                    estimatedTime = "8 min",
                    difficulty = TaskDifficulty.MEDIUM,
                    tags = listOf("education", "organization"),
                    workflowId = "create-notes-task"
                ),
                Task(
                    id = "scrape_url",
                    title = "Web Content Scraper",
                    description = "Extract and analyze web content automatically",
                    icon = "🕷️",
                    estimatedTime = "10 min",
                    difficulty = TaskDifficulty.MEDIUM,
                    tags = listOf("scraping", "data", "research"),
                    workflowId = "web-scraper-task"
                )
            )
        ),
        
        // Creative Tasks - Design and content creation
        TaskCategory(
            id = "creative",
            title = "Creative Tasks",
            description = "Design and content creation",
            icon = "🎨",
            color = NeonPink,
            gradientColors = listOf(NeonPink, AccentRed),
            tasks = listOf(
                Task(
                    id = "write_blog",
                    title = "Write Blog Post",
                    description = "Create engaging blog content",
                    icon = "✍️",
                    estimatedTime = "25 min",
                    difficulty = TaskDifficulty.MEDIUM,
                    tags = listOf("writing", "content"),
                    workflowId = "write-blog-task"
                ),
                Task(
                    id = "design_logo",
                    title = "Design Logo Concepts",
                    description = "Generate logo ideas and descriptions",
                    icon = "🎪",
                    estimatedTime = "15 min",
                    difficulty = TaskDifficulty.HARD,
                    tags = listOf("design", "branding"),
                    workflowId = "design-logo-task"
                ),
                Task(
                    id = "social_media",
                    title = "Social Media Posts",
                    description = "Create engaging social content",
                    icon = "📱",
                    estimatedTime = "10 min",
                    difficulty = TaskDifficulty.EASY,
                    tags = listOf("social", "marketing"),
                    workflowId = "social-media-task"
                )
            )
        ),
        
        // Business Tasks - Entrepreneurial and freelance tools
        TaskCategory(
            id = "business",
            title = "Business Tasks",
            description = "Entrepreneurial and freelance tools",
            icon = "🚀",
            color = AccentOrange,
            gradientColors = listOf(AccentOrange, AccentYellow),
            tasks = listOf(
                Task(
                    id = "create_invoice",
                    title = "Create Invoice",
                    description = "Generate professional invoices",
                    icon = "🧾",
                    estimatedTime = "5 min",
                    difficulty = TaskDifficulty.EASY,
                    tags = listOf("finance", "business"),
                    workflowId = "create-invoice-task"
                ),
                Task(
                    id = "market_analysis",
                    title = "Market Analysis",
                    description = "Analyze market trends and competitors",
                    icon = "📊",
                    estimatedTime = "30 min",
                    difficulty = TaskDifficulty.HARD,
                    tags = listOf("analysis", "strategy"),
                    workflowId = "market-analysis-task"
                ),
                Task(
                    id = "business_plan",
                    title = "Business Plan Draft",
                    description = "Create structured business plan",
                    icon = "📋",
                    estimatedTime = "45 min",
                    difficulty = TaskDifficulty.HARD,
                    tags = listOf("planning", "strategy"),
                    workflowId = "business-plan-task"
                )
            )
        )
    )
    
    /**
     * Get popular/featured tasks across all categories
     */
    fun getPopularTasks(): List<Task> {
        return getAllCategories()
            .filter { it.isPopular }
            .flatMap { it.tasks }
            .take(6)
    }
}

/**
 * VoiceCommandState - Represents different states of voice command processing
 */
enum class VoiceCommandState {
    IDLE,           // Ready to listen
    LISTENING,      // Actively recording audio
    PROCESSING,     // Processing recorded audio
    RECOGNIZED,     // Successfully recognized command
    ERROR           // Error in recognition
}

/**
 * VoiceCommand - Represents a voice command in history
 */
data class VoiceCommand(
    val id: String,
    val text: String,
    val timestamp: Long,
    val confidence: Float,
    val taskExecuted: Task? = null,
    val success: Boolean = true
) {
    companion object {
        /**
         * Generate mock voice commands for demo purposes
         */
        fun getMockCommands(): List<VoiceCommand> {
            val currentTime = System.currentTimeMillis()
            return listOf(
                VoiceCommand(
                    id = "cmd_1",
                    text = "Send email to John about the project meeting",
                    timestamp = currentTime - 3600000, // 1 hour ago
                    confidence = 0.95f,
                    taskExecuted = Task(
                        id = "send_email",
                        title = "Send Email",
                        description = "Email sent successfully",
                        icon = "📧",
                        estimatedTime = "2 min",
                        difficulty = TaskDifficulty.EASY,
                        tags = listOf("communication")
                    ),
                    success = true
                ),
                VoiceCommand(
                    id = "cmd_2", 
                    text = "Create a reminder for tomorrow's meeting at 2 PM",
                    timestamp = currentTime - 7200000, // 2 hours ago
                    confidence = 0.89f,
                    taskExecuted = Task(
                        id = "set_reminder",
                        title = "Set Reminder",
                        description = "Reminder created successfully",
                        icon = "⏰",
                        estimatedTime = "1 min",
                        difficulty = TaskDifficulty.EASY,
                        tags = listOf("scheduling")
                    ),
                    success = true
                ),
                VoiceCommand(
                    id = "cmd_3",
                    text = "Generate report from last week's data",
                    timestamp = currentTime - 10800000, // 3 hours ago
                    confidence = 0.92f,
                    taskExecuted = Task(
                        id = "generate_report",
                        title = "Generate Report",
                        description = "Report generated successfully", 
                        icon = "📊",
                        estimatedTime = "5 min",
                        difficulty = TaskDifficulty.MEDIUM,
                        tags = listOf("analysis", "reporting")
                    ),
                    success = true
                ),
                VoiceCommand(
                    id = "cmd_4",
                    text = "Schedule social media posts for this week",
                    timestamp = currentTime - 14400000, // 4 hours ago
                    confidence = 0.87f,
                    taskExecuted = Task(
                        id = "schedule_posts",
                        title = "Schedule Posts",
                        description = "Posts scheduled successfully",
                        icon = "📱",
                        estimatedTime = "3 min", 
                        difficulty = TaskDifficulty.MEDIUM,
                        tags = listOf("social media", "content")
                    ),
                    success = true
                ),
                VoiceCommand(
                    id = "cmd_5",
                    text = "Analyze competitor pricing data",
                    timestamp = currentTime - 18000000, // 5 hours ago
                    confidence = 0.91f,
                    taskExecuted = Task(
                        id = "analyze_competition",
                        title = "Analyze Competition", 
                        description = "Analysis completed successfully",
                        icon = "🔍",
                        estimatedTime = "8 min",
                        difficulty = TaskDifficulty.HARD,
                        tags = listOf("analysis", "research")
                    ),
                    success = true
                )
            )
        }
    }
}

/**
 * ScheduleItem - Represents a scheduled task or event
 */
data class ScheduleItem(
    val id: String,
    val title: String,
    val description: String,
    val startTime: Long, // Unix timestamp
    val endTime: Long,
    val type: ScheduleType,
    val priority: SchedulePriority,
    val isCompleted: Boolean = false,
    val tags: List<String> = emptyList(),
    val reminderMinutes: Int = 15, // Minutes before to remind
    val associatedTask: Task? = null // Optional linked AI task
)

/**
 * ScheduleType - Different types of scheduled items
 */
enum class ScheduleType(val displayName: String, val icon: String, val color: Color) {
    MEETING("Meeting", "👥", ElectricBlue),
    TASK("Task", "✅", NeonGreen),
    REMINDER("Reminder", "⏰", AccentYellow),
    BREAK("Break", "☕", NeonPurple),
    FOCUS_TIME("Focus Time", "🎯", AccentOrange),
    PERSONAL("Personal", "🏠", AccentRed)
}

/**
 * SchedulePriority - Priority levels for scheduled items
 */
enum class SchedulePriority(val displayName: String, val color: Color) {
    LOW("Low", TextTertiary),
    MEDIUM("Medium", AccentYellow),
    HIGH("High", AccentOrange),
    URGENT("Urgent", AccentRed)
}

/**
 * WeekView - Represents a week view for the scheduler
 */
data class WeekView(
    val weekStart: Long, // Monday of the week
    val days: List<DaySchedule>
)

/**
 * DaySchedule - Represents a single day's schedule
 */
data class DaySchedule(
    val date: Long, // Date as Unix timestamp
    val dayName: String,
    val dayOfMonth: Int,
    val items: List<ScheduleItem>,
    val isToday: Boolean = false
)

/**
 * ChatMessage - Individual message in AI conversation
 */
data class ChatMessage(
    val id: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long,
    val type: MessageType = MessageType.TEXT,
    val metadata: MessageMetadata? = null
)

/**
 * MessageType - Different types of chat messages
 */
enum class MessageType {
    TEXT,
    TASK_SUGGESTION,
    SCHEDULE_REMINDER,
    VOICE_RESPONSE,
    SYSTEM_NOTIFICATION,
    IMAGE,
    FILE
}

/**
 * MessageMetadata - Additional data for specialized messages
 */
data class MessageMetadata(
    val suggestedTask: Task? = null,
    val scheduleItem: ScheduleItem? = null,
    val actionButtons: List<QuickAction>? = null,
    val isThinking: Boolean = false
)

/**
 * QuickAction - Action buttons in chat messages
 */
data class QuickAction(
    val id: String,
    val label: String,
    val action: String,
    val icon: String
)

/**
 * ChatSession - Represents a conversation session
 */
data class ChatSession(
    val id: String,
    val title: String,
    val lastMessage: String,
    val timestamp: Long,
    val messageCount: Int,
    val isActive: Boolean = false
)

/**
 * AIPersonality - Different AI assistant personalities
 */
enum class AIPersonality(
    val displayName: String,
    val description: String,
    val avatar: String,
    val color: Color
) {
    PROFESSIONAL("Professional", "Focused and efficient assistant", "👔", ElectricBlue),
    FRIENDLY("Friendly", "Warm and conversational helper", "😊", NeonGreen),
    CREATIVE("Creative", "Innovative and inspiring companion", "🎨", NeonPurple),
    ANALYTICAL("Analytical", "Data-driven and logical advisor", "📊", AccentOrange)
}

/**
 * Sample data for Smart Scheduler (Module 3)
 */
object SchedulerSampleData {
    
    fun getSampleScheduleItems(): List<ScheduleItem> {
        val currentTime = System.currentTimeMillis()
        val oneHour = 3600000L
        
        return listOf(
            ScheduleItem(
                id = "sch_1",
                title = "Team Standup",
                description = "Daily team synchronization meeting",
                startTime = currentTime + oneHour,
                endTime = currentTime + oneHour + 1800000, // 30 minutes
                type = ScheduleType.MEETING,
                priority = SchedulePriority.HIGH,
                tags = listOf("team", "daily", "work")
            ),
            ScheduleItem(
                id = "sch_2",
                title = "Review Marketing Reports",
                description = "Analyze Q2 marketing performance data",
                startTime = currentTime + (2 * oneHour),
                endTime = currentTime + (3 * oneHour),
                type = ScheduleType.TASK,
                priority = SchedulePriority.MEDIUM,
                tags = listOf("analysis", "marketing", "reports"),
                associatedTask = Task(
                    id = "marketing_analysis",
                    title = "Marketing Analysis",
                    description = "AI-powered marketing report analysis",
                    icon = "📊",
                    estimatedTime = "15 min",
                    difficulty = TaskDifficulty.MEDIUM,
                    tags = listOf("analysis", "marketing")
                )
            ),
            ScheduleItem(
                id = "sch_3",
                title = "Coffee Break",
                description = "Quick break to recharge",
                startTime = currentTime + (3.5 * oneHour).toLong(),
                endTime = currentTime + (3.75 * oneHour).toLong(),
                type = ScheduleType.BREAK,
                priority = SchedulePriority.LOW,
                tags = listOf("break", "personal")
            ),
            ScheduleItem(
                id = "sch_4",
                title = "Focus Time: Project Development",
                description = "Deep work session on AI assistant features",
                startTime = currentTime + (4 * oneHour),
                endTime = currentTime + (6 * oneHour),
                type = ScheduleType.FOCUS_TIME,
                priority = SchedulePriority.HIGH,
                tags = listOf("development", "focus", "ai")
            ),
            ScheduleItem(
                id = "sch_5",
                title = "Client Presentation Prep",
                description = "Prepare slides for tomorrow's client meeting",
                startTime = currentTime + (7 * oneHour),
                endTime = currentTime + (8 * oneHour),
                type = ScheduleType.TASK,
                priority = SchedulePriority.URGENT,
                tags = listOf("presentation", "client", "preparation")
            )
        )
    }
}

/**
 * Sample data for AI Chat Interface (Module 4)
 */
object ChatSampleData {
    
    fun getSampleChatMessages(): List<ChatMessage> {
        val currentTime = System.currentTimeMillis()
        
        return listOf(
            ChatMessage(
                id = "msg_1",
                content = "Hello! I'm your AI assistant. How can I help you today?",
                isFromUser = false,
                timestamp = currentTime - 300000, // 5 minutes ago
                type = MessageType.TEXT
            ),
            ChatMessage(
                id = "msg_2",
                content = "I need help organizing my schedule for this week",
                isFromUser = true,
                timestamp = currentTime - 240000
            ),
            ChatMessage(
                id = "msg_3",
                content = "I'd be happy to help you organize your schedule! I can see you have several upcoming items. Would you like me to suggest some AI tasks to help with your workload?",
                isFromUser = false,
                timestamp = currentTime - 180000,
                type = MessageType.TASK_SUGGESTION,
                metadata = MessageMetadata(
                    actionButtons = listOf(
                        QuickAction("suggest_tasks", "Suggest Tasks", "suggest_tasks", "🤖"),
                        QuickAction("view_schedule", "View Schedule", "view_schedule", "📅")
                    )
                )
            ),
            ChatMessage(
                id = "msg_4",
                content = "Yes, please suggest some tasks that could help me",
                isFromUser = true,
                timestamp = currentTime - 120000
            ),
            ChatMessage(
                id = "msg_5",
                content = "Based on your schedule, I recommend automating your report analysis and email management. These tasks could save you 2-3 hours this week!",
                isFromUser = false,
                timestamp = currentTime - 60000,
                type = MessageType.TASK_SUGGESTION,
                metadata = MessageMetadata(
                    suggestedTask = Task(
                        id = "email_automation",
                        title = "Email Management",
                        description = "Automated email sorting and responses",
                        icon = "📧",
                        estimatedTime = "5 min setup",
                        difficulty = TaskDifficulty.EASY,
                        tags = listOf("automation", "email", "productivity")
                    ),
                    actionButtons = listOf(
                        QuickAction("start_task", "Start Task", "start_task", "▶️"),
                        QuickAction("learn_more", "Learn More", "learn_more", "ℹ️")
                    )
                )
            )
        )
    }
    
    fun getSampleChatSessions(): List<ChatSession> {
        val currentTime = System.currentTimeMillis()
        
        return listOf(
            ChatSession(
                id = "session_1",
                title = "Schedule Organization",
                lastMessage = "Based on your schedule, I recommend...",
                timestamp = currentTime - 60000,
                messageCount = 8,
                isActive = true
            ),
            ChatSession(
                id = "session_2",
                title = "Task Automation Setup",
                lastMessage = "Great! Your email automation is now active.",
                timestamp = currentTime - 3600000,
                messageCount = 12,
                isActive = false
            ),
            ChatSession(
                id = "session_3",
                title = "Weekly Planning",
                lastMessage = "I've created your weekly schedule template.",
                timestamp = currentTime - 86400000,
                messageCount = 6,
                isActive = false
            )
        )
    }
}

// ==================== MODULE 5: WEB SCRAPER ====================

/**
 * Web scraping job configuration
 */
data class ScrapeJob(
    val id: String,
    val name: String,
    val url: String,
    val selector: String = "",
    val frequency: ScrapeFrequency = ScrapeFrequency.MANUAL,
    val isActive: Boolean = true,
    val lastRun: Long? = null,
    val nextRun: Long? = null,
    val status: ScrapeStatus = ScrapeStatus.PENDING,
    val extractedData: List<ScrapedData> = emptyList(),
    val webhook: String? = null, // n8n webhook URL
    val filters: List<ScrapeFilter> = emptyList(),
    val icon: String = "🕷️",
    val category: ScrapeCategory = ScrapeCategory.GENERAL
)

/**
 * Scraped data item
 */
data class ScrapedData(
    val id: String,
    val jobId: String,
    val content: String,
    val metadata: Map<String, String> = emptyMap(),
    val timestamp: Long,
    val url: String,
    val hash: String, // For detecting changes
    val isNew: Boolean = true,
    val tags: List<String> = emptyList()
)

/**
 * Scrape job frequency
 */
enum class ScrapeFrequency(val displayName: String, val icon: String) {
    MANUAL("Manual", "✋"),
    HOURLY("Hourly", "⏰"),
    DAILY("Daily", "📅"),
    WEEKLY("Weekly", "📆"),
    MONTHLY("Monthly", "🗓️")
}

/**
 * Scrape job status
 */
enum class ScrapeStatus(val displayName: String, val color: androidx.compose.ui.graphics.Color) {
    PENDING("Pending", com.example.aiassistant.ui.theme.TextSecondary),
    RUNNING("Running", com.example.aiassistant.ui.theme.ElectricBlue),
    SUCCESS("Success", com.example.aiassistant.ui.theme.NeonGreen),
    ERROR("Error", com.example.aiassistant.ui.theme.AccentRed),
    PAUSED("Paused", com.example.aiassistant.ui.theme.AccentOrange)
}

/**
 * Scrape category
 */
enum class ScrapeCategory(val displayName: String, val icon: String, val color: androidx.compose.ui.graphics.Color) {
    GENERAL("General", "🌐", com.example.aiassistant.ui.theme.ElectricBlue),
    NEWS("News", "📰", com.example.aiassistant.ui.theme.NeonGreen),
    ECOMMERCE("E-commerce", "🛒", com.example.aiassistant.ui.theme.AccentOrange),
    SOCIAL("Social Media", "📱", com.example.aiassistant.ui.theme.NeonPurple),
    RESEARCH("Research", "🔬", com.example.aiassistant.ui.theme.AccentBlue),
    MONITORING("Monitoring", "📊", com.example.aiassistant.ui.theme.NeonPink)
}

/**
 * Scrape filter for data processing
 */
data class ScrapeFilter(
    val id: String,
    val name: String,
    val type: FilterType,
    val condition: String,
    val action: FilterAction,
    val isActive: Boolean = true
)

enum class FilterType(val displayName: String) {
    CONTAINS("Contains"),
    REGEX("Regex"),
    LENGTH("Length"),
    DATE("Date"),
    CUSTOM("Custom")
}

enum class FilterAction(val displayName: String, val icon: String) {
    INCLUDE("Include", "✅"),
    EXCLUDE("Exclude", "❌"),
    TRANSFORM("Transform", "🔄"),
    TAG("Add Tag", "🏷️")
}

/**
 * Sample data for web scraper
 */
object WebScraperSampleData {
    fun getSampleScrapeJobs(): List<ScrapeJob> {
        return listOf(
            ScrapeJob(
                id = "scrape_1",
                name = "Tech News Monitor",
                url = "https://techcrunch.com",
                selector = ".post-title",
                frequency = ScrapeFrequency.HOURLY,
                status = ScrapeStatus.SUCCESS,
                lastRun = System.currentTimeMillis() - 3600000,
                category = ScrapeCategory.NEWS,
                extractedData = listOf(
                    ScrapedData(
                        id = "data_1",
                        jobId = "scrape_1",
                        content = "AI breakthrough in quantum computing announced",
                        timestamp = System.currentTimeMillis() - 1800000,
                        url = "https://techcrunch.com/ai-quantum-breakthrough",
                        hash = "abc123",
                        tags = listOf("AI", "Quantum", "Tech")
                    )
                )
            ),
            ScrapeJob(
                id = "scrape_2",
                name = "Stock Price Tracker",
                url = "https://finance.yahoo.com",
                selector = ".stock-price",
                frequency = ScrapeFrequency.DAILY,
                status = ScrapeStatus.RUNNING,
                category = ScrapeCategory.MONITORING
            ),
            ScrapeJob(
                id = "scrape_3",
                name = "Job Listings Scraper",
                url = "https://jobs.com/remote",
                selector = ".job-card",
                frequency = ScrapeFrequency.WEEKLY,
                status = ScrapeStatus.PENDING,
                category = ScrapeCategory.RESEARCH
            )
        )
    }
}

// ==================== MODULE 6: DOCUMENT HUB ====================

/**
 * Document in the hub
 */
data class Document(
    val id: String,
    val title: String,
    val content: String,
    val type: DocumentType,
    val category: DocumentCategory,
    val tags: List<String> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long,
    val size: Long, // in bytes
    val isBookmarked: Boolean = false,
    val isShared: Boolean = false,
    val shareUrl: String? = null,
    val aiSummary: String? = null,
    val extractedEntities: List<String> = emptyList(),
    val relatedDocuments: List<String> = emptyList(),
    val icon: String = "📄",
    val color: androidx.compose.ui.graphics.Color = com.example.aiassistant.ui.theme.ElectricBlue,
    val n8nWorkflowId: String? = null // For n8n integration
)

/**
 * Document types
 */
enum class DocumentType(val displayName: String, val icon: String, val extension: String) {
    TEXT("Text", "📝", "txt"),
    PDF("PDF", "📄", "pdf"),
    WORD("Word", "📘", "docx"),
    EXCEL("Excel", "📊", "xlsx"),
    POWERPOINT("PowerPoint", "📋", "pptx"),
    IMAGE("Image", "🖼️", "jpg"),
    CODE("Code", "💻", "code"),
    MARKDOWN("Markdown", "📝", "md"),
    JSON("JSON", "🔧", "json"),
    WEB_PAGE("Web Page", "🌐", "html")
}

/**
 * Document categories
 */
enum class DocumentCategory(val displayName: String, val icon: String, val color: androidx.compose.ui.graphics.Color) {
    WORK("Work", "💼", com.example.aiassistant.ui.theme.ElectricBlue),
    PERSONAL("Personal", "👤", com.example.aiassistant.ui.theme.NeonGreen),
    RESEARCH("Research", "🔬", com.example.aiassistant.ui.theme.NeonPurple),
    EDUCATION("Education", "🎓", com.example.aiassistant.ui.theme.AccentBlue),
    FINANCE("Finance", "💰", com.example.aiassistant.ui.theme.AccentOrange),
    HEALTH("Health", "🏥", com.example.aiassistant.ui.theme.NeonPink),
    TRAVEL("Travel", "✈️", com.example.aiassistant.ui.theme.AccentGreen),
    ARCHIVE("Archive", "📦", com.example.aiassistant.ui.theme.TextSecondary)
}

/**
 * Document processing job
 */
data class DocumentProcessingJob(
    val id: String,
    val documentId: String,
    val type: ProcessingType,
    val status: ProcessingStatus,
    val progress: Float = 0f,
    val startTime: Long,
    val endTime: Long? = null,
    val result: String? = null,
    val error: String? = null
)

enum class ProcessingType(val displayName: String, val icon: String) {
    EXTRACT_TEXT("Extract Text", "📝"),
    GENERATE_SUMMARY("AI Summary", "🤖"),
    FIND_ENTITIES("Find Entities", "🏷️"),
    TRANSLATE("Translate", "🌍"),
    ANALYZE_SENTIMENT("Sentiment Analysis", "😊"),
    FIND_SIMILAR("Find Similar", "🔍")
}

enum class ProcessingStatus(val displayName: String, val color: androidx.compose.ui.graphics.Color) {
    QUEUED("Queued", com.example.aiassistant.ui.theme.TextSecondary),
    PROCESSING("Processing", com.example.aiassistant.ui.theme.ElectricBlue),
    COMPLETED("Completed", com.example.aiassistant.ui.theme.NeonGreen),
    FAILED("Failed", com.example.aiassistant.ui.theme.AccentRed)
}

/**
 * Document search result
 */
data class DocumentSearchResult(
    val document: Document,
    val relevanceScore: Float,
    val matchedSnippets: List<String>,
    val matchedTags: List<String>
)

/**
 * Sample data for document hub
 */
object DocumentHubSampleData {
    fun getSampleDocuments(): List<Document> {
        return listOf(
            Document(
                id = "doc_1",
                title = "Project Requirements.pdf",
                content = "Detailed project requirements and specifications...",
                type = DocumentType.PDF,
                category = DocumentCategory.WORK,
                createdAt = System.currentTimeMillis() - 86400000,
                updatedAt = System.currentTimeMillis() - 3600000,
                size = 2048576,
                isBookmarked = true,
                aiSummary = "Project requirements for AI assistant mobile app development",
                extractedEntities = listOf("Mobile App", "AI Assistant", "Android", "Kotlin"),
                tags = listOf("requirements", "project", "mobile"),
                color = com.example.aiassistant.ui.theme.ElectricBlue
            ),
            Document(
                id = "doc_2",
                title = "Meeting Notes - Q4 Planning",
                content = "Notes from quarterly planning meeting...",
                type = DocumentType.TEXT,
                category = DocumentCategory.WORK,
                createdAt = System.currentTimeMillis() - 172800000,
                updatedAt = System.currentTimeMillis() - 7200000,
                size = 15360,
                aiSummary = "Q4 planning meeting with focus on product roadmap and team goals",
                extractedEntities = listOf("Q4", "Planning", "Roadmap", "Goals"),
                tags = listOf("meeting", "planning", "notes"),
                color = com.example.aiassistant.ui.theme.NeonGreen
            ),
            Document(
                id = "doc_3",
                title = "Research Paper - ML Trends",
                content = "Analysis of machine learning trends in 2024...",
                type = DocumentType.PDF,
                category = DocumentCategory.RESEARCH,
                createdAt = System.currentTimeMillis() - 259200000,
                updatedAt = System.currentTimeMillis() - 86400000,
                size = 5242880,
                isShared = true,
                aiSummary = "Comprehensive analysis of emerging ML trends and their applications",
                extractedEntities = listOf("Machine Learning", "Trends", "2024", "Analysis"),
                tags = listOf("research", "ML", "trends", "analysis"),
                color = com.example.aiassistant.ui.theme.AccentPurple
            )
        )
    }
    
    fun getSampleProcessingJobs(): List<DocumentProcessingJob> {
        return listOf(
            DocumentProcessingJob(
                id = "job_1",
                documentId = "doc_1",
                type = ProcessingType.GENERATE_SUMMARY,
                status = ProcessingStatus.COMPLETED,
                progress = 1.0f,
                startTime = System.currentTimeMillis() - 300000,
                endTime = System.currentTimeMillis() - 240000,
                result = "Generated AI summary successfully"
            ),
            DocumentProcessingJob(
                id = "job_2",
                documentId = "doc_2",
                type = ProcessingType.FIND_ENTITIES,
                status = ProcessingStatus.PROCESSING,
                progress = 0.6f,
                startTime = System.currentTimeMillis() - 120000
            )
        )
    }
}
