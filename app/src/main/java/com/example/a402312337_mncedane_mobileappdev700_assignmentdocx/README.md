# Smart Pantry Manager

An Android app (Java) that helps reduce food waste. The user records the
ingredients they have at home, and the app suggests only the recipes they can
cook right now - every ingredient must already be in the pantry, in at least
the required quantity.

Built for Mobile App Development 700 - Practical Assignment.
Author: [Thabo Mncedane] ([402312337])

## Features
- Add, edit and delete pantry ingredients (name, quantity, unit, optional expiry date)
- Pantry list (RecyclerView) bound to the database
- 20 pre-loaded recipes, seeded on first run
- Suggested Recipes screen using a strict-matching rule: a recipe is only
  suggested if every ingredient is available in enough quantity
- Matching copes with plural/case differences ("Tomatoes" = "tomato") and
  unit differences (1 kg covers 250 g)
- Optional "Almost There" list: recipes missing exactly one ingredient
  (kept separate from the strict list)
- Recipe detail screen with a tick/cross per ingredient and the method
- Settings: expiring-soon alerts and default unit (stored in SharedPreferences)
- Input validation on the ingredient form
- No maps, GPS or location features (out of scope per the brief)

## Database choice: SQLite
I used SQLite through `SQLiteOpenHelper`. It runs locally on the device, needs
no account or internet connection, persists data between sessions, and suits
personal pantry data. It is also the approach covered in the module's persistent
data chapter. [Add one or two sentences in your own words.]

Tables: `pantry`, `recipe`, `recipe_ingredient`.

## Screens
1. Pantry List
2. Add / Edit Ingredient
3. Suggested Recipes
4. Recipe Detail
5. Settings

## Setup and run
1. Install Android Studio (version [x]) and clone the repository:
   `git clone [your-repo-url]`
2. Open the project folder in Android Studio and let Gradle sync finish.
3. Create an emulator (e.g. Pixel 6, API [x]) or connect a physical device with
   USB debugging on.
4. Press **Run**. The recipes are seeded the first time the app starts.
5. Run the unit tests for the matching logic with `./gradlew test`.

If you change the database structure, uninstall the app first (or raise
`DATABASE_VERSION`) so the database is recreated.

## Project structure
- `PantryListActivity`, `AddEditPantryActivity`, `SuggestedRecipesActivity`,
  `RecipeDetailActivity`, `SettingsActivity`
- `PantryAdapter`, `RecipeAdapter` (RecyclerView adapters)
- `PantryDBHelper`, `PantryDataSource` (SQLite)
- `RecipeMatcher`, `IngredientNormalizer`, `UnitConverter` (matching logic)
- `SettingsManager`, `ExpiryHelper`, `NavigationHelper`

## Known limitations
- Units from different families cannot be compared (e.g. "1 carton" vs "200 ml").
- [Add your decision on expired items.]