package opendream.infoaid.service

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.User

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(TimeboxingService)
@Mock([Page, Item, Need, Post, User])
class TimeboxingServiceTests {
    def date

    @Before
    void setup() {
        Page.metaClass.generateSlug = {-> delegate.slug += "-slug"}
        Page.metaClass.isDirty = {name -> false}
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}
        date = new Date()

        def nut = new User(username: "nut", password: "nut", firstname: 'firstname', 
            lastname: 'lastname', dateCreated: date, lastUpdated: date, 
            picOriginal: 'picOri', picLarge: 'picLar', picSmall: 'picSma').save()

        def page = new Page(name: "opendream", lat: "1234", lng: "5678", 
            dateCreated: date, lastUpdated: date, about: 'this is page 1', 
            picOriginal: 'picOri', picSmall: 'picSma', picLarge: 'picLar')

        def water = new Item(name: 'water', status: Item.Status.ACTIVE).save(flush:true)

        5.times {
            def need = new Need(item: water, lastActived: new Date(), 
                createdBy: nut, updatedBy: nut, dateCreated:date, 
                expiredDate: date +(it), quantity: 2)
            page.addToPosts(need)
        }

        page.save(failOnError:true, flush:true)
    }

    void testDisableExpiredNeed() {
        // day 1
        service.disableExpiredNeed()
        assert 4 == Need.findAllByStatus(Post.Status.ACTIVE).size()
        // day 2
        service.disableExpiredNeed(date+1)
        assert 3 == Need.findAllByStatus(Post.Status.ACTIVE).size()
        // day 3
        service.disableExpiredNeed(date+2)
        assert 2 == Need.findAllByStatus(Post.Status.ACTIVE).size()
        // day 4
        service.disableExpiredNeed(date+3)
        assert 1 == Need.findAllByStatus(Post.Status.ACTIVE).size()
        // day 5
        service.disableExpiredNeed(date+4)
        assert 0 == Need.findAllByStatus(Post.Status.ACTIVE).size()
    }
}
