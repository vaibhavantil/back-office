import { POLL_START, POLL_STOP } from 'constants';

export const pollStart = (duration) => ({
    type: POLL_START,
    duration
});

export const pollStop = () => ({ type: POLL_STOP });
