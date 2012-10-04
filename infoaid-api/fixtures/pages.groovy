import opendream.infoaid.domain.*

fixture {
    build {
        bangkok(Page, 
                name: 'Bangkok',
                lat: '13.7516899108887',
                lng: '100.491882324219',
                household: 123,
                population: 456
            )

        chiangmai(Page, 
                name: 'Chiang Mai',
                lat: '13.7445802688599',
                lng: '100.498977661133',
                household: 124,
                population: 556
            )

        noom(User,
                username: "noom",
                password: "noomqwer",
                firstname: 'Siriwat',
                lastname: 'Uamngamsup',
                dateCreated: new Date(),
                lastUpdated: new Date(),
                picSmall: 'picSmall.png',
                picOriginal: 'picOriginal.png',
                picLarge: 'picLarge.png',
        )

    }
}