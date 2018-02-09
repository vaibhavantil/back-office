import React from 'react';
import PropTypes from 'prop-types';
import { Segment } from 'semantic-ui-react';
import NewNote from './NewNote';
import NotesList from './NotesList';

const Notes = ({ createNote, removeNote, notes, id }) => (
    <Segment>
        <NewNote createNote={createNote} id={id} />
        <NotesList notes={notes} removeNote={removeNote} claimId={id} />
    </Segment>
);

Notes.propTypes = {
    createNote: PropTypes.func.isRequired,
    removeNote: PropTypes.func.isRequired,
    notes: PropTypes.array.isRequired,
    id: PropTypes.string.isRequired
};

export default Notes;
