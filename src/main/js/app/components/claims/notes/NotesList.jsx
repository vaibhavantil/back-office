import React from 'react';
import PropTypes from 'prop-types';
import { List } from 'semantic-ui-react';

const NotesList = ({ notes }) => (
    <List selection>
        {notes &&
            notes.map((note, id) => (
                <List.Item key={note.id || id}>
                    <List.Content>{note.text}</List.Content>
                    {note.file && (
                        <a href={note.file} target="_blank">
                            file
                        </a>
                    )}
                </List.Item>
            ))}
    </List>
);

NotesList.propTypes = {
    notes: PropTypes.array
};

export default NotesList;
