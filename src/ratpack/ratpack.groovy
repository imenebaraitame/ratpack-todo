import ratpack.groovy.Groovy
import ratpack.hikari.HikariModule
import ratpack.groovy.template.TextTemplateModule
import static ratpack.groovy.Groovy.groovyTemplate
//import ratpack.groovy.template.MarkupTemplateModule
//import static ratpack.groovy.Groovy.groovyMarkupTemplate

Groovy.ratpack {

    // bring functionalities to ratpack
    bindings {
        module(HikariModule){ config ->
            config.dataSourceClassName = 'org.h2.jdbcx.JdbcDataSource'
            config.addDataSourceProperty('URL', "jdbc:h2:mem:todo;INIT=RUNSCRIPT FROM 'classpath:/init.sql'")
        }
        module(TextTemplateModule)
//        module(MarkupTemplateModule)

    }
    handlers {
        insert(new TodoChain())
        /*get {
//            render "index"
//            render file("public/index.html")
//            render(groovyMarkupTemplate([title:"My Todo App", welcome:"Welcome to TodoApp"], "index.gtpl"))
            render groovyTemplate(title:"My TodoApp2", "welcome.html")
        }*/
        files {
            dir "public"
        }
    }

}
