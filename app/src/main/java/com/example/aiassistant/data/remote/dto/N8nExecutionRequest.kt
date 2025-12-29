package com.example.aiassistant.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class N8nExecutionRequest(
    val workflowId: String,
    val data: Map<String, Any> = emptyMap()
)

@Serializable
data class N8nExecutionResponse(
    val id: String,
    val status: String,
    val data: Map<String, Any>? = null,
    val error: String? = null
)

@Serializable
data class N8nWebhookRequest(
    val taskType: String,
    val parameters: Map<String, Any>
)

@Serializable
data class EmailTaskRequest(
    val recipientEmail: String,
    val subject: String? = null,
    val content: String,
    val tone: String = "professional",
    val senderName: String? = null
)

@Serializable
data class ReportTaskRequest(
    val title: String,
    val dataSource: String,
    val reportType: String = "analysis",
    val includeCharts: Boolean = true,
    val format: String = "pdf"
)

@Serializable
data class SummaryTaskRequest(
    val content: String,
    val contentType: String = "text", // text, url, file
    val summaryLength: String = "medium" // short, medium, long
)

@Serializable
data class TranslationTaskRequest(
    val text: String,
    val targetLanguage: String,
    val sourceLanguage: String = "auto-detect"
)

@Serializable
data class ContentTaskRequest(
    val topic: String,
    val contentType: String, // blog, social, marketing
    val tone: String = "professional",
    val length: String = "medium",
    val targetAudience: String? = null
)

// ====================
// EVERYDAY TASKS
// ====================

@Serializable
data class SetReminderRequest(
    val title: String,
    val description: String? = null,
    val reminderTime: String, // ISO 8601 format
    val priority: String = "medium",
    val category: String = "general"
)

@Serializable
data class SummarizeDayRequest(
    val userEmail: String,
    val date: String, // YYYY-MM-DD format
    val includeSchedule: Boolean = true,
    val includeTasks: Boolean = true,
    val includeEmails: Boolean = false,
    val detailLevel: String = "medium" // brief, medium, detailed
)

// ====================
// OFFICE WORK TASKS
// ====================

@Serializable
data class ScheduleMeetingRequest(
    val title: String,
    val description: String? = null,
    val attendees: List<String>,
    val duration: Int, // minutes
    val preferredTimes: List<String>? = null,
    val location: String? = null,
    val meetingType: String = "virtual" // virtual, in-person, hybrid
)

@Serializable
data class CreatePresentationRequest(
    val title: String,
    val topic: String,
    val slideCount: Int = 10,
    val audience: String = "professional",
    val style: String = "business",
    val includeImages: Boolean = true,
    val templatePreference: String = "modern"
)

// ====================
// RESEARCH & STUDY TASKS
// ====================

@Serializable
data class SummarizeVideoRequest(
    val youtubeUrl: String,
    val summaryType: String = "detailed", // key_points, transcript, detailed
    val includeTimestamps: Boolean = false,
    val language: String = "english"
)

@Serializable
data class ResearchTopicRequest(
    val topic: String,
    val researchDepth: String = "comprehensive", // basic, detailed, comprehensive
    val sourcesRequired: Int = 5,
    val includeStats: Boolean = true,
    val includeCitations: Boolean = true,
    val academicLevel: String = "general" // high_school, undergraduate, graduate, professional
)

@Serializable
data class CreateNotesRequest(
    val sourceContent: String,
    val contentType: String = "text", // text, url, pdf
    val noteStyle: String = "structured", // bullet_points, structured, mind_map
    val includeQuestions: Boolean = true,
    val includeKeyTerms: Boolean = true
)

// ====================
// CREATIVE TASKS
// ====================

@Serializable
data class WriteBlogRequest(
    val topic: String,
    val audience: String = "general",
    val tone: String = "informative",
    val wordCount: Int = 800,
    val includeOutline: Boolean = true,
    val seoOptimized: Boolean = true,
    val includeImages: Boolean = false
)

@Serializable
data class DesignLogoRequest(
    val companyName: String,
    val industry: String,
    val style: String = "modern", // modern, classic, minimalist, bold
    val colors: List<String>? = null,
    val includeText: Boolean = true,
    val conceptCount: Int = 3
)

@Serializable
data class SocialMediaPostRequest(
    val platform: String, // instagram, twitter, linkedin, facebook
    val topic: String,
    val tone: String = "engaging",
    val includeHashtags: Boolean = true,
    val includeEmojis: Boolean = true,
    val postType: String = "text" // text, image, video, carousel
)

// ====================
// BUSINESS TASKS
// ====================

@Serializable
data class CreateInvoiceRequest(
    val clientName: String,
    val clientEmail: String,
    val items: List<InvoiceItem>,
    val dueDate: String, // YYYY-MM-DD format
    val currency: String = "USD",
    val taxRate: Double = 0.0,
    val notes: String? = null
)

@Serializable
data class InvoiceItem(
    val description: String,
    val quantity: Int,
    val unitPrice: Double
)

@Serializable
data class MarketAnalysisRequest(
    val industry: String,
    val targetMarket: String,
    val competitors: List<String>? = null,
    val analysisType: String = "comprehensive", // basic, detailed, comprehensive
    val includeSwot: Boolean = true,
    val includeTrends: Boolean = true,
    val region: String = "global"
)

@Serializable
data class BusinessPlanRequest(
    val businessName: String,
    val industry: String,
    val businessType: String, // startup, expansion, acquisition
    val targetMarket: String,
    val fundingNeeded: Double? = null,
    val timeframe: String = "5_years", // 1_year, 3_years, 5_years
    val includeFinancials: Boolean = true
)

@Serializable
data class WebScraperRequest(
    val url: String,
    val jobName: String,
    val category: String = "general",
    val extractionType: String = "general" // general, news, product, research
)
