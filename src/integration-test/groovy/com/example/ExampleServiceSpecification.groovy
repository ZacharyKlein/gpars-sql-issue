package com.example

import example.ExampleDomain
import example.ExampleService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class ExampleServiceSpecification extends Specification {

    @Autowired
    ExampleService exampleService

    void 'test example service'() {

        given:
        ExampleDomain exampleDomain = new ExampleDomain(name: "Test")
        exampleDomain.save(flush: true)

        when:
        exampleService.execute()

        then:
        ExampleDomain.count() == 0
    }

}
