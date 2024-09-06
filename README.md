# Android boilerplate sample  app

The purpose of this repository is to provide a starting point for an app,
containing most common configurations a project would need.

### Project module structure
![Project module dependencies structure](structure/project-dependency-graph.svg)

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

1. Pipeline add api key to CI/CD variables
2. Pipeline generate debug and release with R8.
3. Pipeline publish app to some store.
4. Pipeline rename github repository.

5. Custom material theme object, sizes object, font styles object.

6. Translations (localisations).

7. Multiple news sources filter selection.
8. Multiple languages in filter selection.

9. Update readme.
