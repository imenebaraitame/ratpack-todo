import com.google.common.reflect.TypeToken
import groovy.util.logging.Slf4j
import jooq.tables.Todo
import jooq.tables.records.TodoRecord
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import ratpack.func.Action
import ratpack.handling.ByMethodSpec
import ratpack.handling.Context
import ratpack.handling.InjectionHandler
import ratpack.http.Response
import ratpack.exec.Blocking
import ratpack.jackson.Jackson
import ratpack.groovy.template.TextTemplateModule
import static ratpack.groovy.Groovy.groovyTemplate

import javax.sql.DataSource

/**
 * Created by Master on 25/07/2018.
 */
@Slf4j
class TodoHandler extends InjectionHandler{

    void handle(Context ctx, DataSource ds){
        def slf4jLogger = log
        DSLContext create = DSL.using(ds, SQLDialect.H2)
        Response response = ctx.response
        ctx.byMethod ({ ByMethodSpec method -> method
            .options {
                response.headers.set('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE, PUT')
                response.send()
            }
            .get {
                def selectAll = create.select().from(Todo.TODO)
                def todos = Blocking.get{ selectAll.fetchMaps() }
                todos.then {
                    ctx.render Jackson.json(it)
                }
//                ctx.render(groovyTemplate(title:"My TodoApp2", "welcome.html"))
            }
//            .get('/home'){
//                render groovyMarkupTemplte("index.gtpl", title:" Welcome to TodoManager")
//            }
//            .get('/gr8conf'){
//                ctx.render 'Hello GR8Conf peolple'
//            }
//            .get(':conf'){
//                ctx.render "Hello ${pathTokens.get('conf')}"
//            }
            .post {
                ctx.parse(new TypeToken<Map<String, Object>>(){}).map { map ->
                    slf4jLogger.info("Map: ${map}".toString())
                    return create.newRecord(Todo.TODO, map)
                }.blockingOp { TodoRecord record ->
                    record.store()
                }.blockingOp{ TodoRecord record ->
                    record.refresh()
                }.then{ todo ->
                    ctx.render Jackson.json(todo)
                }

            }
        } as Action<ByMethodSpec>)

    }
}
