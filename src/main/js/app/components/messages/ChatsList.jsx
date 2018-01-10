import React from 'react';
import filter from 'lodash.filter';
import escapeRegExp from 'lodash.escaperegexp';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { List, Input } from 'semantic-ui-react';

const ListContainer = styled.div`
    max-width: 500px;
    border: solid 1px #dfe0e0;
    border-radius: 5px;
    padding: 20px 10px;
`;

const Chats = ({ chats }) => (
    <ListContainer>
        <List celled selection verticalAlign="middle" size="big">
            {chats.map(item => (
                <List.Item key={item.id}>
                    <Link to={`/messages/${item.id}`} replace>
                        <List.Content>
                            <List.Header>{item.name}</List.Header>
                        </List.Content>
                    </Link>
                </List.Item>
            ))}
        </List>
    </ListContainer>
);

export default class ChatsList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            results: [],
            value: ''
        };
        this.handleSearchChange = this.handleSearchChange.bind(this);
        this.resetSearchState = this.resetSearchState.bind(this);
    }

    resetSearchState() {
        this.setState({
            isLoading: false,
            results: this.props.chats,
            value: ''
        });
    }

    handleSearchChange(e) {
        this.setState({ isLoading: true, value: e.target.value });
        // eslint-disable-next-line no-undef
        setTimeout(() => {
            if (!this.state.value) return this.resetSearchState();

            const regexp = new RegExp(escapeRegExp(this.state.value), 'i');
            const isMatch = result => regexp.test(result.name);
            this.setState({
                isLoading: false,
                results: filter(this.props.chats, isMatch)
            });
        }, 500);
    }

    componentWillReceiveProps(newProps) {
        if (newProps.chats.length) {
            this.setState({
                results: newProps.chats
            });
        }
    }

    render() {
        return (
            <div>
                <Input
                    loading={this.state.isLoading}
                    placeholder="Search..."
                    iconPosition="left"
                    onChange={this.handleSearchChange}
                />
                <Chats chats={this.state.results} />
            </div>
        );
    }
}
