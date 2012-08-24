package opendream.infoaid.service

import static org.junit.Assert.*
import org.junit.*

import opendream.infoaid.domain.Page

class SlugGeneratorTests {

    @Before
    void setUp() {
        def date = new Date()
        def page = new Page(name: "ตำบล เวียง", lat: "page1", lng: "page1", 
                            dateCreated: date, lastUpdated: date)
        page.save()
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testPageSlug() {
        def date = new Date()
        assert 1 == Page.count()
        def page = Page.findByName('ตำบล เวียง')
        assert 'ตำบล-เวียง' == page.slug

        20.times {
            new Page(name: "ตำบล เวียง $it", lat: "page1", lng: "page1", 
                            dateCreated: date, lastUpdated: date).save()
        }

        assert 21 == Page.count()

        page = Page.findByName("ตำบล เวียง 19")
        assert page.slug == "ตำบล-เวียง-19"

    }
}
