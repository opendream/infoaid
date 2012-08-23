package opendream.infoaid.service

import opendream.infoaid.domain.Page

import static org.junit.Assert.*
import org.junit.*

class FixturesIntegrationTests {
    def fixtureLoader
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testFixtures() {
        fixtureLoader.load("pages")
        assert Page.count() == 2
    }
}
