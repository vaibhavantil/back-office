export default {
    login: {
        requesting: false,
        successful: false,
        errors: []
    },
    assets: {
        list: [],
        requesting: false,
        successful: false,
        errors: []
    },
    client: {
        id: null,
        token: null
    },
    poll: {
        polling: false
    },
    messages: {
        messages: [
            {
                id: 1,
                content:
                    'Maxime doloribus ratione exercitationem voluptatem debitis tempore similique aperiam nisi cum itaque?',
                author: 21
            },
            {
                id: 2,
                content: 'test message 2',
                author: 12
            },
            {
                id: 3,
                content: 'test message 4',
                author: 21
            },
            {
                id: 4,
                content:
                    ' Sequi cupiditate saepe in quisquam aliquam aliquid dolorum sit veniam?',
                author: 21
            },
            {
                id: 5,
                content:
                    ' Maxime doloribus ratione exercitationem voluptatem debitis tempore similique aperiam nisi cum itaque?',
                author: 12
            },
            {
                id: 6,
                content: 'test message 2',
                author: 12
            }
        ],
        users: [
            {
                id: 12,
                name: 'Test User',
                newMessage: false
            },
            {
                id: 2,
                name: 'Test User 2',
                newMessage: false
            }
        ]
    }
};
