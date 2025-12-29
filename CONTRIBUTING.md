# Contributing to TaskIt

First off, thank you for considering contributing to TaskIt! 🎉

It's people like you that make TaskIt such a great tool for productivity automation.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Coding Guidelines](#coding-guidelines)
- [Commit Messages](#commit-messages)
- [Pull Request Process](#pull-request-process)

## 📜 Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code.

### Our Standards

- **Be respectful** of differing viewpoints and experiences
- **Accept constructive criticism** gracefully
- **Focus on what is best** for the community
- **Show empathy** towards other community members

## 🤝 How Can I Contribute?

### Reporting Bugs 🐛

Before creating bug reports, please check existing issues. When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce** the issue
- **Expected vs actual behavior**
- **Screenshots** if applicable
- **Device/Android version** information
- **Logs** or error messages

### Suggesting Enhancements 💡

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion:

- **Use a clear and descriptive title**
- **Provide detailed description** of the suggested enhancement
- **Explain why this enhancement** would be useful
- **List alternatives** you've considered

### Your First Code Contribution 🎯

Unsure where to begin? Look for issues labeled:

- `good first issue` - Easy issues for beginners
- `help wanted` - Issues that need attention
- `documentation` - Documentation improvements

## 🛠️ Development Setup

### Prerequisites

```bash
# Install Android Studio
# Download from: https://developer.android.com/studio

# Install required SDKs
# Android SDK 24+
# Kotlin 1.9+
```

### Local Setup

1. **Fork the repository**
   ```bash
   # Click the "Fork" button on GitHub
   ```

2. **Clone your fork**
   ```bash
   git clone https://github.com/YOUR_USERNAME/taskit.git
   cd taskit
   ```

3. **Add upstream remote**
   ```bash
   git remote add upstream https://github.com/ORIGINAL_OWNER/taskit.git
   ```

4. **Create a branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

5. **Make your changes**
   ```bash
   # Edit files, add features, fix bugs
   ```

6. **Test your changes**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

## 📝 Coding Guidelines

### Kotlin Style Guide

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use **4 spaces** for indentation
- Maximum line length: **120 characters**
- Use **meaningful variable names**

### Code Structure

```kotlin
// ✅ Good
class TaskExecutionViewModel @Inject constructor(
    private val repository: TaskExecutionRepository
) : ViewModel() {
    // Clear, descriptive names
    private val _taskState = MutableStateFlow<TaskState>(TaskState.Idle)
    val taskState: StateFlow<TaskState> = _taskState.asStateFlow()
}

// ❌ Bad
class VM(val r: Repo) : ViewModel() {
    val s = MutableStateFlow(State())
}
```

### Compose Guidelines

```kotlin
// ✅ Good - Clear component structure
@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        // Content
    }
}

// ❌ Bad - Unclear parameters, no modifiers
@Composable
fun Card1(t: Task, f: () -> Unit) {
    // Implementation
}
```

### Documentation

```kotlin
/**
 * Executes a task with the given parameters
 *
 * @param taskType The type of task to execute
 * @param parameters Task-specific parameters
 * @return Result containing task execution data or error
 */
suspend fun executeTask(
    taskType: TaskType,
    parameters: Map<String, String>
): Result<TaskResult>
```

## 💬 Commit Messages

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Examples

```bash
# Good commits
feat(scraper): add support for PDF content extraction
fix(ui): resolve splash screen animation lag
docs(readme): update installation instructions
refactor(repo): simplify error handling logic

# Bad commits
update stuff
fixed bug
changes
```

## 🔄 Pull Request Process

### Before Submitting

- [ ] Code follows project style guidelines
- [ ] Self-review of your code completed
- [ ] Commented complex code sections
- [ ] Updated documentation as needed
- [ ] No new warnings generated
- [ ] Added tests that prove fix/feature works
- [ ] All tests pass locally
- [ ] Dependent changes merged and published

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
How has this been tested?

## Screenshots (if applicable)
Add screenshots here

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-reviewed code
- [ ] Commented complex areas
- [ ] Updated documentation
- [ ] No new warnings
- [ ] Added tests
- [ ] All tests pass
```

### Review Process

1. **Submit PR** - Create pull request to `main` branch
2. **CI Checks** - Automated tests run
3. **Code Review** - Maintainer reviews code
4. **Address Feedback** - Make requested changes
5. **Approval** - PR gets approved
6. **Merge** - Maintainer merges PR

## 🏗️ Project Structure

```
app/src/main/java/com/example/aiassistant/
├── data/           # Data layer (repositories, APIs)
├── di/             # Dependency injection
├── domain/         # Business logic (models, use cases)
├── presentation/   # UI layer (screens, ViewModels)
└── ui/theme/       # Design system
```

## 🧪 Testing

### Unit Tests

```kotlin
@Test
fun `executeTask returns success when valid parameters provided`() = runTest {
    // Arrange
    val repository = FakeTaskRepository()
    val viewModel = TaskViewModel(repository)
    
    // Act
    viewModel.executeTask(TaskType.WEB_SCRAPER, validParams)
    
    // Assert
    assertEquals(TaskState.Success, viewModel.taskState.value)
}
```

### UI Tests

```kotlin
@Test
fun taskCard_displaysCorrectInformation() {
    composeTestRule.setContent {
        TaskCard(task = sampleTask, onClick = {})
    }
    
    composeTestRule
        .onNodeWithText("Sample Task")
        .assertIsDisplayed()
}
```

## 📚 Additional Resources

- [Android Development Docs](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs)
- [Jetpack Compose Guide](https://developer.android.com/jetpack/compose)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## ❓ Questions?

Feel free to:
- Open an issue for questions
- Start a discussion on GitHub Discussions
- Reach out to maintainers

---

**Thank you for contributing to TaskIt! 🎉**
