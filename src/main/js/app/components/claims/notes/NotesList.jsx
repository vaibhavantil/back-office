import React from 'react';
import PropTypes from 'prop-types';
import { Icon, List } from 'semantic-ui-react';

const NotesList = ({ notes, removeNote, claimId }) => (
    <List selection>
        {notes &&
            notes.map((note, id) => (
                <List.Item key={note.id || id}>
                    <List.Content floated="right" verticalAlign="middle">
                        <Icon
                            name="close"
                            onClick={removeNote.bind(this, claimId, note.id)}
                        />
                    </List.Content>
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
    notes: PropTypes.array,
    removeNote: PropTypes.func.isRequired,
    claimId: PropTypes.string.isRequired
};

export default NotesList;
