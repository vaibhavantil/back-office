export default {
    login: {
        requesting: false
    },
    assets: {
        list: [],
        requesting: false
    },
    client: {},
    poll: {
        polling: false
    },
    messages: {
        list: [],
        activeConnection: null,
        user: null
    },
    users: {
        list: [],
        requesting: false,
        filter: ''
    },
    dashboard: {
        data: null
    },
    claims: {
        list: [],
        requesting: false,
        types: [],
        userClaims: []
    },
    claimDetails: {
        data: null,
        notes: [],
        payments: []
    },
    questions: {
        list: {
            answered: [],
            notAnswered: []
        },
        requesting: false
    },
    insurance: {
        requesting: false,
        data: null
    },
    notifications: []
};
