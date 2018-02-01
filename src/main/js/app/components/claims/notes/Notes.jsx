import React from 'react';
import styled from 'styled-components';
import NewNote from './NewNote';
import NotesList from './NotesList';

const NotesContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 500px;
    margin: 100px;
`;

const Notes = ({ addNote, removeNote, notes }) => (
    <NotesContainer>
        <NewNote addNote={addNote} />
        <NotesList notes={notes} removeNote={removeNote} />
    </NotesContainer>
);

export default Notes;
