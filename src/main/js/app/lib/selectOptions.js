export const assetStates = [
    { key: 0, text: 'CREATED', value: 'CREATED' },
    { key: 1, text: 'PENDING', value: 'PENDING' },
    { key: 2, text: 'WAITING_FOR_PAYMENT', value: 'WAITING_FOR_PAYMENT' },
    { key: 3, text: 'NOT_COVERED', value: 'NOT_COVERED' },
    { key: 4, text: 'COVERED', value: 'COVERED' },
    { key: 5, text: 'DELETED', value: 'DELETED' },
    { key: 6, text: 'ALL', value: 'ALL' }
];

export const messageTypes = [
    { key: 1, text: 'Text', value: 'text' },
    { key: 2, text: 'Number', value: 'number' },
    { key: 3, text: 'Single select', value: 'single_select' },
    { key: 4, text: 'Multiple select', value: 'multiple_select' },
    { key: 5, text: 'Date', value: 'date_picker' },
    { key: 6, text: 'Audio', value: 'audio' },
    { key: 7, text: 'Photo', value: 'photo_upload' },
    { key: 8, text: 'Video', value: 'video' },
    { key: 9, text: 'Hero', value: 'hero' },
    { key: 10, text: 'Paragraph', value: 'paragraph' },
    { key: 11, text: 'Bank id collect', value: 'bankid_collect' }
];

export const routesList = [
    {
        text: 'Assets',
        route: '/assets',
        type: 'ASSETS'
    },
    {
        text: 'Members overview',
        route: '/members',
        type: 'CHATS'
    },
    {
        text: 'Questions',
        route: '',
        type: 'QUESTIONS'
    },
    {
        text: 'Claims',
        route: '/claims',
        type: 'CLAIMS'
    }
];

export const claimStatus = [
    { key: 1, text: 'Open', value: 'OPEN' },
    { key: 2, text: 'Closed', value: 'CLOSED' },
    { key: 3, text: 'Reopened', value: 'REOPENED' }
];

export const userStatus = [
    { key: 0, text: 'ACTIVE', value: 'ACTIVE' },
    { key: 5, text: 'INACTIVE', value: 'INACTIVE' },
    { key: 6, text: 'ALL', value: 'ALL' }
];
