# PowerShell Test Script for YouTube Summary n8n Workflow
# Usage: .\test_youtube_workflow.ps1 [n8n_base_url]

param(
    [string]$N8nBaseUrl = "https://your-n8n-instance.com"
)

# Configuration
$WebhookEndpoint = "$N8nBaseUrl/webhook/youtube-summary"

# Test data
$TestYouTubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
$TestYouTuBeUrl = "https://youtu.be/dQw4w9WgXcQ"
$InvalidUrl = "https://example.com/not-youtube"

Write-Host "🧪 Testing YouTube Summary n8n Workflow" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host "Endpoint: $WebhookEndpoint" -ForegroundColor White
Write-Host ""

# Function to test a URL
function Test-Url {
    param(
        [string]$TestName,
        [string]$Url,
        [bool]$ExpectSuccess
    )
    
    Write-Host "🔍 Test: $TestName" -ForegroundColor Yellow
    Write-Host "URL: $Url" -ForegroundColor Gray
    
    $RequestBody = @{
        youtubeUrl = $Url
        summaryType = "detailed"
        language = "english"
    } | ConvertTo-Json
    
    try {
        $Response = Invoke-RestMethod -Uri $WebhookEndpoint -Method Post -Body $RequestBody -ContentType "application/json" -TimeoutSec 30
        
        Write-Host "✅ Request successful" -ForegroundColor Green
        Write-Host "Response Status: $($Response.status)" -ForegroundColor White
        Write-Host "Message: $($Response.message)" -ForegroundColor White
        
        if ($Response.status -eq "success") {
            Write-Host "✅ Workflow executed successfully" -ForegroundColor Green
            
            if ($Response.data.video.title) {
                Write-Host "Video Title: $($Response.data.video.title)" -ForegroundColor White
            }
            
            if ($Response.data.summary.content) {
                $ContentLength = $Response.data.summary.content.Length
                Write-Host "✅ Summary generated ($ContentLength characters)" -ForegroundColor Green
                Write-Host "Word Count: $($Response.data.summary.wordCount)" -ForegroundColor White
                Write-Host "Reading Time: $($Response.data.summary.estimatedReadingTime)" -ForegroundColor White
            }
            
        } elseif ($Response.status -eq "error") {
            Write-Host "❌ Workflow error: $($Response.message)" -ForegroundColor Red
            if ($ExpectSuccess) {
                Write-Host "⚠️  Expected success but got error" -ForegroundColor Yellow
            }
            
            if ($Response.data.error) {
                Write-Host "Error Details: $($Response.data.error.message)" -ForegroundColor Red
            }
        }
        
    } catch {
        $StatusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "❌ HTTP Error: $StatusCode" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        
        if ($_.Exception.Response) {
            $ResponseStream = $_.Exception.Response.GetResponseStream()
            $Reader = New-Object System.IO.StreamReader($ResponseStream)
            $ResponseBody = $Reader.ReadToEnd()
            Write-Host "Response Body: $ResponseBody" -ForegroundColor Red
        }
    }
    
    Write-Host ""
    Write-Host "---" -ForegroundColor Gray
    Write-Host ""
}

# Test Cases
Write-Host "Running test cases..." -ForegroundColor Cyan
Write-Host ""

# Test 1: Valid YouTube URL (youtube.com format)
Test-Url -TestName "Valid YouTube URL (youtube.com)" -Url $TestYouTubeUrl -ExpectSuccess $true

# Test 2: Valid YouTube URL (youtu.be format)
Test-Url -TestName "Valid YouTube URL (youtu.be)" -Url $TestYouTuBeUrl -ExpectSuccess $true

# Test 3: Invalid URL
Test-Url -TestName "Invalid URL (should fail)" -Url $InvalidUrl -ExpectSuccess $false

# Test 4: Empty URL
Test-Url -TestName "Empty URL (should fail)" -Url "" -ExpectSuccess $false

# Test 5: YouTube URL with parameters
Test-Url -TestName "YouTube URL with parameters" -Url "https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=30s" -ExpectSuccess $true

Write-Host "🏁 Testing Complete" -ForegroundColor Cyan
Write-Host ""
Write-Host "💡 Next Steps:" -ForegroundColor Yellow
Write-Host "1. Check n8n workflow logs for detailed execution info"
Write-Host "2. Verify OpenAI credentials are configured correctly"
Write-Host "3. Test with different video types (short, long, educational)"
Write-Host "4. Monitor execution times and adjust timeouts if needed"
Write-Host ""
Write-Host "📱 Android App Testing:" -ForegroundColor Yellow
Write-Host "1. Update ApiClient.java with your n8n base URL"
Write-Host "2. Test the complete user flow in the app"
Write-Host "3. Verify error handling and timeout behavior"
Write-Host "4. Test share functionality and intent handling"
