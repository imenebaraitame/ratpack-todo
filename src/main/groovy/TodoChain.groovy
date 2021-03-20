import ratpack.groovy.handling.GroovyChainAction

/**
 * Created by Master on 25/07/2018.
 */
class TodoChain extends GroovyChainAction {
    @Override
    void execute() throws Exception {
        path(new TodoHandler())
    }
}
