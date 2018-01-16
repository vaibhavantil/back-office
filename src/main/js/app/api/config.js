export default {
    baseUrl: '/api/',
    tokenHeaderName: 'Authorization',
    asset: {
        get: {
            method: 'get',
            url: 'assets'
        },
        update: {
            method: 'post',
            url: 'assets'
        }
    },
    login: {
        login: {
            method: 'post',
            url: 'login'
        },
        logout: {
            method: 'post',
            url: 'logout'
        }
    },
    chats: {
        get: {
            method: 'get',
            url: 'user'
        },
        search: {
            method: 'get',
            url: 'user/search'
        }
    },
    ws: {
        // eslint-disable-next-line
        endpoint: DEV ? '/sockjs-node': '/chat',
        messages: '/topic/messages/',
        history: '/app/messages/history/'
    }
};
