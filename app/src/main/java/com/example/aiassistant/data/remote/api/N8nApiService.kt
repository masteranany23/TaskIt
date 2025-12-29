package com.example.aiassistant.data.remote.api

import com.example.aiassistant.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface N8nApiService {
    
    companion object {
        // Your n8n instance base URL - replace with your actual n8n URL
        const val BASE_URL = "https://jgyhfkshmani67789.app.n8n.cloud/"
        
        // EVERYDAY TASKS
        const val EMAIL_WEBHOOK = "webhook/email-task"
        const val SET_REMINDER_WEBHOOK = "webhook/set-reminder-task"
        const val SUMMARIZE_DAY_WEBHOOK = "webhook/summarize-day-task"
        
        // OFFICE WORK TASKS
        const val REPORT_WEBHOOK = "webhook/report-task"
        const val SCHEDULE_MEETING_WEBHOOK = "webhook/schedule-meeting-task"
        const val CREATE_PRESENTATION_WEBHOOK = "webhook/create-presentation-task"
        
        // RESEARCH & STUDY TASKS
        const val SUMMARY_WEBHOOK = "webhook/summary-task"
        const val SUMMARIZE_VIDEO_WEBHOOK = "webhook/youtube-summary"
        const val RESEARCH_TOPIC_WEBHOOK = "webhook/research-topic-task"
        const val CREATE_NOTES_WEBHOOK = "webhook/create-notes-task"
        
        // CREATIVE TASKS
        const val WRITE_BLOG_WEBHOOK = "webhook/write-blog-task"
        const val DESIGN_LOGO_WEBHOOK = "webhook/design-logo-task"
        const val SOCIAL_MEDIA_WEBHOOK = "webhook/social-media-task"
        
        // BUSINESS TASKS
        const val CREATE_INVOICE_WEBHOOK = "webhook/create-invoice-task"
        const val MARKET_ANALYSIS_WEBHOOK = "webhook/market-analysis-task"
        const val BUSINESS_PLAN_WEBHOOK = "webhook/business-plan-task"
        
        // WEB SCRAPER TASKS
        const val WEB_SCRAPER_WEBHOOK = "webhook/web-scraper"
        
        // Legacy webhooks (kept for backward compatibility)
        const val TRANSLATION_WEBHOOK = "webhook/translation-task"
        const val CONTENT_WEBHOOK = "webhook/content-task"
        const val DATA_ANALYSIS_WEBHOOK = "webhook/data-analysis-task"
        const val CALENDAR_WEBHOOK = "webhook/calendar-task"
        const val REMINDER_WEBHOOK = "webhook/reminder-task"
        const val FILE_PROCESSING_WEBHOOK = "webhook/file-processing-task"
        const val RESEARCH_WEBHOOK = "webhook/research-task"
        const val AUTOMATION_WEBHOOK = "webhook/automation-task"
        const val OPTIMIZATION_WEBHOOK = "webhook/optimization-task"
        const val MONITORING_WEBHOOK = "webhook/monitoring-task"
        const val BACKUP_WEBHOOK = "webhook/backup-task"
    }

    // ====================
    // EVERYDAY TASKS (3 tasks)
    // ====================
    
    @POST(EMAIL_WEBHOOK)
    suspend fun executeEmailTask(@Body request: EmailTaskRequest): Response<N8nExecutionResponse>
    
    @POST(SET_REMINDER_WEBHOOK)
    suspend fun executeSetReminderTask(@Body request: SetReminderRequest): Response<N8nExecutionResponse>
    
    @POST(SUMMARIZE_DAY_WEBHOOK)
    suspend fun executeSummarizeDayTask(@Body request: SummarizeDayRequest): Response<N8nExecutionResponse>
    
    // ====================
    // OFFICE WORK TASKS (3 tasks)
    // ====================
    
    @POST(REPORT_WEBHOOK)
    suspend fun executeReportTask(@Body request: ReportTaskRequest): Response<N8nExecutionResponse>
    
    @POST(SCHEDULE_MEETING_WEBHOOK)
    suspend fun executeScheduleMeetingTask(@Body request: ScheduleMeetingRequest): Response<N8nExecutionResponse>
    
    @POST(CREATE_PRESENTATION_WEBHOOK)
    suspend fun executeCreatePresentationTask(@Body request: CreatePresentationRequest): Response<N8nExecutionResponse>
    
    // ====================
    // RESEARCH & STUDY TASKS (4 tasks)
    // ====================
    
    @POST(SUMMARY_WEBHOOK)
    suspend fun executeSummaryTask(@Body request: SummaryTaskRequest): Response<N8nExecutionResponse>
    
    @POST(SUMMARIZE_VIDEO_WEBHOOK)
    suspend fun executeSummarizeVideoTask(@Body request: SummarizeVideoRequest): Response<N8nExecutionResponse>
    
    @POST(RESEARCH_TOPIC_WEBHOOK)
    suspend fun executeResearchTopicTask(@Body request: ResearchTopicRequest): Response<N8nExecutionResponse>
    
    @POST(CREATE_NOTES_WEBHOOK)
    suspend fun executeCreateNotesTask(@Body request: CreateNotesRequest): Response<N8nExecutionResponse>
    
    // ====================
    // CREATIVE TASKS (3 tasks)
    // ====================
    
    @POST(WRITE_BLOG_WEBHOOK)
    suspend fun executeWriteBlogTask(@Body request: WriteBlogRequest): Response<N8nExecutionResponse>
    
    @POST(DESIGN_LOGO_WEBHOOK)
    suspend fun executeDesignLogoTask(@Body request: DesignLogoRequest): Response<N8nExecutionResponse>
    
    @POST(SOCIAL_MEDIA_WEBHOOK)
    suspend fun executeSocialMediaTask(@Body request: SocialMediaPostRequest): Response<N8nExecutionResponse>
    
    // ====================
    // BUSINESS TASKS (3 tasks)
    // ====================
    
    @POST(CREATE_INVOICE_WEBHOOK)
    suspend fun executeCreateInvoiceTask(@Body request: CreateInvoiceRequest): Response<N8nExecutionResponse>
    
    @POST(MARKET_ANALYSIS_WEBHOOK)
    suspend fun executeMarketAnalysisTask(@Body request: MarketAnalysisRequest): Response<N8nExecutionResponse>
    
    @POST(BUSINESS_PLAN_WEBHOOK)
    suspend fun executeBusinessPlanTask(@Body request: BusinessPlanRequest): Response<N8nExecutionResponse>
    
    // ====================
    // WEB SCRAPER TASKS (1 task)
    // ====================
    
    @POST(WEB_SCRAPER_WEBHOOK)
    suspend fun executeWebScraperTask(@Body request: WebScraperRequest): Response<N8nExecutionResponse>
    
    // ====================
    // LEGACY ENDPOINTS (kept for backward compatibility)
    // ====================
    
    @POST(TRANSLATION_WEBHOOK)
    suspend fun executeTranslationTask(@Body request: TranslationTaskRequest): Response<N8nExecutionResponse>
    
    @POST(CONTENT_WEBHOOK)
    suspend fun executeContentTask(@Body request: ContentTaskRequest): Response<N8nExecutionResponse>
    
    @POST(DATA_ANALYSIS_WEBHOOK)
    suspend fun executeDataAnalysisTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    @POST(CALENDAR_WEBHOOK)
    suspend fun executeCalendarTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    @POST(REMINDER_WEBHOOK)
    suspend fun executeReminderTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    @POST(FILE_PROCESSING_WEBHOOK)
    suspend fun executeFileProcessingTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    @POST(RESEARCH_WEBHOOK)
    suspend fun executeResearchTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    @POST(AUTOMATION_WEBHOOK)
    suspend fun executeAutomationTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    @POST(OPTIMIZATION_WEBHOOK)
    suspend fun executeOptimizationTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    @POST(MONITORING_WEBHOOK)
    suspend fun executeMonitoringTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    @POST(BACKUP_WEBHOOK)
    suspend fun executeBackupTask(@Body request: Map<String, Any>): Response<N8nExecutionResponse>
    
    // Generic webhook execution
    @POST("webhook/{taskType}")
    suspend fun executeGenericTask(
        @Path("taskType") taskType: String,
        @Body request: Map<String, Any>
    ): Response<N8nExecutionResponse>
    
    // Get execution status
    @GET("api/v1/executions/{executionId}")
    suspend fun getExecutionStatus(
        @Path("executionId") executionId: String,
        @Header("X-N8N-API-KEY") apiKey: String
    ): Response<N8nExecutionResponse>
}
