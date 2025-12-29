package com.example.aiassistant.domain.model

/**
 * Provides task parameters for all 15 AI tasks across 5 categories
 * This centralizes the form configuration for dynamic UI generation
 */
object TaskParameterProvider {

    /**
     * Get parameters for any task by its ID
     */
    fun getParametersForTask(taskId: String): List<TaskParameter> {
        return when (taskId) {
            // Everyday Tasks
            "send_email" -> getEmailParameters()
            "set_reminder" -> getReminderParameters()
            "summarize_day" -> getSummarizeDayParameters()
            
            // Office Work Tasks
            "create_report" -> getReportParameters()
            "schedule_meeting" -> getScheduleMeetingParameters()
            "create_presentation" -> getPresentationParameters()
            
            // Research & Study Tasks
            "summarize_video" -> getSummarizeVideoParameters()
            "research_topic" -> getResearchTopicParameters()
            "create_notes" -> getCreateNotesParameters()
            
            // Creative Tasks
            "write_blog" -> getWriteBlogParameters()
            "design_logo" -> getDesignLogoParameters()
            "social_media" -> getSocialMediaParameters()
            
            // Business Tasks
            "create_invoice" -> getCreateInvoiceParameters()
            "market_analysis" -> getMarketAnalysisParameters()
            "business_plan" -> getBusinessPlanParameters()
            
            // Web Scraper Tasks
            "scrape_url" -> getScrapeUrlParameters()
            
            else -> emptyList()
        }
    }

    // ====================
    // EVERYDAY TASKS
    // ====================

