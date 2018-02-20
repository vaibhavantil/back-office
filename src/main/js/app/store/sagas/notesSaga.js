import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import { CREATE_NOTE_REQUESTING, NOTES_REQUESTING } from '../constants/claims';
import * as actions from '../actions/notesActions';
import { claimRequestError } from '../actions/claimDetailsActions';

function* createNoteFlow({ data, id }) {
    try {
        const token = yield call(getAuthToken);
        const note = yield call(
            api,
            token,
            config.claims.details.create,
            data,
            `${id}/notes`
        );
        yield put(actions.createNoteSuccess(note.data));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* notesRequestFlow({ id }) {
    try {
        const token = yield call(getAuthToken);
        const notes = yield call(
            api,
            token,
            config.claims.details.get,
            null,
            `${id}/notes`
        );
        yield put(actions.notesRequestSuccess(notes));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* watcher() {
    yield [
        takeLatest(CREATE_NOTE_REQUESTING, createNoteFlow),
        takeLatest(NOTES_REQUESTING, notesRequestFlow)
    ];
}

export default watcher;
