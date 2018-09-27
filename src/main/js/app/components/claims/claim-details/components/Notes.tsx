import * as React from 'react'
import { Formik, Form, Field, FieldProps } from 'formik'
import { List, ListItem, CardContent, TextField, Button } from '@material-ui/core';
import { CustomCard } from './Styles';

interface Note {
  text: string,
}

interface Props {
  notes: Array<Note>
  addClaimNote: (note: Note) => void
}

const TextArea: React.SFC<FieldProps<HTMLTextAreaElement>> = ({ field: { onChange, onBlur, name, value } }) => (
  <TextField
    onChange={onChange}
    onBlur={onBlur}
    name={name}
    value={value || ''}
    multiline
    fullWidth
    placeholder="Type note here..."
  />
)

const Notes: React.SFC<Props> = ({ notes, addClaimNote }) => (
  <CustomCard>
    <CardContent>
      <h3>Notes</h3>
      <List>
        {notes.map(note => (
          <ListItem key={note.text}>
            <p>{note.text}</p>
          </ListItem>
        ))}
      </List>
      <Formik
        initialValues={{}}
        onSubmit={(values, { setSubmitting, resetForm }) => { addClaimNote({ text: values.text }); setSubmitting(false); resetForm() }}
      >
        <Form>
          <Field component={TextArea} placeholder="Type note content here" name="text" />
          <Button type="submit" variant="contained" color="primary">Add note</Button>
        </Form>
      </Formik>
    </CardContent>
  </CustomCard>
)

export { Notes }
