import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import {
    CLAIM_REQUESTING,
    CREATE_NOTE_REQUESTING,
    NOTES_REQUESTING,
    REMOVE_NOTE_REQUESTING
} from '../constants/claims';
import * as actions from '../actions/claimDetailsActions';

function* requestFlow({ id }) {
    try {
        const token = yield call(getAuthToken);
        const claim = yield call(api, token, config.claims.getList, null, id);
        yield put(actions.claimRequestSuccess(claim));
    } catch (error) {
        yield put(actions.claimRequestError(error));
    }
}

function* removeNoteFlow({ claimId, noteId }) {
    try {
        const token = yield call(getAuthToken);
        const path = `${claimId}/notes/${noteId}`;
        yield call(api, token, config.claims.details.remove, null, path);
        yield put(actions.removeNoteSuccess(noteId));
    } catch (error) {
        yield put(actions.claimRequestError(error));
    }
}

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
        yield put(actions.claimRequestError(error));
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
        yield put(actions.claimRequestError(error));
    }
}

function* watcher() {
    yield [
        takeLatest(CLAIM_REQUESTING, requestFlow),
        takeLatest(CREATE_NOTE_REQUESTING, createNoteFlow),
        takeLatest(NOTES_REQUESTING, notesRequestFlow),
        takeLatest(REMOVE_NOTE_REQUESTING, removeNoteFlow)
    ];
}

export default watcher;
