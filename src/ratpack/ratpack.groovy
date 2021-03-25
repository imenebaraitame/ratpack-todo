import static ratpack.groovy.Groovy.ratpack
import ratpack.hikari.HikariModule

ratpack {

  bindings {
    module(HikariModule) { config ->
      config.dataSourceClassName = 'org.h2.jdbcx.JdbcDataSource'
      config.addDataSourceProperty('URL', "jdbc:h2:mem:tood;INIT=RUNSCRIPT FROM 'classpath:/init.sql'")
    }
    module(TodoModule)
    bindInstance(new CORSHandler())
    bindInstance(new TodoBaseHandler())
    bindInstance(new TodoHandler())
    bindInstance(new TodoChain())
  }

  handlers {
    all(CORSHandler)
    insert(TodoChain)

    /*path { TodoRepository repository ->
      byMethod {
        // I don't think this is needed here since we'"
        options {
          response.headers.set('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE')
          response.send()
        }
        // curl -X GET http://localhost:5050/
        get {
          // or you can provide your Handler closure with types from the Registry and Ratpack will set them for you
          repository.all
              .map(Jackson.&json)
              .then(context.&render)
        }

        // curl -X POST -H 'Content-type: application/json' --data '{"title":"New Task"}' http://localhost:5050/
        post {
        // the context#parse() returns a Promise
         Promise<TodoModel> todo = parse(Jackson.fromJson(TodoModel))
          todo
           .flatMap(repository.&add)
           .map(Jackson.&json)
           .then ( context.&render )
        }

        // curl -X DELETE http://localhost:5050
        delete {
          repository.deleteAll().then( response.&send )
        }

      } // byMethod
    } // path()
    */

  }
}
