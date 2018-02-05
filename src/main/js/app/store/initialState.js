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
        errors: []
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
    }
};
