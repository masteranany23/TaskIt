# TaskIt - Smart AI-Powered Task Management App 🚀

<div align="center">

![TaskIt Logo](https://img.shields.io/badge/TaskIt-Smart_Task_Management-blue?style=for-the-badge)
[![Android](https://img.shields.io/badge/Platform-Android-green?style=flat-square&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?style=flat-square&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack_Compose-blue?style=flat-square)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

**Transform your productivity with AI-powered task automation**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Installation](#-installation) • [Usage](#-usage) • [Screenshots](#-screenshots) • [Contributing](#-contributing)

</div>

---

## 📱 About TaskIt

TaskIt is a modern Android application that revolutionizes productivity through intelligent AI-powered task automation. Built with cutting-edge Android technologies, it seamlessly integrates multiple AI services to handle complex workflows ranging from content analysis to business report generation.

### 🎯 Why TaskIt?

- ⚡ **Save Time** - Automate repetitive tasks with AI-powered workflows
- 🎨 **Beautiful UI** - Modern, sleek interface with smooth animations
- 🤖 **AI Integration** - Powered by OpenAI, Firecrawl, and n8n automation
- 📊 **Smart Analysis** - Intelligent content processing and summarization
- 🛡️ **Reliable** - Zero-crash architecture with comprehensive error handling

---

## ✨ Features

### 📋 Task Categories

#### 🏠 **Everyday Tasks**
- Send automated emails
- Set smart reminders
- Generate daily summaries
- Create shopping lists

#### 💼 **Office Work**
- Generate business reports
- Schedule meetings with calendar integration
- Create professional presentations
- Automate email responses

#### 📚 **Research & Study**
- Summarize YouTube videos
- Scrape and analyze web content
- Generate study notes
- Extract key insights from articles

#### 🎨 **Creative Tasks**
- Write blog posts
- Generate social media content
- Create design concepts
- Draft marketing copy

#### 📊 **Business Operations**
- Create invoices and receipts
- Perform market analysis
- Generate business plans
- Track expenses

### 🚀 Advanced Capabilities

- **Web Scraping** - Extract and analyze content from any website
- **Video Summarization** - Get key points from YouTube videos instantly
- **Document Generation** - AI-powered reports and presentations
- **Real-time Processing** - Live task execution with progress tracking
- **Smart Analysis** - Context-aware content processing

---

## 🛠️ Tech Stack

### Frontend
- **Kotlin** - Modern, concise language for Android
- **Jetpack Compose** - Declarative UI framework
- **Material Design 3** - Contemporary design system
- **Hilt/Dagger** - Dependency injection
- **Kotlin Coroutines** - Asynchronous programming

### Backend & Integration
- **n8n** - Workflow automation platform
- **OpenAI API** - AI content processing
- **Firecrawl API** - Web scraping service
- **Retrofit + OkHttp** - HTTP networking
- **Gson** - JSON serialization

### Architecture
- **MVVM Pattern** - Clean architecture
- **Repository Pattern** - Data layer abstraction
- **Reactive Programming** - Flow-based streams
- **Clean Code Principles** - Maintainable codebase

---

## 📦 Installation

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+
- Kotlin 1.9+
- JDK 11 or higher

### Setup Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/taskit.git
cd taskit
```

2. **Open in Android Studio**
```bash
# Open Android Studio and select "Open an Existing Project"
# Navigate to the cloned directory
```

3. **Configure API Keys**

Create a `local.properties` file in the root directory:
```properties
# n8n Configuration
N8N_BASE_URL=your_n8n_instance_url
N8N_API_KEY=your_n8n_api_key

# Optional: Add other API keys if needed
```

4. **Build the project**
```bash
./gradlew build
```

5. **Run on device/emulator**
```bash
./gradlew installDebug
```

---

## 🔧 Configuration

### n8n Workflow Setup

1. **Install n8n**
```bash
npm install -g n8n
```

2. **Import Workflows**
- Navigate to `n8n-workflows/` directory
- Import JSON workflow files into your n8n instance
- Configure API credentials (OpenAI, Firecrawl)

3. **Configure Webhooks**
- Update webhook URLs in the app's `NetworkModule.kt`
- Ensure n8n instance is accessible from your device

### API Configuration

**Required APIs:**
- **Firecrawl API** - [Sign up at firecrawl.dev](https://firecrawl.dev)
- **OpenAI API** - [Get API key](https://platform.openai.com)
- **n8n** - [Self-hosted or cloud](https://n8n.io)

---

## 💡 Usage

### Basic Usage

1. **Launch TaskIt**
   - Open the app and explore the task library

2. **Select a Task**
   - Choose from 20+ pre-configured task types
   - Browse by category (Everyday, Office, Research, Creative, Business)

3. **Fill Parameters**
   - Enter required information (URL, text, preferences)
   - Customize task settings

4. **Execute & View Results**
   - Task processes through AI workflow
   - View structured results in real-time

### Example: Web Scraping

```kotlin
// 1. Navigate to Web Scraper screen
// 2. Enter URL: https://example.com
// 3. Select extraction type: General/News/Product/Research
// 4. Click "Create & Execute Job"
// 5. View AI-analyzed results
```

### Example: YouTube Summary

```kotlin
// 1. Select "Summarize YouTube Video"
// 2. Paste video URL
// 3. Choose summary length
// 4. Get key points instantly
```

---



---

## 🏗️ Project Structure

```
taskit/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/aiassistant/
│   │   │   │   ├── data/              # Data layer
│   │   │   │   │   ├── remote/        # API services
│   │   │   │   │   └── repository/    # Repository implementations
│   │   │   │   ├── di/                # Dependency injection
│   │   │   │   ├── domain/            # Domain layer
│   │   │   │   │   ├── model/         # Data models
│   │   │   │   │   └── usecase/       # Business logic
│   │   │   │   ├── presentation/      # UI layer
│   │   │   │   │   ├── ui/            # Composable screens
│   │   │   │   │   └── viewmodel/     # ViewModels
│   │   │   │   └── ui/theme/          # Design system
│   │   │   └── res/                   # Resources
│   │   └── test/                      # Unit tests
│   └── build.gradle.kts
├── n8n-workflows/                     # Automation workflows
│   └── web-scraper-workflow.json
├── screenshots/                       # App screenshots
├── .gitignore
├── README.md
└── LICENSE
```

---

## 🔍 Key Technical Features

### Performance Optimizations
- **60-second timeout** for long-running AI operations
- **Efficient memory management** with proper lifecycle handling
- **Smooth animations** at 60fps throughout the app
- **Fast app launch** with optimized splash screen (2.5s)

### Error Handling
- **Comprehensive fallback mechanisms** for network failures
- **Type-safe data processing** with null safety
- **Graceful error recovery** with user-friendly messages
- **Retry logic** for temporary network issues

### Security
- **API key protection** using local.properties
- **Secure networking** with HTTPS enforcement
- **Input validation** for all user inputs
- **Safe data processing** with sanitization

---



## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@masteranany23](https://github.com/masteranany23)
- LinkedIn: [Anany Mishra](https://linkedin.com/in/mishra-anany)
- Email: masteranany23@gmail.com
---

## 🙏 Acknowledgments

- [OpenAI](https://openai.com) - For AI capabilities
- [Firecrawl](https://firecrawl.dev) - For web scraping
- [n8n](https://n8n.io) - For workflow automation
- [Android Team](https://developer.android.com) - For Jetpack Compose
- All contributors who helped improve TaskIt

---

## 🗺️ Roadmap

### Version 2.0 (Planned)
- [ ] Voice commands for task initiation
- [ ] Offline mode with local processing
- [ ] Team collaboration features
- [ ] Analytics dashboard
- [ ] Custom workflow builder
- [ ] Dark/Light theme toggle
- [ ] Widget support
- [ ] Backup & sync

### Future Ideas
- [ ] iOS version
- [ ] Web dashboard
- [ ] Browser extension
- [ ] API for third-party integrations
- [ ] Machine learning model optimization

---

## ❓ FAQ

**Q: Is TaskIt free to use?**  
A: Yes, TaskIt is open-source. However, you'll need your own API keys for OpenAI and Firecrawl.

**Q: Can I use it offline?**  
A: Currently, TaskIt requires internet connection for AI processing. Offline mode is planned for v2.0.

**Q: How do I add custom tasks?**  
A: Create your own n8n workflows and add them to the app following the existing pattern.

**Q: Is my data secure?**  
A: Yes, all API calls are encrypted and your data is processed securely through your own n8n instance.

---



<div align="center">

**⭐ If you find TaskIt helpful, please star this repository! ⭐**

Made with ❤️ and ☕ by Anany Mishra

</div>
