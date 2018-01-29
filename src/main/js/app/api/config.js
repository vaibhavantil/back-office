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
    users: {
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
        /* eslint-disable */
        endpoint: DEV ? 'http://localhost:8080/chat' : '/chat',
        messages: '/topic/messages/',
        history: '/app/messages/history/',
        send: '/app/messages/send/',
        dashboardSub: '/user/',
        cleanupDashboard: '/app/updates/clear/',
        dashboardUpdates: '/app/updates/',
        cleanupMessages: '/app/clear/',
        newMessagesSub: '/app/updates/',
        messagesUpdates: '/app/updates/'
    }
};
