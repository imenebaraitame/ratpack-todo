import groovy.transform.CompileStatic
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.func.Function
import ratpack.handling.ByMethodSpec
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.handling.InjectionHandler
import ratpack.http.Response
import ratpack.jackson.Jackson

@CompileStatic
class TodoBaseHandler extends InjectionHandler {

    void handle(Context ctx, final TodoRepository repository, String baseUrl) throws Exception {
        Response response = ctx.response
        ctx.byMethod({ ByMethodSpec method -> method
            .options({
                response.headers.set('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE')
                response.send()
            } as Handler).get({
                repository.all
                        .map { todos -> todos.collect { TodoModel todo -> todo.baseUrl(baseUrl) } as List<TodoModel> }
                        .map(Jackson.&json)
                        .then(ctx.&render)
            } as Handler)
                    .post( {
                        Promise<TodoModel> todo = ctx.parse(Jackson.fromJson(TodoModel))
                        todo
                            .flatMap(repository.&add as Function<TodoModel, Promise<TodoModel>>)
                            .map { TodoModel t -> t.baseUrl(baseUrl) }
                            .map(Jackson.&json)
                            .then(ctx.&render)
                    } as Handler)
                    .delete( { repository.deleteAll().then(response.&send) } as Handler)
        } as Action<ByMethodSpec>)
    }


}