export default {
    baseUrl: '/api/',
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
            method: 'get',
            url: 'settings/me'
        },
        logout: {
            method: 'post',
            url: 'logout'
        }
    },
    members: {
        get: {
            method: 'get',
            url: 'member'
        },
        search: {
            method: 'get',
            url: 'member/search'
        },
        findOne: {
            method: 'get',
            url: 'member'
        }
    },
    claims: {
        getList: {
            url: 'claims',
            method: 'get'
        },
        getListByMemberId: {
            url: 'claims/user',
            method: 'get'
        },
        types: {
            url: 'claims/types',
            method: 'get'
        },
        update: {
            url: 'claims',
            method: 'post'
        },
        updateDetails: {
            url: 'claims',
            method: 'put'
        },
        details: {
            get: {
                url: 'claims',
                method: 'get'
            },
            create: {
                url: 'claims',
                method: 'put'
            },
            remove: {
                url: 'claims',
                method: 'delete'
            }
        }
    },
    questions: {
        get: {
            url: 'questions',
            method: 'get'
        },
        answered: {
            url: 'questions/answered',
            method: 'get'
        },
        notAnsewred: {
            url: 'questions/not-answered',
            method: 'get'
        },
        sendAnswer: {
            url: 'questions/answer',
            method: 'post'
        }
    },
    insurance: {
        get: {
            url: 'member/insurance',
            method: 'get'
        },
        setDate: {
            url: 'member/insurance',
            method: 'post'
        }
    },
    insMembers: {
        get: {
            method: 'get',
            url: 'member'
        },
        search: {
            method: 'get',
            url: 'member/search'
        }
    },
    ws: {
        // eslint-disable-next-line
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
