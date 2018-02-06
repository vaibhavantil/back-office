import React from 'react';
import PropTypes from 'prop-types';
import { List } from 'semantic-ui-react';

const SelectList = ({ content }) => {
    const list = content.choices.map((item, id) => {
        if (item.type === 'link') {
            const link = item.appUrl || item.webUrl || item.view;
            return (
                <List.Item key={id} selected={item.selected}>
                    {item.selected && <List.Icon name="check" />}
                    <List.Content>
                        <a href={link}>{item.text}</a>
                    </List.Content>
                </List.Item>
            );
        } else {
            return (
                <List.Item key={id} selected={item.selected}>
                    {item.selected && <List.Icon name="check" />}
                    <List.Content>{item.text}</List.Content>
                </List.Item>
            );
        }
    });

    return (
        <React.Fragment>
            <h4>{content.type}</h4>
            <List>{list}</List>
        </React.Fragment>
    );
};

export default SelectList;

SelectList.propTypes = {
    content: PropTypes.object.isRequired
};
