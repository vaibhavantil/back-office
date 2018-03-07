import React from 'react';
import PropTypes from 'prop-types';
import { List, Checkbox } from 'semantic-ui-react';
import * as types from 'app/lib/messageTypes';

const SelectList = ({ content }) => {
    const list = content.choices.map((item, id) => {
        if (item.type === 'link') {
            const link = item.appUrl || item.webUrl || item.view;
            return (
                <List.Item key={id}>
                    <List.Content>
                        {
                            content.type === types.MULTIPLE_SELECT
                                ? <Checkbox readOnly checked={item.selected}
                                            label={
                                                <label>
                                                    <a href={link}>{item.text}</a>
                                                </label>} />
                                : <Checkbox radio readOnly checked={item.selected}
                                            label={
                                                <label>
                                                    <a href={link}>{item.text}</a>
                                                </label>} />
                        }
                    </List.Content>
                </List.Item>
            );
        } else {
            return (
                <List.Item key={id} selected={item.selected}>
                    <List.Content>
                        {
                            content.type === types.MULTIPLE_SELECT
                                ? <Checkbox readOnly checked={item.selected} label={item.text} />
                                : <Checkbox radio readOnly checked={item.selected} label={item.text} />
                        }
                    </List.Content>
                </List.Item>
            );
        }
    });

    return (
        <React.Fragment>
            <List>{list}</List>
        </React.Fragment>
    );
};

export default SelectList;

SelectList.propTypes = {
    content: PropTypes.object.isRequired
};
