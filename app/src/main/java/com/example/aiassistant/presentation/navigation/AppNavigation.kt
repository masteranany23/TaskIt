package com.example.aiassistant.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aiassistant.presentation.ui.splash.SplashScreen
import com.example.aiassistant.presentation.ui.tasks.TaskLibraryScreen
import com.example.aiassistant.presentation.ui.tasks.TaskDetailScreen
import com.example.aiassistant.presentation.ui.tasks.TaskExecutionScreen
import com.example.aiassistant.presentation.ui.tasks.TaskSearchScreen
import com.example.aiassistant.presentation.ui.settings.SettingsScreen
import com.example.aiassistant.presentation.ui.voice.VoiceCommandScreen
import com.example.aiassistant.presentation.ui.scheduler.SmartSchedulerScreen
import com.example.aiassistant.presentation.ui.chat.AIChatScreen
import com.example.aiassistant.presentation.ui.scraper.WebScraperScreen
import com.example.aiassistant.presentation.ui.documents.DocumentHubScreen

/**
 * AppNavigation - Main navigation controller for the app
 * Handles routing between different screens with smooth transitions
 * Features futuristic slide animations consistent with the app theme
 */
@Composable
fun AppNavigation() {
    // Create navigation controller to manage screen transitions
    val navController = rememberNavController()
    
    // Define navigation host with all app screens and smooth animations
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        // Global animation settings for consistent transitions
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it/3 },
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it/3 },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        }
    ) {
        // Splash screen - App entry point with branding animation
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToTaskLibrary = {
                    // Navigate to main screen after splash animation
                    navController.navigate(Screen.TaskLibrary.route) {
                        // Remove splash from back stack
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Task Library screen - Main feature screen with task categories
        composable(Screen.TaskLibrary.route) {
            TaskLibraryScreen(
                onTaskClick = { task ->
                    // Navigate to task detail screen
                    navController.navigate(Screen.TaskDetail.createRoute(task.id))
                },
                onSearchClick = {
                    // Navigate to search screen
                    navController.navigate(Screen.TaskSearch.route)
                },
                onSettingsClick = {
                    // Navigate to settings screen
                    navController.navigate(Screen.Settings.route)
                },
                onVoiceClick = {
                    // Navigate to voice command screen
                    navController.navigate(Screen.VoiceCommand.route)
                },
                onSchedulerClick = {
                    // Navigate to smart scheduler screen
                    navController.navigate(Screen.SmartScheduler.route)
                },
                onChatClick = {
                    // Navigate to AI chat screen
                    navController.navigate(Screen.AIChat.route)
                },
                onScraperClick = {
                    // Navigate to web scraper screen
                    navController.navigate(Screen.WebScraper.route)
                },
                onDocumentHubClick = {
                    // Navigate to document hub screen
                    navController.navigate(Screen.DocumentHub.route)
                }
            )
        }
        
        // Task Detail screen - Detailed view of individual tasks
        composable(
            route = Screen.TaskDetail.route,
            arguments = Screen.TaskDetail.arguments
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            TaskDetailScreen(
                taskId = taskId,
                onBackClick = {
                    navController.popBackStack()
                },
                onExecuteTask = {
                    // Navigate to task execution screen
                    navController.navigate(Screen.TaskExecution.createRoute(taskId))
                }
            )
        }
        
        // Task Execution screen - Dynamic form and execution
        composable(
            route = Screen.TaskExecution.route,
            arguments = Screen.TaskExecution.arguments
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            TaskExecutionScreen(
                taskId = taskId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Task Search screen - Search and filter tasks
        composable(Screen.TaskSearch.route) {
            TaskSearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTaskClick = { task ->
                    navController.navigate(Screen.TaskDetail.createRoute(task.id))
                }
            )
        }
        
        // Settings screen - User preferences and app configuration
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Voice Command screen - Voice-first AI interaction
        composable(Screen.VoiceCommand.route) {
            VoiceCommandScreen(
                onTaskExecute = { task ->
                    // Navigate to task detail for execution
                    navController.navigate(Screen.TaskDetail.createRoute(task.id))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToTasks = {
                    // Navigate back to task library
                    navController.popBackStack()
                }
            )
        }
        
        // Smart Scheduler screen - Module 3: AI-powered scheduling
        composable(Screen.SmartScheduler.route) {
            SmartSchedulerScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTaskClick = { task ->
                    navController.navigate(Screen.TaskDetail.createRoute(task.id))
                },
                onChatClick = {
                    navController.navigate(Screen.AIChat.route)
                }
            )
        }
        
        // AI Chat screen - Module 4: Intelligent conversation interface
        composable(Screen.AIChat.route) {
            AIChatScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTaskClick = { task ->
                    navController.navigate(Screen.TaskDetail.createRoute(task.id))
                },
                onScheduleClick = {
                    navController.navigate(Screen.SmartScheduler.route)
                },
                onVoiceClick = {
                    navController.navigate(Screen.VoiceCommand.route)
                }
            )
        }
        
        // Web Scraper screen - Module 5: AI-powered web scraping
        composable(Screen.WebScraper.route) {
            WebScraperScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Document Hub screen - Module 6: Centralized document management
        composable(Screen.DocumentHub.route) {
            DocumentHubScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Screen - Sealed class defining all app screens
 * Provides type-safe navigation routes with parameters
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object TaskLibrary : Screen("task_library")
    object VoiceCommand : Screen("voice_command")
    object SmartScheduler : Screen("smart_scheduler")
    object AIChat : Screen("ai_chat")
    object WebScraper : Screen("web_scraper")
    object DocumentHub : Screen("document_hub")
    
    object TaskDetail : Screen("task_detail/{taskId}") {
        val arguments = listOf(
            androidx.navigation.navArgument("taskId") { 
                type = androidx.navigation.NavType.StringType 
            }
        )
        fun createRoute(taskId: String) = "task_detail/$taskId"
    }
    
    object TaskExecution : Screen("task_execution/{taskId}") {
        val arguments = listOf(
            androidx.navigation.navArgument("taskId") { 
                type = androidx.navigation.NavType.StringType 
            }
        )
        fun createRoute(taskId: String) = "task_execution/$taskId"
    }
    
    object TaskSearch : Screen("task_search")
    object Settings : Screen("settings")
}
