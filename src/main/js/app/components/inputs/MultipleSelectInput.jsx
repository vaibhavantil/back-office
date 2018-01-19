import React from 'react';
import { Form, Dropdown, Input } from 'semantic-ui-react';
import { TEXT, MULTIPLE_SELECT } from 'app/lib/messageTypes';

const MultipleSelectInput = ({ changeHandler, options }) => (
    <React.Fragment>
        <Form.Field>
            <label>Text</label>
            <Input fluid onChange={changeHandler.bind(this, TEXT)} />
        </Form.Field>
        <Form.Field>
            <label>Select</label>
            <Dropdown
                placeholder="State"
                multiple
                search
                selection
                options={options}
                onChange={changeHandler.bind(this, MULTIPLE_SELECT)}
            />
        </Form.Field>
    </React.Fragment>
);

export default MultipleSelectInput;
