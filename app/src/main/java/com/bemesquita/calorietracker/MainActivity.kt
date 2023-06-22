package com.bemesquita.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bemesquita.calorietracker.ui.theme.CalorieTrackerTheme
import com.bemesquita.core.domain.preferences.Preferences
import com.bemesquita.calorietracker.navigation.Route
import com.bemesquita.onboarding_presentation.activity.ActivityScreen
import com.bemesquita.onboarding_presentation.age.AgeScreen
import com.bemesquita.onboarding_presentation.gender.GenderScreen
import com.bemesquita.onboarding_presentation.goal.GoalScreen
import com.bemesquita.onboarding_presentation.height.HeightScreen
import com.bemesquita.onboarding_presentation.nutrient.NutrientGoalScreen
import com.bemesquita.onboarding_presentation.weight.WeightScreen
import com.bemesquita.onboarding_presentation.welcome.WelcomeScreen
import com.bemesquita.tracker_presentation.search.SearchScreen
import com.bemesquita.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shouldShowOnBoarding = preferences.loadShouldShowOnBoarding()

        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    content = {it
                        NavHost(
                            navController = navController,
                            startDestination = if(shouldShowOnBoarding) {
                                Route.WELCOME
                            } else Route.TRACKER_OVERVIEW

                        ) {
                            composable(Route.WELCOME) {
                                WelcomeScreen(onNextClick = { navController.navigate(Route.GENDER)})
                            }

                            composable(Route.GENDER) {
                                GenderScreen(onNextClick = { navController.navigate(Route.AGE)})
                            }

                            composable(Route.AGE) {
                                AgeScreen(
                                    scaffoldState = scaffoldState,
                                    onNextClick = { navController.navigate(Route.HEIGHT)}
                                )
                            }

                            composable(Route.HEIGHT) {
                                HeightScreen(
                                    scaffoldState = scaffoldState,
                                    onNextClick = { navController.navigate(Route.WEIGHT)}
                                )
                            }

                            composable(Route.WEIGHT) {
                                WeightScreen(
                                    scaffoldState = scaffoldState,
                                    onNextClick = { navController.navigate(Route.ACTIVITY)}
                                )
                            }

                            composable(Route.ACTIVITY) {
                                ActivityScreen(onNextClick = { navController.navigate(Route.GOAL)})
                            }

                            composable(Route.GOAL) {
                                GoalScreen(onNextClick = { navController.navigate(Route.NUTRIENT_GOAL)})
                            }

                            composable(Route.NUTRIENT_GOAL) {
                                NutrientGoalScreen(
                                    scaffoldState = scaffoldState,
                                    onNextClick = { navController.navigate(Route.TRACKER_OVERVIEW)}
                                )
                            }

                            composable(Route.TRACKER_OVERVIEW) {
                                TrackerOverviewScreen(
                                    onNavigateToSearch = { mealName, day, month, year ->
                                        navController.navigate(
                                            Route.SEARCH + "/${mealName}" +
                                                    "/${day}" +
                                                    "/${month}" +
                                                    "/${year}"
                                        )
                                    }
                                )
                            }

                            composable(
                                route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                                arguments = listOf(
                                    navArgument("mealName") { type = NavType.StringType },
                                    navArgument("dayOfMonth") { type = NavType.IntType },
                                    navArgument("month") { type = NavType.IntType },
                                    navArgument("year") { type = NavType.IntType }
                                )

                            ) { stackEntry ->
                                val mealName = stackEntry.arguments?.getString("mealName")!!
                                val dayOfMonth = stackEntry.arguments?.getInt("dayOfMonth")!!
                                val month = stackEntry.arguments?.getInt("month")!!
                                val year = stackEntry.arguments?.getInt("year")!!

                                SearchScreen(
                                    scaffoldState = scaffoldState,
                                    mealName = mealName,
                                    dayOfMonth = dayOfMonth,
                                    month = month,
                                    year = year,
                                    onNavigateUp = { navController.navigateUp() }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}