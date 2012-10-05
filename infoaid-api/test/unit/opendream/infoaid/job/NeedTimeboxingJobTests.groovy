package opendream.infoaid.job

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Post
import opendream.infoaid.domain.Need
import opendream.infoaid.domain.User
import opendream.infoaid.service.TimeboxingService

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Mock([Page, Item, Need, Post, User])
class NeedTimeboxingJobTests {
    def needTimeboxingJob
    def grailsApplication

    void setUp() {
        // Setup logic here
        needTimeboxingJob = new NeedTimeboxingJob()
        needTimeboxingJob.timeboxingService = new TimeboxingService()
        
        Page.metaClass.generateSlug = {-> delegate.slug += "-slug"}
        Page.metaClass.isDirty = {name -> false}
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}
        def date = new Date()

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

    void tearDown() {
        // Tear down logic here
    }

    void testDisableExpiredNeed() {
        needTimeboxingJob.execute()
        assert 4 == Need.findAllByStatus(Post.Status.ACTIVE).size()
    }
}
