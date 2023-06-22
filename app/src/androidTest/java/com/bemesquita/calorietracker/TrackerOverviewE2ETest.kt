package com.bemesquita.calorietracker

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.google.common.truth.Truth.assertThat
import com.bemesquita.calorietracker.navigation.Route
import com.bemesquita.calorietracker.repository.TrackerRepositoryFake
import com.bemesquita.calorietracker.ui.theme.CalorieTrackerTheme
import com.bemesquita.core.domain.model.ActivityLevel
import com.bemesquita.core.domain.model.Gender
import com.bemesquita.core.domain.model.GoalType
import com.bemesquita.core.domain.model.UserInfo
import com.bemesquita.core.domain.preferences.Preferences
import com.bemesquita.core.domain.use_case.FilterOutDigits
import com.bemesquita.tracker_domain.model.TrackableFood
import com.bemesquita.tracker_domain.use_case.CalculateMealNutrients
import com.bemesquita.tracker_domain.use_case.DeleteTrackedFood
import com.bemesquita.tracker_domain.use_case.GetFoodsByDate
import com.bemesquita.tracker_domain.use_case.SearchFood
import com.bemesquita.tracker_domain.use_case.TrackFood
import com.bemesquita.tracker_domain.use_case.TrackerUseCases
import com.bemesquita.tracker_presentation.search.SearchScreen
import com.bemesquita.tracker_presentation.search.SearchViewModel
import com.bemesquita.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.bemesquita.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@HiltAndroidTest
class TrackerOverviewE2ETest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )
        repositoryFake = TrackerRepositoryFake()
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(repositoryFake),
            searchFood = SearchFood(repositoryFake),
            getFoodsByDate = GetFoodsByDate(repositoryFake),
            deleteTrackedFood = DeleteTrackedFood(repositoryFake),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases
        )
        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            filterOutDigits = FilterOutDigits()
        )

        composeRule.activity.setContent {
            CalorieTrackerTheme {
                val scaffoldState = rememberScaffoldState()
                navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) { it

                    NavHost(
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearch = { mealName, day, month, year ->
                                    navController.navigate(
                                        Route.SEARCH + "/$mealName" +
                                                "/$day" +
                                                "/$month" +
                                                "/$year"
                                    )
                                },
                                viewModel = trackerOverviewViewModel
                            )
                        }

                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") {
                                    type = NavType.StringType
                                },
                                navArgument("dayOfMonth") {
                                    type = NavType.IntType
                                },
                                navArgument("month") {
                                    type = NavType.IntType
                                },
                                navArgument("year") {
                                    type = NavType.IntType
                                },
                            )
                        ) {stackEntry ->
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
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        repositoryFake.searchResults = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                proteinPer100g = 5,
                carbsPer100g = 50,
                fatPer100g = 1
            )
        )

        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        //==========================================================================================
        // Display tracker overview, click on add breakfast, should go to search screen

        composeRule.onNodeWithText("Add Breakfast").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Breakfast").performClick()
        composeRule.onNodeWithText("Add Breakfast").assertIsDisplayed()
        composeRule.onNodeWithText("Add Breakfast").performClick()

        assertThat(navController.currentDestination?.route?.startsWith(Route.SEARCH)).isTrue()

        //==========================================================================================
        // At search screen, perform search and track the fake result. should go back to tracker overview

        composeRule.onNodeWithTag("search_textfield").performTextInput("banana")
        composeRule.onNodeWithContentDescription("Search...").performClick()

        composeRule.onRoot().printToLog("COMPOSE TREE")

        composeRule.onNodeWithText("Carbs").performClick() // expands the food item
        composeRule.onNodeWithContentDescription("Amount").performTextInput(addedAmount.toStr())
        composeRule.onNodeWithContentDescription("Track").performClick()

        assertThat(navController.currentDestination?.route?.startsWith(Route.TRACKER_OVERVIEW)).isTrue()

        //==========================================================================================
        // At tracker overview, verify if tracked food is displayed with info

        composeRule.onAllNodesWithText("banana").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedCarbs.toStr()).onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedProtein.toStr()).onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedFat.toStr()).onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedCalories.toStr()).onFirst().assertIsDisplayed()
    }
}