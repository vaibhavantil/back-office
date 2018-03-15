import React from 'react';
import PropTypes from 'prop-types';
import { Menu } from 'semantic-ui-react';

const menuItems = ['Dashboard', 'Assets', 'Members', 'Questions', 'Claims'];

export default class HorizontalMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeItem: null
        };
    }

    itemClickHander = (e, { name }) => {
        this.setState({ activeItem: name });
        this.props.history.push(`/${name.toLowerCase()}`);
    };

    render() {
        return (
            <Menu stackable>
                {menuItems.map((item, id) => (
                    <Menu.Item
                        key={id}
                        name={item}
                        onClick={this.itemClickHander}
                        active={this.state.activeItem === item}
                    >
                        {item}
                    </Menu.Item>
                ))}
            </Menu>
        );
    }
}

HorizontalMenu.propTypes = {
    history: PropTypes.object.isRequired
};
