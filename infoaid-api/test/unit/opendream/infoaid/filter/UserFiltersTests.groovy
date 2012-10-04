package opendream.infoaid.filter

import opendream.infoaid.domain.Page
import opendream.infoaid.domain.User
import opendream.infoaid.domain.PageUser
import opendream.infoaid.controller.PageController
import opendream.infoaid.service.PageService

import grails.test.mixin.*

@TestFor(PageController)
@Mock([UserFilters, Page, User, PageUser])
class UserFiltersTests {

    void testUserParams() {
        Page.metaClass.generateSlug = {-> delegate.slug = delegate.name+"-slug"}
        Page.metaClass.isDirty = {name -> false}
        User.metaClass.encodePassword = { -> 'password'}
        User.metaClass.isDirty = {password -> false}

        def date = new Date()
        def user1 = new User(username: "nut", password: "nut", firstname: 'firstname', lastname: 'lastname', dateCreated: date, lastUpdated: date, picOriginal: 'picOri', picLarge: 'picLar', picSmall: 'picSma').save()
        def page1 = new Page(name: "page", lat: "111", lng: "222", dateCreated: date, lastUpdated: date, 
            about: 'this is page 1', picOriginal: 'picOri',picSmall: 'picSma', household: 1, population: 11).save()
        new PageUser(page: page1, user: user1, relation: PageUser.Relation.OWNER, conversation: 1).save()
        UserFilters.metaClass.springSecurityService = [principal:[id:user1.id]]
        controller.pageService = new PageService()
        
        withFilters(action:"isOwner") {
            params.slug = 'page-slug'
            controller.isOwner()        
        }
        assert response.json['isOwner'] == true
    }
}
