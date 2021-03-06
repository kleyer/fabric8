/**
 *  Copyright 2005-2014 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.process.spring.boot.registry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.fabric8.process.spring.boot.registry.ZooKeeperProcessRegistries.ZK_CONNECTION_TIMEOUT_MS_PROPERTY;
import static java.lang.System.setProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProcessRegistryAutoConfigurationTest.class)
@EnableAutoConfiguration
public class ProcessRegistryAutoConfigurationTest extends Assert {

    @Autowired
    ProcessRegistry registry;

    @Value("${foo}")
    String fooValue;

    @BeforeClass
    public static void beforeClass() {
        setProperty(ZK_CONNECTION_TIMEOUT_MS_PROPERTY, 1 + "");
    }

    @AfterClass
    public static void afterClass() {
        setProperty(ZK_CONNECTION_TIMEOUT_MS_PROPERTY, 5000 + "");
    }

    @Test
    public void shouldResolveFromSystemProperties() {
        assertEquals("bar", fooValue);
    }

    @Test
    public void propertyResolverShouldDelegateToProcessRegistry() {
        try {
            setProperty("baz", "qux");
            assertEquals("qux", registry.readProperty("baz"));
        } finally {
            System.clearProperty("baz");
        }
    }

}