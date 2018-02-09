import {
    NOTES_REQUESTING,
    NOTES_REQUEST_SUCCESS,
    CREATE_NOTE_REQUESTING,
    CREATE_NOTE_SUCCESS,
    REMOVE_NOTE_REQUESTING,
    REMOVE_NOTE_SUCCESS
} from 'constants/claims';

export const notesRequest = id => ({
    type: NOTES_REQUESTING,
    id
});

export const notesRequestSuccess = notes => ({
    type: NOTES_REQUEST_SUCCESS,
    notes
});

export const createNote = (id, data) => ({
    type: CREATE_NOTE_REQUESTING,
    id,
    data
});

export const createNoteSuccess = note => ({
    type: CREATE_NOTE_SUCCESS,
    note
});

export const removeNote = (claimId, noteId) => ({
    type: REMOVE_NOTE_REQUESTING,
    claimId,
    noteId
});

export const removeNoteSuccess = noteId => ({
    type: REMOVE_NOTE_SUCCESS,
    noteId
});
