# Android boilerplate sample  app

The purpose of this repository is to provide a starting point for an app,
containing most common configurations a project would need.

### Project structure
![Project module dependencies structure](structure/project-dependency-graph.svg)

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

### Todos:
1. Split in modules, problems:
   - Ktor network client configuration.
2. Custom material theme object, sizes object, font styles object.
3. Translations (localisations).
4. Compose 1.7.
5. Multiple news sources filter selection.
6. Multiple languages in filter selection.
7. Hide the API key in the Pipeline settings, so far I need to create two because it has a limit of request per day :).
8. Pipelines should generate debug and release version should pass R8 and upload to the store.
