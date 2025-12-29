#!/bin/bash

# Test script for YouTube Summary n8n Workflow
# Usage: ./test_youtube_workflow.sh [n8n_base_url]

# Configuration
N8N_BASE_URL="${1:-https://your-n8n-instance.com}"
WEBHOOK_ENDPOINT="${N8N_BASE_URL}/webhook/youtube-summary"

# Test data
TEST_YOUTUBE_URL="https://www.youtube.com/watch?v=dQw4w9WgXcQ"
TEST_YOUTU_BE_URL="https://youtu.be/dQw4w9WgXcQ"
INVALID_URL="https://example.com/not-youtube"

echo "🧪 Testing YouTube Summary n8n Workflow"
echo "======================================"
echo "Endpoint: $WEBHOOK_ENDPOINT"
echo ""

# Function to test a URL
test_url() {
    local test_name="$1"
    local url="$2"
    local expect_success="$3"
    
    echo "🔍 Test: $test_name"
    echo "URL: $url"
    
    response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" \
        -X POST \
        -H "Content-Type: application/json" \
        -d "{
            \"youtubeUrl\": \"$url\",
            \"summaryType\": \"detailed\",
            \"language\": \"english\"
        }" \
        "$WEBHOOK_ENDPOINT")
    
    # Extract HTTP status
    http_status=$(echo "$response" | grep "HTTP_STATUS:" | cut -d: -f2)
    json_response=$(echo "$response" | sed '/HTTP_STATUS:/d')
    
    echo "HTTP Status: $http_status"
    
    if [ "$http_status" -eq 200 ]; then
        echo "✅ Request successful"
        
        # Parse JSON response (basic parsing)
        status=$(echo "$json_response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        message=$(echo "$json_response" | grep -o '"message":"[^"]*"' | cut -d'"' -f4)
        
        echo "Response Status: $status"
        echo "Message: $message"
        
        if [ "$status" = "success" ]; then
            echo "✅ Workflow executed successfully"
            
            # Extract video title if available
            title=$(echo "$json_response" | grep -o '"title":"[^"]*"' | cut -d'"' -f4 | head -1)
            if [ ! -z "$title" ]; then
                echo "Video Title: $title"
            fi
            
            # Extract summary content length
            content_length=$(echo "$json_response" | grep -o '"content":"[^"]*"' | wc -c)
            if [ "$content_length" -gt 100 ]; then
                echo "✅ Summary generated (${content_length} characters)"
            fi
            
        elif [ "$status" = "error" ]; then
            echo "❌ Workflow error: $message"
            if [ "$expect_success" = "true" ]; then
                echo "⚠️  Expected success but got error"
            fi
        fi
        
    else
        echo "❌ HTTP Error: $http_status"
        echo "Response: $json_response"
    fi
    
    echo ""
    echo "---"
    echo ""
}

# Test Cases
echo "Running test cases..."
echo ""

# Test 1: Valid YouTube URL (youtube.com format)
test_url "Valid YouTube URL (youtube.com)" "$TEST_YOUTUBE_URL" "true"

# Test 2: Valid YouTube URL (youtu.be format)  
test_url "Valid YouTube URL (youtu.be)" "$TEST_YOUTU_BE_URL" "true"

# Test 3: Invalid URL
test_url "Invalid URL (should fail)" "$INVALID_URL" "false"

# Test 4: Empty URL
test_url "Empty URL (should fail)" "" "false"

# Test 5: YouTube URL with parameters
test_url "YouTube URL with parameters" "https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=30s" "true"

echo "🏁 Testing Complete"
echo ""
echo "💡 Next Steps:"
echo "1. Check n8n workflow logs for detailed execution info"
echo "2. Verify OpenAI credentials are configured correctly"
echo "3. Test with different video types (short, long, educational)"
echo "4. Monitor execution times and adjust timeouts if needed"
echo ""
echo "📱 Android App Testing:"
echo "1. Update ApiClient.java with your n8n base URL"
echo "2. Test the complete user flow in the app"
echo "3. Verify error handling and timeout behavior"
echo "4. Test share functionality and intent handling"
