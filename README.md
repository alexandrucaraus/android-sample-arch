# germanautolabs
Employment test task

### The directory structure is the following
[main] - contain the main entry point, di setup
[models] - represent domain objects
[screens] - contain composable navigation and screen per directory
[infra] - contains some common infrastructure
[data] - contains interfaces and implementations to external systems
[use case] - contains business behaviour

### Additionally there are:
[ktlint] - for code quality check
[integration tests] - for simple navigation testing
[unit test] - for behaviour test, not enough though :(
[github workflows] - to run the pipeline on each commit to main (kept it simple)
[test coverage report] - each pipeline run displays the test coverage

### Paparazzi snapshot testing

1. https://cashapp.github.io/paparazzi/
   Needs git lfs for initial setup of the repo.

### What I would do more if I had more time:
1. Custom material theme object, sizes object, font styles object.
2. Translations (localisations).
3. Compose 1.7.
4. Multiple news sources filter selection.
5. Multiple languages in filter selection.
6. Refactor the ArticleFiltersStateHolder
7. Refactor the ArticlesListViewModel, do better separation of concerns, and add tests :).
8. Hide the API key in the Pipeline settings, so far I need to create two because it has a limit of request per day :).
9. A few bugs.

TODO fixes:

1. Rename package name.
2. Split into features.
