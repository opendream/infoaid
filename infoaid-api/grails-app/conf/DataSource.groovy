dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            /*dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url="jdbc:mysql://localhost:3306/infoaid?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8"
            pooled = true
            driverClassName = "com.mysql.jdbc.Driver"
            dialect = "org.hibernate.dialect.MySQLInnoDBDialect"
            username = "root"
            password = "password"*/ 
            driverClassName = "org.postgresql.Driver"
            dialect = net.sf.hibernate.dialect.PostgreSQLDialect
            dbCreate = "update"
            url="jdbc:postgresql://localhost:5432/infoaid"
            username = "infoaid"
            password = "infoaid"           
        }
        grails {
            mongo {
                host = "127.0.0.1"
                port = 27017
                username = "infoaid"
                password = "openpubyesroti!"
                databaseName = "infoaid"
            }
        }       
    }
    test {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
        grails {
            mongo {
                host = "localhost"
                port = 27017
                username = "infoaid"
                password = "openpubyesroti!"
                databaseName = "infoaid-test"
            }
        }        
    }
    production {
        dataSource {
            dbCreate = "update"
            driverClassName = "org.postgresql.Driver"
            dialect = net.sf.hibernate.dialect.PostgreSQLDialect
            url="jdbc:postgresql://localhost:5432/infoaid"
            username = "infoaid"
            password = "infoaid"
        }
        grails {
        mongo {
            host = "localhost"
            port = 27017
            username = "infoaid"
            password = "openpubyesroti!"
            databaseName = "infoaid"
        }
    }
    }     
}
