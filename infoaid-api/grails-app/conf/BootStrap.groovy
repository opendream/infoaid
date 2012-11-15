import opendream.infoaid.domain.User
import opendream.infoaid.domain.Role
import opendream.infoaid.domain.UserRole
import opendream.infoaid.domain.Item
import opendream.infoaid.domain.Requestmap
import opendream.infoaid.domain.Expertise
import opendream.infoaid.domain.Cause

class BootStrap {

    def init = { servletContext ->

        development {
            if(!User.count()) {
                println "insert user"
                def user = new User(username: 'admin', password: 'password', 
                firstname: 'thawatchai', lastname: 'jong', dateCreated: new Date(), 
                lastUpdated: new Date()).save(flush:true)
                def role = new Role(authority:'ROLE_ADMIN').save(flush:true)
                UserRole.create(user, role)
            }

            initItem()
            initExpertise()
            initCause()
            initRequestMap()
        }

        production {
            initItem()
            initExpertise()
            initCause()
            initRequestMap()
        }

    }

    def initRequestMap = {
        // - user/availableExpertises
        def conf = Requestmap.findByUrl('user/availableExpertises') ?: new Requestmap()
        conf.url = '/user/availableExpertises'
        conf.configAttribute = 'IS_AUTHENTICATED_ANONYMOUSLY'
        conf.save()

        // - user/availableCauses
        conf = Requestmap.findByUrl('user/availableCauses') ?: new Requestmap()
        conf.url = '/user/availableCauses'
        conf.configAttribute = 'IS_AUTHENTICATED_ANONYMOUSLY'
        conf.save()
    }

    def initItem = {
        def item_list = Item.list()
        item_list.each {
            it.status = Item.Status.INACTIVE
            it.save()
        }

        def focus_item_list = [
            'Tent', 'Clothes', 'Man', 'Gas', 'Fuel', 'Electricity', 'Water',
            'Boat', 'Food', 'Medicine', 'Water Closet', 'Water Pump']

        focus_item_list.each { item_name ->
            def item = Item.createCriteria().get {
                ilike('name', item_name)
            }

            if (! item) {
                item = new Item()
            }

            item.name = item_name
            item.status = Item.Status.ACTIVE

            item.save()
        }
    }

    def initExpertise = {
        def expertise_list = [
            [name: "Accounting", description: "Accounting (Accountant, Controller, CPA)"],
            [name: "Advertising", description: "Advertising (Brand Manager, Creative Director, Advertising Executive)"],
            [name: "Brand Strategy", description: "Brand Strategy (Brand Strategist, Creative Director, Marketing Director/Manager, Product Manager)"],
            [name: "Business Strategy", description: "Business Strategy (Senior Business Analyst, Strategic Planning Analyst, Management Consultant, Business Development Director)"],
            [name: "Communications", description: "Communications (Public Relations Specialist/Manager, Corporate Communications Director)"],
            [name: "Copywriting", description: "Copywriting (Copywriter, Writer, Communications Manager)"],
            [name: "Design", description: "Design (Creative Director, Graphic Designer/Artist, Desktop Publisher, Visual Web Designer, UX / UI Designer, Web Designer)"],
            [name: "Entrepreneurship", description: "Entrepreneurship (Entrepreneur, CEO, Company Founder)"],
            [name: "Event Planning", description: "Event Planning (Event Planner)"],
            [name: "Finance", description: "Finance (Financial/Investment Analyst, Management Consultant, Investment Banker, Private Equity/Hedge Fund Analyst)"],
            [name: "Fundraising", description: "Fundraising (Development Director, Grantwriter, Director/Manager of Business Development, Crowdfunding/Social Media Specialist)"],
            [name: "Human Resources", description: "Human Resources (Human Resources Manager, Recruiter, Compensation Analyst)"],
            [name: "Legal", description: "Legal (Attorney, General Counsel, Solicitor, Paralegal)"],
            [name: "Marketing", description: "Marketing (Marketing Manager/Director, Marketing Analyst)"],
            [name: "Multimedia", description: "Multimedia (Video Production Manager, Director, Videographer, Multimedia Designer/Specialist, Multimedia Illustrator)"],
            [name: "Online Marketing", description: "Online Marketing (Digital Marketing Specialist, Social Marketing Manager, Online Marketing Analyst, SEO/SEM Specialist)"],
            [name: "Photography", description: "Photography (Photographer)"],
            [name: "Public Relations", description: "Public Relations (Public Relations Specialist/Manager, Communications Manager, Event Planner)"],
            [name: "Sales & Business Development", description: "Sales & Business Development (Business Development Director/Associate, Sales Manager/Executive)"],
            [name: "Social Media", description: "Social Media (Social Media Specialist, Digital Community Manager, Social Marketing Specialist/Manager, Online Marketing Analyst/Manager)"],
            [name: "Strategic Communications", description: "Strategic Communications (Public Relations Specialist, Corporate Communications Manager)"],
            [name: "Strategic Marketing", description: "Strategic Marketing (VP of Marketing, Brand Strategist, Product Manager)"],
            [name: "Strategy", description: "Strategy (Senior Business Analyst, Stategic Planning Analyst, Management Consultant, Director of Strategy)"],
            [name: "Technology", description: "Technology (Software Engineer, IT Architect, Systems Administrator/Engineer, Database Analyst, Web Designer, Blogger)"],
            [name: "Writing", description: "Writing (Creative Writer, Copywriter, Blogger)"]
        ]

        expertise_list.each { item ->
            def expertise = Expertise.createCriteria().get {
                eq('name', item.name)
            }

            if (! expertise) {
                expertise = new Expertise()
            }

            expertise.name = item.name
            expertise.description = item.description

            expertise.save()
        }
    }

    def initCause = {
        def cause_list = [
            [name: "Animal Rights"],
            [name: "Food"],
            [name: "Media and Public Debate"],
            [name: "Arts and Culture"],
            [name: "Health"],
            [name: "Microfinance"],
            [name: "Children"],
            [name: "Housing & Homelessness"],
            [name: "Poverty Alleviation"],
            [name: "Community and Service"],
            [name: "Human Rights"],
            [name: "Religion"],
            [name: "Democracy and Politics"],
            [name: "Humanitarian Relief"],
            [name: "Science & Technology"],
            [name: "Education"],
            [name: "I Just Want To Do Good"],
            [name: "Senior Citizens Issues"],
            [name: "Environment"],
            [name: "International Affairs"],
            [name: "Women's Issues"],
        ]

        cause_list.each { item ->
            def cause = Cause.createCriteria().get {
                eq('name', item.name)
            }

            if (! cause) {
                cause = new Cause()
            }

            cause.name = item.name
            cause.description = item.description

            cause.save()
        }
    }

    def destroy = {
    }
}
