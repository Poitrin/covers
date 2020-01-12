# Deployment

## Dokku

If you deploy this app via Dokku, you'll need to add some environment variables (via `dokku config:set`)

- `GRADLE_TASK=assemble`
  - By default, the Gradle buildpack tries to execute a Gradle task called `stage`, and fails if it can't find it.
  - The [Heroku documentation](https://devcenter.heroku.com/articles/deploying-gradle-apps-on-heroku#using-webapp-runner-to-deploy-war-files) suggests adding this `stage` task, whereas the [Grails documentation](https://docs.grails.org/latest/guide/deployment.html) suggests executing the Gradle task `assemble`.
  - I tried the “Heroku way” first, but encountered serious issues (see below) which I could not solve, so I went the “Grails way”.
  Therefore, we need to set the `GRADLE_TASK` environment variable.
- `GRADLE_OPTS=-Dfile.encoding=UTF-8`
  - `.properties` files with `UTF-8` characters work in Java 9+, but we still need to tell Gradle that the files are encoding in `UTF-8` ([source](https://stackoverflow.com/questions/21267234/show-utf-8-text-properly-in-gradle)).
- `JDBC_DATABASE_URL="jdbc:postgres://<host>:5432/<database>?user=<username>&password=<password>"`
  - Even though Dokku automatically sets the `DATABASE_URL` env variable, it's not in the right format (`postgres://<username>:<password>@<host>:5432/<database>`).
- If you get the error _Could not create service of type ScriptPluginFactory using BuildScopeServices.createScriptPluginFactory()_ during the build, try removing the folder `/home/dokku/covers/cache/`.
  - Since I moved from Grails 3 / Gradle 3.5 to Grails 4 / Gradle 5, it's possible that the old files interfered with the new ones.
  - In my case, it was _not_ a permissions problem [as described here](https://stackoverflow.com/questions/30840526/gradle-could-not-create-service-of-type-initscripthandler-using-buildscopeservic), so setting `GRADLE_USER_HOME` was useless.

### Webapp-Runner issues
- https://stackoverflow.com/questions/54702195/heroku-wardeploy-java-util-concurrent-executionexception
- https://github.com/heroku/webapp-runner/issues/133
- https://github.com/grails/grails-core/issues/11289