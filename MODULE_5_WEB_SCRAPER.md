# Module 5: Web Scraper - Technical Documentation

## Overview
The Web Scraper module provides intelligent web scraping capabilities with n8n integration, real-time monitoring, and AI-powered data processing. This module is designed to be easily debuggable and implementable in n8n workflows.

## Features

### 🕷️ Core Functionality
- **Automated Web Scraping**: Schedule and execute web scraping jobs
- **Real-time Monitoring**: Live status updates and progress tracking
- **n8n Integration**: Direct webhook support for workflow automation
- **Smart Filtering**: AI-powered data filtering and processing
- **Category Management**: Organize scrapes by type (News, E-commerce, Social, etc.)

### 🎯 Smart Features
- **Frequency Control**: Manual, Hourly, Daily, Weekly, Monthly schedules
- **Status Management**: Real-time job status (Pending, Running, Success, Error, Paused)
- **Data Deduplication**: Hash-based change detection
- **Filter System**: Include/exclude/transform/tag data automatically
- **Error Handling**: Comprehensive error reporting and recovery

## Architecture

### Data Models
```kotlin
// Core scraping job
data class ScrapeJob(
    val id: String,
    val name: String,
    val url: String,
    val selector: String = "",
    val frequency: ScrapeFrequency,
    val isActive: Boolean,
    val status: ScrapeStatus,
    val webhook: String? = null, // n8n webhook URL
    val category: ScrapeCategory
)

// Scraped data with metadata
data class ScrapedData(
    val id: String,
    val jobId: String,
    val content: String,
    val metadata: Map<String, String>,
    val hash: String, // For change detection
    val tags: List<String>
)
```

### UI Components
- **ScraperHeader**: Navigation, search, and monitoring controls
- **ScraperStats**: Real-time statistics dashboard
- **CategoryFilter**: Visual category selection
- **ScrapeJobCard**: Individual job management cards
- **CreateJobDialog**: Job creation with validation

## n8n Integration Guide

### 1. Webhook Setup
```javascript
// n8n HTTP Trigger node
{
  "httpMethod": "POST",
  "path": "scraper-webhook",
  "options": {}
}
```

### 2. Data Processing Workflow
```javascript
// Example n8n workflow structure:
1. HTTP Trigger (receive scraped data)
2. Filter Node (apply data rules)
3. Transform Node (format data)
4. Database Node (store results)
5. Notification Node (send alerts)
```

### 3. Job Control
```javascript
// Create scraping job via n8n
const newJob = {
  name: "Product Price Monitor",
  url: "https://store.example.com/products",
  selector: ".price",
  frequency: "HOURLY",
  webhook: "https://your-n8n.com/webhook/scraper-webhook",
  filters: [
    {
      type: "CONTAINS",
      condition: "$",
      action: "INCLUDE"
    }
  ]
};
```

## Debugging Features

### 1. Visual Status Indicators
- **Real-time Status Badges**: Color-coded job states
- **Progress Monitoring**: Live job execution tracking
- **Error Reporting**: Detailed error messages and logs

### 2. Data Inspection
- **Preview System**: View scraped data before processing
- **Hash Comparison**: Track data changes over time
- **Filter Testing**: Test data filters before applying

### 3. Performance Metrics
- **Success Rate Tracking**: Monitor job reliability
- **Execution Time**: Track scraping performance
- **Data Volume**: Monitor scraped data size

## Implementation Guide

### 1. Job Creation
```kotlin
// Create a new scraping job
val newJob = ScrapeJob(
    id = generateId(),
    name = "News Headlines",
    url = "https://news.site.com",
    selector = "h2.headline",
    frequency = ScrapeFrequency.HOURLY,
    category = ScrapeCategory.NEWS,
    webhook = "https://n8n.example.com/webhook/news"
)
```

### 2. Filter Configuration
```kotlin
// Set up data filters
val filters = listOf(
    ScrapeFilter(
        type = FilterType.CONTAINS,
        condition = "breaking",
        action = FilterAction.TAG
    ),
    ScrapeFilter(
        type = FilterType.LENGTH,
        condition = "10",
        action = FilterAction.EXCLUDE
    )
)
```

### 3. Monitoring Setup
```kotlin
// Monitor job execution
fun monitorJobs(jobs: List<ScrapeJob>) {
    jobs.filter { it.isActive }
        .forEach { job ->
            when (job.status) {
                ScrapeStatus.RUNNING -> showProgress(job)
                ScrapeStatus.ERROR -> showError(job)
                ScrapeStatus.SUCCESS -> showSuccess(job)
            }
        }
}
```

## UI/UX Design Principles

### 🎨 Visual Design
- **Futuristic Theme**: Consistent with app's sci-fi aesthetic
- **Color Coding**: Intuitive status and category colors
- **Smooth Animations**: Engaging transitions and feedback
- **Clean Layout**: Easy-to-scan information hierarchy

### 🔧 Usability
- **One-Click Actions**: Quick job control (start/stop/run)
- **Drag & Drop**: Easy job reordering and organization
- **Smart Defaults**: Intelligent job configuration suggestions
- **Bulk Operations**: Multi-job management capabilities

## Performance Considerations

### 📊 Optimization
- **Lazy Loading**: Load job data on demand
- **Caching**: Store frequently accessed data
- **Pagination**: Handle large job lists efficiently
- **Background Processing**: Non-blocking UI operations

### 🛡️ Security
- **URL Validation**: Prevent malicious scraping targets
- **Rate Limiting**: Respect target site limits
- **Data Sanitization**: Clean scraped content
- **Access Control**: User-based job permissions

## Error Handling

### 🚨 Common Issues
1. **Network Timeouts**: Automatic retry with backoff
2. **Selector Changes**: Smart selector adaptation
3. **Rate Limiting**: Respect robots.txt and limits
4. **Invalid Data**: Robust parsing and validation

### 🔧 Recovery Strategies
- **Automatic Retries**: Exponential backoff for failures
- **Fallback Selectors**: Alternative data extraction methods
- **Error Notifications**: Real-time failure alerts
- **Manual Override**: User intervention for complex issues

## Testing & Validation

### ✅ Test Scenarios
1. **Job Creation**: Validate all required fields
2. **Scraping Logic**: Test selector accuracy
3. **Frequency Execution**: Verify scheduling works
4. **Data Processing**: Confirm filter application
5. **n8n Integration**: Test webhook delivery

### 🔍 Debugging Tools
- **Inspector Mode**: Visual selector testing
- **Log Viewer**: Detailed execution logs
- **Data Preview**: Before/after data comparison
- **Performance Monitor**: Execution time tracking

## Future Enhancements

### 🚀 Roadmap
1. **AI-Powered Selectors**: Automatic element detection
2. **Visual Scraping**: Point-and-click data selection
3. **Collaborative Features**: Team job sharing
4. **Advanced Analytics**: Trend analysis and insights
5. **Mobile Support**: Responsive design improvements

This Web Scraper module provides a comprehensive, user-friendly, and n8n-compatible solution for automated web data extraction with extensive debugging capabilities and a futuristic UI design.
