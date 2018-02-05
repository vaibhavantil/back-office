import React from 'react';
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

export default Notes;
