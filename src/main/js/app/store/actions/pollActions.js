import { POLL_START, POLL_STOP } from 'constants';

export const pollStart = (client, duration) => ({
    type: POLL_START,
    client,
    duration
});

export const pollStop = () => ({ type: POLL_STOP });
