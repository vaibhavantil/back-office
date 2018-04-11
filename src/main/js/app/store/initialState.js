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
        member: null
    },
    members: {
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
        memberClaims: []
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
        data: null,
        error: []
    },
    notifications: [],
    memberInsurance: {
        requesting: false,
        list: []
    }
};
