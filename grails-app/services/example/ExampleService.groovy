package example

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovyx.gpars.GParsPool

import javax.sql.DataSource

class ExampleService {

    DataSource dataSource

    void execute() {
        Integer threads = Runtime.runtime.availableProcessors()
        Integer times = 0

        List<GroovyRowResult> result = getResults()
        while (result.size() > 0 || times < 25) {

            logAllRows("Outside eachParallel")

            GParsPool.withPool(threads) {
                result.eachParallel { GroovyRowResult sre ->
                    logAllRows("Inside eachParallel")
                    cleanResult(sre)
                }
            }

            times++

            result = getResults()
        }

    }

    void cleanResult(GroovyRowResult sre) {
        log.info "Cleaning result..."

        Sql sql = new Sql(dataSource)
        GroovyRowResult row = sql.firstRow('select id from example_domain where name = ?', [sre.name])

        if (row) {
            log.warn "Deleting ${row?.id}..."
            sql.execute('delete from example_domain where id = ?', [row?.id])
        }


    }


    List<GroovyRowResult> getResults() {
        Sql sql = new Sql(dataSource)

        sql.rows('select * from example_domain')
    }


    void logAllRows(String message) {
        println "${message}: ${new Sql(dataSource).rows('select * from example_domain')}"
    }
}
