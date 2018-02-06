import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import NewNote from './NewNote';
import NotesList from './NotesList';

const NotesContainer = styled.div`
    display: flex;
    flex-direction: column;
    margin: 100px;
    padding: 30px;
    border: solid 1px #ccc;
`;

const Notes = ({ createNote, removeNote, notes, id }) => (
    <NotesContainer>
        <NewNote createNote={createNote} id={id} />
        <NotesList notes={notes} removeNote={removeNote} claimId={id} />
    </NotesContainer>
);

Notes.propTypes = {
    createNote: PropTypes.func.isRequired,
    removeNote: PropTypes.func.isRequired,
    notes: PropTypes.array.isRequired,
    id: PropTypes.string.isRequired
};

export default Notes;