    private fun getEmailParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "recipientEmail",
            displayName = "Recipient Email",
            type = ParameterType.EMAIL,
            required = true,
            placeholder = "john@example.com",
            validation = ParameterValidation(
                pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
                errorMessage = "Please enter a valid email address"
            )
        ),
        TaskParameter(
            key = "subject",
            displayName = "Subject",
            type = ParameterType.TEXT,
            required = false,
            placeholder = "Meeting follow-up"
        ),
        TaskParameter(
            key = "content",
            displayName = "Email Content",
            type = ParameterType.MULTILINE_TEXT,
            required = true,
            placeholder = "Hi, I wanted to follow up on our meeting today...",
            validation = ParameterValidation(
                minLength = 10,
                maxLength = 2000,
                errorMessage = "Content must be between 10 and 2000 characters"
            )
        ),
        TaskParameter(
            key = "tone",
            displayName = "Tone",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("professional", "friendly", "formal", "casual")
        ),
        TaskParameter(
            key = "senderName",
            displayName = "Your Name",
            type = ParameterType.TEXT,
            required = false,
            placeholder = "Your full name"
        )
    )

    private fun getReminderParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "title",
            displayName = "Reminder Title",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Team meeting at 2 PM",
            validation = ParameterValidation(
                minLength = 3,
                maxLength = 100,
                errorMessage = "Title must be between 3 and 100 characters"
            )
        ),
        TaskParameter(
            key = "description",
            displayName = "Description",
            type = ParameterType.MULTILINE_TEXT,
            required = false,
            placeholder = "Don't forget to prepare the quarterly report"
        ),
        TaskParameter(
            key = "reminderTime",
            displayName = "Reminder Date & Time",
            type = ParameterType.DATE,
            required = true
        ),
        TaskParameter(
            key = "priority",
            displayName = "Priority",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("low", "medium", "high", "urgent")
        ),
        TaskParameter(
            key = "category",
            displayName = "Category",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("work", "personal", "health", "finance", "social", "general")
        )
    )

    private fun getSummarizeDayParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "userEmail",
            displayName = "Your Email (for summary delivery)",
            type = ParameterType.EMAIL,
            required = true,
            placeholder = "your.email@example.com",
            validation = ParameterValidation(
                pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
                errorMessage = "Please enter a valid email address"
            )
        ),
        TaskParameter(
            key = "date",
            displayName = "Date to Summarize",
            type = ParameterType.DATE,
            required = true
        ),
        TaskParameter(
            key = "includeSchedule",
            displayName = "Include Schedule",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "includeTasks",
            displayName = "Include Completed Tasks",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "includeEmails",
            displayName = "Include Email Summary",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "detailLevel",
            displayName = "Detail Level",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("brief", "medium", "detailed")
        )
    )

    // ====================
    // OFFICE WORK TASKS
    // ====================

    private fun getReportParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "title",
            displayName = "Report Title",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Q4 Sales Analysis Report",
            validation = ParameterValidation(
                minLength = 5,
                maxLength = 100,
                errorMessage = "Title must be between 5 and 100 characters"
            )
        ),
        TaskParameter(
            key = "dataSource",
            displayName = "Data Source",
            type = ParameterType.MULTILINE_TEXT,
            required = true,
            placeholder = "Paste your data or describe the data source...",
            validation = ParameterValidation(
                minLength = 20,
                errorMessage = "Please provide sufficient data or description"
            )
        ),
        TaskParameter(
            key = "reportType",
            displayName = "Report Type",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("analysis", "summary", "comparison", "trend", "performance")
        ),
        TaskParameter(
            key = "includeCharts",
            displayName = "Include Charts",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "format",
            displayName = "Output Format",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("pdf", "docx", "html", "markdown")
        )
    )

    private fun getScheduleMeetingParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "title",
            displayName = "Meeting Title",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Project Review Meeting",
            validation = ParameterValidation(
                minLength = 3,
                maxLength = 100,
                errorMessage = "Title must be between 3 and 100 characters"
            )
        ),
        TaskParameter(
            key = "description",
            displayName = "Meeting Description",
            type = ParameterType.MULTILINE_TEXT,
            required = false,
            placeholder = "Agenda and meeting objectives"
        ),
        TaskParameter(
            key = "attendees",
            displayName = "Attendee Emails (one per line)",
            type = ParameterType.MULTILINE_TEXT,
            required = true,
            placeholder = "john@company.com\nmary@company.com\nteam@company.com"
        ),
        TaskParameter(
            key = "duration",
            displayName = "Duration (minutes)",
            type = ParameterType.NUMBER,
            required = true,
            placeholder = "60",
            validation = ParameterValidation(
                errorMessage = "Duration must be between 15 and 480 minutes"
            )
        ),
        TaskParameter(
            key = "location",
            displayName = "Location",
            type = ParameterType.TEXT,
            required = false,
            placeholder = "Conference Room A or Zoom link"
        ),
        TaskParameter(
            key = "meetingType",
            displayName = "Meeting Type",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("virtual", "in-person", "hybrid")
        )
    )

    private fun getPresentationParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "title",
            displayName = "Presentation Title",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "AI in Modern Business",
            validation = ParameterValidation(
                minLength = 5,
                maxLength = 100,
                errorMessage = "Title must be between 5 and 100 characters"
            )
        ),
        TaskParameter(
            key = "topic",
            displayName = "Topic Description",
            type = ParameterType.MULTILINE_TEXT,
            required = true,
            placeholder = "Describe the main topic and key points to cover...",
            validation = ParameterValidation(
                minLength = 20,
                errorMessage = "Please provide a detailed topic description"
            )
        ),
        TaskParameter(
            key = "slideCount",
            displayName = "Number of Slides",
            type = ParameterType.NUMBER,
            required = false,
            placeholder = "10"
        ),
        TaskParameter(
            key = "audience",
            displayName = "Target Audience",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("executives", "professionals", "students", "general", "technical")
        ),
        TaskParameter(
            key = "style",
            displayName = "Presentation Style",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("business", "academic", "creative", "minimal", "corporate")
        ),
        TaskParameter(
            key = "includeImages",
            displayName = "Include Image Suggestions",
            type = ParameterType.BOOLEAN,
            required = false
        )
    )

    // ====================
    // RESEARCH & STUDY TASKS
    // ====================

    private fun getSummarizeVideoParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "youtubeUrl",
            displayName = "YouTube URL",
            type = ParameterType.URL,
            required = true,
            placeholder = "https://www.youtube.com/watch?v=...",
            validation = ParameterValidation(
                pattern = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/.+",
                errorMessage = "Please enter a valid YouTube URL"
            )
        ),
        TaskParameter(
            key = "summaryType",
            displayName = "Summary Type",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("detailed", "key_points", "transcript", "chapter_summary")
        ),
        TaskParameter(
            key = "includeTimestamps",
            displayName = "Include Timestamps",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "language",
            displayName = "Language",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("english", "spanish", "french", "german", "italian", "portuguese", "hindi", "chinese", "japanese", "auto-detect")
        )
    )

    private fun getResearchTopicParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "topic",
            displayName = "Research Topic",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Climate change impact on agriculture",
            validation = ParameterValidation(
                minLength = 5,
                maxLength = 200,
                errorMessage = "Topic must be between 5 and 200 characters"
            )
        ),
        TaskParameter(
            key = "researchDepth",
            displayName = "Research Depth",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("basic", "detailed", "comprehensive", "academic")
        ),
        TaskParameter(
            key = "sourcesRequired",
            displayName = "Number of Sources",
            type = ParameterType.NUMBER,
            required = false,
            placeholder = "5"
        ),
        TaskParameter(
            key = "includeStats",
            displayName = "Include Statistics",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "includeCitations",
            displayName = "Include Citations",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "academicLevel",
            displayName = "Academic Level",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("general", "high_school", "undergraduate", "graduate", "professional")
        )
    )

    private fun getCreateNotesParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "sourceContent",
            displayName = "Source Content",
            type = ParameterType.MULTILINE_TEXT,
            required = true,
            placeholder = "Paste text, URL, or describe the content to create notes from...",
            validation = ParameterValidation(
                minLength = 50,
                errorMessage = "Please provide sufficient content (minimum 50 characters)"
            )
        ),
        TaskParameter(
            key = "contentType",
            displayName = "Content Type",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("text", "url", "pdf", "article", "lecture", "book")
        ),
        TaskParameter(
            key = "noteStyle",
            displayName = "Note Style",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("bullet_points", "structured", "mind_map", "cornell", "outline")
        ),
        TaskParameter(
            key = "includeQuestions",
            displayName = "Include Study Questions",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "includeKeyTerms",
            displayName = "Include Key Terms",
            type = ParameterType.BOOLEAN,
            required = false
        )
    )

    // ====================
    // CREATIVE TASKS
    // ====================

    private fun getWriteBlogParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "topic",
            displayName = "Blog Topic",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "10 Tips for Remote Work Productivity",
            validation = ParameterValidation(
                minLength = 10,
                maxLength = 200,
                errorMessage = "Topic must be between 10 and 200 characters"
            )
        ),
        TaskParameter(
            key = "audience",
            displayName = "Target Audience",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("general", "professionals", "beginners", "experts", "students", "entrepreneurs")
        ),
        TaskParameter(
            key = "tone",
            displayName = "Writing Tone",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("informative", "conversational", "professional", "humorous", "inspirational")
        ),
        TaskParameter(
            key = "wordCount",
            displayName = "Word Count",
            type = ParameterType.NUMBER,
            required = false,
            placeholder = "800"
        ),
        TaskParameter(
            key = "includeOutline",
            displayName = "Include Outline",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "seoOptimized",
            displayName = "SEO Optimized",
            type = ParameterType.BOOLEAN,
            required = false
        )
    )

    private fun getDesignLogoParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "companyName",
            displayName = "Company Name",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "TechStart Inc.",
            validation = ParameterValidation(
                minLength = 2,
                maxLength = 50,
                errorMessage = "Company name must be between 2 and 50 characters"
            )
        ),
        TaskParameter(
            key = "industry",
            displayName = "Industry",
            type = ParameterType.SELECT,
            required = true,
            options = listOf("technology", "healthcare", "finance", "education", "retail", "food", "consulting", "creative", "manufacturing", "other")
        ),
        TaskParameter(
            key = "style",
            displayName = "Logo Style",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("modern", "classic", "minimalist", "bold", "playful", "elegant", "tech")
        ),
        TaskParameter(
            key = "colors",
            displayName = "Preferred Colors (comma-separated)",
            type = ParameterType.TEXT,
            required = false,
            placeholder = "blue, white, gray"
        ),
        TaskParameter(
            key = "includeText",
            displayName = "Include Company Name in Logo",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "conceptCount",
            displayName = "Number of Concepts",
            type = ParameterType.NUMBER,
            required = false,
            placeholder = "3"
        )
    )

    private fun getSocialMediaParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "platform",
            displayName = "Platform",
            type = ParameterType.SELECT,
            required = true,
            options = listOf("instagram", "twitter", "linkedin", "facebook", "tiktok", "youtube")
        ),
        TaskParameter(
            key = "topic",
            displayName = "Post Topic",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Monday motivation for entrepreneurs",
            validation = ParameterValidation(
                minLength = 5,
                maxLength = 200,
                errorMessage = "Topic must be between 5 and 200 characters"
            )
        ),
        TaskParameter(
            key = "tone",
            displayName = "Tone",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("engaging", "professional", "casual", "humorous", "inspirational", "educational")
        ),
        TaskParameter(
            key = "includeHashtags",
            displayName = "Include Hashtags",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "includeEmojis",
            displayName = "Include Emojis",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "postType",
            displayName = "Post Type",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("text", "image", "video", "carousel", "story", "reel")
        )
    )

    // ====================
    // BUSINESS TASKS
    // ====================

    private fun getCreateInvoiceParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "clientName",
            displayName = "Client Name",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Acme Corporation",
            validation = ParameterValidation(
                minLength = 2,
                maxLength = 100,
                errorMessage = "Client name must be between 2 and 100 characters"
            )
        ),
        TaskParameter(
            key = "clientEmail",
            displayName = "Client Email",
            type = ParameterType.EMAIL,
            required = true,
            placeholder = "billing@acme.com",
            validation = ParameterValidation(
                pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
                errorMessage = "Please enter a valid email address"
            )
        ),
        TaskParameter(
            key = "items",
            displayName = "Invoice Items (JSON format or description)",
            type = ParameterType.MULTILINE_TEXT,
            required = true,
            placeholder = "Web Development - 40 hours at $75/hour\nConsulting - 10 hours at $100/hour"
        ),
        TaskParameter(
            key = "dueDate",
            displayName = "Due Date",
            type = ParameterType.DATE,
            required = true
        ),
        TaskParameter(
            key = "currency",
            displayName = "Currency",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("USD", "EUR", "GBP", "INR", "CAD", "AUD", "JPY")
        ),
        TaskParameter(
            key = "taxRate",
            displayName = "Tax Rate (%)",
            type = ParameterType.NUMBER,
            required = false,
            placeholder = "0"
        ),
        TaskParameter(
            key = "notes",
            displayName = "Additional Notes",
            type = ParameterType.MULTILINE_TEXT,
            required = false,
            placeholder = "Payment terms, special instructions, etc."
        )
    )

    private fun getMarketAnalysisParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "industry",
            displayName = "Industry",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "E-commerce software",
            validation = ParameterValidation(
                minLength = 3,
                maxLength = 100,
                errorMessage = "Industry must be between 3 and 100 characters"
            )
        ),
        TaskParameter(
            key = "targetMarket",
            displayName = "Target Market",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Small to medium businesses",
            validation = ParameterValidation(
                minLength = 5,
                maxLength = 200,
                errorMessage = "Target market must be between 5 and 200 characters"
            )
        ),
        TaskParameter(
            key = "competitors",
            displayName = "Known Competitors (one per line)",
            type = ParameterType.MULTILINE_TEXT,
            required = false,
            placeholder = "Shopify\nWooCommerce\nBigCommerce"
        ),
        TaskParameter(
            key = "analysisType",
            displayName = "Analysis Type",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("basic", "detailed", "comprehensive", "competitive")
        ),
        TaskParameter(
            key = "includeSwot",
            displayName = "Include SWOT Analysis",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "includeTrends",
            displayName = "Include Market Trends",
            type = ParameterType.BOOLEAN,
            required = false
        ),
        TaskParameter(
            key = "region",
            displayName = "Geographic Region",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("global", "north_america", "europe", "asia_pacific", "india", "local")
        )
    )

    private fun getBusinessPlanParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "businessName",
            displayName = "Business Name",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "InnovateTech Solutions",
            validation = ParameterValidation(
                minLength = 2,
                maxLength = 100,
                errorMessage = "Business name must be between 2 and 100 characters"
            )
        ),
        TaskParameter(
            key = "industry",
            displayName = "Industry",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Software Development",
            validation = ParameterValidation(
                minLength = 3,
                maxLength = 100,
                errorMessage = "Industry must be between 3 and 100 characters"
            )
        ),
        TaskParameter(
            key = "businessType",
            displayName = "Business Type",
            type = ParameterType.SELECT,
            required = true,
            options = listOf("startup", "expansion", "acquisition", "franchise", "partnership")
        ),
        TaskParameter(
            key = "targetMarket",
            displayName = "Target Market",
            type = ParameterType.MULTILINE_TEXT,
            required = true,
            placeholder = "Describe your target customers, market size, demographics...",
            validation = ParameterValidation(
                minLength = 20,
                errorMessage = "Please provide a detailed target market description"
            )
        ),
        TaskParameter(
            key = "fundingNeeded",
            displayName = "Funding Needed ($)",
            type = ParameterType.NUMBER,
            required = false,
            placeholder = "100000"
        ),
        TaskParameter(
            key = "timeframe",
            displayName = "Planning Timeframe",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("1_year", "3_years", "5_years", "10_years")
        ),
        TaskParameter(
            key = "includeFinancials",
            displayName = "Include Financial Projections",
            type = ParameterType.BOOLEAN,
            required = false
        )
    )

    // ====================
    // WEB SCRAPER TASKS
    // ====================

    private fun getScrapeUrlParameters(): List<TaskParameter> = listOf(
        TaskParameter(
            key = "url",
            displayName = "Website URL",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "https://example.com"
        ),
        TaskParameter(
            key = "jobName",
            displayName = "Job Name",
            type = ParameterType.TEXT,
            required = true,
            placeholder = "Product Analysis Job"
        ),
        TaskParameter(
            key = "category",
            displayName = "Content Category",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("general", "news", "ecommerce", "research", "social", "monitoring"),
            defaultValue = "general"
        ),
        TaskParameter(
            key = "extractionType",
            displayName = "Extraction Type",
            type = ParameterType.SELECT,
            required = false,
            options = listOf("general", "news", "product", "research"),
            defaultValue = "general"
        )
    )
}
