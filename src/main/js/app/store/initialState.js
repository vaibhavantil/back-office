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
        token: null,
        user: null
    },
    poll: {
        polling: false
    },
    messages: {
        list: [],
        activeConnection: null,
        error: null,
        user: null
    },
    users: {
        list: [],
        requesting: false,
        successful: false,
        errors: [],
        filter: ''
    },
    dashboard: {
        data: null,
        error: null
    },
    claims: {
        list: [],
        requesting: false,
        successful: false,
        errors: [],
        types: []
    },
    claimDetails: {
        data: null,
        notes: [],
        payments: []
    },
    questions: {
        list: [],
        requesting: false,
        successful: false,
        errors: []
    }
};
