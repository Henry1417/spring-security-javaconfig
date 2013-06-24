/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.config.annotation.authentication

import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.BaseSpringSpec
import org.springframework.security.config.annotation.ObjectPostProcessor

/**
 *
 * @author Rob Winch
 *
 */
class AuthenticationManagerBuilderTests extends BaseSpringSpec {
    def "add(AuthenticationProvider) does not perform registration"() {
        setup:
            ObjectPostProcessor opp = Mock()
            AuthenticationProvider provider = Mock()
            AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder().builderPostProcessor(opp)
        when: "Adding an AuthenticationProvider"
            builder.add(provider)
            builder.build()
        then: "AuthenticationProvider is not passed into LifecycleManager (it should be managed externally)"
            0 * opp._(_ as AuthenticationProvider)
    }

    // https://github.com/SpringSource/spring-security-javaconfig/issues/132
    def "#132 Custom AuthenticationEventPublisher with Web registerAuthentication"() {
        setup:
            AuthenticationEventPublisher aep = Mock()
        when:
            AuthenticationManager am = new AuthenticationManagerBuilder()
                .authenticationEventPublisher(aep)
                .inMemoryAuthentication()
                    .and()
                .build()
        then:
            am.eventPublisher == aep
    }
}
